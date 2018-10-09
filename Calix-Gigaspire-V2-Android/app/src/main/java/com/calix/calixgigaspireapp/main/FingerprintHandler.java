package com.calix.calixgigaspireapp.main;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.loginregconfig.Login;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;


@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context mContext;

    // Constructor
    public FingerprintHandler(Context context) {
        mContext = context;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (errMsgId != FingerprintManager.FINGERPRINT_ERROR_CANCELED) {
            DialogManager.getInstance().showAlertPopup(mContext, mContext.getString(R.string.authentication_error), new InterfaceTwoBtnCallback() {
                @Override
                public void onNegativeClick() {

                }

                @Override
                public void onPositiveClick() {

                }
            });
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        DialogManager.getInstance().showAlertPopup(mContext, mContext.getString(R.string.authentication_help), new InterfaceTwoBtnCallback() {
            @Override
            public void onNegativeClick() {

            }

            @Override
            public void onPositiveClick() {

            }
        });
    }

    @Override
    public void onAuthenticationFailed() {
        DialogManager.getInstance().showAlertPopup(mContext, mContext.getString(R.string.authentication_failed), new InterfaceTwoBtnCallback() {
            @Override
            public void onNegativeClick() {

            }

            @Override
            public void onPositiveClick() {

            }
        });
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Class<?> nextScreenClass = Login.class;

        if (PreferenceUtil.getBoolPreferenceValue(mContext, AppConstants.LOGIN_STATUS)) {
            nextScreenClass = Dashboard.class;
        }

        DialogManager.getInstance().showToast(mContext, mContext.getString(R.string.logged_success));
        ((BaseActivity) mContext).nextScreen(nextScreenClass);
    }

}
