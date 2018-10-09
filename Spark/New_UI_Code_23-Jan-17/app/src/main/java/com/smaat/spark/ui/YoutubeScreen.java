package com.smaat.spark.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smaat.spark.R;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.OnSwipeTouchListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class YoutubeScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.video_header_lay)
    RelativeLayout mVideoHeaderLay;


    @BindView(R.id.focus_view)
    View mFocusView;

    @BindView(R.id.bottom_content_lay)
    RelativeLayout mBottomContentLay;

    @BindView(R.id.youtube_video_seek_bar)
    SeekBar mYoutubeVideoSeekBar;

    @BindView(R.id.youtube_player_view)
    YouTubePlayerView mYouTubePlayerView;

    private YouTubePlayer mYoutubePlayer;

    private int mProgressInt = 0, mSeekInt = 0;
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
        mVideoHeaderLay.setVisibility(View.GONE);
        mBottomContentLay.setVisibility(View.GONE);
        mYouTubePlayerView.initialize(AppConstants.YOUTUBE_API_KEY, this);


        mFocusView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
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

        mYoutubeVideoSeekBar.
                setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   mProgressInt = mYoutubeVideoSeekBar.getProgress();
                                                   mSeekInt = 1;

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   mSeekInt = 0;
                                                   mProgressInt = mYoutubeVideoSeekBar.getProgress();
                                                   int length = mProgressInt * 1000;

                                                   mYoutubePlayer.seekToMillis(length);
                                                   if (mYoutubeVideoSeekBar.getVisibility() == View.INVISIBLE) {
                                                       mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
                                                   }

                                               }
                                           }
                );


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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mYoutubePlayer != null) {
            mYouTubePlayerView.onConfigurationChanged(newConfig);
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
                DialogManager.showToast(YoutubeScreen.this, getString(R.string.video_comp));
                stopSetYoutubeSeek();
                finish();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                DialogManager.showToast(YoutubeScreen.this, getString(R.string.video_err));
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
                    mProgressInt = (int) lengthTimeLong;
                    if (mSeekInt == 0) {
                        mYoutubeVideoSeekBar.setProgress(mProgressInt);
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

}
