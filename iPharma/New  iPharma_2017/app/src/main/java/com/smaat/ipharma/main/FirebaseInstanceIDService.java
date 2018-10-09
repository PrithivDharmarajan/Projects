package com.smaat.ipharma.main;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
//        storeStringValue(this, AppConstants.pref_deviceReg_Id, FirebaseInstanceId.getInstance().getToken());

        GlobalMethods.storeValuetoPreference(getApplicationContext(),
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        System.out.println(
                "\nDevice getCreationTime()---" + FirebaseInstanceId.getInstance().getCreationTime() +
                        "\nDevice getId()---" + FirebaseInstanceId.getInstance().getId());

    }

}