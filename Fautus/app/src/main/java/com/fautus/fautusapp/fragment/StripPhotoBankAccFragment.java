package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sys on 17-May-17.
 */

public class StripPhotoBankAccFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.acc_num_edt)
    EditText mAccNumEdt;

    @BindView(R.id.routing_num_edt)
    EditText mRoutingNumEdt;

    @BindView(R.id.available_amt_txt)
    TextView mAvailableAmtTxt;

    @BindView(R.id.pending_amt_txt)
    TextView mPendingAmtTxt;

    private Dialog mBankDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_photo_bank_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.payment_info));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();

    }

    private void initView() {
        getAccountBal();
        mAvailableAmtTxt.setText(String.format(getResources().getString(R.string.available_bal), AppConstants.PHOTOGRAPHER_AVAILABLE_BAL_AMT));
        mPendingAmtTxt.setText(String.format(getResources().getString(R.string.pending_bal), AppConstants.PHOTOGRAPHER_PENDING_BAL_AMT));
    }

    /*View OnClick*/
    @OnClick({R.id.continue_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_lay:
                validateFields();
                break;
        }
    }

    /*Validate the edit text fields and then do API call */
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard();

        String accNumStr = mAccNumEdt.getText().toString().trim();
        String routingNumStr = mRoutingNumEdt.getText().toString().trim();

        if (accNumStr.isEmpty()) {
            mAccNumEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.acc_num_req), getString(R.string.acc_num_msg), StripPhotoBankAccFragment.this);
        } else if (routingNumStr.isEmpty()) {
            mRoutingNumEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.routing_num_req), getString(R.string.routing_num_msg), StripPhotoBankAccFragment.this);
        } else {
            /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                /*Strip API call*/
                createBankAccToken(accNumStr, routingNumStr);
            } else {
                /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);

            }
        }
    }

    private void createBankAccToken(String bankAccNumStr, String routingAccNumStr) {

        DialogManager.getInstance().showProgress(getActivity());
        Stripe stripe = new Stripe(getActivity());
        stripe.setDefaultPublishableKey(AppConstants.STRIP_PUBLISH_KEY);
        BankAccount bankAccount = new BankAccount(bankAccNumStr, AppConstants.STRIP_COUNTRY_CODE, AppConstants.STRIP_CURRENCY_CODE, routingAccNumStr);

        stripe.createBankAccountToken(bankAccount, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                DialogManager.getInstance().hideProgress();
                Log.e(AppConstants.TAG, error.getMessage());
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), error.getMessage(), StripPhotoBankAccFragment.this);
            }

            @Override
            public void onSuccess(com.stripe.android.model.Token token) {
                saveBankAcc(token.getId());
            }
        });
    }

    private void saveBankAcc(String tokenIDstr) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ParseAPIConstants.token, tokenIDstr);
        params.put(ParseAPIConstants.isNewDefault, true);
        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_addStripePhotographerExternalAccount, params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null && object != null) {
                    baseFragmentAlertDismiss(mBankDialog);
                    mBankDialog = DialogManager.getInstance().showPaymentTermsAlertPopup(getActivity(), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onYesClick() {
                            LinkedHashMap<String, Object> params = new LinkedHashMap<>();

                            ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_acceptStripePhotographerTOS, params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object object, ParseException e) {
                                    if (e == null && object != null) {
                                        ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());
                                    } else if (e != null) {
                                        try {
                                            JSONObject errorJsonObj = new JSONObject(e.getMessage());
                                            String messageStr = errorJsonObj.getString(getString(R.string.message));
                                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), StripPhotoBankAccFragment.this);

                                        } catch (Exception ex) {
                                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), StripPhotoBankAccFragment.this);
                                            Log.e(AppConstants.TAG, ex.getMessage());
                                        } }
                                }
                            });
                        }

                        @Override
                        public void onNoClick() {
                            baseFragmentAlertDismiss(mBankDialog);
                            ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());

                        }
                    });
                } else if (e != null) {
                    try {
                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                        String messageStr = errorJsonObj.getString(getString(R.string.message));
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), StripPhotoBankAccFragment.this);

                    } catch (Exception ex) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), StripPhotoBankAccFragment.this);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            }
        });

    }

    private void getAccountBal() {
        HashMap<String, Object> params = new HashMap<>();
        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_getStripePhotographerAccountBalance, params, new FunctionCallback<HashMap<String, String>>() {
            public void done(HashMap resObject, ParseException e) {
                if (e == null && resObject != null && getActivity() != null) {
                    try {
                        ArrayList<HashMap<String, String>> bankAccAvailableDetailsRes = (ArrayList<HashMap<String, String>>) resObject.get(ParseAPIConstants.available);
                        ArrayList<HashMap<String, String>> bankAccPendingDetailsRes = (ArrayList) resObject.get(ParseAPIConstants.pending);
                        String availableAmtStr = AppConstants.FAILURE_CODE, pendingAmtStr = AppConstants.FAILURE_CODE;
                        if (getActivity() != null && bankAccAvailableDetailsRes != null && bankAccAvailableDetailsRes.size() > 0) {
                            availableAmtStr = String.valueOf(bankAccAvailableDetailsRes.get(0).get(ParseAPIConstants.amount));
                        }
                        if (getActivity() != null && bankAccPendingDetailsRes != null && bankAccPendingDetailsRes.size() > 0) {
                            pendingAmtStr = String.valueOf(bankAccPendingDetailsRes.get(0).get(ParseAPIConstants.amount));
                        }
                        AppConstants.PHOTOGRAPHER_AVAILABLE_BAL_AMT = NumberUtil.afterTwoPointVal(String.valueOf((Double.valueOf(NumberUtil.afterTwoPointVal(availableAmtStr)) / 100d)));
                        AppConstants.PHOTOGRAPHER_PENDING_BAL_AMT = NumberUtil.afterTwoPointVal(String.valueOf((Double.valueOf(NumberUtil.afterTwoPointVal(pendingAmtStr)) / 100d)));

                        mAvailableAmtTxt.setText(String.format(getResources().getString(R.string.available_bal), AppConstants.PHOTOGRAPHER_AVAILABLE_BAL_AMT));
                        mPendingAmtTxt.setText(String.format(getResources().getString(R.string.pending_bal), AppConstants.PHOTOGRAPHER_PENDING_BAL_AMT));

                    } catch (Exception ex) {
                        if (getActivity() != null) {
                            mAvailableAmtTxt.setText(String.format(getResources().getString(R.string.available_bal), AppConstants.FAILURE_CODE));
                            mPendingAmtTxt.setText(String.format(getResources().getString(R.string.pending_bal), AppConstants.FAILURE_CODE));
                        }
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            }
        });
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseFragmentAlertDismiss(mBankDialog);
    }
}