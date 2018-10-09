package com.smaat.renterblock.scheduling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.BaseActivity.GsonTransformer;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SchedulingAdapter extends BaseAdapter implements DialogMangerCallback {

	private Context mContext;
	private ArrayList<ScheduleCommonEntity> mScheduleCommonEntityList;
	private String mDate, mTime;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat mNewDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat mTimeFormat = new SimpleDateFormat("hh:mm:ss");
	private SimpleDateFormat mNewTimeFormat = new SimpleDateFormat("hh:mm a");
	private Holder holder = null;
	AQuery aq;
	private String mUserID, mSchedulerID, mIsOnline;
	Typeface helvetica_normal, helvetica_bold, helvetica_light;
	public static ArrayList<String> mSchedule_Ids = new ArrayList<String>();
	private String mScheduleID = "";
	private boolean from_edit = false;

	public SchedulingAdapter(Context context, ArrayList<ScheduleCommonEntity> scheduleCommonEntityList) {
		mContext = context;
		mScheduleCommonEntityList = scheduleCommonEntityList;
		mUserID = GlobalMethods.getUserID(mContext);

		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(context);
	}

	class Holder {

		private TextView mAcceptedGropName, mAcceptedGropAdminName, mAcceptedDate, mAcceptedTime, mFrDate, mFrTime,
				mFriendsGropName, mFriendsGropAdminName, mFriendsAcceptText, mFriendsRejectText, mMyGropName,
				mMyGropAdminName, mMyDate, mMyTime, mcheck_status, maccept_current_status, mFriend_check_status;
		private LinearLayout mMyScheduleLay, mFriendsScheduleLay, mAcceptedScheduleLay, mCheckLay,
				mFriends_group_admin_name;
		private RelativeLayout mSch_main_lay;
		private Button mCheckBtn;
	}

	public class TagClass {
		View view;
		boolean isCheck;
		int position;
		ArrayList<Button> mCheckBtnList = new ArrayList<Button>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new Holder();

		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(R.layout.adapter_common_schedule, null);

		if (mScheduleCommonEntityList.size() != 0) {
			final ScheduleCommonEntity mCommonEntity = mScheduleCommonEntityList.get(position);

			aq = new AQuery(mContext).recycle(convertView);

			holder.mMyScheduleLay = (LinearLayout) convertView.findViewById(R.id.my_schedule_lay);
			holder.mFriendsScheduleLay = (LinearLayout) convertView.findViewById(R.id.friends_schedule_lay);
			holder.mAcceptedScheduleLay = (LinearLayout) convertView.findViewById(R.id.accepted_schedule_lay);

			holder.mAcceptedGropName = (TextView) convertView.findViewById(R.id.accept_group_name);
			holder.mAcceptedGropName.setTypeface(helvetica_bold);

			holder.mAcceptedGropAdminName = (TextView) convertView.findViewById(R.id.accept_group_admin_name);
			holder.mAcceptedGropAdminName.setTypeface(helvetica_bold);

			holder.mAcceptedDate = (TextView) convertView.findViewById(R.id.accept_date_text);
			holder.mAcceptedDate.setTypeface(helvetica_normal);

			holder.mFrDate = (TextView) convertView.findViewById(R.id.fr_date_text);
			holder.mFrDate.setTypeface(helvetica_normal);

			holder.mFrTime = (TextView) convertView.findViewById(R.id.fr_time_text);
			holder.mFrTime.setTypeface(helvetica_normal);

			holder.mAcceptedTime = (TextView) convertView.findViewById(R.id.accept_time_text);
			holder.mAcceptedTime.setTypeface(helvetica_normal);

			holder.mFriendsGropName = (TextView) convertView.findViewById(R.id.friends_group_name);
			holder.mFriendsGropName.setTypeface(helvetica_bold);

			holder.mFriendsGropAdminName = (TextView) convertView.findViewById(R.id.friends_group_admin_name);
			holder.mFriendsGropAdminName.setTypeface(helvetica_bold);

			holder.mFriendsAcceptText = (TextView) convertView.findViewById(R.id.accept_txt);
			holder.mFriendsAcceptText.setTypeface(helvetica_bold);

			holder.mFriendsRejectText = (TextView) convertView.findViewById(R.id.reject_txt);
			holder.mFriendsRejectText.setTypeface(helvetica_bold);

			holder.mMyGropName = (TextView) convertView.findViewById(R.id.my_group_name);
			holder.mMyGropName.setTypeface(helvetica_bold);

			holder.mMyGropAdminName = (TextView) convertView.findViewById(R.id.my_group_admin_name);
			holder.mMyGropAdminName.setTypeface(helvetica_bold);

			holder.mMyDate = (TextView) convertView.findViewById(R.id.my_date_text);
			holder.mMyDate.setTypeface(helvetica_normal);

			holder.mMyTime = (TextView) convertView.findViewById(R.id.my_time_text);
			holder.mMyTime.setTypeface(helvetica_normal);

			holder.mcheck_status = (TextView) convertView.findViewById(R.id.check_status);
			holder.mcheck_status.setTypeface(helvetica_bold);

			holder.maccept_current_status = (TextView) convertView.findViewById(R.id.accept_current_status);
			holder.maccept_current_status.setTypeface(helvetica_bold);

			holder.mFriend_check_status = (TextView) convertView.findViewById(R.id.friend_check_status);
			holder.mFriend_check_status.setTypeface(helvetica_bold);

			// holder.mFriends_group_admin_name = (TextView)
			// convertView.findViewById(R.id.friends_group_admin_name);
			// holder.mFriends_group_admin_name.setTypeface(helvetica_bold);

			holder.mCheckBtn = (Button) convertView.findViewById(R.id.check_btn);

			holder.mSch_main_lay = (RelativeLayout) convertView.findViewById(R.id.sch_main_lay);

			TagClass tag = new TagClass();
			tag.mCheckBtnList.add(holder.mCheckBtn);
			tag.view = holder.mCheckBtn;
			tag.position = position;

			holder.mSch_main_lay.setTag(tag);

			holder.mCheckLay = (LinearLayout) convertView.findViewById(R.id.alerts_check_lay);

			if (AppConstants.IS_EDIT.equals("true")) {
				holder.mCheckLay.setVisibility(View.VISIBLE);
				holder.mSch_main_lay.setClickable(true);
			} else {
				holder.mCheckLay.setVisibility(View.GONE);
				holder.mSch_main_lay.setClickable(true);
			}

			holder.mSch_main_lay.setOnClickListener(new OnClickListener() {
				private boolean isFirst = true;

				@Override
				public void onClick(View v) {
					TagClass tag = (TagClass) v.getTag();
					int pos = tag.position;
					if (AppConstants.IS_EDIT.equals("true")) {
						if (isFirst) {
							String UserID = GlobalMethods.getUserID(mContext);
							if (mScheduleCommonEntityList.get(pos).getUser_id() != null && mScheduleCommonEntityList
									.get(pos).getUser_id().toString().equalsIgnoreCase(UserID)) {
								from_edit = true;
								DialogManager.showCustomAlertDialog(mContext, SchedulingAdapter.this,
										mContext.getString(R.string.schedule_alert_met));
							} else {
								isFirst = false;
								tag.view.setBackgroundResource(R.drawable.tick_on);
								mScheduleID = mScheduleCommonEntityList.get(pos).getSchedule_id();
								// mAlertsEntity =
								// mScheduleCommonEntityList.get(pos);

								SchedulingAdapter.mSchedule_Ids.add(mScheduleID);
							}
						} else {
							isFirst = true;
							tag.view.setBackgroundResource(R.drawable.tick_off);
							mScheduleID = mScheduleCommonEntityList.get(pos).getSchedule_id();
							// mAlertsEntity = mAlertsEntityList.get(pos);
							SchedulingAdapter.mSchedule_Ids.remove(mScheduleID);
						}
					} else {
						SchedulingActivity.scheduleListClick(pos);
					}
				}
			});

			if (mScheduleCommonEntityList.get(position).getIsonline() != null
					&& mScheduleCommonEntityList.get(position).getIsonline().equalsIgnoreCase("0")) {
				holder.mcheck_status.setVisibility(View.GONE);
			} else {
				holder.mcheck_status.setVisibility(View.VISIBLE);
			}

			try {
				Date selectedDate = new Date();
				selectedDate = mDateFormat.parse(mCommonEntity.getDate());
				mDate = mNewDateFormat.format(selectedDate);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Date selectedTime = new Date();
				selectedTime = mTimeFormat.parse(mCommonEntity.getTime());
				mTime = mNewTimeFormat.format(selectedTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String sd = GlobalMethods.dateFormat(mContext, mDate + " " + mTime);
			String date_time[] = sd.split(" ");
			String dat = date_time[0];
			String tim = date_time[1] + " " + date_time[2];

			holder.mFrDate.setText(dat);
			holder.mFrTime.setText(tim);
			if (mCommonEntity.getIs_friends_schedule().equalsIgnoreCase("1")) {

				holder.mFriendsScheduleLay.setVisibility(View.VISIBLE);
				holder.mMyScheduleLay.setVisibility(View.GONE);
				holder.mAcceptedScheduleLay.setVisibility(View.GONE);

				if (mCommonEntity.getIsonline().equalsIgnoreCase("1")) {
					holder.mFriend_check_status.setVisibility(View.VISIBLE);
				} else {
					holder.mFriend_check_status.setVisibility(View.GONE);
				}
				// holder.mFriend_check_status.setText(mCommonEntity.getVenue());
				holder.mFriendsGropName.setText(mCommonEntity.getMeeting_subject());
				holder.mFriendsGropAdminName.setText(mCommonEntity.getVenue());
				if (mCommonEntity.getVenue() != null && !mCommonEntity.getVenue().equalsIgnoreCase("")) {
					holder.mFriendsGropAdminName.setVisibility(View.VISIBLE);
				} else {
					holder.mFriendsGropAdminName.setVisibility(View.GONE);
				}

				holder.mFriendsAcceptText.setTag(position);
				holder.mFriendsAcceptText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						SchedulingActivity.not_from_add = true;
						mIsOnline = "1";
						mSchedulerID = mCommonEntity.getSchedule_id();
						callAcceptorReject();

					}
				});
				holder.mFriendsRejectText.setTag(position);
				holder.mFriendsRejectText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						SchedulingActivity.not_from_add = true;
						mSchedulerID = mCommonEntity.getSchedule_id();
						mIsOnline = "0";
						callAcceptorReject();
					}
				});

			} else if (mCommonEntity.getIs_Accepted_schedule() != null
					&& mCommonEntity.getIs_my_schedule().equalsIgnoreCase("1")
					&& mCommonEntity.getIs_Accepted_schedule().equalsIgnoreCase("0")) {

				holder.mMyScheduleLay.setVisibility(View.VISIBLE);
				holder.mFriendsScheduleLay.setVisibility(View.GONE);
				holder.mAcceptedScheduleLay.setVisibility(View.GONE);

				holder.mMyGropName.setText(mCommonEntity.getMeeting_subject());
				holder.mMyGropAdminName.setText(mCommonEntity.getVenue());
				if (mCommonEntity.getVenue() != null && !mCommonEntity.getVenue().equalsIgnoreCase("")) {
					holder.mMyGropAdminName.setVisibility(View.VISIBLE);
				} else {
					holder.mMyGropAdminName.setVisibility(View.GONE);
				}
				holder.mMyDate.setText(dat);
				holder.mMyTime.setText(tim);
				holder.mFriendsGropAdminName.setText(mCommonEntity.getVenue());
				if (mCommonEntity.getVenue() != null && !mCommonEntity.getVenue().equalsIgnoreCase("")) {
					holder.mFriendsGropAdminName.setVisibility(View.VISIBLE);
				} else {
					holder.mFriendsGropAdminName.setVisibility(View.GONE);
				}

			} else if (mCommonEntity.getIs_Accepted_schedule() != null
					&& mCommonEntity.getIs_Accepted_schedule().equalsIgnoreCase("1")) {

				holder.mAcceptedScheduleLay.setVisibility(View.VISIBLE);
				holder.mMyScheduleLay.setVisibility(View.GONE);
				holder.mFriendsScheduleLay.setVisibility(View.GONE);

				if (mScheduleCommonEntityList.get(position).getIsonline() != null
						&& mScheduleCommonEntityList.get(position).getIsonline().equalsIgnoreCase("0")) {
					holder.maccept_current_status.setVisibility(View.GONE);
				} else {
					holder.maccept_current_status.setVisibility(View.VISIBLE);
				}

				holder.mAcceptedGropName.setText(mCommonEntity.getMeeting_subject());
				holder.mAcceptedGropAdminName.setText(mCommonEntity.getVenue());
				if (mCommonEntity.getVenue() != null && !mCommonEntity.getVenue().equalsIgnoreCase("")) {
					holder.mAcceptedGropAdminName.setVisibility(View.VISIBLE);
				} else {
					holder.mAcceptedGropAdminName.setVisibility(View.GONE);
				}
				holder.mAcceptedDate.setText(dat);
				holder.mAcceptedTime.setText(tim);

			}

		}
		return convertView;
	}

	private void callAcceptorReject() {

		String Url = AppConstants.BASE_URL + "acceptmeeting";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mUserID);
		params.put("schedule_id", mSchedulerID);
		params.put("isonline", mIsOnline);

		((BaseActivity) mContext).aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(Url,
				params, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);

						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								DialogManager.showCustomAlertDialog(mContext, SchedulingAdapter.this,
										mResponse.getMsg());
							}
						} else {
							((BaseActivity) mContext).statusErrorCode(status);
						}
					}

				});

	}

	@Override
	public int getCount() {
		return mScheduleCommonEntityList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mScheduleCommonEntityList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		if (!from_edit) {
			((SchedulingActivity) mContext).getSchedulingList();
		} else {
			from_edit = false;
		}
	}

}
