package com.meetlive.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.meetlive.app.R;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backButton;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        init();
    }

    private void init() {
        backButton = findViewById(R.id.back_arrow);
        title = findViewById(R.id.heading);
        backButton.setOnClickListener(this);
    }

    protected void setToolbarTitle(String s) {
        title.setText(s);
    }

    protected void showToast(String mToastMsg) {
        Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
    }

    protected void backClick() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow:
                backClick();
                return;

        }
    }
}