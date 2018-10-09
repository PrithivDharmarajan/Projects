package com.smaat.renterblock.findagent;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.BaseActivity.GsonTransformer;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class AgentMoreReviewsAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<AgentReviewsEntity> mResult;
	private Holder holder = null;
	AQuery aq;

	public AgentMoreReviewsAdapter(Context context,
			ArrayList<AgentReviewsEntity> mReviewsEntityList) {
		mContext = context;
		mResult = mReviewsEntityList;
	}

	class Holder {

		private TextView mReviewCommentsTxt, mReviewCommentsFullTxt,
				mReadmoreTxt, mReivewUserName, mReviewFriendsCount,
				mReviewReviewsCount, mReviewPhotosCount;

		private RatingBar mReviewRatingBar;
		private ImageView review_user_image;
		private Button chat_icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		holder = new Holder();
		if (mResult.size() != 0) {

			AgentReviewsEntity mReviewsEntity = mResult.get(position);

			LayoutInflater mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mLayoutInflater.inflate(
					R.layout.adapter_agent_more_reviews, null);

			ViewGroup root = (ViewGroup) convertView
					.findViewById(R.id.parent_layout);
			Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
					mContext);
			Typeface mTypefaceBold = TypefaceSingleton.getInstance()
					.getHelveticaBold(mContext);
			((BaseActivity) mContext).setFont(root, mTypeface);

			aq = new AQuery(mContext).recycle(convertView);

			holder.mReivewUserName = (TextView) convertView
					.findViewById(R.id.review_user_name_txt);
			holder.mReivewUserName.setTypeface(mTypefaceBold);
			holder.mReviewCommentsTxt = (TextView) convertView
					.findViewById(R.id.review_comments_txt);
			holder.mReviewCommentsFullTxt = (TextView) convertView
					.findViewById(R.id.review_comments_full_txt);
			holder.mReviewFriendsCount = (TextView) convertView
					.findViewById(R.id.review_friends_count);
			holder.mReviewReviewsCount = (TextView) convertView
					.findViewById(R.id.review_reviews_count);
			holder.mReviewPhotosCount = (TextView) convertView
					.findViewById(R.id.review_photos_count);
			holder.mReadmoreTxt = (TextView) convertView
					.findViewById(R.id.read_more_txt);
			holder.mReadmoreTxt.setVisibility(View.GONE);
			holder.mReviewRatingBar = (RatingBar) convertView
					.findViewById(R.id.review_user_ratingbar);
			holder.review_user_image = (ImageView) convertView
					.findViewById(R.id.review_user_image);
			holder.review_user_image.setTag(position);

			holder.mReivewUserName.setText(mReviewsEntity.getName());
			holder.mReviewFriendsCount.setText(mReviewsEntity
					.getFriends_count());
			holder.mReviewReviewsCount.setText(mReviewsEntity
					.getReviews_count());
			holder.mReviewPhotosCount.setText(mReviewsEntity.getPhotos_count());
			holder.mReviewCommentsTxt.setText(mReviewsEntity.getComments());
			holder.mReviewCommentsFullTxt.setTypeface(mTypefaceBold);
			holder.mReviewCommentsFullTxt.setText(mReviewsEntity.getComments());

			holder.mReviewCommentsTxt.setVisibility(View.GONE);
			holder.mReviewCommentsFullTxt.setVisibility(View.VISIBLE);

			holder.chat_icon = (Button) convertView
					.findViewById(R.id.chat_icon);
			holder.chat_icon.setTag(position);
			final String UserID = GlobalMethods.getUserID(mContext);
			if (mResult.get(position).getUser_id().equalsIgnoreCase(UserID)) {
				holder.chat_icon.setVisibility(View.GONE);
			} else {
				holder.chat_icon.setVisibility(View.VISIBLE);
			}
			
			holder.chat_icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					callGroupIdService(UserID, mResult.get(pos).getUser_id(),
							pos);
				}
			});

			holder.review_user_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					Intent profile = new Intent(mContext, ProfileScreen.class);
					profile.putExtra("user_id", mResult.get(pos).getUser_id());
					profile.putExtra("call_from", "Agent");
					mContext.startActivity(profile);
				}
			});

			float reviewUserRating = Float.parseFloat(mReviewsEntity
					.getRating());
			holder.mReviewRatingBar.setRating(reviewUserRating);

			aq.id(R.id.review_user_image).image(
					mReviewsEntity.getUser_profileImage(), false, false, 200,
					R.drawable.profile_pic, null, 0);

			holder.mReivewUserName.setTypeface(mTypefaceBold);
		}
		return convertView;
	}
	
	private void callGroupIdService(String UserID, String Friend_UserID,
			final int pos) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", Friend_UserID);
		params.put("name", "");
		aq.transformer(t)
				.progress(DialogManager.getProgressDialog(mContext))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									ChatSendResponse obj = new Gson().fromJson(
											json.toString(),
											ChatSendResponse.class);
									callChatService(obj.result, pos);
								} else {
									// statusErrorCode(status);
								}
							}
						});
	}

	private void callChatService(String group_id, int pos) {
		Intent intent = new Intent(mContext, FriendChatScreen.class);
		intent.putExtra("ids", mResult.get(pos).getUser_id());
		intent.putExtra("names", mResult.get(pos).getName());
		intent.putExtra("groupId", group_id);
		intent.putExtra("type", "group");
		intent.putExtra("enhanced_profile_ids", mResult.get(pos)
				.getEnhanced_profile());
		if (mResult.get(pos).getIs_friend().equalsIgnoreCase("1")) {
			intent.putExtra("from_call", "");
		} else {
			intent.putExtra("from_call", "hotleads");
		}
		mContext.startActivity(intent);
	}

	@Override
	public int getCount() {
		return mResult.size();
	}

	@Override
	public Object getItem(int pos) {
		return mResult.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

}
