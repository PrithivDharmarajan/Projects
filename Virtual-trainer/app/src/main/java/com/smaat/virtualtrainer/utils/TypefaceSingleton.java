package com.smaat.virtualtrainer.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceSingleton {

    private static final TypefaceSingleton TYPEFACE = new TypefaceSingleton();
    private static Typeface mHelveticaNeueLightFont;
    private static Typeface mHelveticaNeue;

    private TypefaceSingleton() {
    }


    public static TypefaceSingleton getTypeface() {
        return TYPEFACE;
    }

    public Typeface getHelveticaNeueLightFont(Context context) {
        if (mHelveticaNeueLightFont == null) {
            mHelveticaNeueLightFont = Typeface.createFromAsset(context.getAssets(),
                    "fonts/HelveticaNeue-Light" +
                            ".otf");
        }
        return mHelveticaNeueLightFont;
    }

    public Typeface getHelveticaNeueFont(Context context) {
        if (mHelveticaNeue == null) {
            mHelveticaNeue = Typeface.createFromAsset(context.getAssets(),
                    "fonts/HelveticaNeue" +
                            ".otf");
        }
        return mHelveticaNeue;
    }

}
