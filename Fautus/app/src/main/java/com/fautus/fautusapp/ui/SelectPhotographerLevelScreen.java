package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceTwoEdtCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * This class implements UI and functions for photographers
 * to select their skill level
 */

public class SelectPhotographerLevelScreen extends BaseActivity {

    /*Variable initialization using bind view*/
    @BindView(R.id.parent_lay)
    ViewGroup mSelectPhoLevelViewGroup;

    @BindView(R.id.select_avid_img)
    ImageView mSelectAvidImg;

    @BindView(R.id.select_skill_img)
    ImageView mSelectSkillImg;

    @BindView(R.id.select_pro_img)
    ImageView mSelectProImg;

    @BindView(R.id.select_avid_pointer_img)
    ImageView mSelectAvidPointerImg;

    @BindView(R.id.select_skill_pointer_img)
    ImageView mSelectSkillPointerImg;

    @BindView(R.id.select_pro_pointer_img)
    ImageView mSelectProPointerImg;

    @BindView(R.id.photographer_skill_txt)
    TextView mPhotographerSkillTxt;


    private int mPhotoMaxSkillLevelInt = 0, mPhotoMinSkillLevelInt = 0;

    private ParseObject mParseSelectPhotoLevelObject;

    private String mBusinessNameStr = "", mWebsiteNameStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_select_photographer_level_screen);
        initView();
    }

    private void initView() {
       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);
        /*Keypad to be hidden when a click/touch made outside the edit text*/
        setupUI(mSelectPhoLevelViewGroup);

         /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(this)) {
            /*Get photographer details from parse DB*/
            getPhotographerDetails();
        } else {
            /*Alert message will be Displayed if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);
        }
    }


    /*View Onclick*/
    @OnClick({R.id.header_left_btn_lay, R.id.select_avid_img, R.id.select_avid_pointer_img, R.id.avid_txt, R.id.select_skill_img,
            R.id.select_skill_pointer_img, R.id.skill_txt, R.id.select_pro_img, R.id.select_pro_pointer_img, R.id.pro_txt, R.id.continue_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.select_avid_img:
            case R.id.select_avid_pointer_img:
            case R.id.avid_txt:

                if (mPhotoMaxSkillLevelInt != 0)
                    setData(0);
                break;
            case R.id.select_skill_img:
            case R.id.select_skill_pointer_img:
            case R.id.skill_txt:

                if (mPhotoMaxSkillLevelInt != 1)
                    setData(1);
                break;
            case R.id.select_pro_img:
            case R.id.select_pro_pointer_img:
            case R.id.pro_txt:

                if (mPhotoMaxSkillLevelInt != 2)
                    setData(2);
                break;
            case R.id.continue_lay:

                /*set advertised skill level*/
                ArrayList<Integer> advertisedSkillLevelStrArr = new ArrayList<>();
                for (int i = mPhotoMinSkillLevelInt; i <= mPhotoMaxSkillLevelInt; i++) {
                    advertisedSkillLevelStrArr.add(i);
                }

                if (mParseSelectPhotoLevelObject == null) {
                    mParseSelectPhotoLevelObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
                }

                mParseSelectPhotoLevelObject.put(user, ParseUser.getCurrentUser());
                mParseSelectPhotoLevelObject.put(ParseAPIConstants.maxSkillLevel, mPhotoMaxSkillLevelInt);
                mParseSelectPhotoLevelObject.put(ParseAPIConstants.advertisedSkillLevel, advertisedSkillLevelStrArr);

                if (mPhotoMaxSkillLevelInt == 2) {
                    DialogManager.getInstance().showPhotographerBusinessAlertPopup(this, mBusinessNameStr, mWebsiteNameStr, new InterfaceTwoEdtCallback() {
                        @Override
                        public void onEdtOneClick(String businessNameStr, String websiteNameStr) {
                            mParseSelectPhotoLevelObject.put(ParseAPIConstants.businessName, businessNameStr);
                            mParseSelectPhotoLevelObject.put(ParseAPIConstants.businessWebsite, websiteNameStr);
                            callAPILevelUpdate();
                        }

                    });
                } else {
                    callAPILevelUpdate();
                }


                break;
        }

    }


    private void callAPILevelUpdate() {

        /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(this)) {
            /*update photographer level to parse DB*/
            APIRequestHandler.getInstance().paresSaveInBackground(mParseSelectPhotoLevelObject, SelectPhotographerLevelScreen.this);
        } else {
              /*Alert message will be displayed if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);
        }
    }


    /*Photographer level parse API callback will be resulted*/
    @Override
    public void onParseRequestSuccess() {
        super.onParseRequestSuccess();

        nextScreen(FreeStrapScreen.class, true);
    }

    /*set photographer data*/
    protected void setData(int skillLevelInt) {
        mPhotoMaxSkillLevelInt = skillLevelInt;
        mSelectAvidImg.setVisibility(skillLevelInt == 0 ? View.VISIBLE : View.INVISIBLE);
        mSelectSkillImg.setVisibility(skillLevelInt == 1 ? View.VISIBLE : View.INVISIBLE);
        mSelectProImg.setVisibility(skillLevelInt == 2 ? View.VISIBLE : View.INVISIBLE);

        mSelectAvidPointerImg.setImageResource(skillLevelInt == 0 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);
        mSelectSkillPointerImg.setImageResource(skillLevelInt == 1 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);
        mSelectProPointerImg.setImageResource(skillLevelInt == 2 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);

        String skillTypeStr = getString(R.string.photographer_type_avid);
        if (skillLevelInt > 0) {
            skillTypeStr = (skillLevelInt == 1 ? getString(R.string.photographer_type_skill) : getString(R.string.photographer_type_pro));
        }
        mPhotographerSkillTxt.setText(skillTypeStr);

    }

    @Override
    public void onBackPressed() {
        /*Redirect to selectEquipment screen*/
        previousScreen(SelectEquipmentScreen.class, true);
    }


    /*Get photographer details from parse DB*/
    private void getPhotographerDetails() {
        /*To Display the progress bar*/
        DialogManager.getInstance().showProgress(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(user, currentUser);
        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject photographerObjectRes, ParseException e) {

                /*hiding teh progress bar*/
                DialogManager.getInstance().hideProgress();
                if (photographerObjectRes == null) {
                    mParseSelectPhotoLevelObject = new ParseObject(ParseAPIConstants.Parse_Photographer);
                } else {
                    mParseSelectPhotoLevelObject = photographerObjectRes;
                }

                if (photographerObjectRes != null) {
                    mBusinessNameStr = photographerObjectRes.getString(ParseAPIConstants.businessName) != null ? photographerObjectRes.getString(ParseAPIConstants.businessName) : "";
                    mWebsiteNameStr = photographerObjectRes.getString(ParseAPIConstants.businessWebsite) != null ? photographerObjectRes.getString(ParseAPIConstants.businessWebsite) : "";
                    List<Integer> advertisedSkillList = photographerObjectRes.getList(ParseAPIConstants.advertisedSkillLevel);
                    mPhotoMinSkillLevelInt = 0;
                    if (advertisedSkillList != null && advertisedSkillList.size() > 0 && photographerObjectRes.getInt(ParseAPIConstants.maxSkillLevel) >= advertisedSkillList.get(0)) {
                        mPhotoMinSkillLevelInt = advertisedSkillList.get(0);
                    }

                    setData(mParseSelectPhotoLevelObject.getInt(ParseAPIConstants.maxSkillLevel));
                }
            }
        });

    }

}
