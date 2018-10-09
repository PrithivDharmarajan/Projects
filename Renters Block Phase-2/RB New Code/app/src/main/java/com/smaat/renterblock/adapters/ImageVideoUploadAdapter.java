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
import com.smaat.renterblock.entity.UserPropertyPicsEntity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageVideoUploadAdapter  extends RecyclerView.Adapter<ImageVideoUploadAdapter.Holder> {


    Context mContext;
    ArrayList<UserPropertyPicsEntity> mPropertyPics = new ArrayList<>();

    public ImageVideoUploadAdapter(Context context, ArrayList<UserPropertyPicsEntity> PropertyPics) {

        mContext = context;
        mPropertyPics = PropertyPics;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_upload, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.play_btn.setTag(position);
        holder.profile_pic.setTag(position);
        holder.video_prog.setTag(position);
        holder.image_prog.setVisibility(View.GONE);
        if (mPropertyPics.get(position).getFile_type().equals("image")) {

                if (mPropertyPics.get(position).getFile().isEmpty()) {
                    holder.profile_pic.setImageResource(R.drawable.profile_pic);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(mPropertyPics.get(position).getFile())
                                .into(holder.profile_pic);
                    } catch (Exception ex) {
                        holder.profile_pic.setImageResource(R.drawable.profile_pic);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }

            holder.adap_vidl1.setVisibility(View.GONE);

//            Glide.with(mContext)
//                    .load(mPropertyPics.get(position).getFile())
//                    .placeholder(R.drawable.profile_pic)
//                    .into(holder.profile_pic);
        } else {
            holder.adap_vidl1.setVisibility(View.VISIBLE);
            holder.profile_pic.setVisibility(View.GONE);
            holder.image_prog.setVisibility(View.GONE);
            holder.video_prog.setVisibility(View.GONE);
            holder.play_btn.setVisibility(View.VISIBLE);
            String link = mPropertyPics.get(position).getFile();
            Uri video = Uri.parse(link);
            holder.profile_video.setVideoURI(video);

            holder.profile_video
                    .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            holder.profile_video.seekTo(100);
                        }
                    });
            holder.profile_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return true;
                }
            });
        }

        holder.play_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(String.valueOf(v.getTag()));
                DialogManager.getInstance().showImageVideoDialog(mContext,mPropertyPics.get(pos).getFile_type(),mPropertyPics.get(pos).getFile(),
                        mPropertyPics.get(pos).getDescription(),mPropertyPics.get(pos).getDatetime(),mPropertyPics.get(pos).getFriends(),
                        mPropertyPics.get(pos).getReview(),mPropertyPics.get(pos).getPhotos(),mPropertyPics.get(pos).getFirst_name(),mPropertyPics.get(pos).getUser_pic());

//                UserPropertyImagesandVideos.getUrl(mPropertyPics.get(pos)
//                        .getFile_type(), mPropertyPics.get(pos).getFile());

            }
        });

        holder.profile_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(String.valueOf(v.getTag()));

                DialogManager.getInstance().showImageVideoDialog(mContext,mPropertyPics.get(pos).getFile_type(),mPropertyPics.get(pos).getFile(),
                        mPropertyPics.get(pos).getDescription(),mPropertyPics.get(pos).getDatetime(),mPropertyPics.get(pos).getFriends(),
                        mPropertyPics.get(pos).getReview(),mPropertyPics.get(pos).getPhotos(),mPropertyPics.get(pos).getFirst_name(),mPropertyPics.get(pos).getUser_pic());

//                UserPropertyImagesandVideos.getUrl(mPropertyPics.get(pos)
//                        .getFile_type(), mPropertyPics.get(pos).getFile());

            }
        });

        holder.adap_vidl1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(String.valueOf(v.getTag()));
//                DialogManager.getInstance().showImageVideoDialog(mContext,mPropertyPics.get(pos).getFile_type(),mPropertyPics.get(pos).getFile(),
//                        mPropertyPics.get(pos).getDescription(),mPropertyPics.get(pos).getDatetime(),mPropertyPics.get(pos).getFriends(),
//                        mPropertyPics.get(pos).getReview(),mPropertyPics.get(pos).getPhotos(),mPropertyPics.get(pos).getFirst_name(),mPropertyPics.get(pos).getUser_pic());

//                UserPropertyImagesandVideos.getUrl(mPropertyPics.get(pos)
//                        .getFile_type(), mPropertyPics.get(pos).getFile());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPropertyPics.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.adap_img1)
        ImageView profile_pic;

        @BindView(R.id.adap_video1)
        VideoView profile_video;

        @BindView(R.id.adap_vidl1)
        RelativeLayout adap_vidl1;

        @BindView(R.id.adap_progress1)
        ProgressBar image_prog;

        @BindView(R.id.adap_vid_progress1)
        ProgressBar video_prog;

        @BindView(R.id.adap_video_play_btn1)
        Button play_btn;

        @BindView(R.id.main_frame)
        FrameLayout frame;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
