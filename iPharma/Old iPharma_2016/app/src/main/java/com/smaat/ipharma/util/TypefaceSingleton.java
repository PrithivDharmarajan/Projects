package com.smaat.ipharma.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceSingleton {

	private static final TypefaceSingleton instance = new TypefaceSingleton();
	private static Typeface helvetica, helvetica_bold, helvetica_light,
			high_tower;

	private TypefaceSingleton() {
	}

	public static TypefaceSingleton getInstance() {
		return instance;
	}

	public Typeface getHelvetica(Context context) {
		if (helvetica == null) {
			helvetica = Typeface.createFromAsset(context.getAssets(),
					"fonts/Helvetica.otf");// Helvetica.otf

		}
		return helvetica;
	}

	public Typeface getHelveticaBold(Context context) {
		if (helvetica_bold == null) {
			helvetica_bold = Typeface.createFromAsset(context.getAssets(),
					"fonts/Helvetica-Bold.otf");// Helvetica-Bold.otf

		}
		return helvetica_bold;
	}

	public Typeface getHelveticaLight(Context context) {
		if (helvetica_light == null) {
			helvetica_light = Typeface.createFromAsset(context.getAssets(),
					"fonts/Helvetica-Light.otf");// Helvetica-Light.otf

		}
		return helvetica_light;
	}

	public Typeface getHighTower(Context context) {
		if (high_tower == null) {
			high_tower = Typeface.createFromAsset(context.getAssets(),
					"fonts/Htowert.otf");

		}
		return high_tower;
	}

}
