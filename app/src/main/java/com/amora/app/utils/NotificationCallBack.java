package com.amora.app.utils;

import com.google.firebase.messaging.RemoteMessage;

public interface NotificationCallBack {
    public void onGetMessage(RemoteMessage notificationData);
}
