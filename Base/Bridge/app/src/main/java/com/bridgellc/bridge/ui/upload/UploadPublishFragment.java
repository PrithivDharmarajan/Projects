package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadPublishFragment extends Fragment {

    private View view;
    private Button mPublishBtn;
    private ImageView mPublishSuccessImage;
    private TextView titleTv;
    private boolean isSelling = false;
    private boolean isRequesting = false;
    private boolean isSell = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_publish_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        mPublishBtn = (Button) view.findViewById(R.id.footer_btn);
        mPublishBtn.setText(getString(R.string.publish));
        mPublishSuccessImage = (ImageView) view.findViewById(R.id.offer);
        titleTv = (TextView) view.findViewById(R.id.title_tv);

        titleTv.setText(getActivity().getString(R.string.you_have_green));
        mPublishSuccessImage.setVisibility(View.INVISIBLE);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mPublishBtn.setTypeface(mLightFont);
        titleTv.setTypeface(mLightFont);


        if (UploadScreen.mEntity.isSelling == true) {
            isSell = true;
        } else {
            isSell = false;
        }

        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                UploadScreen.mEntity.payment_mode = GlobalMethods.getStringValue(getActivity(), AppConstants.PAYMENT_MODE);
                if (isSell == true) {


//                    if (GlobalMethods.getStringValue(getActivity(), AppConstants.PAYMENT_MODE).equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

//                        DialogManager.showPayModeAlertDialog(getActivity(), true, new DialogManagerModeCallback() {
//                            @Override
//                            public void onContinueClick(String conti) {
//                                UploadScreen.mEntity.payment_mode = conti;
//
//
//                                GlobalMethods.storeValuetoPreference(getActivity(),
//                                        GlobalMethods.STRING_PREFERENCE,
//                                        AppConstants.PAYMENT_MODE, conti);
//                    pub();

//                            }

//                            @Override
//                            public void onCancelClick(String cancel) {
//                                UploadScreen.mEntity.payment_mode = cancel;
//                                GlobalMethods.storeValuetoPreference(getActivity(),
//                                        GlobalMethods.STRING_PREFERENCE,
//                                        AppConstants.PAYMENT_MODE, cancel);
//                                pub();
//
//                            }
//                        });

//                    } else {
                    pub();
//                    }

//                    if (GlobalMethods.getStringValue(getActivity(), AppConstants.BANK_DETAILS).equalsIgnoreCase(getString(R.string.one))) {

//                    } else {
//                        isSelling = true;
//                        AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.SUCCESS_CODE;
//                        ((BaseFragmentActivity) getActivity()).nextScreen(BankAccDetails.class, false);
//                    }


                } else if (isSell == false) {
//                    if (GlobalMethods.getStringValue(getActivity(), AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
                    pub();
//                    } else {
//                        isRequesting = true;
//                        AppConstants.CARD_DET_BACK_SCREEN = AppConstants.SUCCESS_CODE;
//                        ((BaseFragmentActivity) getActivity()).nextScreen(AddPayCard.class, false);
//                    }
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        resumCall(getActivity());
    }

//    public void resumCall(Context mContext) {
//        if (isSelling == true) {
//            isSelling = false;
//            if (GlobalMethods.getStringValue(mContext, AppConstants.BANK_DETAILS).equalsIgnoreCase(mContext.getString(R.string.one))) {
//                pub();
//            }
//        }
//        if (isRequesting == true) {
//            isRequesting = false;
//            if (GlobalMethods.getStringValue(mContext, AppConstants.CARD_DETAILS).equalsIgnoreCase(mContext.getString(R.string.one))) {
//                pub();
//            }
//        }
//    }

    public void onRequestSuccess(Object responseObj) {

        CommonResponse mCommonResponse = (CommonResponse) responseObj;
        if (mCommonResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//            mPublishSuccessImage.setVisibility(View.VISIBLE);
//            mPublishBtn.setText(getString(R.string.done_btn));
//            titleTv.setText(getString(R.string.publish_success));


            UploadScreen.mEntity = new UploadEntityClass();

            ((BaseFragmentActivity) getActivity()).nextScreen(UploadPublishScreen.class, true);

        } else if (mCommonResponse.getResponse_code().equalsIgnoreCase(getString(R.string.two))) {

            DialogManager.showBasicAlertDialog(getActivity(),
                    mCommonResponse.getMessage(), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                            ((BaseFragmentActivity) getActivity()).nextScreen(HomeScreenActivity.class, true);
                        }
                    });
        } else {
            DialogManager.showBasicAlertDialog(getActivity(), mCommonResponse.getMessage(), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        }


    }


    private void pub() {
        if (mPublishSuccessImage.getVisibility() == View.VISIBLE) {
            goHomeScreen();
        } else {

            UploadPublishScreen.isTicket = UploadScreen.mEntity.category.equalsIgnoreCase(getActivity().getString(R.string.ticket)) ? true : false;

            if (UploadScreen.mEntity.itemId == null || UploadScreen.mEntity.itemId.equalsIgnoreCase("")) {
                UploadPublishScreen.mheaderTxt = getActivity().getString(R.string.upload_txt);
                APIRequestHandler.getInstance().uploadItemResponse(UploadScreen.mEntity, ((BaseFragmentActivity) getActivity()));


            } else {
                UploadPublishScreen.mheaderTxt = getActivity().getString(R.string.update_txt);
                APIRequestHandler.getInstance().updateItemResponse(UploadScreen.mEntity
                        .itemId, UploadScreen.mEntity, ((BaseFragmentActivity) getActivity()));
            }


        }
    }

    private void goHomeScreen() {
//        Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
//        startActivity(intent);
        ((BaseFragmentActivity) getActivity()).nextScreen(HomeScreenActivity.class, true);
    }
}
