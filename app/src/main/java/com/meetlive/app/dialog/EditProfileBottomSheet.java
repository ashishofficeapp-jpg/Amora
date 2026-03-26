package com.meetlive.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meetlive.app.R;
import com.meetlive.app.activity.EditProfile;
import com.meetlive.app.response.DeleteImageResponse;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditProfileBottomSheet extends Dialog implements ApiResponseInterface, View.OnClickListener {

    ApiManager apiManager;
    List<UserListResponse.UserPics> pic;
    int selectedPosition;
    EditProfile context;

    public EditProfileBottomSheet(@NonNull EditProfile context, List<UserListResponse.UserPics> pic, int selectedPosition) {
        super(context);

        this.context = context;
        this.setContentView(R.layout.bottom_sheet_edit_profile);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //this.setCanceledOnTouchOutside(false);

        TextView check = findViewById(R.id.check);
        TextView delete = findViewById(R.id.delete);
        TextView setPic = findViewById(R.id.set_profile_pic);
        Button cancel = findViewById(R.id.cancel);
        check.setOnClickListener(this);
        delete.setOnClickListener(this);
        setPic.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (selectedPosition == 0) {
            delete.setVisibility(View.GONE);
        }
        this.pic = pic;
        this.selectedPosition = selectedPosition;
        apiManager = new ApiManager(getContext(), this);

        show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check:
                new PictureView(getContext(), pic, selectedPosition);
                dismiss();
                return;

            case R.id.delete:
                apiManager.deleteProfileImage(String.valueOf(pic.get(selectedPosition).getId()));
                return;

            case R.id.set_profile_pic:
                apiManager.setProfilePicture(String.valueOf(pic.get(selectedPosition).getId()));
                return;

            case R.id.cancel:
                dismiss();
                return;
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.DELETE_PICTURE) {
            DeleteImageResponse rsp = (DeleteImageResponse) response;
            if (!rsp.getResult().getMsg().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), rsp.getResult().getMsg(), Toast.LENGTH_SHORT).show();
                context.refreshData();
            }
        }
        if (ServiceCode == Constant.SET_PROFILE_PICTURE) {

            DeleteImageResponse.Result rsp = (DeleteImageResponse.Result) response;
            if (!rsp.getMsg().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), rsp.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // I only care if the event is an UP action
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.dismiss();
            // create a rect for storing the window rect
            Rect r = new Rect(0, 0, 0, 0);
            // retrieve the windows rect
            this.getWindow().getDecorView().getHitRect(r);
            // check if the event position is inside the window rect
            boolean intersects = r.contains((int) event.getX(), (int) event.getY());
            // if the event is not inside then we can close the dialog
            if (!intersects) {
                // close the dialog
                this.dismiss();
                // notify that we consumed this event
                return true;
            }
        }
        // let the system handle the event
        return super.onTouchEvent(event);
    }

}