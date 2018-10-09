package com.fautus.fautusapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 21-Apr-17.
 */

public class PhotoMomentUploadAdapter extends RecyclerView.Adapter<PhotoMomentUploadAdapter.Holder> {
    private Context mContext;
    private ArrayList<PhotoEntity> mPhotoArrList;

    public PhotoMomentUploadAdapter(Context context, ArrayList<PhotoEntity> photoList) {
        this.mContext = context;
        this.mPhotoArrList = photoList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_moment_buy_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {


        if (mPhotoArrList.get(position) != null) {

            if (mPhotoArrList.get(position).getPhoto_path() == null || mPhotoArrList.get(position).getPhoto_path().isEmpty() || mPhotoArrList.get(position).getPhoto_path().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                mPhotoArrList.get(position).getPhoto().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        try {
                            Glide.with(mContext)
                                    .load(data)
                                    .into(holder.mMomentImg);
                        } catch (Exception ex) {
                            holder.mMomentImg.setImageResource(R.drawable.app_transparent_img);
                            Log.e(mContext.getClass().getSimpleName(), ex.getMessage());
                        }

                    }
                });
            } else {
                try {
                    Glide.with(mContext)
                            .load(Uri.fromFile(new File(mPhotoArrList.get(position).getPhoto_path())))
                            .into(holder.mMomentImg);

                } catch (Exception ex) {
                    holder.mMomentImg.setImageResource(R.drawable.app_transparent_img);
                    Log.e(mContext.getClass().getSimpleName(), ex.getMessage());
                }
            }
        } else {
            holder.mMomentImg.setImageResource(R.drawable.app_transparent_img);
        }


        holder.mSelectedImg.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return mPhotoArrList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.moment_with_sky_img)
        ImageView mMomentImg;

        @BindView(R.id.selected_img)
        ImageView mSelectedImg;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
