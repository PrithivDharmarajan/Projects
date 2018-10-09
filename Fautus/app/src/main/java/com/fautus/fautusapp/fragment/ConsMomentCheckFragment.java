package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.BackgroundFillColor;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Created by sys on 16-May-17.
 */

public class ConsMomentCheckFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.address_txt)
    TextView mAddressTxt;

    @BindView(R.id.date_txt)
    TextView mDateTxt;

    @BindView(R.id.background_fill_view)
     BackgroundFillColor mBackgroundFillColor;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private boolean isCurrentFocus = false;
    private Timer mMomentCancelledTimer;
    private boolean isMomentCancelled = false;
    private Dialog mMomentCanCelDialog;

    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moment_loader_screen, container, false);
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
        ((HomeScreen) getActivity()).setHeadLeftImg(R.drawable.refresh_img);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.your_moment));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(true, R.drawable.msg_img);
        isCurrentFocus = true;
        initView();

    }

    private void initView() {

        if (AppConstants.CHAT_MOMENT_DETAILS != null) {
            mAddressTxt.setText(AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.locationCity) + getResources().getString(R.string.comma_sym) + AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.locationState));
            mDateTxt.setText(DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(AppConstants.CHAT_MOMENT_DETAILS.getDate(ParseAPIConstants.momentDate), mServerDateFormat), mServerDateFormat, mLocalDateFormat));
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        int countInt = mBackgroundFillColor.getValue();
                        if (countInt < 90) {
                            mBackgroundFillColor.setValue(countInt + 1);
                            mBackgroundFillColor.postDelayed(this, 30);
                        } else {
                            mBackgroundFillColor.postDelayed(this, 0);
                            mBackgroundFillColor.setValue(10);
                        }
                    }
                };

            }
        });

        mHandler.postDelayed(mRunnable, 0);

        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;
            }
        });

        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHandler();

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
    @OnClick({R.id.cancel_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_txt:
                DialogManager.getInstance().showProgress(getActivity());
                isMomentCancelled = true;
                final ParseObject momentObj = AppConstants.CHAT_MOMENT_DETAILS;
                momentObj.put(ParseAPIConstants.enabled, false);
                momentObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            momentObj.saveEventually();
                        }
                        clearHandler();
                        PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
                        disconnectChat();
                    }
                });

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isCurrentFocus = true;
        isMomentCancelled = false;
        checkMomentCancelledByUser();
        checkMomentClosedStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        isCurrentFocus = false;
        baseFragmentAlertDismiss(mMomentCanCelDialog);
    }

    /*check moment closed status*/
    private void checkMomentClosedStatus() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && isCurrentFocus) {
                        ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
                        momentQuery.whereEqualTo(ParseAPIConstants.objectId, AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                        momentQuery.whereEqualTo(ParseAPIConstants.closed, true);
                        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(final ParseObject object, ParseException e) {
                                if (e == null && object != null) {
                                    ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                                    photoQuery.whereEqualTo(ParseAPIConstants.moment, object);
                                    photoQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                                            if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                                                ArrayList<PhotoEntity> ParseFileObj = new ArrayList<>();
                                                int purchasedPhotosCountInt = 0;
                                                for (int j = 0; j < momentPhotosObjList.size(); j++) {
                                                    PhotoEntity photoFile = new PhotoEntity();
                                                    photoFile.setPhotoObj(momentPhotosObjList.get(j));
                                                    photoFile.setPhoto(momentPhotosObjList.get(j).getParseFile(ParseAPIConstants.photo));
                                                    photoFile.setPhoto_purchased(momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                                                    if (momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased)) {
                                                        purchasedPhotosCountInt += 1;
                                                    }
                                                    ParseFileObj.add(photoFile);
                                                }
                                                AppConstants.MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                                                AppConstants.MOMENT_PHOTO_ENTITY.setPurchasedPhotosCount(String.valueOf(purchasedPhotosCountInt));
                                                AppConstants.MOMENT_ALREADY_BOUGHT = purchasedPhotosCountInt > 0 ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                                                AppConstants.MOMENT_PHOTO_ENTITY.setMoment(object);
                                                AppConstants.MOMENT_PHOTO_ENTITY.setPhoto(ParseFileObj);

                                                AppConstants.MOMENT_FROM_LIST = AppConstants.FAILURE_CODE;
                                                if (getActivity() != null) {
                                                    ((HomeScreen) getActivity()).addFragment(new MomentDetailsFragment());
                                                }
                                            } else {
                                                checkMomentClosedStatus();
                                            }
                                        }
                                    });
                                } else {
                                    checkMomentClosedStatus();
                                }
                            }
                        });
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
                                AppConstants.CHAT_MOMENT_DETAILS.fetchInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (isMomentCancelled || !isCurrentFocus) {
                                            cancelCheckMomentCancelledByUserTimer();
                                            if (!isCurrentFocus)
                                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                        } else if (object != null && object.get(ParseAPIConstants.enabled) != null && !object.getBoolean(ParseAPIConstants.enabled) && !isMomentCancelled && isCurrentFocus) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();
                                            PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
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

    /* Disconnect SendBird Chat SDK */
    private void disconnectChat() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            AppConstants.SEND_BIRD_GROUP_CHANNEL.leave(new GroupChannel.GroupChannelLeaveHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    SendBird.disconnect(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {

                            DialogManager.getInstance().hideProgress();
                            ((HomeScreen) getActivity()).addFragment(new ConsHomeFragment());
                        }
                    });
                }
            });
        } else {
            ((HomeScreen) getActivity()).addFragment(new ConsHomeFragment());
        }
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }


    @Override
    public void onDestroy() {
        clearHandler();
        isCurrentFocus = false;
        cancelCheckMomentCancelledByUserTimer();
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

    private void clearHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
