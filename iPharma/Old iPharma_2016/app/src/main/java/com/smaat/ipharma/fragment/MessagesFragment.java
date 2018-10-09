package com.smaat.ipharma.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.ChatCommonEntity;
import com.smaat.ipharma.entity.ChatReceiverEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MessagesFragment extends BaseFragment implements
		DialogMangerCallback, OnClickListener {
	ImageView pharmacyImage;
	TextView pharmacyName, pharmacyAddress, pharmacyReviews, pharmacyDistance,
			deliveryTime, MinOrder;
	RatingBar mRating;
	private TableLayout mChatHistoryTable;
	private String Userid;
	private ChatReceiverEntity chat_entity;
	private ArrayList<ChatReceiverEntity> chat_list = new ArrayList<ChatReceiverEntity>();
	private EditText mSend_chat_text;
	private Button mSend_chat_btn;

	// private Handler chatHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_message_screen,
				container, false);
		setupUI(rootview);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				getActivity());
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		Userid = GlobalMethods.getUserID(getActivity());
		hideSoftKeyboard(getActivity());

		AppConstants.FRAG = AppConstants.MAP_SCREEN;
		initComponents(view);

		HomeScreen.mHeaderRight
				.setBackgroundResource(R.drawable.message_refresh);
		HomeScreen.mHeaderRightLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callReceiverMessage();
			}
		});

		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				HomeScreen.onBackMove(getActivity());
			}
		});
		callReceiverMessage();// API Call
	}

	private void callReceiverMessage() {

		String Userid = GlobalMethods.getUserID(getActivity());
		APIRequestHandler.getInstance().getChathistory(Userid, this);
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof ChatCommonEntity) {
			ChatCommonEntity mChatCommonResponse = (ChatCommonEntity) responseObj;
			if (mChatCommonResponse.getStatus().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				chat_list.clear();
				for (int i = 0; i < mChatCommonResponse.getResult().size(); i++) {
					chat_entity = new ChatReceiverEntity();
					chat_entity.setMessage(mChatCommonResponse.getResult()
							.get(i).getMessage());
					chat_entity.setSenderID(mChatCommonResponse.getResult()
							.get(i).getSenderID());
					chat_entity.setMessageDateTime(mChatCommonResponse
							.getResult().get(i).getMessageDateTime());
					chat_list.add(chat_entity);

				}
				setAdapter(chat_list);
				mSend_chat_text.setText("");

			} else {
				DialogManager.showCustomAlertDialog(getActivity(),
						MessagesFragment.this, mChatCommonResponse.getMsg());
			}
		}
		super.onRequestSuccess(responseObj);
	}

	@Override
	public void onRequestFailure(RetrofitError errorCode) {

		super.onRequestFailure(errorCode);
	}

	private void setAdapter(ArrayList<ChatReceiverEntity> chat_rec_list) {
		mChatHistoryTable.removeAllViews();
		for (int i = 0; i < chat_rec_list.size(); i++) {
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = inflater.inflate(R.layout.chat_list_adapter, null);
			RelativeLayout receiver_list = (RelativeLayout) view
					.findViewById(R.id.receiver_chat_item);
			RelativeLayout sender_list = (RelativeLayout) view
					.findViewById(R.id.sender_chat_item);

			TextView mReceiver_date_time = (TextView) view
					.findViewById(R.id.receiver_date_time);
			TextView mSender_date_time = (TextView) view
					.findViewById(R.id.sender_date_time);
			mReceiver_date_time.setTypeface(HomeScreen.mHelveticaBold);
			mSender_date_time.setTypeface(HomeScreen.mHelveticaBold);

			TextView mReceiver_chat_text = (TextView) view
					.findViewById(R.id.receiver_chat_text);
			TextView mSender_chat_text = (TextView) view
					.findViewById(R.id.sender_txt_msg);

			mReceiver_chat_text.setTypeface(HomeScreen.mHelveticaNormal);
			mSender_chat_text.setTypeface(HomeScreen.mHelveticaNormal);

			if (chat_rec_list.get(i).getSenderID().equalsIgnoreCase(Userid)) {
				sender_list.setVisibility(View.VISIBLE);

				String date_time_cal = GlobalMethods.getFormatedDate(
						chat_rec_list.get(i).getMessageDateTime(),
						"yyyy-MM-dd hh:mm:ss", "MMMM dd,yyyy")
						+ " "
						+ getString(R.string.at)
						+ " "
						+ GlobalMethods.getFormatedDate(chat_rec_list.get(i)
								.getMessageDateTime(), "yyyy-MM-dd hh:mm:ss",
								"hh:mm:ss a");
				mSender_date_time.setText(date_time_cal);
				mSender_chat_text.setText(chat_rec_list.get(i).getMessage());
			} else {
				receiver_list.setVisibility(View.VISIBLE);
				String date_time_cal = GlobalMethods.getFormatedDate(
						chat_rec_list.get(i).getMessageDateTime(),
						"yyyy-MM-dd hh:mm:ss", "MMMM dd,yyyy")
						+ " at "
						+ GlobalMethods.getFormatedDate(chat_rec_list.get(i)
								.getMessageDateTime(), "yyyy-MM-dd hh:mm:ss",
								"hh:mm:ss a");
				mReceiver_date_time.setText(date_time_cal);
				mReceiver_chat_text.setText(chat_rec_list.get(i).getMessage());
			}

			mChatHistoryTable.addView(view);

		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_chat_btn:
			String chat_txt = mSend_chat_text.getText().toString();
			if (chat_txt.equalsIgnoreCase(null)
					|| (chat_txt.equalsIgnoreCase(""))) {

			} else {
				callSendChatService(chat_txt);// API Call
				// callReceiverMessage();// API call
			}
			break;

		default:
			break;
		}
	}

	private void callSendChatService(String chat_txt) {
		DialogManager.showProgress(getActivity());
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.Base_Url).build();
		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);
		String Userid = GlobalMethods.getUserID(getActivity());

		// APIRequestHandler.getInstance().getSendChat(Userid, chat_txt, this);
		interfaces.getSendChat(Userid, chat_txt,
				new Callback<ChatCommonEntity>() {

					public void success(ChatCommonEntity response, Response arg1) {
						DialogManager.hideProgress(getActivity());
						if (response.getStatus().equalsIgnoreCase(
								AppConstants.SUCCESS_CODE)) {

							for (int i = 0; i < response.getResult().size(); i++) {
								chat_entity = new ChatReceiverEntity();
								chat_entity.setMessage(response.getResult()
										.get(i).getMessage());
								chat_entity.setSenderID(response.getResult()
										.get(i).getSenderID());
								chat_entity.setMessageDateTime(response
										.getResult().get(i)
										.getMessageDateTime());
								chat_list.add(0, chat_entity);

							}
							setAdapter(chat_list);
							mSend_chat_text.setText("");

						} else {
							DialogManager.showCustomAlertDialog(getActivity(),
									MessagesFragment.this, response.getMsg());

						}
					}

					public void failure(RetrofitError error) {
						DialogManager.hideProgress(getActivity());
						DialogManager.showCustomAlertDialog(getActivity(),
								MessagesFragment.this,
								getString(R.string.no_network));
					}

				});

	}

	private void initComponents(View view) {

		mChatHistoryTable = (TableLayout) view.findViewById(R.id.chat_table);
		mSend_chat_text = (EditText) view.findViewById(R.id.send_chat_text);
		mSend_chat_btn = (Button) view.findViewById(R.id.send_chat_btn);
		mSend_chat_btn.setTypeface(HomeScreen.mHelveticaBold);
		mSend_chat_btn.setOnClickListener(this);

	}

//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.remove(this).commit();
//	}

	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// chatHandler.removeCallbacks(chatCheckingService);
		// chatHandler.postDelayed(chatCheckingService, 0);
		super.onResume();
	}

	@Override
	public void onPause() {
		// chatHandler.removeCallbacks(chatCheckingService);
		super.onPause();
	}

	// private Runnable chatCheckingService = new Runnable() {
	//
	// @Override
	// public void run() {
	// callReceiverMessage();
	// chatHandler.postDelayed(this, 10000);
	// }
	//
	// };

	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
