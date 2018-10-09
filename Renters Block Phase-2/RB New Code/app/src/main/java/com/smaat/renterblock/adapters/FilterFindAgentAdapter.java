package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AddressUtil;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.InterfaceLocalSearchEntityCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterFindAgentAdapter extends RecyclerView.Adapter<FilterFindAgentAdapter.Holder> {

    private Context mContext;
    private boolean isSavedLocationBool;
    private ArrayList<LocalSavedSearchEntity> mFilterList = new ArrayList<>();
    private InterfaceLocalSearchEntityCallback mCallback;

    public FilterFindAgentAdapter(Context context, ArrayList<LocalSavedSearchEntity> mList, boolean isSavedList, InterfaceLocalSearchEntityCallback callback) {
        isSavedLocationBool = isSavedList;
        mFilterList = mList;
        mContext = context;
        mCallback=callback;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_search_custom_adapter_place_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final LocalSavedSearchEntity mSavedSearchEntity = mFilterList.get(position);

        if (!isSavedLocationBool) {
            /*Assign visibility for Suggetsion place list*/
            holder.mSavedListLay.setVisibility(View.GONE);
            holder.mSearchLocationLay.setVisibility(View.VISIBLE);
             /*setting Value*/
            holder.mLocationTxt.setText(mSavedSearchEntity.getLocation());

        } else {
            /*Assign visibility for saved Location list*/
            holder.mSearchLocationLay.setVisibility(View.GONE);
            /*Only for first Item*/
            holder.mSavedListLay.setVisibility(mSavedSearchEntity.getFilter_object1().getFilter_name().isEmpty() ? View.GONE : View.VISIBLE);
            holder.mSavedLocationTxt.setText(mSavedSearchEntity.getFilter_object1().getFilter_name() + "\n" + mSavedSearchEntity.getFilter_object1().getLocation());
            holder.mFavImg.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.mSavedLocationTxt.setTextColor(position == 0 ? ContextCompat.getColor(mContext, R.color.app_blue) :
                    ContextCompat.getColor(mContext, R.color.black));

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.getLocalSearchEntity(mFilterList.get(holder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        @BindView(R.id.seacrh_location_lay)
        LinearLayout mSearchLocationLay;

        @BindView(R.id.saved_list_lay)
        LinearLayout mSavedListLay;

        @BindView(R.id.location_txt)
        TextView mLocationTxt;

        @BindView(R.id.saved_location_txt)
        TextView mSavedLocationTxt;

        @BindView(R.id.favourite_img)
        ImageView mFavImg;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
