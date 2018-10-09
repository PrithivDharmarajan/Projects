package com.smaat.renterblock.friends.ui;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.adapter.FriendsAdapter;
import com.smaat.renterblock.friends.adapter.FriendsInviteAdapter;
import com.smaat.renterblock.friends.adapter.FriendsRecentsAdapter;
import com.smaat.renterblock.friends.entity.AcceptFriendEntity;
import com.smaat.renterblock.friends.entity.FriendMainScreenEntity;
import com.smaat.renterblock.friends.entity.FriendsDetailsEntity;
import com.smaat.renterblock.friends.entity.FriendsRecentsEntity;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.FriendsRecentsResponse;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FriendsMainScreen extends BaseActivity implements DialogMangerCallback, OnItemClickListener {

	private ListView friends_list;
	private FriendsAdapter friendsadapter;
	private FriendsRecentsAdapter mRecentsAdapter;
	private TextView recents_tab, friends_tab;
	private ArrayList<FriendMainScreenEntity> freinds_entity;
	private ArrayList<FriendMainScreenEntity> search_freinds_entity;
	FriendMainScreenEntity fr_ent;
	private EditText search_txt;
	private String UserID;
	ArrayList<String> friend_ids = new ArrayList<String>();
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private ArrayList<FriendsRecentsEntity> mFriendsRecentsEntityList;
	private ArrayList<FriendsRecentsEntity> mSearchFriendsRecentsEntityList;
	FriendsRecentsEntity fr_recent_ent;
	// private Button mBuySettings, mAgentSettings, mSellSettings;
	private RelativeLayout friends_lay, recent_lay;
	private String pendi_count;
	private Button back_btn;
	private ListView mInviteFriendsList;
	private FriendsInviteAdapter mInviteFriendsAdapter;
	private ArrayList<FriendsDetailsEntity> mFriendsResultList;
	private ArrayList<AcceptFriendEntity> mAcceptedFriendsList;
	public static ArrayList<String> mUserID = new ArrayList<String>();
	public static ArrayList<String> mUserNames = new ArrayList<String>();
	private Dialog dialog;
	private String listview_type = "friends";
	private Dialog d2;
	public static String Friends_ids = "", type_of_chat = "";
	private Button img_in;
	private Dialog d3;
	private EditText mGroupname;

	public static ArrayList<String> friends_chat_user_ids = new ArrayList<String>();

	static Context mContext;
	// private String sync_con;

	private ArrayList<String> email_ids_ar = new ArrayList<String>();
	private ArrayList<String> phone_ids_ar = new ArrayList<String>();

	// private Date sync_date, saved_sync_date;
	long daysBetween = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.friends_main_screen_activity);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		mContext = FriendsMainScreen.this;
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		setGoogleAnalytics(this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			callFriendsService();
		}

		new syncContacts().execute();
	}

	class syncContacts extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			return getContactEmails(mContext);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				new SyncNumbers().execute();
			}
		}
	}

	class SyncNumbers extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			return getContactPhones();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			updateEmailContacts();
		}
	}

	private boolean getContactPhones() {
		phone_ids_ar.clear();
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor people = getContentResolver().query(uri, projection, null, null, null);

		int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

		people.moveToFirst();
		do {
			try{
				String number = people.getString(indexNumber).replace("+", "").replace(" ", "").replace("91", "");
				phone_ids_ar.add("'" + number + "'");
			}catch (Exception e){
				e.printStackTrace();
			}


		} while (people.moveToNext());

		return true;

	}

	private boolean getContactEmails(Context context) {

		try {
			String emailIdOfContact = null;
			int emailType = Email.TYPE_WORK;
			String contactName = null;
			email_ids_ar.clear();

			ContentResolver cr = context.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
					contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// Log.i(TAG,"....contact name....." +
					// contactName);
					try {
						cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

						Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + id, null, null);
						while (emails.moveToNext()) {
							emailIdOfContact = emails.getString(emails.getColumnIndex(Email.DATA));
							email_ids_ar.add("'" + emailIdOfContact + "'");
							emailType = emails.getInt(emails.getColumnIndex(Phone.TYPE));
							deleteCache(context);
						}
						emails.close();
					} catch (Exception e) {
					}
				}
			} // end of contact name cursor
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
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

		// The directory is now empty so delete it
		return dir.delete();
	}

	private void updateEmailContacts() {

		String email_ids = email_ids_ar.toString();
		email_ids = email_ids.replace("[", "");
		email_ids = email_ids.replace("]", "");
		
		String phonenums = phone_ids_ar.toString();
		phonenums = phonenums.replace("[", "");
		phonenums = phonenums.replace("]", "");

		String url = AppConstants.BASE_URL + "contact";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("emails", email_ids);
		params.put("phone_numbers", phonenums);

		aq().transformer(t).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					System.out.println("updated successfully");
				} else {
//					statusErrorCode(status);
				}
			}
		});
	}

	private void initComponents() {
		back_btn = (Button) findViewById(R.id.bac_btn);
		if (AppConstants.from_profile_friends.equals("true")) {
			back_btn.setBackgroundResource(R.drawable.back_arrow_white);
		} else {
			back_btn.setBackgroundResource(R.drawable.slide_menu_icon);
		}

		img_in = (Button) findViewById(R.id.img_in);

		friends_list = (ListView) findViewById(R.id.friends_list);
		recents_tab = (TextView) findViewById(R.id.recents_frag);
		friends_tab = (TextView) findViewById(R.id.friends_frag);
		friends_tab.setTypeface(helvetica_bold);
		recents_tab.setTypeface(helvetica_bold);
		friends_tab.setText("Friends (0)");
		search_txt = (EditText) findViewById(R.id.search_txt);

		search_freinds_entity = new ArrayList<FriendMainScreenEntity>();
		mSearchFriendsRecentsEntityList = new ArrayList<FriendsRecentsEntity>();

		search_txt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (listview_type.equals("friends")) {
					reloadAdapterView(v.getText().toString());
				} else {
					reloadRecentAdapterView(v.getText().toString());
				}
				return false;
			}
		});

		friends_lay = (RelativeLayout) findViewById(R.id.friends_top_right_icon);
		recent_lay = (RelativeLayout) findViewById(R.id.recent_top_right_icon);
		recent_lay.setVisibility(View.GONE);
		friends_lay.setVisibility(View.VISIBLE);
		friends_list.setOnItemClickListener(this);

		/**
		 * Slide menu
		 */
		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_friends);
			AppConstants.view_id = R.id.buy_friends;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_friends);
			AppConstants.view_id = R.id.sell_friends;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_friends);
			AppConstants.view_id = R.id.agent_friends;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_friends);
			AppConstants.view_id = R.id.buy_friends;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (AppConstants.isAPI) {
			AppConstants.isAPI = false;
			if (listview_type.equalsIgnoreCase("recents")) {
				callRecentList();
			} else {
				callFriendsService();
			}
		}
	}

	private void chatScreen(int position) {

		String mString = null;
		ArrayList<String> Names = new ArrayList<String>();
		FriendsRecentsEntity entity = mFriendsRecentsEntityList.get(position);
		Intent intent = new Intent(FriendsMainScreen.this, FriendChatScreen.class);

		if (entity.getUserdetails().size() != 0) {
			if (entity.getGroupname().equals("")) {
				for (int i = 0; i < entity.getUserdetails().size(); i++) {
					if (!UserName.equalsIgnoreCase(entity.getUserdetails().get(i).getUser_name())) {
						Names.add(entity.getUserdetails().get(i).getUser_name());
					}
				}

				mString = Names.toString().replace("[", "").replace("]", "");
			} else {
				mString = entity.getGroupname();
			}
		}
		if (entity.getUserdetails().size() != 0) {
			for (int i = 0; i < entity.getUserdetails().size(); i++) {
				if (entity.getUserdetails().get(i).getEnhanced_profile().equals("1")) {
					friends_chat_user_ids.add(entity.getUserdetails().get(i).getUser_id());
				}
			}
		}

		intent.putExtra("ids", "");
		intent.putExtra("names", mString);
		intent.putExtra("groupId", entity.getGroup_id());
		intent.putExtra("type", entity.getType());
		intent.putExtra("from_call", "recents");
		intent.putExtra("hotleadsmessage", entity.getHotleadsmessage());

		friends_chat_user_ids.remove(UserID);
		String friend_ids = friends_chat_user_ids.toString().replace("[", "");
		friend_ids = friend_ids.replace("]", "");

		intent.putExtra("user_id", UserID);
		intent.putExtra("friend_id", friend_ids);
		intent.putExtra("username", mString);

		// video_chat.putExtra("user_id", UserID);
		// video_chat.putExtra("friend_id", ids);
		// video_chat.putExtra("username", UserName);
		// video_chat.putExtra("type", "video");
		// video_chat.putExtra("subject", "video");

		startActivity(intent);

	}

	private void reloadAdapterView(String name) {
		search_freinds_entity.clear();
		for (int i = 0; i < freinds_entity.size(); i++) {
			if (freinds_entity.get(i).getUser_name().toLowerCase().trim().contains(name)) {
				fr_ent = new FriendMainScreenEntity();
				fr_ent.setUser_name(freinds_entity.get(i).getUser_name());
				fr_ent.setFriends(freinds_entity.get(i).getFriends());
				fr_ent.setIsnew(freinds_entity.get(i).getIsnew());
				fr_ent.setLast_name(freinds_entity.get(i).getLast_name());
				fr_ent.setPhotos(freinds_entity.get(i).getPhotos());
				fr_ent.setRating(freinds_entity.get(i).getRating());
				fr_ent.setReview(freinds_entity.get(i).getReview());
				fr_ent.setStatus(freinds_entity.get(i).getStatus());
				fr_ent.setUser_friend_id(freinds_entity.get(i).getUser_friend_id());
				fr_ent.setUser_pic(freinds_entity.get(i).getUser_pic());
				fr_ent.setAccept(freinds_entity.get(i).getAccept());
				search_freinds_entity.add(fr_ent);
			}
		}

		Collections.sort(search_freinds_entity, FriendMainScreenEntity.FRIEND_IN_ALPHABET_ORDER);

		friendsadapter = new FriendsAdapter(FriendsMainScreen.this, R.layout.friends_list_adapter,
				search_freinds_entity);
		friends_list.setAdapter(friendsadapter);
		friendsadapter.notifyDataSetChanged();
	}

	private void reloadRecentAdapterView(String name) {
		mSearchFriendsRecentsEntityList.clear();
		for (int i = 0; i < mFriendsRecentsEntityList.size(); i++) {
			if (mFriendsRecentsEntityList.get(i).getUser_name().toString().contains(name)) {
				// mFriendsRecentsEntityList.get(i).getUserdetails().get(0)
				// .getUsername().toString().toLowerCase().trim().contains(name)
				fr_recent_ent = new FriendsRecentsEntity();
				fr_recent_ent.setFile(mFriendsRecentsEntityList.get(i).getFile());
				fr_recent_ent.setFile_type(mFriendsRecentsEntityList.get(i).getFile_type());
				fr_recent_ent.setGroup_id(mFriendsRecentsEntityList.get(i).getGroup_id());
				fr_recent_ent.setGroupchat_id(mFriendsRecentsEntityList.get(i).getGroupchat_id());
				fr_recent_ent.setMeeting_subject(mFriendsRecentsEntityList.get(i).getMeeting_subject());
				fr_recent_ent.setMessage(mFriendsRecentsEntityList.get(i).getMessage());
				fr_recent_ent.setRead(mFriendsRecentsEntityList.get(i).getRead());
				fr_recent_ent.setSchedule_id(mFriendsRecentsEntityList.get(i).getSchedule_id());
				fr_recent_ent.setSend_time(mFriendsRecentsEntityList.get(i).getSend_time());
				fr_recent_ent.setType(mFriendsRecentsEntityList.get(i).getType());
				fr_recent_ent.setUser_id(mFriendsRecentsEntityList.get(i).getUser_id());
				fr_recent_ent.setUserdetails(mFriendsRecentsEntityList.get(i).getUserdetails());
				fr_recent_ent.setGroupname(mFriendsRecentsEntityList.get(i).getGroupname());
				fr_recent_ent.setUser_name(mFriendsRecentsEntityList.get(i).getUser_name());
				mSearchFriendsRecentsEntityList.add(fr_recent_ent);
			}
		}

		mRecentsAdapter = new FriendsRecentsAdapter(FriendsMainScreen.this, mSearchFriendsRecentsEntityList);
		friends_list.setAdapter(mRecentsAdapter);
		mRecentsAdapter.notifyDataSetChanged();
	}

	private void callFriendsService() {
		String url = AppConstants.BASE_URL + "friend";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							FriendsResponse obj = new Gson().fromJson(json.toString(), FriendsResponse.class);
							onSuccessRequest(obj);
						} else {
							statusErrorCode(status);
						}

					}
				});
	}

	private void onSuccessRequest(FriendsResponse response) {

		if (response.getResult().size() != 0) {
			pendi_count = response.getResult().get(0).getGetfriends().toString();
			img_in.setText(response.getResult().get(0).getPending_count().toString());
			if(pendi_count!=null) {
			friends_tab.setText("Friends (" + pendi_count + ")");
			} else {
				friends_tab.setText("Friends (0)");
			}
			freinds_entity = new ArrayList<FriendMainScreenEntity>();
			for (int i = 0; i < response.getResult().get(0).getAccept_friend().size(); i++) {
				if (!response.getResult().get(0).getAccept_friend().get(i).getFriends_details().isEmpty()) {
					fr_ent = new FriendMainScreenEntity();
					fr_ent.setEnhanced_profile(response.getResult().get(0).getAccept_friend().get(i)
							.getFriends_details().get(0).getEnhanced_profile());
					fr_ent.setUser_name(response.getResult().get(0).getAccept_friend().get(i).getFriends_details()
							.get(0).getUser_name());
					fr_ent.setFriends(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getFriends());
					fr_ent.setIsnew(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getIsnew());
					fr_ent.setLast_name(response.getResult().get(0).getAccept_friend().get(i).getFriends_details()
							.get(0).getLast_name());
					fr_ent.setPhotos(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getPhotos());
					fr_ent.setRating(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getRating());
					fr_ent.setReview(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getReview());
					fr_ent.setStatus(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getStatus());
					fr_ent.setUser_friend_id(response.getResult().get(0).getAccept_friend().get(i).getFriends_details()
							.get(0).getUser_friend_id());
					fr_ent.setUser_pic(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getUser_pic());
					fr_ent.setId(
							response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0).getId());
					fr_ent.setAccept(response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0)
							.getAccept());
					freinds_entity.add(fr_ent);
					friend_ids.add(
							response.getResult().get(0).getAccept_friend().get(i).getFriends_details().get(0).getId());
				}
			}

			for (int j = 0; j < response.getResult().get(0).getSent_pending_details().size(); j++) {
				if (!response.getResult().get(0).getSent_pending_details().get(j).getFriends_details().isEmpty()) {
					fr_ent = new FriendMainScreenEntity();
					fr_ent.setEnhanced_profile(response.getResult().get(0).getSent_pending_details().get(j)
							.getFriends_details().get(0).getEnhanced_profile());
					fr_ent.setUser_name(response.getResult().get(0).getSent_pending_details().get(j)
							.getFriends_details().get(0).getUser_name());
					fr_ent.setFriends(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getFriends());
					fr_ent.setIsnew(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getIspending());
					fr_ent.setLast_name(response.getResult().get(0).getSent_pending_details().get(j)
							.getFriends_details().get(0).getLast_name());
					fr_ent.setPhotos(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getPhotos());
					fr_ent.setRating(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getRating());
					fr_ent.setReview(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getReview());
					fr_ent.setStatus(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getStatus());
					fr_ent.setUser_friend_id(response.getResult().get(0).getSent_pending_details().get(j)
							.getFriends_details().get(0).getUser_friend_id());
					fr_ent.setUser_pic(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getUser_pic());
					fr_ent.setId("");
					friend_ids.add(response.getResult().get(0).getSent_pending_details().get(j).getFriends_details()
							.get(0).getId());
					freinds_entity.add(fr_ent);
				}
			}
			if (freinds_entity.size() != 0) {
				Collections.sort(freinds_entity, FriendMainScreenEntity.FRIEND_IN_ALPHABET_ORDER);
				friendsadapter = new FriendsAdapter(FriendsMainScreen.this, R.layout.friends_list_adapter,
						freinds_entity);
				friends_list.setAdapter(friendsadapter);
				friendsadapter.notifyDataSetChanged();
				callNewUpdateService();
			}

		}
	}

	private void callNewUpdateService() {

		String url = AppConstants.BASE_URL + "friend/updateisnewstatus";
		GsonTransformer t = new GsonTransformer();
		String f_id = friend_ids.toString().replace("[", "");
		f_id = f_id.replace("]", "");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_id", f_id);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							System.out.println("successfully changed");
						} else {
							statusErrorCode(status);
						}

					}
				});
	}

	public void onClick(View v) {
		if (v.getId() == R.id.bac_lay_btn) {
			if (AppConstants.from_profile_friends.equals("true")) {
				AppConstants.from_profile_friends = "false";
				finish();
			} else {
				slide_holder.toggle();
			}
		} else if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			switch (v.getId()) {
			case R.id.recents_frag:
				listview_type = "recents";
				AppConstants.from_recents_chat = "1";
				recents_tab.setTextColor(Color.parseColor("#007afc"));
				recents_tab.setBackgroundResource(R.drawable.headers_line);
				friends_tab.setTextColor(Color.parseColor("#000000"));
				friends_tab.setBackground(null);

				callRecentList();

				friends_lay.setVisibility(View.GONE);
				recent_lay.setVisibility(View.VISIBLE);
				friends_tab.setText("Friends (" + pendi_count + ")");
				break;
			case R.id.friends_frag:
				listview_type = "friends";
				friends_tab.setTextColor(Color.parseColor("#007afc"));
				friends_tab.setBackgroundResource(R.drawable.headers_line);
				recents_tab.setTextColor(Color.parseColor("#000000"));
				recents_tab.setBackground(null);

				friendsadapter = new FriendsAdapter(FriendsMainScreen.this, R.layout.friends_list_adapter,
						freinds_entity);
				friends_list.setAdapter(friendsadapter);
				friendsadapter.notifyDataSetChanged();
				friends_lay.setVisibility(View.VISIBLE);
				recent_lay.setVisibility(View.GONE);
				friends_tab.setText("Friends (" + pendi_count + ")");
				break;
			case R.id.img_icon_c:
				Intent inte = new Intent(FriendsMainScreen.this, FriendsPendingRequest.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.add_button:
				Intent intes = new Intent(FriendsMainScreen.this, FriendSearchScreen.class);
				startActivity(intes);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.bac_lay_btn:
				if (AppConstants.from_profile_friends.equals("true")) {
					AppConstants.from_profile_friends = "false";
					finish();
				} else {
					slide_holder.toggle();
				}
				break;
			case R.id.call_icon:
				if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
					moveToLogin();
				} else {
					if (mEnhancedProfile.equals("1")) {
						showCalloptionDialog();
					} else {
						DialogManager.showDialogAlert(FriendsMainScreen.this, getString(R.string.enhance_txt),
								getString(R.string.enhanc), getString(R.string.close), ProfileScreen.class,
								R.anim.slide_in_right, R.anim.slide_out_left, ProfileScreen.class,
								FriendsMainScreen.class, this);
					}
				}
				break;
			case R.id.chat_screen:
				AppConstants.from_chat_dialog = "true";
				ShowFriendsDialog("");
				break;
			}
		}
	}

	private void showCalloptionDialog() {
		d2 = new Dialog(FriendsMainScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);

		d2.setContentView(R.layout.friends_call_option_dialog);
		d2.setCancelable(true);

		Button voice_call = (Button) d2.findViewById(R.id.voice_call);
		Button video_call = (Button) d2.findViewById(R.id.video_call);

		voice_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppConstants.Type_of_call = "voice";
				type_of_chat = "voice";
				ShowFriendsDialog("voice_call");
				d2.dismiss();
			}
		});

		video_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.Type_of_call = "video";
				type_of_chat = "video";
				ShowFriendsDialog("video_call");
				d2.dismiss();
			}
		});

		d2.show();
	}

	private void callRecentList() {

		String Url = AppConstants.BASE_URL + "recentlist";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);

						if (json != null) {
							FriendsRecentsResponse mResponse = new Gson().fromJson(json.toString(),
									FriendsRecentsResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								// mFriendsRecentsEntityList.clear();
								mFriendsRecentsEntityList = mResponse.getResult();
								Collections.reverse(mFriendsRecentsEntityList);
								mRecentsAdapter = new FriendsRecentsAdapter(FriendsMainScreen.this,
										mFriendsRecentsEntityList);
								friends_list.setAdapter(mRecentsAdapter);
								mRecentsAdapter.notifyDataSetChanged();

							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

	private void createGroup(String ids, final String type_of_call, final String group_name) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", ids);
		params.put("name", group_name);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
							onSuccessRequest(obj, type_of_call);
						} else {
							statusErrorCode(status);
						}
					}
				});

	}

	protected void onSuccessRequest(ChatSendResponse obj, String type_of_call) {
		if (obj.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			String ids = null;
			if (mUserID.size() == 1) {
				ids = mUserID.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
			} else if (mUserID.size() > 1) {
				ids = mUserID.toString().replace("[", "").replace("]", "").replace(" ", "");
			}
			Intent intent = new Intent(FriendsMainScreen.this, FriendChatScreen.class);
			intent.putExtra("ids", ids);
			intent.putExtra("names", obj.username);
			intent.putExtra("groupId", obj.result);
			intent.putExtra("type", "group");
			intent.putExtra("enhanced_profile_ids", obj.enhanced_profile);
			intent.putExtra("from_call", "recents");
			// intent.putExtra("hotleadsmessage", entity.getHotleadsmessage());
			startActivity(intent);
			mUserID.clear();
			mUserNames.clear();
			dialog.dismiss();
		}
	}

	private void ShowFriendsDialog(final String type_of_call) {

		dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.activity_friends_invite_friends);
		mInviteFriendsList = (ListView) dialog.findViewById(R.id.schedule_friends_list);
		LinearLayout backBtn = (LinearLayout) dialog.findViewById(R.id.back_icon);
		TextView header_txt = (TextView) dialog.findViewById(R.id.header_txt);
		header_txt.setText("Friends List");
		header_txt.setTypeface(helvetica_bold);
		RelativeLayout invite_layout = (RelativeLayout) dialog.findViewById(R.id.invite_lay);
		TextView inviteText = (TextView) dialog.findViewById(R.id.invite_text);
		inviteText.setText("Start Chat");
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.from_chat_dialog = "false";
				dialog.dismiss();
			}
		});
		invite_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO  voice_call
//				if (type_of_call.equals("invite_layout") || type_of_call.equals("voice_call")) {
//					String ids = mUserID.toString().replace("[", "").replace("]", "");
//					Intent video_chat = new Intent(FriendsMainScreen.this, VideoChatMainActivity.class);
//					video_chat.putExtra("user_id", UserID);
//					video_chat.putExtra("friend_id", ids);
//					video_chat.putExtra("username", UserName);
//					if (type_of_call.equalsIgnoreCase("video_call")) {
//						video_chat.putExtra("type", "video");
//						video_chat.putExtra("subject", "video");
//					} else if (type_of_call.equalsIgnoreCase("voice_call")) {
//						video_chat.putExtra("type", "voice");
//						video_chat.putExtra("subject", "voice");
//					}
//					startActivity(video_chat);
//					dialog.dismiss();
//				} else {
//					showGroupnameDialogue(type_of_call);
//				}
			}
		});
		getFriendsList();
		dialog.show();
	}

	private void showGroupnameDialogue(final String type_of_call) {
		d3 = new Dialog(FriendsMainScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.chat_group_name);
		d3.setCancelable(false);
		Button cancel = (Button) d3.findViewById(R.id.cancel);
		cancel.setText("Skip");
		Button save = (Button) d3.findViewById(R.id.save);

		mGroupname = (EditText) d3.findViewById(R.id.enter_search_name);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ids = mUserID.toString().replace("[", "").replace("]", "");

				if (mUserID != null && mUserID.size() > 0) {
					System.out.println("number of ids " + ids);
					Friends_ids = ids;
					createGroup(ids, type_of_call, "");
				} else {
					DialogManager.showCustomAlertDialog(FriendsMainScreen.this, FriendsMainScreen.this,
							"Please select atleast one friend.");
				}
				AppConstants.from_chat_dialog = "false";
				d3.dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ids = mUserID.toString().replace("[", "").replace("]", "");

				if (mUserID != null && mUserID.size() > 0) {
					System.out.println("number of ids " + ids);
					Friends_ids = ids;
					createGroup(ids, type_of_call, mGroupname.getText().toString());
				} else {
					DialogManager.showCustomAlertDialog(FriendsMainScreen.this, FriendsMainScreen.this,
							"Please select atleast one friend.");
				}
				AppConstants.from_chat_dialog = "false";
				d3.dismiss();
			}
		});

		d3.show();
	}

	private void setFriendsAdapter() {
		mInviteFriendsAdapter = new FriendsInviteAdapter(this, mAcceptedFriendsList, null, null);
		mInviteFriendsList.setAdapter(mInviteFriendsAdapter);
		mInviteFriendsAdapter.notifyDataSetChanged();
	}

	private void getFriendsList() {
		String Url = AppConstants.BASE_URL + "friend";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);

						if (json != null) {
							FriendsResponse mResponse = new Gson().fromJson(json.toString(), FriendsResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mFriendsResultList = mResponse.getResult();
								mAcceptedFriendsList = mFriendsResultList.get(0).getAccept_friend();
								setFriendsAdapter();
							}

						} else {
							statusErrorCode(status);
						}
					}

				});
	}

	public void onUserClick(View v) {
		onMenuUserNameClick(v);
	}

	public void onbuyMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuBuyClick(v);
			slide_holder.toggle();
		}
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (listview_type.equalsIgnoreCase("friends")) {

		} else {
			chatScreen(arg2);
		}

	}

	public static void callGroupchatid(String user_friend_id, String user_name) {
		((FriendsMainScreen) mContext).callGroupChatService(user_friend_id, user_name);
	}

	private void callGroupChatService(final String friend_id, String user_name) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", friend_id);
		params.put("name", user_name);
		AppConstants.Friend_Chat_icon = user_name;
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
							Intent intent = new Intent(FriendsMainScreen.this, FriendChatScreen.class);
							intent.putExtra("ids", friend_id);
							intent.putExtra("names", obj.username);
							intent.putExtra("groupId", obj.result);
							intent.putExtra("type", "group");
							intent.putExtra("enhanced_profile_ids", obj.enhanced_profile);
							intent.putExtra("from_call", "friends");
							// intent.putExtra("hotleadsmessage",
							// obj.getHotleadsmessage());
							startActivity(intent);
						} else {
							statusErrorCode(status);
						}
					}
				});
	}
}
