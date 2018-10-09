package com.smaat.ipharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.fragment.MyOrderFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.RoundEdgeImageView;

import java.util.ArrayList;

public class MyOrderAdapter extends BaseAdapter {
	private int mLayout;
	private Context mContext;
	private ArrayList<HistoryOrderEntity> orderList;
	MyOrderFragment myOrderFragemnt;

	public MyOrderAdapter(MyOrderFragment context, int layout,
						  ArrayList<HistoryOrderEntity> review_list) {
		mContext = context.getContext();
		mLayout = layout;
		orderList = review_list;
		myOrderFragemnt= context;
	}

	class Holder {
		 RoundEdgeImageView pharmacyImage;
		 TextView pharmacyName, mStatus, pharmacyAddress, mOrderedDate,
				mOrderedId, mExpectDate;
		RelativeLayout mOrder_main_lay;

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return orderList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View row=convertView;
		Holder holder;
		if(row==null)
		{
			LayoutInflater inflater= (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.myorder_item, parent, false);

			holder= new Holder();
			holder.pharmacyImage = (RoundEdgeImageView) row
					.findViewById(R.id.pharmacy_image_mask);
			holder.pharmacyName = (TextView) row.findViewById(R.id.pharmacy_name);
			holder.pharmacyAddress = (TextView) row
					.findViewById(R.id.pharmacy_address);
			holder.mOrderedDate = (TextView) row.findViewById(R.id.ordered_date);
			holder.mOrderedId = (TextView) row.findViewById(R.id.ordered_id);
			holder.mStatus = (TextView) row.findViewById(R.id.status);
			holder.mExpectDate = (TextView) row.findViewById(R.id.expect_date);

			holder.pharmacyName.setTypeface(HomeScreen.mHelveticaBold);
			holder.mOrder_main_lay = (RelativeLayout) row
					.findViewById(R.id.order_main_lay);

			row.setTag(holder);

		}
		else
		{
			holder=(Holder)row.getTag();
		}

		HistoryOrderEntity tempData = orderList.get(position);
		holder.mOrder_main_lay.setTag(tempData);

		holder.mOrder_main_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				myOrderFragemnt.goToOderDetails((HistoryOrderEntity)view.getTag());
			}
		});



		Glide.with(mContext).load(AppConstants.Base_Url + "/"
				+
				tempData.getPrescriptionURL())
				.thumbnail(0.5f)
				.crossFade()

				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.pharmacyImage);



		String deliver_status = "";
		switch (Integer.parseInt(tempData.getOrderStatus())) {
			case 0:
				deliver_status = mContext.getString(R.string.in_progress);
				holder.mExpectDate.setText("");

				break;
			case 1:
				deliver_status = mContext.getString(R.string.action_required);
				holder.mExpectDate.setText(mContext.getString(R.string.amnt_paid) + " "
						+ tempData.getOrderPrice());
				break;
			case -1:
				deliver_status = mContext.getString(R.string.reject);
				holder.mExpectDate.setText("");
				break;
			case 2:
				deliver_status = mContext.getString(R.string.delivery_started);
				holder.mExpectDate.setText(mContext.getString(R.string.expected_status)
						+ " " + tempData.getDeliveryTime() + " ");
				// + getString(R.string.min));
				break;
			case 3:
				deliver_status = mContext.getString(R.string.delivered);
				holder.mExpectDate.setText("");
				break;
			case 4:
				deliver_status = mContext.getString(R.string.cancel_order);
				holder.mExpectDate.setText("");
				break;
			case 5:
				deliver_status = mContext.getString(R.string.payment_over);
				holder.mExpectDate.setText("");
				break;
			default:
				break;
		}

		holder.pharmacyName.setText(tempData.getShopName());
		holder.pharmacyAddress.setText(tempData.getAddress()
				+ tempData.getArea()
				+ tempData.getCity() + "-"
				+ tempData.getPincode());
		holder.mOrderedDate.setText(mContext.getString(R.string.ordered_at)
				+ tempData.getOrderDateTime());
		holder.mOrderedId.setText("Order Id" + " : "
				+ tempData.getOrderID());
		holder.mStatus.setText(deliver_status);

		return row;
	}



}
