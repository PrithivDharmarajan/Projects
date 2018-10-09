package com.fautus.fautusapp.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.FautusApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.fautus.fautusapp.R.string.directory;

/**
 * Image Common Utils .
 */

public class ImageUtil {

    /*Resize Image*/
    public static Bitmap resizeIcon(Context context, int iconName, int width, int height) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, iconName);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }


    /* Reduces the size of an image without affecting its quality*/
    public static String compressImage(Context context, String imagePath) {
        if (context == null) {
            context = FautusApplication.getContext();
        }

        /*init max height and width*/
        final float maxHeightFloat = 1280.0f;
        final float maxWidthFloat = 1280.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        /*get max height and width from image path*/
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidthFloat / maxHeightFloat;

        if (actualHeight > maxHeightFloat || actualWidth > maxWidthFloat) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeightFloat / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeightFloat;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidthFloat / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidthFloat;
            } else {
                actualHeight = (int) maxHeightFloat;
                actualWidth = (int) maxWidthFloat;
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            if (scaledBitmap == null)
                return imagePath;

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            bmp.recycle();

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String filename = context.getString(R.string.app_name) + context.getString(R.string.colon_sym) + context.getString(R.string.upload_photos) + context.getString(R.string.hyphen_sym) + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss:SSS",
                    Locale.getDefault()).format(new Date());
            // External sdcard location
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e(context.getString(R.string.app_name), context.getString(R.string.oops_create) + " "
                            + context.getString(R.string.app_name) + " " + context.getString(directory));
                }
            }

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + filename + ".jpg");

            try {
                //new File(imageFilePath).delete();
                FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return String.valueOf(mediaFile);
        }else{
            return imagePath;
        }
    }

    /*Calculate image size*/
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    /*Download image from parseImageURL*/
    public static void downloadImage(Context context, ArrayList<String> urlArrayList, int arrayListPosInt) {
        if (context == null) {
            context = FautusApplication.getContext();
        }
        if (context != null && urlArrayList != null && urlArrayList.size() > 0 && urlArrayList.size() > arrayListPosInt) {
            String filename = context.getString(R.string.app_name) + context.getString(R.string.colon_sym) + context.getString(R.string.down_photo) + context.getString(R.string.hyphen_sym) + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss:SSS",
                    Locale.getDefault()).format(new Date());

            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(urlArrayList.get(arrayListPosInt));
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            File.separator + context.getString(R.string.app_name) + File.separator + filename + ".jpg");

            DialogManager.getInstance().showToast(context, context.getString(R.string.downloading));
            dm.enqueue(request);
            downloadImage(context, urlArrayList, arrayListPosInt + 1);
        }
    }


    /*Store image from image with strip line*/
    public static void saveToInternalStorage(Context context, final ArrayList<View> viewArrayList, final int arrayListPosInt) {
        if (context == null) {
            context = FautusApplication.getContext();
        }
        if (context != null && viewArrayList != null && viewArrayList.size() > 0 && viewArrayList.size() > arrayListPosInt) {
            viewArrayList.get(arrayListPosInt).setDrawingCacheEnabled(true);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            viewArrayList.get(arrayListPosInt).getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            String filename = context.getString(R.string.app_name) + context.getString(R.string.colon_sym) + context.getString(R.string.down_photo) + context.getString(R.string.hyphen_sym) + new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss:SSS",
                    Locale.getDefault()).format(new Date());
            // External sdcard location
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e(context.getString(R.string.app_name), context.getString(R.string.oops_create) + " "
                            + context.getString(R.string.app_name) + " " + context.getString(directory));
                }
            }

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + filename + ".jpg");
            try {
                FileOutputStream outputStream = new FileOutputStream(mediaFile);
                outputStream.write(bytes.toByteArray());
                outputStream.close();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));
                DialogManager.getInstance().showToast(context, context.getString(R.string.downloaded));
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveToInternalStorage(context, viewArrayList, arrayListPosInt + 1);
        }
    }


}
