package com.calix.calixgigaspireapp.ui.loginregconfig;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.input.model.ScannerInputModel;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.RouterSetupResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RouterConfiguration extends BaseActivity {

    @BindView(R.id.router_config_parent_lay)
    ViewGroup mRouterConfigViewGroup;

    @BindView(R.id.router_config_header_bg_lay)
    RelativeLayout mRouterConfigHeaderBgLay;

    @BindView(R.id.router_config_header_img)
    ImageView mRouterConfigHeaderImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img_lay)
    RelativeLayout mHeaderLeftImgLay;

    @BindView(R.id.mac_address_edt)
    EditText mMacAddressEdt;

    @BindView(R.id.serial_number_edt)
    EditText mSerialNumberEdt;


    private TextWatcher mMacAddressTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_router_config);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannerInputModel scannerDetailsRes = AppConstants.SCANNED_DETAILS_RES;
        if (!scannerDetailsRes.getScanned_details().isEmpty()) {

            mMacAddressEdt.setText(scannerDetailsRes.getMac_Address().replace(":", ""));
            mSerialNumberEdt.setText(scannerDetailsRes.getSerial_Number());

            String scannedDataStrArr[] = (scannerDetailsRes.getScanned_details().replace("&", "\",\"").replace("=", "\":\"")).split("\\?");

            String scannedDataStr = scannedDataStrArr.length > 1 ? scannedDataStrArr[1] : "";
            AppConstants.SCANNED_DETAILS_RES = new ScannerInputModel();

            if (scannedDataStr.isEmpty()) {

                if (scannedDataStrArr.length > 0 && !scannedDataStrArr[0].isEmpty()) {
                    if (scannerDetailsRes.getId() == R.id.mac_address_img) {
                        if (scannedDataStrArr[0].replace(":", "").length() < 13)
                            mMacAddressEdt.setText(scannedDataStrArr[0].replace(":", ""));
                        else
                            showInvalidQRMsg();
                    } else if (scannerDetailsRes.getId() == R.id.serial_number_img) {
                        mSerialNumberEdt.setText(scannedDataStrArr[0]);
                    } else
                        showInvalidQRMsg();

                } else {
                    showInvalidQRMsg();
                }
            } else {
                try {
                    JSONObject json = new JSONObject("{" + "\"" + scannedDataStr + "\"" + "}");
                    sysOut("json = new" + json.toString());
                    String macStr = (json.has("mac") ? json.getString("mac") : "").replace(":", "");
                    String serialNumberStr = json.has("sn") ? json.getString("sn") : "";

                    if (scannerDetailsRes.getId() == R.id.mac_address_img) {
                        if (macStr.length() < 13)
                            mMacAddressEdt.setText(macStr.replace(":", ""));
                        else
                            showInvalidQRMsg();
                    } else if (scannerDetailsRes.getId() == R.id.serial_number_img) {
                        mSerialNumberEdt.setText(serialNumberStr);
                    } else if (scannerDetailsRes.getId() == R.id.qr_code_scanner_img) {
                        mMacAddressEdt.setText(macStr.replace(":", ""));
                        mSerialNumberEdt.setText(serialNumberStr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showInvalidQRMsg();
                }
            }

        } else if (!scannerDetailsRes.getMac_Address().isEmpty()) {
            mMacAddressEdt.setText(scannerDetailsRes.getMac_Address().replace(":", ""));
        } else if (!scannerDetailsRes.getSerial_Number().isEmpty()) {
            mSerialNumberEdt.setText(scannerDetailsRes.getSerial_Number());

        }
    }

    private void showInvalidQRMsg() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogManager.getInstance().showToast(getApplicationContext(), getString(R.string.invalid_code));
            }
        });
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mRouterConfigViewGroup);

        mMacAddressTextWatcher = new MACAddressTextWatcher();
        mMacAddressEdt.addTextChangedListener(mMacAddressTextWatcher);

        /*Keypad button action*/
        mSerialNumberEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.router_config));
        mRouterConfigHeaderImg.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderLeftImgLay.setVisibility(AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS ? View.VISIBLE : View.INVISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRouterConfigHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mRouterConfigHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(RouterConfiguration.this)));
                    mRouterConfigHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(RouterConfiguration.this), 0, 0);
                    mRouterConfigHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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
    @OnClick({R.id.header_left_img_lay, R.id.mac_address_img, R.id.serial_number_img, R.id.qr_code_scanner_img, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.mac_address_img:
            case R.id.serial_number_img:
            case R.id.qr_code_scanner_img:
                ScannerInputModel scannerDetailsRes = new ScannerInputModel();
                scannerDetailsRes.setId(v.getId());
                scannerDetailsRes.setMac_Address(mMacAddressEdt.getText().toString().trim());
                scannerDetailsRes.setSerial_Number(mSerialNumberEdt.getText().toString().trim());
                AppConstants.SCANNED_DETAILS_RES = scannerDetailsRes;
                if (askPermissions()) {
                    nextScreen(QRBarcodeScanner.class);
                }
                break;
            case R.id.submit_btn:
                validateFields();
                break;
        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String macAddressStr = mMacAddressEdt.getText().toString().trim();
        String serialNumberStr = mSerialNumberEdt.getText().toString().trim();

        if (macAddressStr.isEmpty()) {
            mMacAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_mac_address), this);
        } else if (serialNumberStr.isEmpty()) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_serial_number), this);
        } else {
            APIRequestHandler.getInstance().routerSetupAPICall(macAddressStr, serialNumberStr, this);
        }
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterSetupResponse) {
            RouterSetupResponse routerSetupResponse = (RouterSetupResponse) resObj;
            AppConstants.ROUTER_DETAILS_ENTITY.setRouterId(routerSetupResponse.getRouterId());
            AppConstants.ROUTER_ON_BOARD_FROM_WELCOME = true;
            nextScreen(RouterDetailsUpdate.class);
        }
    }


    @Override
    public void onRequestFailure(final Object inputModelObj, Throwable t) {
        super.onRequestFailure(inputModelObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ||t instanceof java.net.UnknownHostException? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (inputModelObj instanceof RouterSetupResponse) {
                                validateFields();
                            }
                        }
                    });
        }
    }


    private class MACAddressTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int cursorSelectPosInt = mMacAddressEdt.getSelectionStart();
                    char[] ss = (mMacAddressEdt.getText().toString().trim().replace(getString(R.string.colon_sym), "")).toCharArray();
                    StringBuilder fullStr = new StringBuilder();

                    for (int i = 0; i < ss.length; i++) {
                        fullStr.append((i != 0 && i % 2 == 0) ? getString(R.string.colon_sym) : "").append(ss[i]);
                    }

                    mMacAddressEdt.removeTextChangedListener(mMacAddressTextWatcher);
                    mMacAddressEdt.setText(fullStr.toString());
                    mMacAddressEdt.setSelection(cursorSelectPosInt + 1 <= mMacAddressEdt.getText().toString().trim().length() ? cursorSelectPosInt + 1 : mMacAddressEdt.getText().toString().trim().length());
                    mMacAddressEdt.addTextChangedListener(mMacAddressTextWatcher);
                }
            });

        }
    }


    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    nextScreen(QRBarcodeScanner.class);
                }

                public void onNegativeClick() {
                }
            });
        }

        return addPermission;
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS) {
            backScreen();
        } else {
            super.onBackPressed();
        }
    }
}