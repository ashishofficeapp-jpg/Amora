package com.amora.app.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.amora.app.R;
import com.amora.app.activity.EditProfile;
import com.amora.app.response.DeleteImageResponse;
import com.amora.app.response.UserListResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;

import java.util.List;

public class EditProfileBottomSheetDialog extends BottomSheetDialog implements ApiResponseInterface, View.OnClickListener {

    ApiManager apiManager;
    List<UserListResponse.UserPics> pic;
    int selectedPosition;
    EditProfile context;

    public EditProfileBottomSheetDialog(@NonNull EditProfile context, List<UserListResponse.UserPics> pic, int selectedPosition) {
        super(context);

        this.context = context;
        setContentView(R.layout.bottom_sheet_edit_profile_dialog);

        TextView check = findViewById(R.id.check);
        Button delete = findViewById(R.id.delete);
        TextView setPic = findViewById(R.id.set_profile_pic);
        Button cancel = findViewById(R.id.cancel);
        check.setOnClickListener(this);
        delete.setOnClickListener(this);
        setPic.setOnClickListener(this);
        cancel.setOnClickListener(this);

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
}