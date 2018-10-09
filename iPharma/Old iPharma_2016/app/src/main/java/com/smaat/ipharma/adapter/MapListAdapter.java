package com.smaat.ipharma.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.fragment.OrderNowFragment;
import com.smaat.ipharma.fragment.ReviewsFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.RoundEdgeImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapListAdapter extends BaseAdapter implements OnClickListener,
        DialogMangerCallback {
    private Context context;
    private ArrayList<MapPropertyEntity> mPharmacyDetails;
    public static Dialog mDialog;
    public static String mShopId, mUserID;
    public static int mSelectPos;


    public MapListAdapter(Context context,
                          ArrayList<MapPropertyEntity> mPharmacyList) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.mPharmacyDetails = mPharmacyList;
        mUserID = ((String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID));
    }

    public class Holder {
        TextView pharmacy_name, pharmacy_address, pharmacy_distance,
                pharmacy_review, min_order, delivery_time;
        RatingBar review_rating;
        ImageView pharmacy_premium;
        RoundEdgeImageView pharmacy_image_mask;
        RelativeLayout mViewLay;
        LinearLayout pharmacy_rat, pharmacy_revs;

    }

    public View getView(int position, View row, ViewGroup parent) {

        View convertView = row;
        Holder mholder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pharmacy, parent, false);
            mholder = new Holder();

            mholder.mViewLay = (RelativeLayout) convertView
                    .findViewById(R.id.view_lay);
            mholder.pharmacy_rat = (LinearLayout) convertView
                    .findViewById(R.id.pharmacy_rat);
            mholder.pharmacy_revs = (LinearLayout) convertView
                    .findViewById(R.id.pharmacy_revs);


            mholder.pharmacy_name = (TextView) convertView
                    .findViewById(R.id.pharmacy_name);
            mholder.pharmacy_name.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.pharmacy_address = (TextView) convertView
                    .findViewById(R.id.pharmacy_address);
            mholder.pharmacy_address.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.pharmacy_distance = (TextView) convertView
                    .findViewById(R.id.pharmacy_distance);
            mholder.pharmacy_distance.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.pharmacy_review = (TextView) convertView
                    .findViewById(R.id.pharmacy_review_count);
            mholder.pharmacy_review.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.min_order = (TextView) convertView.findViewById(R.id.min_order);
            mholder.min_order.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.delivery_time = (TextView) convertView
                    .findViewById(R.id.delivery_time);
            mholder.delivery_time.setTypeface(HomeScreen.mHelveticaNormal);
            mholder.review_rating = (RatingBar) convertView
                    .findViewById(R.id.pharmacy_rating);

            mholder.pharmacy_premium = (ImageView) convertView
                    .findViewById(R.id.pharmacy_fav);
            mholder.pharmacy_image_mask = (RoundEdgeImageView) convertView
                    .findViewById(R.id.pharmacy_image_mask);

            convertView.setTag(mholder);
        }
        else
        {
            mholder=(Holder)convertView.getTag();
        }

        MapPropertyEntity tempData = mPharmacyDetails.get(position);

            if (tempData.getIsPremium() != null
                    && tempData.getIsPremium()
                    .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mholder.pharmacy_premium.setVisibility(View.VISIBLE);
            } else {
                mholder.pharmacy_premium.setVisibility(View.GONE);
            }
            mholder.pharmacy_name.setText(tempData
                    .getShopName());
            mholder.pharmacy_address.setText(tempData
                    .getAddress());

            if (tempData.getDistance().contains(".")) {
                DecimalFormat distance_roundoff = new DecimalFormat("#.##");
                mholder.pharmacy_distance.setText(Double.valueOf(distance_roundoff
                        .format(Double.valueOf(tempData
                                .getDistance())))
                        + " " + context.getString(R.string.km_away));
            } else {
                mholder.pharmacy_distance.setText(tempData
                        .getDistance() + " " + context.getString(R.string.km_away));
            }

            String url = "";
            if (tempData.getShopIcon() != null && !tempData.getShopIcon().trim().isEmpty()) {
                url = AppConstants.ADMIN_BASE_URL + tempData.getShopIcon();
            } else {
                url = AppConstants.PHARMACY_IMAGE_BASE_URL + tempData.getProfilePic();
            }
            Glide.with(context).load(url)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mholder.pharmacy_image_mask);

            mholder.min_order.setText(context.getString(R.string.min_order_rs)
                    + " " + tempData.getMinimumOrderValue());
            mholder.delivery_time.setText(context.getString(R.string.deliver_time)
                    + " " + tempData.getDeliveryTime() + " "
                    + context.getString(R.string.min));
            mholder.review_rating.setRating(Float.valueOf(mPharmacyDetails.get(
                    position).getAvgRating()));
            mholder.pharmacy_review.setText(tempData
                    .getTotalReviews());

        mholder.pharmacy_rat.setTag(position);
            mholder.pharmacy_rat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    mSelectPos = pos;
                    mShopId = mPharmacyDetails.get(pos).getPharmacyID();
                    showRateDialog();
                }
            });
        mholder.pharmacy_revs.setTag(position);
            mholder.pharmacy_revs.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    mShopId = mPharmacyDetails.get(pos).getPharmacyID();
                    AppConstants.Pharmacy_id = mShopId;
                    AppConstants.from_map_list_review = AppConstants.SUCCESS_CODE;
                    ((HomeScreen) context).replaceFragment(new ReviewsFragment(),
                            true);
                    HomeScreen.mHeaderLeft
                            .setBackgroundResource(R.drawable.back_butto);
                    HomeScreen.mBottombar.setVisibility(View.GONE);
                    HomeScreen.mFooterText.setText(R.string.write_review);
                    HomeScreen.mHeaderRight.setVisibility(View.INVISIBLE);
                }
            });

        mholder.mViewLay.setTag(position);
            mholder.mViewLay.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("static-access")
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    ((HomeScreen) context).mFragment = new OrderNowFragment();
                    Bundle bundle = new Bundle();

                    AppConstants.FROM_MAPFAVORITE_SCREEN = AppConstants.MAP_SCREEN;
                    bundle.putSerializable("pharmcay_details",
                            mPharmacyDetails.get(pos));
                    ((HomeScreen) context).mFragment.setArguments(bundle);
                    ((HomeScreen) context).replaceFragment(
                            ((HomeScreen) context).mFragment, true);
                    HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                    HomeScreen.mHeaderLeft
                            .setBackgroundResource(R.drawable.back_butto);
                }
            });


            return convertView;
        }

    public void showRateDialog() {

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_rate);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        mDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Button rate = (Button) mDialog.findViewById(R.id.rate_it);
        Button cancel = (Button) mDialog.findViewById(R.id.cancel);
        rate.setTypeface(HomeScreen.mHelveticaBold);
        cancel.setTypeface(HomeScreen.mHelveticaBold);
        TextView mRateText = (TextView) mDialog.findViewById(R.id.rate_text);
        final TextView mRating_avg_txt = (TextView) mDialog
                .findViewById(R.id.rating_avg_txt);
        mRating_avg_txt.setTypeface(HomeScreen.mHelveticaBold);
        mRateText.setTypeface(HomeScreen.mHelveticaNormal);
        final RatingBar mFav_ratingbar = (RatingBar) mDialog
                .findViewById(R.id.fav_ratingbar);
        final Button mRating_icons = (Button) mDialog
                .findViewById(R.id.rating_icons);

        mFav_ratingbar
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {
                        switch ((int) mFav_ratingbar.getRating()) {
                            case 1:
                                mRating_avg_txt.setText(context
                                        .getString(R.string.poor));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.poor);
                                break;
                            case 2:
                                mRating_avg_txt.setText(context
                                        .getString(R.string.bad));
                                mRating_icons.setBackgroundResource(R.drawable.bad);
                                break;
                            case 3:
                                mRating_avg_txt.setText(context
                                        .getString(R.string.average));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.average);
                                break;
                            case 4:
                                mRating_avg_txt.setText(context
                                        .getString(R.string.good));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.good);
                                break;
                            case 5:
                                mRating_avg_txt.setText(context
                                        .getString(R.string.excellent));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.excellent);
                                break;
                        }
                    }
                });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        });
        rate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                float rating_value = mFav_ratingbar.getRating();
                callPharmacyRatingService(rating_value);
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void callPharmacyRatingService(float rating_value) {


        DialogManager.showProgress(context);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.Base_Url).build();
        APICommonInterface interfaces = restAdapter
                .create(APICommonInterface.class);

        String rating = String.valueOf(rating_value);

        interfaces.shopRating(mShopId, mUserID, rating,
                new Callback<RateResponseEntity>() {

                    public void failure(RetrofitError arg0) {

                        DialogManager.hideProgress(context);
                        DialogManager.showCustomAlertDialog(context,
                                MapListAdapter.this,
                                context.getString(R.string.no_network));
                    }

                    public void success(RateResponseEntity mRatingResponse,
                                        Response obj) {

                        if (mRatingResponse.getStatus().equalsIgnoreCase(
                                "success")) {

                            DialogManager.hideProgress(context);
                            DialogManager.showCustomAlertDialog(context,
                                    MapListAdapter.this,
                                    context.getString(R.string.rating_added));
                            mPharmacyDetails.get(mSelectPos).setAvgRating(
                                    mRatingResponse.getResult());
                            notifyDataSetChanged();
                        }
                    }

                });
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mPharmacyDetails.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOkclick() {
        // TODO Auto-generated method stub

    }

}
