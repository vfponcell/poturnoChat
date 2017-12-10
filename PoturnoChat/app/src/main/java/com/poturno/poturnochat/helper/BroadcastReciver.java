package com.poturno.poturnochat.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vitor on 10/12/2017.
 */

public class BroadcastReciver {

    public static boolean verifyNet(Context contexto){

        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ( (netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()) )
            return true;
        else {
            return false;
        }
    }
}
