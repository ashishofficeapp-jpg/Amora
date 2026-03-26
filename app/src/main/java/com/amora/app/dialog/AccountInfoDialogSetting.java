package com.amora.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.amora.app.R;
import com.amora.app.databinding.DialogAccountinfoSettingBinding;
import com.amora.app.databinding.DialogLoginInfoBinding;
import com.amora.app.utils.SessionManager;

public class AccountInfoDialogSetting extends Dialog implements View.OnClickListener {

    DialogAccountinfoSettingBinding binding;
    String username, password;
    SessionManager sessionManager;

    public AccountInfoDialogSetting(@NonNull Context context, String username, String password) {
        super(context);

        this.username = username;
        this.password = password;
        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_accountinfo_setting, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.imgCloseDialog.setOnClickListener(this);
        binding.btnOk.setOnClickListener(this);
        sessionManager = new SessionManager(getContext());
        try {
            binding.etUsername.setText("User Id: " + username);
            if (password == null) {
                String newpassword = sessionManager.getUserPassword();
                binding.etPassword.setText("Password: " + newpassword);
            } else {
                binding.etPassword.setText("Password: " + password);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        show();

        binding.setClickListener(new EventHandler(getContext()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_closeDialog:
                dismiss();
                break;

            case R.id.btn_ok:
                dismiss();
                break;
        }
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void changePassword() {
            dismiss();
        }
    }
}
