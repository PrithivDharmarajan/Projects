package com.smaat.ipharma.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.vision.text.Text;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.fragments.PharmacyListFragment;
import com.smaat.ipharma.fragments.ReviewFragment;
import com.smaat.ipharma.fragments.ShopdetailFragment;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.smaat.ipharma.main.BaseActivity.mActivity;
import static com.smaat.ipharma.utils.AppConstants.PharmacyDetails;
import static com.smaat.ipharma.utils.AppConstants.SHOP_ID_VAL;
import static com.smaat.ipharma.utils.GlobalMethods.CalculationByDistancedouble;


public class MapListAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private ArrayList<MapPropertyEntity> mPharmacyDetails;
    public static Dialog mDialog;
    public static String mShopId, mUserID;
    public static int mSelectPos;
    public BaseFragment base_fgmt;
    public boolean is_fav = false;


    public MapListAdapter(Context context,boolean fav,
                          ArrayList<MapPropertyEntity> mPharmacyList, BaseFragment fgmt) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.mPharmacyDetails = mPharmacyList;
        this.base_fgmt = fgmt;
        this.is_fav = fav;
        mUserID = ((String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID));
    }

    public class Holder {
        ImageView pharmacy_image;
        TextView pharmacy_name;
        TextView reviews_count;
        TextView distance_txt;
        RatingBar ratinglayout;
        LinearLayout click_item;
        LinearLayout pharmacy_rat;
        TextView address_txt;
        //ImageView fav_added;



    }

    public View getView(final int position, View row, ViewGroup parent) {

        View convertView = row;
        Holder mholder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_pharmacy_list, parent, false);
            mholder = new Holder();
            mholder.pharmacy_image = (ImageView) convertView
                    .findViewById(R.id.pharmacy_image);
            mholder.pharmacy_name = (TextView) convertView
                    .findViewById(R.id.pharmacy_name);
            mholder.reviews_count = (TextView) convertView
                    .findViewById(R.id.review_count);
            mholder.distance_txt = (TextView) convertView
                    .findViewById(R.id.distance_km);
            mholder.ratinglayout = (RatingBar) convertView
                    .findViewById(R.id.review_rating_bar);
            mholder.click_item = (LinearLayout) convertView
                    .findViewById(R.id.click_item);
            mholder.address_txt = (TextView) convertView
                    .findViewById(R.id.address_txt);

            mholder.pharmacy_rat = (LinearLayout) convertView
                    .findViewById(R.id.pharmacy_rat);
            //mholder.fav_added = (ImageView)convertView.findViewById(R.id.fav_added);


            convertView.setTag(mholder);
        }
        else
        {
            mholder=(Holder)convertView.getTag();
        }

        final MapPropertyEntity tempData = mPharmacyDetails.get(position);
        String url = "";
        if (tempData.getShopIcon() != null && !tempData.getShopIcon().trim().isEmpty()) {
            url = AppConstants.ADMIN_BASE_URL + tempData.getShopIcon();
        } else {
            url = AppConstants.ADMIN_BASE_URL + tempData.getProfilePic();
        }

        Log.e("urlurlurl","urlurlurl"+url);
        mholder.pharmacy_name.setText(GlobalMethods.capitalizeString(tempData.getShopName()));


        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.popup_logo)
                .into(mholder.pharmacy_image);

        Log.e("tempDatatempData","tempDatatempDatatempData"+tempData
                .getAvgRating());
        mholder.ratinglayout.setRating(Float.valueOf(tempData.getAvgRating()));

        if(Integer.parseInt(tempData.getTotalReviews())==0||Integer.parseInt(tempData.getTotalReviews())==1)
        {
            mholder.reviews_count.setText(tempData
                    .getTotalReviews()+" "+"Review");
        }else{
            mholder.reviews_count.setText(tempData
                    .getTotalReviews()+" "+context.getString(R.string.reviews));
        }


        String communication_address = tempData
                .getAddress()
                + ", "
                + tempData.getArea();

        mholder.address_txt.setText(GlobalMethods.capitalizeString(communication_address));

        /*if (tempData.getDistance().contains(".")) {
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            mholder.distance_txt.setText(CalculationByDistancedouble(tempData.getLatitude(),tempData.getLongitude())+ " " + context.getString(R.string.km_away));
        } else {
            mholder.distance_txt.setText(tempData
                    .getDistance() + " " + context.getString(R.string.km_away));
        }*/

        mholder.distance_txt.setText(CalculationByDistancedouble(tempData.getLatitude(),tempData.getLongitude())+ " " + context.getString(R.string.km_away));


        /*if(tempData.getIsFav()!=null)
        {
            mholder.fav_added.setTag(Integer.parseInt(tempData.getIsFav()));
        }

        if(tempData.getIsFav()!=null)
        {

            if(tempData.getIsFav().equalsIgnoreCase("1"))
            {
                mholder.fav_added.setImageResource(R.drawable.thumps_up);
            }else{
                mholder.fav_added.setImageResource(R.drawable.thumps_down);
            }
        }else{
            mholder.fav_added.setImageResource(R.drawable.heart_red);
        }*/

        //mholder.reviews_count.setTag(position);
        mholder.reviews_count.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //int pos = (Integer) v.getTag();
                if(!GlobalMethods.movetologinscreen(base_fgmt.getActivity()))
                {
                    AppConstants.PharmacyDetails = mPharmacyDetails.get(position);
                    ((HomeScreen) context).pushFragment(new ReviewFragment());
                }
            }
        });

        //mholder.fav_added.setTag(position);
        /*mholder.fav_added.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               //int pos = Integer.parseInt(v.getTag().toString());
               final MapPropertyEntity tempData = mPharmacyDetails.get(position);
               ImageView img = (ImageView)v;
                if(tempData.getIsFav()!=null)
                {
                    if(tempData.getIsFav().equalsIgnoreCase("1"))
                    {
                        img.setImageResource(R.drawable.thumps_down);
                        tempData.setIsFav("2");
                        APIRequestHandler.getInstance().addFavourite(tempData.getPharmacyID(), mUserID, "2",
                                base_fgmt);
                        DialogManager.showToast(context,context.getString(R.string.rem_fav));
                    }else{
                        img.setImageResource(R.drawable.thumps_up);
                        tempData.setIsFav("1");
                        APIRequestHandler.getInstance().addFavourite(tempData.getPharmacyID(), mUserID, "1",
                                base_fgmt);
                        DialogManager.showToast(context,context.getString(R.string.add_fav));
                    }
                }else{
                    APIRequestHandler.getInstance().addFavourite(tempData.getPharmacyID(), mUserID, "2",
                            base_fgmt);
                    DialogManager.showToast(context,context.getString(R.string.rem_fav));
                    AppConstants.CLICKED_POSITION = position;
                }

               notifyDataSetChanged();
            }
        });*/
        //mholder.pharmacy_rat.setTag(position);
        mholder.pharmacy_rat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //int pos = (Integer) v.getTag();
                if(!GlobalMethods.movetologinscreen(base_fgmt.getActivity()))
                {
                    AppConstants.PharmacyDetails = mPharmacyDetails.get(position);
                    ((HomeScreen) context).pushFragment(new ReviewFragment());
                }
            }
        });
        //mholder.click_item.setTag(position);
        mholder.click_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstants.enableOneTouch)
                {
                    showOnetouch(context,tempData.getPharmacyID(), tempData.getShopName(),position);
                }else{
                    AppConstants.PharmacyDetails = mPharmacyDetails.get(position);
                    ((HomeScreen) context).pushFragment(new ShopdetailFragment());
                }

            }
        });
        return convertView;
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
        ImageView rate = (ImageView) mDialog.findViewById(R.id.rate_it);
        ImageView cancel = (ImageView) mDialog.findViewById(R.id.cancel);
        final TextView mRating_avg_txt = (TextView) mDialog
                .findViewById(R.id.rating_avg_txt);
        final RatingBar mFav_ratingbar = (RatingBar) mDialog
                .findViewById(R.id.fav_ratingbar);
        mFav_ratingbar.setRating(5);

        final Button mRating_icons = (Button) mDialog
                .findViewById(R.id.rating_icons);

        mFav_ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

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

        APIRequestHandler.getInstance().rateShop(mShopId, mUserID, ""+rating_value,
                base_fgmt);
    }

    public void removefavourites(int pos)
    {
        if(mPharmacyDetails.size()>0)
        {
            mPharmacyDetails.remove(pos);
            notifyDataSetChanged();
        }
    }

    public int getFavsize()
    {
        int size = 0;
        size = mPharmacyDetails.size();
        return size;
    }

    private void showOnetouch(final Context mContext,final String mShopId,final String mShopName,final int position) {
        mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        TextView title = (TextView) mDialog.findViewById(R.id.title_text);
        title.setText(mContext.getString(R.string.enable_one_touch)+" "+mShopName+" "+mContext.getString(R.string.enable_one_touch2));

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                APIRequestHandler.getInstance().OnetouchOrder(mUserID, mShopId,base_fgmt);
            }
        });
        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                AppConstants.enableOneTouch = false;
                AppConstants.PharmacyDetails = mPharmacyDetails.get(position);
                ((HomeScreen) context).pushFragment(new ShopdetailFragment());

            }
        });

        mDialog.show();
    }




}
