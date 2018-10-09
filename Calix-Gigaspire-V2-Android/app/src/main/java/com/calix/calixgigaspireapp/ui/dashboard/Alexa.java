package com.calix.calixgigaspireapp.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdEntity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdResponse;
import com.calix.calixgigaspireapp.output.model.AlexaRefreshTokenResponse;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.RouterAgentListResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Alexa extends BaseActivity {

    @BindView(R.id.alexa_parent_lay)
    ViewGroup mAlexaViewGroup;

    @BindView(R.id.alexa_header_bg_lay)
    RelativeLayout mAlexaHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_right_txt)
    TextView mHeaderRightTxt;

    private RequestContext mRequestContext;
    private String mAlexaTokenIdStr = "0", mAlexaAppIdStr = "1", mCodeVerifierStr = "", mClientIdStr = "",
            mClientSecretStr = "f1e8f309a51fd2ce04ba280552a9241f37fadab4d9da7c729ec9630a588e4c10", mRedirectURIStr = "", mAuthorizationCodeStr = "";
    private static final String CODE_CHALLENGE_METHOD = "S256";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestContext = RequestContext.create(this);
        mRequestContext.registerListener(new AuthorizeListenerImpl());

        setContentView(R.layout.ui_alexa);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRequestContext.onResume();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAlexaViewGroup);

        setHeaderView();

        alexaAppIdAPICall();

    }

    private void setHeaderView() {

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlexaHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size55);
                    int generalPaddingInt = getResources().getDimensionPixelSize(R.dimen.size10);
                    mAlexaHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Alexa.this)));
                    mAlexaHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Alexa.this), 0, 0);
                    mHeaderTxt.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Alexa.this), 0, 0);
                    mHeaderRightTxt.setPadding(generalPaddingInt, NumberUtil.getInstance().getStatusBarHeight(Alexa.this), generalPaddingInt, 0);
                }
            });
        }
    }

    private void setAlexa() {

        final JSONObject scopeData = new JSONObject();
        final JSONObject productInstanceAttributes = new JSONObject();

        try {
            productInstanceAttributes.put("deviceSerialNumber", AppConstants.ALEXA_FSN);
            scopeData.put("productInstanceAttributes", productInstanceAttributes);
            scopeData.put("clientID", "amzn1.application-oa2-client.32b69d30fbd741b08dc834d6c269041e");
            scopeData.put("clientSecret", mClientSecretStr);
            scopeData.put("productID", "GigaSpire");
            mCodeVerifierStr = generateCodeVerifier();
            String codeChallenge = generateCodeChallenge(mCodeVerifierStr, "S256");

            AuthorizationManager.authorize(new AuthorizeRequest.Builder(mRequestContext)
                    .addScope(ScopeFactory.scopeNamed("alexa:all", scopeData))
                    .forGrantType(AuthorizeRequest.GrantType.AUTHORIZATION_CODE)
                    .withProofKeyParameters(codeChallenge, CODE_CHALLENGE_METHOD)
                    .build());

        } catch (JSONException e) {
            Log.d(AppConstants.TAG, e.toString());
            // handle exception here
        }
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.header_right_txt, R.id.sign_in_amazon_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
            case R.id.header_right_txt:
                onBackPressed();
                break;
            case R.id.sign_in_amazon_btn:
                setAlexa();
                break;
        }
    }

    /*Alexa App API calls*/
    private void alexaAppIdAPICall() {
        APIRequestHandler.getInstance().alexaAppIdsAPICall(this);
    }

    /*Update Alexa App API calls*/
    private void updateAlexaAppIdAPICall() {
        APIRequestHandler.getInstance().updateAlexaAppIdAPICall(mAlexaAppIdStr, mAlexaTokenIdStr, mClientIdStr, AppConstants.ALEXA_ROUTER_ID, this);
    }

    private void alexaRefreshToken() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                APIRequestHandler.getInstance().alexaRefreshTokenAPICall(mClientIdStr, mClientSecretStr,
                        mRedirectURIStr, mCodeVerifierStr, mAuthorizationCodeStr, Alexa.this);
            }
        });
    }

    /*Agent APICall calls*/
    private void agentListAPICall() {
        APIRequestHandler.getInstance().routerAgentListAPICall(this);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof AlexaAppIdResponse) {
            AlexaAppIdResponse alexaAppIdResponse = (AlexaAppIdResponse) resObj;

            ArrayList<AlexaAppIdEntity> deviceArrList = alexaAppIdResponse.getApps();
            for (int posInt = 0; posInt < deviceArrList.size(); posInt++) {
                if (deviceArrList.get(posInt).getName().equalsIgnoreCase(getString(R.string.alexa))) {
                    mAlexaAppIdStr = deviceArrList.get(posInt).getId();
                    break;
                }
            }
//            setAlexa();

        } else if (resObj instanceof AlexaRefreshTokenResponse) {

            AlexaRefreshTokenResponse alexaRefreshTokenResponse = (AlexaRefreshTokenResponse) resObj;
            mAlexaTokenIdStr = alexaRefreshTokenResponse.getRefresh_token();
            updateAlexaAppIdAPICall();
        } else if (resObj instanceof CommonResponse) {
            agentListAPICall();
        } else if (resObj instanceof RouterAgentListResponse) {
            RouterAgentListResponse alexaAppIdResponse = (RouterAgentListResponse) resObj;
            if (alexaAppIdResponse.getApps().size() > 0) {
                ArrayList<AlexaAppIdEntity> deviceArrList = alexaAppIdResponse.getApps();
                boolean isAlexaBool = false;
                for (int posInt = 0; posInt < deviceArrList.size(); posInt++) {
                    if (deviceArrList.get(posInt).getName().equalsIgnoreCase(getString(R.string.alexa))) {
                        isAlexaBool = true;
                        break;
                    }
                }
                final boolean isAlexaFinalBool = isAlexaBool;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogManager.getInstance().showToast(Alexa.this, getString(isAlexaFinalBool ? R.string.alexa_enabled :
                                R.string.alexa_install_shortly));
                        onBackPressed();
                    }
                });

            } else {
                DialogManager.getInstance().showToast(Alexa.this, getString(R.string.alexa_install_shortly));
            }
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
                            if (resObj instanceof AlexaAppIdResponse)
                                alexaAppIdAPICall();
                            else if (resObj instanceof AlexaRefreshTokenResponse)
                                alexaRefreshToken();
                            else if (resObj instanceof CommonResponse)
                                updateAlexaAppIdAPICall();
                            else if (resObj instanceof RouterAgentListResponse)
                                agentListAPICall();
                        }
                    });
        }
    }


    private String generateCodeVerifier() {
        String randomOctetSequence = generateRandomOctetSequence();
        return base64UrlEncode(randomOctetSequence.getBytes());
    }

    private String generateRandomOctetSequence() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_.~".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private String generateCodeChallenge(String codeVerifier, String codeChallengeMethod) {
        String codeChallenge = "";
        if ("S256".equalsIgnoreCase(codeChallengeMethod)) {
            try {
                byte[] digest = MessageDigest.getInstance("SHA-256").digest(
                        codeVerifier.getBytes());
                codeChallenge = base64UrlEncode(
                        digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            // Fall back to code_challenge_method = "plain" codeChallenge = codeVerifier;
        }
        return codeChallenge;
    }

    private String base64UrlEncode(byte[] arg) {
        String s = new String(Base64.encode(arg, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
        s = s.split("=")[0]; // Remove any trailing '='s
        s = s.replace('+', '-'); // 62nd char of encoding
        s = s.replace('/', '_'); // 63rd char of encoding
        return s;
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }

    private class AuthorizeListenerImpl extends AuthorizeListener {

        @Override
        public void onSuccess(final AuthorizeResult authorizeResult) {

            mClientIdStr = authorizeResult.getClientId();
            mRedirectURIStr = authorizeResult.getRedirectURI();
            mAuthorizationCodeStr = authorizeResult.getAuthorizationCode();

            AuthorizationManager.signOut(getApplicationContext(), new Listener<Void, AuthError>() {
                @Override
                public void onSuccess(Void response) {
                    alexaRefreshToken();
                }

                @Override
                public void onError(AuthError authError) {
                    Log.d(AppConstants.TAG, authError.toString());
                    onBackPressed();
                }
            });
        }

        @Override
        public void onError(AuthError AuthError) {
            onBackPressed();
        }

        @Override
        public void onCancel(AuthCancellation authCancellation) {
            onBackPressed();
        }
    }


}