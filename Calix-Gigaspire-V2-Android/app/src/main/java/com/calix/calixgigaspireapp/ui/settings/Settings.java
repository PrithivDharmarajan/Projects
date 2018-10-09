package com.calix.calixgigaspireapp.ui.settings;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.BuildConfig;
import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdEntity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdResponse;
import com.calix.calixgigaspireapp.output.model.CommonModuleResponse;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.InstallAlexaResponse;
import com.calix.calixgigaspireapp.output.model.RouterAgentListResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.loginregconfig.Login;
import com.calix.calixgigaspireapp.ui.loginregconfig.RouterConfiguration;
import com.calix.calixgigaspireapp.ui.loginregconfig.RouterDetailsUpdate;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends BaseActivity implements CompoundButton.OnCheckedChangeListener, TextToSpeech.OnInitListener, RecognitionListener {

    @BindView(R.id.settings_parent_lay)
    ViewGroup mSettingsViewGroup;

    @BindView(R.id.settings_header_bg_lay)
    RelativeLayout mSettingsHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.security_lay)
    LinearLayout mSecurityLay;

    @BindView(R.id.enable_pass_code_switch_compat)
    SwitchButton mEnablePassCodeSwitchCompat;

    @BindView(R.id.enable_touch_id_switch_compat)
    SwitchButton mEnableTouchIdSwitchCompat;

    @BindView(R.id.set_reset_pin_pass_code_txt)
    TextView mSetResetPinPassCodeTxt;

    @BindView(R.id.enable_touch_id_lay)
    RelativeLayout mEnableTouchIdLay;

    @BindView(R.id.router_status_txt)
    TextView mRouterStatusTxt;

    @BindView(R.id.app_version_txt)
    TextView mAppVersionTxt;

    private boolean mIsRouterAlreadyOnBoardedBool = false;
    private String mRouterIdStr = "", mUninstallAppNameStr = "", mUninstallAgentIdStr = "";
    private SpeechRecognizer mSpeechRecognizer = null;
    private Intent mSpeechRecognizerIntent;
    private TextToSpeech mTextToSpeech;
    private Dialog mVoiceDialog;
    private ImageView mMicImg;
    private TextView mSpeechPromptTxt, mSpeechPromptStatusTxt;
    private int mTapToDevModeInt = 1;
    private Handler mHandler;
    private Runnable mRunnable;
    private Dialog mAgentDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_settings);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mSettingsViewGroup);

        setHeaderView();

        mEnablePassCodeSwitchCompat.setOnCheckedChangeListener(this);
        mEnableTouchIdSwitchCompat.setOnCheckedChangeListener(this);
        mSecurityLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS) ? View.VISIBLE : View.GONE);
        mEnablePassCodeSwitchCompat.setChecked(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.PASS_CODE_ENABLE_STATUS));
        mEnableTouchIdSwitchCompat.setChecked(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.PASS_CODE_ENABLE_STATUS) && PreferenceUtil.getBoolPreferenceValue(this, AppConstants.TOUCH_ID_ENABLE_STATUS));
        mSetResetPinPassCodeTxt.setText(getString(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_PIN_PWD_STATUS) ? R.string.reset_pin_passcode : R.string.set_pin_passcode));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            mEnableTouchIdLay.setVisibility(fingerprintManager != null && fingerprintManager.isHardwareDetected() ? View.VISIBLE : View.GONE);
        } else {
            mEnableTouchIdLay.setVisibility(View.GONE);
        }

        mAppVersionTxt.setText(BuildConfig.VERSION_NAME);
        mTextToSpeech = new TextToSpeech(this, this);

        /*Router API call*/
        routerMapAPICall();
    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.settings));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSettingsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mSettingsHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Settings.this)));
                    mSettingsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Settings.this), 0, 0);
                }
            });
        }

    }

    /*routerMapAPICall calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*Install APICall calls*/
    private void installAlexaAPICall() {
        APIRequestHandler.getInstance().installAlexaAppIdAPICall(PreferenceUtil.getUserId(Settings.this), mRouterIdStr, this);
    }

    /*Agent APICall calls*/
    private void agentListAPICall() {
        APIRequestHandler.getInstance().routerAgentListAPICall(this);
    }

    /*Agent APICall calls*/
    private void findAgentAppIdAPICall() {
        APIRequestHandler.getInstance().alexaAppIdsAPICall(this);
    }

    /*Agent APICall calls*/
    private void uninstallRouterAgentListAPICall() {
        APIRequestHandler.getInstance().uninstallRouterAgentAPICall(mUninstallAgentIdStr, mRouterIdStr, this);
    }

    /*Remove Router APICall calls*/
    private void removeRouterAPICall() {
        APIRequestHandler.getInstance().removeRouterAPICall(this);
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.set_reset_pin_pass_code_lay, R.id.changes_login_pwd_lay,
            R.id.about_lay, R.id.terms_conditions_lay, R.id.on_board_update_ssid_lay,
            R.id.install_alexa_lay, R.id.things_to_try_lay, R.id.calix_iot_agent_lay,
            R.id.uninstall_alexa_lay, R.id.remove_router_lay, R.id.developer_mode_lay
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.set_reset_pin_pass_code_lay:
                nextScreen(SetPinAndResetPin.class);
                break;
            case R.id.changes_login_pwd_lay:
                nextScreen(ChangeLoginPwd.class);
                break;
            case R.id.about_lay:
                AppConstants.ABOUT_TERMS_HEADER_TEXT = getString(R.string.about);
                nextScreen(AboutTermsConditions.class);
                break;
            case R.id.terms_conditions_lay:
                AppConstants.ABOUT_TERMS_HEADER_TEXT = getString(R.string.terms_conditions);
                nextScreen(AboutTermsConditions.class);
                break;
            case R.id.on_board_update_ssid_lay:
                AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS = true;
                AppConstants.ROUTER_ON_BOARD_FROM_WELCOME = false;
                AppConstants.ROUTER_DETAILS_ENTITY = new RouterMapEntity();
                nextScreen(mIsRouterAlreadyOnBoardedBool ? RouterDetailsUpdate.class : RouterConfiguration.class);
                break;
            case R.id.install_alexa_lay:
                if (mRouterIdStr.isEmpty())
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router), this);
                else if (askPermissions())
                    promptSpeechInput();
                break;
            case R.id.things_to_try_lay:
                final String alexaAppPackageName = AppConstants.ALEXA_APP;
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(alexaAppPackageName);
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + alexaAppPackageName)));
                    }
                } catch (android.content.ActivityNotFoundException an) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + alexaAppPackageName)));
                }
                break;
            case R.id.calix_iot_agent_lay:
                nextScreen(CalixIOTAgent.class);
                break;
            case R.id.uninstall_alexa_lay:
                if (mRouterIdStr.isEmpty())
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router), this);
                else {
                    agentListAPICall();
                }
                break;
            case R.id.remove_router_lay:
                if (mRouterIdStr.isEmpty())
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router), this);
                else {
                    DialogManager.getInstance().showOptionPopup(this, getString(R.string.are_remove_router), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            removeRouterAPICall();
                        }

                        @Override
                        public void onNegativeClick() {

                        }
                    });
                }
                break;
            case R.id.developer_mode_lay:
                if (PreferenceUtil.getBoolPreferenceValue(this, AppConstants.DEVELOPER_MODE_ENABLE_STATUS)) {
                    developmentModeDialog();
                } else {
                    if (mTapToDevModeInt >= 10) {
                        PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.DEVELOPER_MODE_ENABLE_STATUS, true);
                        developmentModeDialog();
                    }

                    mTapToDevModeInt += 1;
                    if (mHandler != null && mRunnable != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                    mHandler = new Handler();
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            mTapToDevModeInt = 0;
                        }
                    };
                    mHandler.postDelayed(mRunnable, 2000);
                    break;
                }


        }
    }

    private void developmentModeDialog() {
        DialogManager.getInstance().showDevelopmentPopup(this, new InterfaceTwoBtnCallback() {
            @Override
            public void onNegativeClick() {

            }

            @Override
            public void onPositiveClick() {
                PreferenceUtil.storeBoolPreferenceValue(Settings.this, AppConstants.LOGIN_STATUS, false);
                AppConstants.BASE_URL = PreferenceUtil.getBaseURL(Settings.this);
                APIRequestHandler.getInstance().refreshBaseUrl();
                previousScreen(Login.class);
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(Settings.this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        showVoicePopup(this);
    }

    /*View onCheckedChanged*/
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.enable_pass_code_switch_compat:
                if (PreferenceUtil.getBoolPreferenceValue(Settings.this, AppConstants.LOGIN_PIN_PWD_STATUS)) {
                    mEnablePassCodeSwitchCompat.setBackColor(ContextCompat.getColorStateList(Settings.this, isChecked ? R.color.blue : R.color.deep_gray));
                    PreferenceUtil.storeBoolPreferenceValue(Settings.this, AppConstants.PASS_CODE_ENABLE_STATUS, isChecked);
                    if (!isChecked) {
                        mEnableTouchIdSwitchCompat.setOnCheckedChangeListener(null);
                        mEnableTouchIdSwitchCompat.setChecked(false);
                        mEnableTouchIdSwitchCompat.setBackColor(ContextCompat.getColorStateList(Settings.this, R.color.deep_gray));
                        mEnableTouchIdSwitchCompat.setOnCheckedChangeListener(this);
                    }
                } else {
                    mEnablePassCodeSwitchCompat.setChecked(false);
                    mEnablePassCodeSwitchCompat.setBackColor(ContextCompat.getColorStateList(Settings.this, R.color.deep_gray));

                    DialogManager.getInstance().showAlertPopup(Settings.this, getString(R.string.please_first_set_pin), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {
                        }

                        @Override
                        public void onPositiveClick() {
                        }
                    });
                }
                break;
            case R.id.enable_touch_id_switch_compat:
                if (PreferenceUtil.getBoolPreferenceValue(Settings.this, AppConstants.PASS_CODE_ENABLE_STATUS)) {
                    PreferenceUtil.storeBoolPreferenceValue(Settings.this, AppConstants.TOUCH_ID_ENABLE_STATUS, isChecked);
                    mEnableTouchIdSwitchCompat.setBackColor(ContextCompat.getColorStateList(Settings.this, isChecked ? R.color.blue : R.color.deep_gray));
                } else {
                    mEnableTouchIdSwitchCompat.setChecked(false);
                    mEnableTouchIdSwitchCompat.setBackColor(ContextCompat.getColorStateList(Settings.this, R.color.deep_gray));

                    DialogManager.getInstance().showAlertPopup(Settings.this, getString(R.string.please_first_enable_pin), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {

                        }

                        @Override
                        public void onPositiveClick() {

                        }
                    });
                }
                break;

        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            mIsRouterAlreadyOnBoardedBool = routerMapResponse.getRouters().size() > 0;
            mRouterIdStr = mIsRouterAlreadyOnBoardedBool && !routerMapResponse.getRouters().get(0).getRouterId().isEmpty() ? routerMapResponse.getRouters().get(0).getRouterId() : "";
            mRouterStatusTxt.setText(getString(mIsRouterAlreadyOnBoardedBool ? R.string.update_ssid : R.string.on_board_router));
        } else if (resObj instanceof InstallAlexaResponse) {
            InstallAlexaResponse installAlexaResponse = (InstallAlexaResponse) resObj;
            mTextToSpeech.speak(installAlexaResponse.getSpeech(), TextToSpeech.QUEUE_FLUSH, null);
        } else if (resObj instanceof RouterAgentListResponse) {
            RouterAgentListResponse alexaAppIdResponse = (RouterAgentListResponse) resObj;
            if (alexaAppIdResponse.getApps().size() > 0) {
                mAgentDialog = DialogManager.getInstance().showAgentListPopup(this, alexaAppIdResponse.getApps(), new InterfaceEdtBtnCallback() {
                    @Override
                    public void onPositiveClick(String uninstallAppNameStr) {
                        alertDismiss(mAgentDialog);
                        mUninstallAppNameStr = uninstallAppNameStr;
                        findAgentAppIdAPICall();
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
            } else {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.agent_not_found), this);
            }
        } else if (resObj instanceof AlexaAppIdResponse) {
            AlexaAppIdResponse alexaAppIdResponse = (AlexaAppIdResponse) resObj;
            if (alexaAppIdResponse.getApps().size() > 0) {
                ArrayList<AlexaAppIdEntity> agentListArrList = alexaAppIdResponse.getApps();
                for (int posInt = 0; posInt < agentListArrList.size(); posInt++) {
                    if (mUninstallAppNameStr.equalsIgnoreCase(agentListArrList.get(posInt).getName())) {
                        mUninstallAgentIdStr = agentListArrList.get(posInt).getId();
                        break;
                    }
                }

                if (mUninstallAgentIdStr.isEmpty()) {
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.agent_not_found), this);
                } else {
                    uninstallRouterAgentListAPICall();
                }

            } else {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.agent_not_found), this);
            }
        } else if (resObj instanceof CommonResponse) {

            DialogManager.getInstance().showAlertPopup(this, getString(R.string.uninstall_success), this);
        }else if (resObj instanceof CommonModuleResponse) {
            routerMapAPICall();
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof RouterMapResponse) {
                                routerMapAPICall();
                            } else if (resObj instanceof InstallAlexaResponse) {
                                installAlexaAPICall();
                            } else if (resObj instanceof RouterAgentListResponse) {
                                agentListAPICall();
                            } else if (resObj instanceof AlexaAppIdResponse) {
                                findAgentAppIdAPICall();
                            } else if (resObj instanceof CommonResponse) {
                                uninstallRouterAgentListAPICall();
                            } else if (resObj instanceof CommonModuleResponse) {
                                removeRouterAPICall();
                            }
                        }
                    });
        }
    }

    /*Text to Voice*/
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = mTextToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                DialogManager.getInstance().showToast(Settings.this, getString(R.string.language_not_supported));
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    public void showVoicePopup(final Context context) {

        alertDismiss(mVoiceDialog);

        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        mVoiceDialog = new Dialog(context);
        mVoiceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mVoiceDialog.getWindow() != null) {
            mVoiceDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mVoiceDialog.setContentView(R.layout.popup_voice_recon_alert);
            mVoiceDialog.getWindow().setGravity(Gravity.CENTER);
            mVoiceDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        mVoiceDialog.setCancelable(true);
        mVoiceDialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mVoiceDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }


        /*Init view*/
        mSpeechPromptTxt = mVoiceDialog.findViewById(R.id.speech_prompt_txt);
        mMicImg = mVoiceDialog.findViewById(R.id.mic_img);
        mSpeechPromptStatusTxt = mVoiceDialog.findViewById(R.id.speech_prompt_status_txt);

        mMicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSpeechPromptStopBool = mSpeechPromptStatusTxt.getText().toString().trim().equalsIgnoreCase(getString(R.string.start));
                mMicImg.setImageResource(isSpeechPromptStopBool ? R.drawable.mic_enable : R.drawable.mic_disable);
                mSpeechPromptTxt.setText(getString(isSpeechPromptStopBool ? R.string.speech_prompt : R.string.speak_try_again));
                mSpeechPromptStatusTxt.setText(getString(isSpeechPromptStopBool ? R.string.stop : R.string.start));
                if (isSpeechPromptStopBool)
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                else
                    mSpeechRecognizer.cancel();
            }
        });

        mVoiceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mSpeechRecognizer.cancel();
            }
        });

        mVoiceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mSpeechRecognizer.cancel();
            }
        });

        alertShowing(mVoiceDialog);

    }

    private void setTextAndImgStatus() {
        if (mMicImg != null)
            mMicImg.setImageResource(R.drawable.mic_disable);

        if (mSpeechPromptTxt != null)
            mSpeechPromptTxt.setText(getString(R.string.speak_try_again));

        if (mSpeechPromptStatusTxt != null)
            mSpeechPromptStatusTxt.setText(getString(R.string.start));

    }

    /*Speech Listener*/
    @Override
    public void onReadyForSpeech(Bundle params) {

        sysOut("onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {

        sysOut("onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        sysOut("rmsdB");

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        sysOut("onBufferReceived");

    }

    @Override
    public void onEndOfSpeech() {
        setTextAndImgStatus();
        sysOut("onEndOfSpeech");

    }

    @Override
    public void onError(int error) {
        setTextAndImgStatus();
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null) {
            StringBuilder text = new StringBuilder();
            for (String result : matches)
                text.append(result).append("\n");

            if (text.toString().toLowerCase().trim().contains(getString(R.string.install_alexa_low)) || text.toString().toLowerCase().trim().contains(getString(R.string.alexa_install_low))) {
                installAlexaAPICall();
                alertDismiss(mVoiceDialog);
            }


        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            int recordAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if (recordAudioPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }


        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    promptSpeechInput();
                }

                public void onNegativeClick() {

                }
            });
        }

        return addPermission;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }

}