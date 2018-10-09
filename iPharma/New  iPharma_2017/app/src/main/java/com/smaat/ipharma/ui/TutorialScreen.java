package com.smaat.ipharma.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;


public class TutorialScreen extends BaseActivity {

	private Button continue_btn;
	private ViewPager pager;
	private ImageView slidepoint1, slidepoint2, slidepoint3;
	private String[] tutorial_text;
	private TextView title;
	String mTemp;
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		initview();
	}

	private void initview() {
		mTemp = ((String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.TUTORIAL_SCREEN));
		title = (TextView) findViewById(R.id.header_text);

		continue_btn = (Button) findViewById(R.id.continue_btn);

		if (mTemp.equalsIgnoreCase(AppConstants.TUTORIAL)) {
			continue_btn.setText(getString(R.string.Skip_text));
		} else {
			continue_btn.setText(getString(R.string.Skip_text));
		}
		title.setVisibility(View.VISIBLE);

		slidepoint1 = (ImageView) findViewById(R.id.slidepointer_one);
		slidepoint2 = (ImageView) findViewById(R.id.slidepointer_two);
		slidepoint3 = (ImageView) findViewById(R.id.slidepointer_three);

		tutorial_text = getResources().getStringArray(R.array.tutorial);

		pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setAdapter(new TextPageAdapter(tutorial_text));
		pager.setCurrentItem(0);

		pager.setOnPageChangeListener(new DetailOnPageChangeListener());
	}

	class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {

			currentPage = position;
			setImageBackground(position);

		}

		public int getCurrentPage() {
			return currentPage;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			pager.getParent().requestDisallowInterceptTouchEvent(true);
		}

	}

	class TextPageAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		public String[] text;
		Context context;

		public TextPageAdapter(String[] strings) {
			this.text = strings;
			inflater = getLayoutInflater();
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
			return text.length;
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View fragment_text = inflater.inflate(
					R.layout.swipe_tutorial_view, null);

			final TextView swipe_text = (TextView) fragment_text
					.findViewById(R.id.swipe_text);
			final Button mSwipeImage = (Button) fragment_text
					.findViewById(R.id.swipe_image);

			swipe_text.setText(tutorial_text[position]);
			// swipe_text.setTypeface(TypefaceSingleton.getInstance()
			// .getHelvetica(TutorialScreen.this));

			if (position == 0) {
				mSwipeImage.setBackgroundResource(R.drawable.ipharma_tutorial_1);
			} else if (position == 1) {
				mSwipeImage.setBackgroundResource(R.drawable.ipharma_tutorial_2);
			} else if (position == 2) {
				mSwipeImage.setBackgroundResource(R.drawable.ipharma_tutorial_3);
			}

			((ViewPager) view).addView(fragment_text, 0);

			return fragment_text;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}

	}

	/**
	 * @param position
	 *            set image background for screen slide move.
	 */
	public void setImageBackground(int position) {

		slidepoint1
				.setImageResource(position == 0 ? R.drawable.reddot
						: R.drawable.greydot);
		slidepoint2
				.setImageResource(position == 1 ? R.drawable.reddot
						: R.drawable.greydot);
		slidepoint3
				.setImageResource(position == 2 ? R.drawable.reddot
						: R.drawable.greydot);

	}

	public void onClick(View login) {

		launchScreen(HomeScreen.class);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
