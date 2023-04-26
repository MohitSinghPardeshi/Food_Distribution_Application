package com.example.fooddistribution.AndroidConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.fooddistribution.Models.UserModel;
import com.google.gson.Gson;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String USERMODEL= "userModel";
    static final String FOOD_IMG= "foodImage";
    static final String BIO= "bio";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserModel(Context ctx, UserModel model)
    {

        SharedPreferences.Editor prefsEditor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        prefsEditor.putString(USERMODEL, json);
        prefsEditor.commit();
    }

    public static UserModel getUserModel(Context ctx)
    {
        Gson gson = new Gson();
        String json = getSharedPreferences(ctx).getString(USERMODEL, "");
        UserModel obj = gson.fromJson(json, UserModel.class);
        return obj;
    }

    public static void setFoodImage(Context ctx, Uri uri)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        if(uri == null){
            editor.putString(FOOD_IMG, "");
            editor.commit();
        }else{
            editor.putString(FOOD_IMG, uri.toString());
            editor.commit();
        }

    }

    public static Uri getFoodImage(Context ctx)
    {
        String uristr = getSharedPreferences(ctx).getString(FOOD_IMG, "");
        if(uristr == "")return null;
        return Uri.parse(uristr);
    }

    public static void setBio(Context ctx, String bio)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(BIO, bio);
        editor.commit();
    }

    public static String getBio(Context ctx)
    {
        return getSharedPreferences(ctx).getString(BIO, "");
    }


}