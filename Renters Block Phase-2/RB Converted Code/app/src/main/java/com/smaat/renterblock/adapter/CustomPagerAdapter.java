package com.smaat.renterblock.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.CreateAccountScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.ui.WriteReviewActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class CustomPagerAdapter extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;
	ArrayList<String> mImagesList = new ArrayList<String>();
	private DisplayMetrics dm;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private int mListViewPosition = 0;
	private static final int MIN_DISTANCE = 50;
	private float downX = 0, downY = 0, upX = 0, upY = 0;

	private PropertyEntity map_loc_info;
	private String tag;

	public static final int VIEW_PAGER_NEXT = 0, VIEW_PAGER_PREVIOUS = 1,
			LIST_NEXT = 2, LIST_PREVIOUS = 3;

	Typeface helvetica_normal, helvetica_bold, helvetica_light;

	public CustomPagerAdapter(Context context, ArrayList<String> imagesList,
			int listViewPosition, PropertyEntity propEntity) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImagesList = imagesList;
		map_loc_info = propEntity;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		mListViewPosition = listViewPosition;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.house_two).cacheOnDisc()
				.cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	@Override
	public int getCount() {
		return mImagesList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final View imageLayout = mLayoutInflater.inflate(
				R.layout.item_image_pager, null);
		final ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.image);
		final TextView property_amount = (TextView) imageLayout
				.findViewById(R.id.property_cost);
		property_amount.setTypeface(helvetica_bold);
		final TextView property_details = (TextView) imageLayout
				.findViewById(R.id.property_details);
		property_details.setTypeface(helvetica_normal);
		final TextView property_address = (TextView) imageLayout
				.findViewById(R.id.property_location);
		property_address.setTypeface(helvetica_normal);
		final TextView mDate = (TextView) imageLayout.findViewById(R.id.time);
		mDate.setTypeface(helvetica_normal);
		final TextView mReviewsCount = (TextView) imageLayout
				.findViewById(R.id.property_reviews);
		mReviewsCount.setTypeface(helvetica_normal);
		final RatingBar mRating = (RatingBar) imageLayout
				.findViewById(R.id.review_ratingbar);
		final ImageView mFav = (ImageView) imageLayout
				.findViewById(R.id.favourite);
		final RelativeLayout fram = (RelativeLayout) imageLayout
				.findViewById(R.id.propert_img_view);

		imageView.setImageResource(R.drawable.image_overlay);

		final ProgressBar spinner = (ProgressBar) imageLayout
				.findViewById(R.id.loading);

		String tag1 = mListViewPosition + "," + position;
		imageLayout.setTag(tag1);
		
		mDate.setText("Last Updated "
				+ GlobalMethods.timeDiff(map_loc_info
						.getProperty_datetime()));

		String am = "";
		String amount_ex = "";
		try {
			if (map_loc_info.getPrice_range().equals("")) {
				property_amount.setText("NA");
			} else {
				amount_ex = map_loc_info.getPrice_range();
				am = NumberFormat.getNumberInstance(Locale.US).format(
						Integer.parseInt(amount_ex));
				Integer validate = Integer.parseInt(map_loc_info
						.getPrice_range());
				if (validate <= 2) {
					property_amount.setText("$" + amount_ex + "/ mo");
				} else {
					property_amount.setText("$" + am + "/ mo");
				}
			}
			property_address.setText(map_loc_info.getAddress());
			property_details.setText(map_loc_info.getBeds() + " " + "bed" + " "
					+ map_loc_info.getBaths() + " " + "bath");
			if (map_loc_info.getReview_count() != null
					&& (map_loc_info.getReview_count().equalsIgnoreCase(""))) {
				mReviewsCount.setText("( 0 )");
			} else {
				mReviewsCount.setText("( " + map_loc_info.getReview_count()
						+ " )");
			}
			float propertyRating = (float) 0.0;
			if (map_loc_info.getProperty_rating() != null) {
				try {
					if (map_loc_info.getProperty_rating().equalsIgnoreCase("")) {
						mRating.setRating(0);
					}
				} catch (Exception e) {

				}
			}
			try {
				propertyRating = Float.parseFloat(map_loc_info
						.getProperty_rating());
			} catch (Exception e) {
				propertyRating = (float) 0.0;
			}
			mRating.setRating(propertyRating);

			if ("1".equalsIgnoreCase(map_loc_info.getIsfavourite())) {
				mFav.setBackgroundResource(R.drawable.red_hear_icon_list);
				mFav.setTag(false);
			} else {
				mFav.setBackgroundResource(R.drawable.favourite_enable);
				mFav.setTag(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		final String UserID = (String) GlobalMethods.getValueFromPreference(
				mContext, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);

		mFav.setFocusableInTouchMode(true);

		mFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
					((BaseActivity) mContext)
							.launchActivity(CreateAccountScreen.class);
				} else {
					boolean isfavourite = (Boolean) v.getTag();
					if (isfavourite) {
						mFav.setBackgroundResource(R.drawable.red_hear_icon_list);
						v.setTag(false);
					} else {
						mFav.setBackgroundResource(R.drawable.favourite_enable);
						v.setTag(true);
					}

					((MapFragmentActivity) mContext).callADDFavouriteService(
							UserID, map_loc_info.getProperty_id());
				}

			}
		});

		imageLoader.displayImage(mImagesList.get(position), imageView, options,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted() {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(FailReason failReason) {

						spinner.setVisibility(View.GONE);
						imageView
								.setImageResource(R.drawable.default_prop_icon);
					}

					@Override
					public void onLoadingComplete(final Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);
						try {
							Animation anim = AnimationUtils.loadAnimation(
									mContext, R.anim.fade_in);
							imageView.setAnimation(anim);
							anim.start();
							((Activity) mContext).runOnUiThread(new Runnable() {

								@Override
								public void run() {

									int width = dm.widthPixels;
									int height = width
											* loadedImage.getHeight()
											/ loadedImage.getWidth();
									imageView.getLayoutParams().width = width;
									imageView.getLayoutParams().height = height;
									imageView.setImageBitmap(loadedImage);

								}
							});
						} catch (Exception e) {
							System.out.println(e);
						}
					}

					@Override
					public void onLoadingCancelled() {
						// Do nothing
					}
				});

		imageLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downX = event.getX();
					downY = event.getY();
					return true;
				}
				case MotionEvent.ACTION_UP: {
					upX = event.getX();
					upY = event.getY();

					float deltaX = downX - upX;
					float deltaY = downY - upY;

					// swipe horizontal?
					if (Math.abs(deltaX) > Math.abs(deltaY)) {
						if (Math.abs(deltaX) > MIN_DISTANCE) {
							// left or right
							if (deltaX < 0) {
								((MapFragmentActivity) mContext).onImageTouch(
										(String) v.getTag(),
										VIEW_PAGER_PREVIOUS);
								return true;
							}
							if (deltaX > 0) {
								((MapFragmentActivity) mContext).onImageTouch(
										(String) v.getTag(), VIEW_PAGER_NEXT);
								return true;
							}
						} else {
							// Log.i("Horizontal", "Horizontal Swipe was only "
							// + Math.abs(deltaX)
							// + " long, need at least " + MIN_DISTANCE);
							tag = (String) v.getTag();
							onClickEvent();
							return false; // We don't consume the event
						}
					}
					// swipe vertical?
					else {
						if (Math.abs(deltaY) > MIN_DISTANCE) {
							// top or down
							if (deltaY < 0) {
								((MapFragmentActivity) mContext).onImageTouch(
										(String) v.getTag(), LIST_PREVIOUS);
								return true;
							}
							if (deltaY > 0) {
								((MapFragmentActivity) mContext).onImageTouch(
										(String) v.getTag(), LIST_NEXT);
								return true;
							}
						} else {
							tag = (String) v.getTag();
							onClickEvent();
							return false; // We don't consume the event
						}
					}

					return true;
				}
				}
				return true;
			}
		});

		container.addView(imageLayout);

		return imageLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((RelativeLayout) object);
	}

	public void onClickEvent() {

		if ("review".equalsIgnoreCase(MapFragmentActivity.isReview)) {
			Intent writeReview = new Intent(mContext, WriteReviewActivity.class);
			writeReview.putExtra("ReviewType", "Post");
			writeReview.putExtra("Rating", "0.0");
			writeReview.putExtra("Comments", "");
			writeReview.putExtra("PropertyId", map_loc_info.getProperty_id());
			writeReview.putExtra("PropertyReviewId", "");
			((Activity) mContext).overridePendingTransition(
					R.anim.slide_in_right, R.anim.slide_out_left);
			mContext.startActivity(writeReview);
		} else {
			String prop_marker = "";
			if (map_loc_info.getRb_block().equalsIgnoreCase("1")) {
				prop_marker = "exclusive";
			} else if (map_loc_info.getOpen_house().equalsIgnoreCase("1")) {
				prop_marker = "openhouse";
			} else if (map_loc_info.getIsfavourite().equalsIgnoreCase("1")) {
				prop_marker = "favourite";
			} else {
				prop_marker = "";
			}
			Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
			intent.putExtra("PropertyId", map_loc_info.getProperty_id());
			intent.putExtra("PropType", prop_marker);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(
					R.anim.slide_in_right, R.anim.slide_out_left);
//			((Activity) mContext).finish();
		}

	}

	class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			// Trigger the touch event on the calendar
			onClickEvent();
			Toast.makeText(mContext, "tab", Toast.LENGTH_LONG).show();
			return super.onSingleTapUp(event);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Toast.makeText(mContext, "fling", Toast.LENGTH_LONG).show();

			// int minSwipeDistance =
			// viewConfiguration.getScaledPagingTouchSlop();
			// int minSwipeVelocity = viewConfiguration
			// .getScaledMinimumFlingVelocity();
			// int maxSwipeOffPath = viewConfiguration.getScaledTouchSlop();
			//
			// if (Math.abs(e1.getY() - e2.getY()) > maxSwipeOffPath) {
			// return false;
			// }

			// Right to left swipe
			if ((e1.getX() - e2.getX()) > MIN_DISTANCE) {
				((MapFragmentActivity) mContext).onImageTouch(tag,
						VIEW_PAGER_NEXT);
			}
			// Left to right
			else if ((e2.getX() - e1.getX()) > MIN_DISTANCE) {
				((MapFragmentActivity) mContext).onImageTouch(tag,
						VIEW_PAGER_PREVIOUS);
			} else if ((e1.getY() - e2.getY()) > MIN_DISTANCE) {

				((MapFragmentActivity) mContext).onImageTouch(tag, LIST_NEXT);

			} else if ((e2.getY() - e1.getY()) > MIN_DISTANCE) {

				((MapFragmentActivity) mContext).onImageTouch(tag,
						LIST_PREVIOUS);

			}

			return false;
		}
	}
}
