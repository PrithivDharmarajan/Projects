package com.smaat.renterblock.myfavourite;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class MyFavouriteDeleteActivity extends BaseActivity {
	private View rootview;
	private String UserID;
	private Button mDelete;
	private ImageView mEmailOption, mAgentOption;
	private boolean isVisible = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favourite_edit_property);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		UserID = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_userId);
		initComponents();
	}

	private void initComponents() {
		mDelete = (Button) findViewById(R.id.delete);
		mEmailOption = (ImageView) findViewById(R.id.email_btn);
		mAgentOption = (ImageView) findViewById(R.id.agent_btn);
		// MapFragmentActivity.mSlide.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// EditFavouriteActivity mFragment = new EditFavouriteActivity();
		// ((MapFragmentActivity) getActivity())
		// .replaceFragment(mFragment);
		//
		// }
		// });
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.email_btn:
			if (isVisible) {
				mEmailOption.setBackgroundResource(R.drawable.button_on);
				isVisible = false;
			} else {
				mEmailOption.setBackgroundResource(R.drawable.button_off);
				isVisible = true;
			}
			break;
		case R.id.agent_btn:
			if (isVisible) {
				mAgentOption.setBackgroundResource(R.drawable.button_on);
				isVisible = false;
			} else {
				mAgentOption.setBackgroundResource(R.drawable.button_off);
				isVisible = true;
			}
			break;
		case R.id.slide:
			// EditFavouriteActivity mFragment = new EditFavouriteActivity();
			// ((MapFragmentActivity) getActivity()).replaceFragment(mFragment);
			break;
		default:
			break;
		}
	}
}
