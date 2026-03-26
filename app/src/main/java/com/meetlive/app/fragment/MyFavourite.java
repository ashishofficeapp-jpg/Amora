package com.meetlive.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.meetlive.app.R;
import com.meetlive.app.activity.ViewProfile;
import com.meetlive.app.adapter.FavouriteAdapter;
import com.meetlive.app.response.FavNew.FavNewData;
import com.meetlive.app.response.FavNew.FavNewResponce;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.PaginationScrollListenerLinear;
import com.meetlive.app.utils.RecyclerTouchListener;
import com.meetlive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFavourite extends Fragment implements ApiResponseInterface {

    RecyclerView userList;
    FavouriteAdapter favouriteAdapter;
    List<FavNewData> list;
    RelativeLayout placeholder;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    public MyFavourite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_favourite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeholder = getActivity().findViewById(R.id.placeholder);
        userList = getActivity().findViewById(R.id.fav_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        userList.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(getActivity(), list);
        userList.setAdapter(favouriteAdapter);

        mSwipeRefreshLayout = getActivity().findViewById(R.id.swipeToRefresh);

        userList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), userList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

               /* if (new SessionManager(getContext()).getGender().equals("male")) {*/
                    Intent intent = new Intent(getContext(), ViewProfile.class);
                    UserListResponse.Data result = new UserListResponse.Data();
                    result.setName(list.get(position).getFavorites().get(0).getName());
                    //result.setUsername(list.get(position).getFavorites().get(0).get);
                    result.setAbout_user(list.get(position).getFavorites().get(0).getAboutUser());
                    result.setCall_rate(list.get(position).getFavorites().get(0).getCallRate());
                    result.setDob(list.get(position).getFavorites().get(0).getDob());
                    result.setFavorite_by_you_count(1);
                    result.setProfile_id(list.get(position).getFavorites().get(0).getProfileId());
                    result.setId(list.get(position).getFavorites().get(0).getId());
                    result.setIs_online(list.get(position).getFavorites().get(0).getIsOnline());
                    result.setProfile_images(list.get(position).getFavorites().get(0).getProfileImages());

                    Bundle bundle = new Bundle();
                    //send data here to view profile page 14/5/21
                    bundle.putSerializable("id", list.get(position).getFavorites().get(0).getId());
                    bundle.putSerializable("profileId", list.get(position).getFavorites().get(0).getProfileId());
                    // bundle.putSerializable("user_data", result);
                    intent.putExtras(bundle);
                    startActivity(intent);
                /*} else {*/
                    /*Intent intent = new Intent(getContext(), EmployeeViewProfile.class);
                    intent.putExtra("recName", list.get(position).getFavorites().get(0).getName());
                    intent.putExtra("recId", list.get(position).getFavorites().get(0).getId() + "");
                    intent.putExtra("recPoints", list.get(position).getPoints() + "");
                    intent.putExtra("recImage", list.get(position).getFavorites().get(0).getProfileImages().get(0).getImage_name());
                    startActivity(intent);*/

               // }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //new ApiManager(getContext(), this).getFavList();
        new ApiManager(getContext(), this).getFavListNew("1");
        getActivity().registerReceiver(myReceiver, new IntentFilter("FBR"));


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                currentPage = 1;
                list.clear();
                new ApiManager(getApplicationContext(), MyFavourite.this).getFavListNew("1");
            }
        });
        userList.addOnScrollListener(new PaginationScrollListenerLinear(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (currentPage == TOTAL_PAGES) {

                } else {
                    isLoading = true;
                    currentPage += 1;
                    mSwipeRefreshLayout.setRefreshing(true);

                    new ApiManager(getApplicationContext(), MyFavourite.this).getFavListNew(String.valueOf(currentPage));
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }
        });
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
       // new ApiManager(getContext(), this).getFavList();

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.FAVOURITE_LIST_NEW) {
            FavNewResponce rsp = (FavNewResponce) response;
            //Log.e("FAVOURITE_LIST", new Gson().toJson(response));
            mSwipeRefreshLayout.setRefreshing(false);

            if (rsp.getResult() != null) {
                list.clear();
                list.addAll(rsp.getResult().getData());
               Log.e("myfavouriteList", new Gson().toJson(list));
                if (list.size() == 0) {
                    userList.setVisibility(View.GONE);
                    placeholder.setVisibility(View.VISIBLE);
                } else {
                    placeholder.setVisibility(View.GONE);
                    isLoading = false;
                    TOTAL_PAGES = rsp.getResult().getLastPage();
                    favouriteAdapter.notifyDataSetChanged();
                }
            } else {
                placeholder.setVisibility(View.VISIBLE);
            }
        }
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("reload")) {
                currentPage = 1;
                list.clear();
                new ApiManager(getApplicationContext(), MyFavourite.this).getFavListNew("1");
            }
        }
    };
}