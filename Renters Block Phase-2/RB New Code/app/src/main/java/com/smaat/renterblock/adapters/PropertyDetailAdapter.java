package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.InterfaceAPICommonCallback;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyDetailAdapter extends RecyclerView.Adapter<PropertyDetailAdapter.Holder> {

    private Context mContext;
    private ArrayList<PropertyEntity> mPropertyList = new ArrayList<>();
    private InterfaceBtnCallback mCallback;
    public PropertyDetailAdapter(Context context, ArrayList<PropertyEntity> mList, InterfaceBtnCallback callback) {

        mPropertyList = mList;
        mContext = context;
        mCallback=callback;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_property_details, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        PropertyEntity propertyDetailsRes = mPropertyList.get(position);

        try {
            if (propertyDetailsRes.getPrice_range().isEmpty()) {
                holder.mCostTxt.setText(mContext.getString(R.string.na));
            } else {
                String priceRangeStr = propertyDetailsRes.getPrice_range();
                String priceStr = NumberFormat.getNumberInstance(Locale.US).format(
                        Integer.parseInt(priceRangeStr));
                Integer validate = Integer.parseInt(propertyDetailsRes
                        .getPrice_range());
                if (validate <= 2) {
                    holder.mCostTxt.setText(mContext.getString(R.string.dollor) + priceRangeStr + mContext.getString(R.string.mo));
                } else {
                    holder.mCostTxt.setText(mContext.getString(R.string.dollor) + priceStr + mContext.getString(R.string.mo));
                }
            }
            holder.mLocationTxt.setText(propertyDetailsRes.getAddress());
            holder.mDetailTxt.setText(propertyDetailsRes.getBeds() + " " + mContext.getString(R.string.bed) + " "
                    + propertyDetailsRes.getBaths() + " " + mContext.getString(R.string.bath));
            if (propertyDetailsRes.getReview_count() != null
                    && (propertyDetailsRes.getReview_count().isEmpty())) {
                holder.mReviewCountTxt.setText(AppConstants.FAILURE_CODE);
            } else {
                holder.mReviewCountTxt.setText("( " + propertyDetailsRes.getReview_count() + " )");
            }

            if (propertyDetailsRes.getProperty_rating() != null) {
                try {
                    if (!propertyDetailsRes.getProperty_rating().isEmpty()) {
                        float propertyRatingFloat = Float.parseFloat(propertyDetailsRes
                                .getProperty_rating());
                        holder.mRatingBar.setRating(propertyRatingFloat);

                    } else {
                        holder.mRatingBar.setRating(0);
                    }
                } catch (Exception e) {
                    Log.d(AppConstants.TAG, e.toString());

                }
            }

            holder.mFavouriteImg.setImageResource(propertyDetailsRes.getIsfavourite().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? R.drawable.favourites_icon : R.drawable.favourite_enable);


            /*Favourite and Un favourite API Call*/
            holder.mFavouriteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final String fav = mPropertyList.get(pos).getIsfavourite();
                    APIRequestHandler.getInstance().favouriteAPICall(mPropertyList.get(holder.getAdapterPosition()).getProperty_id(), mContext, new InterfaceAPICommonCallback() {
                        @Override
                        public void onRequestSuccess(Object obj) {

                            if (mPropertyList.get(holder.getAdapterPosition()).getIsfavourite().equals(AppConstants.SUCCESS_CODE)) {
                                mPropertyList.get(holder.getAdapterPosition()).setIsfavourite(AppConstants.FAILURE_CODE);
                            } else {
                                mPropertyList.get(holder.getAdapterPosition()).setIsfavourite(AppConstants.SUCCESS_CODE);
                            }
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onRequestFailure(Throwable r) {

                        }
                    });

                }
            });

            if (propertyDetailsRes.getProperty_datetime().isEmpty()) {
                holder.mTimeTxt.setVisibility(View.GONE);
            } else {
                holder.mTimeTxt.setVisibility(View.VISIBLE);
                holder.mTimeTxt.setText(mContext.getString(R.string.last_updated) + "  " + DateUtil.timeDiff(propertyDetailsRes.getProperty_datetime()));
            }

            /*Image Pager*/
            if (mPropertyList.get(position).getProperty_pics().size() == 0) {
                ArrayList<PropertyPictures> propertyArrPictures = new ArrayList<>();
                PropertyPictures propertyPictures = new PropertyPictures();
                propertyPictures.setFile("");
                propertyArrPictures.add(propertyPictures);
                mPropertyList.get(position).setProperty_pics(propertyArrPictures);
            }

            PropertyImageAdapter mPagerAdapter = new PropertyImageAdapter(mContext, propertyDetailsRes.getProperty_pics());
            holder.mPager.setAdapter(mPagerAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstants.DETAIL_PROPERTY_ID = mPropertyList.get(holder.getAdapterPosition()).getProperty_id();
                if(AppConstants.IS_FROM_LIST_CLICK){
                    AppConstants.IS_FROM_LIST_ITEM_CLICK=true;
                    AppConstants.IS_FROM_LIST_CLICK=false;
                }
                mCallback.onPositiveClick();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstants.DETAIL_PROPERTY_ID = mPropertyList.get(holder.getAdapterPosition()).getProperty_id();
                if(AppConstants.IS_FROM_LIST_CLICK){
                    AppConstants.IS_FROM_LIST_ITEM_CLICK=true;
                    AppConstants.IS_FROM_LIST_CLICK=false;
                }
                mCallback.onPositiveClick();


            }
        });

        holder.mPropertyImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstants.DETAIL_PROPERTY_ID = mPropertyList.get(holder.getAdapterPosition()).getProperty_id();
                if(AppConstants.IS_FROM_LIST_CLICK){
                    AppConstants.IS_FROM_LIST_ITEM_CLICK=true;
                    AppConstants.IS_FROM_LIST_CLICK=false;
                }
                mCallback.onPositiveClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.property_img_view)
        RelativeLayout mPropertyImgView;

        @BindView(R.id.property_image_pager)
        ViewPager mPager;

        @BindView(R.id.favourite_img)
        ImageView mFavouriteImg;

        @BindView(R.id.time_txt)
        TextView mTimeTxt;

        @BindView(R.id.property_location_txt)
        TextView mLocationTxt;

        @BindView(R.id.property_cost_txt)
        TextView mCostTxt;

        @BindView(R.id.property_details_txt)
        TextView mDetailTxt;

        @BindView(R.id.property_reviews_count_txt)
        TextView mReviewCountTxt;

        @BindView(R.id.review_rating_bar)
        RatingBar mRatingBar;

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

            if (mPropertyImgList.get(position).getFile().isEmpty()) {
                mPagerPropertyImage.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(mPropertyImgList.get(position).getFile()).into(mPagerPropertyImage);
                } catch (Exception e) {
                    mPagerPropertyImage.setImageResource(R.drawable.default_prop_icon);
                }
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

