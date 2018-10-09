package com.smaat.ipharma.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RecentOrderEntity;
import com.smaat.ipharma.fragments.OrderDetailFragment;
import com.smaat.ipharma.fragments.ShopdetailFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;
import static com.smaat.ipharma.utils.AppConstants.DELIVERY_STATUS;
import static com.smaat.ipharma.utils.AppConstants.FROMHISTORY;
import static com.smaat.ipharma.utils.AppConstants.FROM_MY_ORDER;
import static com.smaat.ipharma.utils.AppConstants.PHARMACY_ID;

/**
 * Created by admin on 1/24/2017.
 */

public class RecentOrderListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private ArrayList<HistoryOrderEntity> mRecentOrderList;
    public static Dialog mDialog;
    public static String mShopId, mUserID;
    public static int mSelectPos;

    String deliver_status = "";

    public RecentOrderListAdapter(Context context,
                                  ArrayList<HistoryOrderEntity> mRecentList) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mRecentOrderList = mRecentList;
        mUserID = ((String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID));
    }

    public class Holder {
        ImageView pharmacy_image;
        TextView pharmacy_name;
        TextView status_text;
        TextView distance_txt;
        RatingBar ratinglayout;
        TextView order_id;
        LinearLayout click_item;
        LinearLayout pharmacy_rat;
        TextView mExpectDate;
        TextView comment_note;


    }

    public View getView(final int position, View row, ViewGroup parent) {

        View convertView = row;
        Holder mholder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_recent_order_list, parent, false);
            mholder = new Holder();
            mholder.pharmacy_image = (ImageView) convertView
                    .findViewById(R.id.pharmacy_image);
            mholder.pharmacy_name = (TextView) convertView
                    .findViewById(R.id.pharmacy_name);
            mholder.status_text = (TextView) convertView
                    .findViewById(R.id.staus_textview);
            mholder.order_id = (TextView) convertView
                    .findViewById(R.id.order_id);
            mholder.mExpectDate = (TextView) convertView
                    .findViewById(R.id.expect_date);
            mholder.comment_note = (TextView) convertView
                    .findViewById(R.id.comment_note);



            mholder.click_item = (LinearLayout) convertView
                    .findViewById(R.id.click_item);


            convertView.setTag(mholder);
        } else {
            mholder = (Holder) convertView.getTag();
        }
        final HistoryOrderEntity tempData = mRecentOrderList.get(position);
        String url = "";
        if (tempData.getShopIcon() != null && !tempData.getShopIcon().trim().isEmpty()) {
            url = AppConstants.ADMIN_BASE_URL + tempData.getShopIcon();
        } else {
            url = AppConstants.ADMIN_BASE_URL + tempData.getProfilePic();
        }

        mholder.pharmacy_name.setText(GlobalMethods.capitalizeString(tempData.getShopName()));
        mholder.order_id.setText(mContext.getString(R.string.order_id)+" "+tempData.getOrderID());
        if(tempData.getOrderComments()!=null)
        {
            mholder.comment_note.setText("Note:"+" "+tempData.getOrderComments());
        }else{

            mholder.comment_note.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.popup_logo)
                .into(mholder.pharmacy_image);


        if (tempData.getOrderStatus() != null)

            switch (tempData.getOrderStatus()) {

                case "0":
                    deliver_status = mContext.getString(R.string.in_progress);
                    mholder.mExpectDate.setText("");
                    break;
                case "1":
                    deliver_status = mContext.getString(R.string.action_required);
                    mholder.mExpectDate.setText(mContext.getString(R.string.amnt_paid) + " "
                            + tempData.getOrderPrice());

                    break;
                case "-1":
                    deliver_status = mContext.getString(R.string.reject);
                    mholder.mExpectDate.setText("");
                    break;
                case "2":
                    deliver_status = mContext.getString(R.string.delivery_started);
                    mholder.mExpectDate.setText(mContext.getString(R.string.expected_status)
                            + " " + tempData.getDeliveryTime() + " ");

                    break;
                case "3":
                    deliver_status = mContext.getString(R.string.delivered);
                    mholder.mExpectDate.setText("");
                    break;
                case "4":
                    deliver_status = mContext.getString(R.string.cancel_order);
                    mholder.mExpectDate.setText("");
                    break;
                case "5":
                    deliver_status = mContext.getString(R.string.payment_over);
                    mholder.mExpectDate.setText("");
                    break;


            }
        mholder.status_text.setText(deliver_status);
        mholder.click_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DELIVERY_STATUS = mContext.getString(R.string.status)
                        + " " + mContext.getString(R.string.delivered);*/
                switch (Integer.parseInt(tempData.getOrderStatus())) {
                    case -1:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " " + mContext.getString(R.string.reject);
                        break;
                    case 0:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " " + mContext.getString(R.string.in_progress);

                        break;
                    case 1:
                        DELIVERY_STATUS = mContext.getString(R.string.pay_rs)
                                + " " + tempData.getOrderPrice();
                        GlobalMethods.storeValuetoPreference(
                                mContext,
                                GlobalMethods.STRING_PREFERENCE,
                                AppConstants.IPHARMA_MONEY,
                                tempData.getOrderPrice());
                        break;

                    case 2:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " "
                                + mContext.getString(R.string.delivery_started);

                        break;
                    case 3:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " " + mContext.getString(R.string.delivered);
                        break;
                    case 4:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " "
                                + mContext.getString(R.string.cancel_order);
                        break;
                    case 5:
                        DELIVERY_STATUS = mContext.getString(R.string.status)
                                + " "
                                + mContext.getString(R.string.payment_over);
                        break;
                    default:
                        break;
                }


                MapPropertyEntity ppty = new MapPropertyEntity();
                ppty.setShopIcon(tempData.getShopIcon());
                ppty.setShopName(tempData.getShopName());
                ppty.setAddress(tempData.getAddress());
                ppty.setEmail(tempData.getEmail());
                ppty.setWebsite(tempData.getWebsite());
                ppty.setPhone(tempData.getPhone());
                ppty.setPharmacyID(tempData.getShopID());
                ppty.setOrderNote(tempData.getOrderNote());
                Choosed_Image = tempData.getPrescriptionURL();
                FROM_MY_ORDER = true;
                FROMHISTORY = true;
                PHARMACY_ID = tempData.getShopID();
                AppConstants.PharmacyDetails = ppty;
                ((HomeScreen) mContext).pushFragment(new OrderDetailFragment());
            }
        });

        return convertView;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRecentOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}
