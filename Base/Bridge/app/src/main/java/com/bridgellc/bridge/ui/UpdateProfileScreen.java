package com.bridgellc.bridge.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.SignInEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.SignUpResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateProfileScreen extends BaseActivity implements View.OnClickListener,
        DialogMangerOkCallback {

    private Button mSignUpBtn;
    private EditText mFirstNameEdt, mLastNameEdt, mEmailEdt, mPhNumEdt, mOldPwdEdt, mPwdEdt,
            mConfmPwdEdt,
            mEduEmailEdt;
    private String mFirstName, mLastName, mEmail, mPhNum, mOldPwd, mPwd, mConfmPwd, mSchoolUniv,
            mDOB, mMode, mEduEmail;
    private ImageView mVerifyImg,mEduVerifyImg, mTermImg;
    private TextView mDOBTxt, mMaleTxt, mFemaleTxt, mEduHintTxt;
    public static TextView mSchoolUnivTxt;
    private String mGender = "";
    private int mDate, mMonth, mYear;
    private SignInEntity mSignin;
    private String mDateInput = "MM-dd-yyyy";
    private LinearLayout mFirsTimeLay, mEduEmailLay, mSignInLay;
    public static String mHeader, mSignUp;
    private String mDeviceId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_update_profile_screen);
        initComponents();
    }

    private void initComponents() {


        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mSignUpBtn = (Button) findViewById(R.id.footer_btn);
        mFooterLay = (LinearLayout) findViewById(R.id.footer_parent_one_lay);
        mFirsTimeLay = (LinearLayout) findViewById(R.id.firs_time_lay);
        mEduEmailLay = (LinearLayout) findViewById(R.id.edu_email_lay);
        mSignInLay = (LinearLayout) findViewById(R.id.sign_in_lay);
        mEduEmailLay.setVisibility(View.GONE);


        mFirstNameEdt = (EditText) findViewById(R.id.first_name_edt);
        mLastNameEdt = (EditText) findViewById(R.id.last_name_edt);
        mEmailEdt = (EditText) findViewById(R.id.email_edt);
        mPhNumEdt = (EditText) findViewById(R.id.ph_edt);
        mOldPwdEdt = (EditText) findViewById(R.id.old_pwd_edt);
        mPwdEdt = (EditText) findViewById(R.id.pwd_edt);
        mConfmPwdEdt = (EditText) findViewById(R.id.confm_pwd_edt);
        mEduEmailEdt = (EditText) findViewById(R.id.email_verify_edt);

        mSchoolUnivTxt = (TextView) findViewById(R.id.school_univ_txt);
        mDOBTxt = (TextView) findViewById(R.id.dob_txt);
        mMaleTxt = (TextView) findViewById(R.id.male_txt);
        mFemaleTxt = (TextView) findViewById(R.id.female_txt);
        mEduHintTxt = (TextView) findViewById(R.id.edu_hint_txt);

        AppConstants.SELECT_SCH_UNI = AppConstants.FAILURE_CODE;
        AppConstants.SELECT_SCH_UNI_ID = AppConstants.FAILURE_CODE;
        mPhNumEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM));

        mPhNumEdt.setFocusable(false);
        mPhNumEdt.setFocusableInTouchMode(false);
        mPhNumEdt.setClickable(false);

        mVerifyImg = (ImageView) findViewById(R.id.verify_img);
        mTermImg = (ImageView) findViewById(R.id.term_img);


        mEduVerifyImg = (ImageView) findViewById(R.id.edu_verify_img);

//        mVerifyImg.setVisibility(GlobalMethods.getValueFromPreference(this, GlobalMethods
//                .BOOLEAN_PREFERENCE, AppConstants.ISUSERVERIFIED).equals(true) ? View.VISIBLE : View.INVISIBLE);

        mEduVerifyImg.setVisibility(GlobalMethods.getValueFromPreference(this, GlobalMethods
                .BOOLEAN_PREFERENCE, AppConstants.ISUSERVERIFIED).equals(true) ? View.VISIBLE : View.INVISIBLE);

        mTermImg.setTag(getString(R.string.off));
        mGender = (getString(R.string.male));

        mEmailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (GlobalMethods.isEmailValid(mEmailEdt.getText().toString().trim(), true)) {
//                    mVerifyImg.setVisibility(View.VISIBLE);
//                } else {
//                    mVerifyImg.setVisibility(View.INVISIBLE);
//                }
                if (mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
                    mEduEmailLay.setVisibility(View.GONE);

                } else {
                    if (!GlobalMethods.isEmailValid(mEmailEdt.getText().toString().trim(), true)) {
                        mEduEmailLay.setVisibility(View.VISIBLE);
                    } else {
                        mEduEmailLay.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmailEdt.setClickable(false);
        mHeaderTxt.setText(mHeader.toUpperCase(Locale.ENGLISH));
        mSignUpBtn.setText(mSignUp);
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        if (mHeader.equalsIgnoreCase(getString(R.string.sign_up)) || AppConstants.BACK_FB.equalsIgnoreCase("Fb")) {
            mFirsTimeLay.setVisibility(View.VISIBLE);
            mFooterLay.setBackgroundColor(getResources().getColor(R.color.light_green));
            mSignUpBtn.setClickable(false);
            mEduHintTxt.setVisibility(View.VISIBLE);

            mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
            if (AppConstants.BACK_FB.equalsIgnoreCase("Fb") && !mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
                mSignInLay.setVisibility(View.GONE);
                setData();
            }
        } else {
            mFirsTimeLay.setVisibility(View.GONE);
            mEduHintTxt.setVisibility(View.GONE);

            setData();
        }

    }

    private void setData() {

        mFirstNameEdt.setText(GlobalMethods.getDecodedmessage(GlobalMethods.getStringValue(this, AppConstants.FIRST_NAME)));
        mLastNameEdt.setText(GlobalMethods.getDecodedmessage(GlobalMethods.getStringValue(this, AppConstants.LAST_NAME)));
        mEduEmailEdt.setText(GlobalMethods.getStringValue(this, AppConstants.EDU_EMAIL_ADDRESS));
        mEmailEdt.setText(GlobalMethods.getStringValue(this, AppConstants.EMAIL_ADDRESS));
        mPhNumEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM));
        mPwdEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PASSWORD));
        mConfmPwdEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PASSWORD));
        mDOBTxt.setText(GlobalMethods.getStringValue(this, AppConstants.DOB));
        mGender = (GlobalMethods.getStringValue(this, AppConstants.GENDER));
        if (mGender.equalsIgnoreCase(getString(R.string.one))) {

            mGender = (getString(R.string.male));
            mMaleTxt.setTextColor(getResources().getColor(R.color.blue_gray));
            mFemaleTxt.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            mGender = (getString(R.string.female));
            mMaleTxt.setTextColor(getResources().getColor(R.color.light_gray));
            mFemaleTxt.setTextColor(getResources().getColor(R.color.blue_gray));
        }

//        if (!GlobalMethods.isEmailValid(mEmailEdt.getText().toString().trim(), true)) {
//            mEduEmailLay.setVisibility(View.VISIBLE);
//        } else {
//            mEduEmailLay.setVisibility(View.GONE);
//        }

        if (!GlobalMethods.getStringValue(this, AppConstants.USER_UNIVERSITY_NAME)
                .equalsIgnoreCase("")) {
            AppConstants.SELECT_SCH_UNI = GlobalMethods.getStringValue(this, AppConstants
                    .USER_UNIVERSITY_NAME);
            mSchoolUnivTxt.setText(AppConstants.SELECT_SCH_UNI);
            mSchoolUnivTxt.setVisibility(View.VISIBLE);
        }
        if (!GlobalMethods.getStringValue(this, AppConstants.USER_UNIVERSITY_ID).equalsIgnoreCase("")) {
            AppConstants.SELECT_SCH_UNI_ID = GlobalMethods.getStringValue(this, AppConstants
                    .USER_UNIVERSITY_ID);
        } else {
            AppConstants.SELECT_SCH_UNI_ID = getString(R.string.zero);
        }
        if (GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE).equalsIgnoreCase(getString(R.string.two))) {
            mOldPwdEdt.setFocusable(false);
            mOldPwdEdt.setFocusableInTouchMode(false);
            mOldPwdEdt.setClickable(false);
            mPwdEdt.setFocusable(false);
            mPwdEdt.setFocusableInTouchMode(false);
            mPwdEdt.setClickable(false);
            mConfmPwdEdt.setFocusable(false);
            mConfmPwdEdt.setFocusableInTouchMode(false);
            mConfmPwdEdt.setClickable(false);
            mEmailEdt.setFocusable(false);
            mEmailEdt.setFocusableInTouchMode(false);
            mEmailEdt.setClickable(false);

        }
        if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {

            mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
            mEduEmailLay.setVisibility(View.VISIBLE);
            mPhNumEdt.setFocusable(false);
            mPhNumEdt.setFocusableInTouchMode(false);
            mPhNumEdt.setClickable(false);
        } else {
            mPhNumEdt.setFocusableInTouchMode(true);
            mPhNumEdt.setClickable(true);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
            PhOTPEmailScreen.mScreenShows = 2;
            previousScreen(PhOTPEmailScreen.class, true);
        } else if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {

        } else if (mHeader.equalsIgnoreCase(getString(R.string.update_profile))) {
            if (AppConstants.BACK_FB.equalsIgnoreCase("Setting")) {
                previousScreen(SettingsScreen.class, true);
            } else {
                previousScreen(HomeScreenActivity.class, true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.data_lay:
                showDatePickerDialog();
                break;
            case R.id.male_txt:
                if (mGender.equalsIgnoreCase(getString(R.string.female))) {

                    mGender = (getString(R.string.male));
                    mMaleTxt.setTextColor(getResources().getColor(R.color.blue_gray));
                    mFemaleTxt.setTextColor(getResources().getColor(R.color.light_gray));
                }
                break;
            case R.id.female_txt:
                if (mGender.equalsIgnoreCase(getString(R.string.male))) {

                    mGender = (getString(R.string.female));
                    mMaleTxt.setTextColor(getResources().getColor(R.color.light_gray));
                    mFemaleTxt.setTextColor(getResources().getColor(R.color.blue_gray));
                }
                break;
            case R.id.term_img:
                if (String.valueOf(mTermImg.getTag()).equalsIgnoreCase(getString(R.string.off))) {
                    mTermImg.setTag(getString(R.string.on));
                    mTermImg.setImageResource(R.drawable.term_on);
                    mFooterLay.setBackgroundColor(getResources().getColor(R.color.green));
                    mSignUpBtn.setClickable(true);
                } else {
                    mTermImg.setTag(getString(R.string.off));
                    mTermImg.setImageResource(R.drawable.term_off);
                    mFooterLay.setBackgroundColor(getResources().getColor(R.color.light_green));
                    mSignUpBtn.setClickable(false);
                }

                break;
            case R.id.footer_btn:
                validateFields();
//                if (String.valueOf(mTermImg.getTag()).equalsIgnoreCase(getString(R.string.on))) {
//
//                }
//               previousScreen(SignInScreen.class,true);
                break;
            case R.id.sign_in_txt:

                previousScreen(SignInScreen.class, true);
                break;
            case R.id.univ_lay:
                SelectUniversityScreen.mFromSignup = mHeader;
                nextScreen(SelectUniversityScreen.class, false);
                break;

            case R.id.term_txt:
                nextScreen(TermsCondScreen.class, false);
                break;
            case R.id.email_verify_btn:
                AppConstants.SIGNUPVERIFYBACK = getString(R.string.sign_up);
                PhOTPEmailScreen.mScreenShows = 3;
                nextScreen(PhOTPEmailScreen.class, false);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
//            mEmailEdt.setText(GlobalMethods.getStringValue(this, AppConstants.EMAIL_ADDRESS));
//            if (!GlobalMethods.isEmailValid(mEmailEdt.getText().toString().trim(), true)) {
//                mEduEmailLay.setVisibility(View.VISIBLE);
//            } else {
//                mEduEmailLay.setVisibility(View.GONE);
//            }

        }
        mPhNumEdt.setText(GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM));
    }

    private void validateFields() {

        mFirstName = mFirstNameEdt.getText().toString().trim();
        mLastName = mLastNameEdt.getText().toString().trim();
        mEmail = mEmailEdt.getText().toString().trim();
        mPhNum = mPhNumEdt.getText().toString().trim();
        mOldPwd = mOldPwdEdt.getText().toString().trim();
        mPwd = mPwdEdt.getText().toString().trim();
        mConfmPwd = mConfmPwdEdt.getText().toString().trim();
        mSchoolUniv = mSchoolUnivTxt.getText().toString().trim();
        mDOB = mDOBTxt.getText().toString().trim();
        mEduEmail = mEduEmailEdt.getText().toString().trim();

        if (mFirstName.isEmpty()) {
            mFirstNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .enter_first_name), this);
        } else if (mLastName.isEmpty()) {

            mLastNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .enter_last_name), this);

        } else if (mEmail.isEmpty() || (!GlobalMethods.isEmailValid(mEmail, false))) {

            mEmailEdt.requestFocus();
            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .signup_enter_email), this);

        } else if (mPhNum.isEmpty()) {

            mPhNumEdt.requestFocus();
            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .signup_enter_phone), this);

        } else if (!pwd()) {


        } else if (mSchoolUniv.isEmpty()) {

            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .enter_university), this);

        } else if (mDOB.isEmpty()) {

            DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                    .enter_dob), this);

        } else {
            String gender = "";

            if (mGender.equalsIgnoreCase(getString(R.string.male))) {
                gender = "1";
            } else {
                gender = "2";
            }
            mMode = getString(R.string.three);
            if (mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {

                final String finalGender = gender;


                APIRequestHandler.getInstance().getSignUpResponse(mFirstName, mLastName, mEmail,
                        mPhNum, mPwd, AppConstants.SELECT_SCH_UNI_ID, mDOB, finalGender, GlobalMethods.getStringValue(UpdateProfileScreen.this, AppConstants.LOGINTYPE), mDeviceId, "", mMode,
                        UpdateProfileScreen.this);

            } else {
                mMode = GlobalMethods.getStringValue(UpdateProfileScreen.this, AppConstants.PAYMENT_MODE);
                String socialId = "";
                if (GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE).equalsIgnoreCase(getString(R.string.two))) {

                    socialId = GlobalMethods.getStringValue(UpdateProfileScreen.this, AppConstants.FB_ID);


                }

                if (mEduEmailLay.getVisibility() == View.VISIBLE && !mEduEmail.equalsIgnoreCase("")) {
                    if (!GlobalMethods.isEmailValid(mEduEmail, true)) {
                        DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string.enter_edu_email), this);
                    } else {
                        if (!AppConstants.BACK_FB.equalsIgnoreCase("Setting")) {
                            final String finalGen = gender;
                            final String finalSocialId = socialId;


                            APIRequestHandler.getInstance().getUpdateProfileResponse(mFirstName,
                                    mLastName, mEmail, mEduEmail,
                                    mPhNum, mPwd, AppConstants.SELECT_SCH_UNI_ID, mDOB, finalGen, GlobalMethods.getStringValue(UpdateProfileScreen.this, AppConstants.LOGINTYPE), finalSocialId, mMode,
                                    UpdateProfileScreen.this);
                        } else {
                            APIRequestHandler.getInstance().getUpdateProfileResponse(mFirstName, mLastName, mEmail, mEduEmail,
                                    mPhNum, mPwd, AppConstants.SELECT_SCH_UNI_ID, mDOB, gender, GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE), socialId, mMode,
                                    this);
                        }
                    }
                } else {
                    if (!AppConstants.BACK_FB.equalsIgnoreCase("Setting")) {
                        final String finalGen = gender;
                        final String finalSocialId = socialId;


                        APIRequestHandler.getInstance().getUpdateProfileResponse(mFirstName,
                                mLastName, mEmail, mEduEmail,
                                mPhNum, mPwd, AppConstants.SELECT_SCH_UNI_ID, mDOB, finalGen, GlobalMethods.getStringValue(UpdateProfileScreen.this, AppConstants.LOGINTYPE), finalSocialId, mMode,
                                UpdateProfileScreen.this);
                    } else {
                        APIRequestHandler.getInstance().getUpdateProfileResponse(mFirstName,
                                mLastName, mEmail, mEduEmail,
                                mPhNum, mPwd, AppConstants.SELECT_SCH_UNI_ID, mDOB, gender, GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE), socialId, mMode,
                                this);
                    }
                }
            }

        }
    }


    private boolean validateMatchPass() {
        if (!mConfmPwd.equals(mPwd)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean pwd() {
        boolean pwd = true;
        if (GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE).equalsIgnoreCase(getString(R.string.one))) {
            if (mPwd.isEmpty()) {

                mPwdEdt.requestFocus();
                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                        .signup_enter_pwd), this);
                pwd = false;

//            } else if (!GlobalMethods.isPasswordValid(this, mPwd).equalsIgnoreCase
//                    (getString
//                            (R.string.password_valid))) {
//                mPwdEdt.requestFocus();
//                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string.app_name),
//                        GlobalMethods
//                                .isPasswordValid(this, mPwd), this);
//                pwd = false;
            } else if (mConfmPwd.isEmpty()) {

                mConfmPwdEdt.requestFocus();
                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                        .enter_confm_pwd), this);
                pwd = false;
            } else if (!validateMatchPass()) {

                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                        .enter_diff_pwd_confm), this);
                pwd = false;
            } else if (!GlobalMethods.getStringValue(this, AppConstants.PASSWORD).equalsIgnoreCase
                    (mPwd)||!GlobalMethods.getStringValue(this, AppConstants.PASSWORD).equalsIgnoreCase
                    (mConfmPwd)) {
                if (!mOldPwd.equalsIgnoreCase(GlobalMethods.getStringValue(this, AppConstants
                        .PASSWORD))) {
                    mOldPwdEdt.requestFocus();
                    DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, getString(R.string
                            .signup_enter_old_pwd), this);
                    pwd = false;
                }
            }
        }
        return pwd;
    }

    private void showDatePickerDialog() {

        String dob = mDOBTxt.getText().toString().trim();

        final Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, mDateSetListener,
                mYear, mMonth, mDate);
        if (dob != null && !dob.equalsIgnoreCase("")) {
            String[] dateofbirth = dob.split("-");
            mDatePicker = new DatePickerDialog(this, mDateSetListener,
                    Integer.valueOf(dateofbirth[2]), (Integer.valueOf
                    (dateofbirth[0]) - 1), Integer.valueOf(dateofbirth[1]));
        }

        mDatePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());
        mDatePicker.show();


    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog
            .OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            final Calendar cal = Calendar.getInstance();

            if (mYear >= cal.get(Calendar.YEAR) && mMonth >= cal.get(Calendar.MONTH) && mDate >=
                    cal.get(Calendar.DAY_OF_MONTH)) {

                Toast.makeText(UpdateProfileScreen.this, "Select past date", Toast.LENGTH_LONG).show();
            } else {

                String date = new StringBuilder()
                        .append(mMonth + 1).append("-").append(mDate).append("-").append(mYear).toString();
                SimpleDateFormat dateinputFormat = new SimpleDateFormat(mDateInput,
                        Locale.US);

                Date date1 = null;
                String mDatestr = null;

                try {
                    date1 = dateinputFormat.parse(date);
                    mDatestr = dateinputFormat.format(date1);
                    mDOBTxt.setText(mDatestr);

                } catch (Exception e) {
                }
            }


        }

    };

    @Override
    public void onOkClick() {

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof SignUpResponse) {
            final SignUpResponse signupres = (SignUpResponse) responseObj;

            if (signupres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                mSignin = signupres.getResult();
//                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, mHeader, getString(R.string
//                        .serv_res_success), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {
//                        GlobalMethods.userDetails(true, signupres.getUser_id(), mFirstName,
//                                mLastName, mEmail, mPhNum, mPwd, mSchoolUnivTxt.getText()
//                                        .toString(), mDOB, mGender, UpdateProfileScreen.this);
                GlobalMethods.userDetails(true, mSignin.getUser_id(), mSignin.getFirst_name(),
                        mSignin.getLast_name(), mSignin.getEmail_id(), mSignin.getEdu_email_id(), mSignin.getPhone_number(), mSignin.getPassword(), mSignin.getUniversity_name(), mSignin.getUniversity_id(), mSignin.getDob(), mSignin.getGender(), mSignin.getLogin_type(), mSignin.getBank_details(), mSignin.getCard_details(), mSignin.getPayment_mode(), mSignin.getPayment_details(), mSignin.getPartner(), UpdateProfileScreen.this);

                if (mSignin.getMobile_verify().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    PhOTPEmailScreen.mScreenShows = 2;
                    PhOTPEmailScreen.mFBSignupReg = getString(R.string.three);
                    PhOTPEmailScreen.mOTP = mSignin.getOtp();
                    PhOTPEmailScreen.mOldPh = mSignin.getPhone_number();
                    GlobalMethods.storeValuetoPreference(UpdateProfileScreen.this,
                            GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
                            mSignin.getNew_phone());
                    nextScreen(PhOTPEmailScreen.class, false);
                } else {

                    if (!GlobalMethods.isEmailValid(mEmailEdt.getText().toString().trim(),
                            true)) {

                        if (mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
                            AppConstants.SIGNUPVERIFYBACK = getString(R.string.sign_up);
                            PhOTPEmailScreen.mScreenShows = 3;
                            nextScreen(PhOTPEmailScreen.class, true);
                        } else {
                            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                                GlobalMethods.storeValuetoPreference(UpdateProfileScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, false);
                                nextScreen(HomeScreenActivity.class, true);
                            } else {
                                onBackPressed();
                            }
                        }
                    } else {
                        if (mHeader.equalsIgnoreCase(getString(R.string.sign_up)) || AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                            nextScreen(HomeScreenActivity.class, true);
                        } else
                            onBackPressed();
                    }
                }
//                    }
//                });
            } else {
                DialogManager.showBasicAlertDialog(this,
                        signupres.getMessage(), this);

            }
        }
    }


}


