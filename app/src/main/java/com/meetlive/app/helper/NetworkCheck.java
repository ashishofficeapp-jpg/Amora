package com.meetlive.app.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {
    public boolean isNetworkAvailable(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected())
                isConnected = true;
            else if (mWifi.isConnected())
                isConnected = true;
            else
                isConnected = false;

        }

        return isConnected;
    }
}
