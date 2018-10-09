package com.smaat.renterblock.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FriendDetailsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateScheduleFragment extends BaseFragment {

    @BindView(R.id.title_edt)
    EditText mTitleEdt;

    @BindView(R.id.description_edt)
    EditText mDescriptionEdt;

    @BindView(R.id.date_lay)
    RelativeLayout mDateLay;

    @BindView(R.id.date_txt)
    TextView mDateTxt;

    @BindView(R.id.time_lay)
    RelativeLayout mTimeLay;

    @BindView(R.id.time_txt)
    TextView mTimeTxt;

    @BindView(R.id.location_edt)
    EditText mLocationEdt;

    @BindView(R.id.add_friends_lay)
    RelativeLayout mAddFriendsLay;

    @BindView(R.id.online_img)
    ImageView mOnlineImg;

    @BindView(R.id.offline_img)
    ImageView mOfflineImg;

    @BindView(R.id.save_btn)
    Button mSaveBtn;

    @BindView(R.id.accept_txt)
    TextView mAcceptTxt;

    @BindView(R.id.reject_txt)
    TextView mRejectTxt;

    @BindView(R.id.offline_lay)
    LinearLayout mOfflineLay;

    @BindView(R.id.online_lay)
    LinearLayout mOnlineLay;

    @BindView(R.id.add_friends_txt)
    TextView mFriendsNameTxt;


    String mTitleStr, mDescriptionStr, statusStr = "1", mDateStr, mTimeStr, mLocationStr;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    String[] dateTimeAPI;

    private String mFriendIdStr, mFriendNameStr,mSchedulerId,mUpdateScheduleId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_create_schedule, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*For focus current fragment*/

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });

        if (AppConstants.EDIT.equalsIgnoreCase("EDIT")) {
            editScheduleFillDetails();
            // mSaveBtn.setText("Update");
        } else if (AppConstants.EDIT.equalsIgnoreCase("ADD")) {
            mSaveBtn.setText(getString(R.string.save));
        }
        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {

            AppConstants.TAG = this.getClass().getSimpleName();

            /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

              /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.close), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.con), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.schedule_meeting), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            if (AppConstants.INVITE_FRIENDS_SCHEDULE) {
                AppConstants.INVITE_FRIENDS_SCHEDULE = false;
                setFriendName();

            }
            mDateTxt.setText(AppConstants.SCHEDULE_DATE);
            mTimeTxt.setText(AppConstants.SCHEDULE_TIME);


        }
    }

    private void setFriendName() {

        ArrayList<FriendDetailsEntity> acceptFriendEntityList = new ArrayList<>();
        ArrayList<String> mFriendsIDsList = new ArrayList<String>();
        ArrayList<String> mFriendNameList = new ArrayList<String>();
        if (AppConstants.INVITE_FRIENDS_HASH_MAP.size() > 0) {
            Iterator it = AppConstants.INVITE_FRIENDS_HASH_MAP.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                acceptFriendEntityList = (ArrayList<FriendDetailsEntity>) pair.getValue();

                mFriendsIDsList.add(acceptFriendEntityList.get(0).getUser_friend_id());
                mFriendNameList.add(acceptFriendEntityList.get(0).getFirst_name());

                //  it.remove(); // avoids a ConcurrentModificationException

            }

            mFriendIdStr = mFriendsIDsList.size() != 0 && mFriendsIDsList.size() > 1 ? mFriendsIDsList.toString().replace("[", "").replace("]", "")
                    : mFriendsIDsList.get(0);

            mFriendNameStr = mFriendNameList.size() != 0 && mFriendNameList.size() > 1 ? mFriendNameList.toString().replace("[", "").replace("]", "")
                    : mFriendNameList.get(0);
            mFriendsNameTxt.setText(mFriendNameStr);
        }


    }

    /*View OnClick*/
    @OnClick({R.id.date_lay, R.id.time_lay, R.id.online_lay, R.id.offline_lay, R.id.save_btn, R.id.add_friends_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_lay:
                showDatePickerDialog();
                break;
            case R.id.time_lay:
                showTimePickerDialog();
                break;
            case R.id.online_lay:
                statusStr = "1";
                mOnlineImg.setImageResource(R.drawable.listing_radio_over);
                mOfflineImg.setImageResource(R.drawable.listing_radio_normal);
                break;
            case R.id.offline_lay:
                statusStr = "0";
                mOfflineImg.setImageResource(R.drawable.listing_radio_over);
                mOnlineImg.setImageResource(R.drawable.listing_radio_normal);
                break;
            case R.id.save_btn:

                //validateFields();
                if (AppConstants.EDIT.equalsIgnoreCase("ADD")) {
                    validateFields();
                } else if(mSaveBtn.getText().equals(getString(R.string.chat))){
                    APIRequestHandler.getInstance().getChatIDFromSchedule(mSchedulerId,mFriendIdStr,mFriendNameStr,CreateScheduleFragment.this);
                }else if(mSaveBtn.getText().equals(getString(R.string.update))){
                    validateFields();
                }

                break;
            case R.id.add_friends_lay:
                ((HomeScreen) getActivity()).addFragment(new InviteFriendsFragment());
                break;
        }
    }

    private void editScheduleFillDetails() {
        mTitleEdt.setText(AppConstants.mSelectedPos.getMeeting_subject());
        mDescriptionEdt.setText(AppConstants.mSelectedPos.getDescription());
        mLocationEdt.setText(AppConstants.mSelectedPos.getVenue());

        mDateTxt.setText(AppConstants.SCHEDULE_DATE);
        mTimeTxt.setText(AppConstants.SCHEDULE_TIME);

        if(!validateTime(AppConstants.mSelectedPos.getDate()+" "+AppConstants.SCHEDULE_TIME)){
            mSaveBtn.setText(getString(R.string.chat));
        }else{
            mSaveBtn.setText(getString(R.string.update));
        }

        /*get friends name*/
        if (AppConstants.mSelectedPos.getFriends().size() > 0) {
            ArrayList<String> mFriendsIDsList = new ArrayList<String>();
            ArrayList<String> mFriendNameList = new ArrayList<String>();
            for (int i = 0; i < AppConstants.mSelectedPos.getFriends().size(); i++) {
                mFriendsIDsList.add(AppConstants.mSelectedPos.getFriends().get(i).getUser_id());
                mFriendNameList.add(AppConstants.mSelectedPos.getFriends().get(i).getFirst_name());

            }
            mUpdateScheduleId=AppConstants.mSelectedPos.getSchedule_id();
            mSchedulerId=AppConstants.mSelectedPos.getScheduler_user_id();
            mFriendIdStr = mFriendsIDsList.size() != 0 && mFriendsIDsList.size() > 0 ? mFriendsIDsList.toString().replace("[", "").replace("]", "")
                    : mFriendsIDsList.size() == 0 ? mFriendsIDsList.get(0) : "";

            mFriendNameStr = mFriendNameList.size() != 0 && mFriendNameList.size() > 0 ? mFriendNameList.toString().replace("[", "").replace("]", "")
                    : mFriendNameList.size() == 0 ? mFriendNameList.get(0) : "";
            mFriendsNameTxt.setText(mFriendNameStr);
        }


    }

    private boolean validateTime(String dat_tiem) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Calendar start_time = Calendar.getInstance();
            Date d1 = start_time.getTime();
            String tim = sdf.format(d1);
            String schedule_Time = dat_tiem;

            Date currentdate = sdf.parse(tim);
            Date Given_date = sdf.parse(schedule_Time);

            if (currentdate.before(Given_date)) {
                return true;
            }

        } catch (Exception e) {
        }

        return false;
    }

    private void validateFields() {
        mTitleStr = mTitleEdt.getText().toString().trim();
        mDescriptionStr = mDescriptionEdt.getText().toString().trim();
        mLocationStr = mLocationEdt.getText().toString().trim();
        mDateStr = mDateTxt.getText().toString().trim();
        mTimeStr = mTimeTxt.getText().toString().trim();

        if (mTitleStr.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_title), this);
        } else if (mDescriptionStr.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_description), this);
        } else if (mDateStr.isEmpty() || mDateStr.equals("Date")) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_date), this);
        } else if (mTimeStr.isEmpty() || mTimeStr.equals("Time")) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_time), this);
        } else if (mLocationStr.isEmpty()) {
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_location), this);
        } else {
            if(AppConstants.EDIT.equalsIgnoreCase("EDIT")){

            }
            callCreateEditScheduleAPI();
        }
    }

    private void callCreateEditScheduleAPI() {

        String dateTime = mDateStr + " " + mTimeStr;

        String convertedDateTime = DateUtil.getConvertDateTimeFormat(dateTime);

        dateTimeAPI = convertedDateTime.split(" ");

        if (!dateTimeAPI[0].isEmpty() && !dateTimeAPI[2].isEmpty()) {
            if(AppConstants.EDIT.equalsIgnoreCase("ADD")){
            APIRequestHandler.getInstance().createScheduleAPICall(CreateScheduleFragment.this, mTitleStr, mDescriptionStr, dateTimeAPI[0], dateTimeAPI[2],
                    mLocationStr, mFriendIdStr, statusStr);
            }
                    else if(AppConstants.EDIT.equalsIgnoreCase("EDIT")){
                APIRequestHandler.getInstance().UpdateScheduleAPICall(CreateScheduleFragment.this, mTitleStr, mDescriptionStr, dateTimeAPI[0], dateTimeAPI[2],
                        mLocationStr, mFriendIdStr, statusStr,mUpdateScheduleId,mSchedulerId);
            }
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);


            if (responseObj instanceof CommonResponse) {
                final CommonResponse mResponse = (CommonResponse) responseObj;
                if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    DialogManager.getInstance().showAlertPopup(getActivity(), mResponse.getMsg(), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                            ((HomeScreen) getActivity()).addFragment(new SchedulingFragment());
                        }
                    });

                }
            } else if (responseObj instanceof CreateGroupChatResponse) {
                CreateGroupChatResponse mResponse = (CreateGroupChatResponse) responseObj;
                if (mResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {

                    AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                    AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                    AppConstants.CHAT_INPUT_ENTITY.setFriend_id(mResponse.getFriend_id());
                    AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(mResponse.getResult());
                    ((HomeScreen) getActivity()).addFragment(new ChatFragment());
                }
            }

    }


    private void showTimePickerDialog() {

        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        timepickerdialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = " AM";
                        } else if (hourOfDay == 12) {

                            format = " PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = " PM";

                        } else {

                            format = " AM";
                        }
                        String setTime = "";
                        if (hourOfDay < 10 && minute < 10) {
                            setTime = "0" + hourOfDay + ":" + "0" + minute + format;
                        } else if (hourOfDay < 10) {
                            setTime = "0" + hourOfDay + ":" + minute + format;
                        } else if (minute < 10) {
                            setTime = hourOfDay + ":" + "0" + minute + format;
                        } else {
                            setTime = hourOfDay + ":" + minute + format;
                        }
                        mTimeTxt.setText(setTime);
                        AppConstants.SCHEDULE_TIME = setTime;
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();


//        // TODO Auto-generated method stub
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                mTimeTxt.setText( selectedHour + ":" + selectedMinute);
//            }
//        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();
    }

    private int mDate, mMonth, mYear;
    private String mDateInput = "MM/dd/yyyy";

    private void showDatePickerDialog() {
        final Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener,
                mYear, mMonth, mDate);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;
            final Calendar cal = Calendar.getInstance();
            String date;
            if (mYear <= cal.get(Calendar.YEAR) && mMonth <= cal.get(Calendar.MONTH) && mDate <=
                    cal.get(Calendar.DAY_OF_MONTH)) {
                date = new StringBuilder().append(mMonth + 1).append("/")
                        .append(mDate).append("/").append(mYear).toString();

            } else {

                date = new StringBuilder().append(mMonth + 1).append("/")
                        .append(mDate).append("/").append(mYear).toString();

            }

            SimpleDateFormat dateinputFormat = new SimpleDateFormat(mDateInput,
                    Locale.US);
            try {
                Date date1 = dateinputFormat.parse(date);
                String str1 = dateinputFormat.format(date1);
                mDateTxt.setText(str1);
                AppConstants.SCHEDULE_DATE = str1;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}
