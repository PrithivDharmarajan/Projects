package com.bridgellc.bridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.PaymentBuySellEntity;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.ImageViewRounded;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by admin on 6/29/2016.
 */
public class RecentActivityAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    ArrayList<PaymentBuySellEntity> mPaymentList;

    public RecentActivityAdapter(Context context, ArrayList<PaymentBuySellEntity> mPayList) {
        this.context = context;
        this.mPaymentList = mPayList;


    }

    @Override
    public int getCount() {
        return mPaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        gridView = inflater.inflate(R.layout.adapter_paymentact, parent, false);


        RelativeLayout mLeftLay = (RelativeLayout) gridView.findViewById(R.id.left_side_lay);
        RelativeLayout mRightLay = (RelativeLayout) gridView.findViewById(R.id.right_side_lay);

        TextView mLeftPayTxt = (TextView) gridView.findViewById(R.id.left_user_paymet_txt);
        TextView mRightPayTxt = (TextView) gridView.findViewById(R.id.right_user_paymet_txt);

        TextView mLeftUserNameTxt = (TextView) gridView.findViewById(R.id.left_user_name_txt);
        TextView mRightUserNameTxt = (TextView) gridView.findViewById(R.id.right_user_name_txt);

        ImageViewRounded mLeftUserImg = (ImageViewRounded) gridView.findViewById(R.id.left_user_name_img);
        ImageViewRounded mRightUserImg = (ImageViewRounded) gridView.findViewById(R.id.right_user_name_img);

        if (mPaymentList.get(position).getEscrow().equals(context.getString(R.string.one))) {

            mRightPayTxt.setTextColor(context.getResources().getColor(R.color.gray));
            mLeftPayTxt.setTextColor(context.getResources().getColor(R.color.gray));
        } else {
            mLeftPayTxt.setTextColor(context.getResources().getColor(R.color.green));
            mRightPayTxt.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (mPaymentList.get(position).getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(context))) {

            mLeftLay.setVisibility(View.GONE);
            mRightLay.setVisibility(View.VISIBLE);

            mRightUserNameTxt.setText(mPaymentList.get(position).getFirst_name());
            mRightPayTxt.setText(context.getString(R.string.you_paid) + " " + GlobalMethods.getPriValWithTwoPoint(mPaymentList.get(position).getAmount_received(),false));
            Glide.with(context)
                    .load(mPaymentList.get(position).getPicture1())
                    .asBitmap().into(mRightUserImg);


        } else {

            mLeftLay.setVisibility(View.VISIBLE);
            mRightLay.setVisibility(View.GONE);

            mLeftUserNameTxt.setText(mPaymentList.get(position).getFirst_name());
            mLeftPayTxt.setText(context.getString(R.string.paid_you) + " " + GlobalMethods.getPriValWithTwoPoint(mPaymentList.get(position).getAmount_received(),false));
            Glide.with(context)
                    .load(mPaymentList.get(position).getPicture1())
                    .asBitmap().into(mLeftUserImg);


        }
        return gridView;
    }
}
