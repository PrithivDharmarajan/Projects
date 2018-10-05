package com.bridgellc.bridge.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonModelResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
//import com.dropbox.chooser.android.DbxChooser;

import java.util.Locale;

public class DropboxUpload extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private Button mSignInBtn;
    private EditText mInputBoxEdt;
    private String mInputBox;
    private RelativeLayout mHeadeLay;
//    private DbxChooser mChooser;
    public static String item_ID, buyer_ID, payID,buyer_Name,buyer_Cmd,buyer_Rating, is_Approved;
    private ImageView mDropBoxImage;
    public static final int DBX_CHOOSER_REQUEST = 0;
    private static Dialog mDialog;
    private String fileURLType;
    private String fileStatus;
    private boolean isFirstcall = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_drop_box);
        initComponents();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mSignInBtn = (Button) findViewById(R.id.footer_btn);
        mHeadeLay = (RelativeLayout) findViewById(R.id.heade_lay);
        mHeadeLay.setVisibility(View.VISIBLE);

        mInputBoxEdt = (EditText) findViewById(R.id.input_box);
        mDropBoxImage = (ImageView) findViewById(R.id.drop_box_icon);
        mDropBoxImage.setOnClickListener(this);
//        mChooser = new DbxChooser(AppConstants.DROPBOX_APP_KEY);

        fileURLType = getString(R.string.two);
        fileStatus = AppConstants.FAILURE_CODE;

        mSignInBtn.setText(getString(R.string.next));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);


        if (is_Approved.equalsIgnoreCase(getString(R.string.one))) {

            mHeaderTxt.setText(getString(R.string.upload_preview_file).toUpperCase(Locale.ENGLISH));
            fileStatus = getString(R.string.two);
        } else {

            mHeaderTxt.setText(getString(R.string.upload_preview).toUpperCase(Locale.ENGLISH));
            fileStatus = getString(R.string.one);
        }

    }


    @Override
    public void onBackPressed() {
        finishScreen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_btn:
                mInputBox = mInputBoxEdt.getText().toString().trim();
                if (mInputBox.equalsIgnoreCase("")) {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.enter_url), this);
                } else {
//                    if (fileStatus.equalsIgnoreCase(getString(R.string.one)) || fileStatus.equalsIgnoreCase(getString(R.string.two))) {
                    APIRequestHandler.getInstance().getUploadFileResponse(buyer_ID, item_ID, mInputBox, fileURLType, fileStatus,payID, this);
//                    } else {
//                        if (isFirstcall == false) {
//                            isFirstcall = true;
//                            showOption(DropboxUpload.this);
//                        } else {
//                            DialogManager.showBasicAlertDialog(this, getString(R.string.upload_file_c), getString(R.string.plz_select_file), new DialogMangerOkCallback() {
//                                @Override
//                                public void onOkClick() {
//
//                                    showOption(DropboxUpload.this);
//                                }
//                            });
//                        }
//                    }
                }
                break;
            case R.id.drop_box_icon:
//                mChooser.forResultType(DbxChooser.ResultType.PREVIEW_LINK)
//                        .launch(DropboxUpload.this, DBX_CHOOSER_REQUEST);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
//                DbxChooser.Result result = new DbxChooser.Result(data);
//                Log.e("main", "Link to selected file: " + result.getLink());

                fileURLType = getString(R.string.one);
//                mInputBoxEdt.setText("" + result.getLink());

                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonModelResponse) {
            CommonModelResponse uploadres = (CommonModelResponse) responseObj;

            if (uploadres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


                DialogManager.showBasicAlertDialog(DropboxUpload.this, uploadres.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                        AppConstants.BANK_DETAILS = AppConstants.FAILURE_CODE;

                        if (fileStatus.equalsIgnoreCase(getString(R.string.one))) {
                            nextScreen(HomeScreenActivity.class, true);
                        } else {

                            RateBuySellScreen.mHeader = getString(R.string.ratebuyer);
                            AppConstants.RATING_ITEM_ID = item_ID;
                            AppConstants.RATING_USER_ID = buyer_ID;
                            AppConstants.RATING_USER_NAME = buyer_Name;

                            AppConstants.RATING_RATE = buyer_Rating;
                            AppConstants.RATING_CMD=buyer_Cmd;
                            nextScreen(RateBuySellScreen.class, false);

//                            Intent intent = new Intent(DropboxUpload.this, RateBuySellScreen.class);
//                            intent.putExtra("FriendId", buyer_ID);
//                            intent.putExtra("ItemId", item_ID);
//                            intent.putExtra("UserName", buyer_Name);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.slide_in_right,
//                                    R.anim.slide_out_left);


                        }

                    }
                });
            } else if (uploadres.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {
                AppConstants.BANK_ACC_DET_BACK_SCREEN = getString(R.string.four);
                nextScreen(PaymentHomeScreen.class, false);
            } else {
                DialogManager.showBasicAlertDialog(DropboxUpload.this, uploadres.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }
    }

    @Override
    public void onOkClick() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AppConstants.BANK_ACC_DET_BACK_SCREEN.equalsIgnoreCase(getString(R.string.four))) {
            AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;
            if (GlobalMethods.getStringValue(DropboxUpload.this, AppConstants.BANK_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
                APIRequestHandler.getInstance().getUploadFileResponse(buyer_ID, item_ID, mInputBox, fileURLType, fileStatus,payID, this);
            }
        } else {
            AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.FAILURE_CODE;

        }
    }

    //    private void showOption(Context context) {
//
//        mDialog = new Dialog(context);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.phot_selection);
//        mDialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
//        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wmlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        Button mPreviewBtn, mOrginalBtn, mCancelBtn;
//        TextView mAlertTitleTxt;
//
//        mPreviewBtn = (Button) mDialog.findViewById(R.id.from_camera);
//        mOrginalBtn = (Button) mDialog.findViewById(R.id.from_galery);
//        mCancelBtn = (Button) mDialog.findViewById(R.id.cancel);
//        mAlertTitleTxt = (TextView) mDialog.findViewById(R.id.alertTitle);
//        mPreviewBtn.setText(getString(R.string.upload_preview).toLowerCase());
//        mOrginalBtn.setText(context.getString(R.string.upload_orginal).toLowerCase());
//        mCancelBtn.setText(context.getString(R.string.cancel).toLowerCase());
//
//        mAlertTitleTxt.setText(context.getString(R.string.upload_txt));
//
//        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                fileStatus = getString(R.string.one);
//                APIRequestHandler.getInstance().getUploadFileResponse(buyer_ID, item_ID, mInputBox, fileURLType, fileStatus, DropboxUpload.this);
//            }
//        });
//        mOrginalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fileStatus = getString(R.string.two);
//                mDialog.dismiss();
//                APIRequestHandler.getInstance().getUploadFileResponse(buyer_ID, item_ID, mInputBox, fileURLType, fileStatus, DropboxUpload.this);
//            }
//        });
//        mCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isFirstcall = false;
//                mDialog.dismiss();
//            }
//        });
//        if (is_Approved.equalsIgnoreCase(getString(R.string.one))) {
//            mPreviewBtn.setBackgroundColor(getResources().getColor(R.color.light_blue_gray));
//            mPreviewBtn.setClickable(false);
//
////            mOrginalBtn.setClickable(true);
//        } else {
//
////            mOrginalBtn.setBackgroundColor(getResources().getColor(R.color.light_blue_gray));
////            mOrginalBtn.setClickable(false);
//
//            mPreviewBtn.setClickable(true);
//        }
//        mDialog.show();
//    }
}
