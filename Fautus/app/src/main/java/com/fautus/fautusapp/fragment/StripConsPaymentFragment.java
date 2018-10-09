package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 13-May-17.
 */

public class StripConsPaymentFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.user_name_txt)
    TextView mUserNameTxt;

    @BindView(R.id.card_input_widget)
    CardInputWidget mCardInputWidget;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_cons_payment_screen, container, false);
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

        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.add_card));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        initView();

    }

    private void initView() {

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

           /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            /*Contact photographer API call*/
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null && object.getString(ParseAPIConstants.username) != null) {
                        mUserNameTxt.setText(object.getString(ParseAPIConstants.username));
                    } else if (e != null) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), StripConsPaymentFragment.this);
                    }
                }
            });
        } else {
            /*Internet alert*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), StripConsPaymentFragment.this);
        }


        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Set authenticity strip vision*/
                Card stripCard = mCardInputWidget.getCard();
                if (stripCard == null) {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getResources().getString(R.string.invalid_data), StripConsPaymentFragment.this);
                } else {
                    createCardToken(stripCard);
                }
            }
        });
    }

    private void createCardToken(Card stripCard) {
        DialogManager.getInstance().showProgress(getActivity());
        Stripe stripe = new Stripe(getActivity(), AppConstants.STRIP_PUBLISH_KEY);
        stripe.createToken(stripCard, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                Log.e(AppConstants.TAG, error.getMessage());
                DialogManager.getInstance().hideProgress();
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), error.getMessage(), StripConsPaymentFragment.this);
            }

            @Override
            public void onSuccess(Token token) {
                saveCardDetails(token.getId());
            }
        });

    }

    private void saveCardDetails(final String tokenIDStr) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ParseAPIConstants.source, tokenIDStr);

        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_saveStripeCustomerPaymentSource, params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e == null) {
                    AppConstants.READY_TO_PAY_SOURCE = tokenIDStr;
                    getActivity().onBackPressed();
                } else {
                    try {
                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                        String messageStr = errorJsonObj.getString(getString(R.string.message));
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), StripConsPaymentFragment.this);

                    } catch (Exception ex) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), StripConsPaymentFragment.this);
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


}
