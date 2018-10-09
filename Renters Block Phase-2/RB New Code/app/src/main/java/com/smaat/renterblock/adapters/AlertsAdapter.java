package com.smaat.renterblock.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AlertsResultEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.fragments.AlertsFragment;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.Holder> {

    private Context mContext;
    private ArrayList<AlertsResultEntity> mAlertsResultList = new ArrayList<>();
    private boolean isEdits;

    public AlertsAdapter(Context context, ArrayList<AlertsResultEntity> mList, boolean isEdit) {

        mAlertsResultList = mList;
        mContext = context;
        isEdits = isEdit;
        AppConstants.ALERTS_SELECTED_IDS = new ArrayList<>();
        AppConstants.ALERT_SELECTED_LIST = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alerts_screens, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder mHolder, int position) {
        if (isEdits) {
            mHolder.mTickImg.setImageResource(R.drawable.tick_off);
            mHolder.mTickLay.setVisibility(View.VISIBLE);
        }
        final AlertsResultEntity mAlertsResultEntity = mAlertsResultList.get(position);

        mHolder.mAlertsNameTxt.setText(mAlertsResultEntity.getAlert_name());
        mHolder.mAlertsKeywordsTxt.setText(mAlertsResultEntity.getKeywords());


        if (mAlertsResultEntity.getBaths().isEmpty()) {
            mHolder.mAlertsBathsTxt.setText(mContext.getString(R.string.any_baths));
        } else {
            if (mAlertsResultEntity.getBaths().equalsIgnoreCase(AppConstants.SUCCESS_CODE) || mAlertsResultEntity.getBaths().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                mHolder.mAlertsBathsTxt.setText(mAlertsResultEntity.getBaths() + mContext.getString(R.string.plus) + " " + mContext.getString(R.string.bath));
            } else {
                mHolder.mAlertsBathsTxt.setText(mAlertsResultEntity.getBaths() + mContext.getString(R.string.plus) + " " + mContext.getString(R.string.baths));
            }
        }
        if (mAlertsResultEntity.getBeds().isEmpty()) {
            mHolder.mAlertsBedsTxt.setText(mContext.getString(R.string.any_beds));
        } else {
            if (mAlertsResultEntity.getBeds().equalsIgnoreCase(AppConstants.FAILURE_CODE) || mAlertsResultEntity.getBeds().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mHolder.mAlertsBedsTxt.setText(mAlertsResultEntity.getBeds() + mContext.getString(R.string.plus) + " " + mContext.getString(R.string.bed) + " " + mContext.getString(R.string.slash) + " ");
            } else {
                mHolder.mAlertsBedsTxt.setText(mAlertsResultEntity.getBeds() + mContext.getString(R.string.plus) + " " + mContext.getString(R.string.beds) + " " + mContext.getString(R.string.slash) + " ");
            }
        }


        if (mAlertsResultEntity.getType().isEmpty()) {
            mHolder.mAlertsPropertyTypeTxt.setText(mAlertsResultEntity.getType());
        } else {
            mHolder.mAlertsPropertyTypeTxt.setText(mAlertsResultEntity.getType() + "" + mContext.getString(R.string.colon));
        }
        mHolder.mAlertsCountTxt.setText(mAlertsResultEntity.getCount());

        mHolder.mAlertsMaxPricesTxt.setText(mAlertsResultEntity.getPrice_range_max().isEmpty() ? mContext.getString(R.string.no_max) : mContext.getString(R.string.dollar) + " " + mAlertsResultEntity.getPrice_range_max());

        mHolder.mAlertsMinPricesTxt.setText(mAlertsResultEntity.getPrice_range_min().isEmpty() ? mContext.getString(R.string.no_min) + mContext.getString(R.string.hyphen) : mContext.getString(R.string.dollar) + mAlertsResultEntity.getPrice_range_min() + mContext.getString(R.string.hyphen));

        mHolder.mParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEdits) {
                    mHolder.mTickImg.setImageResource(mAlertsResultEntity.isEditbool() ? R.drawable.tick_off : R.drawable.tick_on);
                    mAlertsResultList.get(mHolder.getAdapterPosition()).setEditbool(!mAlertsResultEntity.isEditbool());
                    if (mAlertsResultEntity.isEditbool()) {
                        AppConstants.ALERTS_SELECTED_IDS.add(mAlertsResultList.get(mHolder.getAdapterPosition()).getAlert_id());
                        AppConstants.ALERT_SELECTED_LIST.add(mAlertsResultEntity);
                    } else {
                        AppConstants.ALERTS_SELECTED_IDS.remove(mAlertsResultList.get(mHolder.getAdapterPosition()).getAlert_id());
                        AppConstants.ALERT_SELECTED_LIST.remove(mAlertsResultEntity);
                    }
                } else {
                    AppConstants.saved_search_json = mAlertsResultList.get(mHolder.getAdapterPosition()).getAlert_object();
                    AppConstants.saved_search_Latitude = mAlertsResultList.get(mHolder.getAdapterPosition()).getLatitude();
                    AppConstants.saved_search_Longitude = mAlertsResultList.get(mHolder.getAdapterPosition()).getLongitude();
                    AppConstants.saved_location_name = mAlertsResultList.get(mHolder.getAdapterPosition()).getLocation();
                    AppConstants.TYPE_OF_FILTER = AppConstants.RENT;
                    AppConstants.TYPE_OF_PROPERTY = AppConstants.RENT;
                    AppConstants.MAP_CURRENT_BACK_FRAGMENT = new AlertsFragment();
                    ((HomeScreen) mContext).addFragment(new MapFragment());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAlertsResultList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        @BindView(R.id.alert_name_txt)
        TextView mAlertsNameTxt;

        @BindView(R.id.alerts_keywords_txt)
        TextView mAlertsKeywordsTxt;

        @BindView(R.id.alerts_count_txt)
        TextView mAlertsCountTxt;

        @BindView(R.id.alerts_baths_txt)
        TextView mAlertsBathsTxt;

        @BindView(R.id.alerts_beds_txt)
        TextView mAlertsBedsTxt;

        @BindView(R.id.alerts_max_price_txt)
        TextView mAlertsMaxPricesTxt;

        @BindView(R.id.alerts_min_price_txt)
        TextView mAlertsMinPricesTxt;

        @BindView(R.id.alerts_property_type_txt)
        TextView mAlertsPropertyTypeTxt;

        @BindView(R.id.tick_lay)
        LinearLayout mTickLay;

        @BindView(R.id.selection_img)
        ImageView mTickImg;

        @BindView(R.id.alert_adpter_parent_lay)
        LinearLayout mParentLay;

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
