package com.example.collegeinfogap.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

    private static final String FILE_NAME = "user";

    public static void saveLogin(Context context,
                                 String username){

        SharedPreferences sp =
                context.getSharedPreferences(
                        FILE_NAME,
                        Context.MODE_PRIVATE);

        sp.edit()
                .putBoolean("login",true)
                .putString("username",username)
                .apply();

    }

    public static boolean isLogin(Context context){

        SharedPreferences sp =
                context.getSharedPreferences(
                        FILE_NAME,
                        Context.MODE_PRIVATE);

        return sp.getBoolean("login",false);

    }

    public static String getUsername(Context context){

        SharedPreferences sp =
                context.getSharedPreferences(
                        FILE_NAME,
                        Context.MODE_PRIVATE);

        return sp.getString("username","");

    }

    public static void logout(Context context){

        SharedPreferences sp =
                context.getSharedPreferences(
                        FILE_NAME,
                        Context.MODE_PRIVATE);

        sp.edit().clear().apply();

    }

}