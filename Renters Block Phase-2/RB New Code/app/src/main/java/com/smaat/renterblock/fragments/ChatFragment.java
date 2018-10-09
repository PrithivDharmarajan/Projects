package com.smaat.renterblock.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.ChatAdapter;
import com.smaat.renterblock.entity.ChatEntity;
import com.smaat.renterblock.entity.ChatMessageResultEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.ChatResponse;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnWithStringCallback;
import com.smaat.renterblock.utils.PathUtils;
import com.smaat.renterblock.utils.PreferenceUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.smaat.renterblock.utils.ImageUtil.getOutputMediaFile;

/**
 * Created by sys on 10/26/2017.
 */

public class ChatFragment extends BaseFragment {


    @BindView(R.id.add_as_friend)
    Button mAddFriendBtn;

    @BindView(R.id.report_as_spam)
    Button mReportSpamBtn;

    @BindView(R.id.conversation_recycler_view)
    RecyclerView mChatRecyclerView;

    @BindView(R.id.message_field_edt)
    EditText mChatEdt;

    @BindView(R.id.send_txt)
    TextView mSendTxt;
    @BindView(R.id.chat_text_img)
    ImageView mChatTextImg;
    @BindView(R.id.chat_camera)
    ImageView mChatCameraImg;
    @BindView(R.id.chat_gallery)
    ImageView mChatGalleryImg;
    @BindView(R.id.chat_voice)
    ImageView mChatVoiceImg;
    @BindView(R.id.chat_video)
    ImageView mChatVideoImg;

    @BindView(R.id.test_img)
    ImageView mTestImage;

    private String chatType = "", option = "";


    private ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private Timer mBackGroundServiceTimer = new Timer();
    private Uri mPictureFileUri;
    private File file;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 999;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private static final int CHOOSEVIDEO = 70;
    private static final int CAPTUREVIDEO = 30;
    private String audioOutput = "", sourcePath;
    private boolean isRecording = false;
    private MediaRecorder mAudioRecorder;

    private MediaPlayer mPlayer;
    private String mLatstChatIdStr = "", mUserIDStr = "";
    private boolean isFirstBool = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_friends_chat, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

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


        return rootView;
    }


    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(" ", 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            initView();

        }
    }

    private void initView() {
        mUserIDStr = PreferenceUtil.getUserID(getActivity());
        String sentImage = File.separator + "RB_Sent_files" + File.separator + "Sent_images";
        String sentAudio = File.separator + "RB_Sent_files" + File.separator + "Sent_Sounds";
        String sentVideo = File.separator + "RB_Sent_files" + File.separator + "Sent_videos";


        String receiveImage = File.separator + "RB_Files" + File.separator + "RB_Images";
        String reciveAudio = File.separator + "RB_Files" + File.separator + "RB_Audios";
        String receiveVideo = File.separator + "RB_Files" + File.separator + "RB_Videos";

        File sentImageDir = new File(Environment.getExternalStorageDirectory().toString() + sentImage);
        File sentAudioDir = new File(Environment.getExternalStorageDirectory().toString() + sentAudio);
        File sentVideoDir = new File(Environment.getExternalStorageDirectory().toString() + sentVideo);


        File receiveImageDir = new File(Environment.getExternalStorageDirectory().toString() + receiveImage);
        File receiveAudioDir = new File(Environment.getExternalStorageDirectory().toString() + reciveAudio);
        File receiveVideoDir = new File(Environment.getExternalStorageDirectory().toString() + receiveVideo);

        if (!sentImageDir.exists()) {
            sentImageDir.mkdirs();
        }
        if (!sentAudioDir.exists()) {
            sentAudioDir.mkdirs();
        }
        if (!sentVideoDir.exists()) {
            sentVideoDir.mkdirs();
        }

        if (!receiveImageDir.exists()) {
            receiveImageDir.mkdirs();
        }
        if (!receiveAudioDir.exists()) {
            receiveAudioDir.mkdirs();
        }
        if (!receiveVideoDir.exists()) {
            receiveVideoDir.mkdirs();
        }
        Log.d("CALLED", "CALLED");


    }

    private void getMessagesApi() {

        APIRequestHandler.getInstance().getChatMessages(PreferenceUtil.getUserID(getActivity()),
                AppConstants.CHAT_INPUT_ENTITY.getSchedule_id(), "group", mLatstChatIdStr, this, isFirstBool);
        isFirstBool = false;
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (getActivity() != null) {
            if (resObj instanceof ChatResponse) {
                ChatResponse response = (ChatResponse) resObj;
                if (response.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                    mLatstChatIdStr = response.getGroup_chat_id();
                    /*setting group name as header title txt*/
                    ((HomeScreen) getActivity()).setHeaderTxt(response.getGroup_name(), 1);
                    setChatListAdapter(response.getResult());
                } else if (response.getMsg() != null) {
                    DialogManager.getInstance().showAlertPopup(getActivity(), response.getMsg(), this);
                }

            } else if (resObj instanceof ChatMessageResultEntity) {
                ChatMessageResultEntity response = (ChatMessageResultEntity) resObj;
                if (response.getError_code().equals(AppConstants.SUCCESS_CODE)) {

                }
            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        DialogManager.getInstance().showToast(getActivity(), t.getMessage());
    }

    public void chatBackGroundTimer() {
        cancelTimer();
        mBackGroundServiceTimer = new Timer();
        mBackGroundServiceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getMessagesApi();
            }
        }, 0, 5000);


    }

    private void cancelTimer() {
        if (mBackGroundServiceTimer != null) {
            mBackGroundServiceTimer.cancel();
            mBackGroundServiceTimer.purge();
        }
    }


    /* chat adapter*/
    private void setChatListAdapter(ArrayList<ChatEntity> chatList) {
        if (getActivity() != null) {
            if (chatList.size() > 0) {
                mChatAdapterMsgList.addAll(chatList);
            }

            if (mChatAdapter == null) {
                mChatAdapter = new ChatAdapter(getActivity(), mChatAdapterMsgList);
                mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mChatRecyclerView.setAdapter(mChatAdapter);
                mChatRecyclerView.setNestedScrollingEnabled(true);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mChatAdapter.notifyDataSetChanged();
                    }
                });
            }



        /* to focus the last message in the list */
            LinearLayoutManager layoutManager = ((LinearLayoutManager) mChatRecyclerView.getLayoutManager());

            if (layoutManager != null) {
                int firstVisiblePosition = layoutManager.findLastVisibleItemPosition();

                boolean isMovBool = firstVisiblePosition == mChatAdapter.getItemCount() - 1 || firstVisiblePosition == mChatAdapter.getItemCount() - 2;

                View v = layoutManager.getChildAt(0);
                if (firstVisiblePosition > 0 && v != null) {
                    if (isMovBool && firstVisiblePosition + 1 >= 0 && mChatAdapter.getItemCount() > 0 && mChatAdapterMsgList.size() > 0 && firstVisiblePosition + 1 <= mChatAdapter.getItemCount()) {
                        mChatRecyclerView.getLayoutManager().smoothScrollToPosition(mChatRecyclerView, null, firstVisiblePosition + 1);
                    }
                } else {
                    layoutManager.setStackFromEnd(true);
                }
            }
        }
    }

    public void showSoftKeyboard(EditText view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mChatEdt.getWindowToken(), 0);
    }

    @OnClick({R.id.chat_gallery, R.id.chat_text_img, R.id.chat_camera, R.id.chat_voice, R.id.chat_video, R.id.send_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_text_img:
                showSoftKeyboard(mChatEdt);
                chatType = "text";
                break;
            case R.id.chat_camera:

                option = "Camera";
                showOptionDialog(option);

                break;
            case R.id.chat_gallery:

                option = "Gallery";
                showOptionDialog(option);
                break;
            case R.id.chat_voice:
                chatType = "audio";
                showAudioRecording();
                break;
            case R.id.chat_video:

                break;
            case R.id.send_txt:
                sendTextMsg();
                break;
            case R.id.chat_container_lay:
                hideKeypad();
                break;
        }
    }

    private void sendTextMsg() {
        String msg = mChatEdt.getText().toString().trim();
        if (!msg.isEmpty()) {

            ChatEntity mSendEntity = new ChatEntity();

            mSendEntity.setFile_type("text");
            mSendEntity.setMessage(msg);
            mSendEntity.setUser_id(mUserIDStr);
            ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
            mChatAdapterMsgList.add(mSendEntity);
            setChatListAdapter(mChatAdapterMsgList);
            mChatEdt.setText("");
            sendMessage(msg, "", "", "");
        }
    }

    private void sendMessage(String msg, String filepath, String fileName, String file_format) {
        APIRequestHandler.getInstance().sendMessageChatMultiPart(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id(), msg, chatType, filepath,
                fileName, "group", file_format, ChatFragment.this);
    }

    private void showOptionDialog(String option) {
        if (option.equalsIgnoreCase("Camera")) {
            DialogManager.getInstance().showImageUploadPopup(getActivity(),
                    getString(R.string.choose), getString(R.string.photo), getString(R.string.video), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {
                            chatType = "image";
                            captureImage();
                        }

                        @Override
                        public void onPositiveClick() {
                            chatType = "video";
                            captureVideo();
                        }
                    });
        } else {

            DialogManager.getInstance().showImageUploadPopup(getActivity(),
                    getString(R.string.choose), getString(R.string.photo), getString(R.string.video), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {
                            chatType = "image";
                            galleryImage();
                        }

                        @Override
                        public void onPositiveClick() {
                            chatType = "video";
                            galleryVideo();
                        }
                    });
        }

    }

    /*open camera Image*/
    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        mPictureFileUri = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE)) : Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFileUri);


        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

    /*open gallery Image*/
    private void galleryImage() {
        Intent j = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        getActivity().startActivityForResult(j, GALLERY_IMAGE_REQUEST_CODE);
    }

    /*open Camera Video*/
    private void captureVideo() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        file = getOutputMediaFile(MEDIA_TYPE_VIDEO);

        mPictureFileUri = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider",
                getOutputMediaFile(MEDIA_TYPE_VIDEO)) : Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_VIDEO));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureFileUri);

        // start the image capture Intent
        getActivity().startActivityForResult(intent, CAPTUREVIDEO);


    }

    /*open Gallery Video*/
    private void galleryVideo() {
        Intent videointent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(Intent.createChooser(videointent, "Select Video"), CHOOSEVIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String selectedImageCapturePath = mPictureFileUri.getPath();
                String ss = file.getAbsolutePath();
                String filename = selectedImageCapturePath.substring(selectedImageCapturePath.lastIndexOf("/") + 1);
                String filename1 = filename.substring(0, filename.indexOf("."));


                String path = sendFiles(ss, filename);
                ChatEntity mSendEntity = new ChatEntity();
                mSendEntity.setUser_id(mUserIDStr);
                mSendEntity.setGroup_id(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id());
                mSendEntity.setFile_type("image");
                mSendEntity.setMessage("");
                mSendEntity.setFile(path);
                mSendEntity.setGroupchat_id("");
                ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
                mChatAdapterMsgList.add(mSendEntity);
                setChatListAdapter(mChatAdapterMsgList);
                sendMessage("", path, filename1, "");


            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }

            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPictureFileUri = data.getData();
                String selectedImageGalleryPath = PathUtils.getPath(getActivity(), mPictureFileUri);
                String filename = selectedImageGalleryPath.substring(selectedImageGalleryPath.lastIndexOf("/") + 1);
                String filename1 = filename.substring(0, filename.indexOf("."));

                String path = sendFiles(selectedImageGalleryPath, filename);

                ChatEntity mSendEntity = new ChatEntity();
                mSendEntity.setUser_id(mUserIDStr);
                mSendEntity.setGroup_id(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id());
                mSendEntity.setFile_type("image");
                mSendEntity.setMessage("");
                mSendEntity.setFile(path);
                mSendEntity.setGroupchat_id("");
                ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
                mChatAdapterMsgList.add(mSendEntity);
                setChatListAdapter(mChatAdapterMsgList);

                sendMessage("", path, filename1, "");

            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }

            }

        } else if (requestCode == CAPTUREVIDEO) {

            if (resultCode == getActivity().RESULT_OK) {

                final String selectedVideoCapturePath = mPictureFileUri.getPath();

                if (selectedVideoCapturePath != null) {
                    String filename = selectedVideoCapturePath.substring(selectedVideoCapturePath.lastIndexOf("/") + 1);
                    String ss = file.getAbsolutePath();

                    String filename1 = filename.substring(0, filename.indexOf("."));

                    String path = sendFiles(ss, filename);
                    ChatEntity mSendEntity = new ChatEntity();
                    mSendEntity.setUser_id(mUserIDStr);
                    mSendEntity.setGroup_id(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id());
                    mSendEntity.setFile_type("video");
                    mSendEntity.setMessage("");
                    mSendEntity.setFile(path);
                    mSendEntity.setGroupchat_id("");
                    ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
                    mChatAdapterMsgList.add(mSendEntity);
                    setChatListAdapter(mChatAdapterMsgList);
                    sendMessage("", path, filename1, "");

                }


            } else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), "Video capture failed.");

            }

        } else if (requestCode == CHOOSEVIDEO) {

            if (resultCode == getActivity().RESULT_OK) {

                final String selectedVideoGalleryPath = PathUtils.getPath(getActivity(), data.getData());

                if (selectedVideoGalleryPath != null) {
                    String filename = selectedVideoGalleryPath.substring(selectedVideoGalleryPath.lastIndexOf("/") + 1);

                    String filename1 = filename.substring(0, filename.indexOf("."));

                    String path = sendFiles(selectedVideoGalleryPath, filename);
                    ChatEntity mSendEntity = new ChatEntity();
                    mSendEntity.setUser_id(mUserIDStr);
                    mSendEntity.setGroup_id(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id());
                    mSendEntity.setFile_type("video");
                    mSendEntity.setMessage("");
                    mSendEntity.setFile(path);
                    mSendEntity.setGroupchat_id("");
                    ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
                    mChatAdapterMsgList.add(mSendEntity);
                    setChatListAdapter(mChatAdapterMsgList);
                    sendMessage("", path, filename1, "");

                }


//                if (selectedVideoGalleryPath != null) {
//                    sendMessage("", selectedVideoGalleryPath, "", "");
//
//                }


            } else {
               /*image capture getting failed due to certail technical issues*/
                DialogManager.getInstance().showToast(getActivity(), "Video capture failed.");

            }
        }

    }


    private void showAudioRecording() {
        if (getActivity() != null) {

            final Dialog audioDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
            audioDialog.setContentView(R.layout.popup_audio_record);

            final TextView recordTxt = (TextView) audioDialog.findViewById(R.id.record_txt);
            TextView sendTxt = (TextView) audioDialog.findViewById(R.id.send_txt);

            final TextView titleTxt = (TextView) audioDialog.findViewById(R.id.title_txt);
            ImageView closeImg = (ImageView) audioDialog.findViewById(R.id.close_img);

            recordTxt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (recordTxt.getText().toString().equalsIgnoreCase(getString(R.string.play))) {
                        stopSound();
                        playSound(audioOutput, titleTxt);
                    } else {

                        if (!isRecording) {
                            startRecord();
                            isRecording = true;
                            titleTxt.setText(getString(R.string.audio_recording));
                            recordTxt.setText(getString(R.string.stop));
                        } else {
                            stopRecord();
                            isRecording = false;
                            titleTxt.setText(getString(R.string.audio_recorded));
                            recordTxt.setText(getString(R.string.play));
                        }
                    }
                }
            });
            closeImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    stopRecord();
                    audioDialog.dismiss();

                }
            });
            sendTxt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    stopRecord();
                    audioDialog.dismiss();
                    if (!audioOutput.isEmpty()) {
                        String filename = audioOutput.substring(audioOutput.lastIndexOf("/") + 1);

                        String filename1 = filename.substring(0, filename.indexOf("."));

                        String path = sendFiles(audioOutput, filename);
                        ChatEntity mSendEntity = new ChatEntity();
                        mSendEntity.setUser_id(mUserIDStr);
                        mSendEntity.setGroup_id(AppConstants.CHAT_INPUT_ENTITY.getSchedule_id());
                        mSendEntity.setFile_type("audio");
                        mSendEntity.setMessage("");
                        mSendEntity.setFile(path);
                        mSendEntity.setGroupchat_id("");
                        ArrayList<ChatEntity> mChatAdapterMsgList = new ArrayList<>();
                        mChatAdapterMsgList.add(mSendEntity);
                        setChatListAdapter(mChatAdapterMsgList);


                        sendMessage("", audioOutput, filename1, "mp3");
                    }


                }
            });

            audioDialog.show();
        }
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

        mAudioRecorder = new MediaRecorder();
        mAudioRecorder.reset();
        mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mAudioRecorder.setOutputFile(audioOutput);
        try {
            mAudioRecorder.prepare();
            mAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopRecord() {
        try {
            mAudioRecorder.stop();
            mAudioRecorder.release();
            mAudioRecorder = null;
        } catch (IllegalStateException e) {
            // it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void playSound(String audioname, final TextView mTitleTxt) {

        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            if (!mPlayer.isPlaying()) {
                mTitleTxt.setText(getString(R.string.audio_playing));
                mPlayer.setDataSource(audioname);
                mPlayer.prepare();
                mPlayer.setVolume(1f, 1f);
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mTitleTxt.setText(getString(R.string.audio_recorded));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopSound() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
    }


    private String sendFiles(String filepath, String file_name) {
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

        File srcFile = new File(filepath);
        File desFile = new File(destination);
        String ss = desFile.getAbsolutePath();
        try {
            copy(srcFile, desFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ss;
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


    @Override
    public void onResume() {
        chatBackGroundTimer();
        super.onResume();
    }

    @Override
    public void onPause() {
        cancelTimer();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }


}
