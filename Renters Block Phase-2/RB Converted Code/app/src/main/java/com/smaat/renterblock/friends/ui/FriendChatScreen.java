package com.smaat.renterblock.friends.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.adapter.FriendsChatAdapter;
import com.smaat.renterblock.friends.entity.ChatEntity;
import com.smaat.renterblock.model.ChatReceiveResponse;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FriendChatScreen extends BaseActivity implements OnClickListener {
	private EditText message_field;
	static String chatType;
	private static String option;
	private ArrayList<ChatEntity> chatMessages = new ArrayList<ChatEntity>();
	private FriendsChatAdapter adapter;
	private ListView chatListView;
	private ChatEntity sendEntity;
	private Handler chatHandler = new Handler();
	private MediaRecorder audioRecorder;
	private boolean isRecording;
	private MediaPlayer m_player;
	private int chatSize;
	public static String sourcePath;
	private String allIds, allNames, groupId, groupType;
	private ImageView back_arrow;
	private TextView title;

	private String call_from;
	private Dialog d3;
	private EditText mGroupname;
	private TextView names;

	private Button add_as_friend, block_user;

	private String video_chat_user_id, video_chat_friend_id, video_user_name;
	private String mHotleadsmessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.friend_chat_screen);

		setGoogleAnalytics(FriendChatScreen.this);

		add_as_friend = (Button) findViewById(R.id.add_as_friend);
		block_user = (Button) findViewById(R.id.report_as_spam);

		Intent intent = getIntent();
		if (intent != null) {

			allIds = intent.getStringExtra("ids");
			allNames = intent.getStringExtra("names");
			groupId = intent.getStringExtra("groupId");
			groupType = intent.getStringExtra("type");

			video_chat_user_id = intent.getStringExtra("UserID");
			video_chat_friend_id = intent.getStringExtra("friend_ids");
			video_user_name = intent.getStringExtra("mString");

			mHotleadsmessage = intent.getStringExtra("hotleadsmessage");

			names = (TextView) findViewById(R.id.header_title);
			names.setTypeface(helvetica_normal);
			if (AppConstants.Friend_Chat_icon.equals("")) {
				names.setText(allNames);
			} else {
				names.setText(AppConstants.Friend_Chat_icon);
				AppConstants.Friend_Chat_icon = "";
			}

			call_from = intent.getStringExtra("from_call");
			if (call_from != null && call_from.equalsIgnoreCase("recents")) {
				names.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showGroupNameDialog();
					}
				});
			} else if (call_from != null && call_from.equalsIgnoreCase("gcm")) {
				if (AppConstants.GCM_chat_message.equalsIgnoreCase("false")) {
					add_as_friend.setVisibility(View.VISIBLE);
					block_user.setVisibility(View.VISIBLE);
				}
			}
		}

		GlobalMethods.storeValuetoPreference(FriendChatScreen.this, GlobalMethods.INT_PREFERENCE,
				AppConstants.pref_chat_size, 0);
		ViewGroup mRootView = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mRootView, mTypeface);
		// setupUI(mRootView);
		initComponents();
		if (call_from != null && !call_from.equalsIgnoreCase("gcm")) {
			receiveChatMessages();
		}
		if (mHotleadsmessage != null && mHotleadsmessage.equalsIgnoreCase("1")) {
			add_as_friend.setVisibility(View.VISIBLE);
			block_user.setVisibility(View.VISIBLE);

		}

		chatListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard(FriendChatScreen.this);
				return false;
			}
		});
	}

	private void hideKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showGroupNameDialog() {
		d3 = new Dialog(FriendChatScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.chat_group_name);
		d3.setCancelable(false);
		Button cancel = (Button) d3.findViewById(R.id.cancel);
		cancel.setText("Cancel");
		Button save = (Button) d3.findViewById(R.id.save);

		TextView text1 = (TextView) d3.findViewById(R.id.text1);
		text1.setText("Update your Group name");

		mGroupname = (EditText) d3.findViewById(R.id.enter_search_name);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
				callGroupnameUpdateService(mGroupname.getText().toString());
			}
		});

		d3.show();
	}

	private void callGroupnameUpdateService(final String group_name) {
		String url = AppConstants.BASE_URL + "group/name";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_id", groupId);
		params.put("name", group_name);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							CommonResponse obj = new Gson().fromJson(json.toString(), CommonResponse.class);
							DialogManager.showCustomAlertDialog(FriendChatScreen.this, FriendChatScreen.this, obj.msg);
							names.setText(group_name);
						} else {
							statusErrorCode(status);
						}

					}
				});
	}

	private void initComponents() {
		message_field = (EditText) findViewById(R.id.message_field);
		message_field.setTypeface(helvetica_normal);
		chatListView = (ListView) findViewById(R.id.chat_listView);
		back_arrow = (ImageView) findViewById(R.id.back_arrow);
		back_arrow.setOnClickListener(this);
		chatListView.requestFocus();
		hideKeypad();

	}

	public void showSoftKeyboard(EditText view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		inputMethodManager.showSoftInput(view, 0);
	}

	private void hideKeypad() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(message_field.getWindowToken(), 0);
	}

	public void onVideoChat(View v) {
		//TODO  video Call
//		chatType = "video";
//		if (mEnhancedProfile.equals("1")) {
//			AppConstants.Type_of_call = "video";
//			Intent video_chat = new Intent(FriendChatScreen.this, VideoChatMainActivity.class);
//			FriendsMainScreen.type_of_chat = "video";
//			video_chat.putExtra("user_id", UserID);
//			if (allIds.equalsIgnoreCase("")) {
//				video_chat.putExtra("friend_id", video_chat_friend_id);
//			} else {
//				video_chat.putExtra("friend_id", allIds);
//			}
//			String mUserName = (String) GlobalMethods.getValueFromPreference(FriendChatScreen.this,
//					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_user_name);
//			// allNames.replace(UserName+" ,", "");
//			video_chat.putExtra("username", mUserName);
//			video_chat.putExtra("type", "video");
//			video_chat.putExtra("subject", "video");
//			startActivity(video_chat);
//		} else {
//			DialogManager.showDialogAlert(FriendChatScreen.this, getString(R.string.enhance_txt),
//					getString(R.string.close), getString(R.string.enhanc), ProfileScreen.class, R.anim.slide_in_right,
//					R.anim.slide_out_left, ProfileScreen.class, FriendsMainScreen.class, this);
//		}
	}

	public void onTextChat(View v) {
		chatType = "text";
		showSoftKeyboard(message_field);
	}

	public void onCameraChat(View v) {
		chatType = "image";
		option = "Camera";
		showOptionDialog(this);

	}

	public void onGalleryChat(View v) {
		chatType = "image";
		option = "Gallery";
		showOptionDialog(this);

	}

	public void onVoiceChat(View v) {
		chatType = "audio";
		showAudioRecording();

	}

	public void onSend(View v) {
		chatType = "text";
		String message = message_field.getText().toString().trim();
		if (AppConstants.friend_block_user.equalsIgnoreCase("false")) {
			if (call_from != null && call_from.equalsIgnoreCase("hotleads")) {
				notificationService(message);
				message_field.setText("");
			} else {
				if (message != null && !message.equalsIgnoreCase("") && message.length() > 0) {
					sendEntity = new ChatEntity();
					sendEntity.setUser_id(UserID);
					sendEntity.setGroup_id(groupId);
					sendEntity.setFile_type("text");
					sendEntity.setMessage(message);
					sendMessage(message, null, "", "");
				}
			}
		} else {
			DialogManager.showCustomAlertDialog(FriendChatScreen.this, FriendChatScreen.this,
					AppConstants.friend_block_username
							+ " not available to receive your message. Please try again later.");
		}

	}

	private void notificationService(String message) {
		String url = AppConstants.BASE_URL + "getmyhotleads/notification";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", allIds);
		params.put("notification_user_id", UserID);
		params.put("message", message);

		aq().transformer(t).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
					onSuccessRequest(obj);
				} else {
					statusErrorCode(status);
				}

			}
		});
	}

	public static void showOptionDialog(final Activity context) {

		mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.image_selection);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		Button mForPicture, mForVideo, mCancel;

		mForPicture = (Button) mDialog.findViewById(R.id.photo);
		mForVideo = (Button) mDialog.findViewById(R.id.video);
		mCancel = (Button) mDialog.findViewById(R.id.cancel);

		mForPicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chatType = "image";
				mDialog.dismiss();
				if (option.equalsIgnoreCase("Camera")) {
					ProfileImageSelectionUtil.openCamera(context, ProfileImageSelectionUtil.CAMERA);
				} else {
					ProfileImageSelectionUtil.selectImageFromGallery(context, ProfileImageSelectionUtil.GALLERY);
				}
			}
		});
		mForVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				chatType = "video";
				if (option.equalsIgnoreCase("Camera")) {
					ProfileImageSelectionUtil.openCamera(context, ProfileImageSelectionUtil.CAMERA_VIDEO);
				} else {
					ProfileImageSelectionUtil.selectVideoFromGallery(context, ProfileImageSelectionUtil.VIDEO_GALLERY);
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void sendMessage(String msg, byte[] data, String file_name, String file_format) {
		String Url = AppConstants.BASE_URL + "groupchat/sendmessage";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("schedule_id", groupId);
		params.put("message", msg);
		params.put("file_type", chatType);
		params.put("chat_file", data);
		params.put("imagename", file_name);
		params.put("type", groupType);
		params.put("file_format", file_format);
		aq().transformer(t)
				// .progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
							onSuccessRequest(obj);
						} else {
							 statusErrorCode(status);
						}
					}
				});
		chatMessages.add(sendEntity);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		message_field.setText("");

	}

	protected void onSuccessRequest(ChatSendResponse obj) {
		if (obj.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			// chatMessages.add(sendEntity);
			// adapter.notifyDataSetChanged();
			// message_field.setText("");
			sendFiles(obj.file);
		}
	}

	private void sendFiles(String file_name) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		String filePath = null;
		File file = null;

		if (chatType != null && chatType.equalsIgnoreCase("image")) {
			filePath = file_name;
			file = new File(extStorageDirectory + File.separator + "RB_Sent_files" + File.separator + "Sent_images");
		} else if (chatType != null && chatType.equalsIgnoreCase("audio")) {
			filePath = file_name;
			file = new File(extStorageDirectory + File.separator + "RB_Sent_files" + File.separator + "Sent_Sounds");
		} else if (chatType != null && chatType.equalsIgnoreCase("video")) {
			filePath = file_name;
			file = new File(extStorageDirectory + File.separator + "RB_Sent_files" + File.separator + "Sent_videos");
		}
		if (file != null && !file.exists()) {
			file.mkdirs();
		}
		String destination = file + File.separator + filePath;

		File srcFile = new File(sourcePath);
		File desFile = new File(destination);
		try {
			copy(srcFile, desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveChatMessages() {
		String Url = AppConstants.BASE_URL + "groupchat";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("schedule_id", groupId);
		params.put("type", groupType);
		aq().transformer(t)
				// .progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							AppConstants.from_recents_chat = "1";
							ChatReceiveResponse obj = new Gson().fromJson(json.toString(), ChatReceiveResponse.class);
							onSuccessRequests(obj);

							if (obj.group_name != null && obj.group_name.equalsIgnoreCase("")) {
								names.setText(obj.group_user_name.replace(",", ""));
							} else {
								names.setText(obj.group_name);
							}
						} else {
							DialogManager.showCustomAlertDialog(FriendChatScreen.this, FriendChatScreen.this,
									getString(R.string.server_unreachable));
							// statusErrorCode(status);
						}
					}
				});

	}

	protected void onSuccessRequests(ChatReceiveResponse obj) {
		if (obj.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

			chatSize = (Integer) GlobalMethods.getValueFromPreference(this, GlobalMethods.INT_PREFERENCE,
					AppConstants.pref_chat_size);
			GlobalMethods.storeValuetoPreference(FriendChatScreen.this, GlobalMethods.INT_PREFERENCE,
					AppConstants.pref_chat_size, obj.result.size());

			if (obj.result.size() != 0) {
				if (obj.result.get(0).getUser_id().equalsIgnoreCase(UserID)) {
					add_as_friend.setVisibility(View.GONE);
					block_user.setVisibility(View.GONE);
				}
			}
			if (AppConstants.from_recents_chat.equalsIgnoreCase("1")) {
				for (int i = 0; i < obj.result.size(); i++) {
					if (obj.result.get(i).getUser_id().equalsIgnoreCase(UserID)) {

					} else {
						if (obj.result.get(i).getHotleadsmessage().equalsIgnoreCase("1")) {
							add_as_friend.setVisibility(View.VISIBLE);
							block_user.setVisibility(View.VISIBLE);
							break;
						}
					}
				}
				AppConstants.from_recents_chat = "0";
			}

			if (obj.result.size() > chatSize) {
				chatMessages = obj.result;
				adapter = new FriendsChatAdapter(FriendChatScreen.this, R.layout.chat_view_row, aq(), chatMessages,
						chatListView);
				chatListView.setAdapter(adapter);
				chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				chatListView.setStackFromBottom(true);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			if (resultCode == RESULT_OK) {

				String imgPath;

				if (requestCode == ProfileImageSelectionUtil.CAMERA
						|| requestCode == ProfileImageSelectionUtil.GALLERY) {

					Bitmap image = ProfileImageSelectionUtil.getImage(data, this);

					if (image != null) {
						if (requestCode == ProfileImageSelectionUtil.CAMERA) {
							if (ProfileImageSelectionUtil.isUriTrue) {
								image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, data.getData(),
										image);
							} else {
								image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, image);
							}

							File file = FileGetImgFilename();
							imgPath = file.getAbsolutePath();
						} else {

							Uri selectedImage = data.getData();

							image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, selectedImage, image);
							imgPath = ProfileImageSelectionUtil.getRealPathFromURI(data.getData(),
									FriendChatScreen.this);
						}
						ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
						image.compress(Bitmap.CompressFormat.JPEG, 100, oututStream);
						byte[] photo = (oututStream).toByteArray();
						if (photo != null) {
							String result = imgPath.substring(imgPath.lastIndexOf("/") + 1);
							sendEntity = new ChatEntity();
							sendEntity.setUser_id(UserID);
							sendEntity.setGroup_id(groupId);
							sendEntity.setFile_type("image");
							sendEntity.setMessage("");
							sendEntity.setLocal(true);
							sendEntity.setFile(imgPath);
							sourcePath = imgPath;
							sendMessage("", photo, result, "");
						}

					} else {
						DialogManager.showCustomAlertDialog(this, this, "Unsupported file format");
					}

				} else if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO) {

					File file = getFilename();
					System.out.println("Path: " + file.getAbsolutePath());
					byte[] videoData = convertAudioToByte(file.getAbsolutePath());
					sendEntity = new ChatEntity();
					sendEntity.setUser_id(UserID);
					sendEntity.setGroup_id(groupId);
					sendEntity.setFile_type("video");
					sendEntity.setLocal(true);
					sendEntity.setMessage("");
					sendEntity.setFile(file.getAbsolutePath());
					sourcePath = file.getAbsolutePath();
					sendMessage("", videoData, getnameOfFile(file.getAbsolutePath()), "");

				} else if (requestCode == ProfileImageSelectionUtil.VIDEO_GALLERY) {

					String path = ProfileImageSelectionUtil.getRealPathFromURI(data.getData(), FriendChatScreen.this);
					byte[] videoData = convertAudioToByte(path);
					sendEntity = new ChatEntity();
					sendEntity.setUser_id(UserID);
					sendEntity.setGroup_id(groupId);
					sendEntity.setFile_type("video");
					sendEntity.setMessage("");
					sendEntity.setFile(path);
					sendEntity.setLocal(true);
					sourcePath = path;
					sendMessage("", videoData, getnameOfFile(path), "");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getnameOfFile(String path) {
		String imgpath = path;
		String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
		return result;
	}

	private File getFilename() {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"cameravideo.mp4");

		System.out.println("video " + file);
		return file;
	}

	private File FileGetImgFilename() {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"camerascript.png");

		System.out.println("video " + file);
		return file;
	}

	@Override
	public void onResume() {
		chatHandler.removeCallbacks(chatCheckingService);
		chatHandler.postDelayed(chatCheckingService, 0);
		super.onResume();
	}

	@Override
	public void onPause() {
		chatHandler.removeCallbacks(chatCheckingService);
		super.onPause();
	}

	private Runnable chatCheckingService = new Runnable() {

		@Override
		public void run() {
			receiveChatMessages();
			chatHandler.postDelayed(this, 5000);
		}

	};
	private String audioOutput;

	private void showAudioRecording() {

		final Dialog audioDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		audioDialog.setContentView(R.layout.audio_recording);

		final Button record = (Button) audioDialog.findViewById(R.id.record_btn);
		Button send = (Button) audioDialog.findViewById(R.id.send_btn);
		send.setTypeface(helvetica_normal);
		title = (TextView) audioDialog.findViewById(R.id.audio_text);
		ImageView close = (ImageView) audioDialog.findViewById(R.id.close);

		record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (record.getText().toString().equalsIgnoreCase(getString(R.string.play))) {
					stopSound();
					playSound(audioOutput);
				} else {

					if (!isRecording) {
						startRecord();
						isRecording = true;
						title.setText(getString(R.string.audio_recording));
						record.setText(getString(R.string.stop));
					} else {
						stopRecord();
						isRecording = false;
						title.setText(getString(R.string.audio_recorded));
						record.setText(getString(R.string.play));
					}
				}
			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRecord();
				audioDialog.dismiss();

			}
		});
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRecord();
				audioDialog.dismiss();
				byte[] data = convertAudioToByte(audioOutput);
				if (data != null) {
					sendEntity = new ChatEntity();
					sendEntity.setUser_id(UserID);
					sendEntity.setGroup_id(groupId);
					sendEntity.setFile_type("audio");
					sendEntity.setMessage("");
					sendEntity.setLocal(true);
					sourcePath = audioOutput;
					sendMessage("", data, getnameOfFile(audioOutput), "mp3");
				}

			}
		});

		audioDialog.show();
	}

	public void startRecord() {
		Random random = new Random();
		int n = random.nextInt();

		String filePath = "audio_" + n + ".mp3";

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

		File file = new File(extStorageDirectory + File.separator + "RB_audios");
		if (!file.exists()) {
			file.mkdirs();
		}
		audioOutput = file + File.separator + filePath;

		audioRecorder = new MediaRecorder();
		audioRecorder.reset();
		audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		audioRecorder.setOutputFile(audioOutput);
		try {
			audioRecorder.prepare();
			audioRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopRecord() {
		try {
			audioRecorder.stop();
			audioRecorder.release();
			audioRecorder = null;

			// stopBtn.setEnabled(false);
			// playBtn.setEnabled(true);

		} catch (IllegalStateException e) {
			// it is called before start()
			e.printStackTrace();
		} catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
		}
	}

	public void playSound(String audioname) {

		try {
			if (m_player == null) {
				m_player = new MediaPlayer();
			}
			if (!m_player.isPlaying()) {
				title.setText(getString(R.string.audio_playing));
				m_player.setDataSource(audioname);
				m_player.prepare();
				m_player.setVolume(1f, 1f);
				m_player.start();
				m_player.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						title.setText(getString(R.string.audio_recorded));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopSound() {
		if (m_player != null) {
			m_player.stop();
			m_player = null;
		}
	}

	private byte[] convertAudioToByte(String outputfile) {
		byte[] fileByteArray = null;
		try {
			FileInputStream fis = new FileInputStream(outputfile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int read;
			while ((read = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, read);
			}
			baos.flush();
			fileByteArray = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileByteArray;

	}

	private void callAddasFriendService() {
		String url = AppConstants.BASE_URL + "getmyhotleads/accept";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("group_id", groupId);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							DialogManager.showCustomAlertDialog(FriendChatScreen.this, FriendChatScreen.this,
									"Friend Added Successfully.");

						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub
		add_as_friend.setVisibility(View.GONE);
		block_user.setVisibility(View.GONE);
		super.onOkclick();
	}

	private void callReportasSpamService() {
		String url = AppConstants.BASE_URL + "getmyhotleads/block";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("group_id", groupId);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							DialogManager.showCustomAlertDialog(FriendChatScreen.this, FriendChatScreen.this,
									"User has been Blocked.");
						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
			Intent inte = new Intent(FriendChatScreen.this, FriendsMainScreen.class);
			startActivity(inte);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			finish();
			AppConstants.isAPI = true;
			AppConstants.push_notification_call = "false";
		} else {
			finish();
			AppConstants.isAPI = true;
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back_arrow) {
			if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
				Intent inte = new Intent(FriendChatScreen.this, FriendsMainScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
				AppConstants.isAPI = true;
				AppConstants.push_notification_call = "false";
			} else {
				AppConstants.isAPI = true;
				finish();
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			}
		} else if (id == R.id.add_as_friend) {
			callAddasFriendService();
		} else if (id == R.id.report_as_spam) {
			callReportasSpamService();
		}
	}
}
