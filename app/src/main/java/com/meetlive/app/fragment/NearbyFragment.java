package com.meetlive.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.meetlive.app.R;
import com.meetlive.app.activity.MainActivity;
import com.meetlive.app.activity.VideoChatActivity;
import com.meetlive.app.adapter.HomeUserAdapter;
import com.meetlive.app.adapter.NearbyAdapter;
import com.meetlive.app.dialog.InsufficientCoins;
import com.meetlive.app.response.Call.ResultCall;
import com.meetlive.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.PaginationAdapterCallback;
import com.meetlive.app.utils.PaginationScrollListener;
import com.meetlive.app.utils.SessionManager;

import java.util.List;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class NearbyFragment extends Fragment implements ApiResponseInterface, PaginationAdapterCallback {
    private AutoScrollViewPager offerBanner;
    RecyclerView userList;
    NearbyAdapter nearbyAdapter;
    List<UserListResponse.Data> list;
    ApiManager apiManager;
    GridLayoutManager gridLayoutManager;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private TextView tv_popular, tv_newone;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ViewGroup viewGroup;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);


        viewGroup = view.findViewById(android.R.id.content);
        offerBanner = view.findViewById(R.id.offer_banner);
        userList = view.findViewById(R.id.user_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        userList.setLayoutManager(gridLayoutManager);

        tv_popular = view.findViewById(R.id.tv_popular);
        tv_newone = view.findViewById(R.id.tv_newone);

        apiManager = new ApiManager(getContext(), this);
        // apiManager.getPromotionBanner();

        userList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListNearbyNextPage(String.valueOf(currentPage), ""), 500);
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

        // showProgress();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mSwipeRefreshLayout.setRefreshing(false);
                currentPage = 1;
                isLastPage = false;
                list.clear();
                apiManager.getUserListNearby(String.valueOf(currentPage), "");
            }
        });

        //  markerForLang();
        checkFreeGift();

       /* tv_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_popular.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_popular.setTextSize(18);

                // Typeface typeface = getResources().getFont(R.font.lobster);
                //or to support all versions use
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_popular.setTypeface(typeface);

                tv_newone.setTextColor(getResources().getColor(R.color.black));
                tv_newone.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_newone.setTypeface(typeface);

                apiManager.getPopularList(String.valueOf(currentPage), "");

            }
        });

        tv_newone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_newone.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_newone.setTextSize(18);

                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lato_bold);
                tv_newone.setTypeface(typeface);


                tv_popular.setTextColor(getResources().getColor(R.color.black));
                tv_popular.setTextSize(14);

                typeface = ResourcesCompat.getFont(getContext(), R.font.lato_regular);
                tv_popular.setTypeface(typeface);


            }
        });

        tv_newone.performClick();
*/
        showProgress();
        apiManager.getUserListNearby(String.valueOf(currentPage), "");

        ((MainActivity) getActivity()).checkLocationSatae();

        return view;
    }

    private void checkFreeGift() {
        if (new SessionManager(getContext()).getGender().equals("male")) {
            //     apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
        } else {
            Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    //    lateinit var dialog1: Dialog
    private Dialog dialog1;

    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        try {

            if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
                RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;

                try {
                    try {
                        success = rsp.getSuccess();
                        remGiftCard = rsp.getResult().getRemGiftCards();
                        freeSeconds = rsp.getResult().getFreeSeconds();
                        if (remGiftCard > 0) {
                            apiManager.searchUser(profileId, "1");
                            return;
                        }
                    } catch (Exception e) {
                    }

                    if (new SessionManager(getContext()).getUserWallet() >= 20) {
                        apiManager.searchUser(profileId, "1");
                    } else {
                        new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
                    }
                } catch (Exception e) {
                    apiManager.searchUser(profileId, "1");
                }

            }
            if (ServiceCode == Constant.SEARCH_USER) {
                UserListResponse rsp = (UserListResponse) response;

                if (rsp != null) {
                    try {
                        int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                        int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                        if (onlineStatus == 1 && busyStatus == 0) {
                            // Check wallet balance before going to make a video call
                            //     apiManager.getWalletAmount();

                            if (callType.equals("video")) {
                                if (remGiftCard > 0) {
                                    apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                                } else {
                                    apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                            Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                                }
                            } else if (callType.equals("audio")) {
                    /*            apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                                        String.valueOf(System.currentTimeMillis()));
                    */
                            }
                        } else if (onlineStatus == 1) {
                            Toast.makeText(getContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                        } else if (onlineStatus == 0) {
                            Toast.makeText(getContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "User is Offline!", Toast.LENGTH_SHORT).show();
                        new SessionManager(getContext()).setOnlineState(0);
                        //     finish();
                    }
                }
            }

            if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
                ResultCall rsp = (ResultCall) response;

                //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
                int walletBalance = rsp.getResult().getPoints().getTotalPoint();
                //int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
                int talkTime = walletBalance * 1000;
                //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                int canCallTill = talkTime - 2000;


                Intent intent = new Intent(getContext(), VideoChatActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
                intent.putExtra("ID", profileId);
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());

                if (remGiftCard > 0) {
                    int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                    intent.putExtra("AUTO_END_TIME", newFreeSec);
                    intent.putExtra("is_free_call", "true");
                } else {
                    intent.putExtra("AUTO_END_TIME", canCallTill);
                    intent.putExtra("is_free_call", "false");
                }
                intent.putExtra("receiver_name", hostName);
                intent.putExtra("converID", "convId");

                intent.putExtra("receiver_image", hostImage);
                startActivity(intent);

            }

            if (ServiceCode == Constant.USER_LIST) {
                UserListResponse rsp = (UserListResponse) response;

                mSwipeRefreshLayout.setRefreshing(false);

                list = rsp.getResult().getData();
                TOTAL_PAGES = rsp.getResult().getLast_page();
                if (list.size() > 0) {
                    nearbyAdapter = new NearbyAdapter(getActivity(), NearbyFragment.this, "dashboard", NearbyFragment.this);
                    // userList.setItemAnimator(new DefaultItemAnimator());
                    userList.setAdapter(nearbyAdapter);

                    // Shuffle Data

                    // Collections.shuffle(list);

                    // Set data in adapter
                    nearbyAdapter.addAll(list);

                    if (currentPage < TOTAL_PAGES) {
                        nearbyAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }

                consentReminder();
            }

            if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
                UserListResponse rsp = (UserListResponse) response;

                mSwipeRefreshLayout.setRefreshing(false);

                nearbyAdapter.removeLoadingFooter();
                isLoading = false;

                List<UserListResponse.Data> results = rsp.getResult().getData();

                // Shuffle Data
                //  Collections.shuffle(results);

                list.addAll(results);


                nearbyAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) nearbyAdapter.addLoadingFooter();
                else isLastPage = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void retryPageLoad() {
        //   apiManager.getUserListNextPage(String.valueOf(currentPage), "");
    }

    void resetPages() {
        // Reset Current page when refresh data
        this.currentPage = 1;
        this.isLastPage = false;
    }

    void consentReminder() {
        if (!new SessionManager(getContext()).getConsent()) {
            // new ConsentDialog(this);
        }
    }

    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private String callType = "", profileId = "", callRate = "", hostName = "", hostImage = "";
    private int userId;

    public void startVideoCall(String profileId, String callRate, int userId, String hostName, String hostImage) {
        callType = "video";
        this.profileId = profileId;
        this.callRate = callRate;
        this.userId = userId;
        this.hostName = hostName;
        this.hostImage = hostImage;
        apiManager.getRemainingGiftCardFunction();
    }


}
