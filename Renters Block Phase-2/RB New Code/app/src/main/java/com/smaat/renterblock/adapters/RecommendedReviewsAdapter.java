package com.smaat.renterblock.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyReview;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;
import com.smaat.renterblock.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RecommendedReviewsAdapter extends RecyclerView.Adapter<RecommendedReviewsAdapter.Holder> {
    BaseFragment mContext;
    ArrayList<PropertyReview> mPropertyReviewList;


    public RecommendedReviewsAdapter(BaseFragment context,
                                     ArrayList<PropertyReview> mPropertyReviewResponse) {
        mContext = context;
        mPropertyReviewList = mPropertyReviewResponse;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recommended_reviews, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final PropertyReview mPropertyReview = mPropertyReviewList
                .get(position);


        holder.recommend_img_lay.setTag(position);
        holder.mRecommend_lay.setTag(position);

        holder.chat_icon_adapter.setTag(position);

        holder.mRecommend_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int pos = Integer.parseInt(String.valueOf(v.getTag()));

                DialogManager.getInstance().showReviewDialog(mContext.getContext(), mPropertyReviewList.get(pos), pos, new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {


                      // PropertyDetailsFragment.callGroupIdService(UserID, mPropertyReviewList.get(pos).getReview_user_id(), pos);
                    }
                });
                            }
        });

        if (mPropertyReviewList.get(position).getReview_user_id()
                .equalsIgnoreCase(PreferenceUtil.getUserID(mContext.getContext()))) {
            holder.chat_icon_adapter.setVisibility(View.GONE);
        } else {
            holder.chat_icon_adapter.setVisibility(View.VISIBLE);
        }

        holder.chat_icon_adapter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(String.valueOf(v.getTag()));
                APIRequestHandler.getInstance().getChatID(mPropertyReviewList.get(holder.getAdapterPosition()).getReview_user_id(),mPropertyReviewList.get(holder.getAdapterPosition()).getReview_user_details().get(0).getFirst_name(),mContext);
            }
        });

//        holder.recommend_img_lay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                int pos = Integer.parseInt(String.valueOf(v.getTag()));
//                Intent profile = new Intent(mContext, ProfileScreen.class);
//                profile.putExtra("user_id", mPropertyReview
//                        .getReview_user_details().get(pos).getUser_id());
//                profile.putExtra("call_from", "Agent");
//                mContext.startActivity(profile);
//
//            }
//        });

        Glide.with(mContext)
                .load(mPropertyReview.getReview_user_details().get(0).getUser_pic()).error(R.drawable.profile_pic)
                .into(holder.mRecommandReviewImage);

        float rating = Float.parseFloat(mPropertyReview.getRating());
        holder.recommand_revie_rating.setRating(rating);
        holder.review_user_name.setText(mPropertyReview
                .getReview_user_details().get(0).getFirst_name());
        holder.recommand_review_comment.setText(mPropertyReview.getComments());
        holder.recommand_review_time.setText(TimeUtil.getTimeDifference(mPropertyReview.getDate_time()));

    }


    @Override
    public int getItemCount() {
        return mPropertyReviewList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_user_name)
        TextView review_user_name;

        @BindView(R.id.recommand_revie_rating)
        RatingBar recommand_revie_rating;

        @BindView(R.id.recommand_review_comment)
        TextView recommand_review_comment;

        @BindView(R.id.recommand_review_time)
        TextView recommand_review_time;

        @BindView(R.id.recommend_img_lay)
        RelativeLayout recommend_img_lay;

        @BindView(R.id.chat_icon_adapter)
        Button chat_icon_adapter;

        @BindView(R.id.recommend_lay1)
        RelativeLayout mRecommend_lay;

        @BindView(R.id.recommand_review_image)
        ImageView mRecommandReviewImage;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
