package com.smaat.renterblock.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageVideoDetailsAdapter extends RecyclerView.Adapter<ImageVideoDetailsAdapter.Holder> {

    private Context mContext;
    private ArrayList<PropertyPictures> mPropertyPics = new ArrayList<>();

    public ImageVideoDetailsAdapter(Context context, ArrayList<PropertyPictures> PropertyPics) {

        mContext = context;
        mPropertyPics = PropertyPics;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photos_videos, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        if (mPropertyPics.get(position).getFile_type().equals("image")) {
            holder.user_image.setVisibility(View.VISIBLE);
            holder.user_video.setVisibility(View.GONE);
            holder.vid_progress.setVisibility(View.GONE);
            holder.vid_lay.setVisibility(View.GONE);
            holder.user_progress.setVisibility(View.GONE);


            if (mPropertyPics.get(0).getFile().isEmpty()) {
                holder.user_image.setImageResource(R.drawable.profile_pic);
            } else {
                try {
                    Glide.with(mContext)
                            .load(mPropertyPics.get(0).getFile())
                            .placeholder(R.drawable.profile_pic)
                            .into(holder.user_image);
                } catch (Exception ex) {
                    holder.user_image.setImageResource(R.drawable.profile_pic);
                    Log.e(AppConstants.TAG, ex.getMessage());
                }
            }
//            if(!mPropertyPics.get(position).getFile().equalsIgnoreCase("")&&!mPropertyPics.get(position).getFile().equalsIgnoreCase(null))
//            {
//                holder.user_image.setImageResource(R.drawable.profile_pic);
//                try{
//                    Glide.with(mContext)
//                            .load(mPropertyPics.get(position).getFile())
//                            .into(holder.user_image);
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                    holder.user_image.setImageResource(R.drawable.profile_pic);
//                }
//
//            }else{
//                holder.user_image.setImageResource(R.drawable.profile_pic);
//            }

        } else {
            holder.user_image.setVisibility(View.GONE);
            holder.user_video.setVisibility(View.VISIBLE);
            holder.vid_progress.setVisibility(View.VISIBLE);
            holder.vid_lay.setVisibility(View.VISIBLE);
            holder.user_progress.setVisibility(View.GONE);
            String link = mPropertyPics.get(position).getFile();
            Uri video = Uri.parse(link);
            holder.user_video.setVideoURI(video);
            holder.vid_progress.setVisibility(View.GONE);
            holder.vid_play_btn.setVisibility(View.VISIBLE);

        }
        holder.user_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.user_video.seekTo(100);
                holder.user_progress.setVisibility(View.GONE);
                holder.vid_play_btn.setVisibility(View.VISIBLE);
                holder.vid_progress.setVisibility(View.GONE);
            }
        });
        holder.user_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
        holder.vid_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int pos = Integer.parseInt(String.valueOf(v.getTag()));

                // video_url = imgUrl;
                // showCustomDialog(video_url, "video");
                // showImageFullView(pos);

            }
        });
        holder.user_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int pos = Integer.parseInt(String.valueOf(v.getTag()));
                // video_url = imgUrl;
                // showCustomDialog(video_url, "image");
                // showImageFullView(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPropertyPics.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_img_view)
        ImageView user_image;

        @BindView(R.id.video_view)
        VideoView user_video;

        @BindView(R.id.progress_bar)
        ProgressBar user_progress;

        @BindView(R.id.video_progress_bar)
        ProgressBar vid_progress;

        @BindView(R.id.video_play_btn)
        Button vid_play_btn;

        @BindView(R.id.video_lay)
        RelativeLayout vid_lay;

        @BindView(R.id.adapter_frame_lay)
        FrameLayout mVideoFrameLay;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
