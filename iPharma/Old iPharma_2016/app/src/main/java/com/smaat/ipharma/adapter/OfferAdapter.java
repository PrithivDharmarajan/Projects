package com.smaat.ipharma.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.fragment.OffersFragment;
import com.smaat.ipharma.fragment.OrderNowFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.RoundEdgeImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OfferAdapter extends BaseAdapter {
	private int mLayout;
	private Context mContext;
	private ArrayList<MapPropertyEntity> orderList;
	OffersFragment myOrderFragemnt;

	public OfferAdapter(OffersFragment context, int layout,
						ArrayList<MapPropertyEntity> review_list) {
		mContext = context.getContext();
		mLayout = layout;
		orderList = review_list;
		myOrderFragemnt= context;
	}

	class Holder {
		RoundEdgeImageView pharmacyImage;
		TextView pharmacyName, pharmacyAddress, pharmacyReviews, pharmacyDistance,
				deliveryTime, MinOrder, mDiscount;
		RatingBar mRating;
		RelativeLayout mFav_lay;
		LinearLayout mOffers_list_item_view;

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
			row=inflater.inflate(R.layout.offers_adapter, parent, false);

			holder= new Holder();
			holder.pharmacyImage = (RoundEdgeImageView) row
					.findViewById(R.id.pharmacy_image_mask);
			holder.pharmacyName = (TextView) row.findViewById(R.id.pharmacy_name);
			holder.pharmacyAddress = (TextView) row
					.findViewById(R.id.pharmacy_address);
			holder.pharmacyReviews = (TextView) row.findViewById(R.id.reviews);
			holder.deliveryTime = (TextView) row.findViewById(R.id.deliver_time);
			holder.MinOrder = (TextView) row.findViewById(R.id.minorder_rupes);
			holder.pharmacyDistance = (TextView) row.findViewById(R.id.distance);

			holder.pharmacyName.setTypeface(HomeScreen.mHelveticaNormal);
			holder.pharmacyAddress.setTypeface(HomeScreen.mHelveticaNormal);
			holder.pharmacyReviews.setTypeface(HomeScreen.mHelveticaNormal);
			TextView deliver_txt = (TextView) row.findViewById(R.id.deliver);
			TextView min_order_txt = (TextView) row
					.findViewById(R.id.minorder);
			holder.mFav_lay = (RelativeLayout) row
					.findViewById(R.id.fav_lay);
			holder.mRating = (RatingBar) row.findViewById(R.id.fav_ratingbar);


			holder.deliveryTime.setTypeface(HomeScreen.mHelveticaBold);
			holder.MinOrder.setTypeface(HomeScreen.mHelveticaBold);
			deliver_txt.setTypeface(HomeScreen.mHelveticaBold);
			min_order_txt.setTypeface(HomeScreen.mHelveticaBold);
			holder.pharmacyDistance.setTypeface(HomeScreen.mHelveticaBold);

			DecimalFormat distance_roundoff = new DecimalFormat("#.##");


			holder.mOffers_list_item_view = (LinearLayout) row
					.findViewById(R.id.offers_list_item_view);





			row.setTag(holder);

		}
		else
		{
			holder=(Holder)row.getTag();
		}

		final MapPropertyEntity tempData = orderList.get(position);

		holder.mFav_lay.setTag(position);
		holder.mOffers_list_item_view.setTag(tempData);

		holder.pharmacyAddress.setText(tempData.getAddress());
		holder.pharmacyReviews.setText(tempData.getTotalReviews());
		holder.deliveryTime.setText(" "+ tempData.getDeliveryTime() + " "
				+ mContext.getString(R.string.min));
		holder.MinOrder.setText(mContext.getString(R.string.Rs) + " "
				+ tempData.getMinimumOrderValue());
		DecimalFormat distance_roundoff = new DecimalFormat("#.##");
		holder.pharmacyDistance.setText(Double.valueOf(distance_roundoff
				.format(Double.valueOf(tempData.getDistance())))
				+ " " + mContext.getString(R.string.km_away));
		holder.mRating.setRating(Float.valueOf(tempData.getAvgRating()));


		String url="";
		if(tempData.getShopIcon() != null && !tempData.getShopIcon().trim().isEmpty()){
			url =AppConstants.ADMIN_BASE_URL + tempData.getShopIcon();
		} else {
			url =AppConstants.PHARMACY_IMAGE_BASE_URL + tempData.getProfilePic();
		}

		Glide.with(mContext).load(url)
				.thumbnail(0.5f)
				.crossFade()

				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.pharmacyImage);



		holder.pharmacyName.setText(tempData.getShopName());

		holder.mFav_lay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				OffersFragment.mSelectedPos = pos;

				myOrderFragemnt.showRateDialog(orderList.get(pos).getPharmacyID());
			}
		});

		holder.mOffers_list_item_view.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {

				HomeScreen .mFragment = new OrderNowFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(AppConstants.PHARMCAY_DETAILS,
						(MapPropertyEntity)v.getTag());
				bundle.putString(AppConstants.REDIRECTION,
						AppConstants.OFFERS);
				((HomeScreen) mContext).mFragment.setArguments(bundle);

				AppConstants.FROM_MAPFAVORITE_SCREEN = AppConstants.OFFERS_SCREEN;
				((HomeScreen) mContext).replaceFragment(
						((HomeScreen) mContext).mFragment, true);
				HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				HomeScreen.mHeaderLeft
						.setBackgroundResource(R.drawable.back_butto);
			}
		});


		return row;
	}



}
