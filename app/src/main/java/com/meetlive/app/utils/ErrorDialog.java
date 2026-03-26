package com.meetlive.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.meetlive.app.R;

public class ErrorDialog extends Dialog {
    public Activity activity;
    String msg;

    public ErrorDialog(Activity activity, String msg) {
        super(activity);
        this.activity = activity;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog);


        Button dimiss_btn = (Button) findViewById(R.id.ok);
        TextView text = (TextView) findViewById(R.id.error_msg);
        text.setText(msg);
        dimiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}