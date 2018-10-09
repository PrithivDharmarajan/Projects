package com.calix.calixgigaspireapp.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdEntity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdResponse;
import com.calix.calixgigaspireapp.output.model.CalixAgentResponse;
import com.calix.calixgigaspireapp.output.model.CommonModuleResponse;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CalixIOTAgent extends BaseActivity {

    @BindView(R.id.settings_parent_lay)
    ViewGroup mSettingsViewGroup;

    @BindView(R.id.settings_header_bg_lay)
    RelativeLayout mSettingsHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.agent_btn)
    Button mAgentBtn;

    private String mRouterIdStr = "", mAlexaAppIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_calix_iot_agent);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mSettingsViewGroup);

        setHeaderView();

        routerMapAPICall();

    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.calix_iot_agent));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSettingsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mSettingsHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(CalixIOTAgent.this)));
                    mSettingsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(CalixIOTAgent.this), 0, 0);
                }
            });
        }

    }

    /*routerMapAPICall*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*Alexa App API calls*/
    private void alexaAppIdAPICall() {
        APIRequestHandler.getInstance().alexaAppIdsAPICall(this);
    }

    /*calixAgentTokenAPICall*/
    private void calixAgentTokenAPICall() {
        APIRequestHandler.getInstance().calixAgentTokenAPICall(mAlexaAppIdStr, mRouterIdStr, this);
    }

    /*calixAgentTokenAPICall*/
    private void installAgentTokenAPICall() {
        APIRequestHandler.getInstance().installRouterAgentAPICall(mAlexaAppIdStr, mRouterIdStr, this);
    }

    /*calixAgentTokenAPICall*/
    private void uninstallAgentTokenAPICall() {
        APIRequestHandler.getInstance().uninstallRouterAgentAPICall(mAlexaAppIdStr, mRouterIdStr, this);
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.agent_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.agent_btn:
                if (mAgentBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.enable_agent)))
                    installAgentTokenAPICall();
                else
                    uninstallAgentTokenAPICall();
                break;
        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            if (routerMapResponse.getRouters().size() > 0) {
                mRouterIdStr = routerMapResponse.getRouters().get(0).getRouterId();
                alexaAppIdAPICall();
            } else {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router), this);
            }
        } else if (resObj instanceof AlexaAppIdResponse) {
            AlexaAppIdResponse alexaAppIdResponse = (AlexaAppIdResponse) resObj;

            ArrayList<AlexaAppIdEntity> deviceArrList = alexaAppIdResponse.getApps();
            for (int posInt = 0; posInt < deviceArrList.size(); posInt++) {
                if (deviceArrList.get(posInt).getName().equalsIgnoreCase(getString(R.string.iothub))) {
                    mAlexaAppIdStr = deviceArrList.get(posInt).getId();
                    break;
                }
            }
            calixAgentTokenAPICall();
        } else if (resObj instanceof CalixAgentResponse) {
            CalixAgentResponse calixAgentResponse = (CalixAgentResponse) resObj;
            mAgentBtn.setText(getString(calixAgentResponse.getToken().isEmpty() ? R.string.enable_agent : R.string.disable_agent));
        } else if (resObj instanceof CommonModuleResponse) {
            mAgentBtn.setText(getString(R.string.disable_agent));
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.install_success), this);
        } else if (resObj instanceof CommonResponse) {
            mAgentBtn.setText(getString(R.string.enable_agent));
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.uninstall_success), this);
        }
    }


    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof RouterMapResponse)
                                routerMapAPICall();
                            else if (resObj instanceof AlexaAppIdResponse)
                                alexaAppIdAPICall();
                            else if (resObj instanceof CalixAgentResponse)
                                calixAgentTokenAPICall();
                            else if (resObj instanceof CommonModuleResponse)
                                installAgentTokenAPICall();
                            else if (resObj instanceof CommonResponse)
                                uninstallAgentTokenAPICall();
                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }


}