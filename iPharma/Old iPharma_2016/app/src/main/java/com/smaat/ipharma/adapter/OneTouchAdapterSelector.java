package com.smaat.ipharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.fragment.OneTouchListSelectFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.RoundEdgeImageView;

import java.util.ArrayList;

public class OneTouchAdapterSelector extends BaseAdapter {
	private int mLayout;
	private Context mContext;
	private ArrayList<MapPropertyEntity> orderList;
	OneTouchListSelectFragment myOrderFragemnt;

	public OneTouchAdapterSelector(OneTouchListSelectFragment context, int layout,
								   ArrayList<MapPropertyEntity> review_list) {
		mContext = context.getContext();
		mLayout = layout;
		orderList = review_list;
		myOrderFragemnt= context;
	}

	class Holder {
		RoundEdgeImageView pharmacyImage;
		TextView pharmacyName, pharmacyAddress, pharmacyReviews, pharmacyDistance,
				deliveryTime, MinOrder;
		RatingBar mRating;
		LinearLayout mOffersListItemView;
		private Button mFavButton;


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
			row=inflater.inflate(R.layout.one_touch_item, parent, false);

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
			holder.mFavButton = (Button) row.findViewById(R.id.fav_button);
			holder.mRating = (RatingBar) row.findViewById(R.id.fav_ratingbar);

			holder.pharmacyName.setTypeface(HomeScreen.mHelveticaNormal);
			holder.pharmacyAddress.setTypeface(HomeScreen.mHelveticaNormal);
			holder.pharmacyReviews.setTypeface(HomeScreen.mHelveticaNormal);
			holder.deliveryTime.setTypeface(HomeScreen.mHelveticaNormal);
			holder.MinOrder.setTypeface(HomeScreen.mHelveticaNormal);
			holder.pharmacyDistance.setTypeface(HomeScreen.mHelveticaNormal);

			holder.mOffersListItemView = (LinearLayout) row
					.findViewById(R.id.offers_list_item_view);





			row.setTag(holder);

		}
		else
		{
			holder=(Holder)row.getTag();
		}

		final MapPropertyEntity tempData = orderList.get(position);


		holder.mOffersListItemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				OneTouchListSelectFragment.mSelectedPos = (Integer) view.getTag();

				MapPropertyEntity temp =  orderList.get(OneTouchListSelectFragment.mSelectedPos);
				String shop_id =temp.getPharmacyID();
				String isFav = temp.getIsFav();
				String isfav = "0";
				if (isFav != null && isFav.equalsIgnoreCase(AppConstants.FAILURE_CODE) || isFav.equalsIgnoreCase(mContext.getString(R.string.two))) {
//					v.setBackgroundResource(R.drawable.one_touch_selected);
					isfav = "1";
					OneTouchListSelectFragment.isFavorite = 1;

				} else if (isFav != null
						&& isFav.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//					v.setBackgroundResource(R.drawable.one_touch_normal);
					isfav = "2";
					OneTouchListSelectFragment.isFavorite = 2;
				}


				myOrderFragemnt.callFavouriteservice(isfav,shop_id);

			}
		});


		holder.mOffersListItemView.setTag(position);
		holder.mFavButton.setTag(position);


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



		String deliver_status = "";
		holder.pharmacyName.setText(tempData.getShopName());
		holder.pharmacyAddress.setText(tempData.getAddress());
		holder.pharmacyReviews.setText(tempData.getTotalReviews());
		holder.deliveryTime.setText(tempData.getDeliveryTime());

		String isFav = tempData.getIsFav();

		if (isFav != null && isFav.equalsIgnoreCase(AppConstants.FAILURE_CODE) || isFav.equalsIgnoreCase(mContext.getString(R.string.two))) {
			holder.mFavButton.setBackgroundResource(R.drawable.one_touch_normal);

		} else if (isFav != null
				&& isFav.equalsIgnoreCase(AppConstants.SUCCESS_CODE)
				) {
			holder.mFavButton.setBackgroundResource(R.drawable.one_touch_selected);
		}

		holder.mFavButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				OneTouchListSelectFragment.mSelectedPos = (Integer) v.getTag();

				MapPropertyEntity temp =  orderList.get(OneTouchListSelectFragment.mSelectedPos);
				String shop_id =temp.getPharmacyID();
				String isFav = temp.getIsFav();
				String isfav = "0";
				if (isFav != null && isFav.equalsIgnoreCase(AppConstants.FAILURE_CODE) || isFav.equalsIgnoreCase(mContext.getString(R.string.two))) {
					v.setBackgroundResource(R.drawable.one_touch_selected);
					isfav = "1";
					OneTouchListSelectFragment.isFavorite = 1;

				} else if (isFav != null
						&& isFav.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
					v.setBackgroundResource(R.drawable.one_touch_normal);
					isfav = "2";
					OneTouchListSelectFragment.isFavorite = 2;
				}


				myOrderFragemnt.callFavouriteservice(isfav,shop_id);


			}
		});



		return row;
	}



}
