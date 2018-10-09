package com.calix.calixgigaspireapp.ui.settings;

import android.app.KeyguardManager;
import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
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
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.main.FingerprintHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.loginregconfig.Login;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PinCodeFingerPrintLogin extends BaseActivity {

    @BindView(R.id.pin_finger_parent_lay)
    ViewGroup mPinFingerViewGroup;

    @BindView(R.id.pin_finger_header_bg_lay)
    RelativeLayout mPinFingerHeaderBgLay;

    @BindView(R.id.pin_finger_header_img)
    ImageView mPinFingerHeaderImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img_lay)
    RelativeLayout mHeaderLeftImgLay;

    @BindView(R.id.pass_code_edt)
    EditText mPassCodeEdt;

    @BindView(R.id.touch_id_lay)
    LinearLayout mTouchIdLay;

    private KeyStore mKeyStore;
    private Cipher mCipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_pin_fingure_print_login);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerPrint();
        }
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mPinFingerViewGroup);

        /*Keypad button action*/
        mPassCodeEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_GO) {
                    validateFields();
                }
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            mTouchIdLay.setVisibility(fingerprintManager != null && fingerprintManager.isHardwareDetected() && PreferenceUtil.getBoolPreferenceValue(this, AppConstants.TOUCH_ID_ENABLE_STATUS) ? View.VISIBLE : View.GONE);
        } else {
            mTouchIdLay.setVisibility(View.GONE);
        }


        setHeaderView();
    }


    private void setHeaderView() {
        /*set header changes*/
        mPinFingerHeaderImg.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImgLay.setVisibility(View.INVISIBLE);
        mHeaderTxt.setText(getString(R.string.welcome));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPinFingerHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mPinFingerHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(PinCodeFingerPrintLogin.this)));
                    mPinFingerHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(PinCodeFingerPrintLogin.this), 0, 0);
                    mPinFingerHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(PinCodeFingerPrintLogin.this);
        String passCodeStr = mPassCodeEdt.getText().toString().trim();


        if (passCodeStr.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_pin), this);
        } else if (!passCodeStr.equals(PreferenceUtil.getStringValue(this, AppConstants.LOGIN_PIN_PWD))) {
            mPassCodeEdt.setSelection(passCodeStr.length());
            String hintMsg=getString(R.string.incorrect_pin)+"\n"+getString(R.string.incorrect_pin_note);
            DialogManager.getInstance().showAlertPopup(this, hintMsg, this);
        } else {
            Class<?> nextScreenClass = Login.class;

            if (PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_STATUS)) {
                nextScreenClass = Dashboard.class;
            }
            DialogManager.getInstance().showToast(PinCodeFingerPrintLogin.this, getString(R.string.logged_success));
            nextScreen(nextScreenClass);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerPrint() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        if (fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints() && keyguardManager != null && keyguardManager.isKeyguardSecure()) {
            generateKey();
            if (cipherInit()) {
                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                FingerprintHandler helper = new FingerprintHandler(this);
                helper.startAuth(fingerprintManager, cryptoObject);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            mKeyStore = KeyStore.getInstance(getString(R.string.android_key_store));
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, getString(R.string.android_key_store));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(getString(R.string.failed_key_generator), e);
        }

        try {
            mKeyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(AppConstants.ANDROID,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(getString(R.string.failed_get_cipher), e);
        }

        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(AppConstants.ANDROID,
                    null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(getString(R.string.failed_init_cipher), e);
        }
    }
}