package com.bridgellc.bridge.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.ui.upload.UploadScreen;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.paypal.android.sdk.payments.PayPalService;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by USER on 3/11/2016.
 */
public class SettingsScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {


    private ImageView mHeaderLeftImage, mTermImg;
    private ViewPager mTutorialPager;
    private RelativeLayout mTutorialLay, mTutBtnLay;
    ArrayList<Integer> mTutorialImgList = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        initComponents();
    }


    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);
        setHeader();

        mTermImg = (ImageView) findViewById(R.id.term_img);
        if (GlobalMethods.getStringValue(this, AppConstants.UNIVERSITY_MODE).equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

            selectUnivImg(getString(R.string.off));
        } else {

            selectUnivImg(getString(R.string.on));
        }
//        mPaymentInformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppConstants.CARD_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;
//                AppConstants.PAYPAL_DOLL = AppConstants.FAILURE_CODE;
//                nextScreen(AddPayCard.class, true);
//            }
//        });
//
//        mSignout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBaseTwoBtnDialog(SettingsScreen.this);
//            }
//        });
//        mUpdateProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppConstants.SIGNUPVERIFYBACK = getString(R.string.update_profile_c);
//                UpdateProfileScreen.mHeader = getString(R.string.update_profile_c);
//                UpdateProfileScreen.mSignUp = getString(R.string.update_c);
//                AppConstants.BACK_FB = "Setting";
//                nextScreen(UpdateProfileScreen.class, true);
//            }
//        });
//        mBank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;
//                nextScreen(BankAccDetails.class, true);
//            }
//        });

    }

    @Override
    protected void onResume() {

        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) SettingsScreen.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    private void setHeader() {


        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeaderTxt.setText(getString(R.string.Setting).toUpperCase(Locale.ENGLISH));
        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);

        RelativeLayout headerLeft = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        RelativeLayout headerRight = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        headerLeft.setVisibility(View.VISIBLE);
        headerRight.setVisibility(View.INVISIBLE);

        mTutorialPager = (ViewPager) findViewById(R.id.tutorial_pager);
        mTutorialLay = (RelativeLayout) findViewById(R.id.tutorial_pager_lay);
        mTutBtnLay = (RelativeLayout) findViewById(R.id.tut_btn_lay);
        mFooterOneBtn = (Button) findViewById(R.id.footer_btn);


        mTutorialImgList.add(R.drawable.tutorial_1);
        mTutorialImgList.add(R.drawable.tutorial_2);
        mTutorialImgList.add(R.drawable.tutorial_3);
        mTutorialImgList.add(R.drawable.tutorial_4);
        mTutorialImgList.add(R.drawable.tutorial_5);
        mTutorialImgList.add(R.drawable.tutorial_6);
        mTutorialImgList.add(R.drawable.tutorial_7);
        mTutorialImgList.add(R.drawable.tutorial_8);
        mTutorialImgList.add(R.drawable.tutorial_9);
        mTutorialImgList.add(Color.TRANSPARENT);


        mTutorialPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == mTutorialImgList.size() - 1) {

                    SettingsScreen.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    GlobalMethods.storeValuetoPreference(SettingsScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                    mTutorialLay.setVisibility(View.GONE);
                }

                if (position == 5 || position == 6) {
                    String tutBtnTxt = "";
                    mTutBtnLay.setVisibility(View.VISIBLE);
                    if (position == 5) {
                        tutBtnTxt = getString(R.string.new_post);
                    } else {
                        tutBtnTxt = getString(R.string.menu).toLowerCase(Locale.ENGLISH);
                    }

                    tutBtnTxt = tutBtnTxt.replace(tutBtnTxt.substring(0, 1), tutBtnTxt.substring(0, 1).toUpperCase(Locale.ENGLISH));
                    mFooterOneBtn.setText(tutBtnTxt);
                } else {
                    mTutBtnLay.setVisibility(View.GONE);
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.update_profile_lay:

                AppConstants.SIGNUPVERIFYBACK = getString(R.string.update_profile);
                UpdateProfileScreen.mHeader = getString(R.string.update_profile);
                UpdateProfileScreen.mSignUp = getString(R.string.update);
                AppConstants.BACK_FB = "Setting";
                nextScreen(UpdateProfileScreen.class, true);

                break;
            case R.id.payment_info_lay:
                AppConstants.CARD_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;
                AppConstants.PAYPAL_DOLL = AppConstants.FAILURE_CODE;
                nextScreen(AddPayCard.class, true);
                break;
            case R.id.bank_lay:
                AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;
                //nextScreen(BankAccDetails.class, true);
                if (GlobalMethods.getStringValue(this, AppConstants.PAYMENT_DETAILS).equalsIgnoreCase(getString(R.string.one))) {

                    nextScreen(PaymentCardListScreen.class, true);
                } else {

                    nextScreen(PaymentHomeScreen.class, true);
                }
//                nextScreen(PaymentCardListScreen.class, true);
                break;
            case R.id.tutorial_lay:
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                mTutorialPager.setAdapter(new TutorialPagerAdapter(mTutorialImgList));
                mTutorialPager.setCurrentItem(0);

                mTutorialLay.setVisibility(View.VISIBLE);

                break;

            case R.id.skip_txt:

                SettingsScreen.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                GlobalMethods.storeValuetoPreference(SettingsScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                mTutorialLay.setVisibility(View.GONE);
                break;
            case R.id.cond_lay:
                final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                emailInt.setData(Uri.parse("mailto:" + AppConstants.PARTNER_EMAIL_ID));
                emailInt.putExtra(Intent.EXTRA_SUBJECT, "");
                emailInt.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailInt);
                break;

            case R.id.signout_lay:
                DialogManager.showBaseTwoBtnDialog(SettingsScreen.this, getString(R.string.sign_out_title), getString(R.string.sign_out_con), getString(R.string.yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                    @Override
                    public void onBtnOkClick(String mOkStr) {

                        APIRequestHandler.getInstance().getLogoutResponse(SettingsScreen.this);
                    }

                    @Override
                    public void onBtnCancelClick(String mCancelStr) {

                    }
                });
                break;
            case R.id.term_img:
                if (String.valueOf(mTermImg.getTag()).equalsIgnoreCase(getString(R.string.off))) {
                    selectUnivImg(getString(R.string.on));

                } else {
                    selectUnivImg(getString(R.string.off));
                }

                break;
            case R.id.footer_btn:
                if (mFooterOneBtn.getText().toString().equalsIgnoreCase(getString(R.string.menu))) {
                    if (slidingDrawer != null) {
                        slidingDrawer.animateOpen();
                    }
                } else {
                    nextScreen(UploadScreen.class, false);
                }
                SettingsScreen.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                GlobalMethods.storeValuetoPreference(SettingsScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                mTutorialLay.setVisibility(View.GONE);
                break;


        }
    }

    private void selectUnivImg(String mtag) {

        if (mtag.equalsIgnoreCase(getString(R.string.on))) {
            mTermImg.setTag(getString(R.string.on));
            mTermImg.setImageResource(R.drawable.term_on);

            GlobalMethods.storeValuetoPreference(this,
                    GlobalMethods.STRING_PREFERENCE,
                    AppConstants.UNIVERSITY_MODE, AppConstants.FAILURE_CODE);

        } else {
            mTermImg.setTag(getString(R.string.off));
            mTermImg.setImageResource(R.drawable.term_off);

            GlobalMethods.storeValuetoPreference(this,
                    GlobalMethods.STRING_PREFERENCE,
                    AppConstants.UNIVERSITY_MODE, AppConstants.SUCCESS_CODE);
        }
    }

    class TutorialPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        ArrayList<Integer> mImgList;

        public TutorialPagerAdapter(ArrayList<Integer> mList) {
            this.mImgList = mList;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mTutorialImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(View view, final int position) {
            final ViewGroup nullParent = null;
            final View pagerview = inflater.inflate(
                    R.layout.swipe_tutorial_view, nullParent,false);


            ImageView tutorialImg = (ImageView) pagerview.findViewById(R.id.tutorial_img);

            tutorialImg.setBackgroundResource(mTutorialImgList.get(position));
            ((ViewPager) view).addView(pagerview, 0);


            return pagerview;
        }


    }
//    private void showBaseTwoBtnDialog(Context mContext) {
//        final Dialog mDialog = DialogManager.getDialog(mContext, R.layout.dialog_signout_alert);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = mDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
//        //This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        RelativeLayout mCloseLay = (RelativeLayout) mDialog.findViewById(R.id.close_img_lay);
//        TextView mTitleTxt = (TextView) mDialog.findViewById(R.id.header_txt);
//        TextView mMessageTxt = (TextView) mDialog
//                .findViewById(R.id.msg_txt);
//        Button mOkBtn = (Button) mDialog.findViewById(R.id.yes_btn);
//
//        Button mCancelBtn = (Button) mDialog.findViewById(R.id.no_btn);
//
//
//        mOkBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                APIRequestHandler.getInstance().getLogoutResponse(SettingsScreen.this);
//
//            }
//        });
//        mCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDialog.dismiss();
//            }
//        });
//        mCloseLay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//    }


    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof CommonResponse) {
            CommonResponse logoutres = (CommonResponse) responseObj;

            if (logoutres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if (mHandlerBaseAct != null) {
                    mHandlerBaseAct.removeCallbacks(mRunBaseAct);
                }
                if (notifyHandler != null) {
                    notifyHandler.removeCallbacks(notifyCheckingService);
                }
//                DialogManager.showBasicAlertDialog(this, mHeaderTxt.getText().toString(), logoutres.getMessage(), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {

//                GlobalMethods.storeValuetoPreference(this,
//                        GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
//                        "");
                GlobalMethods.storeValuetoPreference(SettingsScreen.this,
                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN,
                        false);
                GlobalMethods.storeValuetoPreference(mActivity,
                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISUSERVERIFIED,
                        false);
                PayPalService.clearAllUserData(SettingsScreen.this);
                GlobalMethods.userDetails(false, "", "", "","", "", "", "", "", "", "", "", "",
                        "", "", "", "", "", this);

//                FacebookSdk.sdkInitialize(getApplicationContext());
//                LoginManager.getInstance().logOut();
                GlobalMethods.setBadge(mActivity, Integer.valueOf
                        (AppConstants.FAILURE_CODE));
                previousScreen(SignInScreen.class, true);
//                    }
//                });


            } else {
                DialogManager.showBasicAlertDialog(this, logoutres.getMessage(), this);
            }
        }
    }

    @Override
    public void onOkClick() {

    }

    @Override
    public void onBackPressed() {


        previousScreen(HomeScreenActivity.class, true);
    }


}