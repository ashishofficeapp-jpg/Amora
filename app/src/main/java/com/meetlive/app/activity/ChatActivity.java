package com.meetlive.app.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.meetlive.app.R;
import com.meetlive.app.adapter.GiftAdapter;
import com.meetlive.app.adapter.MessageAdapter;
import com.meetlive.app.dialog.InsufficientCoins;
import com.meetlive.app.helper.ItemClickSupport;
import com.meetlive.app.helper.NetworkCheck;
import com.meetlive.app.helper.VisualizerView;
import com.meetlive.app.response.ActionButtonValue;
import com.meetlive.app.response.AgoraTokenResponse;
import com.meetlive.app.response.Call.ResultCall;
import com.meetlive.app.response.ChatPurchaseValidity;
import com.meetlive.app.response.ChatRoom.RequestChatRoom;
import com.meetlive.app.response.ChatRoom.ResultChatRoom;
import com.meetlive.app.response.Employee.RequestIdForEmployee;
import com.meetlive.app.response.Employee.ResultEmployeeId;
import com.meetlive.app.response.Friendship.RequestFriendShipStatus;
import com.meetlive.app.response.Friendship.ResultFriendShipStatus;
import com.meetlive.app.response.Report.ReportData;
import com.meetlive.app.response.Report.ResultReportIssue;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.coin.CoinPlan;
import com.meetlive.app.response.coin.ResultCoinPlan;
import com.meetlive.app.response.gift.Gift;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.response.gift.SendGiftRequest;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.response.message.Message;
import com.meetlive.app.response.message.Message_;
import com.meetlive.app.response.message.RequestAllMessages;
import com.meetlive.app.response.message.RequestMessageRead;
import com.meetlive.app.response.message.ResultMessage;
import com.meetlive.app.response.message.ResultMessageRead;
import com.meetlive.app.response.message.ResultSendMessage;
import com.meetlive.app.response.order.RequestPlaceOrder;
import com.meetlive.app.response.order.ResultPlaceOrder;
import com.meetlive.app.retrofit.ApiClient;
import com.meetlive.app.retrofit.ApiClientChat;
import com.meetlive.app.retrofit.ApiInterface;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.socketmodel.SocketSendMessage;
import com.meetlive.app.socketmodel.Socketuserid;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements PaymentResultListener, ApiResponseInterface {

    private String converID = "";
    private String reciverId = "";
    private String tokenUserId = "";
    private String reciverName = "";
    private String reciverProfilePic = "";
    private String recAge = "";
    private String recCounrty = "";
    private String stringAudioDuration = "";
    private String callRate = "";

    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private NetworkCheck networkCheck;
    private RecyclerView rv_gift;
    private GridLayoutManager gridLayoutManager;
    private GiftAdapter giftAdapter;
    private ArrayList<Gift> giftArrayList = new ArrayList<>();
    private ViewGroup viewGroup;
    RelativeLayout rl_cancelAudio;
    ImageView img_audio;
    private static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    private ArrayList<CoinPlan> coinPlanArrayList = new ArrayList<>();

    //audioRecord And Visual

    public static final String DIRECTORY_NAME_TEMP = "AudioTemp";
    public static final int REPEAT_INTERVAL = 40;

    VisualizerView visualizerView;

    private MediaRecorder recorder = null;

    File audioDirTemp;
    private boolean isRecording = false;


    private Handler handlerVisual; // Handler for updating the visualizer
    // private boolean recording; // are we curr

    private int inActivity = 0;
    private String userType = "0";
    private boolean isReceverInCall = false;

    LayoutInflater inflater;
    View layout;
    TextView tv;
    Toast toast;

    int purchasePlanStatus = -1;
    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        viewGroup = findViewById(android.R.id.content);

        getIntentData();
        initControls();
        //    initSocket();
        getEmployeeData();

        new ApiManager(this, this).getWalletAmount2();
        apiManager = new ApiManager(this, this);
        apiManager.isChatServicePurchased();

        loadLoader();
    }


    private void loadLoader() {
        Glide.with(getApplicationContext())
                .load(R.drawable.loader)
                .into((ImageView) findViewById(R.id.img_giftloader));
    }

    private void getEmployeeData() {
        Intent in = getIntent();
        Bundle bb = in.getExtras();
        if (networkCheck.isNetworkAvailable(this)) {
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            RequestIdForEmployee requestIdForEmployee = new RequestIdForEmployee(reciverId);

            Call<ResultEmployeeId> call = apiservice.getEmployeeDataByID(new SessionManager(getApplicationContext()).getUserToken(),
                    "default11",3, requestIdForEmployee);

            call.enqueue(new Callback<ResultEmployeeId>() {
                @Override
                public void onResponse(Call<ResultEmployeeId> call, Response<ResultEmployeeId> response) {
                    //   Log.e("onResponseUserinChat: ", new Gson().toJson(response.body()));
           /*         if (response.body().getCode() == 200) {
                        switch (response.body().getData().getOnlineStatus()) {
                            case 0:
                                isReceverInCall = false;

                                ((TextView) findViewById(R.id.tv_userstatus)).setText("offline");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorRedoffline));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline, 0, 0, 0);


                                break;
                            case 1:
                                isReceverInCall = false;

                                ((TextView) findViewById(R.id.tv_userstatus)).setText("online");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);


                                break;

                            case 2:
                                isReceverInCall = true;

                                ((TextView) findViewById(R.id.tv_userstatus)).setText("Busy");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorBusy));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_busy, 0, 0, 0);


                                break;
                        }
                    }
           */
                }

                @Override
                public void onFailure(Call<ResultEmployeeId> call, Throwable t) {
                    //      Log.e("onErrorUserProfile: ", t.getMessage());
                }
            });

        }
    }

    private void initSocket() {

        try {
            //socket = IO.socket("http://welivechat.me:5050");
            socket = IO.socket(ApiClient.SOCKET_URL);
            socket.connect();
            Socketuserid socketuserid = new Socketuserid(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()));
            socket.emit("login", new Gson().toJson(socketuserid));

        } catch (URISyntaxException e) {
            e.printStackTrace();
            //  Log.e("connectError: ", e.getMessage());
        }

        socket.on("message.read", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //    Log.e("socketMessage:", "Message Read");
                        //   Log.e("socketReadData:", new Gson().toJson(args[0]));
                    }
                });
            }
        });


        /*socket.on("showSendFriendRequestAlert", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("socketAlert:", "Message showSendFriendRequestAlert");
                        Log.e("socketReadDataAlert:", new Gson().toJson(args[0]));

                        JSONObject data = (JSONObject) args[0];
                        try {

                            //      Log.e("socketData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            String frndState = data2.getString("alreadysent");

                            switch (frndState) {
                                case "1":
                                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                                    ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                                    break;
                                case "2":
                                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                                    ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.tv_addfriend)).setVisibility(View.GONE);
                                    ((TextView) findViewById(R.id.tv_chatmsg)).setText("Already Sent a Friend Request.");

                                    break;
                                case "3":
                                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                                    ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.GONE);
                                    break;
                                default:
                                    Log.e("FriendReqAltEr:", "Something went wrong!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("FriendReqAltEr:", e.getMessage());
                        }

                    }
                });
            }
        });
*/
        socket.on("friend.online.status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

        socket.on("friend.offline", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Log.e("friend.offline:", "offline Status");
                        //   Log.e("friend.offlineData:", new Gson().toJson(args[0]));
                        JSONObject data = (JSONObject) args[0];
                        try {

                            //   Log.e("MessRecData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            String userID = data2.getString("user_id");
                            if (userID.equals(reciverId)) {

                                isReceverInCall = false;
                                ((TextView) findViewById(R.id.tv_userstatus)).setText("offline");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorRedoffline));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline, 0, 0, 0);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });

        socket.on("User.Busy", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //        Log.e("User.Busy:", "Online Status");
                        //        Log.e("User.Busy:", new Gson().toJson(args[0]));
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //   Log.e("MessRecData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            String userID = data2.getString("user_id");
                            if (userID.equals(reciverId)) {
                                isReceverInCall = true;
                                ((TextView) findViewById(R.id.tv_userstatus)).setText("Busy");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorBusy));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_busy, 0, 0, 0);

                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });


        socket.on("message.get", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                            ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                        }

                        //       Log.e("socketMessage:", "Message Rec");
                        //       Log.e("socketRecData:", new Gson().toJson(args[0]));

                        JSONObject data = (JSONObject) args[0];
                        try {

                            //        Log.e("MessRecData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            //      Log.e("from id:", data2.getString("from"));
                            //    Log.e("from message:", data2.getString("message"));
                            String body = data2.getString("message");
                            String id = data2.getString("from");
                            String conversationId = data2.getString("conversationId");
                            String mimeType = data2.getString("mimeType");
                            String audioDur = "";
                            try {
                                audioDur = data2.getString("audio_duration");
                            } catch (Exception e) {
                                audioDur = "N/A";
                            }
                            if (!body.equals("")) {
                                if (converID.equals(conversationId)) {
                                    if (inActivity == 0) {
                                        readMessage();
                                        final Message_ message;
                                        message = new Message_(id, body, mimeType, audioDur);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageAdapter.add(message);
                                                messagesView.setSelection(messagesView.getCount() - 1);
                                            }
                                        });
                                    }
                                }
                            }

                            JSONArray actionDataArray = data2.getJSONArray("action_button_values");
                            //String valuesOfAction = data2.getString("action_button_values");
                            // Log.e("actionData:", valuesOfAction);
                            ArrayList<ActionButtonValue> actionButtonValueArrayList = new ArrayList<>();
                            ((RelativeLayout) findViewById(R.id.rl_botselection2)).setVisibility(View.VISIBLE);
                            ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.GONE);

                            for (int i = 0; i < actionDataArray.length(); i++) {
                                JSONObject actionData = actionDataArray.getJSONObject(i);
                                //Log.e("actionData:", actionData.getString("action_name"));
                                ActionButtonValue actionButtonValue = new ActionButtonValue();
                                actionButtonValue.setActionId(Integer.parseInt(actionData.getString("action_id")));
                                actionButtonValue.setActionName(actionData.getString("action_name"));
                                actionButtonValue.setConversationId(actionData.getString("conversationId"));
                                actionButtonValue.setUserId(Integer.parseInt(actionData.getString("UserId")));
                                actionButtonValue.setId(Integer.parseInt(actionData.getString("_id")));
                                actionButtonValue.setName(actionData.getString("name"));
                                actionButtonValue.setSenderProfilePic(actionData.getString("senderProfilePic"));
                                actionButtonValue.setSenderType(Integer.parseInt(actionData.getString("senderType")));
                                actionButtonValue.setReceiverId(Integer.parseInt(actionData.getString("receiverId")));
                                actionButtonValue.setReceiverName(actionData.getString("receiverName"));
                                actionButtonValue.setReceiverImageUrl(actionData.getString("receiverImageUrl"));
                                actionButtonValue.setBody(actionData.getString("body"));
                                actionButtonValue.setIsFriendAccept(Integer.parseInt(actionData.getString("isFriendAccept")));
                                actionButtonValue.setMimeType(actionData.getString("mimeType"));
                                actionButtonValue.setFrom(actionData.getString("from"));
                                actionButtonValue.setTo(actionData.getString("to"));
                                actionButtonValue.setIsBotMessage(Integer.parseInt(actionData.getString("isBotMessage")));

                                actionButtonValueArrayList.add(actionButtonValue);
                            }

                            String botmessageid = data2.getString("bot_message_id");

                            if (actionButtonValueArrayList.size() == 0) {
                                ((RelativeLayout) findViewById(R.id.rl_botselection2)).setVisibility(View.GONE);
                                if (((RelativeLayout) findViewById(R.id.rl_botselection)).getVisibility() == View.VISIBLE) {
                                    ((RelativeLayout) findViewById(R.id.rl_botselection)).setVisibility(View.GONE);
                                }
                            } else {
                                ((TextView) findViewById(R.id.tv_msg3)).setText(actionButtonValueArrayList.get(0).getActionName());
                                ((TextView) findViewById(R.id.tv_msg4)).setText(actionButtonValueArrayList.get(1).getActionName());
                            }

                            ((RelativeLayout) findViewById(R.id.rl_msg3)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                                    RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                                            "FSAfsafsdf");
                                    RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                                            converID);
                                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                                            String.valueOf(new SessionManager(getApplicationContext()).getUserId()));
                                    RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                                            new SessionManager(getApplicationContext()).getUserName());
                                    RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                                            new SessionManager(getApplicationContext()).getUserProfilepic());
                                    RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                                            "1");
                                    RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverId);
                                    RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverName);
                                    RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverProfilePic);
                                    RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                            "2");
                                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                            ((TextView) findViewById(R.id.tv_msg3)).getText().toString().trim());
                                    RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                            "1");
                                    RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                            "html/plain");

                                    RequestBody isBotMessage = RequestBody.create(MediaType.parse("text/plain"),
                                            String.valueOf(actionButtonValueArrayList.get(0).getIsBotMessage()));
                                    RequestBody from = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(0).getFrom());
                                    RequestBody to = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(0).getTo());
                                    RequestBody acionName = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(0).getActionName());
                                    RequestBody actionId = RequestBody.create(MediaType.parse("text/plain"),
                                            String.valueOf(actionButtonValueArrayList.get(0).getActionId()));
                                    RequestBody botMessageId = RequestBody.create(MediaType.parse("text/plain"),
                                            botmessageid);

                                    //  Log.e("TAGActionID", );
                                    Call<ResultSendMessage> call = apiservice.sendMessageFromSocket(3,UserId,
                                            conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                                            receiverType, body, isFriendAccept, mimeType, isBotMessage, from, to, acionName, actionId, botMessageId, actionId, botMessageId, botMessageId);

                                    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                                        call.enqueue(new Callback<ResultSendMessage>() {
                                            @Override
                                            public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                                                //   Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                                try {
                                                    if (response.body().getData().get(0).getStatus().equals("sent")) {

                                                        SocketSendMessage socketSendMessage = new SocketSendMessage(
                                                                new SessionManager(getApplicationContext()).getUserId(),
                                                                reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                                                ((TextView) findViewById(R.id.tv_msg3)).getText().toString().trim(),
                                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain", "1", String.valueOf(actionButtonValueArrayList.get(0).getIsBotMessage()),
                                                                String.valueOf(actionButtonValueArrayList.get(0).getActionId()), actionButtonValueArrayList.get(0).getActionName(),
                                                                botmessageid
                                                        );

                                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                                        //   Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));

                                                        final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                                                                ((TextView) findViewById(R.id.tv_msg3)).getText().toString().trim(), "html/plain");
                                                        if (!message.getBody().equals("")) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    messageAdapter.add(message);
                                                                    messagesView.setSelection(messagesView.getCount() - 1);
                                                                }
                                                            });
                                                        }
                                                        if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                                            ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                                        }
                                                        ((RelativeLayout) findViewById(R.id.rl_botselection2)).setVisibility(View.GONE);
                                                        //     ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                                                        getFriendShipStatus(reciverId);
                                                        ((EditText) findViewById(R.id.et_message)).setText("");
                                                        // repSocket();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                                //   Log.e("onResponseSendMessagE: ", t.getMessage());
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            ((RelativeLayout) findViewById(R.id.rl_msg4)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                                    RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                                            "FSAfsafsdf");
                                    RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                                            converID);
                                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                                            new SessionManager(getApplicationContext()).getUserId());
                                    RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                                            new SessionManager(getApplicationContext()).getUserName());
                                    RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                                            new SessionManager(getApplicationContext()).getUserProfilepic());
                                    RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                                            "1");
                                    RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverId);
                                    RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverName);
                                    RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                            reciverProfilePic);
                                    RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                            "2");
                                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                            ((TextView) findViewById(R.id.tv_msg4)).getText().toString().trim());
                                    RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                            "1");
                                    RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                            "html/plain");

                                    RequestBody isBotMessage = RequestBody.create(MediaType.parse("text/plain"),
                                            String.valueOf(actionButtonValueArrayList.get(1).getIsBotMessage()));
                                    RequestBody from = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(1).getFrom());
                                    RequestBody to = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(1).getTo());
                                    RequestBody acionName = RequestBody.create(MediaType.parse("text/plain"),
                                            actionButtonValueArrayList.get(1).getActionName());
                                    RequestBody actionId = RequestBody.create(MediaType.parse("text/plain"),
                                            String.valueOf(actionButtonValueArrayList.get(1).getActionId()));
                                    RequestBody botMessageId = RequestBody.create(MediaType.parse("text/plain"),
                                            botmessageid);

                                    //  Log.e("TAGActionID", );
                                    Call<ResultSendMessage> call = apiservice.sendMessageFromSocket(3,UserId,
                                            conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                                            receiverType, body, isFriendAccept, mimeType, isBotMessage, from, to, acionName, actionId, botMessageId, actionId, botMessageId, botMessageId);

                                    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                                        call.enqueue(new Callback<ResultSendMessage>() {
                                            @Override
                                            public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                                                //   Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                                try {
                                                    if (response.body().getData().get(0).getStatus().equals("sent")) {

                                                        SocketSendMessage socketSendMessage = new SocketSendMessage(
                                                                new SessionManager(getApplicationContext()).getUserId(),
                                                                reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                                                ((TextView) findViewById(R.id.tv_msg4)).getText().toString().trim(),
                                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain", "1", String.valueOf(actionButtonValueArrayList.get(1).getIsBotMessage()),
                                                                String.valueOf(actionButtonValueArrayList.get(1).getActionId()), actionButtonValueArrayList.get(1).getActionName(),
                                                                botmessageid
                                                        );

                                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                                        //   Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));

                                                        final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                                                                ((TextView) findViewById(R.id.tv_msg4)).getText().toString().trim(), "html/plain");
                                                        if (!message.getBody().equals("")) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    messageAdapter.add(message);
                                                                    messagesView.setSelection(messagesView.getCount() - 1);
                                                                }
                                                            });
                                                        }
                                                        if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                                            ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                                        }
                                                        ((RelativeLayout) findViewById(R.id.rl_botselection2)).setVisibility(View.GONE);
                                                        //     ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                                                        getFriendShipStatus(reciverId);
                                                        ((EditText) findViewById(R.id.et_message)).setText("");
                                                        // repSocket();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                                //   Log.e("onResponseSendMessagE: ", t.getMessage());
                                            }
                                        });
                                    } else {
                                        Toast.makeText(com.meetlive.app.activity.ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Log.e("socketError:", e.getMessage());

                        }


                    }
                });
            }
        });

        SocketSendMessage socketSendMessage = new SocketSendMessage(reciverId,
                new SessionManager(getApplicationContext()).getUserId(),
                converID
        );

//        socket.emit("messageScreenLoad", new Gson().toJson(socketSendMessage));

        socket.emit("message.read", new Gson().toJson(socketSendMessage));

        SocketSendMessage socketSendMessage1 = new SocketSendMessage(reciverId,
                new SessionManager(getApplicationContext()).getUserId());
        socket.emit("friend.online.status", new Gson().toJson(socketSendMessage1));
        //  Log.e("friend.online.status", new Gson().toJson(socketSendMessage1));
      /*  socket.on("messageScreenLoad", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("socketMessageLoad:", "Message Load Called");
                        Log.e("socketMsgLoad:", new Gson().toJson(args[0]));
                        JSONObject data = (JSONObject) args[0];
                        try {

                            //      Log.e("socketData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            //      Log.e("from id:", data2.getString("from"));
                            //    Log.e("from message:", data2.getString("message"));
                            String body = data2.getString("message");
                            //String body = "http://welivechat.me/img/uploads/users/photos/5f23fdcd07c661.jpg";
                            String mimeType = data2.getString("mimeType");

                            final Message_ message = new Message_("id", body, mimeType);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messageAdapter.add(message);
                                    messagesView.setSelection(messagesView.getCount() - 1);
                                }
                            });

                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                            Log.e("socketError:", e.getMessage());

                        }


                    }
                });
            }
        });
*/
        socket.on("DoRunSocket", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Log.e("DoRunSocketMessage:", "DoRunSocket Read");
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //   Log.e("DoRunSocketData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");

                            moremessageremanining = data2.getString("more_message_remanining");
                            if (!moremessageremanining.equals("0")) {
                                if (inActivity == 0) {
                                    repSocket();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });

        repSocket();
    }

    private String moremessageremanining = "0";
    Handler handler = new Handler();

    private void repSocket() {
        handler.postDelayed(runnable, 5000);
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {


            if (!moremessageremanining.equals("0")) {

                SocketSendMessage socketSendMessage = new SocketSendMessage(
                        new SessionManager(getApplicationContext()).getUserId(),
                        reciverId,
                        converID
                );

                //     Log.e("messageScreenLoad:", "in flow");
                //     Log.e("messageScreenValue:", moremessageremanining);

                socket.emit("messageScreenLoad", new Gson().toJson(socketSendMessage));
                repSocket();
            } else {
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                    //      Log.e("messageScreenValue:", "release");

                }
                //   Log.e("messageScreenValue:", "not load");
            }
        }
    };

    private void readMessage() {
        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
        RequestMessageRead requestMessageRead = new RequestMessageRead(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()), converID);
        //  Log.e("RequestReadMessage: ", new Gson().toJson(requestMessageRead));
        Call<ResultMessageRead> call = apiservice.readMessagefunction(requestMessageRead,3);

        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultMessageRead>() {
                @Override
                public void onResponse(Call<ResultMessageRead> call, Response<ResultMessageRead> response) {
                    //   Log.e("onResponseReadMessage: ", new Gson().toJson(response.body()));
                }

                @Override
                public void onFailure(Call<ResultMessageRead> call, Throwable t) {
                    //    Log.e("onErrorReadMessage: ", t.getMessage());

                }
            });
        }

    }


    final int radius = 50;
    final int margin = 10;

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            /*
            proPic = bundle.getString("ProPic");
            callRate = bundle.getString("callRate");
            recAge = bundle.getString("recAge");
            recCounrty = bundle.getString("recCounrty");
            */

            converID = bundle.getString("converID");
            reciverId = bundle.getString("receiver_id");
            reciverName = bundle.getString("receiver_name");
            reciverProfilePic = bundle.getString("receiver_image");
            tokenUserId = bundle.getString("tokenUserId");
            callRate = bundle.getString("callrate");

            try {
                userType = bundle.getString("isAdmin");
                if (userType.equals("0") && reciverName.equals("Zeeplive Official")) {
                    userType = "admin";
                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    converID = "5b7aa00db07d21f0ce9f6523";
                } else {
                    userType = "0";
                }
            } catch (Exception e) {

            }
            recAge = "20";
            recCounrty = "India";

            //   Log.e("converID", converID);
            ((TextView) findViewById(R.id.tv_username)).setText(reciverName);
//            final Transformation transformation = new RoundedTransformation(radius, margin);
            Picasso.get().load(reciverProfilePic)
                    .placeholder(R.drawable.defchat)
                    .error(R.drawable.defchat)
                    .into(((ImageView) findViewById(R.id.img_profile)));


            //    Toast.makeText(this, converID, Toast.LENGTH_SHORT).show();

            getAllMessages();

            getFriendShipStatus(reciverId);
        } else {
            Toast.makeText(this, "Something Went Wrong! Come Back Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFriendShipStatus(String id) {
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        RequestFriendShipStatus requestFriendShipStatus = new RequestFriendShipStatus(Integer.parseInt(id), converID);
        Call<ResultFriendShipStatus> call = apiservice.getFriendShipStatus(new SessionManager(getApplicationContext()).getUserToken(),
                "default11",3, requestFriendShipStatus);

        call.enqueue(new Callback<ResultFriendShipStatus>() {
            @Override
            public void onResponse(Call<ResultFriendShipStatus> call, Response<ResultFriendShipStatus> response) {
                //  Log.e("onFriendShipStatus: ", new Gson().toJson(response.body()));
                try {
                   /* if (response.body().getData().getAlreadyRequestSent() == 1) {
                        ((TextView) findViewById(R.id.tv_addfriend)).setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.tv_chatmsg)).setText("Already Sent a Friend Request.");

                    }*/
                    if (/*response.body().getData().getIsFriend() == 0 &&*/ response.body().getData().getMessageAlreadySent() == 0) {
                        ((RelativeLayout) findViewById(R.id.rl_botselection)).setVisibility(View.VISIBLE);

                        ((TextView) findViewById(R.id.tv_msg1)).setText("HELLO");
                        ((TextView) findViewById(R.id.tv_msg2)).setText("MERI FRIEND BANOGI");


                        ((RelativeLayout) findViewById(R.id.rl_msg1)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


//                                    ((RelativeLayout) findViewById(R.id.rl_botselection)).setVisibility(View.VISIBLE);

                                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                                RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                                        "FSAfsafsdf");
                                RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                                        converID);
                                RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                                        new SessionManager(getApplicationContext()).getUserId());
                                RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                                        new SessionManager(getApplicationContext()).getUserName());
                                RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                                        new SessionManager(getApplicationContext()).getUserProfilepic());
                                RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                                        "1");
                                RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverId);
                                RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverName);
                                RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverProfilePic);
                                RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                        "2");
                                RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                        "Hello");
                                RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                        "1");
                                RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                        "html/plain");


                                Call<ResultSendMessage> call = apiservice.sendMessage(3,UserId,
                                        conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                                        receiverType, body, isFriendAccept, mimeType);

                                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                                    call.enqueue(new Callback<ResultSendMessage>() {
                                        @Override
                                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                                            //   Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                            try {
                                                if (response.body().getData().get(0).getStatus().equals("sent")) {

                                                    SocketSendMessage socketSendMessage = new SocketSendMessage(
                                                            new SessionManager(getApplicationContext()).getUserId(),
                                                            reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                                            "Hello",
                                                            new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain", "1"
                                                    );

                                                    socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                                    //  Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));

                                                    final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                                                            "Hello", "html/plain");
                                                    if (!message.getBody().equals("")) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                messageAdapter.add(message);
                                                                messagesView.setSelection(messagesView.getCount() - 1);
                                                            }
                                                        });
                                                    }
                                                    if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                                        ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                                    }
                                                    ((RelativeLayout) findViewById(R.id.rl_botselection)).setVisibility(View.GONE);
                                                    ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);

                                                    ((EditText) findViewById(R.id.et_message)).setText("");
                                                    // repSocket();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                            //  Log.e("onResponseSendMessagE: ", t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(com.meetlive.app.activity.ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        ((RelativeLayout) findViewById(R.id.rl_msg2)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                                RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                                        "FSAfsafsdf");
                                RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                                        converID);
                                RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                                        new SessionManager(getApplicationContext()).getUserId());
                                RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                                        new SessionManager(getApplicationContext()).getUserName());
                                RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                                        "fasfsdfds");
                                RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                                        "1");
                                RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverId);
                                RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverName);
                                RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                        reciverProfilePic);
                                RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                        "2");
                                RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                        "Meri Friend Banogi");
                                RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                        "1");
                                RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                        "html/plain");


                                Call<ResultSendMessage> call = apiservice.sendMessage(3,UserId,
                                        conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                                        receiverType, body, isFriendAccept, mimeType);

                                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                                    call.enqueue(new Callback<ResultSendMessage>() {
                                        @Override
                                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                                            //  Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                            try {
                                                if (response.body().getData().get(0).getStatus().equals("sent")) {


                                                    SocketSendMessage socketSendMessage = new SocketSendMessage(
                                                            new SessionManager(getApplicationContext()).getUserId(),
                                                            reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                                            "Meri Friend Banogi",
                                                            new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain", "1"
                                                    );

                                                    socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                                    //  Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));

                                                    final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                                                            "Meri Friend Banogi", "html/plain");
                                                    if (!message.getBody().equals("")) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                messageAdapter.add(message);
                                                                messagesView.setSelection(messagesView.getCount() - 1);
                                                            }
                                                        });
                                                    }
                                                    if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                                        ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                                    }
                                                    ((RelativeLayout) findViewById(R.id.rl_botselection)).setVisibility(View.GONE);
                                                    ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                                                    ((EditText) findViewById(R.id.et_message)).setText("");
                                                    //         repSocket();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                            //  Log.e("onResponseSendMessagE: ", t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(com.meetlive.app.activity.ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else if (response.body().getData().getIsFriend() == 0 && response.body().getData().getMessageAlreadySent() != 0) {
                        ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                        ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.VISIBLE);
                        messagesView.setSelection(messagesView.getCount() - 1);

                    } else {
                        ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                        ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResultFriendShipStatus> call, Throwable t) {
                // Log.e("onFriendShipError: ", t.getMessage());
            }
        });
    }

    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private ArrayList<Message_> message_arrayList = new ArrayList<>();
    private int page = 1;
    private boolean nxtPage = false;

    private void getAllMessages() {
        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
        RequestAllMessages requestAllMessages = new RequestAllMessages(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                page, converID, userType);
        Call<ResultMessage> call = apiservice.getAllMessages(3,requestAllMessages);
        //Log.e("allMessageReq", new Gson().toJson(requestAllMessages));
        // Log.e("userid", String.valueOf(SharedPrefManager.getInstance(this).getUser().getId()));
        call.enqueue(new Callback<ResultMessage>() {
            @Override
            public void onResponse(Call<ResultMessage> call, Response<ResultMessage> response) {
                //Log.e("onResponseAllMessage: ", new Gson().toJson(response.body()));
                try {
                    messageArrayList.addAll(response.body().getData().getMessages());
                    nxtPage = response.body().getPaging().isNextPage();
                    sortArray(messageArrayList);
                    for (int i = 0; i < messageArrayList.size(); i++) {
                        //Log.e("SortArrayData", String.valueOf(messageArrayList.get(i).getId()));
                        message_arrayList.addAll(messageArrayList.get(i).getMessages());
                    }
                    //Log.e("TAGArraySize", message_arrayList.size() + "");
                    messageAdapter.notifyDataSetChanged();
                    messagesView.setSelection(messagesView.getCount() - 1);

                } catch (Exception e) {
                    ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_nochatmsg)).setText("No message's here.");
                }
            }

            @Override
            public void onFailure(Call<ResultMessage> call, Throwable t) {
                //Log.e("onResponseAllMessage: ", t.getMessage());

            }
        });
    }

    private void getAllMessages2() {
        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
        RequestAllMessages requestAllMessages = new RequestAllMessages(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                page, converID, userType);
        Call<ResultMessage> call = apiservice.getAllMessages(3,requestAllMessages);
        //Log.e("userid", String.valueOf(SharedPrefManager.getInstance(this).getUser().getId()));
        call.enqueue(new Callback<ResultMessage>() {
            @Override
            public void onResponse(Call<ResultMessage> call, Response<ResultMessage> response) {
                //Log.e("onResponseAllMessage2: ", new Gson().toJson(response.body()));
                try {
                    messageArrayList.addAll(response.body().getData().getMessages());
                    nxtPage = response.body().getPaging().isNextPage();
                    //          Log.e("TAGPaging", page + "");
                    sortArray(messageArrayList);
                    for (int i = 0; i < messageArrayList.size(); i++) {
                        //Log.e("SortArrayData", String.valueOf(messageArrayList.get(i).getId()));
                        message_arrayList.addAll(messageArrayList.get(i).getMessages());
                    }
                    //Log.e("TAGArraySize", message_arrayList.size() + "");

                    messageAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_nochatmsg)).setText("No message's here.");
                }
            }

            @Override
            public void onFailure(Call<ResultMessage> call, Throwable t) {
                //Log.e("onResponseAllMessage: ", t.getMessage());

            }
        });

    }

    private void sortArray(ArrayList<Message> arraylist) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //your own date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //your own date format
        Collections.sort(arraylist, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                try {
                    return simpleDateFormat.parse(message.getId()).compareTo(simpleDateFormat.parse(t1.getId()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }

        });
    }

    private Socket socket;

    private boolean listIsAtTop() {
        if (messagesView.getChildCount() == 0) return true;
        return messagesView.getChildAt(0).getTop() == 0;
    }

    private void initControls() {
        Context context;
        context = this;
        messageAdapter = new MessageAdapter(context, message_arrayList);
        messagesView = (ListView) findViewById(R.id.lv_allmessages);
        messagesView.setAdapter(messageAdapter);


        int limitRowsBDshow = 10; //size limit your list
    /*    messagesView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int counter = 1;
            int currentScrollState;
            int currentFirstVisibleItem;
            int currentVisibleItemCount;
            int currentTotalItemCount;


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.currentTotalItemCount = totalItemCount;
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    *//*** detect if there's been a scroll which has completed ***//*

                    counter++;
                    if (currentFirstVisibleItem == 0 && currentTotalItemCount > limitRowsBDshow - 1 && nxtPage) {
                        page = page + 1;
                        getAllMessages2();
                    }
                }
            }
        });*/


     /*   messagesView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 && listIsAtTop()) {
                    if (nxtPage) {
                        page = page + 1;
                        getAllMessages2();
                    }
                }
            }
        });*/

        rv_gift = findViewById(R.id.rv_gift);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        rv_gift.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(giftArrayList, R.layout.rv_gift, getApplicationContext());
        rv_gift.setAdapter(giftAdapter);

        networkCheck = new NetworkCheck();
        readMessage();
        ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
        ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));

        ((RelativeLayout) findViewById(R.id.rl_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(com.meetlive.app.activity.ChatActivity.this);
                Intent broadcastIntent = new Intent("REFRESH_DATA");
                localBroadcastManager.sendBroadcast(broadcastIntent);
                finish();
            }
        });


    /*    ((TextView) findViewById(R.id.tv_nochatmsg)).setTypeface(Typeface.createFromAsset(this.getAssets(),
                "fonts/Poppins-Regular_0.ttf"));
        ((TextView) findViewById(R.id.tv_username)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/POPPINS-SEMIBOLD_0.TTF"));
        ((TextView) findViewById(R.id.tv_audiomessage)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/POPPINS-SEMIBOLD_0.TTF"));
        ((TextView) findViewById(R.id.tv_chatmsg)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/POPPINS-SEMIBOLD_0.TTF"));
        ((TextView) findViewById(R.id.tv_addfriend)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/POPPINS-SEMIBOLD_0.TTF"));

        ((EditText) findViewById(R.id.et_message)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/Poppins-Regular_0.ttf"));

        ((TextView) findViewById(R.id.tv_coinchat)).setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/POPPINS-BOLD_0.TTF"));*/

        // ((TextView) findViewById(R.id.tv_coinchat)).setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes());

        EmojiconEditText emojiconEditText = findViewById(R.id.et_message);
        ImageView img_smile = findViewById(R.id.img_smile);
        EmojIconActions emojIcon;
        View rootView = findViewById(R.id.root_view);

        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, img_smile);
        //   emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_keyboard, R.drawable.smile);
        emojIcon.setUseSystemEmoji(true);

        img_smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojIcon.ShowEmojIcon();
            }
        });

        /*  emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "SHOW");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "HIDE");
            }
        });*/


        emojIcon.addEmojiconEditTextList(emojiconEditText);


        ((EditText) findViewById(R.id.et_message)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 > 0) {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(true);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.activedownloadarrow));
                } else {
                    ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
                    ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((EditText) findViewById(R.id.et_message)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //This is the filter
                ((ImageView) findViewById(R.id.img_send)).performClick();
                return true;
            }
        });

        ((ImageView) findViewById(R.id.img_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                        "FSAfsafsdf");
                RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                        converID);
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                        new SessionManager(getApplicationContext()).getUserId());
                RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                        new SessionManager(getApplicationContext()).getUserName());
                RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                        new SessionManager(getApplicationContext()).getUserProfilepic());
                RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                        "1");
                RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                        reciverId);
                RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                        reciverName);
                RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                        reciverProfilePic);
                RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                        "2");
                RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                        ((EditText) findViewById(R.id.et_message)).getText().toString().trim());
                RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                        "1");
                RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                        "html/plain");

                Call<ResultSendMessage> call = apiservice.sendMessage(3,UserId,
                        conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                        receiverType, body, isFriendAccept, mimeType);

                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    call.enqueue(new Callback<ResultSendMessage>() {
                        @Override
                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                            //  Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                            try {
                                if (response.body().getData().get(0).getStatus().equals("sent")) {

                                    SocketSendMessage socketSendMessage = new SocketSendMessage(
                                            new SessionManager(getApplicationContext()).getUserId(),
                                            reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                            ((EditText) findViewById(R.id.et_message)).getText().toString().trim(),
                                            new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain", "1"
                                    );
                                    socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                    //        Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));

                                    final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                                            ((EditText) findViewById(R.id.et_message)).getText().toString().trim(), "html/plain");
                                    if (!message.getBody().equals("")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageAdapter.add(message);
                                                messagesView.setSelection(messagesView.getCount() - 1);
                                            }
                                        });
                                    }
                                    ((EditText) findViewById(R.id.et_message)).setText("");
                                    if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                        ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                    }
                                }/* else {
                                Log.e("onResponseSubErr: ", response.body().getErrors().get(0));
                                Toast.makeText(ChatActivity.this,
                                        response.body().getErrors().get(0), Toast.LENGTH_SHORT).show();
                            }*/
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                            //   Log.e("onResponseSendMessagE: ", t.getMessage());
                            if (t instanceof SocketTimeoutException) {
                                // "Connection Timeout";
                                //     Log.e("Connection Timeout: ", "Connection Timeout");
                                //       ((ImageView) findViewById(R.id.img_send)).performClick();
                            } else if (t instanceof IOException) {
                                // "Timeout";
                                //   Log.e("Connection Timeout: ", "Timeout");

                            } else {
                                //Call was cancelled by user
                                if (call.isCanceled()) {
                                    System.out.println("Call was cancelled forcefully");
                                    //     Log.e("Connection Timeout: ", "Call was cancelled forcefully");

                                } else {
                                    //Generic error handling
                                    // System.out.println("Network Error :: " + error.getLocalizedMessage());
                                    //     Log.e("Connection Timeout: ", "Network Error :: " + t.getLocalizedMessage());

                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(com.meetlive.app.activity.ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        initKeyBoardListener();

        ((TextView) findViewById(R.id.tv_addfriend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = LayoutInflater.from(com.meetlive.app.activity.ChatActivity.this)
                        .inflate(R.layout.addfriendcoincustom, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(com.meetlive.app.activity.ChatActivity.this);
                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 650);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                ((TextView) dialogView.findViewById(R.id.tv_friendcall)).setTypeface(Typeface.createFromAsset(com.meetlive.app.activity.ChatActivity.this.getAssets(),
                        "fonts/POPPINS-BOLD_0.TTF"));
                ((TextView) dialogView.findViewById(R.id.tv_confirm)).setTypeface(Typeface.createFromAsset(com.meetlive.app.activity.ChatActivity.this.getAssets(),
                        "fonts/POPPINS-BOLD_0.TTF"));
                ((TextView) dialogView.findViewById(R.id.tv_cancel)).setTypeface(Typeface.createFromAsset(com.meetlive.app.activity.ChatActivity.this.getAssets(),
                        "fonts/POPPINS-BOLD_0.TTF"));

                String first = "With only ";
                String next = "<font color='#ff3587'>" + " 1 coins," + "</font>";
                String last = " you can add him/her to your friend list and start chatting. He/she is waiting for you~";
                ((TextView) dialogView.findViewById(R.id.tv_friendcall)).setText(Html.fromHtml(first + next + last));

                ((TextView) dialogView.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                ((TextView) dialogView.findViewById(R.id.tv_confirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                     /*   int currentCoin = Integer.parseInt(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes());

                        if (currentCoin > 0) {
                            ((RelativeLayout) findViewById(R.id.rl_bottomaddfriend)).setVisibility(View.GONE);
                            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);

                            currentCoin = currentCoin - 1;
                            ResponceDataguestLogin responceDataguestLogin = new ResponceDataguestLogin(String.valueOf(currentCoin));
                            SharedPrefManager.getInstance(getApplicationContext()).updatePurchasedMinutes(responceDataguestLogin);
                            updateCoinApi();

                            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

                            RequestFriendRequest requestFriendRequest = new RequestFriendRequest(Integer.parseInt(reciverId));

                            Call<ResultSendFriendRequest> call = apiservice.sendFriendRequest(SharedPrefManager.getInstance(getApplicationContext()).getUser().getAuth_token(),
                                    "default11", requestFriendRequest);

                            call.enqueue(new Callback<ResultSendFriendRequest>() {
                                @Override
                                public void onResponse(Call<ResultSendFriendRequest> call, Response<ResultSendFriendRequest> response) {
                                    //       Log.e("onSendFriendRequest: ", new Gson().toJson(response.body()));
                                    //      getFriendShipStatus(reciverId);
                                }

                                @Override
                                public void onFailure(Call<ResultSendFriendRequest> call, Throwable t) {
                                    //     Log.e("SendFriendRequestErr: ", t.getMessage());

                                }
                            });
                        } else {
                            View dialogView = LayoutInflater.from(ChatActivity.this)
                                    .inflate(R.layout.insufcallratecustom, viewGroup, false);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setView(dialogView);

                            final AlertDialog alertDialog = builder.create();

                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                            layoutParams.gravity = Gravity.CENTER;
                            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 650);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.show();

                            ((TextView) dialogView.findViewById(R.id.tv_friendcall)).setTypeface(Typeface.createFromAsset(ChatActivity.this.getAssets(),
                                    "fonts/POPPINS-EXTRABOLD_0.TTF"));

                            ((TextView) dialogView.findViewById(R.id.tv_cost)).setTypeface(Typeface.createFromAsset(ChatActivity.this.getAssets(),
                                    "fonts/POPPINS-BOLD_0.TTF"));

                            String first = "It will cost ";
                            String next = "<font color='#ffc20e'>" + callRate + " coin/min" + "</font>";
                            String last = " to call her.";
                            ((TextView) dialogView.findViewById(R.id.tv_cost)).setText(Html.fromHtml(first + next + last));
                            ((TextView) dialogView.findViewById(R.id.tv_cost)).setVisibility(View.GONE);

                            for (int i = 0; i < coinPlanArrayList.size(); i++) {
                                if (coinPlanArrayList.get(i).getBestOffer() != 1) {
                                    ((TextView) dialogView.findViewById(R.id.tv_coininsuf)).setText(coinPlanArrayList.get(0).getCoins() + "");
                                    ((TextView) dialogView.findViewById(R.id.tv_coin2insuf)).setText(coinPlanArrayList.get(1).getCoins() + "");

                                    ((Button) dialogView.findViewById(R.id.tv_amountinsuf)).setText("\u20B9 " + coinPlanArrayList.get(0).getAmount());
                                    ((Button) dialogView.findViewById(R.id.tv_amount2insuf)).setText("\u20B9 " + coinPlanArrayList.get(1).getAmount());
                                }
                            }

                            ((Button) dialogView.findViewById(R.id.tv_amountinsuf)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    subscriptionPlanId = String.valueOf(coinPlanArrayList.get(0).getId());
                                    originalPrice = String.valueOf(coinPlanArrayList.get(0).getAmount());
                                    newCoins = String.valueOf(coinPlanArrayList.get(0).getCoins());
                                    startPayment(String.valueOf(coinPlanArrayList.get(0).getAmount()));
                                }
                            });

                            ((Button) dialogView.findViewById(R.id.tv_amount2insuf)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    subscriptionPlanId = String.valueOf(coinPlanArrayList.get(1).getId());
                                    originalPrice = String.valueOf(coinPlanArrayList.get(1).getAmount());
                                    newCoins = String.valueOf(coinPlanArrayList.get(1).getCoins());
                                    startPayment(String.valueOf(coinPlanArrayList.get(1).getAmount()));
                                }
                            });
                            ((RelativeLayout) dialogView.findViewById(R.id.rl_close)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });

                        }*/
                    }
                });

            }
        });

        ((RelativeLayout) findViewById(R.id.rl_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                hideKeybaord(view);
        /*        ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setAnimation(slideUp);
        */
            }
        });

        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          /*      if (((RelativeLayout) findViewById(R.id.rl_blockmenu)).getVisibility() == View.VISIBLE) {
                    ((TextView) findViewById(R.id.tv_cancel)).performClick();
                }
                if (((MaterialCardView) findViewById(R.id.mv_report)).getVisibility() == View.VISIBLE) {
                    ((Button) findViewById(R.id.btn_cancelreport)).performClick();
                }
          */

                if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
                    //    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    hideKeybaord(view);
                }
                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                    messagesView.setVisibility(View.VISIBLE);
                }
            }
        });




       /* ((TextView)

                findViewById(R.id.tv_cancel)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                  *//*      ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setVisibility(View.GONE);
                        ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setAnimation(slideDown);*//*

                    }
                });*/

   /*     ((TextView) findViewById(R.id.tv_report)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setVisibility(View.GONE);

                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ((MaterialCardView) findViewById(R.id.mv_report)).setVisibility(View.VISIBLE);
                ((MaterialCardView) findViewById(R.id.mv_report)).setAnimation(slideUp);
            }
        });

        ((TextView) findViewById(R.id.tv_block)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((RelativeLayout) findViewById(R.id.rl_blockmenu)).setVisibility(View.GONE);
                ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
                RequestBlockUnclock requestBlockUnclock = new RequestBlockUnclock("block", converID,
                        Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()), reciverId,
                        new SessionManager(getApplicationContext()).getUserId());

                //   Log.e("onBlockReq: ", new Gson().toJson(requestBlockUnclock));
                Call<ResultBlockUnblock> call = apiservice.blockUnblockUser(new SessionManager(getApplicationContext()).getUserToken(),
                        "default11", requestBlockUnclock);
                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    call.enqueue(new Callback<ResultBlockUnblock>() {
                        @Override
                        public void onResponse(Call<ResultBlockUnblock> call, Response<ResultBlockUnblock> response) {
                            //   Log.e("onBlock: ", new Gson().toJson(response.body()));
                            try {
                                if (response.body().getType().equals("error")) {
                                    Toast.makeText(ChatActivity.this,
                                            response.body().getErrors().get(0), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChatActivity.this,
                                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResultBlockUnblock> call, Throwable t) {
                            //  Log.e("onFailure: ", t.getMessage());
                            Toast.makeText(ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                        }

                    });
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(ChatActivity.this);
                    Intent broadcastIntent = new Intent("REFRESH_DATA");
                    localBroadcastManager.sendBroadcast(broadcastIntent);
                    finish();
                }
            }
        });

        ((Button) findViewById(R.id.btn_cancelreport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                ((MaterialCardView) findViewById(R.id.mv_report)).setVisibility(View.GONE);
                ((MaterialCardView) findViewById(R.id.mv_report)).setAnimation(slideDown);
            }
        });

        ((Button) findViewById(R.id.btn_reportreport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                ((MaterialCardView) findViewById(R.id.mv_report)).setVisibility(View.GONE);
                ((MaterialCardView) findViewById(R.id.mv_report)).setAnimation(slideDown);

                ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                RequestReport requestReport = new RequestReport(issueId, Integer.parseInt(reciverId));

                Call<ResultSendFriendRequest> call = apiservice.reportUser(new SessionManager(getApplicationContext()).getUserToken(),
                        "default11", requestReport);

                if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                    call.enqueue(new Callback<ResultSendFriendRequest>() {
                        @Override
                        public void onResponse(Call<ResultSendFriendRequest> call, Response<ResultSendFriendRequest> response) {
                            try {
                                Toast.makeText(ChatActivity.this,
                                        response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultSendFriendRequest> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ((RadioButton) findViewById(R.id.rdo1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueId = reportDataArrayList.get(0).getId();
            }
        });

        ((RadioButton) findViewById(R.id.rdo2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueId = reportDataArrayList.get(1).getId();
            }
        });

        ((RadioButton) findViewById(R.id.rdo3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueId = reportDataArrayList.get(2).getId();
            }
        });

        ((RadioButton) findViewById(R.id.rdo4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueId = reportDataArrayList.get(3).getId();
            }
        });
*/
        loadReportIssueData();

        ((TextView) findViewById(R.id.tv_topup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(com.meetlive.app.activity.ChatActivity.this)
                        .inflate(R.layout.insufcallratecustom, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(com.meetlive.app.activity.ChatActivity.this);
                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 650);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                ((TextView) dialogView.findViewById(R.id.tv_friendcall)).setTypeface(Typeface.createFromAsset(com.meetlive.app.activity.ChatActivity.this.getAssets(),
                        "fonts/POPPINS-EXTRABOLD_0.TTF"));

                ((TextView) dialogView.findViewById(R.id.tv_cost)).setTypeface(Typeface.createFromAsset(com.meetlive.app.activity.ChatActivity.this.getAssets(),
                        "fonts/POPPINS-BOLD_0.TTF"));

                String first = "It will cost ";
                String next = "<font color='#ffc20e'>" + callRate + " coin/min" + "</font>";
                String last = " to call her.";
                ((TextView) dialogView.findViewById(R.id.tv_cost)).setText(Html.fromHtml(first + next + last));

                for (int i = 0; i < coinPlanArrayList.size(); i++) {
                    if (coinPlanArrayList.get(i).getBestOffer() != 1) {
                        ((TextView) dialogView.findViewById(R.id.tv_coininsuf)).setText(coinPlanArrayList.get(0).getCoins() + "");
                        ((TextView) dialogView.findViewById(R.id.tv_coin2insuf)).setText(coinPlanArrayList.get(1).getCoins() + "");
                        ((Button) dialogView.findViewById(R.id.tv_amountinsuf)).setText("\u20B9 " + coinPlanArrayList.get(0).getAmount());
                        ((Button) dialogView.findViewById(R.id.tv_amount2insuf)).setText("\u20B9 " + coinPlanArrayList.get(1).getAmount());
                    }
                }

                ((Button) dialogView.findViewById(R.id.tv_amountinsuf)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        subscriptionPlanId = String.valueOf(coinPlanArrayList.get(0).getId());
                        originalPrice = String.valueOf(coinPlanArrayList.get(0).getAmount());
                        newCoins = String.valueOf(coinPlanArrayList.get(0).getCoins());
                        startPayment(String.valueOf(coinPlanArrayList.get(0).getAmount()));
                    }
                });

                ((Button) dialogView.findViewById(R.id.tv_amount2insuf)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        subscriptionPlanId = String.valueOf(coinPlanArrayList.get(1).getId());
                        originalPrice = String.valueOf(coinPlanArrayList.get(1).getAmount());
                        newCoins = String.valueOf(coinPlanArrayList.get(1).getCoins());
                        startPayment(String.valueOf(coinPlanArrayList.get(1).getAmount()));
                    }
                });
                ((RelativeLayout) dialogView.findViewById(R.id.rl_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        getCoinData();
        ((ImageView) findViewById(R.id.img_video)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApiManager(getApplicationContext(), com.meetlive.app.activity.ChatActivity.this).searchUser(reciverId, "1");
            }
        });

        ((ImageView) findViewById(R.id.img_gift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (((RelativeLayout) findViewById(R.id.rl_blockmenu)).getVisibility() == View.VISIBLE) {
                    ((TextView) findViewById(R.id.tv_cancel)).performClick();
                }
                if (((MaterialCardView) findViewById(R.id.mv_report)).getVisibility() == View.VISIBLE) {
                    ((Button) findViewById(R.id.btn_cancelreport)).performClick();
                }*/
                hideKeybaord(view);

                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.GONE) {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);
                    ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                    String authToken = Constant.BEARER + new SessionManager(getApplicationContext()).getUserToken();
                    Call<ResultGift> call = apiservice.getGift(authToken,3);
                    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                        call.enqueue(new Callback<ResultGift>() {
                            @Override
                            public void onResponse(Call<ResultGift> call, Response<ResultGift> response) {
                                //      Log.e("onGift: ", new Gson().toJson(response.body()));
                                try {
                                    if (response.body().isStatus()) {
//                                        ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);
                                        ((ImageView) findViewById(R.id.img_giftloader)).setVisibility(View.GONE);
                                        ((TextView) findViewById(R.id.tv_coinchat)).setVisibility(View.VISIBLE);
                                        giftArrayList.clear();
                                        giftArrayList.addAll(response.body().getResult());
                                        giftAdapter.notifyDataSetChanged();

                                        if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                                            ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                                        }
                                    }

                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultGift> call, Throwable t) {
                                //    Log.e("onErrorGift: ", t.getMessage());

                            }
                        });
                    }

                } else {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                }
            }
        });

        ItemClickSupport.addTo(rv_gift).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                int currentCoin = Integer.parseInt(((TextView) findViewById(R.id.tv_coinchat)).getText().toString());
                // currentCoin = currentCoin - giftArrayList.get(position).getAmount();
                if (currentCoin > giftArrayList.get(position).getAmount()) {
                    fPosition = position;
                    new ApiManager(getApplicationContext(), ChatActivity.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId),
                            giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount()));



                 /*   ResponceDataguestLogin responceDataguestLogin = new ResponceDataguestLogin(String.valueOf(currentCoin));
                    SharedPrefManager.getInstance(getApplicationContext()).updatePurchasedMinutes(responceDataguestLogin);*/

                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                    RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                            "FSAfsafsdf");
                    RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                            converID);
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                            new SessionManager(getApplicationContext()).getUserId());
                    RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                            new SessionManager(getApplicationContext()).getUserName());
                    RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                            new SessionManager(getApplicationContext()).getUserProfilepic());
                    RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                            "1");
                    RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                            reciverId);
                    RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                            reciverName);
                    RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                            reciverProfilePic);
                    RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                            "2");
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                            giftArrayList.get(position).getGiftPhoto());
                    RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                            "1");
                    RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                            "image/gift");
                    RequestBody giftCoins = RequestBody.create(MediaType.parse("text/plain"),
                            String.valueOf(giftArrayList.get(position).getAmount()));

                    Call<ResultSendMessage> call = apiservice.sendMessageGift(3,UserId,
                            conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                            receiverType, body, isFriendAccept, giftCoins, mimeType);

                    call.enqueue(new Callback<ResultSendMessage>() {
                        @Override
                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                            //          Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));

                        }


                        @Override
                        public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                            //         Log.e("onResponseSendMessagE: ", t.getMessage());
                        }
                    });
                }


               /* if (((RelativeLayout) findViewById(R.id.rl_blockmenu)).getVisibility() == View.VISIBLE) {
                    ((TextView) findViewById(R.id.tv_cancel)).performClick();
                }
                if (((MaterialCardView) findViewById(R.id.mv_report)).getVisibility() == View.VISIBLE) {
                    ((Button) findViewById(R.id.btn_cancelreport)).performClick();
                }

                if (giftArrayList.get(position).getAmount() < Integer.parseInt(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes())) {
                    int currentCoin = Integer.parseInt(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes());
                    currentCoin = currentCoin - giftArrayList.get(position).getAmount();

                    ResponceDataguestLogin responceDataguestLogin = new ResponceDataguestLogin(String.valueOf(currentCoin));
                    SharedPrefManager.getInstance(getApplicationContext()).updatePurchasedMinutes(responceDataguestLogin);
                    updateCoinApi();

                    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

                    RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                            "FSAfsafsdf");
                    RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                            converID);
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                            String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId()));
                    RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                            SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_name());
                    RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                            "fasfsdfds");
                    RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                            "1");
                    RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                            reciverId);
                    RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                            reciverName);
                    RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                            reciverProfilePic);
                    RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                            "2");
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                            giftArrayList.get(position).getGiftPhoto());
                    RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                            "1");
                    RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                            "image/gift");
                    RequestBody giftCoins = RequestBody.create(MediaType.parse("text/plain"),
                            String.valueOf(giftArrayList.get(position).getAmount()));

                    Call<ResultSendMessage> call = apiservice.sendMessageGift(UserId,
                            conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                            receiverType, body, isFriendAccept, giftCoins, mimeType);

                    call.enqueue(new Callback<ResultSendMessage>() {
                        @Override
                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                            //  Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                            try {
                                if (response.body().getData().get(0).getStatus().equals("sent")) {

                                    SocketSendMessage socketSendMessage = new SocketSendMessage(
                                            String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId()),
                                            reciverId, converID, SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_name(),
                                            giftArrayList.get(position).getGiftPhoto(),
                                            "asd", "image/gift"
                                    );
                                    socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                    //  Log.e("socketMessage:", new Gson().toJson(socketSendMessage));

                                    final Message_ message = new Message_(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId())
                                            , giftArrayList.get(position).getGiftPhoto(), "image/jpeg");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageAdapter.add(message);
                                            messagesView.setSelection(messagesView.getCount() - 1);
                                        }
                                    });
                                } else {
                                    //   Log.e("onResponseSubErr: ", response.body().getErrors().get(0));
                                    Toast.makeText(ChatActivity.this,
                                            response.body().getErrors().get(0), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                            //  Log.e("onResponseSendMessagE: ", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "update coin", Toast.LENGTH_SHORT).show();
                }
*/


            }
        });

        img_audio = findViewById(R.id.img_audio);

        rl_cancelAudio = findViewById(R.id.rl_cancelAudio);
        img_audio.setTag("audio");


        img_audio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //       img_audio.setImageDrawable(getResources().getDrawable(R.drawable.gift));

                ((RelativeLayout) findViewById(R.id.rl_visualizer)).setVisibility(View.VISIBLE);

                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data//data to be dragged
                        , shadowBuilder //drag shadow
                        , view//local data about the drag and drop operation
                        , 0//no needed flags
                );
                img_audio.setVisibility(View.GONE);
                rl_cancelAudio.setVisibility(View.VISIBLE);

                if (!isRecording) {
                    // isRecording = true;

                    recorder = new MediaRecorder();

                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(audioDirTemp + "/audio_file"
                            + ".mp3");

                    MediaRecorder.OnErrorListener errorListener = null;
                    recorder.setOnErrorListener(errorListener);
                    MediaRecorder.OnInfoListener infoListener = null;
                    recorder.setOnInfoListener(infoListener);

                    try {
                        recorder.prepare();
                        recorder.start();
                        isRecording = true; // we are currently recording
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                        Log.e("TAGERROR", e.getMessage());
                    }
                    handlerVisual.post(updateVisualizer);
                    //handlerTimer = new Handler();
                    StartTime = SystemClock.uptimeMillis();
                    handlerTimer.postDelayed(runnableTimer, 0);
                    ((TextView) findViewById(R.id.tv_audiomessage)).setVisibility(View.VISIBLE);

                }

                return true;
            }
        });


        rl_cancelAudio.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int action = dragEvent.getAction();
                // Handles each of the expected events
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            // if you want to apply color when drag started to your view you can uncomment below lines
                            // to give any color tint to the View to indicate that it can accept
                            // data.

                            //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view

                            // Invalidate the view to force a redraw in the new tint
                            //  view.invalidate();

                            // returns true to indicate that the View can accept the dragged data.
                            return true;

                        }

                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag acceptable view
                        // Return true; the return value is ignored.

//                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

                        // Invalidate the view to force a redraw in the new tint
                        //              view.invalidate();
                        ((TextView) findViewById(R.id.tv_audiomessage)).setText("Let go to cancel");
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        // Ignore the event
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // Re-sets the color tint to blue, if you had set the BLUE color or any color in ACTION_DRAG_STARTED. Returns true; the return value is ignored.

                        //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                        //If u had not provided any color in ACTION_DRAG_STARTED then clear color filter.
//                view.getBackground().clearColorFilter();
                        // Invalidate the view to force a redraw in the new tint
                        //              view.invalidate();
                        ((TextView) findViewById(R.id.tv_audiomessage)).setText("Swipe up to cancel, let go to send");

                        return true;
                    case DragEvent.ACTION_DROP:
                        // Gets the item containing the dragged data
                        ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = item.getText().toString();

                        // Displays a message containing the dragged data.
                        //    Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Turns off any color tints
//                view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        //              view.invalidate();

                        //            View v = (View) event.getLocalState();
//                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                        // Returns true. DragEvent.getResult() will return true.
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // Turns off any color tinting
//                view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        //              view.invalidate();

                        img_audio.setImageDrawable(getResources().getDrawable(R.drawable.audio));
                        rl_cancelAudio.setVisibility(View.GONE);
                        img_audio.setVisibility(View.VISIBLE);
                        ((RelativeLayout) findViewById(R.id.rl_visualizer)).setVisibility(View.GONE);
                        releaseRecorder();
                        if (handlerTimer != null) {
                            handlerTimer.removeCallbacks(runnableTimer);
                        }
                        ((TextView) findViewById(R.id.tv_audiomessage)).setVisibility(View.GONE);
                        // Does a getResult(), and displays what happened.
                        if (dragEvent.getResult()) {
                        } else {
                            sendAudio();
                        }

                        // returns true; the value is ignored.
                        return true;

                    // An unknown action type was received.
                    default:
                        //  Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }

                return false;
            }
        });

        visualizerView = (VisualizerView) findViewById(R.id.visualizer);

        audioDirTemp = new File(Environment.getExternalStorageDirectory(), DIRECTORY_NAME_TEMP);
        if (audioDirTemp.exists()) {
            deleteFilesInDir(audioDirTemp);
        } else {
            audioDirTemp.mkdirs();
        }

        // create the Handler for visualizer update
        handlerVisual = new Handler();

        handlerTimer = new Handler();

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

       /* messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                messagesView.setEnabled(false);

                messageAdapter.shartMediaPlayer(i);
            }

        });*/

    }

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Hours, Seconds, Minutes, MilliSeconds;

    Handler handlerTimer;

    public Runnable runnableTimer = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Hours = Minutes / 60;
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            stringAudioDuration = String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds);

            handlerTimer.postDelayed(this, 0);
        }

    };


    public void updateCoinApi() {
    /*    ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
        RequestCoinUpdate requestCoinUpdate = new RequestCoinUpdate(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),
                Integer.parseInt(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes()));

        Call<ResultCoinUpdate> call = apiservice.updateCoin(requestCoinUpdate);
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultCoinUpdate>() {
                @Override
                public void onResponse(Call<ResultCoinUpdate> call, Response<ResultCoinUpdate> response) {
                    //  Log.e("onResponceCoinUpdate", new Gson().toJson(response.body()));
                    ((TextView) findViewById(R.id.tv_coinchat)).setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getPurchased_minutes());

                }

                @Override
                public void onFailure(Call<ResultCoinUpdate> call, Throwable t) {
                    //   Log.e("onErrorCoinUpdate", t.getMessage());
                }
            });
        }*/
    }


    private ArrayList<ReportData> reportDataArrayList = new ArrayList<>();
    private int issueId;

    private void loadReportIssueData() {
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<ResultReportIssue> call = apiservice.getReportIssues(new SessionManager(getApplicationContext()).getUserToken(), "default11",3);

        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultReportIssue>() {
                @Override
                public void onResponse(Call<ResultReportIssue> call, Response<ResultReportIssue> response) {
                    //  Log.e("onReportIss: ", new Gson().toJson(response.body()));
                    try {
                        reportDataArrayList.addAll(response.body().getData());
                        issueId = reportDataArrayList.get(0).getId();
                       /* ((RadioButton) findViewById(R.id.rdo1)).setText(reportDataArrayList.get(0).getIssue());
                        ((RadioButton) findViewById(R.id.rdo2)).setText(reportDataArrayList.get(1).getIssue());
                        ((RadioButton) findViewById(R.id.rdo3)).setText(reportDataArrayList.get(2).getIssue());
                        ((RadioButton) findViewById(R.id.rdo4)).setText(reportDataArrayList.get(3).getIssue());*/
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResultReportIssue> call, Throwable t) {
                    //  Log.e("onReportIssError: ", t.getMessage());

                }
            });
        }
    }

    private void initKeyBoardListener() {
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        // Top-level window decor view.
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // Retrieve visible rectangle inside window.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        //  Log.e("Keyboard", "SHOW");
                        ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.img_video)).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.img_send)).setVisibility(View.VISIBLE);
                        messagesView.setSelection(messagesView.getCount() - 1);

                     /*   if (((RelativeLayout) findViewById(R.id.rl_blockmenu)).getVisibility() == View.VISIBLE) {
                            ((TextView) findViewById(R.id.tv_cancel)).performClick();
                        }
                        if (((MaterialCardView) findViewById(R.id.mv_report)).getVisibility() == View.VISIBLE) {
                            ((Button) findViewById(R.id.btn_cancelreport)).performClick();
                        }*/
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        //  Log.e("Keyboard", "HIDE");
                        ((ImageView) findViewById(R.id.img_send)).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.img_video)).setVisibility(View.VISIBLE);

                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });

        ((RelativeLayout) findViewById(R.id.rl_gift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   if (((RelativeLayout) findViewById(R.id.rl_blockmenu)).getVisibility() == View.VISIBLE) {
                    ((TextView) findViewById(R.id.tv_cancel)).performClick();
                }
                if (((MaterialCardView) findViewById(R.id.mv_report)).getVisibility() == View.VISIBLE) {
                    ((Button) findViewById(R.id.btn_cancelreport)).performClick();
                }*/
            }
        });
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        // Log.e("TAGOnPause", "M here in background");
        //  socket.off("showSendFriendRequestAlert");
        socket.off("DoRunSocket");
        socket.off("messageScreenLoad");
        super.onPause();
    }

    @Override
    protected void onResume() {
        initSocket();
       /* try {
            socket = IO.socket("http://welivechat.me:5050");
            socket.connect();

            Socketuserid socketuserid = new Socketuserid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

            socket.emit("login", new Gson().toJson(socketuserid));
            Log.e("socketMessage:", "Message Sent");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("connectError: ", e.getMessage());
        }
*/
        SocketSendMessage socketSendMessage1 = new SocketSendMessage(reciverId,
                new SessionManager(getApplicationContext()).getUserId());
        socket.emit("friend.online.status", new Gson().toJson(socketSendMessage1));

        socket.on("friend.online", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    /*    Log.e("friend.online:", "Online Status");
                        Log.e("friend.online:", new Gson().toJson(args[0]));
                        Log.e("socketOnline:", "Online Status");
                        Log.e("socketOnlineData:", new Gson().toJson(args[0]));
                    */
                        JSONObject data = (JSONObject) args[0];
                        try {

                            //   Log.e("MessRecData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            String userID = data2.getString("user_id");
                            if (userID.equals(reciverId)) {
                                isReceverInCall = false;
                                ((TextView) findViewById(R.id.tv_userstatus)).setText("online");
                                ((TextView) findViewById(R.id.tv_userstatus)).setTextColor(getResources().getColor(R.color.colorGreen));
                                ((TextView) findViewById(R.id.tv_userstatus)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });

        super.onResume();
    }


    private void releaseRecorder() {
        try {
            if (recorder != null) {
                isRecording = false; // stop recording
                handlerVisual.removeCallbacks(updateVisualizer);
                visualizerView.clear();
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFilesInDir(File path) {

        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()) {

                } else {
                    files[i].delete();
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inActivity = 2;
        socket.disconnect();
        socket.off("message.read");
        socket.off("showSendFriendRequestAlert");
        socket.off("friend.online.status");

        socket.off("friend.offline");
        socket.off("User.Busy");
        socket.off("message.get");

        socket.off("messageScreenLoad");
        socket.off("DoRunSocket");
        socket.off("friend.online");

        //socket.off("message.get", onNewMessage);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
       //socket.emit("disconnecting");
        releaseRecorder();
    }

    // updates the visualizer every 50 milliseconds
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x = recorder.getMaxAmplitude();
                visualizerView.addAmplitude(x); // update the VisualizeView
                visualizerView.invalidate(); // refresh the VisualizerView

                // update in 40 milliseconds
                handlerVisual.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };

    private void sendAudio() {

        File audioPath = new File(audioDirTemp + "/audio_file" + ".mp3");

        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"),
                "FSAfsafsdf");
        RequestBody conversationId = RequestBody.create(MediaType.parse("text/plain"),
                converID);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),
                new SessionManager(getApplicationContext()).getUserId());
        RequestBody name_1 = RequestBody.create(MediaType.parse("text/plain"),
                new SessionManager(getApplicationContext()).getUserName());
        RequestBody senderProfilePic = RequestBody.create(MediaType.parse("text/plain"),
                new SessionManager(getApplicationContext()).getUserProfilepic());
        RequestBody senderType = RequestBody.create(MediaType.parse("text/plain"),
                "1");
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"),
                reciverId);
        RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                reciverName);
        RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                reciverProfilePic);
        RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                "2");
        RequestBody audioDuration = RequestBody.create(MediaType.parse("text/plain"),
                stringAudioDuration);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), audioPath);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("files", audioPath.getName(), requestFile);

        RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                "1");

        Call<ResultSendMessage> call = apiservice.sendMessageAudio(3,UserId,
                conversationId, id, name_1, senderProfilePic, senderType, receiverId, receiverName, receiverImageUrl,
                receiverType, body, isFriendAccept, audioDuration);


        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultSendMessage>() {
                @Override
                public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                    //  Log.e("onSendAudio: ", new Gson().toJson(response.body()));

                    SocketSendMessage socketSendMessage = new SocketSendMessage(
                            new SessionManager(getApplicationContext()).getUserId(),
                            reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                            response.body().getData().get(0).getBody(),
                            "asd", "audio/mp3", "1", stringAudioDuration
                    );
                    socket.emit("message.send", new Gson().toJson(socketSendMessage));
                    //  Log.e("socketMessageSend:", new Gson().toJson(socketSendMessage));
                    final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId(),
                            response.body().getData().get(0).getBody(), "audio/mp3", stringAudioDuration);
                    if (!message.getBody().equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.add(message);
                                messagesView.setSelection(messagesView.getCount() - 1);
                            }
                        });
                        if (((TextView) findViewById(R.id.tv_nochatmsg)).getVisibility() == View.VISIBLE) {
                            ((TextView) findViewById(R.id.tv_nochatmsg)).setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                    //   Log.e("onAudioError: ", t.getMessage());

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        //socket.emit("disconnecting");
        if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
        } else {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(com.meetlive.app.activity.ChatActivity.this);
            Intent broadcastIntent = new Intent("REFRESH_DATA");
            localBroadcastManager.sendBroadcast(broadcastIntent);
            finish();
            super.onBackPressed();
        }
    }

    private void getCoinData() {
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<ResultCoinPlan> call = apiservice.getCoinPlan(new SessionManager(getApplicationContext()).getUserToken(),
                "default11",3);

        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultCoinPlan>() {
                @Override
                public void onResponse(Call<ResultCoinPlan> call, Response<ResultCoinPlan> response) {
                    //   Log.e("onResponseCoinPlain: ", new Gson().toJson(response.body()));
                    coinPlanArrayList.clear();
//                    coinPlanArrayList.addAll(response.body().getData().getPlans());
                }

                @Override
                public void onFailure(Call<ResultCoinPlan> call, Throwable t) {
                    //    Log.e("onErrorCoinPlain: ", t.getMessage());

                }
            });
        }

    }


    public void startPayment(String amount) {

        this.amount = amount;
        //    new backClass().execute();

    }

    String amount = "";


   /* private class backClass extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            final Checkout co = new Checkout();
            co.setImage(R.mipmap.icon);
            String id = "";
            try {
                RazorpayClient razorpay = new RazorpayClient("rzp_live_QB6JxMmCH4WD1O", "Dss4e2eiX7zSzYz8XRPNRz0v");

                JSONObject orderRequest = new JSONObject();
                double total = Double.parseDouble(amount);
                total = total * 100;
                orderRequest.put("amount", total); // amount in the smallest currency unit
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "order_rcptid_11");
                orderRequest.put("payment_capture", true);


                Order order = razorpay.Orders.create(orderRequest);

                JSONObject jsonObject = new JSONObject(String.valueOf(order));
                id = jsonObject.getString("id");
                orderid = id;
            } catch (RazorpayException | JSONException e) {
                System.out.println(e.getMessage());
            }

            try {

                JSONObject option = new JSONObject();
                option.put("name", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_name());
                option.put("description", "Total Amount");
                option.put("currency", "INR");
                option.put("order_id", id);//from response of step 3.
                //String Payment = amount;
                double total = Double.parseDouble(amount);
                total = total * 100;
                option.put("amount", total);


                JSONObject notes = new JSONObject();
                notes.put("user_id", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());

                option.put("notes", notes);

                co.open(com.welive.chat.activity.ChatActivity.this, option);

            } catch (JSONException e) {
                Toast.makeText(com.welive.chat.activity.ChatActivity.this, "Errorin Payment" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return null;
        }

    }*/

    private String subscriptionPlanId, originalPrice, newCoins, orderid;

    @Override
    public void onPaymentSuccess(String s) {
        placeOrderFunction(s);
        updateCoinApi();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void placeOrderFunction(String trnsid) {
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        RequestPlaceOrder requestPlaceOrder = new RequestPlaceOrder(subscriptionPlanId, Double.parseDouble(originalPrice),
                0, trnsid, 1, orderid);
        //  Log.e("onRequestPlaceOrder", new Gson().toJson(requestPlaceOrder));
        Call<ResultPlaceOrder> call = apiservice.placeOrder(new SessionManager(getApplicationContext()).getUserToken(),
                "default11",3, requestPlaceOrder);

        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultPlaceOrder>() {
                @Override
                public void onResponse(Call<ResultPlaceOrder> call, Response<ResultPlaceOrder> response) {
                    //  Log.e("onResponcePlaceOrder", new Gson().toJson(response.body()));
                    if (response.body().isStatus()) {
                        Toast.makeText(com.meetlive.app.activity.ChatActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResultPlaceOrder> call, Throwable t) {
                    //   Log.e("onErrorPlaceOrder", t.getMessage());
                }
            });
        }
    }

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(com.meetlive.app.activity.ChatActivity.this, 2, Integer.parseInt(callRate));
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    private int fPosition;

    /* public void openVideoChat(View view) {
         // Check user is online before make a call
         new ApiManager(getApplicationContext(), ChatActivity.this).searchUser(reciverId, "1");
     }
 */
    int walletBalance;

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GENERATE_AGORA_TOKEN) {
            AgoraTokenResponse rsp = (AgoraTokenResponse) response;

            if (rsp.getResult().getNotification() != null && rsp.getResult().getNotification().getSuccess() == 1) {
                int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;
                // Minus 2 sec to prevent balance goes into minus
                int canCallTill = talkTime - 2000;

                Intent intent = new Intent(com.meetlive.app.activity.ChatActivity.this, VideoChatActivity.class);
                intent.putExtra("TOKEN", rsp.getResult().getToken());
                intent.putExtra("ID", reciverId);
                intent.putExtra("CALL_RATE", callRate);
                intent.putExtra("UNIQUE_ID", rsp.getResult().getUnique_id());
                intent.putExtra("AUTO_END_TIME", canCallTill);
                intent.putExtra("receiver_name", reciverName);
                intent.putExtra("UID", tokenUserId);
                intent.putExtra("converID", converID);
                intent.putExtra("receiver_image", reciverProfilePic);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Server is busy, Please try again", Toast.LENGTH_SHORT).show();
            }


        } else if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;

            // if wallet balance greater than call rate , Generate token to make a call
            if (rsp.getResult().getTotal_point() >= Integer.parseInt(callRate)) {
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
                    RequestChatRoom requestChatRoom = new RequestChatRoom("FSAfsafsdf", Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                            new SessionManager(getApplicationContext()).getUserName(), new SessionManager(getApplicationContext()).getUserProfilepic(),
                            "1", Integer.parseInt(reciverId), reciverName, reciverProfilePic, "2", 0, Integer.parseInt(callRate), 0, 20, "", "countrtStstic", tokenUserId);
                    Call<ResultChatRoom> chatRoomCall = apiservice.createChatRoom("application/json", requestChatRoom,3);

                    chatRoomCall.enqueue(new Callback<ResultChatRoom>() {
                        @Override
                        public void onResponse(Call<ResultChatRoom> call, Response<ResultChatRoom> response) {
                            //     Log.e("onResponseRoom: ", new Gson().toJson(response.body()));
                            try {
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                            try {
                                if (!response.body().getData().getId().equals("")) {
                                    converID = response.body().getData().getId();

                                    new ApiManager(getApplicationContext(), com.meetlive.app.activity.ChatActivity.this).generateAgoraToken(Integer.parseInt(tokenUserId), String.valueOf(System.currentTimeMillis()), converID);


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultChatRoom> call, Throwable t) {
                            //Log.e("onResponseChatRoom: ", t.getMessage());
                            try {
                                apiManager.closeDialog();
                            } catch (Exception e) {
                            }
                            new ApiManager(getApplicationContext(), com.meetlive.app.activity.ChatActivity.this).generateAgoraToken(Integer.parseInt(tokenUserId), String.valueOf(System.currentTimeMillis()), converID);

                        }
                    });
                }

            } else {
                // Open Insufficient Coin popup
                new InsufficientCoins(com.meetlive.app.activity.ChatActivity.this, 2, Integer.parseInt(callRate));
            }
        }


        if (ServiceCode == Constant.WALLET_AMOUNT2) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getResult().getTotal_point()));
        }
        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getResult()));

            try {

                SocketSendMessage socketSendMessage = new SocketSendMessage(
                        new SessionManager(getApplicationContext()).getUserId(),
                        reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                        giftArrayList.get(fPosition).getGiftPhoto(),
                        "asd", "image/gift"
                );
                socket.emit("message.send", new Gson().toJson(socketSendMessage));
                //  Log.e("socketMessage:", new Gson().toJson(socketSendMessage));

                final Message_ message = new Message_(new SessionManager(getApplicationContext()).getUserId()
                        , giftArrayList.get(fPosition).getGiftPhoto(), "image/jpeg");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.add(message);
                        messagesView.setSelection(messagesView.getCount() - 1);
                    }
                });


            } catch (Exception e) {

            }

        }

        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;

            //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;

            //  int talkTime2 = userData.getCall_rate() * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus
            int canCallTill = talkTime - 2000;


            Intent intent = new Intent(com.meetlive.app.activity.ChatActivity.this, VideoChatActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("ID", reciverId);
            intent.putExtra("CALL_RATE", callRate);
            intent.putExtra("UNIQUE_ID", rsp.getResult().getData().getUniqueId());
            intent.putExtra("AUTO_END_TIME", canCallTill);
            intent.putExtra("receiver_name", reciverName);
            intent.putExtra("UID", tokenUserId);
            intent.putExtra("converID", converID);
            intent.putExtra("receiver_image", reciverProfilePic);
            startActivity(intent);

        }

        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponse rsp = (UserListResponse) response;

            try {
                if (rsp != null) {
                    int onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                    int busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                    if (onlineStatus == 1 && busyStatus == 0) {
                        // Check wallet balance before going to make a video call

                        // new ApiManager(getApplicationContext(), ChatActivity.this).getWalletAmount();
                        // apiManager.generateCallRequest(Integer.parseInt(tokenUserId), String.valueOf(System.currentTimeMillis()), converID, Integer.parseInt(callRate));


                    } else if (onlineStatus == 1) {
                        Toast.makeText(this, reciverName + " is Busy", Toast.LENGTH_SHORT).show();

                    } else if (onlineStatus == 0) {
                        Toast.makeText(this, reciverName + " is Offline", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
            }
        }

        if (ServiceCode == Constant.IS_CHAT_SERVICE_ACTIVE) {
            ChatPurchaseValidity rsp = (ChatPurchaseValidity) response;
            purchasePlanStatus = rsp.getResult().getChat();

            if (purchasePlanStatus == 0) {
                // Open Insufficient Coin popup
                new InsufficientCoins(com.meetlive.app.activity.ChatActivity.this, 6, 0);
                ((ImageView) findViewById(R.id.img_audio)).setEnabled(false);
                ((ImageView) findViewById(R.id.img_audio1)).setEnabled(false);
                ((ImageView) findViewById(R.id.img_gift)).setEnabled(false);
                ((ImageView) findViewById(R.id.img_smile)).setEnabled(false);
                ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
                ((EditText) findViewById(R.id.et_message)).setEnabled(false);
            }
        }

    }
}