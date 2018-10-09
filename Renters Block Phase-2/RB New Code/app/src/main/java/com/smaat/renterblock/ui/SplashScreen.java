package com.smaat.renterblock.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.ViewGroup;

import com.smaat.renterblock.R;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class implements  UI for splash screen
 * Created by Prithiv on 8/19/2017.
 */

public class SplashScreen extends BaseActivity {


    /*Variable initialization using bind view*/

    @BindView(R.id.parent_lay)
    ViewGroup mSplashViewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_splash_screen);
        initView();
        facebookHashKey(this);
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(mSplashViewGroup);

        /*Ask permission for */
        if (askPermissions()) {
            nextScreenCheck();
        }
    }

    private void nextScreenCheck() {
        AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
        AppConstants.LAST_CURRENT_FRAG=new MapFragment();
        /*If user logged in already, this process will happen*/
        nextScreen(PreferenceUtil.getBoolPreferenceValue(SplashScreen.this, AppConstants.TUTORIAL_SEEN) ? HomeScreen.class : TutorialScreen.class, true);

    }


    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            int microPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);


            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
            }
            if (microPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    nextScreenCheck();
                }

                public void onNegativeClick() {
                    nextScreenCheck();
                }
            });
        }

        return addPermission;
    }

    @SuppressLint("PackageManagerGetSignatures")
    private void facebookHashKey(BaseActivity context) {
        PackageInfo packageInfo;
        String key;
        try {
            String packageName = context.getApplicationContext().getPackageName();

            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                System.out.println("Hash Key-----" + key);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
