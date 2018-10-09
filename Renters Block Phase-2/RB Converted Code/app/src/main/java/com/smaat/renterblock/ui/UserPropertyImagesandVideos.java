package com.smaat.renterblock.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.ImageAdapter;
import com.smaat.renterblock.entity.UserPropertyPics;
import com.smaat.renterblock.model.UserPropertPicResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.ExtendedViewPager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.ScaleImageView;
import com.smaat.renterblock.util.TouchImageView;

public class UserPropertyImagesandVideos extends BaseActivity {

	ArrayList<UserPropertyPics> prop_pics;
	static Context context;
	private Dialog d2;
	ScaleImageView profile_pic;
	ProgressBar image_prog;
	Button play_btn, pause_btn;
	boolean istouched = false;
	int stopPosition;

	TouchImageView profile_pics;
	ExtendedViewPager mViewPager;
	private VideoDetailOnPageChangeListener list_ner;

	FrameLayout frame;
	private RelativeLayout close_lay;

	File file;
	FileInputStream fileInputStream = null;
	byte[] bFile;
	private Bitmap mainImage;
	// private Dialog d3;
	private EditText mDescription;
	private LinearLayout add_lay;
	private GridView gridView;
	private ImageAdapter img_adapter;

	private String mPropertyId = "", mPropertyTyp = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_property_image_grid);

		context = UserPropertyImagesandVideos.this;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			prop_pics = new ArrayList<UserPropertyPics>();
			prop_pics = (ArrayList<UserPropertyPics>) extras
					.getSerializable("user_prop");
			mPropertyId = extras.getString("PropertyId");
			mPropertyTyp = extras.getString("PropType");
		}

		gridView = (GridView) findViewById(R.id.user_grid);

		img_adapter = new ImageAdapter(UserPropertyImagesandVideos.this,
				prop_pics);

		gridView.setAdapter(img_adapter);
		add_lay = (LinearLayout) findViewById(R.id.add_lay);
		add_lay.setVisibility(View.VISIBLE);

	}

	public static void getUrl(String file_type, String file) {
		((UserPropertyImagesandVideos) context).showImageFullView(file_type,
				file);
	}

	private void showImageFullView(String file_type, String file) {
		d2 = new Dialog(UserPropertyImagesandVideos.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d2.setContentView(R.layout.photo_video_full_view);
		d2.setCancelable(true);

		mViewPager = (ExtendedViewPager) d2.findViewById(R.id.view_pager);
		mViewPager.setAdapter(new VideoImagePagerAdapter(prop_pics));
		list_ner = new VideoDetailOnPageChangeListener();
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(list_ner);

		d2.show();
	}

	class VideoImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<UserPropertyPics> prop_pic;

		VideoImagePagerAdapter(ArrayList<UserPropertyPics> user_img) {
			inflater = getLayoutInflater();
			this.prop_pic = user_img;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ExtendedViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return prop_pic.size();
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(
					R.layout.profile_photo_video_full_view, null);

			final VideoView profile_video;
			RelativeLayout adap_vidl1;
			final ProgressBar video_prog;

			// user Profile details
			ImageView profile_img = (ImageView) imageLayout
					.findViewById(R.id.profile_img);
			TextView user_name = (TextView) imageLayout
					.findViewById(R.id.user_name);
			TextView friends_count = (TextView) imageLayout
					.findViewById(R.id.friends_count);
			TextView reviews_count = (TextView) imageLayout
					.findViewById(R.id.reviews_count);
			TextView photos_count = (TextView) imageLayout
					.findViewById(R.id.photos_count);

			aq().progress(R.id.progre)
					.id(profile_img)
					.image(prop_pic.get(position).getUser_pic(), true, true, 0,
							R.drawable.profile_pic, null, 0, 1.0f / 1.0f);

			user_name.setText(prop_pic.get(position).getUser_name());
			friends_count.setText(prop_pic.get(position).getFriends());
			reviews_count.setText(prop_pic.get(position).getReview());
			photos_count.setText(prop_pic.get(position).getPhotos());

			TextView time_txt = (TextView) imageLayout
					.findViewById(R.id.date_time);
			time_txt.setText("Latest updated "
					+ GlobalMethods.timeDiff(prop_pic.get(position)
							.getDatetime()));

			profile_pics = (TouchImageView) imageLayout
					.findViewById(R.id.adap_img1);
			profile_video = (VideoView) imageLayout
					.findViewById(R.id.adap_video1);
			adap_vidl1 = (RelativeLayout) imageLayout
					.findViewById(R.id.adap_vidl1);
			image_prog = (ProgressBar) imageLayout
					.findViewById(R.id.adap_progress1);
			video_prog = (ProgressBar) imageLayout
					.findViewById(R.id.adap_vid_progress1);
			play_btn = (Button) imageLayout
					.findViewById(R.id.adap_video_play_btn1);
			frame = (FrameLayout) imageLayout.findViewById(R.id.main_frame);
			close_lay = (RelativeLayout) imageLayout
					.findViewById(R.id.close_dia);
			Button close_btn = (Button) imageLayout
					.findViewById(R.id.close_dialog);
			RelativeLayout main_lay = (RelativeLayout) imageLayout
					.findViewById(R.id.parent_layout);
			pause_btn = (Button) imageLayout
					.findViewById(R.id.adap_video_pause_btn1);
			TextView desc_text = (TextView) imageLayout
					.findViewById(R.id.desc_text);

			desc_text.setText(prop_pic.get(position).getDescription());

			close_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					d2.dismiss();
				}
			});

			close_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					d2.dismiss();
				}
			});

			if (prop_pic.get(position).getFile_type().equals("image")) {
				// image_prog.setVisibility(View.VISIBLE);
				profile_pics.setVisibility(View.VISIBLE);
				String url = prop_pic.get(position).getFile();
				aq().id(profile_pics).progress(image_prog).image(url);
			} else {
				adap_vidl1.setVisibility(View.VISIBLE);
				profile_pics.setVisibility(View.GONE);
				image_prog.setVisibility(View.GONE);
				main_lay.setBackgroundColor(Color.parseColor("#000000"));
				String file = prop_pic.get(position).getFile();
				String link = file;
				Uri video = Uri.parse(link);
				profile_video.setVideoURI(video);
				profile_video.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						profile_video.setBackground(null);
						profile_video.start();
						video_prog.setVisibility(View.GONE);
					}
				});

				profile_video.setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						video_prog.setVisibility(View.GONE);
						Toast.makeText(UserPropertyImagesandVideos.this,
								"Sorry! Video Cannot be Played.",
								Toast.LENGTH_SHORT).show();
						// d2.dismiss();
						return true;
					}
				});

				profile_video.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {

						if (!istouched) {
							pause_btn.setVisibility(View.VISIBLE);
							istouched = true;
						} else {
							pause_btn.setVisibility(View.GONE);
							istouched = false;
						}
						return false;
					}
				});

				pause_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						stopPosition = profile_video.getCurrentPosition();
						play_btn.setVisibility(View.VISIBLE);
						pause_btn.setVisibility(View.GONE);
						profile_video.pause();
					}
				});

				play_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pause_btn.setVisibility(View.GONE);
						play_btn.setVisibility(View.GONE);
						profile_video.seekTo(stopPosition);
						profile_video.start();
					}
				});

				profile_video
						.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								play_btn.setVisibility(View.VISIBLE);
							}
						});
			}
			((ExtendedViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}

	}

	class VideoDetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			mViewPager.getParent().requestDisallowInterceptTouchEvent(true);

		}

	}

	class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

			try {
				URL urlConnection = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			image_prog.setVisibility(View.GONE);
			profile_pic.setImageBitmap(result);
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bac_btn:
			Intent intent = new Intent(UserPropertyImagesandVideos.this,
					PropertyDetailsActivity.class);
			intent.putExtra("PropertyId", mPropertyId);
			intent.putExtra("PropType", mPropertyTyp);

			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
			break;
		case R.id.add_lay:
			showOptionDialog(this);
			break;

		default:
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

						mainImage = image;

					} else {
						DialogManager.showCustomAlertDialog(this, this,
								"Unsupported file format");
					}

					showDescriptionAlert("image", mainImage);

				} else if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO
						|| requestCode == ProfileImageSelectionUtil.VIDEO_GALLERY) {
					Bitmap thumb = null;
					if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO) {
						thumb = ThumbnailUtils.createVideoThumbnail(
								getFilename().getAbsolutePath(), 70);
					} else {
						Uri selectedVideo = data.getData();
						file = new File(convertMediaUriToPath(selectedVideo));
						thumb = ThumbnailUtils.createVideoThumbnail(
								convertMediaUriToPath(selectedVideo), 70);
					}

					showDescriptionAlert("video", thumb);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDescriptionAlert(final String type, Bitmap thumb) {
		final Dialog d3 = new Dialog(UserPropertyImagesandVideos.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.post_photo_video_view);
		d3.setCancelable(false);
		TextView post_desc = (TextView) d3.findViewById(R.id.post_desc);
		LinearLayout cancel = (LinearLayout) d3.findViewById(R.id.back_icon);
		mDescription = (EditText) d3.findViewById(R.id.description);
		ImageView capture_image = (ImageView) d3
				.findViewById(R.id.capture_image);

		capture_image.setImageBitmap(thumb);

		post_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
				updatePropertyFile(type, mDescription.getText().toString());
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updatePropertyFile(type, "");
				d3.dismiss();
			}
		});

		d3.show();
	}

	private void updatePropertyFile(String filetype, String description) {
		long length = 0;
		String url = AppConstants.BASE_URL + "photovideo";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("property_id",
				PropertyDetailsActivity.for_image_act_property_id + "");
		params.put("agent_or_broker", "0");
		params.put("file_type", filetype);
		params.put("description", description);
		if (filetype.equals("video")) {
			if (file != null && !file.equals("")) {

				try {
					bFile = new byte[(int) file.length()];
					fileInputStream = new FileInputStream(file);
					fileInputStream.read(bFile);
					fileInputStream.close();
					length = file.length();
					length = length / 1024;
					params.put("data", bFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
			mainImage.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
			byte[] mainImageData = (oututStream).toByteArray();
			params.put("data", mainImageData);
		}
		if (filetype.equals("video") && length > 25360) {
			mDialog = DialogManager.showDialog(this,
					"Video Limit Exceeded. Max size is 25 MB.",
					getString(R.string.ok), null, 0, 0);
		} else {
			aq().transformer(t)
					.progress(DialogManager.getProgressDialog(this))
					.ajax(url, params, JSONObject.class,
							new AjaxCallback<JSONObject>() {

								@Override
								public void callback(String url,
										JSONObject json, AjaxStatus status) {

									if (json != null) {
										UserPropertPicResponse mResponse = new Gson().fromJson(
												json.toString(),
												UserPropertPicResponse.class);

										onSuccessPropResponse(mResponse);
									} else {
										statusErrorCode(status);
									}

								}
							});
		}
	}

	private void onSuccessPropResponse(UserPropertPicResponse response) {
		prop_pics.clear();
		PropertyDetailsActivity.mPropertyDetailsResponse.get(0)
				.getUserpropertypic().clear();
		PropertyDetailsActivity.mPropertyDetailsResponse.get(0)
				.setUserpropertypic(response.getUserpropertypic());
		prop_pics = response.getUserpropertypic();
		// gridView.invalidate();
		img_adapter = new ImageAdapter(UserPropertyImagesandVideos.this,
				prop_pics);
		// img_adapter.notifyDataSetChanged();
		gridView.setAdapter(img_adapter);
	}

	protected String convertMediaUriToPath(Uri uri) {
		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	private File getFilename() {

		file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"cameravideo.mp4");

		System.out.println("audio " + file);
		return file;
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		Intent intent = new Intent(UserPropertyImagesandVideos.this,
				PropertyDetailsActivity.class);
		intent.putExtra("PropertyId", mPropertyId);
		intent.putExtra("PropType", mPropertyTyp);

		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

}
