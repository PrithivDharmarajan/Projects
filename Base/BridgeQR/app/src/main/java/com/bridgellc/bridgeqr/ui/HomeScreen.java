package com.bridgellc.bridgeqr.ui;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridgeqr.R;
import com.bridgellc.bridgeqr.apiinterface.APIRequestHandler;
import com.bridgellc.bridgeqr.main.BaseActivity;
import com.bridgellc.bridgeqr.model.CommonResponse;
import com.bridgellc.bridgeqr.utils.AppConstants;
import com.bridgellc.bridgeqr.utils.CameraPreview;
import com.bridgellc.bridgeqr.utils.DialogManager;
import com.bridgellc.bridgeqr.utils.DialogMangerOkCallback;
import com.bridgellc.bridgeqr.utils.GlobalMethods;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.net.ConnectException;
import java.util.Locale;

import retrofit.RetrofitError;


public class HomeScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private Camera mCamera;
    private Handler autoFocusHandler;
    private Vibrator vibe;
    private ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private Dialog mHomeSceendialog;

    static {
        System.loadLibrary("iconv");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        initComponents();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Header and Footer Init
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);
        mHeadeLeftImg = (ImageView) findViewById(R.id.header_left_img);
        mHeadeRightImg = (ImageView) findViewById(R.id.header_right_img);

        //SetData
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightImg.setImageResource(R.drawable.logout_img);
        mHeaderTxt.setText(getString(R.string.qr_code).toUpperCase(Locale.ENGLISH));

        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.ENABLE, 0);
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        scanner.setConfig(Symbol.QRCODE, Config.ENABLE, 1);


        CameraPreview mPreview = new CameraPreview(HomeScreen.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        TextView mScanner = (TextView) findViewById(R.id.text2);
        Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.top_bottom_top);
        localAnimation.setFillAfter(true);
        mScanner.setAnimation(localAnimation);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right_btn_lay:

                releaseCamera(false);
                if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
                    mHomeSceendialog.dismiss();
                }

                mHomeSceendialog = DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), getString(R.string.sign_out), getString(R.string.yes), getString(R.string.no), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }

                    @Override
                    public void onYesClick() {
                        releaseCamera(true);
                        if (GlobalMethods.getStringValue(HomeScreen.this, AppConstants.LOGINTYPE).equalsIgnoreCase(getString(R.string.two))) {
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                        }
                        GlobalMethods.userDetails(false, "", "", "", "", "", "", "", "", "", "", "", HomeScreen.this);
                        previousScreen(SignInScreen.class, true);
                    }


                    @Override
                    public void onCancelClick() {
                        startCamera();
                    }
                });
                break;
            case R.id.header_left_btn_lay:

                break;
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof CommonResponse) {
            CommonResponse mQRresponse = (CommonResponse) responseObj;

            if (mQRresponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                AppConstants.QR_STATUS = AppConstants.SUCCESS_CODE;
                AppConstants.QR_STATUS_TXT = mQRresponse.getMessage();
            } else {
                AppConstants.QR_STATUS = AppConstants.FAILURE_CODE;
                AppConstants.QR_STATUS_TXT = mQRresponse.getMessage();
            }

            releaseCamera(true);
            nextScreen(QRSuccScreen.class, true);
        }

    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {
        releaseCamera(false);

        if (errorCode.getCause() instanceof ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {

            if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
                mHomeSceendialog.dismiss();
            }

            mHomeSceendialog = DialogManager.showBasicAlertDialog(this, getString(R.string.app_name), getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {
                    startCamera();
                }

                @Override
                public void onYesClick() {

                }


                @Override
                public void onCancelClick() {

                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
                mHomeSceendialog.dismiss();
            }

            mHomeSceendialog = DialogManager.showBasicAlertDialog(this, getString(R.string.app_name),
                    getString(R.string.connection_timeout), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            startCamera();
                        }

                        @Override
                        public void onYesClick() {

                        }


                        @Override
                        public void onCancelClick() {

                        }
                    });


        } else {
            if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
                mHomeSceendialog.dismiss();
            }

            mHomeSceendialog = DialogManager.showBasicAlertDialog(this, getString(R.string.app_name),
                    getString(R.string.connection_timeout), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            startCamera();
                        }

                        @Override
                        public void onYesClick() {

                        }


                        @Override
                        public void onCancelClick() {

                        }
                    });

        }

    }


    //A safe way to get an instance of the Camera object.
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private void releaseCamera(boolean isExit) {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);

            if (isExit) {
                mCamera.release();
                mCamera = null;
            } else {
                barcodeScanned = true;
                mCamera.stopPreview();
            }
        }

    }

    private void startCamera() {
        if (barcodeScanned) {
            barcodeScanned = false;
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }


    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {


                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    String bridgeQRResult = sym.getData().trim();
                    barcodeScanned = true;
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    vibe.vibrate(100);
                    APIRequestHandler.getInstance().getQRReaderResponse(bridgeQRResult, HomeScreen.this);

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    @Override
    public void onBackPressed() {

        releaseCamera(false);
        if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
            mHomeSceendialog.dismiss();
        }

        mHomeSceendialog = DialogManager.showBasicBtnAlertDialog(this, getString(R.string.app_name), getString(R.string.app_exit), getString(R.string.yes), getString(R.string.no), new DialogMangerOkCallback() {
            @Override
            public void onOkClick() {

            }

            @Override
            public void onYesClick() {
                releaseCamera(true);
                finish();

            }


            @Override
            public void onCancelClick() {
                startCamera();
            }
        });
    }

    @Override
    public void onOkClick() {

    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onCancelClick() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
            mHomeSceendialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomeSceendialog != null && mHomeSceendialog.isShowing()) {
            mHomeSceendialog.dismiss();
        }
    }
}
