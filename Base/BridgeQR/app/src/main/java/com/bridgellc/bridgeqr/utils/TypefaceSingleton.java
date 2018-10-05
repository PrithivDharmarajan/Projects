package com.bridgellc.bridgeqr.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceSingleton {

        private static final TypefaceSingleton TYPEFACE = new TypefaceSingleton();
        private static Typeface mLight,mRegular;

        private TypefaceSingleton() {
        }


        public static TypefaceSingleton getTypeface() {
            return TYPEFACE;
        }

        public Typeface getLightFont(Context context) {
            if (mLight == null) {
                mLight = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Light" +
                                ".otf");
            }
            return mLight;
        }
    public Typeface getRegularFont(Context context) {
        if (mRegular == null) {
            mRegular = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Lato-Regular" +
                            ".otf");
        }
        return mRegular;
    }


}
