package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.UserPropertyPics;
import com.smaat.renterblock.ui.UserPropertyImagesandVideos;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	Holder holder = null;
	ArrayList<UserPropertyPics> user_pics = new ArrayList<UserPropertyPics>();

	public ImageAdapter(Context c, ArrayList<UserPropertyPics> prop_pics) {
		mContext = c;
		user_pics = prop_pics;
	}

	@Override
	public int getCount() {
		return user_pics.size();
	}

	static class Holder {

		ImageView profile_pic;
		VideoView profile_video;
		RelativeLayout adap_vidl1;
		ProgressBar image_prog, video_prog;
		Button play_btn;
		FrameLayout frame;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new Holder();
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(R.layout.gridview_image_vid_lay,
				null);

		holder.profile_pic = (ImageView) convertView
				.findViewById(R.id.adap_img1);
		holder.profile_video = (VideoView) convertView
				.findViewById(R.id.adap_video1);
		holder.adap_vidl1 = (RelativeLayout) convertView
				.findViewById(R.id.adap_vidl1);
		holder.image_prog = (ProgressBar) convertView
				.findViewById(R.id.adap_progress1);
		holder.video_prog = (ProgressBar) convertView
				.findViewById(R.id.adap_vid_progress1);
		holder.play_btn = (Button) convertView
				.findViewById(R.id.adap_video_play_btn1);
		holder.frame = (FrameLayout) convertView.findViewById(R.id.main_frame);

		holder.play_btn.setTag(position);
		holder.profile_pic.setTag(position);
		holder.video_prog.setTag(position);

		if (user_pics.get(position).getFile_type().equals("image")) {
			holder.adap_vidl1.setVisibility(View.GONE);
			AQuery aq1 = new AQuery(mContext).recycle(convertView);
			aq1.id(R.id.adap_img1)
					.progress(R.id.adap_progress1)
					.image(user_pics.get(position).getFile(), true, true, 0,
							R.drawable.profile_pic, null, 0, 1.0f);
		} else {
			holder.adap_vidl1.setVisibility(View.VISIBLE);
			holder.profile_pic.setVisibility(View.GONE);
			holder.image_prog.setVisibility(View.GONE);
			holder.video_prog.setVisibility(View.GONE);
			holder.play_btn.setVisibility(View.VISIBLE);
			String link = user_pics.get(position).getFile();
			Uri video = Uri.parse(link);
			holder.profile_video.setVideoURI(video);

			holder.profile_video
					.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							holder.profile_video.seekTo(100);
						}
					});
			holder.profile_video.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {

					return true;
				}
			});
		}

		holder.play_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				UserPropertyImagesandVideos.getUrl(user_pics.get(pos)
						.getFile_type(), user_pics.get(pos).getFile());

			}
		});

		holder.profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				UserPropertyImagesandVideos.getUrl(user_pics.get(pos)
						.getFile_type(), user_pics.get(pos).getFile());

			}
		});

		holder.video_prog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				UserPropertyImagesandVideos.getUrl(user_pics.get(pos)
						.getFile_type(), user_pics.get(pos).getFile());

			}
		});

		return convertView;
	}

}
