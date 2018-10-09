package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.fragments.PropertyDetailsFragment;
import com.smaat.renterblock.fragments.PropertyListingFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Smaat on 8/18/2017.
 */

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ItemViewHolder> {

    private Context mContext;
    private List<PropertyEntity> mPropertyList;
    private int newWidth;
    private boolean isEditBool = false;
    DeletePropertyIDsInterface deletePropertyIDsListener;

    public PropertyAdapter(Context context, ArrayList<PropertyEntity> mPropertyList, boolean isEditBool,
                           PropertyListingFragment propertyListingFragment) {
        mContext = context;
        this.mPropertyList = mPropertyList;
        this.isEditBool = isEditBool;
        deletePropertyIDsListener = propertyListingFragment;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_property_listing_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final PropertyEntity propertyEntity = mPropertyList.get(position);


        if (isEditBool) {
            holder.mCardGridRowLay.setVisibility(View.VISIBLE);
            holder.mMainLay.setVisibility(View.GONE);
            holder.mAddress.setText(propertyEntity.getAddress());
            holder.mReviewsCount.setText("(" + propertyEntity.getReview_count()
                    + ")");

            holder.mRatingBar.setRating(NumberUtil.getRatingVal(propertyEntity.getProperty_rating()));
            if (propertyEntity.getProperty_pics().size() != 0) {
                if (propertyEntity.getProperty_pics().get(0).getFile().isEmpty()) {
                    holder.mBuildingImg.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(propertyEntity.getProperty_pics().get(0).getFile()).error(R.drawable.default_prop_icon)
                                .into(holder.mBuildingImg);
                    } catch (Exception ex) {
                        holder.mBuildingImg.setImageResource(R.drawable.default_prop_icon);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            }


            holder.mCardGridRowLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppConstants.DETAIL_PROPERTY_ID = mPropertyList.get(holder.getAdapterPosition()).getProperty_id();
                    ((HomeScreen) mContext).addFragment(new PropertyDetailsFragment());

                }
            });

        } else {
            if (propertyEntity.isEditbool()) {
                holder.mCheckBtn.setBackgroundResource(R.drawable.tick_on);
            } else {
                holder.mCheckBtn.setBackgroundResource(R.drawable.tick_off);

            }


            holder.mMainLay.setOnClickListener(new View.OnClickListener() {
                private boolean isSelect = true;

                @Override
                public void onClick(View view) {
                    deletePropertyIDsListener.addRemoveIDs(mPropertyList.get(holder.getAdapterPosition()).getProperty_id(),
                            holder.mCheckBtn, !propertyEntity.isEditbool(), mPropertyList.get(holder.getAdapterPosition()));
                    mPropertyList.get(holder.getAdapterPosition()).setEditbool(!propertyEntity.isEditbool());
                }
            });
            holder.mCheckBtn.setOnClickListener(new View.OnClickListener() {
                private boolean isSelect = true;

                @Override
                public void onClick(View view) {
                    deletePropertyIDsListener.addRemoveIDs(mPropertyList.get(holder.getAdapterPosition()).getProperty_id(),
                            holder.mCheckBtn, !propertyEntity.isEditbool(), mPropertyList.get(holder.getAdapterPosition()));
                    mPropertyList.get(holder.getAdapterPosition()).setEditbool(!propertyEntity.isEditbool());

                }
            });
            holder.mCardGridRowLay.setVisibility(View.GONE);
            holder.mMainLay.setVisibility(View.VISIBLE);
            holder.mPropertyCostTxt.setText("$" + ""
                    + propertyEntity.getPrice_range());
            holder.mPropertyDetailsTxt.setText(propertyEntity.getBeds()
                    + mContext.getString(R.string.bed) + propertyEntity.getBaths()
                    + mContext.getString(R.string.bath));
            holder.mPropertyLocationTxt
                    .setText(propertyEntity.getAddress());
            holder.mPropertyReviews
                    .setText("( " + propertyEntity
                            .getReview_count() + " )");
            if (propertyEntity.getProperty_rating() != null) {
                holder.mRating.setRating(Float.valueOf(propertyEntity
                        .getProperty_rating()));
            } else {
                holder.mRating.setRating(0);
            }


            if (propertyEntity.getProperty_pics().size() != 0) {
                if (propertyEntity.getProperty_pics().get(0).getFile().isEmpty()) {
                    holder.mPropertyImage.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(propertyEntity.getProperty_pics().get(0).getFile()).error(R.drawable.default_prop_icon)
                                .into(holder.mPropertyImage);
                    } catch (Exception ex) {
                        holder.mPropertyImage.setImageResource(R.drawable.default_prop_icon);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.address)
        TextView mAddress;
        @BindView(R.id.reviews_count)
        TextView mReviewsCount;

        @BindView(R.id.review_rating_bar)
        RatingBar mRatingBar;

        @BindView(R.id.building_image)
        ImageView mBuildingImg;

        @BindView(R.id.grid_card_lay)
        RelativeLayout mCardGridRowLay;


        /*Edit or Delete flow ids*/
        @BindView(R.id.property_cost)
        TextView mPropertyCostTxt;
        @BindView(R.id.property_details)
        TextView mPropertyDetailsTxt;
        @BindView(R.id.property_location)
        TextView mPropertyLocationTxt;
        @BindView(R.id.time)
        TextView mDateTxt;
        @BindView(R.id.property_reviews)
        TextView mPropertyReviews;
        @BindView(R.id.review_ratingbar)
        RatingBar mRating;
        @BindView(R.id.main_lay)
        RelativeLayout mMainLay;
        @BindView(R.id.property_image)
        ImageView mPropertyImage;

        @BindView(R.id.check_btn)
        Button mCheckBtn;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface DeletePropertyIDsInterface {
        void addRemoveIDs(String propid, View view, boolean isSelect, PropertyEntity propertyEntity);
    }


}
