package com.smaat.renterblock.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.util.TypefaceSingleton;

public class WriteReviewActivity extends BaseActivity implements
		OnClickListener {
	private EditText mReviewText;
	private TextView mClose, mContinue;
	private Bundle mBundle;
	private String mReviewType, mCommentStr = "", mRating = "0.0",
			mPropertyId = "", mPropertyReviewId = "", mPropertyCommentId = "";
	private RatingBar mRatingBar;
	private TextView mReviewHeaderText;
	private LinearLayout mUpdateReviewAlertLay;
	private String mPropertyType;
	private Typeface mTypeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_write_review);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypeface);
		setupUI(root);

		initComponents();
	}

	private void initComponents() {

		mReviewText = (EditText) findViewById(R.id.review_edit);
		mReviewText.setHint(getString(R.string.review_hint));
		mReviewHeaderText = (TextView) findViewById(R.id.review_head_txt);
		mRatingBar = (RatingBar) findViewById(R.id.review_ratingbar);

		mClose = (TextView) findViewById(R.id.close);
		mContinue = (TextView) findViewById(R.id.continue_txt);
		mClose.setTypeface(helvetica_bold);
		mContinue.setTypeface(helvetica_bold);

		mUpdateReviewAlertLay = (LinearLayout) findViewById(R.id.update_review_alert_lay);
		mReviewText.setMovementMethod(new ScrollingMovementMethod());

		mReviewText.setCursorVisible(true);

		mClose.setOnClickListener(this);
		mContinue.setOnClickListener(this);

		mReviewText.post(new Runnable() {
			@Override
			public void run() {
				mReviewText.setSelection(mReviewText.getText().toString()
						.length());
			}
		});

		mBundle = getIntent().getExtras();

		if (mBundle != null) {
			mReviewType = mBundle.getString("ReviewType");

			mCommentStr = mBundle.getString("Comments");
			mReviewText.setText(mCommentStr);

			mPropertyId = mBundle.getString("PropertyId");
			mPropertyReviewId = mBundle.getString("PropertyReviewId");
			mPropertyCommentId = mBundle.getString("PropertyCommentId");
			mRating = mBundle.getString("Rating");
			mPropertyType = mBundle.getString("PropType");

			mRatingBar.setRating(Float.parseFloat(mRating));

			if (mReviewType.equalsIgnoreCase("Edit")) {
				mRatingBar.setIsIndicator(false);
				mUpdateReviewAlertLay.setVisibility(View.GONE);
				mReviewHeaderText.setText("Edit Review");
			} else if (mReviewType.equalsIgnoreCase("Update")) {
				mRatingBar.setIsIndicator(false);
				mUpdateReviewAlertLay.setVisibility(View.VISIBLE);
				mReviewHeaderText.setText("Update Review");
			} else if (mReviewType.equalsIgnoreCase("Post")) {
				mRatingBar.setIsIndicator(false);
				mUpdateReviewAlertLay.setVisibility(View.GONE);
				mReviewHeaderText.setText("Post Review");
			}
		}

		mRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						mRating = (Float.toString(mRatingBar.getRating()));

						System.out.println(mRating + "rATING"
								+ ratingBar.getRating() + "float" + rating);

					}
				});
	}

	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_txt:

			Intent intent = new Intent(WriteReviewActivity.this,
					PostReviewActivty.class);
			intent.putExtra("ReviewText", mReviewText.getText().toString());
			intent.putExtra("ReviewType", mReviewType);
			intent.putExtra("PropertyId", mPropertyId);
			intent.putExtra("PropertyReviewId", mPropertyReviewId);
			intent.putExtra("PropertyCommentId", mPropertyCommentId);
			intent.putExtra("Rating", mRating);
			intent.putExtra("PropType", mPropertyType);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			startActivity(intent);

			break;

		case R.id.close:
			// if (mBundle != null) {
			// mReviewType = mBundle.getString("ReviewType");
			// if (mReviewType.equalsIgnoreCase("Edit")) {
			// launchActivity(MyReviewActivity.class);
			// } else if (mReviewType.equalsIgnoreCase("Update")) {
			// launchActivity(MyReviewActivity.class);
			// } else if (mReviewType.equalsIgnoreCase("Post")) {
			// launchActivity(PropertyDetailsActivity.class);
			// } else {
			launchActivity(MyReviewActivity.class);

			// }
			// }

			break;
		}

	}

}
