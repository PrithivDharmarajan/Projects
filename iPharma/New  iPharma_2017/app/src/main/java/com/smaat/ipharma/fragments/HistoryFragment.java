package com.smaat.ipharma.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.adapter.SwipeAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;
import static com.smaat.ipharma.utils.AppConstants.SELECTED_POSITION;

/**
 * Created by admin on 1/25/2017.
 */

public class HistoryFragment extends BaseFragment {


    private String UserID = "";
    public static  ArrayList<HistoryOrderEntity> history_list = new ArrayList<HistoryOrderEntity>();

    @BindView(R.id.view_pager)
    ViewPager historyviewpager;

    @BindView(R.id.left_arrow)
    ImageView mleft_arrow;
    @BindView(R.id.right_arrow)
    ImageView mright_arrow;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ui_history_fragment,
                container, false);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        UserID = GlobalMethods.getUserID(getActivity());
        mleft_arrow.setVisibility(View.GONE);
        mright_arrow.setVisibility(View.VISIBLE);
        LoadallHistory();
        historyviewpager.addOnPageChangeListener (mPageLitsener);


        mleft_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyviewpager.setCurrentItem(historyviewpager.getCurrentItem() - 1);
            }
        });

        mright_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyviewpager.setCurrentItem(historyviewpager.getCurrentItem() + 1);
            }
        });
        return rootview;
    }

    private void LoadallHistory() {
        APIRequestHandler.getInstance().GetHistory(UserID,null,null,HistoryFragment.this);
    }


    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof OrderHistoryCommonResponseEntity) {
            history_list.clear();
            OrderHistoryCommonResponseEntity orderhistory = (OrderHistoryCommonResponseEntity)responseObj;
            if(orderhistory.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
            {
            if(orderhistory.getResult().getOrderResult().size()>0)
            {
                history_list = orderhistory.getResult().getOrderResult();
                setImageadapter();
            }else{
                showMsgPopup(getActivity(),"",getString(R.string.no_history));
            }


            }
        }
    }


    public void showMsgPopup(final Context mContext, String title, String msg) {
        mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                getActivity().onBackPressed();


            }
        });

        mDialog.show();
    }


    private void setImageadapter() {
        SwipeAdapter sAdapter = new SwipeAdapter(getActivity(),history_list);
        historyviewpager.setAdapter(sAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getActivity().getString(R.string.history),getActivity().getString(R.string.select_pre));

    }

    private ViewPager.OnPageChangeListener mPageLitsener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            SELECTED_POSITION = position;
            if(position==0)
            {
                mleft_arrow.setVisibility(View.GONE);
                mright_arrow.setVisibility(View.VISIBLE);
            }else if(position==history_list.size()-1){
                mleft_arrow.setVisibility(View.VISIBLE);
                mright_arrow.setVisibility(View.GONE);
            }else{
                mleft_arrow.setVisibility(View.VISIBLE);
                mright_arrow.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };


}
