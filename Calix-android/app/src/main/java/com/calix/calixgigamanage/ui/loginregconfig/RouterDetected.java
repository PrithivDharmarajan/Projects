package com.calix.calixgigamanage.ui.loginregconfig;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.EncryptionTypeEntity;
import com.calix.calixgigamanage.output.model.EncryptionTypeResponse;
import com.calix.calixgigamanage.output.model.RouterMapEntity;
import com.calix.calixgigamanage.output.model.RouterMapResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.dashboard.Dashboard;
import com.calix.calixgigamanage.ui.settings.Settings;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RouterDetected extends BaseActivity {

    @BindView(R.id.detected_parent_lay)
    ViewGroup mDetectedViewGroup;

    @BindView(R.id.detected_header_bg_lay)
    RelativeLayout mDetectedHeaderBgLay;

    @BindView(R.id.detected_header_msg_lay)
    RelativeLayout mDetectedHeaderMsgLay;

    @BindView(R.id.header_left_img_lay)
    RelativeLayout mHeaderLeftImgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.network_name_edt)
    EditText mNetworkNameEdt;

    @BindView(R.id.router_name_edt)
    EditText mRouterNameEdt;

    @BindView(R.id.pwd_confirm_pwd_lay)
    LinearLayout mPwdConfirmPwdLay;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.confirm_pwd_edt)
    EditText mConfirmPwdEdt;

    @BindView(R.id.auth_type_spinner)
    Spinner mAuthTypeSpinner;

    private LinkedHashMap<String, Integer> mEncryptionTypeHasMap = new LinkedHashMap<>();
    private ArrayList<String> mEncryptionTypStrArrList = new ArrayList<>();
    private boolean isSelectedEncryptionBool = false;
    private int mSelectedEncryptionType = 0, mEncryptionTypeInt = 0;
    private String mRouterIdStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_router_detected);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDetectedViewGroup);

        /*Keypad button action*/
        mConfirmPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        setHeaderView();
        routerMapAPICall();
    }

    private void setHeaderView() {

        /*set header changes*/
        mDetectedHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.network_detected));
        mHeaderLeftImgLay.setVisibility(View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDetectedHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mDetectedHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(RouterDetected.this)));
                    mDetectedHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(RouterDetected.this), 0, 0);
                    mDetectedHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
                }

            });
        }

    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.spinner_drop_down_img, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.spinner_drop_down_img:
                mAuthTypeSpinner.performClick();
                break;
            case R.id.submit_btn:
                validateFields();
                break;
        }
    }


    /*Set Data*/
    private void setData(RouterMapEntity routerDetailsInputModel) {
        mRouterIdStr = routerDetailsInputModel.getRouterId();
        mSelectedEncryptionType = routerDetailsInputModel.getEncryptType();
        mRouterNameEdt.setText(routerDetailsInputModel.getName());
        mNetworkNameEdt.setText(routerDetailsInputModel.getDefaultSsid());
        mPwdEdt.setText(routerDetailsInputModel.getPassword());
        encryptionTypeAPICall();
        sysOut("routerDetailsInputModel.getEncryptType()" + routerDetailsInputModel.getEncryptType());
    }


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String routerNameStr = mRouterNameEdt.getText().toString().trim();
        String networkNameStr = mNetworkNameEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();
        String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();

        if (routerNameStr.isEmpty()) {
            mRouterNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_router_name), this);
        } else if (networkNameStr.isEmpty()) {
            mNetworkNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_network_name), this);
        } else if (mPwdConfirmPwdLay.getVisibility() == View.VISIBLE && pwdStr.length() < 8) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pass_contains_eight_char), this);
        } else if (mPwdConfirmPwdLay.getVisibility() == View.VISIBLE && !pwdStr.equals(confirmPwdStr)) {
            mConfirmPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_does_not_match), this);
        } else {
            /*API call*/
            pwdStr = mPwdConfirmPwdLay.getVisibility() == View.VISIBLE ? pwdStr : "";
            APIRequestHandler.getInstance().routerUpdateAPICall(mRouterIdStr, routerNameStr, networkNameStr, pwdStr, String.valueOf(mEncryptionTypeInt), this);
        }
    }

    /*encryptionTypeAPICall calls*/
    private void encryptionTypeAPICall() {
        APIRequestHandler.getInstance().encryptionTypeAPICall(this);
    }

    /*routerMapAPICall calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            if (AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS) {
                previousScreen(Settings.class);
            } else {
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
                nextScreen(Dashboard.class);
            }
        } else if (resObj instanceof EncryptionTypeResponse) {
            EncryptionTypeResponse routerSetupResponse = (EncryptionTypeResponse) resObj;
            ArrayList<EncryptionTypeEntity> encryptionTypeEntity = routerSetupResponse.getTypes();

            mEncryptionTypeHasMap = new LinkedHashMap<>();
            mEncryptionTypStrArrList = new ArrayList<>();

            for (int i = 0; i < encryptionTypeEntity.size(); i++) {
                if (mSelectedEncryptionType != -1 || (mSelectedEncryptionType == -1 && !encryptionTypeEntity.get(i).getDescription().equalsIgnoreCase(getString(R.string.none)))) {
                    mEncryptionTypeHasMap.put(encryptionTypeEntity.get(i).getDescription(), encryptionTypeEntity.get(i).getValue());
                    mEncryptionTypStrArrList.add(encryptionTypeEntity.get(i).getDescription());

                    if (mSelectedEncryptionType == encryptionTypeEntity.get(i).getValue()) {
                        isSelectedEncryptionBool = true;
                        mEncryptionTypeInt = encryptionTypeEntity.get(i).getValue();
                    }
                }
            }

            if (!isSelectedEncryptionBool && mEncryptionTypStrArrList.size() > 0) {
                mSelectedEncryptionType = 0;
                mEncryptionTypeInt = mEncryptionTypeHasMap.get(mEncryptionTypStrArrList.get(0));
            }

            setSpinnerData();
        } else if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            if (routerMapResponse.getRouters().size() > 0 && routerMapResponse.getRouters().size() > 0) {
                setData(routerMapResponse.getRouters().get(0));
            } else {
                setData(AppConstants.ROUTER_DETAILS_ENTITY);
            }
        }
    }


    private void setSpinnerData() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RouterDetected.this, R.layout.adap_spinner_equ_selected, mEncryptionTypStrArrList);
        adapter.setDropDownViewResource(R.layout.adap_spinner_equ_list);
        mAuthTypeSpinner.setAdapter(adapter);

        sysOut("Before mSelectedEncryptionType---" + mSelectedEncryptionType);


        //Model Spinner item click
        mAuthTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEncryptionTypeInt = mEncryptionTypeHasMap.get(mEncryptionTypStrArrList.get(position));
                mPwdConfirmPwdLay.setVisibility(mEncryptionTypStrArrList.get(position).equalsIgnoreCase(getString(R.string.none)) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (mEncryptionTypStrArrList.size() > 0) {
            mAuthTypeSpinner.setSelection(mSelectedEncryptionType, true);
            mPwdConfirmPwdLay.setVisibility(mEncryptionTypStrArrList.get(mSelectedEncryptionType).equalsIgnoreCase(getString(R.string.none)) ? View.GONE : View.VISIBLE);
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
                            if (resObj instanceof EncryptionTypeResponse) {
                                encryptionTypeAPICall();
                            } else if (resObj instanceof CommonResponse) {
                                validateFields();
                            } else if (resObj instanceof RouterMapResponse) {
                                routerMapAPICall();
                            }
                        }
                    });
        }
    }


    /*onBackPressed*/
    @Override
    public void onBackPressed() {
        if (AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS) {
            previousScreen(Settings.class);
        } else {
            backScreen();
        }
    }
}