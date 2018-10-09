package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FilterAPIEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.fragments.SavedSearchFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedSearchAdapter extends RecyclerView.Adapter<SavedSearchAdapter.Holder> {
    private Context mContext;
    private ArrayList<LocalSavedSearchEntity> mSavedSearchList;
    private boolean isEditVar;


    public SavedSearchAdapter(Context context, ArrayList<LocalSavedSearchEntity> savedSearchList, boolean isEdit) {
        mContext = context;
        mSavedSearchList = savedSearchList;
        isEditVar = isEdit;
        AppConstants.SELECTED_SAVED_SEARCH_ARRAY = new ArrayList<>();
        AppConstants.SELECTED_SAVED_SEARCH_IDS = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_saved_search, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        LocalSavedSearchEntity entity = mSavedSearchList.get(position);
        if (isEditVar){
            holder.mSelectedImg.setVisibility(View.VISIBLE);
        }
        else {
            holder.mSelectedImg.setVisibility(View.GONE);
            entity.setSelected(false);
        }


        try {
            final FilterEntity filterEntity = new Gson().fromJson(entity.getFilter_object(), FilterEntity.class);

            if (filterEntity != null) {
                holder.mLocationTxt.setText(filterEntity.getFilter_name());


                    holder.mSelectedImg.setImageResource(isEditVar && entity.isSelected() ? R.drawable.tick_on : R.drawable.tick_off);


                String mPriceMinStr = filterEntity.getPrice_range_min().isEmpty() ? mContext.getString(R.string.no_min) : ("$" + filterEntity.getPrice_range_min());


                String mPriceMaxStr = filterEntity.getPrice_range_max().isEmpty() ? mContext.getString(R.string.no_max) : ("$" + filterEntity.getPrice_range_max());


                String mBedCountStr;
                if (filterEntity.getBeds().isEmpty()) {
                    mBedCountStr = mContext.getString(R.string.any_beds);
                } else {
                    if ((filterEntity.getBeds().equals(AppConstants.SUCCESS_CODE) || filterEntity.getBeds().equals(AppConstants.FAILURE_CODE))) {
                        mBedCountStr = filterEntity.getBeds() + mContext.getString(R.string.bed_single);
                    } else {
                        mBedCountStr = filterEntity.getBeds() + mContext.getString(R.string.beds);
                    }
                }
                String mBathCountStr;
                if (filterEntity.getBaths().isEmpty()) {
                    mBathCountStr = mContext.getString(R.string.any_baths);
                } else {
                    if ((filterEntity.getBaths().equals(AppConstants.SUCCESS_CODE) || filterEntity.getBaths().equals(AppConstants.FAILURE_CODE))) {
                        mBathCountStr = filterEntity.getBaths() + mContext.getString(R.string.bath_single);
                    } else {
                        mBathCountStr = filterEntity.getBaths() + mContext.getString(R.string.baths);
                    }
                }


                holder.mFilterDetailTxt.setText(String.format(mContext.getString(R.string.price_range_show_format), entity.getFilter_type(), mPriceMinStr, mPriceMaxStr));
                holder.mBedBathTxt.setText(String.format(mContext.getString(R.string.bed_bath_count_show_format), mBedCountStr, mBathCountStr));

            /*Edit view*/
                holder.mParentLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isEditVar && !mSavedSearchList.get(holder.getAdapterPosition()).isSelected()) {
                            mSavedSearchList.get(holder.getAdapterPosition()).setSelected(true);
                            holder.mSelectedImg.setImageResource(R.drawable.tick_on);
                            AppConstants.SELECTED_SAVED_SEARCH_IDS.add(mSavedSearchList.get(holder.getAdapterPosition()).getSave_search_id());
                            AppConstants.SELECTED_SAVED_SEARCH_ARRAY.add(mSavedSearchList.get(holder.getAdapterPosition()));
                        } else if (isEditVar && mSavedSearchList.get(holder.getAdapterPosition()).isSelected()) {
                            mSavedSearchList.get(holder.getAdapterPosition()).setSelected(false);
                            holder.mSelectedImg.setImageResource(R.drawable.tick_off);
                            AppConstants.SELECTED_SAVED_SEARCH_IDS.remove(mSavedSearchList.get(holder.getAdapterPosition()).getSave_search_id());
                            AppConstants.SELECTED_SAVED_SEARCH_ARRAY.remove(mSavedSearchList.get(holder.getAdapterPosition()));
                        } else if (!isEditVar) {
//                            FilterAPIEntity mFilterAPIEntity = new FilterAPIEntity();
//                            mFilterAPIEntity.setRent(new Gson().toJson(AppConstants.RENT_FILTER_ENTITY));
//                            mFilterAPIEntity.setSale(new Gson().toJson(AppConstants.SALE_FILTER_ENTITY));
//                            PreferenceUtil.storeFilterObject(mContext, AppConstants.RENT_FILTER_ENTITY, AppConstants.SALE_FILTER_ENTITY);



                            AppConstants.saved_search_json = mSavedSearchList.get(holder.getAdapterPosition()).getFilter_object();
                            AppConstants.saved_search_Latitude = filterEntity.getLatitude();
                            AppConstants.saved_search_Longitude = filterEntity.getLongitude();
                            AppConstants.saved_location_name = filterEntity.getFilter_name();
                            AppConstants.TYPE_OF_FILTER = AppConstants.RENT;
                            AppConstants.TYPE_OF_PROPERTY = AppConstants.RENT;
                            AppConstants.MAP_CURRENT_BACK_FRAGMENT = new SavedSearchFragment();
                            ((HomeScreen) mContext).addFragment(new MapFragment());
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return mSavedSearchList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.location_txt)
        TextView mLocationTxt;
        @BindView(R.id.filter_details_txt)
        TextView mFilterDetailTxt;
        @BindView(R.id.bed_bath_txt)
        TextView mBedBathTxt;
        @BindView(R.id.selection_img)
        ImageView mSelectedImg;
        @BindView(R.id.parent_lay)
        LinearLayout mParentLay;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
