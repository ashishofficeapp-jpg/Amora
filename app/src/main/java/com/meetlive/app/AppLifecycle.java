package com.meetlive.app;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.utils.SessionManager;

import io.agora.rtm.RtmClient;

public class AppLifecycle extends Application implements LifecycleObserver {
    private static Context appContext;
    public static boolean wasInBackground;
    public static RtmClient mRtmClient;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        wasInBackground = true;
        //FirebaseDatabase.getInstance().goOnline();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onMoveToBackground() {
        // app moved to background
        wasInBackground = false;
        new ApiManager(appContext).changeOnlineStatusBack(0);
        new SessionManager(appContext).setUserLoaddata();

        //  FirebaseDatabase.getInstance().goOffline();
    }


}