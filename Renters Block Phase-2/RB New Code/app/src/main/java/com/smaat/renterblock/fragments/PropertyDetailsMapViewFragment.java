package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

public class PropertyDetailsMapViewFragment extends BaseFragment implements OnMapReadyCallback {

    private double mLat = 0.0, mLong = 0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mPropertyDetailView = inflater.inflate(R.layout.frag_property_details_map_view, container, false);


        initView();
        return mPropertyDetailView;
    }

    private void initView() {

        mLat = Double.parseDouble(PreferenceUtil.getStringValue(getActivity(), AppConstants.PROPERTY_DETAILS_LATITUDE));
        mLong = Double.parseDouble(PreferenceUtil.getStringValue(getActivity(), AppConstants.PROPERTY_DETAILS_LONGITUDE));

          /*Map Showing*/
        SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        fragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in CLRI and move the camera
        LatLng rb = new LatLng(mLat, mLong);
        googleMap.addMarker(new MarkerOptions().position(rb));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rb, 13));
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.back_arrow, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.map_view), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

        }
    }

}
