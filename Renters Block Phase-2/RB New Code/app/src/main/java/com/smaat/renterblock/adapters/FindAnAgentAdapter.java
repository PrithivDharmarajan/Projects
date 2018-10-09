package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FindAgentFilterResultEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.fragments.FindAgentDetailsFragment;
import com.smaat.renterblock.fragments.FindAgentFragment;
import com.smaat.renterblock.fragments.ProfileFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.EmailUtil;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindAnAgentAdapter extends RecyclerView.Adapter<FindAnAgentAdapter.Holder> {

    private Context mContext;
    private ArrayList<FindAgentFilterResultEntity> mFilterList = new ArrayList<>();
    private FindAgentFragment mFindAgentFragment;

    public FindAnAgentAdapter(Context context,
                              ArrayList<FindAgentFilterResultEntity> mList, FindAgentFragment findAgentFragment) {

        mFilterList = mList;
        mContext = context;
        mFindAgentFragment = findAgentFragment;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter_find_agent, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final FindAgentFilterResultEntity findAgentFilterEntity = mFilterList.get(position);


        if (findAgentFilterEntity.getUser_pic().isEmpty()) {
            holder.mUserImagesImg.setImageResource(R.drawable.default_prop_icon);
        } else {
            try {
                Glide.with(mContext)
                        .load(findAgentFilterEntity.getUser_pic()).placeholder(R.drawable.profile_pic).into(holder.mUserImagesImg);
            } catch (Exception e) {
                holder.mUserImagesImg.setImageResource(R.drawable.profile_pic);
            }
        }

        holder.mFriendCountTxt.setText(findAgentFilterEntity.getFriends_count());
        holder.mPhotosCountTxt.setText(findAgentFilterEntity.getPhotos_count());
        holder.mReviewCountTxt.setText(findAgentFilterEntity.getReviews_count());
        holder.mUserRatingBar.setRating(NumberUtil.getRatingVal(findAgentFilterEntity.getUser_avg_rating()));

        holder.mUserNameTxt.setText(findAgentFilterEntity.getUser_name());
        holder.mAgentWithTxt.setText(findAgentFilterEntity.getUser_type() + " " + mContext.getString(R.string.with) + " " + findAgentFilterEntity.getBusiness_name());
        holder.mInPlaceTxt.setText(mContext.getString(R.string.in) + " " + findAgentFilterEntity.getCity());
        holder.mInPlaceTxt.setVisibility(findAgentFilterEntity.getCity().isEmpty() ? View.GONE : View.VISIBLE);
        holder.mSoldHomesCountTxt.setText(findAgentFilterEntity.getSoldhomes());
        holder.mActiveListingCountTxt.setText(findAgentFilterEntity.getActivelisting());
        holder.mFriendRequestImg.setVisibility(findAgentFilterEntity.getIs_friend().equals(AppConstants.FAILURE_CODE)
                && findAgentFilterEntity.getRb_user().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.INVISIBLE);
        holder.mSponsorLay.setVisibility(findAgentFilterEntity.getEnhanced_profile().equals(AppConstants.FAILURE_CODE) ?
                View.INVISIBLE : View.VISIBLE);

        holder.mChatImg.setVisibility(findAgentFilterEntity.getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
        holder.mMessageImg.setVisibility(findAgentFilterEntity.getRb_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Redirect to FindAgentDetails
                AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY = mFilterList.get(holder.getAdapterPosition());
                ((HomeScreen) mContext).addFragment(new FindAgentDetailsFragment());
            }
        });
        holder.mChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*callGroupIdService */
                APIRequestHandler.getInstance().getChatID(findAgentFilterEntity.getUser_id(),findAgentFilterEntity.getUser_name(),mFindAgentFragment);
            }
        });

        holder.mMessageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FindAgentActivity.showMessageMailer */
                if (!mFilterList.get(holder.getAdapterPosition()).getEmail_id().isEmpty()) {
                    EmailUtil.sendMail(mContext, mFilterList.get(holder.getAdapterPosition()).getEmail_id(), mContext.getString(R.string.send_agent_mail_sub), Html.fromHtml(mContext.getString(R.string.send_agent_mail_msg)) + "");
                }
            }
        });

        holder.mUserImagesImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO Redirect to Profile screen
                AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new FindAgentFragment();
                AppConstants.PROFILE_ID = mFilterList.get(holder.getAdapterPosition()).getUser_id();
                ((HomeScreen) mContext).addFragment(new ProfileFragment());

            }
        });
        holder.mFriendRequestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(mFilterList.get(holder.getAdapterPosition()).getUser_id());
            }
        });

/*need to remove*/
        holder.mMessageImg.setVisibility(View.GONE);
    }

    private void sendFriendRequest(String friend_id) {
        APIRequestHandler.getInstance().sendFriendRequest(mFindAgentFragment, friend_id);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_image)
        ImageView mUserImagesImg;

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.agent_with_txt)
        TextView mAgentWithTxt;

        @BindView(R.id.in_place_txt)
        TextView mInPlaceTxt;

        @BindView(R.id.sold_homes_count_txt)
        TextView mSoldHomesCountTxt;

        @BindView(R.id.active_listing_count_txt)
        TextView mActiveListingCountTxt;

        @BindView(R.id.active_listing_txt)
        TextView mActivitListingTxt;


        @BindView(R.id.friends_count_txt)
        TextView mFriendCountTxt;

        @BindView(R.id.reviews_count_txt)
        TextView mReviewCountTxt;

        @BindView(R.id.photos_count_txt)
        TextView mPhotosCountTxt;

        @BindView(R.id.user_rating_bar)
        RatingBar mUserRatingBar;

        @BindView(R.id.friend_request_img)
        ImageView mFriendRequestImg;

        @BindView(R.id.chat_img)
        ImageView mChatImg;

        @BindView(R.id.message_img)
        ImageView mMessageImg;

        @BindView(R.id.sponsor_lay)
        LinearLayout mSponsorLay;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class PropertyImageAdapter extends PagerAdapter {

        Context mContext;
        ArrayList<PropertyPictures> mPropertyImgList;
        LayoutInflater mLayoutInflater;
        ImageView mPagerPropertyImage;

        private PropertyImageAdapter(Context context, ArrayList<PropertyPictures> mPropertyImageList) {
            mContext = context;
            mPropertyImgList = mPropertyImageList;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mPropertyImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = mLayoutInflater.inflate(R.layout.adapter_image_pager, container, false);
            mPagerPropertyImage = view.findViewById(R.id.property_image);

            try {
                Glide.with(mContext)
                        .load(mPropertyImgList.get(position).getFile()).into(mPagerPropertyImage);
            } catch (Exception e) {
                mPagerPropertyImage.setImageResource(R.drawable.default_prop_icon);
            }


            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
