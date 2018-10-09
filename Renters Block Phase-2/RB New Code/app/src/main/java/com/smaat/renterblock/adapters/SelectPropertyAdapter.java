package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.utils.AppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Smaat on 9/7/2017.
 */

public class SelectPropertyAdapter extends RecyclerView.Adapter<SelectPropertyAdapter.ItemViewHolder> {

    private Context mContext;
    private List<String> mPropertyTypeList;

    private int mPosInt = -1;

    public SelectPropertyAdapter(Context context, List<String> mPropertyTypeList) {
        mContext = context;
        this.mPropertyTypeList = mPropertyTypeList;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_property_type, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.mPropTypeTxt.setText(mPropertyTypeList.get(position));
        holder.mTickImg.setImageResource(mPosInt != -1 ? R.drawable.tick_on : R.drawable.tick_off);
        holder.mTickImg.setImageResource(mPropertyTypeList.get(position).toString().equalsIgnoreCase(AppConstants.SELECTED_PROPERTY_TYPE) ? R.drawable.tick_on : R.drawable.tick_off);
        holder.mTypesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosInt = position;
                AppConstants.SELECTED_PROPERTY_TYPE = mPropertyTypeList
                        .get(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mPropertyTypeList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.type_txt)
        TextView mPropTypeTxt;
        @BindView(R.id.type_img)
        ImageView mTickImg;
        @BindView(R.id.types_lay)
        RelativeLayout mTypesLay;


        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
