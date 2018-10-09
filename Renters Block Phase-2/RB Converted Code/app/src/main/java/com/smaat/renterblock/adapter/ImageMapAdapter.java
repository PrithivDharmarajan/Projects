package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.R;

public class ImageMapAdapter extends BaseAdapter {
	private Context mContext;
	private int mLayoutId;
	private View cell;
	ArrayList<String> imageUrls = null;
	// private ViewPager pager;
	private Holder holder;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	Bitmap bitmap[];
	private DetailOnPageChangeListener listener;
	DisplayMetrics dm;

	public ImageMapAdapter(Context context, int layout) {
		super();
		mContext = context;
		mLayoutId = layout;
	}

	class Holder {
		ViewPager pager;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = null;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		holder = new Holder();
		convertView = inflater.inflate(mLayoutId, parent, false);
		imageUrls = new ArrayList<String>();
		holder.pager = (ViewPager) convertView.findViewById(R.id.pager);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
				.cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
		bitmap = new Bitmap[6];
		imageUrls
				.add("http://www.ht-real-estate.com/template/ht2014/images/landscape/05.jpg");
		imageUrls
				.add("https://sp.yimg.com/ib/th?id=HN.608018506045394196&pid=15.1&P=0");
		imageUrls
				.add("http://childrenshospitalblog.org/wp-content/uploads/2010/12/Yawkey-family-Inn.jpg");
		imageUrls
				.add("http://www.northeasternlogny.com/burnslider/source/images/slides/home7.jpg");
		imageUrls
				.add("http://www.vintageluxuryhomes.com/images/philosophy-1.jpg");
		imageUrls
				.add("https://caringresourcesinfo.files.wordpress.com/2011/01/tennessee-williams-home-in-columbus-ms_mr.jpg");
		holder.pager.setAdapter(new ImagePagerAdapter(imageUrls));
		// pager.setCurrentItem(0);

		holder.pager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		listener = new DetailOnPageChangeListener();
		holder.pager.setOnPageChangeListener(listener);
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		holder.pager.getLayoutParams().height = (int) (dm.heightPixels - ((dm.heightPixels * 2) / 5));

		holder.pager.getViewTreeObserver().addOnGlobalFocusChangeListener(
				new OnGlobalFocusChangeListener() {

					@Override
					public void onGlobalFocusChanged(View oldFocus,
							View newFocus) {

						View view = holder.pager.getChildAt(position);

						if (view != null) {
							view.bringToFront();
						}
					}
				});

		return convertView;
	}

	@Override
	public int getCount() {

		return 10;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {

		return position;
	}

	class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
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
			holder.pager.getParent().requestDisallowInterceptTouchEvent(true);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;

		ImagePagerAdapter(ArrayList<String> imageUrls) {
			this.images = imageUrls;
			inflater = ((Activity) mContext).getLayoutInflater();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
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
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			final TextView imageCount = (TextView) imageLayout
					.findViewById(R.id.image_count);
			imageCount.setText((position + 1) + "/ " + images.size());
			imageLoader.displayImage(images.get(position), imageView, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted() {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(FailReason failReason) {

							spinner.setVisibility(View.GONE);
							imageView.setImageResource(R.drawable.ic_launcher);
						}

						@Override
						public void onLoadingComplete(final Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							try {
								Animation anim = AnimationUtils.loadAnimation(
										mContext, R.anim.fade_in);
								imageView.setAnimation(anim);
								anim.start();
								((Activity) mContext)
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {

												int width = dm.widthPixels;
												int height = width
														* loadedImage
																.getHeight()
														/ loadedImage
																.getWidth();
												imageView.getLayoutParams().width = width;
												imageView.getLayoutParams().height = height;
												imageView
														.setImageBitmap(loadedImage);
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

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}
	}

}
