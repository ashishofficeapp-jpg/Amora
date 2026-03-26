package com.meetlive.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.meetlive.app.R;
import com.meetlive.app.adapter.RankAdapter;
import com.meetlive.app.databinding.ActivityWinnerBinding;
import com.meetlive.app.response.TopReceiver.Result;
import com.meetlive.app.response.TopReceiver.TopHostData;
import com.meetlive.app.response.TopReceiver.TopReceiverResponse;
import com.meetlive.app.response.UserListResponseNew.FemaleImage;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WinnerActivity extends AppCompatActivity implements ApiResponseInterface {
    ActivityWinnerBinding binding;
    List<Result> list = new ArrayList<Result>();
    private ApiManager apiManager;
    RankAdapter rankAdapter;
    GridLayoutManager gridLayoutManager;
    int selectedValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_winner);
        apiManager = new ApiManager(this, this);
        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerViewTop.setLayoutManager(gridLayoutManager);
        binding.setClickListener(new EventHandler(this));
        binding.view2.setVisibility(View.GONE);

        apiManager.getWinnerList("top_receiver", "today");
        binding.view1.setVisibility(View.VISIBLE);
        binding.topGiverTabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110));
        binding.topGiverTabs.setBackgroundColor(getResources().getColor(R.color.white80));



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void backPage() {
            Log.e("winnerOnBack", " click here "+mContext);
            onBackPressed();
        }

    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.WINNER_USER) {
            TopReceiverResponse rsp = (TopReceiverResponse) response;
            Log.e("Winnerdata", new Gson().toJson(rsp.getResult()));

            if (rsp != null) {
                //list.clear();
                list = rsp.getResult();
                Log.e("Winnerdata", new Gson().toJson(list));

                if (list.size() > 0) {
                    Glide.with(this).load(list.get(0).getUserId().getProfileImages().get(0).getImageName()).apply(new RequestOptions()
                            .placeholder(R.drawable.default_profile).error(R.drawable.default_profile).circleCrop()).into(binding.ivTop1User);
                    binding.userName1.setText(list.get(0).getUserId().getName());
                    binding.userCoin1.setText(list.get(0).getTotalCoins());

                    Glide.with(this).load(list.get(1).getUserId().getProfileImages().get(0).getImageName()).apply(new RequestOptions()
                            .placeholder(R.drawable.default_profile).error(R.drawable.default_profile).circleCrop()).into(binding.ivTop2User);
                    binding.userName2.setText(list.get(1).getUserId().getName());
                    binding.userCoin2.setText(list.get(1).getTotalCoins());

                    Glide.with(this).load(list.get(2).getUserId().getProfileImages().get(0).getImageName()).apply(new RequestOptions()
                            .placeholder(R.drawable.default_profile).error(R.drawable.default_profile).circleCrop()).into(binding.ivTop3User);
                    binding.userName3.setText(list.get(2).getUserId().getName());
                    binding.userCoin3.setText(list.get(2).getTotalCoins());

                    Log.e("WinnerdataBefore", new Gson().toJson(list));
                    Log.e("winnerActivity", "winner1 " + new Gson().toJson(list.size()));

                    if (list.size() > 0) {
                        Log.e("winnerActivityNewB", "winner1 " + new Gson().toJson(list.get(0)));
                        Log.e("winnerActivitynewB", "winner1 " + new Gson().toJson(list.get(1)));
                        Log.e("winnerActivitynewB", "winner1 " + new Gson().toJson(list.get(2)));
                        list.remove(2);
                        list.remove(1);
                        list.remove(0);

                    }

                    Log.e("winnerActivityNewA", "winner1 " + new Gson().toJson(list.get(0)));
                    Log.e("winnerActivityNewA", "winner1 " + new Gson().toJson(list.get(1)));
                    Log.e("winnerActivityNewA", "winner1 " + new Gson().toJson(list.get(2)));
                    rankAdapter = new RankAdapter(WinnerActivity.this, list, "top_receiver");
                    binding.recyclerViewTop.setAdapter(rankAdapter);
                    rankAdapter.notifyDataSetChanged();

                    Log.e("WinnerdataAfter", new Gson().toJson(list));
                    Log.e("winnerActivity", "winner2 " + new Gson().toJson(list.size()));

                    binding.topReceiverTabs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedValue = 0;
                            apiManager.getWinnerList("top_receiver", "this_week");
                            rankAdapter.notifyDataSetChanged();
                            binding.view1.setVisibility(View.VISIBLE);
                            binding.view2.setVisibility(View.GONE);
                            binding.topReceiverTabs.setBackgroundColor(getResources().getColor(R.color.white));
                            binding.topGiverTabs.setBackgroundColor(getResources().getColor(R.color.white80));
                            binding.topReceiverTabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140));
                            binding.topGiverTabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110));
                        }
                    });

                    binding.topGiverTabs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedValue = 1;
                            apiManager.getWinnerList("top_giver", "today");
                            rankAdapter.notifyDataSetChanged();
                            binding.view1.setVisibility(View.VISIBLE);
                            binding.view2.setVisibility(View.GONE);
                            binding.topGiverTabs.setBackgroundColor(getResources().getColor(R.color.white));
                            binding.topReceiverTabs.setBackgroundColor(getResources().getColor(R.color.white80));
                            binding.topGiverTabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140));
                            binding.topReceiverTabs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110));
                        }
                    });
                }

                binding.tvToday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedValue == 0) {
                            apiManager.getWinnerList("top_receiver", "today");
                            rankAdapter.notifyDataSetChanged();
                            binding.view1.setVisibility(View.VISIBLE);
                            binding.view2.setVisibility(View.GONE);
                        } else {
                            apiManager.getWinnerList("top_giver", "today");
                            rankAdapter.notifyDataSetChanged();
                            binding.view1.setVisibility(View.VISIBLE);
                            binding.view2.setVisibility(View.GONE);
                        }

                    }
                });
                binding.tvThisWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedValue == 0) {
                            apiManager.getWinnerList("top_receiver", "this_week");
                            rankAdapter.notifyDataSetChanged();
                            binding.view2.setVisibility(View.VISIBLE);
                            binding.view1.setVisibility(View.GONE);
                        } else {
                            apiManager.getWinnerList("top_giver", "this_week");
                            rankAdapter.notifyDataSetChanged();
                            binding.view2.setVisibility(View.VISIBLE);
                            binding.view1.setVisibility(View.GONE);
                        }


                    }
                });


            }
        } else {
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}