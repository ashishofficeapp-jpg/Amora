package com.meetlive.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.meetlive.app.dialog.InsufficientCoins;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;
import com.meetlive.app.R;
import com.meetlive.app.adapter.UserLevelUpAdapter;
import com.meetlive.app.response.GetUserLevelResponse.GetLevelResponce;
import com.meetlive.app.response.GetUserLevelResponse.UserLevelResult;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.ArrayList;

public class LevelUpActivity extends AppCompatActivity implements ApiResponseInterface {
    private RoundedHorizontalProgressBar level_user_progressBar;
    private TextView tv_level_percentage,availableCoins;
    private Button btn_top_up;
    TextView tv_user_level, level_tv_current_level, level_tv_current_exp;
    ImageView level_iv_back_btn;
    ApiManager apiManager;
    RecyclerView user_level_recyclerview;
    ArrayList<UserLevelResult> userLevelArrayList;
    UserLevelUpAdapter userLevelUpAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_up);

        level_user_progressBar = findViewById(R.id.level_user_progressBar);
        btn_top_up = findViewById(R.id.btn_top_up);
        availableCoins = findViewById(R.id.availableCoins);
        tv_level_percentage = findViewById(R.id.tv_level_percentage);
        level_tv_current_exp = findViewById(R.id.level_tv_current_exp);
        tv_user_level = findViewById(R.id.tv_user_level);
        level_user_progressBar = findViewById(R.id.level_user_progressBar);
        level_tv_current_level = findViewById(R.id.level_tv_current_level);
        level_iv_back_btn = findViewById(R.id.level_iv_back_btn);
        user_level_recyclerview = findViewById(R.id.user_level_recyclerview);
        user_level_recyclerview.setNestedScrollingEnabled(false);

        init();
    }

    void init() {
        apiManager = new ApiManager(this, this);
        userLevelArrayList = new ArrayList<>();
        userLevelUpAdapter = new UserLevelUpAdapter(getApplicationContext(), userLevelArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        user_level_recyclerview.setLayoutManager(linearLayoutManager);
        user_level_recyclerview.setAdapter(userLevelUpAdapter);
        apiManager.getUserLevelHistory();
        apiManager.getWalletAmount();

        level_user_progressBar.animateProgress(3000, 0, 10);
        level_user_progressBar.setProgressColors(ResourcesCompat.getColor(getResources(),
                R.color.white, null), ResourcesCompat.getColor(getResources(), R.color.pinkNew, null));
        tv_level_percentage.setText("10%");

        level_iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        btn_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InsufficientCoins(LevelUpActivity.this, 2, Integer.parseInt(availableCoins.getText().toString()));
            }
        });
    }


    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_USER_LEVELS) {
            GetLevelResponce rsp = (GetLevelResponce) response;
            Log.e("inuserLevel", new Gson().toJson(rsp.getResult()));
            userLevelArrayList.addAll(rsp.getResult());
            Log.e("inuserLevelList", new Gson().toJson(userLevelArrayList));
            userLevelUpAdapter.notifyDataSetChanged();
        }
        if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            availableCoins.setText(String.valueOf(rsp.getResult().getTotal_point()));
        }

    }

}