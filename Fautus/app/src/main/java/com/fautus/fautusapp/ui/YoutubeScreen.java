package com.fautus.fautusapp.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.fautus.fautusapp.utils.AppConstants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class implements UI and functions youtube player
 */
public class YoutubeScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.youtube_player_view)
    YouTubePlayerView mYouTubePlayerView;

    private YouTubePlayer mYoutubePlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_youtube_player_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        mYouTubePlayerView.initialize(getString(R.string.google_console_key), this);


    }

    @OnClick({R.id.header_left_btn_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mYoutubePlayer != null) {
            mYoutubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_LD);
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
        DialogManager.getInstance().showToast(this, getString(R.string.video_err));
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youtubePlayer, boolean wasRestored) {
        if (null == youtubePlayer) return;
        youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYoutubePlayer = youtubePlayer;
        if (!wasRestored) {
            youtubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_LD);
        }

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
                finish();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
