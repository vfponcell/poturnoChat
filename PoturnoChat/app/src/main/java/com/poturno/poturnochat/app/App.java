package com.poturno.poturnochat.app;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by Iago Belo on 05/10/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());
    }
}
