package com.smaat.renterblock.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.TypefaceSingleton;

public class TutorialScreen extends BaseActivity {

	private Button continue_btn;
	private ViewPager pager;
	private ImageView slidepoint1, slidepoint2, slidepoint3, slidepoint4,
			slidepoint5, back_arrow;
	private String[] tutorial_text, tutorial_text1;
	SharedPreferences pref;
	Editor editor;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_screen);

		AppConstants.Login_From_Map = "false";
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		context = TutorialScreen.this;
		continue_btn = (Button) findViewById(R.id.get_started);
		slidepoint1 = (ImageView) findViewById(R.id.slidepointer_one);
		slidepoint2 = (ImageView) findViewById(R.id.slidepointer_two);
		slidepoint3 = (ImageView) findViewById(R.id.slidepointer_three);
		slidepoint4 = (ImageView) findViewById(R.id.slidepointer_four);
		slidepoint5 = (ImageView) findViewById(R.id.slidepointer_five);
		tutorial_text = getResources().getStringArray(R.array.tutorial_text);
		tutorial_text1 = getResources().getStringArray(R.array.desc_text);

		pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setAdapter(new TextPageAdapter(tutorial_text));
		pager.setCurrentItem(0);

		pager.setOnPageChangeListener(new DetailOnPageChangeListener());

	}

	public void onClick(View v) {
		AppConstants.GET_START = "GET_START";
		Intent in = new Intent(TutorialScreen.this, LoginActivity.class);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		startActivity(in);
	}

	class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int position) {

			setImageBackground(position);

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (position == 4) {
				continue_btn.setVisibility(View.VISIBLE);
			} else {
				continue_btn.setVisibility(View.GONE);
			}
			pager.getParent().requestDisallowInterceptTouchEvent(true);

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

			final TextView swipe_text = (TextView) fragment_text
					.findViewById(R.id.swipe_text);
			final TextView swipe_text1 = (TextView) fragment_text
					.findViewById(R.id.swipe_text1);
			final ImageView mSlidepointer_one = (ImageView) fragment_text
					.findViewById(R.id.slidepointer_one);

			swipe_text.setText(tutorial_text[position]);
			swipe_text1.setText(tutorial_text1[position]);
			swipe_text1.setTextColor(Color.parseColor("#5d5d5d"));

			((ViewPager) view).addView(fragment_text, 0);
			ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
			Typeface mTypeface = TypefaceSingleton.getInstance()
					.getHelveticaBold(context);
			// setFont(mViewGroup, mTypeface);

			Typeface mType = TypefaceSingleton.getInstance().getHelvetica(
					context);
			swipe_text1.setTypeface(mType);
			swipe_text.setTypeface(mTypeface);
			
			if(position == 0) {
				mSlidepointer_one.setImageResource(R.drawable.tut_screen_1);
			} else if(position == 1) {
				mSlidepointer_one.setImageResource(R.drawable.tut_screen_2);
			} else if(position == 2) {
				mSlidepointer_one.setImageResource(R.drawable.tut_screen_3);
			} else if(position == 3) {
				mSlidepointer_one.setImageResource(R.drawable.tut_screen_4);
			} else if(position == 4) {
				mSlidepointer_one.setImageResource(R.drawable.tut_screen_5);
			}

			return fragment_text;
		}

	}

	public void setImageBackground(int position) {

		slidepoint1.setImageResource(position == 0 ? R.drawable.page_enable
				: R.drawable.page_disable);
		slidepoint2.setImageResource(position == 1 ? R.drawable.page_enable
				: R.drawable.page_disable);
		slidepoint3.setImageResource(position == 2 ? R.drawable.page_enable
				: R.drawable.page_disable);
		slidepoint4.setImageResource(position == 3 ? R.drawable.page_enable
				: R.drawable.page_disable);
		slidepoint5.setImageResource(position == 4 ? R.drawable.page_enable
				: R.drawable.page_disable);

	}

}
