package com.meetlive.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.meetlive.app.fragment.HomeFragment;
import com.meetlive.app.fragment.InboxFragment;
import com.meetlive.app.fragment.MyAccountFragment;
import com.meetlive.app.fragment.OnCamFragment;
import com.meetlive.app.fragment.UserMenuFragment;

import java.util.HashMap;
import java.util.Map;


public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    private final Map<Integer, Fragment> fragmentMap;

    //Constructor to the class
    public HomePagerAdapter(FragmentManager fm, int tabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Initializing tab count
        this.tabCount = tabCount;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                UserMenuFragment homeFragment = new UserMenuFragment();
                fragmentMap.put(0, homeFragment);
                return homeFragment;
            case 1:
                OnCamFragment onCamFragment = new OnCamFragment();
                fragmentMap.put(1, onCamFragment);
                return onCamFragment;
            case 2:
                InboxFragment inboxFragment = new InboxFragment();
                fragmentMap.put(2, inboxFragment);
                return inboxFragment;
            case 3:
                MyAccountFragment myAccountFragment = new MyAccountFragment();
                fragmentMap.put(3, myAccountFragment);
                return myAccountFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public Fragment getFragment(int pos) {
        if (fragmentMap == null) return null;
        return fragmentMap.get(pos);
    }
}
