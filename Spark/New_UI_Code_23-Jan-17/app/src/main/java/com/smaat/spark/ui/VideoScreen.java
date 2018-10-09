package com.smaat.spark.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APICommonInterface;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;
import com.smaat.spark.utils.OnSwipeTouchListener;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    @BindView(R.id.parent_lay)
    ViewGroup mVideoViewGrp;

    @BindView(R.id.video_header_lay)
    RelativeLayout mVideoHeaderLay;

    @BindView(R.id.youtube_player_lay)
    RelativeLayout mYoutubePlayerLay;

    @BindView(R.id.focus_view)
    View mFocusView;

    @BindView(R.id.video_header_txt)
    TextView mVideoHeaderTxt;

    @BindView(R.id.bottom_content_lay)
    RelativeLayout mBottomContentLay;

    @BindView(R.id.youtube_video_seek_bar)
    SeekBar mYoutubeVideoSeekBar;

    @BindView(R.id.youtube_player_view)
    YouTubePlayerView mYouTubePlayerView;

    private YouTubePlayer mYoutubePlayer;
    //    private Handler mYoutubeHandler;
    int progress = 0, seek = 0;
    private Timer mYoutubeSeekTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_video_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mFocusView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
                if (mBottomContentLay.getVisibility() == View.VISIBLE) {
                    mBottomContentLay.setVisibility(View.INVISIBLE);
                    mVideoHeaderLay.setVisibility(View.INVISIBLE);
                    mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);
                } else {
                    mBottomContentLay.setVisibility(View.VISIBLE);
                    mVideoHeaderLay.setVisibility(View.VISIBLE);
                    mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                }
            }

            public void onSwipeTop() {
                onBackPressed();
            }

            public void onSwipeRight() {
                onBackPressed();
            }

            public void onSwipeLeft() {
                onBackPressed();
            }

            public void onSwipeBottom() {
                onBackPressed();
            }

        });

        mVideoHeaderTxt.setText(AppConstants.DISCOVER_VIDEO_NAME);

        mYouTubePlayerView.initialize(AppConstants.YOUTUBE_API_KEY, this);
        mYoutubeVideoSeekBar.
                setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   progress = mYoutubeVideoSeekBar.getProgress();
                                                   seek = 1;

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   seek = 0;
                                                   progress = mYoutubeVideoSeekBar.getProgress();
                                                   int length = progress * 1000;

                                                   mYoutubePlayer.seekToMillis(length);
                                                   if (mYoutubeVideoSeekBar.getVisibility() == View.INVISIBLE) {
                                                       mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                                                   }

                                               }
                                           }
                );


    }


    @OnClick({R.id.close_btn, R.id.chat_lay, R.id.share_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                stopSetYoutubeSeek();
                finish();
                break;
            case R.id.chat_lay:
                stopSetYoutubeSeek();
                checkChatScreen();
                break;
            case R.id.share_lay:
                GlobalMethods.shareText(this, AppConstants.DISCOVER_VIDEO_LINK);
                break;

        }

    }

    long seekTime = 0;

    @Override
    protected void onPause() {
        super.onPause();
        if (mYoutubePlayer != null) {
            seekTime = mYoutubePlayer.getCurrentTimeMillis();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mYoutubePlayer != null) {
            mYoutubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_ID, (int) seekTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSetYoutubeSeek();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mYoutubePlayer != null) {
            mYouTubePlayerView.onConfigurationChanged(newConfig);
        }
    }

    private void checkChatScreen() {
        if (!GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECTED_STATUS).equals(AppConstants.FAILURE_CODE) && !GlobalMethods.getStringValue(this, AppConstants.CHAT_CONNECTED_STATUS).isEmpty()) {
            DialogManager.getInstance().showOptionPopup(VideoScreen.this, getString(R.string.already_connected), getString(R.string.end_chat), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    callDisConnectAPI();
                }

                @Override
                public void onNoClick() {

                }
            });

        } else {
            AppConstants.VIDEO_CHAT_SCREEN = AppConstants.SUCCESS_CODE;
            GlobalMethods.storeStringValue(this, AppConstants.CHAT_CONNECT_SUB, GlobalMethods.setMsgEncoder(AppConstants.DISCOVER_VIDEO_NAME));
            GlobalMethods.storeStringValue(this, AppConstants.CHAT_CONNECT_ORIGINAL_SUB, GlobalMethods.setMsgEncoder(AppConstants.DISCOVER_VIDEO_NAME));
            GlobalMethods.storeStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_ID, AppConstants.FAILURE_CODE);
            GlobalMethods.storeStringValue(this, AppConstants.CHAT_CONNECT_FRIEND_NAME, AppConstants.FAILURE_CODE);
            finish();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        DialogManager.showToast(this, getString(R.string.video_err));
        stopSetYoutubeSeek();
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youtubePlayer, boolean wasRestored) {
        if (null == youtubePlayer) return;
        youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYoutubePlayer = youtubePlayer;
        if (!wasRestored) {
            youtubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_ID);
        }

        youtubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                long duration = mYoutubePlayer.getDurationMillis() / 1000;
                mYoutubeVideoSeekBar.setMax((int) duration);
                mYoutubeVideoSeekBar.setProgress(youtubePlayer.getCurrentTimeMillis() / 1000);
                if (mYoutubeVideoSeekBar.getVisibility() == View.INVISIBLE) {
                    mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                }
                stopSetYoutubeSeek();
                startSetYoutubeSeek();

            }

            @Override
            public void onPaused() {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onSeekTo(int i) {
                if (mYoutubeVideoSeekBar.getVisibility() == View.INVISIBLE) {
                    mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                }
                stopSetYoutubeSeek();
                startSetYoutubeSeek();
            }
        });
        youtubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoaded(String s) {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAdStarted() {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onVideoStarted() {
                if (mYoutubeVideoSeekBar.getVisibility() == View.INVISIBLE) {
                    mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onVideoEnded() {
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_comp));
                stopSetYoutubeSeek();
                finish();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_err));
                mYoutubeVideoSeekBar.setVisibility(View.INVISIBLE);
                stopSetYoutubeSeek();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopSetYoutubeSeek();
        finish();
    }


    private void startSetYoutubeSeek() {
        mYoutubeSeekTimer = new Timer();
        mYoutubeSeekTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                long durationTimeLong = 0, lengthTimeLong = 0;
                if (mYoutubePlayer != null) {
                    durationTimeLong = (mYoutubePlayer.getDurationMillis()) / 1000;
                    lengthTimeLong = (mYoutubePlayer.getCurrentTimeMillis()) / 1000;
                }
                if (durationTimeLong >= lengthTimeLong) {
                    progress = (int) lengthTimeLong;
                    if (seek == 0) {
                        mYoutubeVideoSeekBar.setProgress(progress);
                    }
                } else {
                    stopSetYoutubeSeek();
                }


            }
        }, 0, 1000);
    }

    private void stopSetYoutubeSeek() {
        if (mYoutubeSeekTimer != null) {
            mYoutubeSeekTimer.cancel();
            mYoutubeSeekTimer.purge();
        }
    }

    public void callDisConnectAPI() {
        ChatConnDisInputEntity chatDisConInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_CHAT_DISCONNECT, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(this), AppConstants.FAILURE_CODE);
        DialogManager.showProgress(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        APICommonInterface serviceInterface = retrofit.create(APICommonInterface.class);

        serviceInterface.disConnectAPICall(chatDisConInputEntityRes).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                DialogManager.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    GlobalMethods.storeStringValue(VideoScreen.this, AppConstants.CHAT_CONNECTED_STATUS, AppConstants.FAILURE_CODE);
                    AppConstants.CHAT_BACK_PRESSED = AppConstants.FAILURE_CODE;
                    checkChatScreen();
                } else {
                    DialogManager.getInstance().showAlertPopup(VideoScreen.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                DialogManager.hideProgress();
                onRequestFailure(t);
            }
        });

    }

    public void onRequestFailure(Throwable t) {

        if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.getInstance().showAlertPopup(this, getString(R.string.no_internet));
        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.getInstance().showAlertPopup(this, getString(R.string.connect_time_out));
        } else {
            DialogManager.getInstance().showAlertPopup(this, t.getMessage());

        }
    }

}
