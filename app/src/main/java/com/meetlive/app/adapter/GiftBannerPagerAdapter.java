package com.meetlive.app.adapter;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.meetlive.app.activity.InboxDetails;
import com.meetlive.app.fragment.GiftFragment;
import com.meetlive.app.response.GiftImageModel;
import com.meetlive.app.response.gift.Gift;

import java.util.ArrayList;
import java.util.List;

public class GiftBannerPagerAdapter extends FragmentStateAdapter {

    List<Gift> giftImageModelArrayList;
    int giftPerFragment = 8;

    public GiftBannerPagerAdapter(Fragment fragment, List<Gift> giftImageModels) {
        super(fragment);
        giftImageModelArrayList = giftImageModels;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new GiftFragment();
        Bundle args = new Bundle();
        args.putInt(GiftFragment.ARG_POSITION, position);

        //args.putParcelableArrayList(GiftFragment.ARG_GIFT_LIST, giftImageModelArrayList);
       // args.putParcelable(GiftFragment.ARG_GIFT_LIST,  giftImageModelArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (giftImageModelArrayList != null) {
            int listSize = giftImageModelArrayList.size();
            count = listSize / giftPerFragment;
            if (listSize % giftPerFragment != 0) {
                count++;
            }
        }
        return count;
    }
}