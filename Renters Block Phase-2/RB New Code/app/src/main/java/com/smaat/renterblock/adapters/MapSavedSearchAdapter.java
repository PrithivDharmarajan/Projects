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
import com.smaat.renterblock.entity.SavedSearchEntity;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapSavedSearchAdapter extends RecyclerView.Adapter<MapSavedSearchAdapter.Holder> {

    private Context mContext;
    private boolean isSavedLocationBool;
    private ArrayList<LocalSavedSearchEntity> mLocalSearchList;
    SearchParamsInterface mSearchParamsInterface;
    MapFragment mapFragment;

    public MapSavedSearchAdapter(ArrayList<LocalSavedSearchEntity> mList, Context context, boolean isSavedLocation,
                                 MapFragment mapFragment) {
        mLocalSearchList = mList;
        mContext = context;
        isSavedLocationBool = isSavedLocation;
        mSearchParamsInterface = mapFragment;
        this.mapFragment = mapFragment;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.save_search_custom_adapter_place_list, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final LocalSavedSearchEntity mEntity = mLocalSearchList.get(position);

        if (!isSavedLocationBool) {
            /*Assign visibility for Suggetsion place list*/
            holder.mSavedListLay.setVisibility(View.GONE);
            holder.mSearchLocationLay.setVisibility(View.VISIBLE);
             /*setting Value*/
            holder.mLocationTxt.setText(mEntity.getLocation());

        } else {
            /*Assign visibility for saved Location list*/
            holder.mSearchLocationLay.setVisibility(View.GONE);
            /*Only for first Item*/
            holder.mSavedListLay.setVisibility(mEntity.getFilter_object1().getFilter_name().isEmpty() ? View.GONE : View.VISIBLE);
            holder.mSavedLocationTxt.setText(mEntity.getFilter_object1().getFilter_name() + "\n" + mEntity.getFilter_object1().getLocation());
            holder.mFavImg.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.mSavedLocationTxt.setTextColor(position == 0 ? ContextCompat.getColor(mContext, R.color.app_blue) :
                    ContextCompat.getColor(mContext, R.color.black));

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSavedLocationBool && mEntity.getPlaceID() != null) {
                    String url = AppConstants.PLACE_URL_LAT_LANG + mEntity.getPlaceID() + AppConstants.MAP_API_KEY;
                    APIRequestHandler.getInstance().getLatLangApi(mapFragment, url);
                } else {
                    if (mEntity.getFilter_object1() != null && mEntity.getFilter_object1().getLatitude() != null &&
                            mEntity.getFilter_object1().getLongitude() != null) {
                        mSearchParamsInterface.searchProperty(mEntity.getFilter_object1().getLatitude(), mEntity.getFilter_object1().getLongitude()
                                , mEntity.getFilter_object1().getFilter_name());

                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLocalSearchList.size();

    }

    class Holder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.parent_lay)
        LinearLayout mParentLay;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SearchParamsInterface {
        void searchProperty(String lat, String lang, String locName);
    }
}
