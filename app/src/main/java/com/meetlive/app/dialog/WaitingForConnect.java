package com.meetlive.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.databinding.DailogWaitingToConnectBinding;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

public class WaitingForConnect extends Dialog {

    DailogWaitingToConnectBinding binding;

    public WaitingForConnect(@NonNull Context context, String image_url, String userName) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        init(image_url, userName);
    }

    void init(String image_url, String userName) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dailog_waiting_to_connect, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        try {
            binding.callerName.setText(userName);

            Glide.with(getContext())
                    .load(image_url)
                    .apply(new RequestOptions().centerCrop())
                    .transform(new CropCircleWithBorderTransformation(6, getContext().getResources().getColor(R.color.white)))
                    .into(binding.callerPic);

        } catch (Exception e) {
        }
        show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent=new Intent("FBR-ENDTHIS");
        myIntent.putExtra("action","end");
        getContext().sendBroadcast(myIntent);
    }
}