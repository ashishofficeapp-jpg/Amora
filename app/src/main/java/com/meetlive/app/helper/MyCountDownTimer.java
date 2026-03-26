package com.meetlive.app.helper;

import android.content.Context;
import android.os.CountDownTimer;

import com.meetlive.app.utils.SessionManager;

public class MyCountDownTimer extends CountDownTimer {
    Context context;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {

       /* int progress = (int) (millisUntilFinished / 1000);
        Log.e("countProgress", progress + "");
   */
    }

    @Override
    public void onFinish() {
        new SessionManager(context).setOnlineState(0);
        // sessionManager.setOnlineState(0);
        //   Log.e("countEnd", "FINISH");
    }
}
