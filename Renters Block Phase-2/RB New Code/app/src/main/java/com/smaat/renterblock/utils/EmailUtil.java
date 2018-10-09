package com.smaat.renterblock.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailUtil {

    /*to Check if the string is the mail ID or not*/
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void sendMail(Context context, String mailToStr, String subStr, String shareMsgStr) {
        final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
        emailInt.setData(Uri.parse("mailto:" + mailToStr));
        emailInt.putExtra(Intent.EXTRA_SUBJECT, subStr);
        emailInt.putExtra(Intent.EXTRA_TEXT, shareMsgStr);
        context.startActivity(emailInt);
    }

}
