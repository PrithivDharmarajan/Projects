package com.smaat.spark.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResetPwdFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.confirm_pwd_edt)
    EditText mConfirmPwdEdt;

    @BindView(R.id.pwd_valid_img)
    ImageView mPwdValidImg;

    @BindView(R.id.confirm_pwd_valid_img)
    ImageView mConfirmPwdValidImg;

    private UserDetailsEntity mUserDetailsRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_reset_pwd_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.change_pwd));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);

        mUserDetailsRes = GlobalMethods.getUserDetailsRes(getActivity());

        mPwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwdStr = mPwdEdt.getText().toString().trim();
                mPwdValidImg.setVisibility(pwdStr.length() > 7 && !pwdStr.equals(mUserDetailsRes.getPassword()) ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mConfirmPwdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();
                mConfirmPwdValidImg.setVisibility(confirmPwdStr.length() > 7 && confirmPwdStr.equals(mPwdEdt.getText().toString().trim()) ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mConfirmPwdEdt.requestFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });
//        mConfirmPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == 100) {
//                    validateFields();
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

    }

    @OnClick({R.id.change_pwd_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_pwd_btn:
                validateFields();
                break;
        }
    }

    private void validateFields() {
        String pwdStr = mPwdEdt.getText().toString().trim();
        String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();

        if (pwdStr.isEmpty()) {
            shakeAnimEdt(mPwdEdt);
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_pwd));
        } else if (pwdStr.length() < 8) {
            shakeAnimEdt(mPwdEdt);
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.pwd_length_valid));
        } else if (pwdStr.equals(mUserDetailsRes.getPassword())) {
            shakeAnimEdt(mPwdEdt);
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.old_pwd_same));
        } else if (confirmPwdStr.isEmpty()) {
            shakeAnimEdt(mConfirmPwdEdt);
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_confirm_pwd));
        } else if (!pwdStr.equals(confirmPwdStr)) {
            shakeAnimEdt(mConfirmPwdEdt);
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.enter_diff_pwd_confirm));
        } else {
            //Reset API Call
            LoginRegResetInputEntity loginRegResetInputEntity = new LoginRegResetInputEntity(AppConstants.API_UPDATE_PWD, AppConstants.PARAMS_UPDATE_PWD, GlobalMethods.getUserID(getActivity()), "", pwdStr);
            APIRequestHandler.getInstance().callUpdateUserNameAPI(loginRegResetInputEntity, this);

        }

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            CommonResponse userDetailRes = (CommonResponse) resObj;
            if (userDetailRes.getResponse_code().equals(AppConstants.SUCCESS_CODE)) {
                mUserDetailsRes.setPassword(mPwdEdt.getText().toString().trim());
                GlobalMethods.storeUserDetails(getActivity(), mUserDetailsRes);
                DialogManager.getInstance().showAlertPopup(getActivity(), userDetailRes.getMessage());
                getActivity().onBackPressed();
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), userDetailRes.getMessage());
            }

        }

    }


    @Override
    public void onYesClick() {

    }
}
