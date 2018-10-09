package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ParsePhotographerEntity;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.ParseAPIConstants.dateOfBirth;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * This class implements UI and functions for photographers
 * to fill their profile strap details
 */
public class FreeStrapScreen extends BaseActivity implements InterfaceBtnCallback {


    /*Variable initialization using bind view*/
    @BindView(R.id.parent_lay)
    ViewGroup mFreeStrapViewGroup;

    @BindView(R.id.full_name_edt)
    EditText mFullNameEdt;

    @BindView(R.id.address_one_edt)
    EditText mAddressOneEdt;

    @BindView(R.id.address_two_edt)
    EditText mAddressTwoEdt;

    @BindView(R.id.city_edt)
    EditText mCityEdt;

    @BindView(R.id.state_edt)
    EditText mStateEdt;

    @BindView(R.id.zip_edt)
    EditText mZipEdt;

    @BindView(R.id.country_txt)
    TextView mCountryTxt;

    private ParseObject mParseProfileObject;

    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_free_strap_screen);
        initView();
    }

    private void initView() {
       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mFreeStrapViewGroup);

         /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(this)) {
            /*Get photographer Free strap from parse DB*/
            getPhotographerDetails();
        } else {
            /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);
        }

        /*Keypad button action*/
        mZipEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });


    }


    /*View onClick*/
    @OnClick({R.id.header_left_btn_lay, R.id.continue_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.continue_lay:
                validateFields();
                break;

        }

    }

    /*Validate edit text and then call API*/
    private void validateFields() {

        /*hiding keypad for user interaction*/
        hideSoftKeyboard(this);

        String fullNameStr = mFullNameEdt.getText().toString().trim();
        String addressOneStr = mAddressOneEdt.getText().toString().trim();
        String addressTwoStr = mAddressTwoEdt.getText().toString().trim();
        String cityStr = mCityEdt.getText().toString().trim();
        String stateStr = mStateEdt.getText().toString().trim();
        String zipStr = mZipEdt.getText().toString().trim();


        if (fullNameStr.isEmpty()) {
            mFullNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_name_topic), getString(R.string.req_name_strap_msg), FreeStrapScreen.this);
        } else if (addressOneStr.isEmpty()) {
            mAddressOneEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_address_one_topic), getString(R.string.req_address_one_strap_msg), FreeStrapScreen.this);
        } else if (cityStr.isEmpty()) {
            mCityEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_city_topic), getString(R.string.req_city_strap_msg), FreeStrapScreen.this);
        } else if (stateStr.isEmpty()) {
            mStateEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_state_topic), getString(R.string.req_state_strap_msg), FreeStrapScreen.this);
        } else if (zipStr.isEmpty()) {
            mZipEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_zip_topic), getString(R.string.req_zip_strap_msg), FreeStrapScreen.this);
        } else {

            /*API call for freeStrap data*/
            if (mParseProfileObject == null) {
                mParseProfileObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
            }
            mParseProfileObject.put(user, ParseUser.getCurrentUser());
            mParseProfileObject.put(ParseAPIConstants.fullName, fullNameStr);
            mParseProfileObject.put(ParseAPIConstants.address1, addressOneStr);
            mParseProfileObject.put(ParseAPIConstants.address2, addressTwoStr);
            mParseProfileObject.put(ParseAPIConstants.city, cityStr);
            mParseProfileObject.put(ParseAPIConstants.state, stateStr);
            mParseProfileObject.put(ParseAPIConstants.postalCode, zipStr);

             /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(this)) {
                //Free strap API Call
                saveStripeVerificationInfo(mParseProfileObject);
            } else {
                /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

            }

        }
    }

    /*Free strap parse API callback will be resulted*/
    @Override
    public void onParseRequestSuccess() {
        super.onParseRequestSuccess();
        if (AppConstants.PROFILE_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            nextScreen(HomeScreen.class, true);
        } else {

            DialogManager.getInstance().showBasicInfoAlertPopup(this, getString(R.string.congratulations), getString(R.string.sign_up_entry_msg), getResources().getString(R.string.cancel_underline), true, false, new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                /*redirect to home screen*/
                    nextScreen(HomeScreen.class, true);
                }

                @Override
                public void onNoClick() {

                }
            });
        }
    }

    /*Set user details*/
    private void setData(ParsePhotographerEntity details) {
        mFullNameEdt.setText(details.getFullName());
        mAddressOneEdt.setText(details.getAddress1());
        mAddressTwoEdt.setText(details.getAddress2());
        mCityEdt.setText(details.getCity());
        mStateEdt.setText(details.getState());
        mZipEdt.setText(details.getLangugages());
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }

    @Override
    public void onBackPressed() {
        /*redirect to previous screen*/
        previousScreen(SelectPhotographerLevelScreen.class, true);
    }

    /*Get photographer's details from parse DB*/
    private void getPhotographerDetails() {

        DialogManager.getInstance().showProgress(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(user, currentUser);
        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject photographerObjectRes, ParseException e) {

                DialogManager.getInstance().hideProgress();
                if (photographerObjectRes == null) {
                    mParseProfileObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
                } else {
                    mParseProfileObject = photographerObjectRes;
                }
                if (photographerObjectRes != null) {
                    ParsePhotographerEntity parsePhotographerEntity = new ParsePhotographerEntity();
                    parsePhotographerEntity.setFullName(mParseProfileObject.getString(ParseAPIConstants.fullName));
                    parsePhotographerEntity.setAddress1(mParseProfileObject.getString(ParseAPIConstants.address1));
                    parsePhotographerEntity.setAddress2(mParseProfileObject.getString(ParseAPIConstants.address2));
                    parsePhotographerEntity.setCity(mParseProfileObject.getString(ParseAPIConstants.city));
                    parsePhotographerEntity.setState(mParseProfileObject.getString(ParseAPIConstants.state));
                    parsePhotographerEntity.setLangugages(mParseProfileObject.getString(ParseAPIConstants.postalCode));


                    /*To check if the value is empty*/
                    if (!parsePhotographerEntity.getFullName().isEmpty() || !parsePhotographerEntity.getAddress1().isEmpty()
                            || !parsePhotographerEntity.getAddress2().isEmpty() || !parsePhotographerEntity.getCity().isEmpty()
                            || !parsePhotographerEntity.getState().isEmpty() || !parsePhotographerEntity.getPostalCode().isEmpty()) {
                        setData(parsePhotographerEntity);
                    }
                }
            }
        });

    }

    private void saveStripeVerificationInfo(ParseObject photographerObj) {
        DialogManager.getInstance().showProgress(this);

        String userName[] = photographerObj.getString(ParseAPIConstants.fullName).split("\\s+");
        LinkedHashMap<String, Object> saveStripeDetailsHashMap = new LinkedHashMap<>();
        saveStripeDetailsHashMap.put(ParseAPIConstants.type, ParseAPIConstants.individual);
        saveStripeDetailsHashMap.put(ParseAPIConstants.first_name, userName[0]);
        saveStripeDetailsHashMap.put(ParseAPIConstants.last_name, userName[userName.length - 1]);
        saveStripeDetailsHashMap.put(ParseAPIConstants.address, address(photographerObj));
        saveStripeDetailsHashMap.put(ParseAPIConstants.dob, dateOfBirth(photographerObj));
        saveStripeDetailsHashMap.put(ParseAPIConstants.ssn_last_4, photographerObj.getString(ParseAPIConstants.last4SSN));

        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_saveStripePhotographerVerificationInfo, saveStripeDetailsHashMap, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    APIRequestHandler.getInstance().paresSaveInBackground(mParseProfileObject, FreeStrapScreen.this);
                } else {
                    DialogManager.getInstance().hideProgress();
                    try {
                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                        String messageStr = errorJsonObj.getString(getString(R.string.message));
                        DialogManager.getInstance().showAlertPopup(FreeStrapScreen.this, getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), FreeStrapScreen.this);

                    } catch (Exception ex) {
                        DialogManager.getInstance().showAlertPopup(FreeStrapScreen.this, getString(R.string.app_name), e.getMessage(), FreeStrapScreen.this);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                 }
            }
        });
    }

    private LinkedHashMap address(ParseObject photographerObj) {
        LinkedHashMap<String, Object> addressHashMap = new LinkedHashMap<>();
        addressHashMap.put(ParseAPIConstants.city, photographerObj.getString(ParseAPIConstants.city) != null ? photographerObj.getString(ParseAPIConstants.city) : "");
        addressHashMap.put(ParseAPIConstants.state, photographerObj.getString(ParseAPIConstants.state) != null ? photographerObj.getString(ParseAPIConstants.state) : "");
        addressHashMap.put(ParseAPIConstants.line1, photographerObj.getString(ParseAPIConstants.address1) != null ? photographerObj.getString(ParseAPIConstants.address1) : "");
        addressHashMap.put(ParseAPIConstants.line2, photographerObj.getString(ParseAPIConstants.address2) != null ? photographerObj.getString(ParseAPIConstants.address2) : "");
        addressHashMap.put(ParseAPIConstants.postal_code, photographerObj.getString(ParseAPIConstants.postalCode) != null ? photographerObj.getString(ParseAPIConstants.postalCode) : "");
        addressHashMap.put(ParseAPIConstants.country, ParseAPIConstants.US);
        return addressHashMap;
    }

    private LinkedHashMap dateOfBirth(ParseObject photographerObj) {
        LinkedHashMap<String, Object> dobHashMap = new LinkedHashMap<>();
        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM", Locale.US), dayDateFormat = new SimpleDateFormat("dd", Locale.US), yearDateFormat = new SimpleDateFormat("yyyy", Locale.US);

        String dateStr = (mParseProfileObject.getDate(dateOfBirth) != null ? DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(mParseProfileObject.getDate(dateOfBirth), mServerDateFormat), mServerDateFormat, mLocalDateFormat) : "");
        if (!dateStr.isEmpty()) {
            dobHashMap.put(ParseAPIConstants.day, DateUtil.getCustomDateFormat(dateStr, mLocalDateFormat, dayDateFormat));
            dobHashMap.put(ParseAPIConstants.month, DateUtil.getCustomDateFormat(dateStr, mLocalDateFormat, monthDateFormat));
            dobHashMap.put(ParseAPIConstants.year, DateUtil.getCustomDateFormat(dateStr, mLocalDateFormat, yearDateFormat));
        }
        return dobHashMap;
    }
}
