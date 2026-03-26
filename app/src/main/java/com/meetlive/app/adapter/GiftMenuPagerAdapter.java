package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.meetlive.app.R;
import com.meetlive.app.fragment.GiftPagerFragment;
import java.util.HashMap;
import java.util.Map;
public class GiftMenuPagerAdapter extends FragmentStatePagerAdapter {
    private final Map<Integer, Fragment> fragmentMap;
    private final String[] tabTitles = new String[]{"Popular", "Lucky"};
    Context context;
    //Constructor to the class
    public GiftMenuPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        GiftPagerFragment fragment1 = new GiftPagerFragment();
        return fragment1;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        return v;
    }

    // I add two func here.
    public void setOnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        textView.setTextColor(context.getResources().getColor(R.color.pinkNew));
        textView.setTextSize(16f);
    }

    public void setUnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View view = tab.getCustomView();
        TextView textView = view.findViewById(R.id.textView);
        textView.setTextColor(context.getResources().getColor(R.color.gray_6));
        textView.setTextSize(12f);
    }
}