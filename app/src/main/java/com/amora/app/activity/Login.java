package com.amora.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.amora.app.R;
import com.amora.app.databinding.ActivityLoginBinding;
import com.amora.app.response.LoginResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.CommonMethod;
import com.amora.app.utils.Constant;
import com.amora.app.utils.HideStatus;
import com.amora.app.utils.SessionManager;

public class Login extends AppCompatActivity implements ApiResponseInterface {
    ActivityLoginBinding binding;
    TextView tv_sign, et_forgot_password;
    Button login_btn;
    EditText username, password;
    SessionManager session;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        //WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setClickListener(new EventHandler(this));

        session = new SessionManager(this);
        tv_sign = findViewById(R.id.tv_sign);
        et_forgot_password = findViewById(R.id.et_forgot_password);
        login_btn = findViewById(R.id.login_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        //Prevent auto open edittext
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        apiManager = new ApiManager(this, this);

        getLoginDetails();
    }

    private void getLoginDetails() {
        if (!session.getUserEmail().equals("null")) {
            username.setText(session.getUserEmail());
        }
        if (!session.getUserPassword().equals("null")) {
            password.setText(session.getUserPassword());
        }

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void onLoginClicked() {
            if (validateAllDetails()) {
                if (CommonMethod.isOnline(mContext)) {
                    apiManager.login(username.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(mContext, "Internet not connected !", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void onRegisterClicked() {

        }

        public void onForgotPasswordClicked() {

        }

    }


    private boolean validateAllDetails() {
        boolean checkFields = true;
        if (!validateUserName()) {
            checkFields = false;
        }
        if (!validatePassword()) {
            checkFields = false;
        }
        return checkFields;
    }

    private boolean validateUserName() {
        if (username.getText().toString().trim().length() < 10) {
            username.setError("Incorrect Username");
            requestFocus(username);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().length() < 6) {
            password.setError("Incorrect Password");
            requestFocus(password);
            return false;
        } else {
            //inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.LOGIN) {
            LoginResponse rsp = (LoginResponse) response;
            session.createLoginSession(rsp);
            new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
            session.setUserEmail(username.getText().toString());
            session.setUserPassword(password.getText().toString());
            // new SessionManager(this).setUserLocation("India");
            Intent intent = new Intent(this, MainActivity.class);
            finishAffinity();
            startActivity(intent);

        }
    }
}