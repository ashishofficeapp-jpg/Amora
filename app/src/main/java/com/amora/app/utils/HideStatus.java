package com.amora.app.utils;

import android.os.Build.VERSION;
import android.view.Window;

public class HideStatus {
    Window window;
    boolean darkText;

    public HideStatus(Window window, boolean darkText) {
        this.window = window;
        this.darkText = darkText;
        this.hideStatusBar(window, darkText);
    }

    protected void hideStatusBar(Window window, boolean darkText) {
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        int flag = 256;
        if (VERSION.SDK_INT >= 23 && darkText) {
            flag = 8192;
        }

        window.getDecorView().setSystemUiVisibility(flag | 1024);
    }
}
