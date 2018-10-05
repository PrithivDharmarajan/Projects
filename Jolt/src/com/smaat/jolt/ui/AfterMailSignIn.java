package com.smaat.jolt.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.util.TypefaceSingleton;

public class AfterMailSignIn extends BaseActivity {
	private TextView mCheckInbox, mSentMailId, mOpenMsg;
	private Typeface kGBlankSpace, helveticaNeueMedium;
	private LinearLayout mTryAgainLay;
	private Bundle mBundle;
	private String mEmailId, mUserName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_with_email_aft_check);
		helveticaNeueMedium = TypefaceSingleton.getTypeface()
				.getHelveticaNeueMedium(this);
		kGBlankSpace = TypefaceSingleton.getTypeface().getKGBlankSpace(this);
		mCheckInbox = (TextView) findViewById(R.id.check_inbox);
		mSentMailId = (TextView) findViewById(R.id.sent_mail_id);
		mOpenMsg = (TextView) findViewById(R.id.open_msg);
		mTryAgainLay = (LinearLayout) findViewById(R.id.try_again_lay);
		mCheckInbox.setTypeface(kGBlankSpace);
		mOpenMsg.setTypeface(helveticaNeueMedium);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mEmailId = mBundle.getString("EMAIL");
			mUserName = mBundle.getString("USER");
			if (mEmailId != null) {
				mSentMailId.setText(mEmailId);
			}
			if (mUserName != null) {
			}
		}
		mTryAgainLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}