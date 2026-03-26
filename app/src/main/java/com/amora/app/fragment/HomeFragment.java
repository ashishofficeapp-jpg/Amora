package com.amora.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.amora.app.R;
import com.amora.app.activity.VideoChatActivity;
import com.amora.app.adapter.HomeUserAdapter;
import com.amora.app.dialog.InsufficientCoins;
import com.amora.app.response.Call.ResultCall;
import com.amora.app.response.PaymentGatewayDetails.PaymentGatewayResponce;
import com.amora.app.response.ProfileDetailsResponse;
import com.amora.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.amora.app.response.UserListResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.PaginationAdapterCallback;
import com.amora.app.utils.PaginationScrollListener;
import com.amora.app.utils.SessionManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ApiResponseInterface, PaginationAdapterCallback {
    private List<UserListResponse.Data> list;
    RecyclerView userList;
    private HomeUserAdapter homeUserAdapter;
    GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ApiManager apiManager;
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        // sessionManager = new SessionManager(context);

        apiManager = new ApiManager(getContext(), this);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userList = view.findViewById(R.id.user_list);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        userList.setLayoutManager(gridLayoutManager);
        apiManager.getUserList(String.valueOf(currentPage), "");

        userList.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                showProgress();
                // mocking network delay for API call
                new Handler().postDelayed(() -> apiManager.getUserListNextPage(String.valueOf(currentPage), ""), 500);
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


        apiManager.getProfileDetails();
        // showProgress();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                isLastPage = false;
                list.clear();
                apiManager.getUserList(String.valueOf(currentPage), "");
            }
        });

        checkFreeGift();

        return view;

    }



    private void checkFreeGift() {
        if (new SessionManager(getContext()).getGender().equals("male")) {
            apiManager.getRemainingGiftCardDisplayFunction();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        apiManager.getPaymentData();
        //resetPages();
        //apiManager.getUserList(String.valueOf(currentPage), "");
        //Log.e("onlineState", new SessionManager(getContext()).getOnlineState() + "");
        if (new SessionManager(getContext()).getUserLoaddata().equals("yes")) {
            new SessionManager(getContext()).setUserLoaddataNo();
        }
        if (new SessionManager(getContext()).getOnlineState() == 0) {
            showProgress();
            new SessionManager(getContext()).setOnlineState(1);
        }

        // Log.e("onlineState2", new SessionManager(getContext()).getOnlineState() + "");

    }


    private Dialog dialog1;
    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
        } else {
            Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.USER_LIST) {
            UserListResponse rsp = (UserListResponse) response;
            mSwipeRefreshLayout.setRefreshing(false);
            list = rsp.getResult().getData();
            TOTAL_PAGES = rsp.getResult().getLast_page();
            Log.e("inHostdata", new Gson().toJson(rsp.getResult()));
            if (list.size() > 0) {
                homeUserAdapter = new HomeUserAdapter(getActivity(), HomeFragment.this, "dashboard", this);
                userList.setAdapter(homeUserAdapter);
                //Log.e("inuserLevel", new Gson().toJson(rsp.getResult()));
                homeUserAdapter.addAll(list);
                // Log.e("inuserLevelList", new Gson().toJson(list));
                if (currentPage < TOTAL_PAGES) {
                    homeUserAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
                homeUserAdapter.notifyDataSetChanged();
            }
            consentReminder();
        }

        if (ServiceCode == Constant.USER_LIST_NEXT_PAGE) {
            UserListResponse rsp = (UserListResponse) response;
            mSwipeRefreshLayout.setRefreshing(false);
            homeUserAdapter.removeLoadingFooter();
            isLoading = false;
            List<UserListResponse.Data> results = rsp.getResult().getData();
            list.addAll(results);
            homeUserAdapter.addAll(results);
            if (currentPage != TOTAL_PAGES) homeUserAdapter.addLoadingFooter();
            else isLastPage = true;
        }
        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;
            //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            int walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;

            //int talkTime2 = userData.getCall_rate() * 1000 * 60;
            //Minus 2 sec to prevent balance goes into minus
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

                if (new SessionManager(getContext()).getUserWallet() >= Integer.parseInt(callRate)) {
                    apiManager.searchUser(profileId, "1");
                } else {
                    new InsufficientCoins(getContext(), 2, Integer.parseInt(callRate));
                }
            } catch (Exception e) {
                apiManager.searchUser(profileId, "1");
            }
        }
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
            if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                new SessionManager(getContext()).setUserProfilepic(rsp.getSuccess().getProfile_images().get(0).getImage_name());
            }
        }
        try {
            if (ServiceCode == Constant.PAY_DETAILS) {
                PaymentGatewayResponce rsp = (PaymentGatewayResponce) response;
                new SessionManager(getContext()).setUserWall(rsp.getData().getBalance().getTotalPoint());
                new SessionManager(getContext()).setUserStriepKS(rsp.getData().getStripe().getPublishableKey(),
                        rsp.getData().getStripe().getSecretKey());
                new SessionManager(getContext()).setUserRazKS(rsp.getData().getRazorpay().getKeyId(),
                        rsp.getData().getRazorpay().getKeySecret());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponse rsp = (UserListResponse) response;
            if (rsp != null) {
                try {
                    int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                    int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                    if (onlineStatus == 1 && busyStatus == 0) {
                        // Check wallet balance before going to make a video call
                        //apiManager.getWalletAmount();

                        if (callType.equals("video")) {
                            if (remGiftCard > 0) {
                                apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                            } else {
                                apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), "0", Integer.parseInt(callRate),
                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                            }
                        } else if (callType.equals("audio")) {
                       /* apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                                        String.valueOf(System.currentTimeMillis()));  */

                        }
                    } else if (onlineStatus == 1) {
                        Toast.makeText(getContext(), hostName + " is Busy", Toast.LENGTH_SHORT).show();

                    } else if (onlineStatus == 0) {
                        Toast.makeText(getContext(), hostName + " is Offline", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "User is Offline!", Toast.LENGTH_SHORT).show();
                    new SessionManager(getContext()).setOnlineState(0);
                    //finish();
                }
            }
        }

        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;
            //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            int walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;
            //int talkTime2 = userData.getCall_rate() * 1000 * 60;
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


    }


    void logoutDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);

        tv_msg.setText("You have been logout. As your access token is expired.");

        closeDialog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setText("OK");
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            new SessionManager(getContext()).logoutUser();
            apiManager.getUserLogout();
            getActivity().finish();
        });
    }


    void consentReminder() {
        try {
            if (!new SessionManager(getContext()).getConsent()) {
                // new ConsentDialog(this);
            }
        }catch (Exception e){

        }

    }


    @Override
    public void retryPageLoad() {
        //apiManager.getUserListNextPage(String.valueOf(currentPage), "");
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

