package com.smaat.renterblock.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;


public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        GlobalMethods.storeValuetoPreference(getApplicationContext(),
                GlobalMethods.STRING_PREFERENCE ,AppConstants.pref_deviceReg_Id,FirebaseInstanceId.getInstance().getToken());

    }
}