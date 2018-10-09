package com.smaat.spark.main;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smaat.spark.utils.AppConstants;

import static com.smaat.spark.utils.GlobalMethods.getStringValue;
import static com.smaat.spark.utils.GlobalMethods.storeStringValue;


public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        storeStringValue(this, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        System.out.println("Device getToken()---" + getStringValue(this, AppConstants.DEVICE_ID) +
                "\nDevice getCreationTime()---" + FirebaseInstanceId.getInstance().getCreationTime() +
                "\nDevice getId()---" + FirebaseInstanceId.getInstance().getId());
    }

}