package com.smaat.ipharma.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.NewOrderEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;
import com.smaat.ipharma.utils.ProfileImageSelectionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;
import static com.smaat.ipharma.utils.AppConstants.DELIVERY_STATUS;
import static com.smaat.ipharma.utils.AppConstants.FROMHISTORY;
import static com.smaat.ipharma.utils.AppConstants.FROM_MY_ORDER;

/**
 * Created by admin on 1/24/2017.
 */

public class OrderDetailFragment extends BaseFragment {

    @BindView(R.id.shop_image)
    ImageView m_shopimage;

    @BindView(R.id.shop_name)
    TextView m_shopName;

    @BindView(R.id.shop_address)
    TextView m_shopAddress;

    @BindView(R.id.del_address)
    EditText m_del_addr;

    @BindView(R.id.note)
    EditText m_note_addr;

    @BindView(R.id.note_title)
    TextView m_note_title;

    @BindView(R.id.change_addr)
    TextView m_Change_addr;




    public static MapPropertyEntity mpentity;

    public static boolean clickable = false;

    private String UserID = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ui_order_detail,
                container, false);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        UserID = GlobalMethods.getUserID(getActivity());
        mpentity = AppConstants.PharmacyDetails;
        initcomponents();
        GlobalMethods.storeAddress(getActivity(),"");
        return rootview;
    }


    private void initcomponents() {
        mpentity =AppConstants.PharmacyDetails;
        if(FROMHISTORY)
        {
            Log.e("Choosed_Ima","Choosed_ImageChoosed_Image"+AppConstants.BASE_URL2 + "/"+ Choosed_Image);
            Glide.with(this).load(AppConstants.BASE_URL2 + "/"
                    +
                    Choosed_Image)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(m_shopimage);
        }else{
            m_shopimage.setImageBitmap(HomeScreen.mSelectedImgBitmap);
        }

        if(FROM_MY_ORDER)
        {
            m_del_addr.setEnabled(false);
            m_del_addr.setBackground(null);
            if(!mpentity.getOrderNote().isEmpty())
            {
                m_note_addr.setText(mpentity.getOrderNote());
                m_note_addr.setVisibility(View.VISIBLE);
                m_note_addr.setBackground(null);
                m_note_title.setVisibility(View.VISIBLE);
            }else{
                m_note_addr.setVisibility(View.GONE);
                m_note_title.setVisibility(View.GONE);
            }
            m_del_addr.setHint("");
        }else{
            m_del_addr.setEnabled(true);
            m_del_addr.setBackgroundResource(R.drawable.txtunderline);
            m_note_addr.setEnabled(true);
            m_note_addr.setVisibility(View.VISIBLE);
            m_note_title.setVisibility(View.VISIBLE);
            m_note_addr.setBackgroundResource(R.drawable.txtunderline);
            m_note_addr.setHint(getString(R.string.enter_note));
            m_del_addr.setHint(getString(R.string.enter_address));
        }
        m_shopName.setText(mpentity.getShopName());
        m_shopAddress.setText(GlobalMethods.capitalizeString(mpentity.getAddress()));
        //m_del_addr.setText(GlobalMethods.getStringValue(getActivity(),AppConstants.COMMUNICATION_ADDRESS));

        m_shopimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!clickable)
                {
                    Intent i = new Intent(getActivity(),ViewShopMapFragment.class);
                    i.putExtra("ShowImage",true);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                    clickable = true;
                }

            }
        });

        m_Change_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddresspopuop(getActivity(),m_del_addr);
            }
        });
    }

    public void LoadPlaceorder()
    {
        APIRequestHandler.getInstance().Placeorder(mpentity.getPharmacyID(), UserID,
                m_del_addr.getText().toString().trim(), m_shopAddress.getText().toString().trim(), m_note_addr.getText().toString().trim(),
                OrderDetailFragment.this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(FROM_MY_ORDER)
        {
            if(DELIVERY_STATUS.equalsIgnoreCase(getActivity().getString(R.string.status)
                    + " " + getActivity().getString(R.string.delivered)))
            {
                ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.or_det),getString(R.string.wr_review));
            }
            else
            {
                ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.or_det),AppConstants.DELIVERY_STATUS);
             }

        }else{
           ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.or_det),getString(R.string.plce_order));
        }




    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof NewOrderEntity) {
            NewOrderEntity resultObj = (NewOrderEntity) responseObj;
            if(resultObj.getMsg().equalsIgnoreCase(AppConstants.SUCCESS))
            {
                GlobalMethods.storeAddress(getActivity(),m_del_addr.getText().toString());
                DialogManager.showSucessPopup(getActivity(), getString(R.string.app_name), getString(R.string.order_dialog_text));
            }
        }else if(responseObj instanceof WriteReviewEntity)
        {
            WriteReviewEntity resultObj = (WriteReviewEntity) responseObj;
            if (resultObj.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
            DialogManager.showMsgPopup(getActivity(),"",resultObj.getMsg());
            }
        }

    }

    public void WriteReview(String reviewMessage,String mRating)
    {
        APIRequestHandler.getInstance().Writereview_api(
                reviewMessage,
                GlobalMethods.getUserID(getActivity()),
                mpentity.getPharmacyID(),mRating,OrderDetailFragment.this);

    }


    public static void showAddresspopuop(final Context mContext,final EditText mDeladdr) {
        final Dialog mDialog = getDialog(mContext, R.layout.ui_address_list);

        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        ListView address_list = (ListView) mDialog.findViewById(R.id.address_list);
        address_list.setVisibility(View.VISIBLE);

        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.adapter_address_list,R.id.addresslist, GlobalMethods.getAddressList(mContext));
        address_list.setAdapter(adapter);
        address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDeladdr.setText(GlobalMethods.getAddressList(mContext).get(position));
                mDialog.dismiss();
            }
        });
        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();


            }
        });

        mDialog.show();
    }

    public static Dialog getDialog(Context mContext, int mLayout) {


        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        return mDialog;
    }



}
