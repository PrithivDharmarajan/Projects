package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MyOrderAdapter;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.MyOrderRespose;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyOrderFragment extends BaseFragment implements
        DialogMangerCallback {

    private String UserID, mTempPayPrice;
    private ListView favourite_list;
    private ArrayList<HistoryOrderEntity> orderList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_favourite,
                container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
                getActivity());
        setFont(mViewGroup, mTypeface);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());
        AppConstants.FRAG = AppConstants.MAP_SCREEN;
        HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
        UserID = GlobalMethods.getUserID(getActivity());
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                HomeScreen.onBackMove(getActivity());
            }
        });
        orderList = new ArrayList<>();
        initComponents(view);
        callOrderHistoryService(); // API CAll

    }

    private void callOrderHistoryService() {
        DialogManager.showProgress(getActivity());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.Base_Url).build();
        APICommonInterface interfaces = restAdapter
                .create(APICommonInterface.class);

        interfaces.getMyOrders(UserID,
                new Callback<OrderHistoryCommonResponseEntity>() {

                    public void failure(RetrofitError arg0) {
                        DialogManager.hideProgress(getActivity());
                        DialogManager.showCustomAlertDialog(getActivity(),
                                MyOrderFragment.this,
                                getString(R.string.no_network));
                    }

                    public void success(
                            OrderHistoryCommonResponseEntity response,
                            Response obj) {

                        if (response.getStatus().equalsIgnoreCase(
                                AppConstants.SUCCESS_CODE)) {


                            orderList = response.getResult().getOrderResult();

                            MyOrderAdapter adapter = new MyOrderAdapter(MyOrderFragment.this,R.layout.fragment_favourite,orderList);
                            favourite_list.setAdapter(adapter);
                            DialogManager.hideProgress(getActivity());
                        } else {
                            DialogManager.hideProgress(getActivity());

                        }
                    }

                });
    }

    private void initComponents(View view) {

        favourite_list = (ListView) view.findViewById(R.id.favourite_list);
    }

    public void goToOderDetails(HistoryOrderEntity orderDetail) {


        AppConstants.from_my_order = AppConstants.SUCCESS_CODE;
        AppConstants.photo = AppConstants.PHOTO;
        HomeScreen.mHeaderRightLay
                .setVisibility(View.INVISIBLE);
        HomeScreen.mBottombar.setVisibility(View.GONE);
        HomeScreen.mFooterText.setText(R.string.place_order);

        AppConstants.history_prec_img = orderDetail
                .getPrescriptionURL();
        AppConstants.history_mPharmacyName = orderDetail.getShopName();
        AppConstants.history_mPharmacyAddress = orderDetail.getAddress();
        AppConstants.history_mDistance = orderDetail
                .getDeliveryTime();
        AppConstants.history_mShopId = orderDetail
                .getShopID();
        AppConstants.history_mPrescriptionId = orderDetail.getOrderID();
        AppConstants.history_mNotehere = orderDetail.getOrderNote();
        mTempPayPrice = orderDetail.getOrderPrice();

        switch (Integer.parseInt(orderDetail.getOrderStatus())) {
            case -1:
                AppConstants.delivery_status = getString(R.string.status)
                        + " " + getString(R.string.reject);
                break;
            case 0:
                AppConstants.delivery_status = getString(R.string.status)
                        + " " + getString(R.string.in_progress);

                break;
            case 1:
                AppConstants.delivery_status = getString(R.string.pay_rs)
                        + " " + mTempPayPrice;
                GlobalMethods.storeValuetoPreference(
                        getActivity(),
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.IPHARMA_MONEY,
                        mTempPayPrice);
                break;

            case 2:
                AppConstants.delivery_status = getString(R.string.status)
                        + " "
                        + getString(R.string.delivery_started);

                break;
            case 3:
                AppConstants.delivery_status = getString(R.string.status)
                        + " " + getString(R.string.delivered);
                break;
            case 4:
                AppConstants.delivery_status = getString(R.string.status)
                        + " "
                        + getString(R.string.cancel_order);
                break;
            case 5:
                AppConstants.delivery_status = getString(R.string.status)
                        + " "
                        + getString(R.string.payment_over);
                break;
            default:
                break;
        }


        AppConstants.purchase_amount = orderDetail
                .getOrderPrice();


        ((HomeScreen) getActivity()).replaceFragment(
                new OrderDetailsFragment(), true);


    }



    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj != null) {
            MyOrderRespose order = (MyOrderRespose) responseObj;
            if (order.getError_code().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                // mOrderEntity = order.getResult();
                DialogManager.showPopUpDialog(getActivity(), this,
                        order.getMsg());
            }
        }

    }

    // @Override
    // public void onDestroy() {
    // // TODO Auto-generated method stub
    // }

    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }

}
