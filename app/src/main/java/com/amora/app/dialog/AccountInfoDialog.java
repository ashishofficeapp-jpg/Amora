package com.amora.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.amora.app.R;
import com.amora.app.activity.MainActivity;
import com.amora.app.databinding.DialogLoginInfoBinding;
import com.amora.app.response.LoginResponse;
import com.amora.app.retrofit.ApiClient;
import com.amora.app.retrofit.ApiInterface;
import com.amora.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInfoDialog extends Dialog implements View.OnClickListener {

    DialogLoginInfoBinding binding;
    String username, password;
    SessionManager sessionManager;
    Context context;

    public AccountInfoDialog(@NonNull Context context, String username, String password) {
        super(context);
        this.context = context;
        this.username = username;
        this.password = password;
        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_login_info, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.imgCloseDialog.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.img_closeDialog) {
            dismiss();

        } else if (id == R.id.btn_login) {
            username = binding.etUsername.getText().toString();
            password = binding.etPassword.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
        }
    }


    public void login(String email, String password) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.loginUser(email, password,3);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("loginResponce", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        LoginResponse rsp = (LoginResponse) response.body();
                        if (rsp.getResult() != null) {
                            sessionManager = new SessionManager(context);
                            //sessionManager.createLoginSession(username, password, rsp.getResult().getToken());
                        }

                        // Log.e("Login",user);
                        //sessionManager.createLoginSession(rsp);
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        dismiss();

                    }
                } else if (response.code() == 401) {
                      Log.e("errorResponce", response.body().getError());
                    Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(mContext, response.body().getError(), Toast.LENGTH_SHORT).show();

                }

                //      closeDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //    closeDialog();
                Log.e("loginResponceError", t.getMessage());
                Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

   /* public void loginUser() {
        sessionManager = new SessionManager(context);
        // Get username, password from EditText
        String user = binding.etUsername.getText().toString();
        String pass = binding.etPassword.getText().toString();

        // Check if username, password is filled
        if (user.trim().length() > 0 && pass.trim().length() > 0) {
            // For testing purpose username, password is checked with sample data
            //String user = username
            //String pass = password
            String username = user;
            String password = pass;
            if (username.equals(user) && password.equals(pass)) {
                // Creating user login session
                // Use user real data
                sessionManager.createLoginSession(username, password);
                // Staring MainActivity
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                dismiss();

            } else {
                // username / password doesn't match
                Toast.makeText(context, "Username/Password is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            // user didn't entered username or password
            // Show alert asking him to enter the details
            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }


    }*/


    /*@Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.LOGIN) {
            //Log.e("LoginLog", "In Action Area");
            LoginResponse rsp = (LoginResponse) response;
            if (rsp.getResult() != null) {
                sessionManager = new SessionManager(context);
                sessionManager.createLoginSession(username, password, rsp.getResult().getToken());
            }

            // Log.e("Login",user);
            //sessionManager.createLoginSession(rsp);
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            dismiss();


            //String user = binding.etUsername.getText().toString();
            // String pass = binding.etPassword.getText().toString();
            *//*if (user.trim().length() > 0 && pass.trim().length() > 0) {
                String username = user;
                String password = pass;
                if (username.equals(user) && password.equals(pass)) {
                    sessionManager.createLoginSession(username, password);
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    dismiss();

                } else {
                    Toast.makeText(context, "Username/Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show();
            }*//*

        }
    }*/
}