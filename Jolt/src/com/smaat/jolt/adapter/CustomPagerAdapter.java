package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.fragment.AvailableDrinks;
import com.smaat.jolt.fragment.MapScreenFragment;
import com.smaat.jolt.ui.BaseActivity;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.squareup.picasso.Picasso;

public class CustomPagerAdapter  extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;

	ArrayList<String> imageList = new ArrayList<String>();
	ShopDetailsEntity mList;

	public CustomPagerAdapter(Context context, ShopDetailsEntity mList) {
		mContext = context;
		imageList.add(mList.getShopImage1());
		imageList.add(mList.getShopImage2());
		imageList.add(mList.getShopImage3());
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// only we have maximum 3 images
		return imageList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = mLayoutInflater.inflate(R.layout.pager_item,
				container, false);

		ImageView imageView = (ImageView) itemView
				.findViewById(R.id.imageView);

		Picasso.with(mContext)
				.load(AppConstants.BASE_TIMTHUMB + imageList.get(position)
						+ "&q=100&zc=0&w=" + HomeScreen.screenWidth)
				.into(imageView);

		FrameLayout pagerlist = (FrameLayout) itemView
				.findViewById(R.id.pagerlist);
		pagerlist.setTag(mList);

		pagerlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActivity.hideSoftKeyboard((HomeScreen)mContext);
				MapScreenFragment.updateHeader();
				HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
				HomeScreen.mHeaderLeftBtn
						.setBackgroundResource(R.drawable.back_btn);

				AvailableDrinks.shopDetails = ((ShopDetailsEntity) v
						.getTag());

				((HomeScreen) mContext).mFragment = new AvailableDrinks();

				((HomeScreen) mContext).replaceFragment(
						((HomeScreen) mContext).mFragment, true);
			}
		});

		container.addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}
}