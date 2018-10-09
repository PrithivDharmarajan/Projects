package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.Holder> {
    private Context mContext;
    private ArrayList<LocalSavedSearchEntity> mPlaceList;
    private InterfaceEdtWithBtnCallback mCallback;

    public PlaceListAdapter(Context context, ArrayList<LocalSavedSearchEntity> placeList, InterfaceEdtWithBtnCallback callback) {
        mContext = context;
        mPlaceList = placeList;
        mCallback = callback;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_placelist, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final LocalSavedSearchEntity entity = mPlaceList.get(holder.getAdapterPosition());
        holder.mLocationTxt.setText(entity.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFirstEdtBtnClick(entity.getLocation());

            }
        });


    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.location_txt)
        TextView mLocationTxt;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
