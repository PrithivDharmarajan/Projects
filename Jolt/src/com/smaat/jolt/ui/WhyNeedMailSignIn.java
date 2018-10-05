package com.smaat.jolt.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaat.jolt.R;

public class WhyNeedMailSignIn extends BaseActivity {
	private static TextView mOkGotItTxt, mHeaderText, mWhyDontNeedDetails;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_with_mail_dont_need_pwd);

		ViewGroup root = (ViewGroup) findViewById(R.id.email_sign_in_lay);
		setFont(root, HomeScreen.helveticaNeueRegular);

		mOkGotItTxt = (TextView) findViewById(R.id.ok_got_it_txt);
		mWhyDontNeedDetails = (TextView) findViewById(R.id.why_dont_need_details);

		mHeaderText = (TextView) findViewById(R.id.why_dont_need_txt);
		mHeaderText.setTypeface(HomeScreen.kGBlankSpace);
		mOkGotItTxt.setTypeface(HomeScreen.helveticaNeueBold);
		mWhyDontNeedDetails.setTypeface(HomeScreen.helveticaNeueMedium);
	}

	public void onClick(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}