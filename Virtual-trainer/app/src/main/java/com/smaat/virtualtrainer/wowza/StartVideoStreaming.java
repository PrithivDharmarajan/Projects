package com.smaat.virtualtrainer.wowza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.CommonModelEntity;
import com.smaat.virtualtrainer.ui.AddJoinessScreen;
import com.smaat.virtualtrainer.ui.EntryScreen;
import com.smaat.virtualtrainer.ui.HomeScreen;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.configuration.WowzaConfig;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartVideoStreaming extends BaseActivity implements WZStatusCallback, DialogMangerTwoBtnCallback {


    @BindView(R.id.parent_lay)
    ViewGroup mStartStreamingViewGroup;

    // The top level GoCoder API interface
    private WowzaGoCoder mGoCoder;

    // The GoCoder SDK camera view
    private WZCameraView mGoCoderCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_start_video_streaming);

        initComponents();
    }


    private void initComponents() {

        ButterKnife.bind(this);
        setupUI(mStartStreamingViewGroup);

        // Initialize the GoCoder SDK
        this.mGoCoder = WowzaGoCoder.init(getApplicationContext(), AppConstants.WOWZA_LICENSE_KEY);

        if (this.mGoCoder == null) {
            // If initialization failed, retrieve the last error and display it
            WZError goCoderInitError = WowzaGoCoder.getLastError();
            Toast.makeText(this,
                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Set the camera view
        this.mGoCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);
        this.mGoCoder.setCameraView(this.mGoCoderCameraView);

        // Specify the broadcast configuration parameters
        WowzaConfig broadcastConfig = this.mGoCoder.getConfig();

        // Update the active config to the defaults for 720p video
        broadcastConfig.set(WZMediaConfig.FRAME_SIZE_1920x1080);
        // Set the address for the Wowza Streaming Engine server or Wowza Cloud
        broadcastConfig.setHostAddress("192.168.1.180");

        // Set the name of the stream
        // broadcastConfig.setStreamName(AppConstants.STREAMING_NAME);
        broadcastConfig.setStreamName("myStream");
        broadcastConfig.setUsername("Android");
        broadcastConfig.setPassword("smaat123");
        // broadcastConfig.setApplicationName("live");
        // broadcastConfig.setPortNumber(1935);

        // Update the active config
        this.mGoCoder.setConfig(broadcastConfig);

        WZStreamingError configValidationError = mGoCoder.getConfig().validateForBroadcast();

        if (configValidationError != null) {
            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (this.mGoCoder.isStreaming()) {
            // Stop the broadcast that is currently running
            this.mGoCoder.endStreaming(this);
        } else {
            // Start streaming
            DialogManager.showProgress(this);
            this.mGoCoder.startStreaming(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (this.mGoCoder != null) {
            this.mGoCoder.startCameraPreview();

            WZCamera activeCamera = this.mGoCoderCameraView.getCamera();
            if (activeCamera != null && activeCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS)) {
                activeCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);
            }
        }
    }

    @OnClick({R.id.camera_img, R.id.flash_img, R.id.profile_img, R.id.share_img, R.id.end_call_img, R.id.add_user_img, R.id.setting_img})
    public void setOnClick(View v) {
        switch (v.getId()) {

            case R.id.camera_img:
//
//                if (mCameraImg.getTag().equals(AppConstants.SUCCESS_CODE)) {
//                    mCameraImg.setTag(AppConstants.FAILURE_CODE);
//                    mCameraImg.setImageResource(R.drawable.camera_back_img);
//                    app().selectCamera("BACK");
//                } else {
//                    mCameraImg.setTag(AppConstants.SUCCESS_CODE);
//                    mCameraImg.setImageResource(R.drawable.camera_front_img);
//                    app().selectCamera("FRONT");
//                }
                break;

            case R.id.flash_img:

                boolean hasFlash = this.getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

                if (!hasFlash) {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.flash_nt_support), getString(R.string.ok), false, getString(R.string.ok), true, true, StartVideoStreaming.this);
                } else {
                    //Flash light Action
                }


                break;
            case R.id.profile_img:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, StartVideoStreaming.this);
                break;

            case R.id.share_img:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.streaming_id) + " " + AppConstants.STREAMING_ID);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.end_call_img:


                WZStreamingError configValidationError = mGoCoder.getConfig().validateForBroadcast();

                if (configValidationError != null) {
                    Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                } else if (this.mGoCoder.isStreaming()) {
                    // Stop the broadcast that is currently running
                    APIRequestHandler.getInstance().startStreamingAPICall(AppConstants.STREAMING_NAME, AppConstants.STREAMING_ID,
                            getString(R.string.two), this);
                }
                break;

            case R.id.add_user_img:
                nextScreen(AddJoinessScreen.class, false);

                break;

            case R.id.setting_img:
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, StartVideoStreaming.this);
                break;

        }

    }


//    @Override
//    public void onClick(View view) {
//        // Ensure the minimum set of configuration settings have been specified necessary to
//        // initiate a broadcast streaming session
//        WZStreamingError configValidationError = mGoCoder.getConfig().validateForBroadcast();
//
//        if (configValidationError != null) {
//            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
//        } else if (this.mGoCoder.isStreaming()) {
//            // Stop the broadcast that is currently running
//            this.mGoCoder.endStreaming(this);
//            onBackPressed();
//        }else
//
//        {
//            // Start streaming
//            this.mGoCoder.startStreaming(this);
//        }
//    }


    @Override
    public void onWZStatus(WZStatus goCoderStatus) {
        {
            // A successful status transition has been reported by the GoCoder SDK

            switch (goCoderStatus.getState()) {
                case WZState.STARTING:
                    System.out.println("STARTING...");
                    break;

                case WZState.READY:
                    System.out.println("READY...");
                    break;

                case WZState.RUNNING:
                    DialogManager.hideProgress();
                    System.out.println("RUNNING...");
                    break;

                case WZState.STOPPING:
                    System.out.println("STOPPING...");
                    break;

                case WZState.IDLE:
                    System.out.println("IDLE...");
                    DialogManager.hideProgress();
                    onBackPressed();
                    break;

                default:
                    System.out.println("default...");
                    break;
            }

        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonModelEntity) {
            CommonModelEntity callEndEntityRes = (CommonModelEntity) responseObj;
            if (callEndEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                this.mGoCoder.endStreaming(this);
            } else {
                DialogManager.showBasicAlertDialog(this, callEndEntityRes.getMessage(), getString(R.string.ok), false,
                        getString(R.string.ok), true, true, this);
            }
        }
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartVideoStreaming.this,
                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (GlobalMethods.getStringValue(StartVideoStreaming.this, AppConstants.USER_STATUS).equalsIgnoreCase
                (AppConstants.SUCCESS_CODE)) {
            previousScreen(HomeScreen.class, true);
        } else {
            previousScreen(EntryScreen.class, true);
        }

    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

}
