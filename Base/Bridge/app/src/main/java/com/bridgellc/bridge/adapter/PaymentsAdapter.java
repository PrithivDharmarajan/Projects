package com.bridgellc.bridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.PaymentBuySellEntity;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.ImageViewRounded;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Dell on 3/21/2016.
 */
public class PaymentsAdapter extends BaseAdapter {

    private Context context;
    private int btnPos;
    private ArrayList<PaymentBuySellEntity> mPaymentList;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    public PaymentsAdapter(Context context, ArrayList<PaymentBuySellEntity> mPayList, int buttonPos) {
        this.context = context;
        this.mPaymentList = mPayList;
        this.btnPos = buttonPos;

    }

    @Override
    public int getCount() {
        return mPaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPaymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.adapter_paymentlist_row, parent,false);
            TextView textname = (TextView) view.findViewById(R.id.name_txt);
            TextView username = (TextView) view.findViewById(R.id.product_name_txt);
            TextView datetxt = (TextView) view.findViewById(R.id.date_txt);
            TextView productPrice = (TextView) view.findViewById(R.id.product_price);
            ImageViewRounded productImg = (ImageViewRounded) view.findViewById(R.id.product_img);

//            String date[] = mPaymentList.get(position).getDate_time().split("\\s+");
            datetxt.setText(GlobalMethods.getCustomDateFormate(mPaymentList.get(position).getDate_time(), mTargetDateTime));

            textname.setText(mPaymentList.get(position).getItem_name());
            username.setText(mPaymentList.get(position).getFirst_name());
            if (btnPos == 2 && !GlobalMethods.isSeller(context, mPaymentList.get(position).getUser_id())) {
                textname.setTextColor(context.getResources().getColor(R.color.swipe_red));
                username.setTextColor(context.getResources().getColor(R.color.swipe_red));
            }
            try {

                if ((mPaymentList.get(position).getPicture1() != null && !mPaymentList.get(position).getPicture1().equalsIgnoreCase(""))) {
                    Glide.with(context)
                            .load(mPaymentList.get(position).getPicture1())
                            .asBitmap().into(productImg);
                } else if ((mPaymentList.get(position).getPicture2() != null && !mPaymentList.get(position).getPicture2().equalsIgnoreCase(""))) {
                    Glide.with(context)
                            .load(mPaymentList.get(position).getPicture2())
                            .asBitmap().into(productImg);
                } else if ((mPaymentList.get(position).getPicture3() != null && !mPaymentList.get(position).getPicture3().equalsIgnoreCase(""))) {
                    Glide.with(context)
                            .load(mPaymentList.get(position).getPicture3())
                            .asBitmap().into(productImg);
                }
            } catch (Exception e) {

            }
            String productPriceText = "";
            if (mPaymentList.get(position).getItem_cost() != null) {
                productPriceText = "$" + String.format(Locale.ENGLISH,"%.0f", Double.parseDouble(mPaymentList.get(position).getItem_cost()));

            }

            productPrice.setText(productPriceText);
        } else {
            view = (View) convertView;
        }


        return view;
    }


}
