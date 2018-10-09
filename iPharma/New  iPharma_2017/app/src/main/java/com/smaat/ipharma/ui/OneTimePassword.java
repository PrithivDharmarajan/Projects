package com.smaat.ipharma.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.ipharma.utils.GlobalMethods.BOOLEAN_PREFERENCE;

/**
 * Created by admin on 1/10/2017.
 */

public class OneTimePassword extends BaseActivity {

    @BindView(R.id.parent_layout)
    LinearLayout m_Mainlayout;

    @BindView(R.id.submit_otp)
    Button Submit_otp;
    @BindView(R.id.resend_otp)
    Button Resendotp;

    @BindView(R.id.otp_text)
    EditText Otptext;

    @BindView(R.id.backbutton)
    ImageView backButton;

    private String Userid;

    private boolean from_resend = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        setupUI(m_Mainlayout);
        Userid = GlobalMethods.getUserID(this);
    }


    @OnClick({R.id.submit_otp, R.id.resend_otp,R.id.backbutton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_otp:
                from_resend = false;
                String otp_txt = Otptext.getText().toString();
                if (otp_txt.equalsIgnoreCase("")) {
                    DialogManager.showMsgPopup(this, getString(R.string.app_name),getString(R.string.enter_OTP));
                } else {
                    APIRequestHandler.getInstance().otpConfirmation(Userid,
                            otp_txt, this);
                }
                break;
            case R.id.resend_otp:
                from_resend = true;
                APIRequestHandler.getInstance().ResendOtp(Userid,
                        this);
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof OtpEntity) {
            OtpEntity mOtpResponse = (OtpEntity) responseObj;
            if(mOtpResponse.getResult().equalsIgnoreCase(getString(R.string.Success)))
            {
                if (!from_resend) {
                    GlobalMethods.storeValuetoPreference(getApplicationContext(), BOOLEAN_PREFERENCE,AppConstants.IS_LOGGEDIN,true);
                    launchScreen(HomeScreen.class);
                    finishScreen();
                }else{
                    DialogManager.showMsgPopup(OneTimePassword.this,"",getString(R.string.pwd_res));
                }


            }else{
                DialogManager.showMsgPopup(this,getString(R.string.app_name),
                        mOtpResponse.getResult());
            }
        }
    }


}
