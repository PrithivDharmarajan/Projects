package com.bridgellc.bridge.main;

import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class SSInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        GlobalMethods.storeValuetoPreference(getApplicationContext(),
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.DEVICE_ID, refreshedToken);
    }

}