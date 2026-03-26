package com.amora.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.amora.app.R;
import com.amora.app.adapter.SearchUserAdapter;
import com.amora.app.databinding.ActivitySearchUserBinding;
import com.amora.app.response.UserListResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.PaginationAdapterCallback;
import com.amora.app.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity implements ApiResponseInterface, TextWatcher, PaginationAdapterCallback {
    ActivitySearchUserBinding binding;
    List<UserListResponse.Data> list = new ArrayList<>();
    ApiManager apiManager;
    SearchUserAdapter searchUserAdapter;

    GridLayoutManager gridLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);

        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.searchList.setLayoutManager(gridLayoutManager);
        apiManager = new ApiManager(this, this);
        binding.searchEd.addTextChangedListener(this);


        binding.searchList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListNextPage(String.valueOf(currentPage)
                        , binding.searchEd.getText().toString()), 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        changeLoaderColor();


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        resetPages();
        apiManager.searchUser(s.toString(), String.valueOf(currentPage));
        //binding.placeholder.setVisibility(View.GONE);
        binding.icon.setVisibility(View.GONE);
        binding.tvSearch.setVisibility(View.GONE);
        binding.searchLoader.setVisibility(View.VISIBLE);
    }

    void changeLoaderColor() {
        binding.searchLoader.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                frameInfo -> new PorterDuffColorFilter(this.getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP)
        );
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponse rsp = (UserListResponse) response;

            if (rsp != null) {
                list.clear();
                list = rsp.getResult().getData();
                TOTAL_PAGES = rsp.getResult().getLast_page();
                if (list.size() > 0) {
                    searchUserAdapter = new SearchUserAdapter(SearchUserActivity.this, this);
                    binding.searchList.setItemAnimator(new DefaultItemAnimator());
                    binding.searchList.setAdapter(searchUserAdapter);
                    // Set data in adapter
                    searchUserAdapter.addAll(list);
                    if (currentPage < TOTAL_PAGES) {
                        searchUserAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            } else {
                clearSearch();
                //binding.placeholder.setVisibility(View.VISIBLE);
                binding.icon.setVisibility(View.VISIBLE);
                binding.tvSearch.setVisibility(View.VISIBLE);
            }
        }
        if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
            UserListResponse rsp = (UserListResponse) response;
            searchUserAdapter.removeLoadingFooter();
            isLoading = false;

            List<UserListResponse.Data> results = rsp.getResult().getData();
            list.addAll(results);
            searchUserAdapter.addAll(results);

            if (currentPage != TOTAL_PAGES) searchUserAdapter.addLoadingFooter();
            else isLastPage = true;
        }

        binding.searchLoader.setVisibility(View.GONE);
    }

    void clearSearch() {
        list.clear();
        if (searchUserAdapter != null) {
            searchUserAdapter.removeAll();
            searchUserAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void retryPageLoad() {
        apiManager.getUserListNextPage(String.valueOf(currentPage), binding.searchEd.getText().toString());
    }

    void resetPages() {
        // Reset Current page when refresh data
        this.currentPage = 1;
        this.isLastPage = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}