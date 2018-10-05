package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.SelectUniversityAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.SignInEntity;
import com.bridgellc.bridge.entity.UniversityEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.UniversityResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class SelectUniversityScreen extends BaseActivity implements DialogMangerOkCallback {


    private SelectUniversityAdapter mAdapter;
    private ListView mList;
    private Button mOkBtn;
    private EditText mSearchText;
    private RelativeLayout mHeaBgLay;
    private ArrayList<UniversityEntity> mUniversityList, mUniverList;
    private SignInEntity mSignin;
    private String mDeviceId;
    public static String mFromSignup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_university_screen);
        initComponents();

        mUniverList = new ArrayList<UniversityEntity>();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeaBgLay = (RelativeLayout) findViewById(R.id.curve_view);
        mOkBtn = (Button) findViewById(R.id.footer_btn);
        mFooterLay = (LinearLayout) findViewById(R.id.footer_parent_lay);

        mSearchText = (EditText) findViewById(R.id.search_text);
        ImageView mSearchImg = (ImageView) findViewById(R.id.search_img);
        mSearchText.setHint(R.string.school_un);

        mHeaBgLay.setVisibility(View.GONE);

        mList = (ListView) findViewById(R.id.list_view);

        mHeaderTxt.setText(getString(R.string.sele_school_unv).toUpperCase(Locale.ENGLISH));
//        mHeaderTxt.setTextSize(18);
        mOkBtn.setText(getString(R.string.ok));
//        if (mFromSignup.equalsIgnoreCase(getString(R.string.update_profile))) {
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
//        } else {
//            mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
//            getGCMId();
//        }
        mHeadeRightBtnLay.setVisibility(View.INVISIBLE);

//        mUniversityList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.university_list)));
//        Collections.sort(mUniversityList,
//                new Comparator<String>() {
//                    @Override
//                    public int compare(String s1, String s2) {
//                        return s1.compareToIgnoreCase(s2);
//                    }
//                });

//        searchSports(mSearchText.getText().toString().trim());
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchUniversity(mSearchText.getText().toString().trim());
                    hideSoftKeyboard(SelectUniversityScreen.this);
                    return true;
                }
                return false;
            }
        });


        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUniversity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchText.getText().toString().length() > 0) {
                    searchUniversity(mSearchText.getText().toString().trim());
                }
            }
        });

        //API Call
        APIRequestHandler.getInstance().getUniversityResponse(this);
    }

    @Override
    public void onBackPressed() {

//        if (mFromSignup.equalsIgnoreCase(getString(R.string.update_profile))) {
        if (AppConstants.SELECT_SCH_UNI.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            AppConstants.SELECT_SCH_UNI = AppConstants.FAILURE_CODE;
            AppConstants.SELECT_SCH_UNI_ID = AppConstants.FAILURE_CODE;


            if (mFromSignup.equalsIgnoreCase(getString(R.string.update_profile))) {
                UpdateProfileScreen.mSchoolUnivTxt.setText("");
                UpdateProfileScreen.mSchoolUnivTxt.setVisibility(View.GONE);
            } else {

                SelectUniversityNewScreen.mSelectUnivTxt.setText("");
            }
        } else {
            if (!AppConstants.SELECT_SCH_UNI.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                if (mFromSignup.equalsIgnoreCase(getString(R.string.update_profile))) {
                    UpdateProfileScreen.mSchoolUnivTxt.setText(AppConstants.SELECT_SCH_UNI);
                    UpdateProfileScreen.mSchoolUnivTxt.setVisibility(View.VISIBLE);

                } else {
                    SelectUniversityNewScreen.mSelectUnivTxt.setText(AppConstants.SELECT_SCH_UNI);

                }
            }
        }
        finishScreen();
//        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_btn:
                if (!AppConstants.SELECT_SCH_UNI.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    if (mFromSignup.equalsIgnoreCase(getString(R.string.update_profile))) {
                        UpdateProfileScreen.mSchoolUnivTxt.setText(AppConstants.SELECT_SCH_UNI);
                        UpdateProfileScreen.mSchoolUnivTxt.setVisibility(View.VISIBLE);

                    } else {

                        SelectUniversityNewScreen.mSelectUnivTxt.setText(AppConstants.SELECT_SCH_UNI);

//                        APIRequestHandler.getInstance().getSignUpResponse(AppConstants.SIGNUP_FIRSTNAME, AppConstants.SIGNUP_LASTNAME, AppConstants.SIGNUP_EMAIL,
//                                GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM), AppConstants.SIGNUP_PWD, AppConstants.SELECT_SCH_UNI_ID, "01-01-1950", getString(R.string.one), GlobalMethods.getStringValue(SelectUniversityScreen.this, AppConstants.LOGINTYPE), mDeviceId, "", getString(R.string.three),
//                                SelectUniversityScreen.this);
//
                    }
                    finishScreen();
                } else {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.enter_university), this);
                }
                break;
        }
    }


    @Override
    public void onOkClick() {

    }

    private void searchUniversity(String mSearchVal) {

        if (mSearchVal.trim().isEmpty()) {
            adapter(mUniversityList);
        } else {
            mUniverList.clear();

            if (mUniversityList != null && mUniversityList.size() > 0) {
                for (int i = 0; i < mUniversityList.size(); i++) {
                    String universit = mUniversityList.get(i).getUniversity_name().toLowerCase(Locale.ENGLISH);
                    if (universit.contains(mSearchVal.toLowerCase(Locale.ENGLISH))) {

                        mUniverList.add(mUniversityList.get(i));

                    }

                    adapter(mUniverList);
                }
            }
        }

    }

    private void adapter(ArrayList<UniversityEntity> mUnvList) {

        mAdapter = new SelectUniversityAdapter(SelectUniversityScreen.this, mUnvList);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof UniversityResponse) {
            UniversityResponse universityres = (UniversityResponse) responseObj;

            if (universityres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if (universityres.getResult() != null) {
                    mUniversityList = universityres.getResult();
                    Collections.sort(mUniversityList,
                            new Comparator<UniversityEntity>() {
                                @Override
                                public int compare(UniversityEntity s1,
                                                   UniversityEntity s2) {
                                    return s1.getUniversity_name().compareToIgnoreCase(
                                            s2.getUniversity_name());
                                }
                            });

                    mAdapter = new SelectUniversityAdapter(SelectUniversityScreen.this, mUniversityList);
                    mList.setAdapter(mAdapter);
                }

            } else {
                DialogManager.showBasicAlertDialog(this, universityres.getMessage(), this);

            }
        }
    }


}




