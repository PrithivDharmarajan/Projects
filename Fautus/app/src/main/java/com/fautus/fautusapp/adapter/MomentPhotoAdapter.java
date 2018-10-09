package com.fautus.fautusapp.adapter;

import android.content.Context;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 21-Apr-17.
 */

public class MomentPhotoAdapter extends RecyclerView.Adapter<MomentPhotoAdapter.Holder> {
    private Context mContext;
    private ArrayList<PhotoEntity> mPhotoArrList;

    public MomentPhotoAdapter(Context context, ArrayList<PhotoEntity> photoList) {
        this.mContext = context;
        this.mPhotoArrList = photoList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_moment_image_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        if (mPhotoArrList.get(position) != null) {
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
            holder.mMomentImg.setImageResource(R.drawable.app_transparent_img);
        }

    }

    @Override
    public int getItemCount() {
        return mPhotoArrList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.moment_img)
        ImageView mMomentImg;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
