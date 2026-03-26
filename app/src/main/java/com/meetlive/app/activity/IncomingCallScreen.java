package com.meetlive.app.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

public class IncomingCallScreen extends AppCompatActivity implements View.OnClickListener {

    private Vibrator vib;
    private MediaPlayer mp;
    ImageView decline_call, accept_call;
    String token, unique_id, convId, userId, callerName, callerImage, callType;
    String userpoints, receiveraudiocallRate, receiverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_incoming_call_screen);

        TextView caller_name = findViewById(R.id.caller_name);
        decline_call = findViewById(R.id.decline_call);
        accept_call = findViewById(R.id.accept_call);
        decline_call.setOnClickListener(this);
        accept_call.setOnClickListener(this);

        token = getIntent().getStringExtra("token");
        callerName = getIntent().getStringExtra("caller_name");
        callerImage = getIntent().getStringExtra("caller_image");
        unique_id = getIntent().getStringExtra("UNIQUE_ID");
        convId = getIntent().getStringExtra("convId");
        userId = getIntent().getStringExtra("userId");

        callType = getIntent().getStringExtra("callType");
        caller_name.setText(callerName);

        if (callType.equals("audio")) {
            userpoints = getIntent().getStringExtra("userpoints");
            receiveraudiocallRate = getIntent().getStringExtra("receiveraudiocallRate");
            receiverid = getIntent().getStringExtra("receiverid");

        }

        if (!callerImage.equals("")) {
            Glide.with(getApplicationContext())
                    .load(callerImage)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new BlurTransformation(20, 4))
                    .into((ImageView) findViewById(R.id.background));

            Glide.with(getApplicationContext())
                    .load(callerImage)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new CropCircleWithBorderTransformation(6, getResources().getColor(R.color.white)))
                    .into((ImageView) findViewById(R.id.caller_pic));
        }
        mp = MediaPlayer.create(this, R.raw.let_me_love_you);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(500);
        mp.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_call:
                Intent intent = null;
                if (callType.equals("video")) {
                    intent = new Intent(this, VideoChatActivity.class);
                    intent.putExtra("TOKEN", token);
                    intent.putExtra("UNIQUE_ID", unique_id);
                    intent.putExtra("converID", convId);
                    intent.putExtra("ID", userId);
                    intent.putExtra("receiver_name", callerName);
                    intent.putExtra("receiver_image", callerImage);
                } else if (callType.equals("audio")) {
                    intent = new Intent(this, VoiceChatViewActivity.class);
                    intent.putExtra("TOKEN", token);
                    intent.putExtra("UNIQUE_ID", unique_id);
                    intent.putExtra("ID", userId);
                    intent.putExtra("userpoints", userpoints);
                    intent.putExtra("receiveraudiocallRate", receiveraudiocallRate);
                    intent.putExtra("receiverid", receiverid);

                }
                startActivity(intent);
                stopRingtone();
                finish();
                return;

            case R.id.decline_call:
                stopRingtone();
                return;

        }
    }

    void stopRingtone() {
        mp.stop();
        vib.cancel();
        finish();
    }
}