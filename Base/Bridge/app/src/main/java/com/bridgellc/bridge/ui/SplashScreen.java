package com.bridgellc.bridge.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends BaseActivity {


    private ImageView mSplashImagView;
    private static final int SPLASH_DURATION = 3000;
    //    private Handler mHandler;
    private Button mSignInBtn;
    private static int _index = 1;
    private View mView;
    int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_splash_screen);
        initComponents();
        if (addPermissions()) {
            nextScreenCheck();
        }

    }

    private boolean addPermissions() {
        boolean addPermission = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            addPermission = checkAndRequestPermissions();
        }
        return addPermission;
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int readContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission
                .READ_CONTACTS);
        int writeContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (readContactPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (writeContactPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), permsRequestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        //Next screen
                        nextScreenCheck();
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                            showDialogOK(getString(R.string.camera_storage_req),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, getString(R.string.go_settings_per), Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .create()
                .show();
    }


    boolean isIncreased = false;
    final Handler handler = new Handler();
    Runnable run;

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mAnimFade = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade);

        mSignInBtn = (Button) findViewById(R.id.footer_btn);
        mFooterLay = (LinearLayout) findViewById(R.id.footer_parent_one_lay);
        mSplashImagView = (ImageView) findViewById(R.id.splash_img);
        mView = (View) findViewById(R.id.view);
        mSignInBtn.setVisibility(View.GONE);
        mFooterLay.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);


        run = new Runnable() {
            @Override
            public void run() {
                {
                    try {

                        Bitmap bmp = BitmapFactory
                                .decodeStream(SplashScreen.this.getAssets()
                                        .open("splashAnimation/bridge-loading-"
                                                + _index + ".jpg"));
                        mSplashImagView.setImageBitmap(bmp);
                        if (!isIncreased || _index == 1) {
                            _index++;

                            isIncreased = false;
                            if (_index == 8) {
                                _index = 6;
                                isIncreased = true;
                            }
                        } else {
                            _index--;
                        }

                        handler.postDelayed(this, 300);

                    } catch (IOException e) {
                        Log.v("", e.getMessage());
                    }

                }
            }
        };

        handler.postDelayed(run, 0);


        mSignInBtn.setText(getString(R.string.start));
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeCallbacks(run);
                nextScreen(SignInScreen.class, true);
            }
        });


    }


    private void nextScreenCheck() {
        if (GlobalMethods.isLoggedIn(SplashScreen.this)) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            nextHomeScreen();
        } else {
            nextSignInScreen();
        }
    }


    private void nextSignInScreen() {
        mFooterLay.setVisibility(View.VISIBLE);
        mSignInBtn.setVisibility(View.VISIBLE);
        mView.setVisibility(View.VISIBLE);
    }

    private void nextHomeScreen() {

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                handler.removeCallbacks(run);
                nextScreen(HomeScreenActivity.class, true);
            }
        }, SPLASH_DURATION);
    }


//    private void facebookHashKey(BaseActivity context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            String packageName = context.getApplicationContext().getPackageName();
//
//            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
//
//
//            for (Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
