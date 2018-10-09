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

import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.R;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

public class TutorialScreen extends BaseActivity {

	private Button continue_btn;
	private ViewPager pager;
	private ImageView slidepoint1, slidepoint2, slidepoint3;
	private String[] tutorial_text;
	private TextView title;
	String mTemp;
	Typeface helvetica_normal, helvetica_bold, helvetica_light, hightower;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				this);
		hightower = TypefaceSingleton.getInstance().getHighTower(this);
		setupUI(mViewGroup);
		initview();
	}

	private void initview() {
		mTemp = ((String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.TUTORIAL_SCREEN));
		title = (TextView) findViewById(R.id.header_text);
		title.setTypeface(hightower);

		continue_btn = (Button) findViewById(R.id.continue_btn);
		continue_btn.setTypeface(helvetica_bold);
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
			// if (position == 0) {
			//
			// title.setText(R.string.business);
			//
			// }
			// if (position == 1) {
			//
			// title.setText(R.string.customer);
			// }
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
			swipe_text.setTypeface(helvetica_bold);
			final Button mSwipeImage = (Button) fragment_text
					.findViewById(R.id.swipe_image);

			swipe_text.setText(tutorial_text[position]);
			// swipe_text.setTypeface(TypefaceSingleton.getInstance()
			// .getHelvetica(TutorialScreen.this));

			if (position == 0) {
				mSwipeImage
						.setBackgroundResource(R.drawable.ipharma_tutorial_1);
			} else if (position == 1) {
				mSwipeImage
						.setBackgroundResource(R.drawable.ipharma_tutorial_2);
			} else if (position == 2) {
				mSwipeImage
						.setBackgroundResource(R.drawable.ipharma_tutorial_3);
			}

			// mSwipeImage.setImageResource(position == 0 ? R.color.dark_grey
			// : R.color.gray_2);
			// mSwipeImage.setImageResource(position == 1 ? R.color.dark_grey
			// : R.color.gray_2);
			// mSwipeImage.setImageResource(position == 2 ? R.color.dark_grey
			// : R.color.gray_2);

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
				.setImageResource(position == 0 ? R.drawable.ipharma_green_dot
						: R.drawable.ipharma_grey_dot);
		slidepoint2
				.setImageResource(position == 1 ? R.drawable.ipharma_green_dot
						: R.drawable.ipharma_grey_dot);
		slidepoint3
				.setImageResource(position == 2 ? R.drawable.ipharma_green_dot
						: R.drawable.ipharma_grey_dot);

	}

	public void onClick(View login) {
		// GlobalMethods.storeValuetoPreference(this,
		// GlobalMethods.BOOLEAN_PREFERENCE,
		// AYSAAppConstants.pref_isTutorailSeen, true);

		if (((String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.TUTORIAL_SCREEN))
				.equalsIgnoreCase(AppConstants.TUTORIAL)) {

			finish();
		} else {
			launchActivity(LoginActivity.class);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
