package com.smaat.ipharma.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smaat.ipharma.R;
import com.smaat.ipharma.fragments.HistoryFragment;
import com.smaat.ipharma.ui.HomeScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ProfileImageSelectionUtil {

	public static final int CAMERA = 100;
	public static final int GALLERY = 200;
	public static String IMAGEPATH = "IMAGEPATH";
	public static String CURERENTPOS = "CURERENTPOS";
	static Dialog alertOptionDialog;
	public static boolean isUriTrue;
	public static String ImageName = "";

	/**
	 * @return
	 *
	 *         Check the Image capture functionality has the bug in device
	 */

	public static ProfileImageSelectionUtil INSTANCE = new ProfileImageSelectionUtil();

	public static ProfileImageSelectionUtil getInstance() {
		return INSTANCE;

	}

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

	public Bitmap getBitmapFromCamera(Context mContext, Intent data) {
		Bitmap mThumbnail = null;
		/**
		 * 1. While capture image from camera get Bitmap from (onActivityResult)
		 * data(Intent). 2. Create ByteArrayOutputStream and compress the
		 * bitmap. Note : Give quality value as 100 - is give good quality 3.
		 * Create File and set destination path. 4. Create FileOutputStream and
		 * add the destination file location and close the FileOutputStream.
		 */

		// ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Bitmap bitmap = HomeScreen.profile_img;
		// if (bitmap != null) {
		// bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
		// Image myImg;
		// myImg = Image.getInstance(stream.toByteArray());
		// }
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		mThumbnail = (Bitmap) data.getExtras().get("data");

		ByteArrayOutputStream mByteOutputStream = new ByteArrayOutputStream();

		mThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, mByteOutputStream);

		File mDestination = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis()
						+ mContext.getString(R.string.pic_format));

		FileOutputStream mFileOutputStream;

		try {
			mDestination.createNewFile();

			mFileOutputStream = new FileOutputStream(mDestination);
			mFileOutputStream.close();

		} catch (FileNotFoundException e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
		}
		return mThumbnail;

	}
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}
	@SuppressWarnings("deprecation")
	public Bitmap getBitmapFromGallery(Context mContext, Intent data) {
		Bitmap mThumbnail = null;
		/**
		 * 1. While took image from gallery get Uri from (onActivityResult)
		 * data(Intent). 2. Create String array and get MediaColumns data. 3.
		 * Create Cursor, Index and get Selected Image path from cursor. 4.
		 * Create bitmap and get value from Selected Image path using
		 * BitmapFactory.
		 */

		Uri mSelectedImageURI = data.getData();
		String[] mProjection = { MediaColumns.DATA };

		Cursor mCursor = ((Activity) mContext).managedQuery(mSelectedImageURI,
				mProjection, null, null, null);

		int mColumIndex = mCursor.getColumnIndexOrThrow(MediaColumns.DATA);
		mCursor.moveToFirst();

		String mSelectedImagePath = mCursor.getString(mColumIndex);

		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(mSelectedImagePath, mOptions);
		final int REQUIRED_SIZE = 200;
		int scale = 1;
		while (mOptions.outWidth / scale / 2 >= REQUIRED_SIZE
				&& mOptions.outHeight / scale / 2 >= REQUIRED_SIZE)
			scale *= 2;
		mOptions.inSampleSize = scale;
		mOptions.inJustDecodeBounds = false;
		mThumbnail = BitmapFactory.decodeFile(mSelectedImagePath, mOptions);

		return mThumbnail;

	}

	public static void openCamera(Activity context, int requestCode) {

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		if (ProfileImageSelectionUtil.hasImageCaptureBug()) {
			cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory().getPath())));
		} else {


			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			ImageName = "iPharma"+".png";
			File file = new File(Environment.getExternalStorageDirectory()+File.separator + ImageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			context.startActivityForResult(intent, CAMERA);

		}
		// if (cameraIntent.resolveActivity(context.getPackageManager()) !=
		// null) {
		// context.startActivityForResult(cameraIntent, requestCode);
		// }
		context.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);

	}

	private static String imagePathGlobal;

	// private static String imagePathGlobal;

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
							"camerascript"+ System.currentTimeMillis()+".png");

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
						imagePath = getRealPathFromURI(uri,context);

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



	/*public static Bitmap getImage(Intent data, Activity context) {

		try {
			Bitmap image = null;
			String imagePath = null;
			Uri uri = null;

			if (hasImageCaptureBug()) {
				File fi = new File(Environment.getExternalStorageDirectory()
						.getPath());
				try {
					uri = Uri.parse(android.provider.MediaStore.Images.Media
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

	}*/

	/**
	 * @param contentUri
	 * @param context
	 * @return
	 *
	 *         Get original path from the given URI
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


	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}


	/*public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}*/



	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static void selectImageFromGallery(Activity context, int requestCode) {

		/*Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image*//*");
		context.startActivityForResult(
				Intent.createChooser(intent, "Select Picture"), requestCode);*/


		Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		context.startActivityForResult(pickPhoto , requestCode);


		context.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);

	}

	public static Dialog showscanPopUp(final Activity context) {

		//Dialog mDialog = getDialog(context, R.layout.scan_popup);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.scan_popup, null);

		alertOptionDialog = new Dialog(context);
		alertOptionDialog.getWindow().setGravity(Gravity.CENTER);
		alertOptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		alertOptionDialog.setContentView(view);

		alertOptionDialog.setCancelable(true);
		LinearLayout mCamera = (LinearLayout) alertOptionDialog
				.findViewById(R.id.camera_layout);
		LinearLayout mGallery = (LinearLayout) alertOptionDialog
				.findViewById(R.id.gallery_layout);
		LinearLayout mHistory = (LinearLayout) alertOptionDialog
				.findViewById(R.id.history_layout);

		mCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertOptionDialog.cancel();
				openCamera(context, ProfileImageSelectionUtil.CAMERA);

			}
		});

		mGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertOptionDialog.cancel();
				selectImageFromGallery(context,
						ProfileImageSelectionUtil.GALLERY);
			}
		});

		mHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertOptionDialog.cancel();
				((HomeScreen) context).pushFragment(new HistoryFragment());
				/*((HomeScreen) context).replaceFragment(new HistoryFragment(),
						true);
				HomeScreen.mHeaderLeft
						.setBackgroundResource(R.drawable.back_butto);
				HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				HomeScreen.mBottombar.setVisibility(View.GONE);
				HomeScreen.mFooterText.setText(R.string.select_pres);*/
			}
		});

		alertOptionDialog.show();

		return alertOptionDialog;
	}


	public static Dialog getDialog(Context mContext, int mLayout) {


		Dialog mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
				.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mDialog.setContentView(mLayout);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(true);

		return mDialog;
	}

	public static Bitmap getBitmap(String path) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, o);
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
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

	public static Bitmap getCorrectOrientationImage(Context context,Uri selectedImage, Bitmap image) {

		Log.e("selectedImage","selectedImage"+selectedImage);
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
