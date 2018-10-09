package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyReview;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class RecommendedReviewsAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<PropertyReview> mPropertyReviewList;
	Typeface helvetica_normal, helvetica_bold, helvetica_light;
	String UserID;
	private Dialog rev_dialog;

	public RecommendedReviewsAdapter(Context context,
			ArrayList<PropertyReview> mPropertyReviewResponse) {
		mContext = context;
		mPropertyReviewList = mPropertyReviewResponse;

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	static class Holder {

		private TextView review_user_name, recommand_review_comment,
				recommand_review_time;
		private RatingBar recommand_revie_rating;
		private ImageView recommand_review_image;
		private RelativeLayout recommend_img_lay;
		private Button chat_icon_adapter;
		private RelativeLayout mRecommend_lay;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final PropertyReview mPropertyReview = mPropertyReviewList
				.get(position);
		if(convertView == null){
			
		
		Holder holder = new Holder();

		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(
				R.layout.adapter_recommended_reviews, null);

		holder.review_user_name = (TextView) convertView
				.findViewById(R.id.review_user_name);
		holder.review_user_name.setTypeface(helvetica_normal);
		holder.recommand_review_comment = (TextView) convertView
				.findViewById(R.id.recommand_review_comment);
		holder.recommand_review_comment.setTypeface(helvetica_light);
		holder.recommand_review_time = (TextView) convertView
				.findViewById(R.id.recommand_review_time);
		holder.recommand_review_time.setTypeface(helvetica_normal);
		holder.recommand_revie_rating = (RatingBar) convertView
				.findViewById(R.id.recommand_revie_rating);
		holder.recommend_img_lay = (RelativeLayout) convertView
				.findViewById(R.id.recommend_img_lay);
		holder.recommend_img_lay.setTag(position);

		holder.chat_icon_adapter = (Button) convertView
				.findViewById(R.id.chat_icon_adapter);

		holder.mRecommend_lay = (RelativeLayout) convertView
				.findViewById(R.id.recommend_lay1);
		holder.mRecommend_lay.setTag(position);

		holder.chat_icon_adapter.setTag(position);

		UserID = GlobalMethods.getUserID(mContext);

		holder.mRecommend_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				showReviewDialog(mPropertyReviewList.get(pos), pos);
			}
		});

		if (mPropertyReviewList.get(position).getReview_user_id()
				.equalsIgnoreCase(UserID)) {
			holder.chat_icon_adapter.setVisibility(View.GONE);
		} else {
			holder.chat_icon_adapter.setVisibility(View.VISIBLE);
		}

		holder.chat_icon_adapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				PropertyDetailsActivity.adapterChatService(UserID,
						mPropertyReviewList.get(pos).getReview_user_details()
								.get(0).getUser_id(), pos);
			}
		});

		holder.recommend_img_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				Intent profile = new Intent(mContext, ProfileScreen.class);
				profile.putExtra("user_id", mPropertyReview
						.getReview_user_details().get(pos).getUser_id());
				profile.putExtra("call_from", "Agent");
				mContext.startActivity(profile);

			}
		});

		convertView.setTag(holder);
		}
		
		Holder holder = (Holder) convertView.getTag();
		AQuery aq = new AQuery(mContext).recycle(convertView);

		aq.id(R.id.user_img_view)
				.progress(R.id.review_progress_bar)
				.image(mPropertyReview.getReview_user_details().get(0)
						.getUser_pic(), true, true, 0, R.drawable.profile_pic,
						null, 0, 1.0f / 1.0f);
		float rating = Float.parseFloat(mPropertyReview.getRating());
		holder.recommand_revie_rating.setRating(rating);
		holder.review_user_name.setText(mPropertyReview
				.getReview_user_details().get(0).getFirst_name());
		holder.recommand_review_comment.setText(mPropertyReview.getComments());
		holder.recommand_review_time.setText(GlobalMethods
				.timeDiff(mPropertyReview.getDate_time()));

		return convertView;
	}

	private void showReviewDialog(final PropertyReview propertyReview, final int pos) {
		TextView dialog_review_user_name, dialog_recommand_review_comment, dialog_recommand_review_time;
		RatingBar dialog_recommand_revie_rating;
		ImageView dialog_recommand_review_image;
		Button dialog_chat_icon_adapter;

		rev_dialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		rev_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		rev_dialog.setContentView(R.layout.dialog_recommended_review);
		rev_dialog.setCancelable(true);

		dialog_review_user_name = (TextView) rev_dialog
				.findViewById(R.id.review_user_name);
		dialog_review_user_name.setTypeface(helvetica_normal);
		dialog_review_user_name.setText(propertyReview.getReview_user_details()
				.get(pos).getUser_name());

		dialog_recommand_review_comment = (TextView) rev_dialog
				.findViewById(R.id.recommand_review_comment);
		dialog_recommand_review_comment.setTypeface(helvetica_light);
		dialog_recommand_review_comment.setText(propertyReview.getComments());

		dialog_recommand_review_time = (TextView) rev_dialog
				.findViewById(R.id.recommand_review_time);
		dialog_recommand_review_time.setTypeface(helvetica_normal);
		dialog_recommand_review_time.setText(GlobalMethods
				.timeDiff(propertyReview.getDate_time()));

		dialog_recommand_revie_rating = (RatingBar) rev_dialog
				.findViewById(R.id.recommand_revie_rating);
		float rating = Float.parseFloat(propertyReview.getRating());
		dialog_recommand_revie_rating.setRating(rating);

		dialog_chat_icon_adapter = (Button) rev_dialog
				.findViewById(R.id.chat_icon_adapter);

		if (propertyReview.getReview_user_id().equalsIgnoreCase(UserID)) {
			dialog_chat_icon_adapter.setVisibility(View.GONE);
		} else {
			dialog_chat_icon_adapter.setVisibility(View.VISIBLE);
		}

		dialog_chat_icon_adapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PropertyDetailsActivity.adapterChatService(UserID,
						propertyReview.getReview_user_details().get(pos)
								.getUser_id(), pos);
				// mPropertyReviewList.get(pos).getReview_user_details()
				// .get(0).getUser_id(), pos);
			}
		});

		rev_dialog.show();

	}

	@Override
	public int getCount() {
		return mPropertyReviewList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mPropertyReviewList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
}
