package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.PropertyAdapter;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PropertyListingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Smaat on 8/18/2017.
 */

public class PropertyListingFragment extends BaseFragment implements PropertyAdapter.DeletePropertyIDsInterface {


    @BindView(R.id.property_list)
    RecyclerView mPropertyRecyclerView;

    @BindView(R.id.edit_delete_lay)
    RelativeLayout mEditDeleteLay;


    private ArrayList<PropertyEntity> mPropertyList = new ArrayList<>();
    private ArrayList<String> mPropIDS = new ArrayList<String>();

    private PropertyAdapter mPropertyAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isEditBool = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_property_listing_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        initView();

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
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyListingFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
//            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(isEditBool ? R.drawable.edit_icon : R.drawable.close_icon, AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyListingFragment? 1 : 2);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, PreferenceUtil.getUserID(getContext()).equals(AppConstants.PROPERTY_LIST_USER_ID) ? 1 : 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_icon, PreferenceUtil.getUserID(getContext()).equals(AppConstants.PROPERTY_LIST_USER_ID) ? 1 : 2,"");
//            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
//            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_icon, AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyListingFragment ? 1 : 0);

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.listing), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        isEditBool = !isEditBool;
                        ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(isEditBool ? R.drawable.edit_icon : R.drawable.close_icon, 1);
                        mEditDeleteLay.setVisibility(isEditBool ? View.GONE : View.VISIBLE);
                        if (isEditBool) {
                            setAdapter();
                        } else {
                            setEditAdapter();
                        }
                    }

                }
            });

            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        AppConstants.PROPERTY_DETAILS = null;
                        AppConstants.ADD_PROPERTY = AppConstants.KEY_TRUE;
                        ((HomeScreen) getActivity()).addFragment(new AddOrEditPropertyFragment());
                    }
                }
            });
            isEditBool = true;
            mPropIDS.clear();
            initView();
        }
    }


    /*InitViews*/
    private void initView() {
        AppConstants.TAG = this.getClass().getSimpleName();
        getPropertyListingApi();

    }


    private void getPropertyListingApi() {
        //String Url = AppConstants.BASE_URL + "getmylisting";
        APIRequestHandler.getInstance().getMyProperties(AppConstants.PROPERTY_LIST_USER_ID, PropertyListingFragment.this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof PropertyListingResponse) {
            PropertyListingResponse response = (PropertyListingResponse) resObj;
            if (response != null) {
                if (response.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    mPropertyList.clear();
                    mPropertyList = response.getResult();
                    if (isEditBool) {
                        setAdapter();
                    } else {
                        setEditAdapter();
                    }
                }
            }
        }
        if (resObj instanceof CommonResponse) {
            CommonResponse response = (CommonResponse) resObj;
            if (response != null) {
                if (response.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    mPropIDS.clear();
                    DialogManager.getInstance().showAlertPopup(getActivity(), response.getMsg(), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            getPropertyListingApi();
                        }
                    });
                }
            }
        }


    }

    private void setAdapter() {
        mPropertyAdapter = new PropertyAdapter(getActivity(), mPropertyList, isEditBool, this);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mPropertyRecyclerView.setLayoutManager(mGridLayoutManager);
        mPropertyRecyclerView.setAdapter(mPropertyAdapter);


    }


    private void setEditAdapter() {
        mPropertyAdapter = new PropertyAdapter(getActivity(), mPropertyList, isEditBool, this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mPropertyRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPropertyRecyclerView.setAdapter(mPropertyAdapter);

    }


    @OnClick({R.id.edit_text, R.id.txtDelete})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.edit_text:
                if (mPropIDS.size() == 0) {
                    DialogManager.getInstance().showAlertPopup(getActivity(),
                            getString(R.string.select_any_property), this);
                } else if (mPropIDS.size() > 1) {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.select_one_property), this);
                } else {
                    AppConstants.ADD_PROPERTY = AppConstants.KEY_FALSE;
                    AppConstants.DETAIL_PROPERTY_ID = mPropIDS.get(0);
                    ((HomeScreen) getActivity()).addFragment(new AddOrEditPropertyFragment());
                }
                break;
            case R.id.txtDelete:
                if (mPropIDS.size() == 0) {
                    DialogManager.getInstance().showAlertPopup(getActivity(),
                            getString(R.string.select_any_property), this);
                } else {
                    String mPropID;
                    if (mPropIDS.size() != 0 && mPropIDS.size() > 1) {
                        mPropID = mPropIDS.toString().replace("[", "").replace("]", "");
                    } else {
                        mPropID = mPropIDS.get(0);
                    }
                    callDeleteAPI(mPropID);
                }


                break;
        }
    }

    public void callDeleteAPI(String propertyIds) {
        // String Url = AppConstants.BASE_URL + "addnewproperty/delete";
        APIRequestHandler.getInstance().propertyDelete(PropertyListingFragment.this, propertyIds);
    }


    @Override
    public void addRemoveIDs(String propid, View view, boolean isSelect, PropertyEntity propertyEntity) {
        AppConstants.PROPERTY_DETAILS = propertyEntity;
        if (isSelect) {
            view.setBackgroundResource(R.drawable.tick_on);
            mPropIDS.add(propid);
        } else {
            view.setBackgroundResource(R.drawable.tick_off);
            mPropIDS.remove(propid);

        }
    }
}
