package com.example.myapplication.ui.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class sessionmanager {
    private static String TAG = sessionmanager.class.getName();
    public SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    private int PRE_MODE = 1;
    private static final String NAME = "android_demo";
    private static final String KEY_LOGIN = "islogin";
    private static String ID = "";

    @SuppressLint("WrongConstant")
    public sessionmanager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void SetLogin(boolean isLogin,String idsave,String token,String name){
        editor.putBoolean(KEY_LOGIN,isLogin);
        ID = idsave;
        editor.putString("idna",idsave);
        editor.putString("tokenna",token);
        editor.putString("namene",name);
        editor.commit();
    }
    public boolean Check(){
        return preferences.getBoolean(KEY_LOGIN,false);
    }
}
