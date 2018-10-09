package com.smaat.ipharma.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.fragment;
import static com.smaat.ipharma.fragments.OrderDetailFragment.clickable;
import static com.smaat.ipharma.utils.AppConstants.Choosed_Image;
import static com.smaat.ipharma.utils.AppConstants.FROMHISTORY;

/**
 * Created by sys on 11/11/2016.
 */

public class ViewShopMapFragment extends BaseActivity implements OnMapReadyCallback {

    private View mRootView;
    private GoogleMap m_Map;


    @BindView(R.id.mapview)
    LinearLayout mapview;

    @BindView(R.id.fill_imgview)
    ImageView fill_imgview;

    @BindView(R.id.close_button)
    ImageView close_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_shop_mapview);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        if(getIntent().getBooleanExtra("ShowImage",false))
        {
            fill_imgview.setVisibility(View.VISIBLE);
            mapview.setVisibility(View.GONE);
            if(FROMHISTORY)
            {
                Glide.with(this).load(AppConstants.BASE_URL2 + "/"
                        +
                        Choosed_Image)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(fill_imgview);
            }else{
                fill_imgview.setImageBitmap(HomeScreen.mSelectedImgBitmap);
            }


        }else{
            loadMapFragment();
            fill_imgview.setVisibility(View.GONE);
            mapview.setVisibility(View.VISIBLE);
        }


        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                clickable = false;
                overridePendingTransition(R.anim.slide_out_to_bottom, R.anim.slide_out_to_bottom);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            clickable = false;
            overridePendingTransition(R.anim.slide_out_to_bottom, R.anim.slide_out_to_bottom);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.ui_shop_mapview, container, false);
        ButterKnife.bind(this, mRootView);
        loadMapFragment();
        return mRootView;
    }*/

   /* public static ViewShopMapFragment newInstance(FragmentActivity activity) {
        ViewShopMapFragment frag = new ViewShopMapFragment();

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final View view = getActivity().getLayoutInflater().inflate(R.layout.ui_shop_mapview, null);

        dialog.getWindow().setContentView(view);
        loadMapFragment();

        return dialog;
    }*/


    private void loadMapFragment() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(ViewShopMapFragment.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_Map = googleMap;
        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(AppConstants.SHOP_LATITUDE!=""?AppConstants.SHOP_LATITUDE:"0.0"),
                Double.parseDouble(AppConstants.SHOP_LONGITUDE!=""?AppConstants.SHOP_LONGITUDE:"0.0")));
        //markerOption.icon(BitmapDescriptorFactory.fromBitmap(ResizeMapIcons("map_pointer", 45, 70)));
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer));
        m_Map.addMarker(markerOption);
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(12);
        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(AppConstants.SHOP_LATITUDE!=""?AppConstants.SHOP_LATITUDE:"0.0"),
                Double.parseDouble(AppConstants.SHOP_LONGITUDE!=""?AppConstants.SHOP_LONGITUDE:"0.0")));
        m_Map.moveCamera(center);
        m_Map.animateCamera(zoom);
    }


    @Override
    public void onResume() {
        super.onResume();
        //((HomeScreen)ViewShopMapFragment.this).setToolbarTitle(AppConstants.SHOP_NAME,getString(R.string.home));

    }

    /*public Bitmap ResizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable",  getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }*/

}
