package com.smaat.renterblock.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.renterblock.R;

import java.text.NumberFormat;

public class PropertyMapView extends BaseActivity implements OnMapReadyCallback {

	Bundle mBundle;
	private double pLat = 0.0, pLong = 0.0;
	String Pproperty;
	private GoogleMap mGoogleMap;
	private String ppropertyid, propertyamount;
	private String isfavourite;
	private Marker marker;
	private View marker_view_shown;
	private TextView numTxt;
	private View marker_view, selected_marker;
	private ImageView mMarkerImg, mfavicon;
	private String am = "";
	private RelativeLayout backpress;
	private String PropertyTyp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_map_property_view);

		mBundle = getIntent().getExtras();
		if (mBundle != null) {

			pLat = mBundle.getDouble("PropertyLatitude");
			pLong = mBundle.getDouble("PropertyLongtitude");
			Pproperty = mBundle.getString("Propertymap");
			ppropertyid = mBundle.getString("PropertyId");
			propertyamount = mBundle.getString("PropertyAmount");
			isfavourite = mBundle.getString("isFavourite");
			PropertyTyp = mBundle.getString("PropertyTyp");

			initilizeMap();

		}
		backpress = (RelativeLayout) findViewById(R.id.backheader);
		backpress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			}
		});

		FragmentManager fragmentManager = getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
		fragment.getMapAsync(PropertyMapView.this);

	}

	private void initilizeMap() {
		if (mGoogleMap != null) {
			/*mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();*/
			if (mGoogleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {

				mGoogleMap.setMyLocationEnabled(false);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
				mGoogleMap.getUiSettings().setCompassEnabled(true);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
				mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

				marker_view_shown = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.map_list_item, null);

				numTxt = (TextView) marker_view_shown
						.findViewById(R.id.num_txt);
				mMarkerImg = (ImageView) marker_view_shown
						.findViewById(R.id.marker);
				mfavicon = (ImageView) marker_view_shown
						.findViewById(R.id.favicon);

				if (PropertyTyp.equals("exclusive")) {
					mfavicon.setVisibility(View.VISIBLE);
					mfavicon.setImageResource(R.drawable.exclusives_icon);
				} else if (PropertyTyp.equals("openhouse")) {
					mfavicon.setVisibility(View.VISIBLE);
					mfavicon.setImageResource(R.drawable.open_houses_icon);
				} else if (PropertyTyp.equals("favourite")) {
					mfavicon.setVisibility(View.VISIBLE);
					mfavicon.setImageResource(R.drawable.favourite_disable);
				} else {
					mfavicon.setVisibility(View.GONE);
				}

				viewamount();

				LatLng latLng = new LatLng(pLat, pLong);
				marker = mGoogleMap
						.addMarker(new MarkerOptions().position(latLng).icon(
								BitmapDescriptorFactory
										.fromBitmap(createDrawableFromView(
												PropertyMapView.this,
												marker_view_shown))));

				LatLng currentPos = new LatLng(pLat, pLong);
				CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
						currentPos, 11.0f);
				mGoogleMap.animateCamera(yourLocation);

			}

		}
	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams());
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	private void viewamount() {

		if (isfavourite.equals("true")) {
			mfavicon.setVisibility(View.VISIBLE);
			System.out.println(NumberFormat.getIntegerInstance().format(
					Integer.parseInt(propertyamount)));
			String amount = NumberFormat.getIntegerInstance().format(
					Integer.parseInt(propertyamount));
			try {

				am = amount.substring(0, amount.lastIndexOf(","));
				numTxt.setText("$" + am + " k");
			} catch (Exception e) {
				e.printStackTrace();
				numTxt.setText("$" + amount + " k");
			}

		} else {
			mfavicon.setVisibility(View.INVISIBLE);
			System.out.println(NumberFormat.getIntegerInstance().format(
					Integer.parseInt(propertyamount)));
			String amount = NumberFormat.getIntegerInstance().format(
					Integer.parseInt(propertyamount));
			try {

				am = amount.substring(0, amount.lastIndexOf(","));
				numTxt.setText("$" + am + " k");
			} catch (Exception e) {
				e.printStackTrace();
				numTxt.setText("$" + amount + " k");
			}

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

    @Override
    public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		initilizeMap();
    }
}
