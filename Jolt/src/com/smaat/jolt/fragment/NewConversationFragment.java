package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.smaat.jolt.R;
import com.smaat.jolt.adapter.MessageDetailsAdapter;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.JoltMessageDetailsEntity;
import com.smaat.jolt.model.ConversationResponse;
import com.smaat.jolt.model.MessageDetailsResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class NewConversationFragment extends BaseFragment implements
		OnClickListener {
	private ListView mChatListView;
	private Button mConversationsendBtn;
	ArrayList<JoltMessageDetailsEntity> mMessageDetails = new ArrayList<JoltMessageDetailsEntity>();
	private Bundle mBundle;
	private EditText mMessageField;
	private String conversationId;
	private LinearLayout mNewConvBgLay;
	private MessageDetailsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.conversation, container,
				false);
		setupUI(rootview);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());
		mBundle = getArguments();
		mNewConvBgLay = (LinearLayout) view.findViewById(R.id.new_conv_bg_lay);
		if (mBundle != null) {
			conversationId = mBundle.getString(AppConstants.CONVERSATION_ID);
			mNewConvBgLay.setVisibility(View.GONE);
			// Api call
			APIRequestHandler.getInstance().getMessageDetails(
					GlobalMethods.getUserID(getActivity()), conversationId,
					this);
		} else {
			conversationId = AppConstants.DEFAULT_USERID;
			mNewConvBgLay.setVisibility(View.VISIBLE);
			HomeScreen.mHeaderText.setText(R.string.new_conversations_txt);
		}
		initComponents(view);

	}

	private void initComponents(View view) {
		mConversationsendBtn = (Button) view.findViewById(R.id.send_btn);
		mChatListView = (ListView) view.findViewById(R.id.chat_listView);
		mMessageField = (EditText) view.findViewById(R.id.edit_chat);
		mConversationsendBtn.setOnClickListener(this);

		adapter = new MessageDetailsAdapter(getActivity(), mMessageDetails);

	}

	private void sendConversationMessage(String userId, String message,
			String conversationId) {

		APIRequestHandler.getInstance().sendConversationMessage(userId,
				conversationId, message, this);
	}

	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof MessageDetailsResponse) {

			MessageDetailsResponse mResponse = (MessageDetailsResponse) responseObj;
			if (mResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				mMessageDetails = mResponse.getResult();
				adapter = new MessageDetailsAdapter(getActivity(),
						mMessageDetails);
				mChatListView.setAdapter(adapter);
			}
		} else if (responseObj instanceof ConversationResponse) {
			ConversationResponse mResponse = (ConversationResponse) responseObj;
			if (mResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				conversationId = mResponse.getResult();
			}

		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.send_btn:

			String message = mMessageField.getText().toString().trim();
			if (!message.isEmpty()) {
				hideSoftKeyboard(getActivity());
				String userId = GlobalMethods.getUserID(getActivity());
				JoltMessageDetailsEntity sendMessage = new JoltMessageDetailsEntity();
				sendMessage.setConversationID(conversationId);
				sendMessage.setUserID(userId);
				sendMessage.setMessageText(mMessageField.getText().toString());
				mMessageDetails.add(sendMessage);

				adapter = new MessageDetailsAdapter(getActivity(),
						mMessageDetails);
				mChatListView.setAdapter(adapter);
				mMessageField.setText("");

				sendConversationMessage(GlobalMethods.getUserID(getActivity()),
						message, conversationId);
			}
			break;

		default:
			break;
		}
	}
}