package com.calix.calixgigamanage.utils;

import java.util.regex.Pattern;

public class PatternMatcherUtil {

    /*to Check if the string is the mail ID or not*/
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*to Check if the string is the valid password or not*/
    public static boolean isPasswordValid(String password) {
        return Pattern.compile(AppConstants.PASS_WORD_PATTERN).matcher(password).matches();
    }

}
