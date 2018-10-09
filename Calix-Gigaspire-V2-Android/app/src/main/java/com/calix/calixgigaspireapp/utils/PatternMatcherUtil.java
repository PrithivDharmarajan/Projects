package com.calix.calixgigaspireapp.utils;

public class PatternMatcherUtil {

    /*to Check if the string is the mail ID or not*/
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



}
