package com.smaat.renterblock.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.renterblock.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ListingProfileImageSelection {

	public static final int CAMERA = 100;
	public static final int GALLERY = 200;
	public static final int CAMERA_VIDEO = 300;
	public static final int VIDEO_GALLERY = 400;
	public static boolean isUriTrue;
	static File output;

	/**
	 * @return Check the Image capture functionality has the bug in device
	 */
	public static boolean hasImageCaptureBug() {

		// list of known devices that have the bug
		ArrayList<String> devices = new ArrayList<String>();
		devices.add("android-devphone1/dream_devphone/dream");
		devices.add("generic/sdk/generic");
		devices.add("vodafone/vfpioneer/sapphire");
		devices.add("tmobile/kila/dream");
		devices.add("verizon/voles/sholes");
		devices.add("google_ion/google_ion/sapphire");

		boolean bool = devices.contains(android.os.Build.BRAND + "/"
				+ android.os.Build.PRODUCT + "/" + android.os.Build.DEVICE);

		return bool;

	}

	public static void openCamera(Activity context, int requestCode,String clickPosition) {
		Intent cameraIntent = null;
		if (requestCode == 100) {
			cameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			if (ListingProfileImageSelection.hasImageCaptureBug()) {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory().getPath())));
			} else {
				File dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				File output = new File(dir, "camerascript.png");
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(output));
			}

		} else {
			cameraIntent = new Intent(
					MediaStore.ACTION_VIDEO_CAPTURE);
				File dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				if(clickPosition.equals("1")) {
					output = new File(dir, "cameravideo1.mp4");
				} else if(clickPosition.equals("2")) {
					output = new File(dir, "cameravideo2.mp4");
				} else if(clickPosition.equals("3")) {
					output = new File(dir, "cameravideo3.mp4");
				} else if(clickPosition.equals("4")) {
					output = new File(dir, "cameravideo4.mp4");
				} else if(clickPosition.equals("5")) {
			output = new File(dir, "cameravideo5.mp4");
		}
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(output));
		}

		context.startActivityForResult(cameraIntent, requestCode);

		context.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);

	}

	private static String imagePathGlobal;
	private static Dialog mDialog;

	public static Bitmap getImage(Intent data, Activity context) {

		try {
			Bitmap image = null;
			String imagePath = null;
			Uri uri = null;

			if (hasImageCaptureBug()) {
				File fi = new File(Environment.getExternalStorageDirectory()
						.getPath());
				try {
					uri = Uri.parse(MediaStore.Images.Media
							.insertImage(context.getContentResolver(),
									fi.getAbsolutePath(), null, null));
					if (!fi.delete()) {
						Log.i("logMarker", "Failed to delete " + fi);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				if (data == null) {

					isUriTrue = false;
					File fi = new File(
							Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
							"camerascript.png");

					imagePath = fi.getAbsolutePath();

					imagePathGlobal = imagePath;

					System.out.println("ImagepAth" + imagePath);

				} else {

					isUriTrue = true;
					uri = data.getData();
				}

			}

			if (uri != null || imagePath != null) {

				try {

					if (uri != null) {
						imagePath = getRealPathFromURI(uri, context);

						image = getBitmap(imagePath);
					} else if (imagePath != null) {
						image = getBitmap(imagePath);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param contentUri
	 * @return Get original path from the given URI
	 */
	public static String getRealPathFromURI(Uri contentUri, Activity context) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	public static void selectImageFromGallery(Activity context, int requestCode) {

		Intent intent = new Intent();

		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);

		context.startActivityForResult(intent, requestCode);

		context.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);

	}

	public static void selectVideoFromGallery(Activity context, int requestCode) {

		Intent intent = new Intent();

		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		context.startActivityForResult(intent, requestCode);

		context.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);

	}

	public static void showOptionNew(final Activity context, final String option,final String clickPosition) {
		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.phot_selection);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView mTitile = (TextView) mDialog.findViewById(R.id.alertTitle);
		
		Button mFromCamera, mFromGallery, mCancel;
		mFromCamera = (Button) mDialog.findViewById(R.id.from_camera);
		mFromGallery = (Button) mDialog.findViewById(R.id.from_galery);
		if (option.equalsIgnoreCase(context.getString(R.string.picture))) {
			mTitile.setText(context.getString(R.string.choose_pic));
			mFromCamera.setText(context.getString(R.string.take_photo));
			mFromGallery.setText(context.getString(R.string.choose_existing_photo));
		} else {
			mTitile.setText(context.getString(R.string.choose_video));
			mFromCamera.setText(context.getString(R.string.take_video));
			mFromGallery.setText(context.getString(R.string.choose_existing_video));
		}
		mCancel = (Button) mDialog.findViewById(R.id.cancel);
		mFromCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				if (option.equalsIgnoreCase(context.getString(R.string.picture))) {
					openCamera(context, ListingProfileImageSelection.CAMERA,clickPosition);
				} else {
					openCamera(context, ListingProfileImageSelection.CAMERA_VIDEO,clickPosition);
				}
			}
		});
		mFromGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();

				if (option.equalsIgnoreCase(context.getString(R.string.picture))) {
					selectImageFromGallery(context,
							ListingProfileImageSelection.GALLERY);
				} else {
					selectVideoFromGallery(context,
							ListingProfileImageSelection.VIDEO_GALLERY);
				}
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

	public static Bitmap getBitmap(String path) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, o);
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;

			Bitmap bitmap = BitmapFactory.decodeStream(
					new FileInputStream(path), null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}

	public static Bitmap getCorrectOrientationImage(Context context,
			Uri selectedImage, Bitmap image) {

		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		int rotate = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(filePath);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);

			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				rotate = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				rotate = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				rotate = 270;
			}
			if (rotate != 0) {
				int w = image.getWidth();
				int h = image.getHeight();

				matrix.preRotate(rotate);
				// Rotate the bitmap
				image = Bitmap.createBitmap(image, 0, 0, w, h, matrix, true);
				image = image.copy(Bitmap.Config.ARGB_8888, true);
			}
		} catch (Exception exception) {
			Log.d("check", "Could not rotate the image");
		}
		return image;
	}

	public static Bitmap getCorrectOrientationImage(Context context,
			Bitmap image) {

		int rotate = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(imagePathGlobal);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);

			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				rotate = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				rotate = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				rotate = 270;
			}
			if (rotate != 0) {
				int w = image.getWidth();
				int h = image.getHeight();

				matrix.preRotate(rotate);
				// Rotate the bitmap
				image = Bitmap.createBitmap(image, 0, 0, w, h, matrix, true);
				image = image.copy(Bitmap.Config.ARGB_8888, true);
			}
		} catch (Exception exception) {
			Log.d("check", "Could not rotate the image");
		}

		return image;
	}

}
