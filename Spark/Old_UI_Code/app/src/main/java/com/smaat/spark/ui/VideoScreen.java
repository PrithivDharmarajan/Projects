package com.smaat.spark.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import com.smaat.spark.media.GiraffePlayer;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.OnSwipeTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;


public class VideoScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    @BindView(R.id.parent_lay)
    ViewGroup mVideoViewGrp;

    @BindView(R.id.java_player_lay)
    RelativeLayout mJavaPlayerLay;

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

    private GiraffePlayer mJavaPlayer;
    private YouTubePlayer mYoutubePlayer;
    private Handler mYoutubeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_video_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mJavaPlayer = new GiraffePlayer(this);
        mJavaPlayerLay.setVisibility((AppConstants.YOUTUBE_VIDEO_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) ? View.VISIBLE : View.GONE);
        mYoutubePlayerLay.setVisibility((AppConstants.YOUTUBE_VIDEO_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) ? View.GONE : View.VISIBLE);


        mJavaPlayer.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_comp));
                finish();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_err));
                finish();
            }
        });

        mFocusView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
                if (mBottomContentLay.getVisibility() == View.VISIBLE) {
                    mBottomContentLay.setVisibility(View.INVISIBLE);
                    mVideoHeaderTxt.setVisibility(View.INVISIBLE);
                } else {
                    mBottomContentLay.setVisibility(View.VISIBLE);
                    mVideoHeaderTxt.setVisibility(View.VISIBLE);
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

        if (AppConstants.YOUTUBE_VIDEO_ID.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            mJavaPlayer.play(AppConstants.DISCOVER_VIDEO_LINK);
        } else {
            mYouTubePlayerView.initialize(AppConstants.YOUTUBE_API_KEY, this);
//            mYoutubeVideoSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);
//            mYoutubeHandler = new Handler();
        }


    }


    @OnClick({R.id.close_btn, R.id.chat_lay, R.id.share_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                finish();
                break;
            case R.id.chat_lay:
                AppConstants.VIDEO_SCREEN = AppConstants.SUCCESS_CODE;
                finish();
                break;
            case R.id.share_lay:
                GlobalMethods.shareText(this, AppConstants.DISCOVER_VIDEO_LINK);
                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mJavaPlayer != null) {
            mJavaPlayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mJavaPlayer != null) {
            mJavaPlayer.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mJavaPlayer != null) {
            mJavaPlayer.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mJavaPlayer != null) {
            mJavaPlayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        DialogManager.showToast(this, getString(R.string.video_err));
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youtubePlayer, boolean wasRestored) {
        if (null == youtubePlayer) return;

//        youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYoutubePlayer = youtubePlayer;
        if (!wasRestored) {
            youtubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_ID);
        }
//        youtubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
//            @Override
//            public void onPlaying() {
//                mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
//                if(mYoutubeHandler!=null) {
//                    mYoutubeHandler.removeCallbacks(runnable);
//                    mYoutubeHandler.postDelayed(runnable, 0);
//                }
//            }
//
//            @Override
//            public void onPaused() {
//                mYoutubeVideoSeekBar.setVisibility(View.GONE);
//                mYoutubeHandler.removeCallbacks(runnable);
//            }
//
//            @Override
//            public void onStopped() {
//
//            }
//
//            @Override
//            public void onBuffering(boolean b) {
//                mYoutubeVideoSeekBar.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onSeekTo(int i) {
//                mYoutubeVideoSeekBar.setVisibility(View.VISIBLE);
//                if(mYoutubeHandler!=null) {
//                    mYoutubeHandler.removeCallbacks(runnable);
//                }
//                mYoutubeHandler.postDelayed(runnable, 0);
//            }
//        });
        youtubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                mYoutubeVideoSeekBar.setVisibility(View.GONE);
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_comp));
                finish();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                mYoutubeVideoSeekBar.setVisibility(View.GONE);
                System.out.println("errorReason "+errorReason);
                DialogManager.showToast(VideoScreen.this, getString(R.string.video_err));
                finish();
            }
        });
    }

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mYoutubePlayer.getDurationMillis() * progress) / 100;
            mYoutubePlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onBackPressed() {
        if (mJavaPlayer != null && mJavaPlayer.onBackPressed()) {
            return;
        }
        finish();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mYoutubeHandler.postDelayed(this, 0);
        }
    };

}
