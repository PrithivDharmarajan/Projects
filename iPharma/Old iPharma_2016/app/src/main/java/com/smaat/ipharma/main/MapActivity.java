package com.smaat.ipharma.main;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.ipharma.R;
import com.smaat.ipharma.util.AppConstants;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

	GoogleMap googleMap;
	SharedPreferences sharedPreferences;
	int locationCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
			// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);


		}


	}

	@Override
	public void onMapReady(GoogleMap googleMapResult) {


		// Getting GoogleMap object from the fragment
		googleMap = googleMapResult;

		// Enabling MyLocation Layer of Google Map
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		googleMap.setMyLocationEnabled(true);

		// Opening the sharedPreferences object
		sharedPreferences = getSharedPreferences(
				getString(R.string.location), 0);

		// Getting number of locations already stored
		locationCount = sharedPreferences.getInt(
				getString(R.string.locationCount), 0);

		// Getting stored zoom level if exists else return 0
		String zoom = sharedPreferences.getString(getString(R.string.zoom),
				AppConstants.FAILURE_CODE);

		// If locations are already saved
		if (locationCount != 0) {

			String lat = "";
			String lng = "";

			// Iterating through all the locations stored
			for (int i = 0; i < locationCount; i++) {

				// Getting the latitude of the i-th location
				lat = sharedPreferences.getString(getString(R.string.lat)
						+ i, AppConstants.FAILURE_CODE);

				// Getting the longitude of the i-th location
				lng = sharedPreferences.getString(getString(R.string.lng)
						+ i, AppConstants.FAILURE_CODE);

				// Drawing marker on the map
				drawMarker(new LatLng(Double.parseDouble(lat),
						Double.parseDouble(lng)));
			}

			// Moving CameraPosition to last clicked position
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
					Double.parseDouble(lat), Double.parseDouble(lng))));

			// Setting the zoom level in the map on last position is clicked
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float
					.parseFloat(zoom)));
		}

		googleMap.setOnMapClickListener(new OnMapClickListener() {

			public void onMapClick(LatLng point) {
				locationCount++;

				// Drawing marker on the map
				drawMarker(point);

				/** Opening the editor object to write data to sharedPreferences */
				SharedPreferences.Editor editor = sharedPreferences.edit();

				// Storing the latitude for the i-th location
				editor.putString(
						getString(R.string.lat)
								+ Integer.toString((locationCount - 1)),
						Double.toString(point.latitude));

				// Storing the longitude for the i-th location
				editor.putString(
						getString(R.string.lng)
								+ Integer.toString((locationCount - 1)),
						Double.toString(point.longitude));

				// Storing the count of locations or marker count
				editor.putInt(getString(R.string.locationCount), locationCount);

				/** Storing the zoom level to the shared preferences */
				editor.putString(getString(R.string.zoom),
						Float.toString(googleMap.getCameraPosition().zoom));

				/** Saving the values stored in the shared preferences */
				editor.commit();
				// after that removed
				Toast.makeText(getBaseContext(), "Marker is added to the Map",
						Toast.LENGTH_SHORT).show();
			}
		});

		googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			public void onMapLongClick(LatLng arg0) {
				// Removing the marker and circle from the Google Map
				googleMap.clear();

				// Opening the editor object to delete data from
				// sharedPreferences
				SharedPreferences.Editor editor = sharedPreferences.edit();

				// Clearing the editor
				editor.clear();

				// Committing the changes
				editor.commit();

				// Setting locationCount to zero
				locationCount = 0;
			}
		});


	}
	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		googleMap.addMarker(markerOptions);
	}

}
