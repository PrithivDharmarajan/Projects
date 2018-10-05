package com.smaat.jolt.ui;

import java.util.ArrayList;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;
import com.smaat.jolt.util.TypefaceSingleton;

public class TutorialScreen extends BaseActivity {

	private Button continue_btn;
	private ViewPager pager;
	private ImageView slidepoint1, slidepoint2, slidepoint3, slidepoint4,
			mTopImg;
	private String[] tutorial_text1;
	SharedPreferences pref;
	Editor editor;
	TextView mTopText;
	RelativeLayout mTutorialBg;
	FrameLayout viewPager_bg;
	TextPageAdapter mAdapter;
	ArrayList<Integer> list = new ArrayList<Integer>();
	ArrayList<Integer> list1 = new ArrayList<Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_screen);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getTypeface().getHelveticaNeue(
				this);
		setFont(root, mTypeface);
		initComponents();
	}

	private void initComponents() {
		// TODO Auto-generated method stub

		continue_btn = (Button) findViewById(R.id.get_started);

		slidepoint1 = (ImageView) findViewById(R.id.slidepointer_one);
		slidepoint2 = (ImageView) findViewById(R.id.slidepointer_two);
		slidepoint3 = (ImageView) findViewById(R.id.slidepointer_three);
		slidepoint4 = (ImageView) findViewById(R.id.slidepointer_four);
		tutorial_text1 = getResources().getStringArray(R.array.desc_text);

		Typeface bold = TypefaceSingleton.getTypeface().getHelveticaNeueBold(
				TutorialScreen.this);
		continue_btn.setTypeface(bold);
		mAdapter = new TextPageAdapter(tutorial_text1);
		pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(0);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// mAdapter.showMenu();
			}

			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				if (pos == 0) {
					// mTopImg.setVisibility(View.GONE);

					continue_btn.setVisibility(View.INVISIBLE);
				} else if (pos == 1) {

					continue_btn.setVisibility(View.INVISIBLE);

				} else if (pos == 2) {
					continue_btn.setVisibility(View.INVISIBLE);
				} else if (pos == 3) {
					continue_btn.setVisibility(View.VISIBLE);
				}
				setImageBackground(pos);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		list.clear();
		list.add(R.drawable.why_dont_bg);
		list.add(R.drawable.tutorial2bg);
		list.add(R.drawable.tutorial3bg);
		list.add(R.drawable.tutorial4_bg);
		list1.clear();
		list1.add(0);
		list1.add(R.drawable.tutorial2);
		list1.add(R.drawable.tutorial3);
		list1.add(R.drawable.tutorial4);

	}

	public void onClick(View v) {

		if (((String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.TUTORIAL_SCREEN))
				.equalsIgnoreCase(AppConstants.IS_TUTORIAL_SCREEN)) {
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.BOOLEAN_PREFERENCE,
					AppConstants.TUTORIAL_SEEN, true);
			launchActivity(SignInScreen.class);
		} else {

			finish();
		}

	}

	class TextPageAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		public String[] text;

		public TextPageAdapter(String[] strings) {
			this.text = strings;
			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return text.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View fragment_text = inflater.inflate(
					R.layout.swipe_tutorial_view, null);

			final TextView swipe_text1 = (TextView) fragment_text
					.findViewById(R.id.swipe_text1);

			RelativeLayout mTutorialBg = (RelativeLayout) fragment_text
					.findViewById(R.id.scrollView1);

			TextView mTopText = (TextView) fragment_text
					.findViewById(R.id.top_text);

			mTopImg = (ImageView) fragment_text
					.findViewById(R.id.slidepointer_one_img);

			Typeface corbel = TypefaceSingleton.getTypeface().getCorbel(
					TutorialScreen.this);
			mTopText.setTypeface(corbel);

			Typeface kg = TypefaceSingleton.getTypeface().getKGBlankSpace(
					TutorialScreen.this);
			swipe_text1.setTypeface(kg);

			mTutorialBg.setBackgroundResource(list.get(position));
			mTopImg.setImageResource(list1.get(position));
			// showMenu();

			mTopText.setVisibility(View.VISIBLE);

			if (position == 0) {
				mTopText.setVisibility(View.VISIBLE);
			} else {
				mTopText.setVisibility(View.GONE);

			}
			if (position == 0) {

				mTopImg.setVisibility(View.GONE);
			} else {
				mTopImg.setVisibility(View.VISIBLE);
			}
			pager.getParent().requestDisallowInterceptTouchEvent(true);

			swipe_text1.setText(tutorial_text1[position]);

			((ViewPager) view).addView(fragment_text, 0);

			return fragment_text;
		}

		
	}

	private void setImageBackground(int position) {

		slidepoint1.setImageResource(position == 0 ? R.drawable.green_dot
				: R.drawable.grey_dot);
		slidepoint2.setImageResource(position == 1 ? R.drawable.green_dot
				: R.drawable.grey_dot);
		slidepoint3.setImageResource(position == 2 ? R.drawable.green_dot
				: R.drawable.grey_dot);
		slidepoint4.setImageResource(position == 3 ? R.drawable.green_dot
				: R.drawable.grey_dot);

	}

}
