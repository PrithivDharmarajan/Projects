package com.smaat.renterblock.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.MyReviewUserDetails;
import com.smaat.renterblock.entity.MyReviews;
import com.smaat.renterblock.entity.MyReviewsResult;
import com.smaat.renterblock.entity.PropertyReviewComments;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import java.util.ArrayList;

public class EditUpdateReviewActivity extends BaseActivity implements OnClickListener {

	private LinearLayout mBackIcon, mViewBusiness;
	private Bundle mBundle;
	private MyReviewUserDetails UserDetails;
	private ArrayList<PropertyReviewComments> mPropertyReviewComments;
	private TextView mUserName, mFriendsCount, mReviewsCount, mPhotosCount, mAddress, mTime, mMainComment;
	private TextView mCommentText, mTimeTxt;
	private MyReviewsResult mResult;
	private ArrayList<MyReviews> mMyReviews;
	private int pos;
	private RatingBar mRatingBar, mUpdatedRatingBar;
	private Button mEdit, mUpdate, mView_list;
	private TableLayout mReviewCommentsLay;
	private String mPropertyId = "", mPropertyReviewId = "", mPropertyCommentId = "", mComments = "", mRating = "0.0";
	private Typeface mTypeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editupdate_review);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);

		initComponents();
	}

	private void initComponents() {

		mBackIcon = (LinearLayout) findViewById(R.id.back_icon);
		mViewBusiness = (LinearLayout) findViewById(R.id.view_business);

		mUserName = (TextView) findViewById(R.id.user_name);
		mUserName.setTypeface(helvetica_bold);
		mFriendsCount = (TextView) findViewById(R.id.friends_count);
		mReviewsCount = (TextView) findViewById(R.id.reviews_count);
		mPhotosCount = (TextView) findViewById(R.id.photos_count);
		mAddress = (TextView) findViewById(R.id.address);
		mTime = (TextView) findViewById(R.id.minutes_txt);
		mMainComment = (TextView) findViewById(R.id.main_comment);

		mReviewCommentsLay = (TableLayout) findViewById(R.id.comment_list);
		mEdit = (Button) findViewById(R.id.edit_btn);
		mUpdate = (Button) findViewById(R.id.update_btn);

		mEdit.setTypeface(helvetica_bold);
		mUpdate.setTypeface(helvetica_bold);

		mRatingBar = (RatingBar) findViewById(R.id.user_ratingbar);

		mView_list = (Button) findViewById(R.id.view_list);
		mView_list.setTypeface(helvetica_bold);

		mBackIcon.setOnClickListener(this);
		mViewBusiness.setOnClickListener(this);
		mEdit.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {

			mResult = (MyReviewsResult) mBundle.getSerializable("mMyReviewsResult");
			pos = mBundle.getInt("pos");
			mMyReviews = mResult.getReview();
			UserDetails = mResult.getUser_details();
			mPropertyReviewComments = mResult.getReview().get(pos).getProperty_review_comment();
			mPropertyId = mResult.getReview().get(pos).getProperty_id();
			mPropertyReviewId = mResult.getReview().get(pos).getProperty_review_id();

			if (mMyReviews.get(pos).getProperty_name() != null && mMyReviews.get(pos).getAddress() != null) {
//				if (mMyReviews.get(pos).getProperty_name().equalsIgnoreCase("")) {
					mAddress.setText(mMyReviews.get(pos).getAddress());
//				} else {
//					mAddress.setText(mMyReviews.get(pos).getProperty_name());
//				}
			} else {
				mAddress.setText("");
			}

			mMainComment.setText(mMyReviews.get(pos).getComments());
			mUserName.setText(UserDetails.getUser_name());
			mFriendsCount.setText(UserDetails.getGetfriends());
			mReviewsCount.setText(UserDetails.getGetreviewcount());
			mPhotosCount.setText(UserDetails.getGetproimagecount());
			mTime.setText(GlobalMethods.timeDiff(mMyReviews.get(pos).getDate_time()));

			AQuery aq1 = new AQuery(this);

			aq1.id(R.id.user_image).image(UserDetails.getUser_pic(), true, true, 0, R.drawable.profile_pic, null, 0,
					1.0f);

			float rating = Float.parseFloat(mMyReviews.get(pos).getRating());
			mRatingBar.setRating(rating);
			if (mPropertyReviewComments.size() != 0) {
				setCommentsAdapter(mPropertyReviewComments);

				mPropertyCommentId = mPropertyReviewComments.get(mPropertyReviewComments.size() - 1)
						.getProperty_review_comment_id();

				mComments = mPropertyReviewComments.get(mPropertyReviewComments.size() - 1).getReview_comments();

				mRating = mPropertyReviewComments.get(mPropertyReviewComments.size() - 1).getReview_rating();
			} else {
				mPropertyCommentId = "";
				mComments = mMainComment.getText().toString();
				mRating = Float.toString(mRatingBar.getRating());
			}
		}
	}

	private void setCommentsAdapter(ArrayList<PropertyReviewComments> mPropertyReviewComments) {

		mReviewCommentsLay.removeAllViews();

		if (mPropertyReviewComments != null && mPropertyReviewComments.size() > 0) {
			for (int i = 0; i < mPropertyReviewComments.size(); i++) {

				final PropertyReviewComments mComments = mPropertyReviewComments.get(i);

				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				final View convertView = inflater.inflate(R.layout.adapter_review_comments, null);

				mCommentText = (TextView) convertView.findViewById(R.id.comment_text);
				mTimeTxt = (TextView) convertView.findViewById(R.id.minutes_txt);
				mUpdatedRatingBar = (RatingBar) convertView.findViewById(R.id.update_ratingbar);

				mCommentText.setTypeface(helvetica_normal);
				mTimeTxt.setTypeface(helvetica_normal);

				mCommentText.setText(mComments.getReview_comments());
				mTimeTxt.setText(GlobalMethods.timeDiff(mComments.getReview_date_time()));

				float rating = Float.parseFloat(mComments.getReview_rating());
				mUpdatedRatingBar.setRating(rating);

				mReviewCommentsLay.addView(convertView);

			}
		}
	}

	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			launchActivity(MyReviewActivity.class);
			break;

		case R.id.view_business:
			Intent view_business = new Intent(EditUpdateReviewActivity.this, PropertyDetailsActivity.class);
			view_business.putExtra("PropertyId", mPropertyId);
			view_business.putExtra("PropType", "");
			// view_business.putExtra("CallFrom", "reviews");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(view_business);
			break;

		case R.id.edit_btn:
			Intent edit = new Intent(EditUpdateReviewActivity.this, WriteReviewActivity.class);
			edit.putExtra("ReviewType", "Edit");
			edit.putExtra("Comments", mComments);
			edit.putExtra("PropertyId", mPropertyId);
			edit.putExtra("PropertyReviewId", mPropertyReviewId);
			edit.putExtra("PropertyCommentId", mPropertyCommentId);
			edit.putExtra("Rating", mRating);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(edit);
			break;

		case R.id.update_btn:
			Intent update = new Intent(EditUpdateReviewActivity.this, WriteReviewActivity.class);
			update.putExtra("ReviewType", "Update");
			update.putExtra("Comments", "");
			update.putExtra("PropertyId", mPropertyId);
			update.putExtra("PropertyReviewId", mPropertyReviewId);
			update.putExtra("Rating", mRating);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(update);
			break;

		}

	}
}
