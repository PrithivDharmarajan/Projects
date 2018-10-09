package com.smaat.renterblock.myfavourite;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FavouriteDetails;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class EditFavouriteActivity extends BaseActivity implements
		OnClickListener {

	private View rootview;
	private String UserID;
	private ListView mEditFavList;
	private TextView mEdit, mDelete;
	private ImageView mHeaderLeft, mHeaderright;
	ArrayList<FavouriteDetails> mFavDetails;
	FavouriteDetails fav_ent = new FavouriteDetails();
	private TableLayout List_view_container;
	private DirectionalViewPager pager;
	private DetailOnPageChangeListener listener;
	DisplayMetrics dm;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	ArrayList<PropertyPictures> mPropertyPics;
	private ArrayList<String> imageUrls;
	private Bitmap[] bitmap;
	TextView myFavourites, myBoards, mPropertycost, mBedDetails, mAddress,
			mReviewsCount, mDate;
	private RatingBar mRating;
	Button delet;
	ImagePagerAdapter img_adapter;
	HashMap<String, ArrayList<FavouriteDetails>> map_val = new HashMap<String, ArrayList<FavouriteDetails>>();
	private TextView header_txt;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_favourite_list);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		UserID = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_userId);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		if (bundle != null) {
			mFavDetails = (ArrayList<FavouriteDetails>) bundle
					.getSerializable("items");
			for (int i = 0; i < mFavDetails.size(); i++) {
				map_val.put(mFavDetails.get(i).getProperty_id(), mFavDetails);
			}
		}
		initComponents();
	}

	public void initComponents() {
		// mEditFavList = (ListView) findViewById(R.id.edit_fvourite_list);
		// mEdit = (TextView) findViewById(R.id.edit_btn);
		header_txt = (TextView) findViewById(R.id.how);
		header_txt.setTypeface(helvetica_bold);
		mHeaderLeft = (ImageView) findViewById(R.id.slide);
		mHeaderright = (ImageView) findViewById(R.id.filter);
		mHeaderright.setVisibility(View.INVISIBLE);
		List_view_container = (TableLayout) findViewById(R.id.view_containers);
		setAdapter();
	}

	public void setAdapter() {
		if (map_val != null) {
			List_view_container.removeAllViews();
			for (int i = 0; i < map_val.size(); i++) {
				LayoutInflater inflater = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View convertView = inflater.inflate(
						R.layout.favourite_slider_adapter_view, null);
				delet = (Button) convertView.findViewById(R.id.property_radio);
				delet.setTag(i);
				imageUrls = new ArrayList<String>();
				bitmap = new Bitmap[6];
				pager = (DirectionalViewPager) convertView
						.findViewById(R.id.pager);
				imageLoader = ImageLoader.getInstance();
				imageLoader.init(ImageLoaderConfiguration
						.createDefault(EditFavouriteActivity.this));
				options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.ic_launcher)
						.cacheOnDisc().cacheInMemory()
						.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

				// addImages(i);
				mPropertyPics = mFavDetails.get(i).getProperty_pics();
				// map_val = map_val.get(mFavDetails.get(i).getProperty_id());
				// mPropertyPics =
				// map_val.get(mFavDetails.get(i).getProperty_pics());
				if (mPropertyPics != null) {
					for (int k = 0; k < mPropertyPics.size(); k++) {
						if (mPropertyPics.get(k).getFile() != null
								&& !mPropertyPics.get(k).getFile().trim()
										.equals("")) {
							imageUrls.add(mPropertyPics.get(k).getFile());

						}
					}
				}
				if (mPropertyPics.isEmpty()) {
					imageUrls
							.add("https://sp.yimg.com/ib/th?id=HN.608018506045394196&pid=15.1&P=0");
				}

				img_adapter = new ImagePagerAdapter(imageUrls,
						map_val.get(mFavDetails.get(i).getProperty_id()));
				pager.setAdapter(img_adapter);
				listener = new DetailOnPageChangeListener();
				pager.setOnPageChangeListener(listener);
				dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				pager.getLayoutParams().height = (int) (dm.heightPixels - ((dm.heightPixels * 2) / 3));

				delet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						callDeleteService(mFavDetails.get(pos).getProperty_id());
						// map_val.get(pos).;
						map_val.remove(mFavDetails.get(pos).getProperty_id());
						int count = map_val.size();
						// img_adapter = new ImagePagerAdapter(imageUrls,
						// map_val.get(mFavDetails.get(pos).getProperty_id()));
						// pager.setAdapter(img_adapter);
						// listener = new DetailOnPageChangeListener();
						// pager.setOnPageChangeListener(listener);
						setAdapter();
						// img_adapter.notifyDataSetChanged();
					}
				});

				List_view_container.addView(convertView);
			}
		}
	}

	private void callDeleteService(String property_id) {
		String url = AppConstants.BASE_URL + "addtofavorite";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("property_id", property_id);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									// FriendsResponse obj = new
									// Gson().fromJson(
									// json.toString(),
									// FriendsResponse.class);
									// onSuccessRequest(obj);
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;
		private ArrayList<FavouriteDetails> fav_info;

		ImagePagerAdapter(ArrayList<String> imageUrls,
				ArrayList<FavouriteDetails> fav_infoc) {
			this.images = imageUrls;
			inflater = getLayoutInflater();
			this.fav_info = fav_infoc;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((DirectionalViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(
					R.layout.adapter_favourites, null);
			final ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ImageView favourite = (ImageView) imageLayout
					.findViewById(R.id.favourite);
			final ImageView addProperty = (ImageView) imageLayout
					.findViewById(R.id.add_property);
			mPropertycost = (TextView) imageLayout
					.findViewById(R.id.property_cost);
			mBedDetails = (TextView) imageLayout
					.findViewById(R.id.property_details);
			mAddress = (TextView) imageLayout
					.findViewById(R.id.property_location);
			mReviewsCount = (TextView) imageLayout
					.findViewById(R.id.property_reviews);
			mDate = (TextView) imageLayout.findViewById(R.id.time);
			mRating = (RatingBar) imageLayout
					.findViewById(R.id.review_ratingbar);

			try {
				if (fav_info.get(position).getProperty_details().get(0)
						.getPrice_range().equals("")) {
					mPropertycost.setText("NA");
				} else {
					System.out.println(NumberFormat.getIntegerInstance()
							.format(Integer.parseInt(fav_info.get(position)
									.getProperty_details().get(0)
									.getPrice_range())));
					String amount = NumberFormat.getIntegerInstance().format(
							Integer.parseInt(fav_info.get(position)
									.getProperty_details().get(0)
									.getPrice_range()));
					String am = amount.substring(0, amount.lastIndexOf(","));

					mPropertycost.setText("$" + am + " k");
				}
				mAddress.setText(fav_info.get(position).getProperty_details()
						.get(0).getAddress());
				mBedDetails.setText(fav_info.get(position)
						.getProperty_details().get(0).getBeds()
						+ " "
						+ "bd"
						+ " "
						+ fav_info.get(position).getProperty_details().get(0)
								.getBaths() + " " + "ba");
				mDate.setText(GlobalMethods.timeDiff(fav_info.get(position)
						.getDate_time()));
				mReviewsCount.setText(fav_info.get(position).getReview_count()
						+ " " + "Reviews");
				float propertyRating = (float) 0.0;
				try {
					propertyRating = Float.parseFloat(fav_info.get(position)
							.getProperty_rating());
				} catch (Exception e) {
					propertyRating = (float) 0.0;
				}
				mRating.setRating(propertyRating);

				// if (fav_info.getIsfavourite().equalsIgnoreCase("1")) {
				favourite.setBackgroundResource(R.drawable.favourites_icon);
				//
				// } else {
				// favourite
				// .setBackgroundResource(R.drawable.favourite_enable);
				// }

			} catch (Exception e) {
				System.out.println(e);
			}
			favourite.setClickable(false);

			// favourite.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (isfavourite) {
			// callADDFavouriteService();
			// favourite
			// .setBackgroundResource(R.drawable.favourite_enable);
			// isfavourite = false;
			// } else {
			// callADDFavouriteService();
			// favourite
			// .setBackgroundResource(R.drawable.favourites_icon);
			// isfavourite = true;
			// }
			//
			// }
			// });

			imageLoader.displayImage(images.get(position), imageView, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted() {
							// spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(FailReason failReason) {

							// spinner.setVisibility(View.GONE);
							// imageView.setImageResource(R.drawable.ic_launcher);
						}

						@Override
						public void onLoadingComplete(final Bitmap loadedImage) {
							// spinner.setVisibility(View.GONE);
							try {
								Animation anim = AnimationUtils.loadAnimation(
										EditFavouriteActivity.this,
										R.anim.fade_in);
								imageView.setAnimation(anim);
								anim.start();
								runOnUiThread(new Runnable() {

									@Override
									public void run() {

										int width = dm.widthPixels;
										int height = width
												* loadedImage.getHeight()
												/ loadedImage.getWidth();
										imageView.getLayoutParams().width = width;
										imageView.getLayoutParams().height = height;
										imageView.setImageBitmap(loadedImage);
										bitmap[position] = loadedImage;
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

			((DirectionalViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		case R.id.slide:
			launchActivity(MyFavouriteActivity.class);
			break;

		default:
			break;
		}
	}

	class DetailOnPageChangeListener extends
			DirectionalViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			currentPage = position;

		}

		public int getCurrentPage() {
			return currentPage;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

			pager.getParent().requestDisallowInterceptTouchEvent(true);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		launchActivity(MyFavouriteActivity.class);
	}
}
