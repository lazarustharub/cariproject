package id.project.lazarus.cariproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lazar on 5/14/2017.
 */

public class Util {
    public static boolean InternetConnectionStatus(Context context){
        boolean isConnectedToWifi = false;
        boolean isConnectedToMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null){
            if(netInfo.isAvailable()){
                if(netInfo.isConnected()){
                    if(netInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        isConnectedToWifi = true;
                    }else if(netInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                        isConnectedToMobile = true;
                    }
                }
            }
        }

        return isConnectedToMobile || isConnectedToWifi;
    }

    public static boolean RequestChecker(JSONObject response){
        try {
            int status = response.getInt(Api.RESPONSE_CODE);
            return status == 200;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}
