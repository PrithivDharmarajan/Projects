package com.smaat.renterblock.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by sys on 13-Sep-17.
 */

public class ImageUtil {

    public static Bitmap loadBitmapFromView(View v) {

        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;

//        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//        v.draw(c);
//        return b;
    }


    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "RentersBlock Image");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("RentersBlock Image", "Oops! Failed create "
                        + "RentersBlock Image" + " directory");
                return null;
            }
        }

        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HHmmss",
                Locale.getDefault()).format(new Date());


        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + ".png");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + ".mp4");
        } else {

            return null;
        }

        return mediaFile;
    }

    public static MultipartBody.Part getMultipartBody(String key, String type, String filepath) {
        MultipartBody.Part multiPartBody = null;
        File file = new File(filepath);
        RequestBody requestBody;
        switch (type) {
            case "image":
                requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                multiPartBody = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                break;
            case "video":
                requestBody = RequestBody.create(MediaType.parse("video/*"), file);
                multiPartBody = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                break;
            case "audio":
                requestBody = RequestBody.create(MediaType.parse("audio/*"), file);
                multiPartBody = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
                break;
        }

        return multiPartBody;

    }
}
