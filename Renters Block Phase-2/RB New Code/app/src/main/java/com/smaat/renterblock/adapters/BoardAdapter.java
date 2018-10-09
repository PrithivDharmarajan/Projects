package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.BoardsEntity;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.fragments.MyBoardMessageFragment;
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

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ItemViewHolder> {

    private Context mContext;
    private List<BoardsEntity> mBoardPropertyList;

    public BoardAdapter(Context context, ArrayList<BoardsEntity> mPropertyList) {
        mContext = context;
        this.mBoardPropertyList = mPropertyList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_property_listing_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        if (mBoardPropertyList.size() > 0 && mBoardPropertyList.get(position).getProperty_details().size() > 0) {

            final PropertyEntity propertyEntity = mBoardPropertyList.get(position).getProperty_details().get(0);

            holder.mAddress.setText(propertyEntity.getAddress());
            holder.mReviewsCount.setText("(" + mBoardPropertyList.get(position).getReview_count()
                    + ")");
            holder.mRatingBar.setRating(NumberUtil.getRatingVal(propertyEntity.getRating()));

            if (mBoardPropertyList.get(position).getProperty_pics().size() > 0) {
                if (mBoardPropertyList.get(position).getProperty_pics().get(0).getFile().isEmpty()) {
                    holder.mBuildingImg.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(propertyEntity.getProperty_pics().get(0).getFile())
                                .into(holder.mBuildingImg);
                    } catch (Exception ex) {
                        holder.mBuildingImg.setImageResource(R.drawable.default_prop_icon);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            } else {
                holder.mBuildingImg.setImageResource(R.drawable.default_prop_icon);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstants.BOARD_MESSAGE_PROPERTY_ID=mBoardPropertyList.get(holder.getAdapterPosition()).getProperty_id();
                    ((HomeScreen)mContext).addFragment(new MyBoardMessageFragment());

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return mBoardPropertyList.size();
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


        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
