package com.smaat.spark.ui;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smaat.spark.R;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.OnSwipeTouchListener;


public class YoutubeScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    //    public static final String API_KEY = "YOUR_API_KEY";
//
//    //https://www.youtube.com/watch?v=<VIDEO_ID>
//    public static final String VIDEO_ID = "-m3V8w_7vhk";
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // attaching layout xml
        setContentView(R.layout.ui_youtube_player_screen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Initializing YouTube player view
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(AppConstants.YOUTUBE_API_KEY, this);

        youTubePlayerView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSingleTap() {
                finish();
            }

        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        DialogManager.showToast(this, getString(R.string.video_err));
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youtubePlayer, boolean wasRestored) {
        if (null == youtubePlayer) return;

        if (!wasRestored) {
            youtubePlayer.loadVideo(AppConstants.YOUTUBE_VIDEO_ID);
//            youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
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
                DialogManager.showToast(YoutubeScreen.this, getString(R.string.video_comp));
                finish();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                DialogManager.showToast(YoutubeScreen.this, getString(R.string.video_err));
                finish();
            }
        });
    }


}
