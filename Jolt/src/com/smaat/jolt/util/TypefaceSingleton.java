package com.smaat.jolt.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceSingleton {

	private static final TypefaceSingleton TYPEFACE = new TypefaceSingleton();
	private static Typeface helveticaNeue;
	private static Typeface helveticaNeue_bold;
	private static Typeface helveticaNeue_italic;
	private static Typeface helveticaNeue_medium;
	private static Typeface corbel;
	private static Typeface kgblankspace;
	private static Typeface helveticaNeue_light;

	private TypefaceSingleton() {
	}

	public static TypefaceSingleton getTypeface() {
		return TYPEFACE;
	}

	public Typeface getHelveticaNeue(Context context) {
		if (helveticaNeue == null) {
			helveticaNeue = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeue.otf");

		}
		return helveticaNeue;
	}

	public Typeface getHelveticaNeueBold(Context context) {
		if (helveticaNeue_bold == null) {
			helveticaNeue_bold = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeue-Bold.otf");

		}
		return helveticaNeue_bold;
	}

	public Typeface getHelveticaNeueItalic(Context context) {
		if (helveticaNeue_italic == null) {
			helveticaNeue_italic = Typeface.createFromAsset(
					context.getAssets(), "font/HelveticaNeueItalic.otf");

		}
		return helveticaNeue_italic;
	}

	public Typeface getHelveticaNeueMedium(Context context) {
		if (helveticaNeue_medium == null) {
			helveticaNeue_medium = Typeface.createFromAsset(
					context.getAssets(), "font/HelveticaNeue-Medium.otf");

		}
		return helveticaNeue_medium;
	}

	public Typeface getHelveticaNeueLight(Context context) {
		if (helveticaNeue_light == null) {
			helveticaNeue_light = Typeface.createFromAsset(context.getAssets(),
					"font/HelveticaNeueLight.otf");

		}
		return helveticaNeue_light;
	}

	public Typeface getCorbel(Context context) {
		if (corbel == null) {
			corbel = Typeface.createFromAsset(context.getAssets(),
					"font/corbel.otf");

		}
		return corbel;
	}

	public Typeface getKGBlankSpace(Context context) {
		if (kgblankspace == null) {
			kgblankspace = Typeface.createFromAsset(context.getAssets(),
					"font/KGBlankSpaceSolid.otf");

		}
		return kgblankspace;
	}
}
