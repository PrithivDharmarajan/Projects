package com.smaat.spark.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.model.VimeoVideoImgResponse;
import com.smaat.spark.services.APICommonInterface;
import com.smaat.spark.ui.YoutubeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
            addConverterFactory(GsonConverterFactory.create()).build();
    private APICommonInterface mServiceInterface = retrofit.create(APICommonInterface.class);

    private Context mContext;
    private ArrayList<ChatReceiveEntity> mChatListRes;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);


    public ChatAdapter(Context context, ArrayList<ChatReceiveEntity> chatReceiveEntityRes) {
        mContext = context;
        mChatListRes = chatReceiveEntityRes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_chat_test_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        ChatReceiveEntity chatReceiveRes = mChatListRes.get(position);

        mChatListRes.get(position).setMessage_type(AppConstants.FAILURE_CODE);
        if (GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()).contains(mContext.getString(R.string.youtube_link)) || GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()).contains(mContext.getString(R.string.youtube)) || GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()).contains(mContext.getString(R.string.ph_youtube_format))) {
            mChatListRes.get(position).setMessage_type(AppConstants.SUCCESS_CODE);
        } else if (GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()).contains(mContext.getString(R.string.vimeo_link)) || GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()).contains(mContext.getString(R.string.vimeo))) {
            mChatListRes.get(position).setMessage_type(mContext.getString(R.string.two));
        }

        if (chatReceiveRes.getMsg_sent_user().equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
            holder.mLeftLay.setVisibility(View.GONE);
            holder.mRightLay.setVisibility(View.VISIBLE);

            if (chatReceiveRes.getDatetimeVisible().equals(AppConstants.FAILURE_CODE)) {
                holder.mRightDateTxt.setText(GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime));

//            } else {
//                mChatListRes.get(position).setDatetimeVisible(AppConstants.SUCCESS_CODE);
//                if (position > 1 && mChatListRes.get(position - 1).getMsg_sent_user().equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
//                    if (GlobalMethods.getCustomDateFormat(mChatListRes.get(position - 1).getDatetime(), mTargetDateTime).equals(GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime))) {
//
//                    }
//                }
            }

            if (chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE) || chatReceiveRes.getMessage_type().equalsIgnoreCase(mContext.getString(R.string.two))) {
                //Right Side Youtube Msg
                String videoIDStr = chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? GlobalMethods.getYoutubeVideoID(GlobalMethods.getMsgDecoder(mChatListRes.get(position).getMessage())) : GlobalMethods.getVimeoVideoID(GlobalMethods.getMsgDecoder(mChatListRes.get(position).getMessage()));

                System.out.println("Chat videoIDStr" + videoIDStr);
                mChatListRes.get(position).setVideo_id(videoIDStr);
                holder.mRightNormalVideoImg.setVisibility(View.GONE);
                if (videoIDStr.isEmpty()) {
                    holder.mRightNormalVideoImg.setImageResource(R.drawable.default_user_img);
                } else {
                    try {
                        holder.mRightNormalVideoImg.setVisibility(View.VISIBLE);

                        if (chatReceiveRes.getMessage_type().equalsIgnoreCase(mContext.getString(R.string.two))) {
                            if (chatReceiveRes.getVideo_img().isEmpty()) {
                                callVimeoImageAPI(AppConstants.VIMEO_IMG_API + videoIDStr, holder.mRightNormalVideoImg, position);
                            } else {
                                Glide.with(mContext)
                                        .load(videoIDStr).into(holder.mRightNormalVideoImg);
                            }
                        } else if (chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            videoIDStr = "http://img.youtube.com/vi/" + videoIDStr + "/0.jpg";
                            mChatListRes.get(position).setVideo_img(videoIDStr);
                            Glide.with(mContext)
                                    .load(videoIDStr).into(holder.mRightNormalVideoImg);
                        }
                    } catch (Exception e) {
                        Log.d(AppConstants.TAG, e.toString());
                        holder.mRightNormalVideoImg.setImageResource(R.drawable.default_user_img);
                    }
                }

            } else {
                //Right Side Normal Msg
                holder.mRightNormalVideoImg.setVisibility(View.GONE);
                holder.mRightMsgTxt.setVisibility(View.VISIBLE);
                holder.mRightMsgTxt.setText(GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()));
            }

            if (chatReceiveRes.getUserimage().isEmpty()) {
                holder.mRightProfileImg.setImageResource(R.drawable.default_user_img);
            } else {
                try {
                    Glide.with(mContext)
                            .load(chatReceiveRes.getUserimage())
                            .into(holder.mRightProfileImg);
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());
                    holder.mRightProfileImg.setImageResource(R.drawable.default_user_img);
                }
            }
        } else {

            holder.mLeftLay.setVisibility(View.VISIBLE);
            holder.mRightLay.setVisibility(View.GONE);
            if (chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE) || chatReceiveRes.getMessage_type().equalsIgnoreCase(mContext.getString(R.string.two))) {
                //Left Side Youtube Msg
                String videoIDStr = chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? GlobalMethods.getYoutubeVideoID(GlobalMethods.getMsgDecoder(mChatListRes.get(position).getMessage())) : GlobalMethods.getVimeoVideoID(GlobalMethods.getMsgDecoder(mChatListRes.get(position).getMessage()));
                mChatListRes.get(position).setVideo_id(videoIDStr);
                holder.mRightNormalVideoImg.setVisibility(View.GONE);
                if (videoIDStr.isEmpty()) {
                    holder.mLeftNormalVideoImg.setImageResource(R.drawable.default_user_img);
                } else {
                    try {
                        holder.mLeftNormalVideoImg.setVisibility(View.VISIBLE);
                        if (chatReceiveRes.getMessage_type().equalsIgnoreCase(mContext.getString(R.string.two))) {
                            if (chatReceiveRes.getVideo_img().isEmpty()) {
                                callVimeoImageAPI(AppConstants.VIMEO_IMG_API + videoIDStr, holder.mLeftNormalVideoImg, position);
//                                Glide.with(mContext)
//                                        .load("http://i.vimeocdn.com/video/611866076_640.jpg").into(holder.mLeftNormalVideoImg);

                            } else {
                                Glide.with(mContext)
                                        .load(videoIDStr).into(holder.mLeftNormalVideoImg);
                            }
                        } else if (chatReceiveRes.getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            videoIDStr = "http://img.youtube.com/vi/" + videoIDStr + "/0.jpg";
                            mChatListRes.get(position).setVideo_img(videoIDStr);
                            Glide.with(mContext)
                                    .load(videoIDStr).into(holder.mLeftNormalVideoImg);
                        }
                    } catch (Exception e) {
                        Log.d(AppConstants.TAG, e.toString());
                        holder.mLeftNormalVideoImg.setImageResource(R.drawable.default_user_img);
                    }
                }

            } else {
                //Left Side Normal Msg
                holder.mLeftNormalVideoImg.setVisibility(View.GONE);
                holder.mLeftMsgTxt.setVisibility(View.VISIBLE);
                holder.mLeftMsgTxt.setText(GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()));
            }

            if (chatReceiveRes.getFriendimage().isEmpty()) {
                holder.mLeftProfileImg.setImageResource(R.drawable.default_user_img);
            } else {
                try {
                    Glide.with(mContext)
                            .load(chatReceiveRes.getFriendimage())
                            .into(holder.mLeftProfileImg);
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());
                    holder.mLeftProfileImg.setImageResource(R.drawable.default_user_img);
                }
            }

            holder.mLeftDateTxt.setText(GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime));

        }

        if (chatReceiveRes.getMessage().isEmpty()) {
            holder.mLeftLay.setVisibility(View.GONE);
            holder.mRightLay.setVisibility(View.GONE);
        }

        holder.mLeftNormalVideoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mChatListRes.get(holder.getAdapterPosition()).getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    AppConstants.YOUTUBE_VIDEO_ID = mChatListRes.get(holder.getAdapterPosition()).getVideo_id();
                    intent = new Intent(mContext, YoutubeScreen.class);
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalMethods.getMsgDecoder(mChatListRes.get(holder.getAdapterPosition()).getMessage())));
                }

                mContext.startActivity(intent);
            }
        });
        holder.mRightNormalVideoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mChatListRes.get(holder.getAdapterPosition()).getMessage_type().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    AppConstants.YOUTUBE_VIDEO_ID = mChatListRes.get(holder.getAdapterPosition()).getVideo_id();
                    intent = new Intent(mContext, YoutubeScreen.class);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalMethods.getMsgDecoder(mChatListRes.get(holder.getAdapterPosition()).getMessage())));
                }

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.left_lay)
        LinearLayout mLeftLay;

        @BindView(R.id.right_lay)
        RelativeLayout mRightLay;

        @BindView(R.id.left_profile_img)
        ImageView mLeftProfileImg;

        @BindView(R.id.right_profile_img)
        ImageView mRightProfileImg;

        @BindView(R.id.left_msg_txt)
        TextView mLeftMsgTxt;

        @BindView(R.id.right_msg_txt)
        TextView mRightMsgTxt;

        @BindView(R.id.left_normal_video_img)
        ImageView mLeftNormalVideoImg;

        @BindView(R.id.right_normal_video_img)
        ImageView mRightNormalVideoImg;

        @BindView(R.id.left_date_txt)
        TextView mLeftDateTxt;

        @BindView(R.id.right_date_txt)
        TextView mRightDateTxt;


        Holder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    private void callVimeoImageAPI(String loginInput, final ImageView imageView, final int pos) {

        mServiceInterface.getVimeoImageAPICall(loginInput).enqueue(new Callback<VimeoVideoImgResponse>() {
            @Override
            public void onResponse(Call<VimeoVideoImgResponse> call, Response<VimeoVideoImgResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getThumbnail_url().isEmpty()) {
                        imageView.setImageResource(R.drawable.default_user_img);
                    } else {
                        try {
                            mChatListRes.get(pos).setVideo_img(response.body().getThumbnail_url());
                            System.out.println("pos Vimeo video Pic" + response.body().getThumbnail_url());
                            imageView.setVisibility(View.VISIBLE);
                            Glide.with(mContext)
                                    .load(response.body().getThumbnail_url()).into(imageView);
//                            mChatListRes.notifyAll();
//                            notifyDataSetChanged();
//                            notifyItemChanged(pos);
                        } catch (Exception e) {
                            Log.d(AppConstants.TAG, e.toString());
                            imageView.setImageResource(R.drawable.default_user_img);
                        }
                    }

                } else {
                    imageView.setImageResource(R.drawable.default_user_img);
                }
            }

            @Override
            public void onFailure(Call<VimeoVideoImgResponse> call, Throwable t) {

            }
        });

    }

}
