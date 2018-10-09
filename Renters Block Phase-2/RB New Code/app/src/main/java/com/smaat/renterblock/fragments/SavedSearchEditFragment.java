package com.smaat.renterblock.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.entity.SavedSearchEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SavedSearchEditFragment extends BaseFragment {
    @BindView(R.id.email_toggle_img)
    ImageView mEmailToggleImg;
	
    @BindView(R.id.inquiry_toggle_img)
    ImageView mInquiryToggleImg;
	
    @BindView(R.id.location_txt)
    TextView mLocationTxt;
	
    private boolean mEmailBool, mInquiryBool;
    private String mLocationStr = "";
    private LocalSavedSearchEntity mSavedSearchEntity;
    private FilterEntity mFilterEntity;
    private Dialog mSaveSearchUpdateDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_saved_search_edit, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);


        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.saved_searches), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            initView();
        }
    }

    /*InitViews*/
    private void initView() {
        AppConstants.TAG = this.getClass().getSimpleName();
        if (AppConstants.SELECTED_SAVED_SEARCH_ARRAY != null) {

            /*this Appconstant array would have only one tem,so tried to get 0 position*/
            mSavedSearchEntity = new LocalSavedSearchEntity();
            mSavedSearchEntity = AppConstants.SELECTED_SAVED_SEARCH_ARRAY.get(0);

            mFilterEntity=new FilterEntity();
            mFilterEntity = new Gson().fromJson(mSavedSearchEntity.getFilter_object(), FilterEntity.class);

            mLocationStr = mFilterEntity.getFilter_name();
            mLocationTxt.setText(mLocationStr);

            mEmailToggleImg.setImageResource(mSavedSearchEntity.getEmail_notification().equals(AppConstants.SUCCESS_CODE) ? R.drawable.toggle_on : R.drawable.toggle_off);
            mEmailBool = mSavedSearchEntity.getEmail_notification().equals(AppConstants.SUCCESS_CODE);
            mInquiryToggleImg.setImageResource(mSavedSearchEntity.getInquiry().equals(AppConstants.SUCCESS_CODE) ? R.drawable.toggle_on : R.drawable.toggle_off);
            mInquiryBool = mSavedSearchEntity.getInquiry().equals(AppConstants.SUCCESS_CODE);


        }

    }

    @OnClick({R.id.email_toggle_img, R.id.inquiry_toggle_img, R.id.update_btn, R.id.location_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_txt:
                mSaveSearchUpdateDialog= DialogManager.getInstance().saveSearchDialog(getActivity(), mLocationStr, new InterfaceEdtWithBtnCallback() {

                    @Override
                    public void onFirstEdtBtnClick(String firstEdtStr) {
                        if (!firstEdtStr.isEmpty()) {
                            mLocationStr = firstEdtStr;
                            mLocationTxt.setText(mLocationStr);
                            mFilterEntity.setFilter_name(mLocationStr);
                            mSaveSearchUpdateDialog.dismiss();

                        }

                    }
                });
                break;
            case R.id.email_toggle_img:
                mEmailBool = !mEmailBool;
                mEmailToggleImg.setImageResource(mEmailBool ? R.drawable.toggle_on : R.drawable.toggle_off);
                break;
            case R.id.inquiry_toggle_img:
                mInquiryBool = !mInquiryBool;
                mInquiryToggleImg.setImageResource(mInquiryBool ? R.drawable.toggle_on : R.drawable.toggle_off);

                break;
            case R.id.update_btn:

                /*API call update */
                String emailStr = mEmailBool ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                String inquiryStr = mInquiryBool ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                String filterObj = new Gson().toJson(mFilterEntity, FilterEntity.class);
                APIRequestHandler.getInstance().updateSavedSearchAPICall(SavedSearchEditFragment.this, mSavedSearchEntity.getSave_search_id(), filterObj, mSavedSearchEntity.getFilter_type(), emailStr, inquiryStr);

                break;

        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof SettingResponse) {
            SettingResponse mUpdateAPIResponse = (SettingResponse) resObj;
            if (mUpdateAPIResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), mUpdateAPIResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });

            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
    }
}
