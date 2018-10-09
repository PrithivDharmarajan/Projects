package com.smaat.ipharma.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.adapter.RecentOrderListAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.entity.RecentOrderDetailList;
import com.smaat.ipharma.entity.RecentOrderEntity;
import com.smaat.ipharma.entity.RecentOrderResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 1/24/2017.
 */

public class RecentOrderListFragment extends BaseFragment {
    @BindView(R.id.main_layout)
    LinearLayout m_Mainlayout;



    @BindView(R.id.pharmacy_list)
    ListView m_pharmacylist;

    @BindView(R.id.supportview1)
    View mSupportView1;
    @BindView(R.id.supportview2)
    View mSupportView2;


    @BindView(R.id.internetconnection_layout)
    RelativeLayout internetconnection_layout;

    String UserID = "";



    @BindView(R.id.refresh_btn)
    ImageView refresh_btn;

    @BindView(R.id.completed)
    Button mCompleted;

    @BindView(R.id.ongoing)
    Button mOngoing;

    ArrayList<HistoryOrderEntity> ongoingarray = new ArrayList<>();
    ArrayList<HistoryOrderEntity> completedarray = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ui_recent_order_fragment, container,
                false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);

        UserID = GlobalMethods.getUserID(getActivity());
        APIRequestHandler.getInstance().GetHistory(UserID,internetconnection_layout,m_pharmacylist, RecentOrderListFragment.this);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIRequestHandler.getInstance().GetHistory(UserID,internetconnection_layout,m_pharmacylist, RecentOrderListFragment.this);
            }
        });

        mSupportView1.setBackgroundColor(getResources().getColor(R.color.app_color));
        mSupportView2.setBackgroundColor(getResources().getColor(R.color.whitecolor));

        mOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSupportView1.setBackgroundColor(getResources().getColor(R.color.app_color));
                mSupportView2.setBackgroundColor(getResources().getColor(R.color.whitecolor));

                setListAdapter(ongoingarray);
            }
        });

        mCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSupportView1.setBackgroundColor(getResources().getColor(R.color.whitecolor));
                mSupportView2.setBackgroundColor(getResources().getColor(R.color.app_color));
                setListAdapter(completedarray);
            }
        });


        return rootview;
    }





    public static String getURL(String apiKey, String place) {
        String key = "key=" + apiKey;
        String input = "";
        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String types = "types=geocode";
        String sensor = "sensor=false";
        String parameters = input + "&" + types + "&" + sensor + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameters;
        return url;
    }




    @Override
    public void onRequestSuccess(Object responseObj) {

        if(responseObj instanceof OrderHistoryCommonResponseEntity)
        {
            OrderHistoryCommonResponseEntity mRecentOrderResponse = (OrderHistoryCommonResponseEntity) responseObj;
            if(mRecentOrderResponse.getResult()!=null)
            {
                if(internetconnection_layout.getVisibility()==View.VISIBLE)
                {
                    internetconnection_layout.setVisibility(View.GONE);
                }


                OrderHistoryCommonResponseEntity mOrderResponse=(OrderHistoryCommonResponseEntity)mRecentOrderResponse;
                ongoingarray.clear();
                for(int fl = 0;fl<mOrderResponse.getResult().getOrderResult().size();fl++)
                {
                    if(mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("0")
                            ||mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("1")
                            ||mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("-1")
                            ||mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("2"))
                    ongoingarray.add(mOrderResponse.getResult().getOrderResult().get(fl));
                }

                for(int fl = 0;fl<mOrderResponse.getResult().getOrderResult().size();fl++)
                {
                    if(mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("3")
                            ||mOrderResponse.getResult().getOrderResult().get(fl).getOrderStatus().equalsIgnoreCase("5"))
                        completedarray.add(mOrderResponse.getResult().getOrderResult().get(fl));
                }

                setListAdapter(ongoingarray);
               /* if(ongoingarray.size()>0){
                    m_pharmacylist.setVisibility(View.VISIBLE);
                    setListAdapter(ongoingarray);
                }else{
                    //setListAdapter(mMapResponse.getResult());
                    m_pharmacylist.setVisibility(View.GONE);
                    showMsgPopup(getActivity(),getString(R.string.app_name),getString(R.string.no_orders));
                }*/

            }else{
                //setListAdapter(mMapResponse.getResult());
                m_pharmacylist.setVisibility(View.GONE);
                DialogManager.showMsgPopup(getActivity(),getString(R.string.app_name),getString(R.string.no_orders));
            }
//
//
        }

    }

    private void setListAdapter(ArrayList<HistoryOrderEntity> mRecentOrdreList) {

        if (mRecentOrdreList != null) {
            if(mRecentOrdreList.size()>0)
            {
                RecentOrderListAdapter adapter = new RecentOrderListAdapter(getActivity(),
                        mRecentOrdreList);
                m_pharmacylist.setAdapter(adapter);
            }else{
                showMsgPopup(getActivity(),getString(R.string.app_name),getString(R.string.no_orders));
            }



        }
    }

    public static class GsonTransformer implements Transformer {

        public <T> T transform(String url, Class<T> type, String encoding,
                               byte[] data, AjaxStatus status) {
            Gson g = new Gson();
            return g.fromJson(new String(data), type);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //((HomeScreen) getActivity()).setToolbarTitle(getActivity().getString(R.string.rec_orders),getActivity().getString(R.string.one_touch_order));
        /*if (!GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT).isEmpty()) {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.rec_orders), getString(R.string.one_touch_updated)+" "+
                    GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT));
        } else {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.rec_orders),getString(R.string.one_touch_order));
        }*/
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.rec_orders),getString(R.string.home));

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

}
