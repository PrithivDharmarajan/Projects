package com.smaat.virtualtrainer.main;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.GlobalMethods;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //Getting registration token and Store that Values
        System.out.println("Device Tocken---" + FirebaseInstanceId.getInstance().getToken());
        GlobalMethods.storeStringValue(this, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
    }

}