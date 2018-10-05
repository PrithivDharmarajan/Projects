package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.fragment.MapScreenFragment;
import com.smaat.jolt.sqlite.DatabaseUtil;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class ShopListAdapter extends ArrayAdapter<ShopDetailsEntity> {

	private int mCurrentPos = 0;
	private DatabaseUtil db = new DatabaseUtil();

	static class ViewHolder {
		TextView mShopName, address, distance,atmosphere;
		ImageView mSlidepointerOne, mSlidepointerTwo, mSlidepointerThree,
				mFavouriteSelectImg;
		RelativeLayout leftarrow, rightarrow, favourite, imgmap, imgDetails;
		ViewPager pager;
	}

	private ArrayList<ShopDetailsEntity> mList;
	private Context mContext;

	public ShopListAdapter(Context context, int textViewResourceId,
			ArrayList<ShopDetailsEntity> mList) {
		super(context, textViewResourceId, mList);
		this.mList = mList;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater
					.inflate(R.layout.image_slide, parent, false);
			ViewHolder holder = new ViewHolder();

			holder.pager = (ViewPager) convertView
					.findViewById(R.id.view_pager);

			holder.leftarrow = (RelativeLayout) convertView
					.findViewById(R.id.leftArrow);
			holder.rightarrow = (RelativeLayout) convertView
					.findViewById(R.id.rightArrow);
			holder.mSlidepointerOne = (ImageView) convertView
					.findViewById(R.id.slidepointer_one);
			holder.mSlidepointerTwo = (ImageView) convertView
					.findViewById(R.id.slidepointer_two);
			holder.mSlidepointerThree = (ImageView) convertView
					.findViewById(R.id.slidepointer_three);
			holder.mShopName = (TextView) convertView
					.findViewById(R.id.shopname);
			holder.atmosphere = (TextView) convertView
					.findViewById(R.id.atmosphere);
			

			holder.address = (TextView) convertView.findViewById(R.id.address);

			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);

			holder.imgmap = (RelativeLayout) convertView
					.findViewById(R.id.imgMap);
			holder.imgDetails = (RelativeLayout) convertView
					.findViewById(R.id.imgDetails);
			holder.favourite = (RelativeLayout) convertView
					.findViewById(R.id.favouriteselect);
			holder.mFavouriteSelectImg = (ImageView) convertView
					.findViewById(R.id.favouriteselect_img);

			holder.mShopName.setTypeface(HomeScreen.helveticaNeueBold);
			holder.address.setTypeface(HomeScreen.helveticaNeueLight);
			holder.distance.setTypeface(HomeScreen.helveticaNeueMedium);
			holder.atmosphere .setTypeface(HomeScreen.helveticaNeueBold);
			convertView.setTag(holder);

		}

		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.distance.setText(GlobalMethods.getDistanceString(
				mList.get(position).getDistance(), mContext));
		holder.address.setText(mList.get(position).getShopStreet());
		holder.mShopName.setText(mList.get(position).getShopName());
		holder.atmosphere.setText(mList.get(position).getAtmosphere());
		holder.leftarrow.setVisibility(View.INVISIBLE);
		CustomPagerAdapter adapter = new CustomPagerAdapter(mContext,
				mList.get(position));
		holder.pager.setAdapter(adapter);

		holder.pager.setCurrentItem(0);

		holder.imgmap.setVisibility(View.GONE);
		holder.imgDetails.setVisibility(View.GONE);

		final TagClass tagClass = new TagClass();
		tagClass.viewPager = holder.pager;
		tagClass.leftArrow = holder.leftarrow;
		tagClass.rightArrow = holder.rightarrow;
		tagClass.mSlidepointerOne = holder.mSlidepointerOne;
		tagClass.mSlidepointerTwo = holder.mSlidepointerTwo;
		tagClass.mSlidepointerThree = holder.mSlidepointerThree;
		tagClass.mFavouriteSelectImg = holder.mFavouriteSelectImg;
		tagClass.coffeShopName = mList.get(position).getShopName();
		tagClass.shopId = mList.get(position).getShopId();
		holder.mSlidepointerOne.setImageResource(R.drawable.nextpageselect);

		holder.leftarrow.setTag(tagClass);
		holder.rightarrow.setTag(tagClass);
		holder.favourite.setTag(tagClass);

		if (db.isFavorite(mList.get(position).getShopId())) {
			tagClass.isFavorite = true;
			holder.mFavouriteSelectImg
					.setImageResource(R.drawable.favouriteheartselected);
		} else {
			tagClass.isFavorite = false;
			holder.mFavouriteSelectImg
					.setImageResource(R.drawable.favouriteheartunselected);
		}

		holder.favourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TagClass tagClass = (TagClass) v.getTag();
				if (!tagClass.isFavorite) {
					tagClass.mFavouriteSelectImg
							.setImageResource(R.drawable.favouriteheartselected);

					String temp = String.format(
							mContext.getString(R.string.added_favorite),
							tagClass.coffeShopName);
					DialogManager.showToast(mContext, temp);
					tagClass.isFavorite = true;
					db.makeFavorite(tagClass.shopId);
				} else {
					tagClass.mFavouriteSelectImg
							.setImageResource(R.drawable.favouriteheartunselected);

					String temp = String.format(
							mContext.getString(R.string.removed_favorite),
							tagClass.coffeShopName);
					DialogManager.showToast(mContext, temp);
					tagClass.isFavorite = false;
					db.makeUnfavorite(tagClass.shopId);
				}
				if (MapScreenFragment.mSerchbylay.getVisibility() == View.VISIBLE) {
					MapScreenFragment.updateHeader();
					MapScreenFragment.mSerchbylay.setVisibility(View.GONE);
				}
			}
		});

		holder.rightarrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TagClass tagClass = (TagClass) v.getTag();
				mCurrentPos = tagClass.viewPager.getCurrentItem();
				tagClass.viewPager.setCurrentItem(++mCurrentPos);

			}

		});

		holder.leftarrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TagClass tagClass = (TagClass) v.getTag();
				mCurrentPos = tagClass.viewPager.getCurrentItem();
				tagClass.viewPager.setCurrentItem(--mCurrentPos);

			}
		});

		holder.pager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				MapScreenFragment.mMapListView
						.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		holder.pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int mCurrentPos, float arg1, int arg2) {
				if (mCurrentPos == 0) {
					tagClass.mSlidepointerOne
							.setImageResource(R.drawable.nextpageselect);
					tagClass.mSlidepointerTwo
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.mSlidepointerThree
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.leftArrow.setVisibility(View.INVISIBLE);
					tagClass.rightArrow.setVisibility(View.VISIBLE);
				} else if (mCurrentPos == 1) {
					tagClass.mSlidepointerOne
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.mSlidepointerTwo
							.setImageResource(R.drawable.nextpageselect);
					tagClass.mSlidepointerThree
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.leftArrow.setVisibility(View.VISIBLE);
					tagClass.rightArrow.setVisibility(View.VISIBLE);
				} else {
					tagClass.mSlidepointerOne
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.mSlidepointerTwo
							.setImageResource(R.drawable.nextpageunselect);
					tagClass.mSlidepointerThree
							.setImageResource(R.drawable.nextpageselect);

					tagClass.leftArrow.setVisibility(View.VISIBLE);
					tagClass.rightArrow.setVisibility(View.INVISIBLE);
				}

				if (MapScreenFragment.mSerchbylay.getVisibility() == View.VISIBLE) {
					MapScreenFragment.updateHeader();
					MapScreenFragment.mSerchbylay.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		return convertView;

	}

	public class TagClass {
		ViewPager viewPager;
		RelativeLayout leftArrow;
		RelativeLayout rightArrow;
		ImageView mSlidepointerOne, mSlidepointerTwo, mSlidepointerThree,
				imgmap, imgDetails, favourite, mFavouriteSelectImg;
		boolean isFavorite;
		String coffeShopName, shopId;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int position) {

			if (tagClass.viewPager.getAdapter().getCount() == mCurrentPos) {
				tagClass.rightArrow.setVisibility(View.INVISIBLE);
			}
			if (0 == mCurrentPos) {
				tagClass.leftArrow.setVisibility(View.INVISIBLE);
			}

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		TagClass tagClass;

		public DetailOnPageChangeListener(TagClass tagClass) {
			this.tagClass = tagClass;
		}

	}
}
