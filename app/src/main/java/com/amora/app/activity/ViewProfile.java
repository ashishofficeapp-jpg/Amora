package com.amora.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.amora.app.R;
import com.amora.app.adapter.AlbumAdapterViewProfile;
import com.amora.app.adapter.GiftCountDisplayAdapter;
import com.amora.app.adapter.ProfilePagerAdapterViewProfile;
import com.amora.app.adapter.RateCountDisplayAdapter;
import com.amora.app.databinding.ActivityViewProfileBinding;
import com.amora.app.dialog.InsufficientCoins;
import com.amora.app.dialog.ReportDialog;
import com.amora.app.helper.NetworkCheck;
import com.amora.app.response.AgoraTokenResponse;
import com.amora.app.response.Call.ResultCall;
import com.amora.app.response.ChatRoom.RequestChatRoom;
import com.amora.app.response.ChatRoom.ResultChatRoom;
import com.amora.app.response.DisplayGiftCount.GiftCountResult;
import com.amora.app.response.DisplayGiftCount.GiftDetails;
import com.amora.app.response.DisplayGiftCount.Result;
import com.amora.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.amora.app.response.UserListResponse;
import com.amora.app.response.UserListResponseNew.FemaleImage;
import com.amora.app.response.UserListResponseNew.GetRatingTag;
import com.amora.app.response.UserListResponseNew.ResultDataNewProfile;
import com.amora.app.response.UserListResponseNew.UserListResponseNewData;
import com.amora.app.response.VoiceCall.VoiceCallResponce;
import com.amora.app.response.WalletBalResponse;
import com.amora.app.response.videoplay.VideoPlayResponce;
import com.amora.app.retrofit.ApiClientChat;
import com.amora.app.retrofit.ApiInterface;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfile extends AppCompatActivity implements ApiResponseInterface {
    private int isFavourite = 0;
    private boolean success;
    private int remGiftCard = 0;
    private String freeSeconds;
    int walletBalance;

    private NetworkCheck networkCheck;
    private String convId = "";
    SessionManager sessionManager;
    ApiManager apiManager;
    private int userId, callRate;
    private int hostId;
    ArrayList<ResultDataNewProfile> userData = new ArrayList<>();
    RecyclerView rv_giftshow;
    GiftCountDisplayAdapter giftCountDisplayAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<GiftDetails> giftDetailsArrayList;
    ArrayList<Result> resultArrayList;
    RecyclerView rv_tagshow;
    RecyclerView rv_albumShow;
    RateCountDisplayAdapter rateCountDisplayAdapter;
    ArrayList<GetRatingTag> ratingArrayList;
    String hostIdFemale, hostProfileID;
    AlbumAdapterViewProfile adapter_album;
    ActivityViewProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile);
        binding.setClickListener(new EventHandler(this));
        networkCheck = new NetworkCheck();
        sessionManager = new SessionManager(this);
        init();
        getPermission();
    }


    private void init() {

        apiManager = new ApiManager(this, this);
        hostIdFemale = String.valueOf(getIntent().getSerializableExtra("id"));
        hostProfileID = String.valueOf(getIntent().getSerializableExtra("profileId"));

        rv_giftshow = findViewById(R.id.rv_giftshow);
        rv_tagshow = findViewById(R.id.rv_rateShow);
        rv_albumShow = findViewById(R.id.rv_albumShow);
        giftDetailsArrayList = new ArrayList<>();
        resultArrayList = new ArrayList<>();
        ratingArrayList = new ArrayList<>();

        giftCountDisplayAdapter = new GiftCountDisplayAdapter(getApplicationContext(), giftDetailsArrayList, resultArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_giftshow.setLayoutManager(linearLayoutManager);
        rv_giftshow.setAdapter(giftCountDisplayAdapter);

        FlexboxLayoutManager flexLayout = new FlexboxLayoutManager(ViewProfile.this);
        rv_tagshow.setLayoutManager(flexLayout);
        rateCountDisplayAdapter = new RateCountDisplayAdapter(ViewProfile.this, ratingArrayList);
        rv_tagshow.setAdapter(rateCountDisplayAdapter);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_albumShow.setLayoutManager(linearLayoutManager);

        apiManager.getProfileData(String.valueOf(hostIdFemale), "");

    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void addToFav() {
            addRemoveFav();
            apiManager.doFavourite(userId);
            Log.e("newUserId", userId + "");
        }

        public void onBack() {
            onBackPressed();
        }

        public void gotoChatConversation() {
            try {
                //Here pass userId and callRate send data on InboxDetail activity. by Kalpesh Sir..
                Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
                intent.putExtra("receiver_id", String.valueOf(userData.get(0).getProfileId()));
                intent.putExtra("newParem", String.valueOf(userData.get(0).getId()));
                intent.putExtra("receiver_name", userData.get(0).getName());
                intent.putExtra("user_id", String.valueOf(userId));
                intent.putExtra("call_rate", String.valueOf(callRate));
                /*Intent intent = new Intent(ViewProfile.this, InboxDetails.class);
                intent.putExtra("receiver_id", String.valueOf(userData.getProfile_id()));
                intent.putExtra("receiver_name", userData.getName());*/
                if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                    intent.putExtra("receiver_image", "empty");
                } else {
                    intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
                }
                startActivity(intent);
            } catch (Exception e) {

            }
            new SessionManager(ViewProfile.this).isRecentChatListUpdateNeeded(true);
        }

        public void reportUser() {
            new ReportDialog(ViewProfile.this, String.valueOf(userId));
        }

    }

    public void addRemoveFav() {
        Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent);

        if (isFavourite == 0) {
            binding.nonFavourite.setText("Follow");
            binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
            isFavourite = 1;
        } else {
            binding.nonFavourite.setText("UnFollow");
            binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
            isFavourite = 0;
        }
    }

    private void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void openVideoChat(View view) {
        // Check user is online before make a call
        callType = "video";
        apiManager.getRemainingGiftCardFunction();

    }

    private String callType = "";

    public void openVoiceChat(View view) {
        // Check user is online before make a call
        callType = "audio";
        try {
            if (new SessionManager(getApplicationContext()).getUserWallet() > callRate) {
                apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
            } else {
                new InsufficientCoins(ViewProfile.this, 2, callRate);
            }
        } catch (Exception e) {
            apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
        }
    }

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(ViewProfile.this, 2, callRate);
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
            int balance = sessionManager.getUserWallet();
            Log.e("maleWallet", String.valueOf(balance));

            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;
            try {
                try {
                    success = rsp.getSuccess();
                    remGiftCard = rsp.getResult().getRemGiftCards();
                    freeSeconds = rsp.getResult().getFreeSeconds();
                    if (remGiftCard > 0) {
                        apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
                        return;
                    }
                } catch (Exception e) {
                }

                if (new SessionManager(getApplicationContext()).getUserWallet() >= callRate) {
                    apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
                } else {
                    new InsufficientCoins(ViewProfile.this, 2, callRate);
                }
            } catch (Exception e) {
                apiManager.searchUser(String.valueOf(userData.get(0).getProfileId()), "1");
            }

        }
        if (ServiceCode == Constant.GET_GIFT_COUNT) {
            GiftCountResult rsp = (GiftCountResult) response;

            try {
                resultArrayList.addAll(rsp.getResult());
                if (resultArrayList.size() == 0) {
                    rv_giftshow.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < rsp.getResult().size(); i++) {
                        giftDetailsArrayList.add(rsp.getResult().get(i).getGiftDetails());
                        // Log.e("receviedGiftfemale", new Gson().toJson(giftDetailsArrayList));
                    }
                    //giftDetailsArrayList.add(rsp.getResult().get(0).getGiftDetails());
                    giftCountDisplayAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
            }
        }
        if (ServiceCode == Constant.GENERATE_AGORA_TOKEN) {
            AgoraTokenResponse rsp = (AgoraTokenResponse) response;
            if (rsp.getResult().getNotification() != null && rsp.getResult().getNotification().getSuccess() == 1) {
                int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
                //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                int canCallTill = talkTime - 2000;

               /*Log.e("walletBalance", walletBalance + "");
                Log.e("callRate", userData.getCall_rate() + "");
                Log.e("talkTime", talkTime + "");
                Log.e("talkTime2", talkTime2 + "");
                Log.e("canCallTill", canCallTill + "");*/

                Intent intent = new Intent(ViewProfile.this, VideoChatActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getToken());
                intent.putExtra("ID", String.valueOf(userData.get(0).getProfileId()));
                intent.putExtra("UID", String.valueOf(userId));
                intent.putExtra("CALL_RATE", String.valueOf(userData.get(0).getCallRate()));
                intent.putExtra("UNIQUE_ID", rsp.getResult().getUnique_id());
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("receiver_name", userData.get(0).getName());
                intent.putExtra("converID", convId);

                if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                    intent.putExtra("receiver_image", "empty");
                } else {
                    intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
                }
                startActivity(intent);

            } else {
                Toast.makeText(this, "Server is busy, Please try again", Toast.LENGTH_SHORT).show();
            }


        } else if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            // if wallet balance greater than call rate , Generate token to make a call
            if (rsp.getResult().getTotal_point() >= callRate) {
                walletBalance = rsp.getResult().getTotal_point();
                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiManager.showDialog();
                            } catch (Exception e) {
                            }
                        }
                    }, 100);
                    RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf",
                            Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                            new SessionManager(getApplicationContext()).getUserName(),
                            "ProfilePhoto", "1", userData.get(0).getProfileId(),
                            userData.get(0).getName(), userData.get(0).getFemaleImages().get(0).getImageName(),
                            "2", 0, callRate, 0, 20, "",
                            "countrtStstic", String.valueOf(userId));
                    Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom, 3);
                    chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                        @Override
                        public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
                            // Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                            try {
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                            try {
                                if (!response.body().getData().getId().equals("")) {
                                    convId = response.body().getData().getId();
                                    apiManager.generateAgoraToken(userId, String.valueOf(System.currentTimeMillis()), convId);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                            // Log.e("onResponseChatRoom: ", t.getMessage());
                            try {
                                apiManager.generateAgoraToken(userId, String.valueOf(System.currentTimeMillis()), convId);
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                        }
                    });
                }

            } else {
                // Open Insufficient Coin popup
                new InsufficientCoins(ViewProfile.this, 2, callRate);
            }
        }
        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;
            Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
            //int talkTime2 = userData.getCall_rate() * 1000 * 60;
            //Minus 2 sec to prevent balance goes into minus
            int canCallTill = talkTime - 2000;
            Intent intent = new Intent(ViewProfile.this, VideoChatActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("ID", String.valueOf(userData.get(0).getProfileId()));
            intent.putExtra("UID", String.valueOf(userId));
            intent.putExtra("CALL_RATE", String.valueOf(userData.get(0).getCallRate()));
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());

            if (remGiftCard > 0) {
                int newFreeSec = Integer.parseInt(freeSeconds) * 1000;
                intent.putExtra("AUTO_END_TIME", newFreeSec);
                intent.putExtra("is_free_call", "true");
            } else {
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("is_free_call", "false");
            }
            intent.putExtra("receiver_name", userData.get(0).getName());
            intent.putExtra("converID", convId);

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }
            startActivity(intent);

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
                                apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), convId, callRate,
                                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                            } else {
                                apiManager.generateCallRequest(userId, String.valueOf(System.currentTimeMillis()), convId, callRate,
                                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                            }
                        } else if (callType.equals("audio")) {
                            apiManager.dailVoiceCallUser(String.valueOf(userData.get(0).getAudioCallRate()), String.valueOf(userId),
                                    String.valueOf(System.currentTimeMillis()));
                        }
                    } else if (onlineStatus == 1) {
                        Toast.makeText(this, userData.get(0).getName() + " is Busy", Toast.LENGTH_SHORT).show();

                    } else if (onlineStatus == 0) {
                        Toast.makeText(this, userData.get(0).getName() + " is Offline", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "User is Offline!", Toast.LENGTH_SHORT).show();
                    new SessionManager(getApplicationContext()).setOnlineState(0);
                    finish();
                }
            }


        }
        if (ServiceCode == Constant.PLAY_VIDEO) {
            try {
                VideoPlayResponce rsp = (VideoPlayResponce) response;
                if (rsp != null) {
                    //    Log.e("getVideoForProfile", new Gson().toJson(response));
                    binding.cvVideo.setVisibility(View.VISIBLE);
                    String videourl = ((VideoPlayResponce) response).getResult().get(0).getVideoName();
                    //Log.e("vvURL", videourl);
                    Uri uri = Uri.parse(videourl);
                    binding.vvVideo.setVideoURI(uri);
                    binding.vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {

                            binding.searchLoader.setVisibility(View.GONE);

                            mediaPlayer.setVolume(0f, 0f);
                            binding.vvVideo.start();

                        }
                    });

                    binding.vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.stop();
                            binding.cvVideo.setVisibility(View.GONE);
                            binding.vvVideo.setVisibility(View.GONE);
                        }
                    });
                }
            } catch (Exception e) {

            }

        }

        if (ServiceCode == Constant.GENERATE_VOICE_CALL_TOKEN) {
            VoiceCallResponce rsp = (VoiceCallResponce) response;
            walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / userData.get(0).getCallRate() * 1000 * 60;
            //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus
            int canCallTill = talkTime - 2000;
            Intent intent = new Intent(ViewProfile.this, VoiceChatViewActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("UID", String.valueOf(userId));
            //intent.putExtra("CALL_RATE", String.valueOf(userData.getCall_rate()));
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());
            intent.putExtra("AUTO_END_TIME", canCallTill);
            intent.putExtra("receiver_name", userData.get(0).getName());
            //   intent.putExtra("converID", convId);

            if (userData.get(0).getFemaleImages() == null || userData.get(0).getFemaleImages().size() == 0) {
                intent.putExtra("receiver_image", "empty");
            } else {
                intent.putExtra("receiver_image", userData.get(0).getFemaleImages().get(0).getImageName());
            }
            startActivity(intent);

        }
        //Show female profile data for male 10/5/21
        if (ServiceCode == Constant.GET_PROFILE_DATA) {
            try {
                //UserListResponse.Data userData;
                UserListResponseNewData rsp = (UserListResponseNewData) response;
                //userData = (ResultDataNewProfile) rsp.getResult();
                userData.addAll(rsp.getResult());

                isFavourite = userData.get(0).getFavoriteByYouCount();
                if (isFavourite == 0) {
                    binding.nonFavourite.setText("Follow");
                    binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_fallow_background);
                    isFavourite = 1;
                } else {
                    binding.nonFavourite.setText("UnFollow");
                    binding.nonFavourite.setBackgroundResource(R.drawable.viewprofile_offline_background);
                    isFavourite = 0;
                }

                String[] dob = userData.get(0).getDob().split("-");
                int date = Integer.parseInt(dob[0]);
                int month = Integer.parseInt(dob[1]);
                int year = Integer.parseInt(dob[2]);
                binding.detailsAge.setText("Age: " + getAge(year, month, date));

                userId = userData.get(0).getId();
                hostId = userData.get(0).getProfileId();
                apiManager.getVideoForProfile(String.valueOf(userId));
                apiManager.getGiftCountForHost(String.valueOf(userId));
                //call api getRateCountForHost 6/5/21 send host profile_id here
                apiManager.getRateCountForHost(String.valueOf(userId));
                callRate = userData.get(0).getCallRate();
                binding.userName.setText(userData.get(0).getName());
                //binding.userId.setText("ID : " + userData.get(0).getProfileId());
                binding.detailsPrice.setText(callRate + "/min");
                binding.userName.setText(userData.get(0).getName());
                //binding.detailsLanguage.setText("Hindi English");
                binding.detailsPrice.setText(callRate + "/min");
                binding.detailsCountrytext.setText(userData.get(0).getCity());

                ProfilePagerAdapterViewProfile adapter = new ProfilePagerAdapterViewProfile(this, userData.get(0).getFemaleImages(), true);
                binding.viewpager.setAdapter(adapter);

                List<FemaleImage> albumList = new ArrayList<>();
                albumList.addAll(userData.get(0).getFemaleImages());
                Log.e("listFemaleSizeIn", String.valueOf(albumList.size()));
                if (albumList.size() > 0) {
                    albumList.remove(0);

                    if (albumList.size() < 1) {
                        binding.liAlbum.setVisibility(View.GONE);
                    }
                }
                adapter_album = new AlbumAdapterViewProfile(ViewProfile.this, albumList, true);
                rv_albumShow.setAdapter(adapter_album);

                new TabLayoutMediator(binding.indicatorDot, binding.viewpager,
                        (tab, position) -> {
                            //tab.setText(" " + (position + 1));
                        }
                ).attach();

            } catch (Exception e) {

            }

        }
        //Show Rating for female 6/5/21
        if (ServiceCode == Constant.GET_RATING_COUNT) {
            UserListResponseNewData rsp = (UserListResponseNewData) response;
            // Log.e("inViewPeofileFR", new Gson().toJson(rsp.getResult()));
            ratingArrayList.addAll(rsp.getResult().get(0).getGetRatingTag());
            // Log.e("inViewPeofile", new Gson().toJson(ratingArrayList));
            String score = rsp.getResult().get(0).getRatingsAverage();
            if (TextUtils.isEmpty(score)) {
                //binding.tvRate.setText("Score 0");
                binding.tvRate.setVisibility(View.GONE);
            } else {
                binding.tvRate.setText("Score " + score);
            }
            rateCountDisplayAdapter.notifyDataSetChanged();
        }

    }

    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month - 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;
        return Integer.toString(ageInt);
    }
}
