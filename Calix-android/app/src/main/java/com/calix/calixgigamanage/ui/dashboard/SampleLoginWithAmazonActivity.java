package com.calix.calixgigamanage.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SampleLoginWithAmazonActivity extends BaseActivity {

    private static final String TAG = SampleLoginWithAmazonActivity.class.getName();

    private RequestContext requestContext;
    private static final String CODE_CHALLENGE = "85:EF:64:C3:B2:C4:A2:8F:5C:87:34:5E:99:9B:5C:28:A1:61:EF:B5:A0:E9:E8:0B:5E:A3:DA:3E:1C:FB:99:67";
    private static final String CODE_CHALLENGE_METHOD = "S256";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContext = RequestContext.create(this);

        requestContext.registerListener(new AuthorizeListener() {
            /* Authorization was completed successfully. */
            @Override
            public void onSuccess(AuthorizeResult authorizeResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                fetchUserProfile();
            }

            /* There was an error during the attempt to authorize the application */
            @Override
            public void onError(AuthError authError) {
                Log.e(TAG, "AuthError during authorization", authError);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAuthToast("Error during authorization.  Please try again.");

                    }
                });
            }

            /* Authorization was cancelled before it could be completed. */
            @Override
            public void onCancel(AuthCancellation authCancellation) {
                Log.e(TAG, "User cancelled authorization");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAuthToast("Authorization cancelled");
                    }
                });
            }
        });


        setContentView(R.layout.ui_alexa);
        initializeUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String PRODUCT_ID = "elitetestandroid";
        String PRODUCT_DSN = "A1P63SL7HP47CD";
        JSONObject scopeData = new JSONObject();
        JSONObject productInstanceAttributes = new JSONObject();

        try {
            productInstanceAttributes.put("deviceSerialNumber", PRODUCT_DSN);
            scopeData.put("productInstanceAttributes", productInstanceAttributes);
            scopeData.put("productID", PRODUCT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Scope[] scopes1 = {ProfileScope.profile(), ProfileScope.postalCode()};
        Scope scopes = ScopeFactory.scopeNamed("alexa:all", scopeData);
        AuthorizationManager.getToken(this, new Scope[]{scopes}, new Listener<AuthorizeResult, AuthError>() {
            @Override
            public void onSuccess(AuthorizeResult result) {
                if (result.getAccessToken() != null) {
                    /* The user is signed in */
                    fetchUserProfile();
                } else {
                    /* The user is not signed in */
                }
            }

            @Override
            public void onError(AuthError ae) {
                /* The user is not signed in */
            }
        });
    }


    private void fetchUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {

            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
                final String name = user.getUserName();
                final String email = user.getUserEmail();
                final String account = user.getUserId();
                final String zipCode = user.getUserPostalCode();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateProfileData(name, email, account, zipCode);
                    }
                });
            }

            /* There was an error during the attempt to get the profile. */
            @Override
            public void onError(AuthError ae) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        setLoggedOutState();
                        String errorMessage = "Error retrieving profile information.\nPlease log in again";
                        Toast errorToast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                        errorToast.setGravity(Gravity.CENTER, 0, 0);
                        errorToast.show();
                    }
                });
            }
        });
    }

    private void updateProfileData(String name, String email, String account, String zipCode) {
        StringBuilder profileBuilder = new StringBuilder();
        profileBuilder.append(String.format("Welcome, %s!\n", name));
        profileBuilder.append(String.format("Your email is %s\n", email));
        profileBuilder.append(String.format("Your zipCode is %s\n", zipCode));
        final String profile = profileBuilder.toString();
        Log.d(TAG, "Profile Response: " + profile);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateProfileView(profile);
            }
        });
    }

    /**
     * Initializes all of the UI elements in the activity
     */
    private void initializeUI() {
        String PRODUCT_ID = "elitetestandroid";
        String PRODUCT_DSN = "A1P63SL7HP47CD";
        JSONObject scopeData = new JSONObject();
        JSONObject productInstanceAttributes = new JSONObject();

        try {
            productInstanceAttributes.put("deviceSerialNumber", PRODUCT_DSN);
            scopeData.put("productInstanceAttributes", productInstanceAttributes);
            scopeData.put("productID", PRODUCT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Scope[] scopes1 = {ProfileScope.profile(), ProfileScope.postalCode()};
        Scope scopes = ScopeFactory.scopeNamed("alexa:all", scopeData);
                AuthorizationManager.authorize(
                        new AuthorizeRequest.Builder(requestContext)
                                .addScopes(scopes1)
                                .build());


    }

    /**
     * Sets the text in the mProfileText {@link TextView} to the value of the provided String.
     *
     * @param profileInfo the String with which to update the {@link TextView}.
     */
    private void updateProfileView(String profileInfo) {
        Log.d(TAG, "Updating profile view");
        sysOut("Updating profile view");
    }





    private void showAuthToast(String authToastMessage) {
        Toast authToast = Toast.makeText(getApplicationContext(), authToastMessage, Toast.LENGTH_LONG);
        authToast.setGravity(Gravity.CENTER, 0, 0);
        authToast.show();
    }

}
