package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.adapter.PhotoMomentUploadAdapter;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.SendEmailResponse;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoEdtCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 11-May-17.
 */

public class PhotoMomentUploadFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.address_txt)
    TextView mAddressTxt;

    @BindView(R.id.date_txt)
    TextView mDateTxt;

    @BindView(R.id.photo_selected_txt)
    public TextView mPhotoSelectedTxt;

    @BindView(R.id.amt_txt_lay)
    LinearLayout mAmtTxtLay;

    @BindView(R.id.per_photo_txt)
    TextView mPerPhotoTxt;

    @BindView(R.id.moment_img_recycler_view)
    RecyclerView mMomentImgRecyclerView;

    @BindView(R.id.bottom_txt)
    TextView mBottomTxt;

    @BindView(R.id.share_img)
    ImageView mShareImg;


    private ArrayList<ParseFile> mParsePhotoFiles;
    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private int mParseDeleteFilePos = 0, mParseSaveFilePos = 0, mParseUploadFilePos = 0;
    private Timer mMomentCancelledTimer;
    private boolean isMomentCancelled = false, isMomentUploaded = false;
    private Dialog mMomentCanCelDialog, mMomentCloseDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moment_buy_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(false);

        ((HomeScreen) getActivity()).setHeadLeftImg(R.drawable.menu_cls_img);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(true, R.drawable.reupload_img);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isMomentCancelled = false;
        initView();
    }

    private void initView() {
        mAmtTxtLay.setVisibility(View.GONE);
        mShareImg.setVisibility(View.INVISIBLE);
        mBottomTxt.setText(AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? getResources().getString(R.string.done) : getResources().getString(R.string.email_invite));
        MomentPhotoEntity momentRes = AppConstants.MOMENT_PHOTO_ENTITY;
        if (momentRes != null) {
            ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + momentRes.getMoment().getString(ParseAPIConstants.adHocCode));
            mAddressTxt.setText(momentRes.getMoment().getString(ParseAPIConstants.locationCity));
            mDateTxt.setText(DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(momentRes.getMoment().getDate(ParseAPIConstants.momentDate), mServerDateFormat), mServerDateFormat, mLocalDateFormat));

            mPhotoSelectedTxt.setText(momentRes.getPhoto().size() + " " + (momentRes.getPhoto().size() > 1 ? getResources().getString(R.string.photos) : getResources().getString(R.string.photo)) + " " + getResources().getString(R.string.in_moment).toUpperCase(Locale.US));
            mPerPhotoTxt.setText(momentRes.getMoment().getString(ParseAPIConstants.adHocCode));

            PhotoMomentUploadAdapter galleryMomentAdapter = new PhotoMomentUploadAdapter(getActivity(), momentRes.getPhoto());
            mMomentImgRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mMomentImgRecyclerView.setAdapter(galleryMomentAdapter);
            mMomentImgRecyclerView.setNestedScrollingEnabled(true);
        }
        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentBackPressed();
            }
        });
        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeScreen) getActivity()).addFragment(new ImageUploadFragment());
            }
        });

        if (AppConstants.PHOTO_NEW_UPLOAD.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            DialogManager.getInstance().showProgress(getActivity());
            isMomentUploaded = false;
            mParsePhotoFiles = new ArrayList<>();
            mParseSaveFilePos = 0;
            getOldPhotosFromDB();
        }
        checkMomentCancelledByUser();
    }


    /*View OnClick*/
    @OnClick({R.id.buy_moment_lay, R.id.bottom_txt, R.id.bottom_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_moment_lay:
            case R.id.bottom_txt:
            case R.id.bottom_img:
                if (mParsePhotoFiles != null && mParsePhotoFiles.size() > 0) {
                        /*Check internet connection*/
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        if (AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            if (!isMomentUploaded) {
                                DialogManager.getInstance().showProgress(getActivity());
                                mParseUploadFilePos = 0;
                                uploadPhotoFile(mParseUploadFilePos);
                            } else {
                                DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                                    @Override
                                    public void onYesClick() {
                                /*Check internet connection*/
                                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                                            DialogManager.getInstance().showProgress(getActivity());
                                            checkAndCloseMoment();
                                        } else {
                                    /*Alert message will be appeared if there is no internet connection*/
                                            DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
                                        }
                                    }

                                    @Override
                                    public void onNoClick() {
                                    }
                                });
                            }

                        } else {
                            DialogManager.getInstance().showEmailInviteAlertPopup(getActivity(), new InterfaceTwoEdtCallback() {
                                @Override
                                public void onEdtOneClick(String firstEdtStr, String secondEdtStr) {
                                    APIRequestHandler.getInstance().callSendEmailAPI(firstEdtStr, getResources().getString(R.string.your_moment), String.format(getResources().getString(R.string.your_fautus_snap_code), AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode)), PhotoMomentUploadFragment.this);

                                }
                            });
                        }

                    } else {
                        /*Alert message will be appeared if there is no internet connection*/
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
                    }
                }
                break;

        }

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        if (resObj instanceof SendEmailResponse) {
            mParseUploadFilePos = 0;
            uploadPhotoFile(mParseUploadFilePos);
        }
    }

    @Override
    public void onOkClick() {

    }

    private void saveParseFile(final int filePos) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto() != null && AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() > 0) {
                        ArrayList<PhotoEntity> momentPhotoRes = AppConstants.MOMENT_PHOTO_ENTITY.getPhoto();

                        String convertedImgPathStr = ImageUtil.compressImage(getActivity(), momentPhotoRes.get(filePos).getPhoto_path());
                        byte[] imageInByte = getBytes(Uri.fromFile(new File(convertedImgPathStr)));

                        String parseFileNameStr = getString(R.string.app_name) + NumberUtil.generateRandomNum() + ".png";
                        final ParseFile photoParseFile = new ParseFile(parseFileNameStr, imageInByte);

                        // Upload the image into Parse Cloud
                        photoParseFile.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    mParsePhotoFiles.add(photoParseFile);
                                    mParseSaveFilePos += 1;
                                }
                                if (AppConstants.MOMENT_PHOTO_ENTITY.getPhoto().size() == mParseSaveFilePos) {
                                    DialogManager.getInstance().hideProgress();
                                } else if (e == null) {
                                    saveParseFile(mParseSaveFilePos);
                                } else {
                                    DialogManager.getInstance().hideProgress();
                                    DialogManager.getInstance().showToast(getActivity(), getActivity().getString(R.string.something_went_wrong));
                                    AppConstants.PHOTO_NEW_UPLOAD = AppConstants.SUCCESS_CODE;
                                    ((HomeScreen) getActivity()).addFragment(new ImageUploadFragment());
                                }
                            }
                        });
                    } else {
                        DialogManager.getInstance().hideProgress();
                    }
                }
            });
        } else {
            DialogManager.getInstance().hideProgress();
        }

    }

    /*Upload all photos to parse DB*/
    private void uploadPhotoFile(int parseFilePos) {
        if (mParsePhotoFiles != null && mParsePhotoFiles.size() > 0 && mParsePhotoFiles.size() > parseFilePos && mParsePhotoFiles.get(parseFilePos) != null) {

            ParseObject parsePhotoObject = new ParseObject(ParseAPIConstants.Parse_Photo);
            parsePhotoObject.put(ParseAPIConstants.moment, AppConstants.MOMENT_PHOTO_ENTITY.getMoment());
            parsePhotoObject.put(ParseAPIConstants.photo, mParsePhotoFiles.get(parseFilePos));
            parsePhotoObject.put(ParseAPIConstants.dateTaken, new Date());
            parsePhotoObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        mParseUploadFilePos += 1;
                        if (mParsePhotoFiles.size() == mParseUploadFilePos) {
                            isMomentUploaded = true;
                            if (AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                                DialogManager.getInstance().hideProgress();
                                DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                                    @Override
                                    public void onYesClick() {
                                /*Check internet connection*/
                                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                                            DialogManager.getInstance().showProgress(getActivity());
                                            checkAndCloseMoment();
                                        } else {
                                    /*Alert message will be appeared if there is no internet connection*/
                                            DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
                                        }
                                    }

                                    @Override
                                    public void onNoClick() {
                                    }
                                });
                            } else {
                                checkAndCloseMoment();
                            }
                        } else {
                            uploadPhotoFile(mParseUploadFilePos);
                        }
                    } else {
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), e.toString(), PhotoMomentUploadFragment.this);
                    }
                }
            });
        } else {

            isMomentUploaded = true;
            if (AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                DialogManager.getInstance().hideProgress();
                DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                                /*Check internet connection*/
                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                            DialogManager.getInstance().showProgress(getActivity());
                            checkAndCloseMoment();
                        } else {
                                    /*Alert message will be appeared if there is no internet connection*/
                            DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
                        }
                    }

                    @Override
                    public void onNoClick() {
                    }
                });
            } else {
                checkAndCloseMoment();
            }

        }
    }

    private void getOldPhotosFromDB() {
        if (!isMomentCancelled) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                photoQuery.whereEqualTo(ParseAPIConstants.moment, AppConstants.MOMENT_PHOTO_ENTITY.getMoment());
                photoQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                        if (!isMomentCancelled) {
                            if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                mParseDeleteFilePos = 0;
                                deleteOldPhotos(mParseDeleteFilePos, momentPhotosObjList);
                            } else {
                                saveParseFile(mParseSaveFilePos);
                            }
                        } else {
                            disconnectChat();
                        }
                    }
                });
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().hideProgress();
                DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
            }
        } else {
            disconnectChat();
        }
    }

    private void deleteOldPhotos(int parseFilePos, final List<ParseObject> momentPhotosObjLis) {
        if (!isMomentCancelled) {
            if (momentPhotosObjLis != null && momentPhotosObjLis.size() > parseFilePos) {
                momentPhotosObjLis.get(parseFilePos).deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            mParseDeleteFilePos += 1;
                            if (momentPhotosObjLis.size() == mParseDeleteFilePos) {
                                saveParseFile(mParseSaveFilePos);
                            } else {
                                deleteOldPhotos(mParseDeleteFilePos, momentPhotosObjLis);
                            }
                        } else {
                            DialogManager.getInstance().hideProgress();
                            DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);

                        }
                    }
                });
            } else {
                saveParseFile(mParseSaveFilePos);
            }
        } else {
            disconnectChat();
        }
    }

    /*close moment*/

    private void checkAndCloseMoment() {
        /*Check internet connection*/
        if (!isMomentCancelled) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                photoQuery.whereEqualTo(ParseAPIConstants.moment, AppConstants.MOMENT_PHOTO_ENTITY.getMoment());
                photoQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                        if (!isMomentCancelled) {

                            if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().put(ParseAPIConstants.closed, true);
                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            AppConstants.MOMENT_PHOTO_ENTITY.getMoment().saveEventually();
                                        }
                                        disconnectChat();
                                    }
                                });

                            } else if (e == null) {

                                isMomentCancelled = true;
                                cancelCheckMomentCancelledByUserTimer();
                                /*If there were no photos uploaded, hide this moment*/

                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().put(ParseAPIConstants.enabled, false);
                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            AppConstants.MOMENT_PHOTO_ENTITY.getMoment().saveEventually();
                                        }
                                        disconnectChat();
                                    }
                                });
                            } else {
                                disconnectChat();
                            }
                        } else {
                            DialogManager.getInstance().hideProgress();
                        }
                    }
                });
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().hideProgress();
                DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
            }
        } else {
            disconnectChat();
        }
    }

    /*Byte conversion from image uri*/
    private byte[] getBytes(Uri uri) {
        try {
            InputStream iStream = getActivity().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            if (iStream != null) {
                while ((len = iStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    /* Disconnect SendBird Chat SDK */
    private void disconnectChat() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            AppConstants.SEND_BIRD_GROUP_CHANNEL.leave(new GroupChannel.GroupChannelLeaveHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    SendBird.disconnect(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                            moveToHomeScreen();
                        }
                    });
                }
            });
        } else {
            moveToHomeScreen();
        }
    }

    private void moveToHomeScreen() {
        if (getActivity() != null) {
            final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
            photographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
            photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (getActivity() != null) {
                        if (e == null && object != null) {
                            object.put(ParseAPIConstants.isAvailable, AppConstants.PHOTOGRAPHER_AVA);
                            object.saveInBackground();
                            DialogManager.getInstance().hideProgress();
                            ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());
                        } else {
                            DialogManager.getInstance().hideProgress();
                            ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());
                        }
                    }
                }
            });
        }
    }


    /* To check if the moment is cancelled or not */
    private void checkMomentCancelledByUser() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelCheckMomentCancelledByUserTimer();
                    mMomentCancelledTimer = new Timer();
                    mMomentCancelledTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity())) {
                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().fetchInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (object != null && object.get(ParseAPIConstants.enabled) != null && !object.getBoolean(ParseAPIConstants.enabled) && !isMomentCancelled) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();

                                            baseFragmentAlertDismiss(mMomentCloseDialog);
                                            baseFragmentAlertDismiss(mMomentCanCelDialog);
                                            mMomentCanCelDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.moment_cancelled_topic), getResources().getString(R.string.moment_cancelled_msg), new InterfaceBtnCallback() {
                                                @Override
                                                public void onOkClick() {
                                                    if (getActivity() != null) {
                                                        DialogManager.getInstance().showProgress(getActivity());
                                                        disconnectChat();
                                                    }
                                                }
                                            });

                                        }
                                        if (isMomentCancelled) {
                                            cancelCheckMomentCancelledByUserTimer();
                                        }
                                    }
                                });
                            }

                        }
                    }, 3000, 3000);
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isMomentCancelled = true;
        cancelCheckMomentCancelledByUserTimer();
        baseFragmentAlertDismiss(mMomentCloseDialog);
        baseFragmentAlertDismiss(mMomentCanCelDialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        isMomentCancelled = false;
        checkMomentCancelledByUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isMomentCancelled = true;
        cancelCheckMomentCancelledByUserTimer();
        baseFragmentAlertDismiss(mMomentCloseDialog);
        baseFragmentAlertDismiss(mMomentCanCelDialog);
    }


    /*Cancelling the check moment cancelled timer */
    private void cancelCheckMomentCancelledByUserTimer() {
        if (mMomentCancelledTimer != null) {
            mMomentCancelledTimer.cancel();
            mMomentCancelledTimer.purge();
        }

    }

    @Override
    public void onFragmentBackPressed() {

        if (getActivity() != null) {
            baseFragmentAlertDismiss(mMomentCloseDialog);
            mMomentCloseDialog = DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                        /*Check internet connection*/
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        DialogManager.getInstance().showProgress(getActivity());
                        if (AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            checkAndCloseMoment();
                        } else {
                            mParseUploadFilePos = 0;
                            uploadPhotoFile(mParseUploadFilePos);
                        }


                    } else {
                        /*Alert message will be appeared if there is no internet connection*/
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), PhotoMomentUploadFragment.this);
                    }
                }

                @Override
                public void onNoClick() {

                }
            });
        }
    }
}
