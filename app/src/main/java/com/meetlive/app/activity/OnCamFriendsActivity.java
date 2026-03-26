package com.meetlive.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.meetlive.app.R;
import com.meetlive.app.databinding.ActivityOnCamFriendsBinding;
import com.meetlive.app.dialog.InsufficientCoins;
import com.meetlive.app.fragment.OnCamFragment;
import com.meetlive.app.response.AgoraTokenResponse;
import com.meetlive.app.response.Call.ResultCall;
import com.meetlive.app.response.ChatRoom.RequestChatRoom;
import com.meetlive.app.response.ChatRoom.ResultChatRoom;
import com.meetlive.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.retrofit.ApiClientChat;
import com.meetlive.app.retrofit.ApiInterface;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnCamFriendsActivity extends AppCompatActivity implements ApiResponseInterface {
    //field declaration
    ActivityOnCamFriendsBinding binding;
    String userId, userName, tokenUserId, converID;
    ApiManager apiManager;
    int walletBalance, callRate;
    String userImage;
    private int forChat = 0;


    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_cam_friends);

        Intent intent = getIntent();
        userId = intent.getStringExtra(OnCamFragment.USER_ID);
        userName = intent.getStringExtra(OnCamFragment.LIKED_GIRL_NAME);
        userImage = intent.getStringExtra(OnCamFragment.LIKED_GIRL_IMG_URL);
        tokenUserId = intent.getStringExtra("tokenUserId");

        binding.youTwoLikedEachOtherTxt.setText("You and " + userName + " liked each other.");

        if (!userImage.equals("")) {
            Glide.with(this)
                    .load(userImage)
                    .apply(new RequestOptions().placeholder(R.drawable.female_placeholder).error(R.drawable.female_placeholder))
                    .into(binding.backgroundImage);

            Glide.with(this).
                    load(userImage)
                    .apply(new RequestOptions().placeholder(R.drawable.female_placeholder_circular).error(R.drawable.female_placeholder_circular))
                    .apply(RequestOptions.circleCropTransform()).into(binding.femaleImage);
        }

        binding.videoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check user is online before make a call
                forChat = 0;
                apiManager.getRemainingGiftCardFunction();
              /*  try {
                    try {
                        if (remGiftCard > 0) {
                            apiManager.searchUser(tokenUserId, "1");
                            return;
                        }
                    } catch (Exception e) {
                    }
                    if (new SessionManager(getApplicationContext()).getUserWallet() > callRate) {
                        apiManager.searchUser(tokenUserId, "1");
                    } else {
                        new InsufficientCoins(OnCamFriendsActivity.this, 2, callRate);
                    }
                } catch (Exception e) {
                    apiManager.searchUser(tokenUserId, "1");
                }*/
            }
        });

        binding.sayHelloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(OnCamFriendsActivity.this, InboxDetails.class);
                forChat = 1;
                //apiManager.searchUser(tokenUserId, "1");
            }
        });

        apiManager = new ApiManager(this, this);
        createChatRoom();

    }

    private void createChatRoom() {
        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
        RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf", Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                new SessionManager(getApplicationContext()).getUserName(), new SessionManager(getApplicationContext()).getUserProfilepic(),
                "1", Integer.parseInt(tokenUserId), userName, userImage, "2", 0, callRate, 0, 20, "", "countrtStstic", userId);
        Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom,3);

        chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
            @Override
            public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
//                Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                try {
                    if (!response.body().getData().getId().equals("")) {
                        converID = response.body().getData().getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                //         Log.e("onResponseChatRoom: ", t.getMessage());
            }
        });

    }


    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(OnCamFriendsActivity.this, 2, callRate);
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;


            try {
                try {
                    success = rsp.getSuccess();
                    remGiftCard = rsp.getResult().getRemGiftCards();
                    freeSeconds = rsp.getResult().getFreeSeconds();
                    if (remGiftCard > 0) {
                        apiManager.searchUser(tokenUserId, "1");
                        return;
                    }
                } catch (Exception e) {
                }
                if (new SessionManager(getApplicationContext()).getUserWallet() >= callRate) {
                    apiManager.searchUser(tokenUserId, "1");
                } else {
                    new InsufficientCoins(OnCamFriendsActivity.this, 2, callRate);
                }
            } catch (Exception e) {
                apiManager.searchUser(tokenUserId, "1");
            }

        }
        if (ServiceCode == Constant.GENERATE_AGORA_TOKEN) {
            AgoraTokenResponse rsp = (AgoraTokenResponse) response;

            if (rsp.getResult().getNotification() != null && rsp.getResult().getNotification().getSuccess() == 1) {
                int talkTime = walletBalance / callRate * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                int canCallTill = talkTime - 2000;

                if (rsp.getResult() != null && !rsp.getResult().getToken().isEmpty()) {
                    Intent intent = new Intent(this, VideoChatActivity.class);
                    intent.putExtra("TOKEN", rsp.getResult().getToken());
                    intent.putExtra("ID", tokenUserId);
                    intent.putExtra("receiver_name", userName);
                    intent.putExtra("receiver_image", userImage);
                    intent.putExtra("UID", userId);
                    intent.putExtra("CALL_RATE", callRate);
                    intent.putExtra("converID", converID);
                    intent.putExtra("UNIQUE_ID", rsp.getResult().getUnique_id());
                    intent.putExtra("AUTO_END_TIME", canCallTill);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Server is busy, Please try again", Toast.LENGTH_SHORT).show();
            }


        } else if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;

            // if wallet balance greater than call rate , Generate token to make a call
            if (rsp.getResult().getTotal_point() >= callRate) {


                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf", Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                        new SessionManager(getApplicationContext()).getUserName(), new SessionManager(getApplicationContext()).getUserProfilepic(),
                        "1", Integer.parseInt(tokenUserId), userName, userImage, "2", 0, callRate, 0, 20, "", "countrtStstic", userId);
                Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom,3);

                chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                    @Override
                    public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
                        Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                        try {
                            if (!response.body().getData().getId().equals("")) {
                                if (rsp.getResult() != null) {
                                    converID = response.body().getData().getId();
                                    walletBalance = rsp.getResult().getTotal_point();

                                    if (forChat == 1) {
                                        Intent intent = new Intent(OnCamFriendsActivity.this, ChatActivity.class);
                                        intent.putExtra("receiver_id", tokenUserId);
                                        intent.putExtra("receiver_name", userName);
                                        intent.putExtra("tokenUserId", userId);
                                        intent.putExtra("callrate", String.valueOf(callRate));
                                        intent.putExtra("converID", converID);

                                        if (userImage == null) {
                                            intent.putExtra("receiver_image", "empty");
                                        } else {
                                            intent.putExtra("receiver_image", userImage);
                                        }
                                        startActivity(intent);
                                    } else {
                                        apiManager.generateAgoraToken(Integer.parseInt(userId), String.valueOf(System.currentTimeMillis()), response.body().getData().getId());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                        //         Log.e("onResponseChatRoom: ", t.getMessage());
                    }
                });


            } else {
                // Open Insufficient Coin popup
                new InsufficientCoins(OnCamFriendsActivity.this, 2, callRate);
            }
        }

        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;

            //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / callRate * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus
            int canCallTill = talkTime - 2000;

            Intent intent = new Intent(this, VideoChatActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("ID", tokenUserId);
            intent.putExtra("receiver_name", userName);
            intent.putExtra("receiver_image", userImage);
            intent.putExtra("UID", userId);
            intent.putExtra("CALL_RATE", String.valueOf(callRate));
            intent.putExtra("converID", converID);
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());
            if (remGiftCard > 0) {
                int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                intent.putExtra("AUTO_END_TIME", newFreeSec);
                intent.putExtra("is_free_call", "true");
            } else {
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("is_free_call", "false");
            }
            startActivity(intent);
        }


        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponse rsp = (UserListResponse) response;

            try {
                if (rsp != null) {
                    // Init call rate from here
                    callRate = rsp.getResult().getData().get(0).getCall_rate();

                    int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                    int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                    if (onlineStatus == 1 && busyStatus == 0) {
                        // Check wallet balance before going to make a video call
                        //    apiManager.getWalletAmount();
//                        apiManager.generateCallRequest(Integer.parseInt(userId), String.valueOf(System.currentTimeMillis()), converID, callRate);
                        if (remGiftCard > 0) {
                            apiManager.generateCallRequest(Integer.parseInt(userId), String.valueOf(System.currentTimeMillis()), converID, callRate,
                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));

                        } else {
                            apiManager.generateCallRequest(Integer.parseInt(userId), String.valueOf(System.currentTimeMillis()), converID, callRate,
                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                        }
                    } else if (onlineStatus == 1) {
                        Toast.makeText(this, userName + " is Busy", Toast.LENGTH_SHORT).show();

                    } else if (onlineStatus == 0) {
                        Toast.makeText(this, userName + " is Offline", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}