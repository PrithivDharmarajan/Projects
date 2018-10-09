package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.SavedSearchAdapter;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.SavedSearchResponse;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SavedSearchFragment extends BaseFragment {
    @BindView(R.id.edit_delete_lay)
    RelativeLayout mBottomEditDeleteLay;
    @BindView(R.id.saved_search_recycler_view)
    RecyclerView mSavedSearchRecyclerView;

    private ArrayList<LocalSavedSearchEntity> mSavedSearchList = new ArrayList<>();
    private boolean isEditView = false;
    private boolean mDeleteBool = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_saved_search, container, false);
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
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_button, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.saved_searches), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            mBottomEditDeleteLay.setVisibility(View.GONE);
            isEditView = false;
            initView();

            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        if (mSavedSearchList.size() == 0) {
                            DialogManager.getInstance().showToast(getActivity(), getString(R.string.alert_no_item));
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 1);
                            mBottomEditDeleteLay.setVisibility(View.GONE);
                        } else if (!isEditView) {
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.close_icon, 1);
                            isEditView = true;
                            setAdapter(true);
                            mBottomEditDeleteLay.setVisibility(View.VISIBLE);
                        } else {
                            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 1);
                            isEditView = false;
                            setAdapter(false);
                            mBottomEditDeleteLay.setVisibility(View.GONE);
                        }
                    }
                }
            });


        }
    }

    /*InitViews*/
    private void initView() {
        AppConstants.TAG = this.getClass().getSimpleName();
        APIRequestHandler.getInstance().savedSearchAPICall(SavedSearchFragment.this);


    }

    /*Click Event*/
    @OnClick({R.id.edit_txt, R.id.delete_txt})
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.edit_txt:
                    if (AppConstants.SELECTED_SAVED_SEARCH_ARRAY != null && AppConstants.SELECTED_SAVED_SEARCH_ARRAY.size() == 0 || AppConstants.SELECTED_SAVED_SEARCH_ARRAY.size() > 1) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.error_msg_select_one_item_edit), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });

                    } else {
                        ((HomeScreen) getActivity()).addFragment(new SavedSearchEditFragment());
                    }
                    break;
                case R.id.delete_txt:
                    if (AppConstants.SELECTED_SAVED_SEARCH_IDS != null && AppConstants.SELECTED_SAVED_SEARCH_IDS.size() == 0) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.error_msg_select_one_item_delete), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });

                    } else {
                        DialogManager.getInstance().showOptionPopup(getActivity(), getString(R.string.alert_delete), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onNegativeClick() {

                            }

                            @Override
                            public void onPositiveClick() {


                            /*Delete API call*/
                                String selectedIDs = TextUtils.join(",", AppConstants.SELECTED_SAVED_SEARCH_IDS);
                                APIRequestHandler.getInstance().deleteSavedSearchAPICall(SavedSearchFragment.this, selectedIDs);

                            }
                        });


                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof SavedSearchResponse) {
            SavedSearchResponse mSavedSearchResponse = (SavedSearchResponse) resObj;
            if (mSavedSearchResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                mSavedSearchList = mSavedSearchResponse.getResult();
                if (!mDeleteBool)
                    setAdapter(false);
                else {
                    setAdapter(true);
                    mDeleteBool = false;
                }

            }

        }
        if (resObj instanceof SettingResponse) {
            SettingResponse mDeleteAPIResponse = (SettingResponse) resObj;
            if (mDeleteAPIResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), mDeleteAPIResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        mDeleteBool = true;
                        APIRequestHandler.getInstance().savedSearchAPICall(SavedSearchFragment.this);

                    }
                });

            }
        }
    }

    private void setAdapter(boolean isEditClicked) {
        if (getActivity() != null) {
            if (mSavedSearchList.size() == 0) {
                DialogManager.getInstance().showToast(getActivity(), getString(R.string.alert_no_item));
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 2);
                mBottomEditDeleteLay.setVisibility(View.GONE);
            }
            SavedSearchAdapter mSavedSearchAdapter = new SavedSearchAdapter(getActivity(), mSavedSearchList, isEditClicked);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
            mSavedSearchRecyclerView.setLayoutManager(manager);
            mSavedSearchRecyclerView.setNestedScrollingEnabled(false);
            mSavedSearchRecyclerView.setFocusable(false);
            mSavedSearchRecyclerView.setAdapter(mSavedSearchAdapter);
        }
    }


    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
    }
}
