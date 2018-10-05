package com.bridgellc.bridge.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APICommonInterface;
import com.bridgellc.bridge.model.NotificationCountResponse;
import com.bridgellc.bridge.ui.DashboardScreen;
import com.bridgellc.bridge.ui.ForgotPwdScreen;
import com.bridgellc.bridge.ui.NegotiationChatRoom;
import com.bridgellc.bridge.ui.NotificationScreen;
import com.bridgellc.bridge.ui.PhOTPEmailScreen;
import com.bridgellc.bridge.ui.ReferalScreen;
import com.bridgellc.bridge.ui.SettingsScreen;
import com.bridgellc.bridge.ui.SignInScreen;
import com.bridgellc.bridge.ui.SignUpScreen;
import com.bridgellc.bridge.ui.SplashScreen;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.ui.home.HomeScreenFilterDialog;
import com.bridgellc.bridge.ui.home.SearchScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

import java.io.IOException;
import java.net.ConnectException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

@SuppressLint("Registered")
public class BaseFragmentActivity extends FragmentActivity {


    public ViewGroup mViewGroup;
    public RelativeLayout mHeadeLeftBtnLay, mHeadeRightBtnLay;
    public TextView mHeaderTxt;
    public SlidingDrawer slidingDrawer;
    public Typeface mLightFont, mRegulartFont;
    private static int handleHgt, handleWit;
    public static String headerText;
    long timeInterval = 10000;
    public static Handler notifyHandler = new Handler();
    public static TextView mHomeFTxt, mSearchFTxt, mProfileFTxt, mNotificationFTxt, mNotifyLableFTxt, mRefproTxt, mSettingsFTxt;
    public static boolean isMenuOpenBaseFrgAct = false;
    private static ImageView mImgCloseBaseFrgAct;

    public static boolean isIncBaseFrgAct = false;
    public static Handler mHandlerBaseFrgAct = new Handler();
    public static Runnable mRunBaseFrgAct;
    private static int _indexNotifyBaseFrgAct = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BaseActivity.mActivity = this;
        mLightFont = TypefaceSingleton.getTypeface().getLightFont(BaseActivity.mActivity);
        mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(BaseActivity.mActivity);
        handelerClear();
    }

    private void handelerClear() {
        if (BaseActivity.mActivity instanceof SplashScreen || BaseActivity.mActivity instanceof SignInScreen || BaseActivity.mActivity instanceof ForgotPwdScreen || BaseActivity.mActivity instanceof PhOTPEmailScreen || BaseActivity.mActivity instanceof SignUpScreen || BaseActivity.mActivity
                instanceof NegotiationChatRoom) {
            if (mHandlerBaseFrgAct != null) {
                mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
            }
            if (notifyHandler != null) {
                notifyHandler.removeCallbacks(notifyCheckingService);
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseActivity.mActivity = this;
        setOnClickLisMenu();
        BridgeApplication.activityResumed();
        if (mHandlerBaseFrgAct != null) {
            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
        }
        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
        notifyHandler.postDelayed(notifyCheckingService, 0);
        BaseActivity.mActivity.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        BaseActivity.mActivity.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mHandlerBaseFrgAct != null) {
            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
        }
        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
        BridgeApplication.activityStoped();
    }

    public void addFragment(Fragment fragment, int position) {

    }

    protected Runnable notifyCheckingService = new Runnable() {
        @Override
        public void run() {
            if (BaseActivity.mActivity instanceof BaseFragmentActivity && mNotifyLableFTxt != null) {
                if (GlobalMethods.isLoggedIn(BaseActivity.mActivity) && mSearchFTxt != null) {
                    getNotificationCountResponse(BaseActivity.mActivity);
                    notifyHandler.postDelayed(this, timeInterval);
                }
            }
        }
    };

    public void setOnClickLisMenu() {

        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);

        handleWit = (int) getResources().getDimension(R.dimen.size90);
        headerText = getString(R.string.home_c);

        if (slidingDrawer != null) {

            final TextView tv = (TextView) findViewById(R.id.header_txt);

            mImgCloseBaseFrgAct = (ImageView) findViewById(R.id.close_img);

            if (mImgCloseBaseFrgAct != null) {
                mImgCloseBaseFrgAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (slidingDrawer.isOpened()) {
                            slidingDrawer.animateClose();
                        } else {
//                            if (!(HomeScreenActivity.isFilterShown)) {
                            slidingDrawer.animateOpen();
//                            }

                        }
                    }
                });
            }

            slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                @Override
                public void onDrawerOpened() {
                    isMenuOpenBaseFrgAct = true;
                    handleHgt = (int) getResources().getDimension(R.dimen.height110);
                    slidingDrawer.getHandle().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, handleHgt));
                    slidingDrawer.getHandle().setBackgroundColor(Color.parseColor("#04384C"));
                    slidingDrawer.getHandle().setAlpha((float) 0.9);
                    if (tv != null) {
                        headerText = tv.getText().toString().trim();
                        tv.setText(getString(R.string.menu));
                    }
                    if (mHandlerBaseFrgAct != null) {
                        mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
                    }
                    mImgCloseBaseFrgAct.setImageBitmap(BitmapFactory.decodeResource(BaseActivity.mActivity.getResources(), R.drawable.close_img));


                    if (HomeScreenActivity.isFilterShown) {
                        HomeScreenFilterDialog.closeFilterDialog();
                    }

                }
            });

            slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
                @Override
                public void onDrawerClosed() {
                    isMenuOpenBaseFrgAct = false;
                    handleHgt = (int) getResources().getDimension(R.dimen.height110);
                    slidingDrawer.getHandle().setLayoutParams(new RelativeLayout.LayoutParams(handleWit, handleHgt));
                    slidingDrawer.getHandle().setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    if (tv != null) {
                        tv.setText(headerText);
                    }

                    if (mNotifyLableFTxt.getVisibility() == View.VISIBLE) {
                        NotifyImgBaseFrgLoad();
                    } else {
                        if (mHandlerBaseFrgAct != null) {
                            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
                        }
                        mImgCloseBaseFrgAct.setImageBitmap(BitmapFactory.decodeResource(BaseActivity.mActivity.getResources(), R.drawable.down_arrow_darkb_img));

                    }

                }
            });


            mHomeFTxt = (TextView) findViewById(R.id.home);
            mSearchFTxt = (TextView) findViewById(R.id.search);
            mProfileFTxt = (TextView) findViewById(R.id.profile);
            mNotificationFTxt = (TextView) findViewById(R.id.notification);
            mNotifyLableFTxt = (TextView) findViewById(R.id.notify_lable);
            mRefproTxt = (TextView) findViewById(R.id.refpro_txt);
            mSettingsFTxt = (TextView) findViewById(R.id.settings);
            if (mHomeFTxt != null) {
                mHomeFTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(BaseActivity.mActivity instanceof HomeScreenActivity)) {
                            nextScreen(HomeScreenActivity.class, true);
                        }


                    }
                });
            }

            if (mSearchFTxt != null) {
                mSearchFTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(BaseActivity.mActivity instanceof SearchScreenActivity)) {
                            nextScreen(SearchScreenActivity.class, true);
                        }

                    }
                });
            }

            if (mProfileFTxt != null) {
                mProfileFTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(BaseActivity.mActivity instanceof DashboardScreen)) {
                            nextScreen(DashboardScreen.class, true);
                        }

                    }
                });
            }
            if (mNotificationFTxt != null) {
                mNotificationFTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(BaseActivity.mActivity instanceof NotificationScreen)) {
                            AppConstants.PAYPAL_NOTIFY = AppConstants.FAILURE_CODE;
                            nextScreen(NotificationScreen.class, true);
                        }

                    }
                });
            }
            if (mRefproTxt != null) {
                mRefproTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();

//                        GlobalMethods.shareTxt(BaseActivity.mActivity);
                        if (!(BaseActivity.mActivity instanceof ReferalScreen)) {
                            Intent referInt = new Intent(BaseActivity.mActivity, ReferalScreen.class);
                            BaseActivity.mActivity.startActivity(referInt);
                        }

                    }
                });
            }
            if (mSettingsFTxt != null) {
                mSettingsFTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(BaseActivity.mActivity instanceof SettingsScreen)) {
                            nextScreen(SettingsScreen.class, true);
                        }

                    }
                });
            }
        }
    }


    public void onRequestSuccess(Object responseObj) {

    }

    /**
     * Touch and Hide keyboard
     *
     * @param view
     */
    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(BaseActivity.mActivity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View mInnerView = ((ViewGroup) view).getChildAt(i);
                setupUI(mInnerView);
            }
        }

//        setOnClickLisMenu();
    }

    public void hideSoftKeyboard(Activity mActivity) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (mActivity.getCurrentFocus() != null
                        && mActivity.getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(mActivity
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Set Font
     *
     * @param mViewGroup
     * @param mTypeface
     */
    public void setFont(ViewGroup mViewGroup, Typeface mTypeface) {

        int mCount = mViewGroup.getChildCount();
        View mView;
        for (int i = 0; i < mCount; i++) {
            mView = mViewGroup.getChildAt(i);

            if (mView instanceof TextView || mView instanceof Button
                    || mView instanceof EditText/* etc. */) {
                ((TextView) mView).setTypeface(mTypeface);
            }
            if (mView instanceof ViewGroup) {
                setFont((ViewGroup) mView, mTypeface);
            }
        }

    }

    /**
     * Move to Next Screen
     *
     * @param clazz
     */

    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(BaseActivity.mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN) || clazz
                .getSimpleName().equalsIgnoreCase(AppConstants.SEARCH_SCREEN) || clazz.getSimpleName
                ().equalsIgnoreCase(AppConstants.DASHBOARD_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.NOTIFICATION_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.SETTINGS_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.SING_IN_SCREEN)) {

            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        hideSlidingDrawer();

        BaseActivity.mActivity.startActivity(mIntent);
        BaseActivity.mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            BaseActivity.mActivity.finish();
    }

    public void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(BaseActivity.mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN) || clazz
                .getSimpleName().equalsIgnoreCase(AppConstants.SEARCH_SCREEN) || clazz.getSimpleName
                ().equalsIgnoreCase(AppConstants.DASHBOARD_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.NOTIFICATION_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.SETTINGS_SCREEN)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        hideSlidingDrawer();

        BaseActivity.mActivity.startActivity(mIntent);
        BaseActivity.mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        if (finish)
            BaseActivity.mActivity.finish();
    }

    public void finishScreen() {
        hideSlidingDrawer();
        BaseActivity.mActivity.finish();

        BaseActivity.mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
    }

    public void hideSlidingDrawer() {
        if (slidingDrawer != null && slidingDrawer.isOpened()) {
            slidingDrawer.animateClose();
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = BaseActivity.mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }


    public void onRequestFailure(RetrofitError errorCode) {
//        if (errorCode.getCause() instanceof SocketTimeoutException) {
//            DialogManager.showBasicAlertDialog(BaseActivity.mActivity, getString(R.string.app_name),
//                    getString(R.string.connection_timeout), new DialogMangerOkCallback() {
//                        @Override
//                        public void onOkClick() {
//
//                        }
//                    });
//        } else {
//            DialogManager.showBasicAlertDialog(BaseActivity.mActivity, getString(R.string.app_name), getString(R.string.no_internet), new DialogMangerOkCallback() {
//                @Override
//                public void onOkClick() {
//
//                }
//            });
//        }
        try {

            System.out.println("errorCode.getCause() --------" + errorCode.getCause().toString());
            System.out.println("errorCode.getCause() Msg--------" + errorCode.getCause().getMessage());

        } catch (Exception e) {

        }
        if (errorCode.getCause() instanceof ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(BaseActivity.mActivity, getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(BaseActivity.mActivity,
                    getString(R.string.connect_time_out), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            DialogManager.showBasicAlertDialog(BaseActivity.mActivity, BaseActivity.mActivity.getString(R.string.serv_con_exce),
                    new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
        } else {
            DialogManager.showBasicAlertDialog(BaseActivity.mActivity,
                    getString(R.string.serv_not_res), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

        }
    }

    private static void getNotificationCountResponse(final Activity mActivity) {
//        DialogManager.showProgress(mActivity);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.BASE_URL).build();
        APICommonInterface mInterfaces = restAdapter
                .create(APICommonInterface.class);
        mInterfaces.getNotificationCountResponse(GlobalMethods.getUserID(mActivity),
                new Callback<NotificationCountResponse>() {

                    @Override
                    public void success(NotificationCountResponse mNotifyResponse,
                                        Response arg1) {
//                        DialogManager.hideProgress(mActivity);
                        if (mNotifyResponse != null) {

                            if (mNotifyResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                                if (mNotifyResponse.getResult().getCount() != null && !mNotifyResponse.getResult().getCount().equalsIgnoreCase("") && !mNotifyResponse.getResult().getCount().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                                    String notifiCount = mNotifyResponse.getResult().getCount();


//                                    GlobalMethods.storeValuetoPreference(mActivity,
//                                            GlobalMethods.STRING_PREFERENCE,
//                                            AppConstants.PAYMENT_DETAILS, mNotifyResponse.getResult().getPayment_details());
                                    if (mActivity instanceof BaseFragmentActivity && mNotifyLableFTxt != null) {
                                        mNotifyLableFTxt.setVisibility(View.VISIBLE);
                                        mNotifyLableFTxt.setText(notifiCount);
                                        GlobalMethods.setBadge(mActivity, Integer.valueOf
                                                (notifiCount));

                                    }

                                    if (!isMenuOpenBaseFrgAct) {
                                        NotifyImgBaseFrgLoad();
//                                        if (!is_FirstBaseFrgAct) {
//                                            NotifyImgBaseFrgLoad();
//                                            is_FirstBaseFrgAct = true;
//                                        }
                                    } else {
//                                        if (mHandlerBaseFrgAct != null) {
//                                            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
//                                        }
                                        mImgCloseBaseFrgAct.setImageBitmap(BitmapFactory.decodeResource(BaseActivity.mActivity.getResources(), R.drawable.close_img));


                                    }
                                } else {
                                    if (mActivity instanceof BaseFragmentActivity && mNotifyLableFTxt != null) {
                                        mNotifyLableFTxt.setVisibility(View.GONE);
                                        GlobalMethods.setBadge(mActivity, Integer.valueOf
                                                (AppConstants.FAILURE_CODE));
                                    }

                                    if (!isMenuOpenBaseFrgAct) {
                                        mImgCloseBaseFrgAct.setImageBitmap(BitmapFactory.decodeResource(BaseActivity.mActivity.getResources(), R.drawable.down_arrow_darkb_img));
                                    } else {
//                                        if (mHandlerBaseFrgAct != null) {
//                                            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
//                                        }
                                        mImgCloseBaseFrgAct.setImageBitmap(BitmapFactory.decodeResource(BaseActivity.mActivity.getResources(), R.drawable.close_img));
                                    }
                                }

                            } else {

//                                DialogManager.showBasicAlertDialog(mActivity, mActivity.getString(R.string.app_name), mNotifyResponse.getMessage(), new DialogMangerOkCallback() {
//                                    @Override
//                                    public void onOkClick() {
//
//                                    }
//                                });
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
//                        DialogManager.hideProgress(mActivity);

                    }
                });
    }

    public static void NotifyImgBaseFrgLoad() {
        if (mHandlerBaseFrgAct != null) {
            mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
        }
        mRunBaseFrgAct = new Runnable() {
            @Override
            public void run() {
                {
                    try {

                        Bitmap bmp = BitmapFactory
                                .decodeStream(BaseActivity.mActivity.getAssets()
                                        .open("notificationAnimation/bridge-notification-"
                                                + _indexNotifyBaseFrgAct + ".png"));
                        mImgCloseBaseFrgAct.setImageBitmap(bmp);
                        if (!isIncBaseFrgAct || _indexNotifyBaseFrgAct == 1) {
                            _indexNotifyBaseFrgAct++;

                            isIncBaseFrgAct = false;
                            if (_indexNotifyBaseFrgAct == 7) {
                                _indexNotifyBaseFrgAct = 5;
                                isIncBaseFrgAct = true;
                            }
                        } else {
                            _indexNotifyBaseFrgAct--;
                        }

                        mHandlerBaseFrgAct.postDelayed(mRunBaseFrgAct, 70);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        mHandlerBaseFrgAct.postDelayed(mRunBaseFrgAct, 0);
    }
}
