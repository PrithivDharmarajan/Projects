package com.smaat.jolt.ui;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smaat.jolt.R;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class SplashScreen extends BaseActivity {

	public String mImgLoad;
	private int _index = 1;
	private ImageView mImagView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		facebookHashKey();

		mImagView = (ImageView) findViewById(R.id.imageView);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					if (!(_index >= 44)) {
						Bitmap bmp = BitmapFactory
								.decodeStream(SplashScreen.this.getAssets()
										.open("splashAnimation/jolt-loading-"
												+ _index + ".png"));
						mImagView.setImageBitmap(bmp);
						_index++;
						handler.postDelayed(this, 100);
					} else {
						nextScreen();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch blocks
					Log.v("", e.getMessage());
				}

			}
		}, 100);

	}

	public void nextScreen() {
		if (GlobalMethods.isLoggedIn(SplashScreen.this)) {
			launchActivity(HomeScreen.class);
		} else if (GlobalMethods.isTutorialSeen(SplashScreen.this)) {
			launchActivity(SignInScreen.class);
		} else {

			GlobalMethods.storeValuetoPreference(SplashScreen.this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.TUTORIAL_SCREEN,
					AppConstants.IS_TUTORIAL_SCREEN);
			launchActivity(TutorialScreen.class);
		}
	}

	private void facebookHashKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					AppConstants.com_smaat_jolt, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest
						.getInstance(getString(R.string.SHA));
				md.update(signature.toByteArray());
				String hashCode = Base64.encodeToString(md.digest(),
						Base64.DEFAULT);
				System.out.println("Print the hashKey for Facebook :"
						+ hashCode);
				Log.i("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

}