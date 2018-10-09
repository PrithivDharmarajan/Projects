package com.smaat.renterblock.ui;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;

public class PhotosandVideoFullView extends BaseActivity implements
		DialogMangerCallback {

	private String file_type, file;
	ImageView profile_pic;
	VideoView profile_video;
	RelativeLayout adap_vidl1;
	ProgressBar image_prog, video_prog;
	Button play_btn;
	FrameLayout frame;
	MediaController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_video_full_view);

		profile_pic = (ImageView) findViewById(R.id.adap_img1);
		profile_video = (VideoView) findViewById(R.id.adap_video1);
		adap_vidl1 = (RelativeLayout) findViewById(R.id.adap_vidl1);
		image_prog = (ProgressBar) findViewById(R.id.adap_progress1);
		video_prog = (ProgressBar) findViewById(R.id.adap_vid_progress1);
		play_btn = (Button) findViewById(R.id.adap_video_play_btn1);
		frame = (FrameLayout) findViewById(R.id.main_frame);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			file_type = extras.getString("file_type");
			file = extras.getString("file");
		}

		if (file_type.equals("image")) {
			aq().id(R.id.adap_img1).progress(R.id.adap_progress1)
					.image(file, true, true, 0, 0, null, 0, 1.0f / 1.0f);
		} else {
			adap_vidl1.setVisibility(View.VISIBLE);
			profile_pic.setVisibility(View.GONE);
			image_prog.setVisibility(View.GONE);
			String link = file;
			Uri video = Uri.parse(link);
			// controller = new MediaController(this);
			profile_video.setVideoURI(video);
			profile_video.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {

					// profile_video.setMediaController(controller);
					profile_video.start();
					video_prog.setVisibility(View.GONE);
				}
			});

			profile_video.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {

					video_prog.setVisibility(View.GONE);
					DialogManager.showPopUpDialog(PhotosandVideoFullView.this,
							PhotosandVideoFullView.this,
							"Can't play this video");
					return true;
				}
			});
		}

	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		finish();

	}

}
