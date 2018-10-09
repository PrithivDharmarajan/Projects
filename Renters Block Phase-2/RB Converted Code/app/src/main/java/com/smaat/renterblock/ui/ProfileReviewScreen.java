package com.smaat.renterblock.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.profile.Entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.profile.adapter.ProfileReviewScreenAdapter;

public class ProfileReviewScreen extends BaseActivity {

	private ListView profile_rev_list;
	private ArrayList<ProfileMyFeedsEntity> review_list = new ArrayList<ProfileMyFeedsEntity>();
	private ArrayList<ProfileMyFeedsEntity> review_list_temp = new ArrayList<ProfileMyFeedsEntity>();
	private ProfileReviewScreenAdapter my_reviews_adapter;
	ProfileMyFeedsEntity review_ent;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_review_activity);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			review_list = (ArrayList<ProfileMyFeedsEntity>) extras
					.getSerializable("review_list");
		}

		profile_rev_list = (ListView) findViewById(R.id.review_list);

		for (int i = 0; i < review_list.size(); i++) {
			if (review_list.get(i).getComments() != null) {
				review_ent = new ProfileMyFeedsEntity();
				review_ent.setProperty_review_id(review_list.get(i)
						.getProperty_review_id());
				review_ent.setProperty_id(review_list.get(i).getProperty_id());
				review_ent.setReview_user_id(review_list.get(i)
						.getReview_user_id());
				review_ent.setComments(review_list.get(i).getComments());
				review_ent.setRating(review_list.get(i).getRating());
				review_ent.setDate_time(review_list.get(i).getDate_time());
				review_ent.setProperty_name(review_list.get(i)
						.getProperty_name());
				review_ent.setFriends_count(review_list.get(i)
						.getFriends_count());
				review_ent.setReviews_count(review_list.get(i)
						.getReviews_count());
				review_ent
						.setPhotos_Count(review_list.get(i).getPhotos_Count());
				review_ent.setUser_name(review_list.get(i).getUser_name());
				review_ent.setUser_profile_image(review_list.get(i)
						.getUser_profile_image());
				review_list_temp.add(review_ent);
			}
		}

		my_reviews_adapter = new ProfileReviewScreenAdapter(
				ProfileReviewScreen.this,
				R.layout.my_feeds_profile_list_adapter, review_list_temp);
		profile_rev_list.setAdapter(my_reviews_adapter);
		my_reviews_adapter.notifyDataSetChanged();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			launchActivity(ProfileScreen.class);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
}
