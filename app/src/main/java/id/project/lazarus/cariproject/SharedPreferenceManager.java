package id.project.lazarus.cariproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lazar on 7/21/2017.
 */

public class SharedPreferenceManager {
    private Context context;

    public static final String KEY_NOMERINDUK = "nomerinduk";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LOGIN_STATUS = "login_status";
    public static final String PREFNAME = "prefname";

    public SharedPreferenceManager(Context context){
        this.context = context;

    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context);
        return sharedPreferenceManager;
    }

    public void login(String nomerinduk, String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NOMERINDUK, nomerinduk);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_LOGIN_STATUS, true);
        editor.apply();
        editor.commit();

    }

    public String getNomerInduk(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFNAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NOMERINDUK,null);
    }

    public boolean isLogin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        return ((sharedPreferences.contains(KEY_LOGIN_STATUS)&& sharedPreferences.getBoolean(KEY_LOGIN_STATUS,false)));

    }
    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }
}
