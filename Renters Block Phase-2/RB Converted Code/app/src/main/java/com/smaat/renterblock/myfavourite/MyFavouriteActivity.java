package com.smaat.renterblock.myfavourite;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.BoardsDetails;
import com.smaat.renterblock.entity.FavouriteDetails;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.model.BoardsResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.FavouriteReponse;
import com.smaat.renterblock.model.PropertyResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.LoginActivity;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;
import com.smaat.renterblock.webservice.ServiceRequestHandler;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFavouriteActivity extends BaseActivity implements
		DialogMangerCallback {

	private TextView myFavourites, myBoards, mPropertycost, mBedDetails,
			mAddress, mReviewsCount, mDate;
	private ImageView addProperty;
	private ScrollView mFavouritesList;
	private GridView mBoardsGrid;
	private BoardsGridAdapter mBoardsAdapter;
	private FrameLayout mGridLay;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<String> imageUrls = null;
	public Bitmap bitmap[];
	private DirectionalViewPager pager;
	private DetailOnPageChangeListener listener;
	private DisplayMetrics dm;
	private LinearLayout List_view_container;
	private RelativeLayout my_boards_lay;
	private boolean isfavourite = true;
	private String UserID = "";
	private int service;
	private ArrayList<FavouriteDetails> mFavDetails;
	private RatingBar mRating;
	private ArrayList<BoardsDetails> mBoardDetails;
	private ArrayList<PropertyPictures> mPropertyPic;

	private ArrayList<PropertyEntity> mProperty;
	private ImageView imageView;
	private Button btnPropertyRadio;
	ImagePagerAdapter mPageAdapter;
	private Button mRightBtn;
	private String mRightBtnValue = "Edit";
	/**
	 * Slide menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private TextView header_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_favourite);

		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		UserID = GlobalMethods.getUserID(this);

		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initcomponents();
		setGoogleAnalytics(this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getFavouriteListService();
		}

	}

	public void initcomponents() {

		myFavourites = (TextView) findViewById(R.id.favourites);
		myBoards = (TextView) findViewById(R.id.my_board);

		myFavourites.setTypeface(helvetica_bold);
		myBoards.setTypeface(helvetica_bold);

		myFavourites.setBackgroundResource(R.drawable.headers_line);
		myFavourites.setTextColor(getResources().getColor(R.color.blue_color));

		mRightBtn = (Button) findViewById(R.id.right_btn);
		mRightBtnValue = "Edit";
		mRightBtn.setBackgroundResource(R.drawable.edit_button);

		mFavouritesList = (ScrollView) findViewById(R.id.favourites_list);
		mFavouritesList.setVisibility(View.VISIBLE);

		mBoardsGrid = (GridView) findViewById(R.id.my_boards_grid);
		// mBoardsGrid.setOnItemClickListener(this);

		mGridLay = (FrameLayout) findViewById(R.id.grid_layout);
		List_view_container = (LinearLayout) findViewById(R.id.view_containers);
		my_boards_lay = (RelativeLayout) findViewById(R.id.my_boards_layout);

		mFavDetails = new ArrayList<FavouriteDetails>();
		mBoardDetails = new ArrayList<BoardsDetails>();
		mPropertyPic = new ArrayList<PropertyPictures>();
		mProperty = new ArrayList<PropertyEntity>();
		/**
		 * Slide Menu Iniialization
		 */
		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_my_favourites);
			AppConstants.view_id = R.id.buy_my_favourites;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_my_favourites);
			AppConstants.view_id = R.id.sell_my_favourites;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_my_favourites);
			AppConstants.view_id = R.id.agent_my_favourites;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_my_favourites);
			AppConstants.view_id = R.id.buy_my_favourites;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.menu_icon:
			slide_holder.toggle();
			break;
		case R.id.right_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mRightBtnValue.equals("Edit")) {
					if (mFavDetails.size() == 0) {
						DialogManager.showCustomAlertDialog(this, this,
								"No favourites are available");
					} else {
						mRightBtnValue = "Close";
						mRightBtn.setBackgroundResource(R.drawable.close_icon);
						AppConstants.IS_VISIBLE = "true";
						setListAdpater();
					}

				} else if (mRightBtnValue.equals("Add")) {
					mGridLay.setVisibility(View.GONE);
					mFavouritesList.setVisibility(View.VISIBLE);
					launchActivity(MapFragmentActivity.class);
					AppConstants.from_favourites_act = true;
					AppConstants.from_map_list = false;
				} else {
					mRightBtnValue = "Edit";
					mRightBtn.setBackgroundResource(R.drawable.edit_button);
					AppConstants.IS_VISIBLE = "false";
					setListAdpater();
				}
			}
			break;
		case R.id.favourites:
			header_txt.setText("My Favorites");
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				mFavouritesList.setVisibility(View.VISIBLE);
				mGridLay.setVisibility(View.GONE);
				mRightBtnValue = "Edit";
				mRightBtn.setBackgroundResource(R.drawable.edit_button);
				myFavourites.setBackgroundResource(R.drawable.headers_line);
				myFavourites.setTextColor(getResources().getColor(
						R.color.blue_color));
				myBoards.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
				getFavouriteListService();
				myBoards.setTextColor(getResources().getColor(R.color.black));
				my_boards_lay.setVisibility(View.GONE);
			}
			break;
		case R.id.my_board:
			header_txt.setText("My Boards");
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				mGridLay.setVisibility(View.VISIBLE);
				mFavouritesList.setVisibility(View.GONE);
				my_boards_lay.setVisibility(View.GONE);
				mRightBtnValue = "Add";
				mRightBtn.setBackgroundResource(R.drawable.add_button);

				myFavourites.setBackgroundResource(R.drawable.headers_line);
				myFavourites.setTextColor(getResources()
						.getColor(R.color.black));
				myBoards.setTextColor(getResources().getColor(
						R.color.blue_color));
				myFavourites.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
				myBoards.setBackgroundResource(R.drawable.headers_line);
				if (mBoardDetails != null) {
					getBoardsListService();
				} else {
					Toast.makeText(MyFavouriteActivity.this,
							"Sorry,You have no Boards", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}

	}

	public void setListAdpater() {
		List_view_container.removeAllViews();
		if (mFavDetails.size() != 0 || mFavDetails != null) {
			for (int i = 0; i < mFavDetails.size(); i++) {
				LayoutInflater inflater = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View convertView = inflater.inflate(
						R.layout.image_slider_adapter_view, null);
				imageUrls = new ArrayList<String>();
				pager = (DirectionalViewPager) convertView
						.findViewById(R.id.pager);
				imageLoader = ImageLoader.getInstance();
				imageLoader.init(ImageLoaderConfiguration
						.createDefault(MyFavouriteActivity.this));
				options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.ic_launcher)
						.cacheOnDisc().cacheInMemory()
						.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

				bitmap = new Bitmap[6];

				mPropertyPic = mFavDetails.get(i).getProperty_pics();
				if (mPropertyPic != null) {
					for (int k = 0; k < mPropertyPic.size(); k++) {
						if (mPropertyPic.get(k).getFile() != null
								&& !mPropertyPic.get(k).getFile().trim()
										.equals("")) {
							imageUrls.add(mPropertyPic.get(k).getFile());

						}
					}
				}
				if (mPropertyPic.isEmpty()) {
					imageUrls
							.add("https://sp.yimg.com/ib/th?id=HN.608018506045394196&pid=15.1&P=0");
				}
				pagemethod(i);
				List_view_container.addView(convertView);
			}
		}
	}

	private void pagemethod(int i) {
		mPageAdapter = new ImagePagerAdapter(imageUrls, mFavDetails.get(i));
		pager.setAdapter(mPageAdapter);

		listener = new DetailOnPageChangeListener();
		pager.setOnPageChangeListener(listener);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		pager.getLayoutParams().height = (int) (dm.heightPixels - ((dm.heightPixels * 2) / 3));
	}

	public void ShowListAdapter() {
		List_view_container.removeAllViews();

		for (int i = 0; i < mProperty.size(); i++) {

			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View convertView = inflater.inflate(
					R.layout.image_slider_adapter_view, null);

			imageUrls = new ArrayList<String>();
			pager = (DirectionalViewPager) convertView.findViewById(R.id.pager);
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(MyFavouriteActivity.this));
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
					.cacheInMemory()
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

			bitmap = new Bitmap[6];
			mPropertyPic = mProperty.get(i).getProperty_pics();
			if (mPropertyPic != null) {
				for (int k = 0; k < mPropertyPic.size(); k++) {
					if (mPropertyPic.get(k).getFile() != null
							&& !mPropertyPic.get(k).getFile().trim().equals("")) {
						imageUrls.add(mPropertyPic.get(k).getFile());

					}
				}
			}

			pager.setAdapter(new ImagePagerAdapter1(imageUrls, mProperty.get(i)));
			listener = new DetailOnPageChangeListener();
			pager.setOnPageChangeListener(listener);
			dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			pager.getLayoutParams().height = (int) (dm.heightPixels - ((dm.heightPixels * 2) / 3));

			List_view_container.addView(convertView);
		}
	}

	public void setGridAdapter() {
		mBoardsAdapter = new BoardsGridAdapter(MyFavouriteActivity.this,
				R.layout.adapter_myboards_grid, mBoardDetails);
		mBoardsGrid.setAdapter(mBoardsAdapter);
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

	public class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;
		private FavouriteDetails fav_info;

		ImagePagerAdapter(ArrayList<String> imageUrls,
				FavouriteDetails fav_infoc) {
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
			imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ImageView favourite = (ImageView) imageLayout
					.findViewById(R.id.favourite);
			addProperty = (ImageView) imageLayout
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
			btnPropertyRadio = (Button) imageLayout
					.findViewById(R.id.property_radio);
			RelativeLayout mParent_lay = (RelativeLayout) imageLayout
					.findViewById(R.id.parent_lay);
			
			imageView.setTag(position);
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String prop_marker = "";
//					if (fav_info.getRb_block().equalsIgnoreCase("1")) {
//						prop_marker = "exclusive";
//					} else if (map_loc_info.getOpen_house().equalsIgnoreCase("1")) {
//						prop_marker = "openhouse";
//					} else if (map_loc_info.getIsfavourite().equalsIgnoreCase("1")) {
						prop_marker = "favourite";
//					} else {
//						prop_marker = "";
//					}
					Intent intent = new Intent(MyFavouriteActivity.this, PropertyDetailsActivity.class);
					intent.putExtra("PropertyId", fav_info.getProperty_id());
					intent.putExtra("PropType", prop_marker);
					startActivity(intent);
					overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				}
			});

			if (AppConstants.IS_VISIBLE.equals("true")) {
				btnPropertyRadio.setVisibility(View.VISIBLE);
			} else {
				btnPropertyRadio.setVisibility(View.GONE);
			}

			btnPropertyRadio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					callDeleteService(fav_info.getProperty_id());

					getFavouriteListService();

					mPageAdapter.notifyDataSetChanged();

				}

				private void callDeleteService(String propertyID) {
					String url = AppConstants.BASE_URL + "addtofavorite";
					GsonTransformer t = new GsonTransformer();

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", UserID);
					params.put("property_id", propertyID);

					aq().transformer(t)
							.progress(
									DialogManager
											.getProgressDialog(MyFavouriteActivity.this))
							.ajax(url, params, JSONObject.class,
									new AjaxCallback<JSONObject>() {

										@Override
										public void callback(String url,
												JSONObject json,
												AjaxStatus status) {

											if (json != null) {

											} else {
												statusErrorCode(status);
											}

										}
									});
				}

			});

			try {
				if (fav_info.getProperty_details().get(0).getPrice_range()
						.equals("")) {
					mPropertycost.setText("NA");
				} else {
					System.out.println(NumberFormat.getIntegerInstance()
							.format(Integer.parseInt(fav_info
									.getProperty_details().get(0)
									.getPrice_range())));
					String amount = NumberFormat.getIntegerInstance().format(
							Integer.parseInt(fav_info.getProperty_details()
									.get(0).getPrice_range()));
					// if (amount.length() != 0) {
					// String am = amount
					// .substring(0, amount.lastIndexOf(","));
					int validate = Integer.parseInt(fav_info
							.getProperty_details().get(0).getPrice_range());
					if (validate <= 2) {
						mPropertycost.setText("$"
								+ fav_info.getProperty_details().get(0)
										.getPrice_range() + " / mo");
					} else {
						mPropertycost.setText("$" + amount + " / mo");
					}
				}
				mAddress.setText(fav_info.getProperty_details().get(0)
						.getAddress());
				mBedDetails.setText(fav_info.getProperty_details().get(0)
						.getBeds()
						+ " "
						+ "bed"
						+ " "
						+ fav_info.getProperty_details().get(0).getBaths()
						+ " " + "bath");
				mDate.setText(GlobalMethods.myFavtimeDiff(fav_info
						.getProperty_details().get(0).getProperty_datetime()));
				mReviewsCount.setText("( " + fav_info.getReview_count() + " )");
				float propertyRating = (float) 0.0;
				try {
					propertyRating = Float.parseFloat(fav_info
							.getProperty_rating());
				} catch (Exception e) {
					propertyRating = (float) 0.0;
				}
				mRating.setRating(propertyRating);

				favourite.setBackgroundResource(R.drawable.favourites_icon);

				if (fav_info.getIsboard().equalsIgnoreCase("1")) {
					isfavourite = false;
					addProperty.setImageResource(R.drawable.minus_icon);

				} else {
					isfavourite = true;
					addProperty.setImageResource(R.drawable.add_button);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			favourite.setClickable(false);

			if (images.isEmpty() || images.size() == 0) {
				imageView.setVisibility(View.GONE);
			}

			addProperty.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (UserID.equalsIgnoreCase("")
							|| UserID.equalsIgnoreCase("0")) {
						launchActivity(LoginActivity.class);
					} else {

						if (isfavourite) {

							if (AppConstants.MYFAVPLUS == "isfirst") {

								AppConstants.MYFAVPLUS = "isnext";

								showAlertPopup(
										"You have just added this listing to your boards.See what other people have to say about this property.",
										fav_info.getProperty_id());

							} else {
								callSetBoardsService(fav_info.getProperty_id());
								addProperty
										.setImageResource(R.drawable.minus_icon);
								isfavourite = false;
							}

						} else {
							if (AppConstants.MYFAVPLUS == "isfirst") {

								AppConstants.MYFAVPLUS = "isnext";

								showAlertPopup(
										"Do you want remove this property from my boards?",
										fav_info.getProperty_id());
							} else {
								callSetBoardsService(fav_info.getProperty_id());
								addProperty
										.setImageResource(R.drawable.add_button);
								isfavourite = true;
							}

						}
					}

				}
			});
			if (images.isEmpty()) {
				imageView.setVisibility(View.GONE);
			}
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
										MyFavouriteActivity.this,
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

	private void showAlertPopup(final String message, final String mPropId) {

		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_confirmation);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mAlertText = (TextView) mDialog.findViewById(R.id.alert_text);
		mAlertText.setText(message);
		mAlertText.setMovementMethod(new ScrollingMovementMethod());
		Button mYes = (Button) mDialog.findViewById(R.id.yes);
		Button mNo = (Button) mDialog.findViewById(R.id.no);

		mYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();
				callSetBoardsService(mPropId);
				if (message
						.equalsIgnoreCase("Do you want add this property to my boards?")) {
					addProperty.setImageResource(R.drawable.minus_icon);
					isfavourite = false;
				} else {
					addProperty.setImageResource(R.drawable.add_button);
					isfavourite = true;
				}

			}
		});

		mNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.MYFAVPLUS = "isfirst";
				mDialog.dismiss();

			}
		});
		mDialog.show();

	}

	class ImagePagerAdapter1 extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;
		private PropertyEntity map_loc_info;

		ImagePagerAdapter1(ArrayList<String> imageUrls, PropertyEntity map_loc) {
			this.images = imageUrls;
			this.map_loc_info = map_loc;
			inflater = getLayoutInflater();
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
					R.layout.item_image_pager, null);
			final ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final TextView property_amount = (TextView) imageLayout
					.findViewById(R.id.property_cost);
			final TextView property_details = (TextView) imageLayout
					.findViewById(R.id.property_details);
			final TextView property_address = (TextView) imageLayout
					.findViewById(R.id.property_location);
			final TextView mDate = (TextView) imageLayout
					.findViewById(R.id.time);
			final TextView mReviewsCount = (TextView) imageLayout
					.findViewById(R.id.property_reviews);
			final RatingBar mRating = (RatingBar) imageLayout
					.findViewById(R.id.review_ratingbar);
			final ImageView mFav = (ImageView) imageLayout
					.findViewById(R.id.favourite);

			property_amount.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyFavouriteActivity.this,
							PropertyDetailsActivity.class);
					startActivity(intent);
					finish();

				}
			});
			try {
				if (map_loc_info.getPrice_range().equals("")) {
					property_amount.setText("NA");
				} else {
					System.out.println(NumberFormat.getIntegerInstance()
							.format(Integer.parseInt(map_loc_info
									.getPrice_range())));
					String amount = NumberFormat.getIntegerInstance().format(
							Integer.parseInt(map_loc_info.getPrice_range()));
					String am = amount.substring(0, amount.lastIndexOf(","));

					property_amount.setText("$" + am + " k");
				}

				property_address.setText(map_loc_info.getAddress());
				property_details.setText(map_loc_info.getBeds() + " " + "bed"
						+ " " + map_loc_info.getBaths() + " " + "bath");
				mDate.setText(GlobalMethods.timeDiff(map_loc_info
						.getProperty_posted_user_details().get(0).getDate()));

				if ((map_loc_info.getReview_count().equalsIgnoreCase(" "))) {
					mReviewsCount.setText("0" + " " + "Review");
				}
				mReviewsCount.setText(map_loc_info.getReview_count() + " "
						+ "Reviews");

				float propertyRating = (float) 0.0;
				if (map_loc_info.getProperty_rating().equalsIgnoreCase(" ")) {
					mRating.setRating(0);
				}

				try {
					propertyRating = Float.parseFloat(map_loc_info
							.getProperty_rating());
				} catch (Exception e) {
					propertyRating = (float) 0.0;
				}
				mRating.setRating(propertyRating);
			} catch (Exception e) {
				System.out.println(e);
			}
			mFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (UserID.equalsIgnoreCase(" ")
							&& UserID.equalsIgnoreCase("0")) {
						launchActivity(LoginActivity.class);
					} else {
						if (isfavourite) {
							callADDFavouriteService(map_loc_info.getPro_id());
							mFav.setBackgroundResource(R.drawable.favourite_disable);
							isfavourite = false;
						} else {
							callADDFavouriteService(map_loc_info.getPro_id());
							mFav.setBackgroundResource(R.drawable.favourite_enable);
							isfavourite = true;
						}
					}

				}
			});

			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			if (images.size() == 0 || images == null || images.isEmpty()) {
				imageView.setVisibility(View.GONE);
			}
			imageLoader.displayImage(images.get(position), imageView, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted() {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(FailReason failReason) {

							spinner.setVisibility(View.GONE);
							// imageView.setImageResource(R.drawable.ic_launcher);
						}

						@Override
						public void onLoadingComplete(final Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							try {
								Animation anim = AnimationUtils.loadAnimation(
										MyFavouriteActivity.this,
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

	public void addImages(int position) {

		mPropertyPic = mFavDetails.get(position).getProperty_pics();
		if (mPropertyPic != null) {
			for (int i = 0; i < mPropertyPic.size(); i++) {
				if (mPropertyPic.get(i).getFile() != null
						&& !mPropertyPic.get(i).getFile().trim().equals("")) {
					imageUrls.add(mPropertyPic.get(i).getFile());

				}
			}
		}

	}

	// public void showMyBoardsAdapter(String prop_id) {
	// Intent inte = new Intent(MyFavouriteActivity.this,
	// BoardsChatActivity.class);
	// inte.putExtra("property_id", prop_id);
	// overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	// startActivity(inte);
	// }

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View v, int position, long
	// arg3) {
	// if (mBoardDetails != null && !mBoardDetails.isEmpty()) {
	// showMyBoardsAdapter(mBoardDetails.get(position).getProperty_id());
	//
	// if (UserID.equalsIgnoreCase(mBoardDetails.get(position)
	// .getProperty_details().get(0).getUser_id())) {
	// AppConstants.user_property = "1";
	// } else {
	// AppConstants.user_property = "0";
	// }
	// }
	// }

	public void callADDFavouriteService(String propertyID) {
		service = 1;
		String url = AppConstants.BASE_URL + "addtofavorite";

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("property_id", propertyID);

		ServiceRequestHandler.getInstance().CommonService(url, aq(), this,
				params);

	}

	public void callSetBoardsService(String propertyID) {
		service = 2;
		String url = AppConstants.BASE_URL + "addtoboards";

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("property_id", propertyID);

		ServiceRequestHandler.getInstance().CommonService(url, aq(), this,
				params);
	}

	public void getFavouriteListService() {
		service = 3;
		String url = AppConstants.BASE_URL + "addtofavorite/view";

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);

		ServiceRequestHandler.getInstance().getFavouriteService(url, aq(),
				this, params);
	}

	public void getBoardsListService() {
		service = 4;
		String url = AppConstants.BASE_URL + "postboards/view";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		// ServiceRequestHandler.getInstance().getBoardsService(url, aq(), this,
		// params);
		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {
								super.callback(url, json, status);

								if (json != null) {
									BoardsResponse obj = new Gson().fromJson(
											json.toString(),
											BoardsResponse.class);

									onRequestSuccess(obj);

								} else {
									statusErrorCode(status);
								}
							}

						});
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		super.onRequestSuccess(responseObj);
		if (service == 1 || service == 2) {
			CommonResponse commonresponse = (CommonResponse) responseObj;
			if (commonresponse.error_code
					.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				getFavouriteListService();
			} else {
				DialogManager.showCustomAlertDialog(MyFavouriteActivity.this,
						MyFavouriteActivity.this, commonresponse.getMsg());
			}
		}

	}

	@Override
	public void onRequestSuccess(FavouriteReponse responseObj) {
		super.onRequestSuccess(responseObj);
		FavouriteReponse favResponse = (FavouriteReponse) responseObj;
		if (favResponse.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			mFavDetails = favResponse.getResult();

			setListAdpater();
		} else {
			DialogManager.showCustomAlertDialog(MyFavouriteActivity.this,
					MyFavouriteActivity.this, favResponse.getMsg());
		}
	}

	@Override
	public void onRequestSuccess(BoardsResponse responseObj) {
		super.onRequestSuccess(responseObj);
		BoardsResponse boardResponse = (BoardsResponse) responseObj;
		if (boardResponse.error_code
				.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			mBoardDetails = boardResponse.getResult();
			setGridAdapter();
		} else {
			DialogManager.showCustomAlertDialog(MyFavouriteActivity.this,
					MyFavouriteActivity.this, boardResponse.getMsg());
		}
	}

	@Override
	public void onRequestSuccess(PropertyResponse responseObj) {

		PropertyResponse propertyResponse = (PropertyResponse) responseObj;
		if (propertyResponse.error_code
				.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			mProperty = propertyResponse.getResult();
			ShowListAdapter();

		} else {
			DialogManager.showCustomAlertDialog(MyFavouriteActivity.this,
					MyFavouriteActivity.this, propertyResponse.getMsg());
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		launchActivity(MapFragmentActivity.class);
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	public void onUserClick(View v) {
		onMenuUserNameClick(v);
	}

	public void onbuyMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuBuyClick(v);
			slide_holder.toggle();
		}
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {

	}
}
