package com.smaat.renterblock.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.BoardsChatEntity;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.BaseActivity.GsonTransformer;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class MyBoardsListAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<BoardsChatEntity> mBoardsList = new ArrayList<BoardsChatEntity>();
	Typeface helvetica_normal, helvetica_bold, helvetica_light;
	AQuery aq1;

	public MyBoardsListAdapter(Context context, int layout,
			ArrayList<BoardsChatEntity> boards_list) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.mBoardsList = boards_list;

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	@Override
	public int getCount() {
		return mBoardsList.size();
	}

	static class Holder {
		TextView username, friends, review, photos, chat_text, date_time;
		RatingBar rating;
		ImageView profile_pic;
		Button chat_icon;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);
		aq1 = new AQuery(context).recycle(view);

		holder.username = (TextView) view.findViewById(R.id.name);
		holder.username.setTypeface(helvetica_bold);
		holder.friends = (TextView) view.findViewById(R.id.friends_count);
		holder.friends.setTypeface(helvetica_normal);
		holder.review = (TextView) view.findViewById(R.id.review_count);
		holder.review.setTypeface(helvetica_normal);
		holder.photos = (TextView) view.findViewById(R.id.photos_count);
		holder.photos.setTypeface(helvetica_normal);
		holder.chat_text = (TextView) view.findViewById(R.id.chat_text);
		holder.chat_text.setTypeface(helvetica_normal);
		holder.date_time = (TextView) view.findViewById(R.id.time);
		holder.date_time.setTypeface(helvetica_normal);
		holder.rating = (RatingBar) view
				.findViewById(R.id.client_review_ratingbar);

		holder.chat_icon = (Button) view.findViewById(R.id.chat_icon);
		holder.chat_icon.setTag(position);
		final String UserID = GlobalMethods.getUserID(context);
		if (mBoardsList.get(position).getUser_id().equalsIgnoreCase(UserID)) {
			holder.chat_icon.setVisibility(View.GONE);
		} else {
			holder.chat_icon.setVisibility(View.VISIBLE);
		}

		holder.chat_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				callGroupIdService(UserID, mBoardsList.get(pos).getUser_id(),
						pos);
			}
		});

		holder.username.setText(mBoardsList.get(position).getUser_name());
		holder.friends.setText(mBoardsList.get(position).getFriends_count());
		holder.review.setText(mBoardsList.get(position).getReviews_count());
		holder.photos.setText(mBoardsList.get(position).getPhotos_count());
		holder.chat_text.setText(mBoardsList.get(position).getMessage());
		try {
			holder.rating.setRating(Float.valueOf(mBoardsList.get(position)
					.getUser_avg_rating()));

		} catch (Exception e) {
			holder.rating.setRating(Float.valueOf("0"));
		}

		holder.date_time.setText(GlobalMethods.timeDiff(mBoardsList.get(
				position).getDate_time()));

		aq1.id(R.id.profile_image)
				.progress(R.id.progress)
				.image(mBoardsList.get(position).getUser_profileImage(), true,
						true, 0, R.drawable.profile_pic, null, 0, 1.0f);

		return view;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private void callGroupIdService(String UserID, String Friend_UserID,
			final int pos) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", Friend_UserID);
		params.put("name", "");
		aq1.transformer(t)
				.progress(DialogManager.getProgressDialog(context))
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
		Intent intent = new Intent(context, FriendChatScreen.class);
		intent.putExtra("ids", mBoardsList.get(pos).getUser_id());
		intent.putExtra("names", mBoardsList.get(pos).getUser_name());
		intent.putExtra("groupId", group_id);
		intent.putExtra("type", "group");
		intent.putExtra("enhanced_profile_ids", mBoardsList.get(pos)
				.getEnhanced_profile());
		if (mBoardsList.get(pos).getIs_friend().equalsIgnoreCase("1")) {
			intent.putExtra("from_call", "");
		} else {
			intent.putExtra("from_call", "hotleads");
		}
		context.startActivity(intent);
	}
}
