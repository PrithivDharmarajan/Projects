package com.fautus.fautusapp.main;

import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        PreferenceUtil.storeStringValue(this, AppConstants.SEND_BIRD_DEVICE_ID,FirebaseInstanceId.getInstance().getToken());
    }
}