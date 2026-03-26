package com.meetlive.app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetlive.app.R;
import com.meetlive.app.adapter.GiftBannerPagerAdapter;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.gift.Gift;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class GiftPagerFragment extends Fragment implements ApiResponseInterface {
    private static GiftPagerFragment giftFragment;
    private ViewPager2 viewPager;
    private GiftBannerPagerAdapter pagerAdapter;
    private CircleIndicator3 circleIndicator3;
    private ApiManager apiManager;
    List<Gift> giftList ;

    public static Fragment getInstance() {
        if (giftFragment == null) {
            giftFragment = new GiftPagerFragment();
        }
        return giftFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        apiManager = new ApiManager(getContext(), this);
        View view = inflater.inflate(R.layout.fragment_gift_pager, container, false);
        apiManager.getGiftList();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setSaveEnabled(false);
        circleIndicator3 = view.findViewById(R.id.indicator);
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_GIFT_LIST) {
            ResultGift giftResponse = (ResultGift) response;

            if (giftResponse.getResult() != null) {
                giftList = giftResponse.getResult();
                Log.e("giftListHere", String.valueOf(giftList.size()));
                giftList.addAll(giftResponse.getResult());
                pagerAdapter = new GiftBannerPagerAdapter(this, giftList);
                viewPager.setAdapter(pagerAdapter);
                circleIndicator3.setViewPager(viewPager);
            } else {
                Toast.makeText(getContext(), "No Gift available", Toast.LENGTH_SHORT).show();
            }
        }


    }

}