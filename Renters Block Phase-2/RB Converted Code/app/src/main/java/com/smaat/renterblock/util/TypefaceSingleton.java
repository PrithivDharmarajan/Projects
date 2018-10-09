package com.smaat.renterblock.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceSingleton {

	private static final TypefaceSingleton instance = new TypefaceSingleton();
	private static Typeface helvetica, helveticaBold, helveticaLight, signikaBold;

	private TypefaceSingleton() {
	}

	public static TypefaceSingleton getInstance() {
		return instance;
	}

	public Typeface getHelvetica(Context context) {
		if (helvetica == null) {
			helvetica = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeue.ttf");

		}
		return helvetica;
	}

	public Typeface getHelveticaBold(Context context) {
		if (helveticaBold == null) {
			helveticaBold = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeue-Bold.ttf");

		}
		return helveticaBold;
	}

	public Typeface getHelveticaLight(Context context) {
		if (helveticaLight == null) {
			helveticaLight = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeueLight.ttf");

		}
		return helveticaLight;
	}
	
	public Typeface getSignikaBold(Context context) {
		if (signikaBold == null) {
			signikaBold = Typeface.createFromAsset(context.getAssets(),
					"font/SignikaNegative-Bold.ttf");

		}
		return signikaBold;
	}

}
