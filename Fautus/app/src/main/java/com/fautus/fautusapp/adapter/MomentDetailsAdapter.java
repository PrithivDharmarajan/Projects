package com.fautus.fautusapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.fragment.ActualImageFragment;
import com.fautus.fautusapp.fragment.MomentDetailsFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 21-Apr-17.
 */

public class MomentDetailsAdapter extends RecyclerView.Adapter<MomentDetailsAdapter.Holder> {
    private Context mContext;
    private ArrayList<PhotoEntity> mPhotoArrList;
    private int photoSelectedInt = 0;
    private MomentDetailsFragment mCurrentFrag;
    private boolean isStripeOn = false;

    public MomentDetailsAdapter(Context context, int unPurchasedPhotoCountInt, ArrayList<PhotoEntity> photoList, MomentDetailsFragment currentFrag) {
        this.mContext = context;
        this.mPhotoArrList = photoList;
        this.mCurrentFrag = currentFrag;
        AppConstants.MOMENT_SELECTED_PHOTO_FILE = new ArrayList<>();
        photoSelectedInt = unPurchasedPhotoCountInt;
        isStripeOn = PreferenceUtil.getBoolPreferenceValue(context, AppConstants.SETTINGS_STRIP_ON);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_moment_buy_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.mMomentWithSkyImg.setVisibility(isStripeOn ? View.VISIBLE : View.GONE);
        holder.mMomentWithOutSkyImg.setVisibility(isStripeOn ? View.GONE : View.VISIBLE);

        if (mPhotoArrList.get(position) != null) {
            mPhotoArrList.get(position).getPhoto().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    try {
                        Glide.with(mContext)
                                .load(data)
                                .into(isStripeOn ? holder.mMomentWithSkyImg : holder.mMomentWithOutSkyImg);
                    } catch (Exception ex) {
                        (isStripeOn ? holder.mMomentWithSkyImg : holder.mMomentWithOutSkyImg).setImageResource(R.drawable.app_transparent_img);
                        Log.e(mContext.getClass().getSimpleName(), ex.getMessage());
                    }
                }
            });
        } else {
            (isStripeOn ? holder.mMomentWithSkyImg : holder.mMomentWithOutSkyImg).setImageResource(R.drawable.app_transparent_img);
        }

        holder.mMomentWithSkyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.MOMENT_PHOTO_FILE = mPhotoArrList.get(holder.getAdapterPosition());
                ((HomeScreen) mContext).addFragment(new ActualImageFragment());
            }
        });
        holder.mMomentWithOutSkyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.MOMENT_PHOTO_FILE = mPhotoArrList.get(holder.getAdapterPosition());
                ((HomeScreen) mContext).addFragment(new ActualImageFragment());
            }
        });

        /*AppConstants.MOMENT_BUY is 1 here already item bought */
        if (PreferenceUtil.getBoolPreferenceValue(mContext, AppConstants.USER_IS_CONSUMER) && mPhotoArrList.get(position).getPhoto_purchased().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            holder.mSelectedImg.setTag(AppConstants.SUCCESS_CODE);
            mCurrentFrag.setConsumerDetails(photoSelectedInt);
            AppConstants.MOMENT_SELECTED_PHOTO_FILE.add(mPhotoArrList.get(position));
            holder.mSelectedImg.setVisibility(View.VISIBLE);
        } else {
            holder.mSelectedImg.setVisibility(View.GONE);
        }

        holder.mMomentHolderImg.setVisibility(mPhotoArrList.get(position).getPhoto_purchased().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.GONE : View.VISIBLE);

        holder.mSelectedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View selectedImg) {
                String imgTagStatusStr = (String) selectedImg.getTag();
                photoSelectedInt = imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? (photoSelectedInt - 1) : (photoSelectedInt + 1);
                mCurrentFrag.setConsumerDetails(photoSelectedInt);
                selectedImg.setAlpha(imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? 0.2f : 1.0f);


                boolean isProceedBool = false;
                for (int i = 0; i < AppConstants.MOMENT_SELECTED_PHOTO_FILE.size(); i++) {
                    if (AppConstants.MOMENT_SELECTED_PHOTO_FILE.get(i).equals(mPhotoArrList.get(holder.getAdapterPosition()))) {
                        isProceedBool = true;
                        if (imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            AppConstants.MOMENT_SELECTED_PHOTO_FILE.remove(i);
                        } else {
                            AppConstants.MOMENT_SELECTED_PHOTO_FILE.add(mPhotoArrList.get(holder.getAdapterPosition()));
                        }
                        break;
                    }
                }
                if (!isProceedBool) {
                    AppConstants.MOMENT_SELECTED_PHOTO_FILE.add(mPhotoArrList.get(holder.getAdapterPosition()));
                }
                selectedImg.setTag(imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhotoArrList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.moment_with_sky_img)
        ImageView mMomentWithSkyImg;

        @BindView(R.id.moment_with_out_sky_img)
        ImageView mMomentWithOutSkyImg;

        @BindView(R.id.selected_img)
        ImageView mSelectedImg;

        @BindView(R.id.moment_holder_img)
        ImageView mMomentHolderImg;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
