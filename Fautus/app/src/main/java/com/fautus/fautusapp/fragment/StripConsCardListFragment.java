package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.adapter.CardListAdapter;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.CardDeatilsResponse;
import com.fautus.fautusapp.model.RetrieveStripeCustomerResponse;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 13-May-17.
 */

public class StripConsCardListFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.card_recycler_view)
    RecyclerView mCardRecyclerView;

    private String mStripeCusIdStr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_cons_card_list_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {

        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.pay_method));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();

    }

    private void initView() {
           /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            /*Contact photographer API call*/
            getCardList();
        } else {
            /*Internet alert*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), StripConsCardListFragment.this);
        }


        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }


    private void getCardList() {
        DialogManager.getInstance().showProgress(getActivity());
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                if (e == null && object != null) {
                    mStripeCusIdStr = object.getString(ParseAPIConstants.stripeCustomerId);
                    APIRequestHandler.getInstance().stripCardAPICall(object.getString(ParseAPIConstants.stripeCustomerId), StripConsCardListFragment.this);
                } else if (e != null) {
                    DialogManager.getInstance().hideProgress();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), StripConsCardListFragment.this);
                }
            }
        });
    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RetrieveStripeCustomerResponse) {
            RetrieveStripeCustomerResponse res = (RetrieveStripeCustomerResponse) resObj;
            if (res.getSources() != null && res.getSources().getData() != null && res.getSources().getData().size() > 0) {
                setAdapter(res.getSources().getData(), res.getDefault_source());
            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {

    }

    private void setAdapter(ArrayList<CardDeatilsResponse> cardDetailsResponses, String cardNumStr) {
        CardListAdapter cardListAdapter = new CardListAdapter(getActivity(), cardDetailsResponses, cardNumStr, mStripeCusIdStr, this);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCardRecyclerView.setAdapter(cardListAdapter);
        mCardRecyclerView.setNestedScrollingEnabled(true);
    }

    /*View OnClick*/
    @OnClick({R.id.add_card_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_card_lay:
                AppConstants.READY_TO_PAY_SOURCE = AppConstants.FAILURE_CODE;
                ((HomeScreen) getActivity()).addFragment(new StripConsPaymentFragment());
                break;
        }
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }


}
