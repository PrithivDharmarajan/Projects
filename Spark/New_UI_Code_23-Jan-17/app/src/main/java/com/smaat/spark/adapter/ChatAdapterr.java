package com.smaat.spark.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
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
import com.smaat.spark.fragment.UserDetailsFragment;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.ui.YoutubeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapterr extends RecyclerView.Adapter<ChatAdapterr.Holder> {

    private Context mContext;
    private ArrayList<ChatReceiveEntity> mChatListRes;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);


    public ChatAdapterr(Context context, ArrayList<ChatReceiveEntity> chatReceiveEntityRes) {
        mContext = context;
        mChatListRes = chatReceiveEntityRes;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_chat_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        ChatReceiveEntity chatReceiveRes = mChatListRes.get(position);
        String headerDateStr = AppConstants.SUCCESS_CODE, msgDateVisibleStr = AppConstants.SUCCESS_CODE;


        if (position > 0 && chatReceiveRes.getMsg_sent_user().equalsIgnoreCase(mChatListRes.get(position - 1).getMsg_sent_user()) && GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime).equalsIgnoreCase(GlobalMethods.getCustomDateFormat(mChatListRes.get(position - 1).getDatetime(), mTargetDateTime))) {
            msgDateVisibleStr = AppConstants.FAILURE_CODE;
        }

        if (position > 0 && mChatListRes.get(position - 1).getHeader_datetime().equals(GlobalMethods.checkBetweentime(mContext, chatReceiveRes.getDatetime()))) {
            headerDateStr = AppConstants.FAILURE_CODE;
        }


        chatReceiveRes.setDatetimeVisible(msgDateVisibleStr);
        chatReceiveRes.setHeader_datetimeVisible(headerDateStr);

        if (!chatReceiveRes.getHeader_datetimeVisible().equals(AppConstants.FAILURE_CODE)) {
            holder.mHeaderDateTxt.setText(chatReceiveRes.getHeader_datetime());
        }
        holder.mHeaderDateTxt.setVisibility(chatReceiveRes.getHeader_datetimeVisible().equals(AppConstants.FAILURE_CODE) ? View.GONE : View.VISIBLE);


        if (chatReceiveRes.getMsg_sent_user().equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
            holder.mLeftLay.setVisibility(View.GONE);
            holder.mRightLay.setVisibility(View.VISIBLE);

            if (chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE)) {
                holder.mRightDateTxt.setText(GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime));
            }
            holder.mRightDateTxt.setVisibility(chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
            holder.mRightSideImg.setVisibility(chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
            holder.mRightLay.setVisibility(View.VISIBLE);

            if (!chatReceiveRes.getIs_attachement().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                //Right Side Youtube Msg

                holder.mRightNormalVideoImg.setVisibility(View.VISIBLE);
//                holder.mRightNormalVideoTxt.setVisibility(chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : ViewGroup.GONE);
                holder.mRightNormalVideoTxt.setText(GlobalMethods.getMsgDecoder(chatReceiveRes.getAttachement_title()));

                System.out.println("Adapter Att URL---" + chatReceiveRes.getAttachement_url());
                holder.mRightNormalVideoTxt.setText(chatReceiveRes.getIs_attachement().equalsIgnoreCase(mContext.getString(R.string.three)) ? GlobalMethods.getYoutubeTitle(chatReceiveRes.getVideo_id()) : GlobalMethods.getMsgDecoder(chatReceiveRes.getAttachement_title()));
                holder.mRightNormalVideoTxt.setVisibility(holder.mRightNormalVideoTxt.getText().toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                holder.mRightMsgTxt.setVisibility(View.GONE);
                holder.mRightNormalVideoImg.setVisibility(View.VISIBLE);
                if (chatReceiveRes.getIs_attachement().isEmpty() || chatReceiveRes.getIs_attachement().equals(AppConstants.FAILURE_CODE)) {
                    holder.mRightNormalVideoImg.setImageResource(R.drawable.default_user_img);
                } else {
                    try {

//                        if (chatReceiveRes.getIs_attachement().equals(mContext.getString(R.string.two))) {
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 8;
//                            final Bitmap bitmap = BitmapFactory.decodeFile(chatReceiveRes.getAttachement_url(), options);
//                            holder.mRightNormalVideoImg.setImageBitmap(bitmap);
//                        } else {
                        Glide.with(mContext)
                                .load(chatReceiveRes.getAttachement_url()).into(holder.mRightNormalVideoImg);
//                        Glide.with(mContext)
////                                .load(new File(chatReceiveRes.getAttachement_url())) // Uri of the picture
//                                .load(Uri.parse(chatReceiveRes.getAttachement_url())) // Uri of the picture


//                                .into(holder.mRightNormalVideoImg);

//                        }
                    } catch (Exception e) {
                        Log.d(AppConstants.TAG, e.toString());
                        holder.mRightNormalVideoImg.setImageResource(R.drawable.default_user_img);
                    }
                }
            } else {
                //Right Side Normal Msg
                holder.mRightNormalVideoImg.setVisibility(View.GONE);
                holder.mRightNormalVideoTxt.setVisibility(View.GONE);
                holder.mRightMsgTxt.setVisibility(View.VISIBLE);
                holder.mRightMsgTxt.setText(GlobalMethods.getMsgDecoder(chatReceiveRes.getMessage()));
            }

            if (chatReceiveRes.getDatetimeVisible().equals(AppConstants.FAILURE_CODE)) {
                holder.mRightProfileLay.setVisibility(View.INVISIBLE);
            } else if (chatReceiveRes.getUser_connect_anonymous().equals(AppConstants.SUCCESS_CODE)) {
                holder.mRightProfileLay.setVisibility(View.VISIBLE);
                holder.mRightProfileImg.setImageResource(R.drawable.profile_anonymous_img);

            } else if (chatReceiveRes.getUserimage().isEmpty()) {
                holder.mRightProfileLay.setVisibility(View.VISIBLE);
                holder.mRightProfileImg.setImageResource(R.drawable.default_user_img);
            } else {
                holder.mRightProfileLay.setVisibility(View.VISIBLE);
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

            holder.mLeftLay.setVisibility(View.VISIBLE);
            holder.mLeftSideImg.setVisibility(chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);


            if (!chatReceiveRes.getIs_attachement().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                //Left Side Youtube Msg

                holder.mLeftNormalVideoImg.setVisibility(View.VISIBLE);
                holder.mLeftNormalVideoTxt.setText(GlobalMethods.getMsgDecoder(chatReceiveRes.getAttachement_title()));


                holder.mLeftNormalVideoTxt.setText(chatReceiveRes.getIs_attachement().equalsIgnoreCase(mContext.getString(R.string.three)) ? GlobalMethods.getYoutubeTitle(chatReceiveRes.getVideo_id()) : GlobalMethods.getMsgDecoder(chatReceiveRes.getAttachement_title()));
                holder.mLeftNormalVideoTxt.setVisibility(holder.mLeftNormalVideoTxt.getText().toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                holder.mLeftMsgTxt.setVisibility(View.GONE);

                if (chatReceiveRes.getIs_attachement().isEmpty() || chatReceiveRes.getIs_attachement().equals(AppConstants.FAILURE_CODE)) {
                    holder.mLeftNormalVideoImg.setImageResource(R.drawable.default_user_img);

                } else {
                    try {
                        if (chatReceiveRes.getIs_attachement().equalsIgnoreCase(mContext.getString(R.string.two))) {
                            System.out.println("chatReceiveRes.getAttachement_url()---" + chatReceiveRes.getAttachement_url());
                        }
                        Glide.with(mContext)
                                .load(chatReceiveRes.getAttachement_url()).into(holder.mLeftNormalVideoImg);

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

                holder.mLeftNormalVideoTxt.setVisibility(View.GONE);
            }

            if (chatReceiveRes.getDatetimeVisible().equals(AppConstants.FAILURE_CODE)) {
                holder.mLeftProfileLay.setVisibility(View.INVISIBLE);
            } else if (chatReceiveRes.getFriend_connect_anonymous().equals(AppConstants.SUCCESS_CODE)) {
                holder.mLeftProfileLay.setVisibility(View.VISIBLE);
                holder.mLeftProfileImg.setImageResource(R.drawable.profile_anonymous_img);

            } else if (chatReceiveRes.getFriendimage().isEmpty()) {
                holder.mLeftProfileLay.setVisibility(View.VISIBLE);
                holder.mLeftProfileImg.setImageResource(R.drawable.default_user_img);
            } else {
                holder.mLeftProfileLay.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext)
                            .load(chatReceiveRes.getFriendimage())
                            .into(holder.mLeftProfileImg);
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());
                    holder.mLeftProfileImg.setImageResource(R.drawable.default_user_img);
                }
            }

            if (chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE)) {
                holder.mLeftDateTxt.setText(GlobalMethods.getCustomDateFormat(chatReceiveRes.getDatetime(), mTargetDateTime));
            }

            holder.mLeftDateTxt.setVisibility(chatReceiveRes.getDatetimeVisible().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
        }

//        if (chatReceiveRes.getMessage().isEmpty()) {
//            holder.mHeaderDateTxt.setVisibility(View.GONE);
//            holder.mLeftLay.setVisibility(View.GONE);
//            holder.mRightLay.setVisibility(View.GONE);
//        }

        holder.mLeftNormalVideoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mChatListRes.get(holder.getAdapterPosition()).getIs_attachement().equalsIgnoreCase(mContext.getString(R.string.two))) {
                    AppConstants.YOUTUBE_VIDEO_ID = mChatListRes.get(holder.getAdapterPosition()).getVideo_id();
                    intent = new Intent(mContext, YoutubeScreen.class);
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
                if (mChatListRes.get(holder.getAdapterPosition()).getIs_attachement().equalsIgnoreCase(mContext.getString(R.string.two))) {
                    AppConstants.YOUTUBE_VIDEO_ID = mChatListRes.get(holder.getAdapterPosition()).getVideo_id();
                    intent = new Intent(mContext, YoutubeScreen.class);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalMethods.getMsgDecoder(mChatListRes.get(holder.getAdapterPosition()).getMessage())));
                }

                mContext.startActivity(intent);
            }
        });
//
        holder.mLeftProfileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((HomeScreen) mContext).mHeaderTxt.getText().toString().equals(mContext.getString(R.string.anonymous))) {

                    AppConstants.CHAT_USER_BACK=AppConstants.SUCCESS_CODE;
                    ((HomeScreen) mContext).replaceFragment(new UserDetailsFragment(), 1);
                }
            }
        });
//        holder.mRightNormalVideoImg.setVisibility(View.VISIBLE);
//        try {
//
//            Glide.with(mContext)
//                    .load(chatReceiveRes.getAttachement_url()).into(holder.mRightNormalVideoImg);
//
//        } catch (Exception e) {
//            Log.d(AppConstants.TAG, e.toString());
//            holder.mRightNormalVideoImg.setImageResource(R.drawable.default_user_img);
//        }
    }

    @Override
    public int getItemCount() {
        return mChatListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {


        @BindView(R.id.header_date_txt)
        TextView mHeaderDateTxt;


        @BindView(R.id.left_lay)
        LinearLayout mLeftLay;

        @BindView(R.id.right_lay)
        RelativeLayout mRightLay;

        @BindView(R.id.right_profile_lay)
        RelativeLayout mRightProfileLay;

        @BindView(R.id.left_profile_lay)
        RelativeLayout mLeftProfileLay;

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

        @BindView(R.id.left_normal_video_txt)
        TextView mLeftNormalVideoTxt;

        @BindView(R.id.right_normal_video_img)
        ImageView mRightNormalVideoImg;

        @BindView(R.id.right_normal_video_txt)
        TextView mRightNormalVideoTxt;


        @BindView(R.id.left_date_txt)
        TextView mLeftDateTxt;

        @BindView(R.id.right_date_txt)
        TextView mRightDateTxt;

        @BindView(R.id.right_side_curve_img)
        ImageView mRightSideImg;

        @BindView(R.id.left_side_curve_img)
        ImageView mLeftSideImg;


        Holder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

}
