package com.smaat.renterblock.ui;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.TypefaceSingleton;

public class AddSingleListingActivity extends BaseActivity implements
		OnMapClickListener, OnClickListener, DialogMangerCallback {

	private LinearLayout mSingleImageLay;
	private Bitmap mBitmapFromDevice;
	private ImageView mListImg1;
	private Button mListCloseBtn1;
	private int mImgID;

	byte[] mPropImg1;
	private String mAddress = "", mLatitude = "", mLongtitude = "",
			mDescription = "";
	private EditText mAddressEdit, mDescriptionEdit;
	private String selectedType;
	private TextView mHeaderText;
	private String mCallAPI = "Ok";
	private GoogleMap mGoogleMap;
	protected boolean donzoom = false;
	private Marker marker;
	private String location_address_temp;
	private boolean isFirst;
	private PropertyEntity prop_entity;
	private LinearLayout save_top;
	private String property_id = "";
	private String description = "";

	// private ArrayList<PropertyPictures> prop_pic_vid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_add_listing);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout_add_listing);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			prop_entity = new PropertyEntity();
			prop_entity = (PropertyEntity) extras
					.getSerializable("property_entity");
		}

		initComponents();
		initializeMap();
	}

	private void initComponents() {

		UserID = (String) GlobalMethods.getValueFromPreference(
				AddSingleListingActivity.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);
		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		mSingleImageLay = (LinearLayout) findViewById(R.id.single_img_lay);
		mSingleImageLay.setVisibility(View.VISIBLE);

		mListImg1 = (ImageView) findViewById(R.id.list_img11);
		mListCloseBtn1 = (Button) findViewById(R.id.list_img_close11);

		mHeaderText = (TextView) findViewById(R.id.header_txt);

		if (prop_entity != null) {
			mHeaderText.setText("Edit Listing");
		} else {
			if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
				mHeaderText.setText(getString(R.string.add_property_header));
			} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
					|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
				mHeaderText.setText(getString(R.string.add_listing_header));
			}
		}

		save_top = (LinearLayout) findViewById(R.id.save_top);
		save_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validateAddProperty();
			}
		});

		mAddressEdit = (EditText) findViewById(R.id.address_edit1);
		mDescriptionEdit = (EditText) findViewById(R.id.description_edit1);

		mAddressEdit.setMovementMethod(new ScrollingMovementMethod());
		mDescriptionEdit.setMovementMethod(new ScrollingMovementMethod());

		mAddressEdit.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					new getPositionInfo().execute((mAddressEdit.getText()
							.toString()));
					hideSoftKeyboard(AddSingleListingActivity.this);
					return true;
				}
				return false;
			}
		});

		mDescriptionEdit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (view.getId() == R.id.description_edit1) {
					view.getParent().requestDisallowInterceptTouchEvent(true);
					switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						view.getParent().requestDisallowInterceptTouchEvent(
								false);
						break;
					}
				}
				return false;
			}
		});

		if (prop_entity != null) {

			AppConstants.SELECTED_PROPERTY_TYPE = prop_entity
					.getProperty_type().toString();
			property_id = prop_entity.getProperty_id().toString();

			// prop_pic_vid = new ArrayList<PropertyPictures>();

			if (prop_entity.getProperty_pics().get(0).getDescription() != null) {
				description = prop_entity.getProperty_pics().get(0)
						.getDescription().toString();
			}
			if (prop_entity.getProperty_pics().get(0).getFile_type()
					.equals("image")) {
				aq().id(R.id.list_img1)
						.progress(R.id.img_progress_1)
						.image(prop_entity.getProperty_pics().get(0).getFile(),
								true, true, 0, 0, null, 0, 1.0f / 1.0f);
				mListCloseBtn1.setVisibility(View.VISIBLE);
				new convertUrltoBitmap().execute(prop_entity.getProperty_pics()
						.get(0).getFile(), "0");

			}

			String txt_arr = prop_entity.getAddress().toString();
			int i = txt_arr.indexOf(',',
					1 + txt_arr.indexOf(',', 1 + txt_arr.indexOf(',')));

			String firstPart = txt_arr.substring(0, i);
			String secondPart = txt_arr.substring(i + 1);
			mAddressEdit.setText(firstPart + "\n" + secondPart);

			new getPositionInfo().execute((mAddressEdit.getText().toString()));
			mDescriptionEdit.setText(prop_entity.getDescription());
		}
	}

	class convertUrltoBitmap extends AsyncTask<String, Void, Bitmap> {

		String position = "";

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap image = null;
			position = params[1];
			try {
				URL url = new URL(params[0]);
				image = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return image;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			isFirst = true;
			mBitmapFromDevice = result;
			mListImg1.setImageBitmap(mBitmapFromDevice);
			ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
			mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
					oututStream);
			byte[] mainImageData = (oututStream).toByteArray();
			mPropImg1 = mainImageData;

		}
	}

	private void initializeMap() {
		if (mGoogleMap == null) {
			/*mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();*/
			mGoogleMap.setOnMapClickListener(this);
			if (mGoogleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {
				mGoogleMap.setMyLocationEnabled(true);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
				mGoogleMap.getUiSettings().setCompassEnabled(true);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
				mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

				mGoogleMap
						.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

							@Override
							public void onMyLocationChange(Location arg0) {
								CameraPosition cameraPosition;
								if (!donzoom) {
									marker = mGoogleMap.addMarker(new MarkerOptions()
											.position(
													new LatLng(
															Double.valueOf(arg0
																	.getLatitude()),
															Double.valueOf(arg0
																	.getLongitude())))
											.title("Current Location")
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.listing_map_marker)));
									cameraPosition = new CameraPosition.Builder()
											.target(new LatLng(
													Double.valueOf(arg0
															.getLatitude()),
													Double.valueOf(arg0
															.getLongitude())))
											.zoom((float) 13).build();
									mGoogleMap.animateCamera(CameraUpdateFactory
											.newCameraPosition(cameraPosition));
									donzoom = true;
									mLatitude = String.valueOf(arg0
											.getLatitude());
									mLongtitude = String.valueOf(arg0
											.getLongitude());
									callGoogleApiService(arg0.getLatitude(),
											arg0.getLongitude());
								}
							}
						});
			}
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mLatitude = String.valueOf(point.latitude);
		mLongtitude = String.valueOf(point.longitude);
		if (!mAddressEdit.getText().toString().trim().equalsIgnoreCase("")
				&& !location_address_temp.equalsIgnoreCase(mAddressEdit
						.getText().toString().trim())) {
			new getPositionInfo().execute((mAddressEdit.getText().toString()));
		} else {
			if (marker != null) {
				mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
				marker.remove();
			}
			marker = mGoogleMap.addMarker(new MarkerOptions()
					.position(point)
					.title("Current Location")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.listing_map_marker)));
			callGoogleApiService(point.latitude, point.longitude);
		}
	}

	private void callGoogleApiService(double latitude, double longitude) {
		LatLng latLng = new LatLng(latitude, longitude);
		mGoogleMap.addMarker(new MarkerOptions().position(latLng));
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=true";
		aq().ajax(url, JSONObject.class, this, "addresslocation");
	}

	public void addresslocation(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				GoogleApiEntity obj = new Gson().fromJson(json.toString(),
						GoogleApiEntity.class);
				onRequest(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onRequest(GoogleApiEntity response) {
		try {
			if (response != null) {
				mAddressEdit.setText(response.getResults().get(0)
						.getFormatted_address().toString());
				location_address_temp = response.getResults().get(0)
						.getFormatted_address().toString();
			}
		} catch (Exception e) {
		}
	}

	class getPositionInfo extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {

			String address = params[0];
			return GlobalMethods.getLatlong(address,
					AddSingleListingActivity.this);
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			if (result != null) {
				String[] position_points = result.get(1).split(",");
				if (marker != null) {
					mGoogleMap.animateCamera(CameraUpdateFactory
							.newLatLng(new LatLng(Double
									.valueOf(position_points[0]), Double
									.valueOf(position_points[1]))));
					marker.remove();
				}
				marker = mGoogleMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.valueOf(position_points[0]),
										Double.valueOf(position_points[1])))
						.title("Current Location")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.listing_map_marker)));
				callGoogleApiService(Double.valueOf(position_points[0]),
						Double.valueOf(position_points[1]));
				System.out.println("found result" + " " + result);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			Intent intent = new Intent(AddSingleListingActivity.this,
					ListingActivity.class);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			startActivity(intent);
			break;
		// case R.id.save_top:
		// validateAddProperty();
		// break;
		case R.id.save_bottom1:
			validateAddProperty();
			break;
		case R.id.list_img11:
			mImgID = v.getId();
			// ProfileImageSelectionUtil.showOptionNew(this, "pic");
			showOptionDialog(AddSingleListingActivity.this);
			break;
		case R.id.list_img_close11:
			isFirst = false;
			mListImg1.setImageResource(R.drawable.listing_add_photo_normal);
			mListCloseBtn1.setVisibility(View.GONE);
			break;

		}

	}

	private void showOptionDialog(Context context) {
		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.image_selection);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		Button mPhoto, mVideo, mCancel;
		mPhoto = (Button) mDialog.findViewById(R.id.photo);
		mVideo = (Button) mDialog.findViewById(R.id.video);
		mCancel = (Button) mDialog.findViewById(R.id.cancel);
		mPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				cameraOption("pic");
			}
		});
		mVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				cameraOption("video");
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();

	}

	private void cameraOption(String option) {
		ProfileImageSelectionUtil.showOptionNew(this, option);

	}

	private void validateAddProperty() {

		mAddress = mAddressEdit.getText().toString().trim();
		mDescription = mDescriptionEdit.getText().toString().trim();

		if (!isFirst) {
			mBitmapFromDevice = null;
		}

		if (mBitmapFromDevice == null) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_upload_image));

		} else if (mAddress.equals("") && mAddress.length() <= 0) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_enter_address));
		} else if (mDescription.equals("") && mDescription.length() <= 0) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_enter_description));
		} else {
			callAddProperty();
		}
	}

	private void callAddProperty() {

		String Url = AppConstants.BASE_URL + "addnewproperty";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);
		params.put("property_name", "");
		params.put("address", mAddress);
		params.put("type", "Rent");
		params.put("latitude", mLatitude);
		params.put("longitude", mLongtitude);
		params.put("description", mDescription);
		params.put("property_type", "");
		params.put("price_range", "0");
		params.put("beds", "0");
		params.put("baths", "0");
		params.put("fee", "0");
		params.put("square_footage", "0");
		params.put("build_year", "0");
		params.put("lot_size", "0");
		params.put("new_construction", "0");
		params.put("foreclosure", "0");
		params.put("open_house", "0");
		params.put("reduced_prices", "0");
		params.put("MLS", "0");
		params.put("resale", "");
		params.put("property_image1", mPropImg1);
		params.put("property_image2", "");
		params.put("property_image3", "");
		params.put("property_image4", "");
		params.put("property_image5", "");
		params.put("property_id", property_id);
		params.put("description1", description);
		params.put("description2", "");
		params.put("description3", "");
		params.put("description4", "");
		params.put("description5", "");
		params.put("property_video1", "");
		params.put("property_video2", "");
		params.put("property_video3", "");
		params.put("property_video4", "");
		params.put("property_video5", "");

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {
								if (json != null) {
									System.out.println(json);
									CommonResponse mResponse = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									onSuccessResponse(mResponse);
								} else {
									statusErrorCode(status);
								}
							}

						});
	}

	protected void onSuccessResponse(CommonResponse mResponse) {

		if (mResponse.getError_code().equalsIgnoreCase(
				AppConstants.SUCCESS_CODE)) {
			mCallAPI = "CallAPI";
			DialogManager.showCustomAlertDialog(this, this, mResponse.getMsg());

		}

	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		if (mCallAPI.equalsIgnoreCase("CallAPI")) {
			launchActivity(ListingActivity.class);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else {
			/**
			 * Close Popup
			 */
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			if (resultCode == RESULT_OK) {

				if (requestCode == ProfileImageSelectionUtil.CAMERA
						|| requestCode == ProfileImageSelectionUtil.GALLERY) {

					Bitmap image = ProfileImageSelectionUtil.getImage(data,
							this);

					if (image != null) {
						if (requestCode == ProfileImageSelectionUtil.CAMERA) {
							if (ProfileImageSelectionUtil.isUriTrue) {
								image = ProfileImageSelectionUtil
										.getCorrectOrientationImage(this,
												data.getData(), image);
							} else {
								image = ProfileImageSelectionUtil
										.getCorrectOrientationImage(this, image);
							}
						} else {

							Uri selectedImage = data.getData();

							image = ProfileImageSelectionUtil
									.getCorrectOrientationImage(this,
											selectedImage, image);
						}

						mBitmapFromDevice = image;

						setImage(mImgID, mBitmapFromDevice);

					} else {
						DialogManager.showCustomAlertDialog(this, this,
								"Unsupported file format");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setImage(int mImgID, Bitmap mBitmap) {

		mBitmapFromDevice = mBitmap;
		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
		byte[] mainImageData = (oututStream).toByteArray();

		if (mImgID == R.id.list_img11) {
			isFirst = true;
			mPropImg1 = mainImageData;
			mListImg1.setImageBitmap(mBitmap);
			mListCloseBtn1.setVisibility(View.VISIBLE);

		}
		showDescriptionAlert(mBitmap);
	}

	private void showDescriptionAlert(Bitmap thumb) {
		final Dialog d3 = new Dialog(AddSingleListingActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.post_photo_video_view);
		d3.setCancelable(false);
		TextView post_desc = (TextView) d3.findViewById(R.id.post_desc);
		LinearLayout cancel = (LinearLayout) d3.findViewById(R.id.back_icon);
		final EditText mDescription = (EditText) d3
				.findViewById(R.id.description);
		ImageView capture_image = (ImageView) d3
				.findViewById(R.id.capture_image);

		capture_image.setImageBitmap(thumb);

		post_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				description = mDescription.getText().toString();
				d3.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
			}
		});

		d3.show();
	}
}
