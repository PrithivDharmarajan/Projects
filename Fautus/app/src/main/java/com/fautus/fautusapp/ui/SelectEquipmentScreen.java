package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ParsePhotographerEntity;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * This class implements UI and Functions for photographer to fill their profile details

 */
public class SelectEquipmentScreen extends BaseActivity {


    /*Variable initialization using bind view*/
    @BindView(R.id.parent_lay)
    ViewGroup mSelectEqViewGroup;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.brand_spinner)
    Spinner mBrandSpinner;

    @BindView(R.id.model_spinner)
    Spinner mModelSpinner;

    @BindView(R.id.other_camera_model_edt)
    EditText mOtherCameraModelEdt;

    private LinkedHashMap<String, ArrayList<String>> mBrandModelHasMap = new LinkedHashMap<>();
    private ArrayList<String> mBrandStrArrList = new ArrayList<>();
    private ParseObject mParseSelectEqObject;
    private String mCameraBrandStr = AppConstants.FAILURE_CODE, mCameraModelStr = AppConstants.FAILURE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_select_equipment_screen);
        initView();
    }

    private void initView() {
       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

         /*Keypad to be hidden when a click/touch made outside the edit text*/
        setupUI(mSelectEqViewGroup);

        /*Set header data*/
        mHeaderTxt.setText(getString(R.string.select_equipment).toUpperCase(Locale.US));

        /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(this)) {
            /*Get photographer details from parse DB*/
            getPhotographerDetails();
        } else {
            /*Alert message will be appreared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

        }
    }


    private void callAPIBrandsAndModel() {
        ParseQuery<ParseObject> cameraQuery = ParseQuery.getQuery(ParseAPIConstants.Cameras);
        cameraQuery.setLimit(1000);
        cameraQuery.addAscendingOrder(ParseAPIConstants.Brand);
        cameraQuery.whereEqualTo(ParseAPIConstants.Type, ParseAPIConstants.DSLR);

         /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(this)) {
            /*Get photographer's equipment details*/
            APIRequestHandler.getInstance().parseFindInBackgroundList(cameraQuery, this);
        } else {
            /*Alert message will be appreared if there is no internet connection*/
            DialogManager.getInstance().hideProgress();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

        }

    }


    /*Equipment parse API callback will be resulted*/
    @Override
    public void onParseSuccess(List<ParseObject> parseObjectList) {
        super.onParseSuccess(parseObjectList);

        ArrayList<String> modelArrList = new ArrayList<>();
        modelArrList.add(getString(R.string.model));
        mBrandModelHasMap.put(getString(R.string.brand), modelArrList);

        for (int i = 0; i < parseObjectList.size(); i++) {

            String brandName = parseObjectList.get(i).getString(ParseAPIConstants.Brand);

            if (mBrandModelHasMap.containsKey(brandName)) {
                modelArrList = mBrandModelHasMap.get(brandName);
                modelArrList.add(parseObjectList.get(i).getString(ParseAPIConstants.Model));

            } else {
                modelArrList = new ArrayList<>();
                modelArrList.add(getString(R.string.model));
                modelArrList.add(parseObjectList.get(i).getString(ParseAPIConstants.Model));
                mBrandModelHasMap.put(brandName, modelArrList);
            }
        }

        /*Set Spinner data*/
        setBrandAndModelSpinnerData();
    }


    @Override
    public void onParseRequestFailure(@NonNull ParseException e) {
        super.onParseRequestFailure(e);
        DialogManager.getInstance().hideProgress();
    }


    private void setBrandAndModelSpinnerData() {

        mBrandStrArrList = new ArrayList<>();
        for (String key : mBrandModelHasMap.keySet()) {
            mBrandStrArrList.add(key);
        }

        setAdapter(mBrandStrArrList, mBrandSpinner);
        if (mBrandStrArrList.size() > 0) {
            mBrandSpinner.setSelection(0, true);
        }


        /*set camera brand*/
        if (!mCameraBrandStr.isEmpty() && !mCameraBrandStr.equals(AppConstants.FAILURE_CODE)) {
            for (int cameraBrandPos = 0; mBrandStrArrList.size() > 0; cameraBrandPos++) {
                if (mCameraBrandStr.equals(mBrandStrArrList.get(cameraBrandPos))) {
                    mBrandSpinner.setSelection(cameraBrandPos, true);
                    mCameraBrandStr = AppConstants.FAILURE_CODE;

                    break;
                }
            }
        }

        /*set camera model*/
        if (!mCameraModelStr.isEmpty() && !mCameraModelStr.equals(AppConstants.FAILURE_CODE)) {
            int s = mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())).size();
            for (int cameraModelPos = 0; mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())).size() > 0; cameraModelPos++) {
                if (s > cameraModelPos) {
                    if (mCameraModelStr.equals(mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())).get(cameraModelPos))) {
                        setAdapter(mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())), mModelSpinner);
                        mModelSpinner.setSelection(cameraModelPos, true);
                        mCameraModelStr = AppConstants.FAILURE_CODE;
                        break;
                    }
                } else {
                    setAdapter(mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())), mModelSpinner);
                    break;
                }
            }
        } else {
            setAdapter(mBrandModelHasMap.get(mBrandStrArrList.get(mBrandSpinner.getSelectedItemPosition())), mModelSpinner);
        }
        //Brand Spinner item click
        mBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(SelectEquipmentScreen.this, ((TextView) parent.getChildAt(0)).getText().toString().equalsIgnoreCase(getString(R.string.brand)) ? R.color.gray_edit_hint : R.color.black_txt));
                }
                setAdapter(mBrandModelHasMap.get(mBrandStrArrList.get(position)), mModelSpinner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Model Spinner item click
        mModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(SelectEquipmentScreen.this, ((TextView) parent.getChildAt(0)).getText().toString().equalsIgnoreCase(getString(R.string.model)) ? R.color.gray_edit_hint : R.color.black_txt));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DialogManager.getInstance().hideProgress();
    }


    /*set Spinner Adapter*/
    private void setAdapter(ArrayList<String> spinnerArrList, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectEquipmentScreen.this, R.layout.adap_spinner_equ_selected, spinnerArrList);
        adapter.setDropDownViewResource(R.layout.adap_spinner_equ_list);
        spinner.setAdapter(adapter);
    }


    /*View OnClick*/
    @OnClick({R.id.header_left_btn_lay, R.id.select_brand_lay, R.id.select_model_lay, R.id.continue_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.select_brand_lay:
                break;
            case R.id.select_model_lay:
                break;
            case R.id.continue_lay:
                if (mBrandSpinner.getSelectedItem() != null && mModelSpinner.getSelectedItem() != null) {
                    validateFields();
                } else {
                   /*Check internet connection*/
                    if (NetworkUtil.isNetworkAvailable(this)) {
            /*Get photographer details from parse DB*/
                        getPhotographerDetails();
                    } else {
            /*Alert message will be appreared if there is no internet connection*/
                        DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

                    }
                }
                break;

        }

    }

    /*Validate edit text and then call API*/
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard(this);

        String brandStr = mBrandSpinner.getSelectedItem().toString().trim();
        String modelStr = mModelSpinner.getSelectedItem().toString().trim();
        String otherCameraModelStr = mOtherCameraModelEdt.getText().toString().trim();

        if (otherCameraModelStr.isEmpty() && (brandStr.isEmpty() || brandStr.equals(getString(R.string.brand)))) {

            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_camera_brand_topic), getString(R.string.req_camera_brand_msg), SelectEquipmentScreen.this);
        } else if (otherCameraModelStr.isEmpty() && (modelStr.isEmpty() || modelStr.equals(getString(R.string.model)))) {

            DialogManager.getInstance().showAlertPopup(this, getString(R.string.req_camera_model_topic), getString(R.string.req_camera_model_msg), SelectEquipmentScreen.this);
        } else {
            //Parse API Call
            if (mParseSelectEqObject == null) {
                mParseSelectEqObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
            }
            ParseUser parseUser= ParseUser.getCurrentUser();
            mParseSelectEqObject.put(ParseAPIConstants.user, parseUser);
            if (parseUser.getUsername() != null) {
                mParseSelectEqObject.put(ParseAPIConstants.email, parseUser.getUsername());
            }
            if (otherCameraModelStr.isEmpty()) {
                mParseSelectEqObject.put(ParseAPIConstants.cameraBrand, brandStr);
                mParseSelectEqObject.put(ParseAPIConstants.cameraModel, modelStr);
            } else {
                mParseSelectEqObject.put(ParseAPIConstants.otherCamera, otherCameraModelStr);
                if (!brandStr.isEmpty() && !brandStr.equals(getString(R.string.brand))) {
                    mParseSelectEqObject.put(ParseAPIConstants.cameraBrand, brandStr);
                }
                if (!modelStr.isEmpty() && !modelStr.equals(getString(R.string.model))) {
                    mParseSelectEqObject.put(ParseAPIConstants.cameraModel, modelStr);
                }
            }

             /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(this)) {
            /*update photographer's equipment details*/
                updatePhotographerDetails();
            } else {
            /*Alert message will be appreared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

            }

        }
    }

    /*get photographer details from parse DB*/
    private void getPhotographerDetails() {

        /*Display progress bar*/
        DialogManager.getInstance().showProgress(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(user, currentUser);
        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject photographerObjectRes, ParseException e) {

                if (photographerObjectRes == null) {
                    mParseSelectEqObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
                } else {
                    mParseSelectEqObject = photographerObjectRes;
                }
                if (photographerObjectRes != null) {
                    ParsePhotographerEntity parsePhotographerEntity = new ParsePhotographerEntity();
                    parsePhotographerEntity.setCameraBrand(mParseSelectEqObject.getString(ParseAPIConstants.cameraBrand));
                    parsePhotographerEntity.setCameraModel(mParseSelectEqObject.getString(ParseAPIConstants.cameraModel));
                    parsePhotographerEntity.setOtherCamera(mParseSelectEqObject.getString(ParseAPIConstants.otherCamera));

                    /* To check if the get values are empty*/
                    if (!parsePhotographerEntity.getCameraBrand().isEmpty() || !parsePhotographerEntity.getCameraModel().isEmpty() || !parsePhotographerEntity.getOtherCamera().isEmpty()) {
                        mCameraBrandStr = parsePhotographerEntity.getCameraBrand();
                        mCameraModelStr = parsePhotographerEntity.getCameraModel();
                        mOtherCameraModelEdt.setText(parsePhotographerEntity.getOtherCamera());
                    }
                }
                /*get camera brand and model */
                callAPIBrandsAndModel();
            }
        });

    }

    private void updatePhotographerDetails() {
        DialogManager.getInstance().showProgress(SelectEquipmentScreen.this);
        mParseSelectEqObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                        /*Direct to Photographer level*/
                    nextScreen(SelectPhotographerLevelScreen.class, true);
                } else {
                    DialogManager.getInstance().showAlertPopup(SelectEquipmentScreen.this, getString(R.string.app_name), e.getMessage(), SelectEquipmentScreen.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*Redirect to photographer profile screen*/
        if (AppConstants.PROFILE_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            finishScreen();
        } else {
            previousScreen(PhotographerProfileScreen.class, true);
        }
    }
}
