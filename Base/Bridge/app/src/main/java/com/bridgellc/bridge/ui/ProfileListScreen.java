package com.bridgellc.bridge.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.ProfileListAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.ProfileListEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.ProfileListResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.FullWrapGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ProfileListScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private LinearLayout mSellingLay;
    private ArrayList<String> mProfileList;
    private ProfileListEntity mProListRes;
    private ArrayList<HomeSingleItemEntity> mProItemRes;
    private int j = 0;

    private String otherUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling_list_screen);
//        setStatusBarColor(getResources().getColor(R.color.status_gray_color));
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);


        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mSellingLay = (LinearLayout) findViewById(R.id.list_lay);

        mHeaderTxt.setText(AppConstants.PROFILE_HEADER.toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);


        if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_selling))) {
            mProfileList = new ArrayList<>(Arrays.asList(getResources()
                    .getStringArray(R.array.selling_item_list)));
            APIRequestHandler.getInstance().getMySellingResponse(this);

        } else if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_buying))) {
            mProfileList = new ArrayList<>(Arrays.asList(getResources()
                    .getStringArray(R.array.buyer_item_list)));
            APIRequestHandler.getInstance().getMyBuyingResponse(this);

        } else if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_req))) {

            mProfileList = new ArrayList<>(Arrays.asList(getResources()
                    .getStringArray(R.array.request_item_list)));
            APIRequestHandler.getInstance().getMyRequestResponse(this);
        } else {
            mProfileList = new ArrayList<>(Arrays.asList(getResources()
                    .getStringArray(R.array.bidding_item_list)));
            APIRequestHandler.getInstance().getMyBiddingsResponse(this);

        }

    }

    private void setData() {
        if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_selling))) {


//            if (mProListRes != null) {
            mProItemRes = mProListRes.getUploaded();
            if (nullCheck(mProItemRes)) {
                j = 0;

                setAdapterView();
            } else {
                j = 0;
                setNullView();
            }
            mProItemRes = mProListRes.getApproved();
            if (nullCheck(mProItemRes)) {
                j = 1;
                setAdapterView();
            } else {
                j = 1;
                setNullView();
            }
            mProItemRes = mProListRes.getNegotiation();
            if (nullCheck(mProItemRes)) {
                j = 2;
                setAdapterView();
            } else {
                j = 2;
                setNullView();
            }
            mProItemRes = mProListRes.getHistory();
            if (nullCheck(mProItemRes)) {
                j = 3;
                setAdapterView();
            } else {
                j = 3;
                setNullView();
            }
//            }

        } else if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_buying))) {
//            if (mProListRes != null) {
            mProItemRes = mProListRes.getBuying_order();
            if (nullCheck(mProItemRes)) {
                j = 0;
                setAdapterView();
            } else {
                j = 0;
                setNullView();
            }
            mProItemRes = mProListRes.getNegotiation();
            if (nullCheck(mProItemRes)) {
                j = 1;
                setAdapterView();
            } else {
                j = 1;
                setNullView();
            }
            mProItemRes = mProListRes.getHistory();
            if (nullCheck(mProItemRes)) {
                j = 2;
                setAdapterView();
            } else {
                j = 2;
                setNullView();
            }
//            }
        } else if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_req))) {
//            if (mProListRes != null) {
            mProItemRes = mProListRes.getUploaded();
            if (nullCheck(mProItemRes)) {
                j = 0;
                setAdapterView();
            } else {
                j = 0;
                setNullView();
            }
            mProItemRes = mProListRes.getRequest_bid();
            if (nullCheck(mProItemRes)) {
                j = 1;
                setAdapterView();
            } else {
                j = 1;
                setNullView();
            }
            mProItemRes = mProListRes.getHistory();
            if (nullCheck(mProItemRes)) {
                j = 2;
                setAdapterView();
            } else {
                j = 2;
                setNullView();
            }
//            }
        } else if (AppConstants.PROFILE_HEADER.equalsIgnoreCase(getString(R.string.my_bidding))) {
//            if (mProListRes != null) {
            mProItemRes = mProListRes.getRequest_bid();
            if (nullCheck(mProItemRes)) {
                j = 0;
                setAdapterView();
            } else {
                j = 0;
                setNullView();
            }
            mProItemRes = mProListRes.getHistory();
            if (nullCheck(mProItemRes)) {
                j = 1;
                setAdapterView();
            } else {
                j = 1;
                setNullView();
            }

//            }
        }
    }

    private boolean nullCheck(ArrayList<HomeSingleItemEntity> mProRes) {
        boolean mReturn = false;
        if (mProRes != null && mProRes.size() > 0) {
            mReturn = true;
        }

        return mReturn;
    }

    private void setAdapterView() {

        LayoutInflater view = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        View v = view.inflate(R.layout.ui_sticky_header_view, nullParent,false);

        TextView ListName, ItemName, mNoItems;
        ListName = (TextView) v.findViewById(R.id.list_name);
        ItemName = (TextView) v.findViewById(R.id.item_name);
        mNoItems = (TextView) v.findViewById(R.id.no_items_txt);

        mNoItems.setVisibility(View.GONE);
        ListName.setText(mProfileList.get(j));
        ItemName.setText(itemCount(String.valueOf(mProItemRes.size())));

        mNoItems.setTypeface(mLightFont);
        ListName.setTypeface(mLightFont);
        ItemName.setTypeface(mLightFont);

        FullWrapGridView mHomeGridView = (FullWrapGridView) v.findViewById(R.id.gridView);
        ProfileListAdapter mHomeAdapter = new ProfileListAdapter(this, mProItemRes, mProfileList.get(j));
        mHomeGridView.setAdapter(mHomeAdapter);
        mSellingLay.addView(v);

    }

    private void setNullView() {

        LayoutInflater view = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        View v = view.inflate(R.layout.ui_sticky_header_view, nullParent,false);

        TextView ListName, ItemName, mNoItems;
        FullWrapGridView mHomeGridView;

        ListName = (TextView) v.findViewById(R.id.list_name);
        ItemName = (TextView) v.findViewById(R.id.item_name);
        mNoItems = (TextView) v.findViewById(R.id.no_items_txt);


        mNoItems.setTypeface(mLightFont);
        ListName.setTypeface(mLightFont);
        ItemName.setTypeface(mLightFont);

        mHomeGridView = (FullWrapGridView) v.findViewById(R.id.gridView);
        mNoItems.setVisibility(View.VISIBLE);
        mHomeGridView.setVisibility(View.GONE);

        ListName.setText(mProfileList.get(j));
        ItemName.setText(0 + " " + getString(R.string.ite));

        mSellingLay.addView(v);

    }

    private String itemCount(String mItem) {
        String returnItem = mItem + " " + getString(R.string.items);

        if (mItem.equalsIgnoreCase(getString(R.string.zero)) || mItem.equalsIgnoreCase(getString(R.string.one)))
            returnItem = mItem + " " + getString(R.string.ite);
        return returnItem;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previousScreen(DashboardScreen.class, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ProfileListResponse) {
            ProfileListResponse profilelistres = (ProfileListResponse) responseObj;
            if (profilelistres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mProListRes = profilelistres.getResult();
                setData();

            } else {
                DialogManager.showBasicAlertDialog(this,
                        profilelistres.getMessage(), this);
            }
        }
    }

    @Override
    public void onOkClick() {

    }
}
