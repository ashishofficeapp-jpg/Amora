package com.amora.app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.amora.app.R;
import com.amora.app.activity.SearchUserActivity;
import com.amora.app.activity.WinnerActivity;
import com.amora.app.adapter.HomeMenuPagerAdapter;


public class UserMenuFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager tabViewpager;

    private HomeMenuPagerAdapter homeMenuPagerAdapter;
    private ImageView img_search, img_graph;

    public UserMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_blank, container, false);
        View view = inflater.inflate(R.layout.fragment_user_menu, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabViewpager = view.findViewById(R.id.tabViewpager);
        img_search = view.findViewById(R.id.img_search);
        img_graph = view.findViewById(R.id.img_graph);
        initViews();
        initListners();
        return view;
    }

    protected void initViews() {
        homeMenuPagerAdapter = new HomeMenuPagerAdapter(getChildFragmentManager(), getContext());
        tabViewpager.setAdapter(homeMenuPagerAdapter);
        tabLayout.setupWithViewPager(tabViewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(homeMenuPagerAdapter.getTabView(i));

        }
        homeMenuPagerAdapter.setOnSelectView(tabLayout, 0);


    }


    protected void initListners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                homeMenuPagerAdapter.setOnSelectView(tabLayout, position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                homeMenuPagerAdapter.setUnSelectView(tabLayout, position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        img_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WinnerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

    }




}