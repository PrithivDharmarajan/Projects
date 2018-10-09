package com.smaat.ipharma.main;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.GlobalMethods;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is also called
	 * when the InstanceID token is initially generated, so this is where
	 * you retrieve the token.
	 */
	// [START refresh_token]
	@Override
	public void onTokenRefresh() {
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d("iPharma", "Refreshed token: " + refreshedToken);
		// TODO: Implement this method to send any registration to your app's servers.

		System.out.println("Token Id:"+ refreshedToken);
		GlobalMethods.storeValuetoPreference(getApplicationContext(),
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id, refreshedToken);

		GlobalMethods.storeValuetoPreference(getApplicationContext(),
				GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.pref_device_reg_status, false);

		GlobalMethods
				.storeValuetoPreference(getApplicationContext(),
						GlobalMethods.BOOLEAN_PREFERENCE,
						AppConstants.pref_device_id_changed,
						AppConstants.pref_deviceReg_Id
								.equalsIgnoreCase(refreshedToken) ? false
								: true);
		//sendRegistrationToServer(refreshedToken);
	}

}