package com.education.librarymanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.education.librarymanagement.model.UserModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

/*
 * Class to hold common Functions and Constants used through out the Application
 *
 * */
public class Utility {
    public static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("E, dd MMM yyyy");
    public static SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int TAG_BOOK_NAME = 0;
    public static final int TAG_AUTHOR_NAME = 1;
    public static final int TAG_PUBLISHER_NAME = 2;
    public static final int TAG_ISBN_NO = 3;
    public static final int TAG_SCAN_ISBN = 4;

    private static UserModel mLoggedInUserDetails;

    public static String USER_DETAILS = "user";

    public static UserModel getLoggedInUser() {
        return mLoggedInUserDetails;
    }

    public static void setLoggedInUser(UserModel userDetails) {
        mLoggedInUserDetails = userDetails;
    }

    public static void writeToSharedPrefs(Context ctx, String key, String data) {
        SharedPreferences.Editor prefsEdit = ctx.getSharedPreferences(key, Context.MODE_PRIVATE).edit();
        prefsEdit.putString(key, data);
        prefsEdit.commit();
    }

    public static String readFromSharedPrefs(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void saveUserDetails(Context ctx, UserModel userDetails) {
        writeToSharedPrefs(ctx, USER_DETAILS, new Gson().toJson(userDetails));
    }

    public static UserModel getUserDetails(Context ctx) {
        String data = readFromSharedPrefs(ctx, USER_DETAILS);

        return new Gson().fromJson(data, UserModel.class);
    }

    public static boolean isUserADMIN() {
        if (mLoggedInUserDetails != null) {
            return mLoggedInUserDetails.getRoleId() == 2;
        }
        return false;
    }
}