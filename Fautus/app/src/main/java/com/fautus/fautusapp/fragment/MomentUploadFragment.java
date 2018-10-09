package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.R.string.your_cus_loc;
import static com.fautus.fautusapp.R.string.your_pho_loc;

/**
 * Created by sys on 28-Apr-17.
 */

public class MomentUploadFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.date_txt)
    TextView mDateTxt;
    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private Timer mMomentCancelledTimer;
    private boolean isMomentCancelled = false, isCurrentFocus = false;
    private Dialog mMomentCanCelDialog,mMomentCloseDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moment_upload_screen, container, false);
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
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(false);

        ((HomeScreen) getActivity()).setHeadLeftImg(R.drawable.menu_cls_img);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE), R.drawable.msg_img);
        isCurrentFocus = true;
        isMomentCancelled = false;
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        isCurrentFocus = true;
        isMomentCancelled = false;
        checkMomentCancelledByUser();
    }

    private void initView() {
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode));
        mDateTxt.setText(DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getDate(ParseAPIConstants.momentDate), mServerDateFormat), mServerDateFormat, mLocalDateFormat));

        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;
                onFragmentBackPressed();
            }
        });
        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.HEADER_RIGHT_CLICK = AppConstants.SUCCESS_CODE;
                getActivity().onBackPressed();
            }
        });
        ((HomeScreen) getActivity()).mHeaderRightSecondBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() != null) {

                    String plcNameStr = PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? getResources().getString(your_pho_loc) : getResources().getString(your_cus_loc);
                    String uri = String.format(Locale.US, "geo:%f,%f?z=%d&q=%f,%f (%s)",
                            AppConstants.USER_MAP_LOC.getLatitude(), AppConstants.USER_MAP_LOC.getLongitude(), 15, AppConstants.USER_MAP_LOC.getLatitude(), AppConstants.USER_MAP_LOC.getLongitude(), plcNameStr);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getActivity().startActivity(intent);
                }
            }
        });

    }


    /*View OnClick*/
    @OnClick({R.id.upload_photos_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_photos_lay:
                if (!isMomentCancelled) {
                    ((HomeScreen) getActivity()).addFragment(new ImageUploadFragment());
                }
                break;
        }
    }

    /*close moment*/
    private void checkAndCloseMoment() {

        if (!isMomentCancelled) {

            DialogManager.getInstance().showProgress(getActivity());
            ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
            momentQuery.whereEqualTo(ParseAPIConstants.objectId, AppConstants.MOMENT_PHOTO_ENTITY.getMoment().getObjectId());
            momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                /*Check internet connection*/
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        if (!isMomentCancelled) {
                            ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                            photoQuery.whereEqualTo(ParseAPIConstants.moment, object);
                            photoQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> momentPhotosObjList, ParseException e) {

                                    if (!isMomentCancelled) {
                                        if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                            object.put(ParseAPIConstants.closed, true);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e != null) {
                                                        object.saveEventually();
                                                    }
                                                    disconnectChat();
                                                }
                                            });

                                        } else if (e == null) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();

                                           /*If there were no photos uploaded, hide this moment*/
                                            object.put(ParseAPIConstants.enabled, false);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e != null) {
                                                        object.saveEventually();
                                                    }
                                                    disconnectChat();
                                                }
                                            });
                                        } else {
                                            disconnectChat();
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                    /*Alert message will be appeared if there is no internet connection*/
                        DialogManager.getInstance().hideProgress();
                        DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), MomentUploadFragment.this);
                    }
                }
            });
        }
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
                            object.saveEventually();
                        }
                        DialogManager.getInstance().hideProgress();
                        ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());

                    }
                }
            });
        }
    }


    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

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
                                        if (isMomentCancelled || !isCurrentFocus) {
                                            cancelCheckMomentCancelledByUserTimer();
                                            if (!isCurrentFocus)
                                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                        } else if (object != null && object.get(ParseAPIConstants.enabled) != null && !object.getBoolean(ParseAPIConstants.enabled) && !isMomentCancelled && isCurrentFocus) {
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

                                    }
                                });
                            } else {
                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                cancelCheckMomentCancelledByUserTimer();
                            }

                        }
                    }, 3000, 3000);
                }
            });
        }

    }

    @Override
    public void onPause() {
        isMomentCancelled = true;
        isCurrentFocus = false;
        baseFragmentAlertDismiss(mMomentCloseDialog);
        baseFragmentAlertDismiss(mMomentCanCelDialog);
        cancelCheckMomentCancelledByUserTimer();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        isCurrentFocus = false;
        cancelCheckMomentCancelledByUserTimer();
        baseFragmentAlertDismiss(mMomentCloseDialog);
        baseFragmentAlertDismiss(mMomentCanCelDialog);
        super.onDestroy();

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
            mMomentCloseDialog=  DialogManager.getInstance().showOptionAlertPopup(getActivity(), getResources().getString(R.string.close_moment_topic).toUpperCase(Locale.US), getResources().getString(R.string.close_moment_msg), getResources().getString(R.string.cancel).toUpperCase(Locale.US), getResources().getString(R.string.ok), new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    if (!isMomentCancelled) {
                        checkAndCloseMoment();
                    }
                }

                @Override
                public void onNoClick() {

                }
            });
        }
    }
}

