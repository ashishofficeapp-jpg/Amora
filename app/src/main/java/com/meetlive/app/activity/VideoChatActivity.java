package com.meetlive.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.meetlive.app.R;
import com.meetlive.app.broadcast.Constants;
import com.meetlive.app.adapter.GiftAdapter;
import com.meetlive.app.adapter.MessageAdaptervdo;
import com.meetlive.app.body.CallRecordBody;
import com.meetlive.app.dialog.WaitingForConnect;
import com.meetlive.app.empadapter.MessageAdaptervdoEmployee;
import com.meetlive.app.helper.ItemClickSupport;
import com.meetlive.app.helper.NetworkCheck;
import com.meetlive.app.response.EndCallData.EndCallData;
import com.meetlive.app.response.Misscall.RequestMissCall;
import com.meetlive.app.response.Misscall.ResultMissCall;
import com.meetlive.app.response.RequestGiftRequest.RequestGiftRequest;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.gift.Gift;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.response.gift.SendGiftRequest;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.response.message.Message_;
import com.meetlive.app.response.message.ResultSendMessage;
import com.meetlive.app.retrofit.ApiClient;
import com.meetlive.app.retrofit.ApiClientChat;
import com.meetlive.app.retrofit.ApiInterface;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.socketmodel.SocketCallOperation;
import com.meetlive.app.socketmodel.SocketSendMessage;
import com.meetlive.app.socketmodel.Socketuserid;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meetlive.app.utils.SessionManager.GENDER;

@RequiresApi(api = Build.VERSION_CODES.O)
public class VideoChatActivity extends AppCompatActivity implements ApiResponseInterface {

    Chronometer chronometer;
    Date callStartTime;
    String gender;

    ////////////////////////////
    private static final String TAG = com.meetlive.app.activity.VideoChatActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private VideoCanvas mLocalView;
    private VideoCanvas mRemoteView;


    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;

    WaitingForConnect waitingForConnect;
    private static String token, call_rate, reciverId, unique_id, call_unique_id, UID, isFreeCall = "false";
    ApiManager apiManager;
    int AUTO_END_TIME;
    Handler handler, talkTimeHandler, giftRequestDismissHandler;

    // private MediaPlayer mp;
    // Customized logger view
    //  private LoggerRecyclerView mLogView;


    /*   * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.*/


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("onJoinChannelSuccess", "Join channel success, uid: " + (uid & 0xFFFFFFFFL));

                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // Cancel handler if receiver picked the call in 25 sec
                    handler.removeCallbacksAndMessages(null);
                    waitingForConnect.dismiss();
                    // mp.stop();
                    //  mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                    //set boolean value here is true if function is called 13/5/21 RatingDialog = true
                    RatingDialog = true;
                    callStartTime = Calendar.getInstance().getTime();
                    // start a chronometer
                    chronometer.start();

                    if (gender.equals("male")) {
                        apiManager.sendCallRecord(new CallRecordBody(UID, unique_id,
                                new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), "")));

                        startTimeStamp = String.valueOf(System.currentTimeMillis());
                      /*  Log.e("startCallReq", new Gson().toJson(new CallRecordBody(UID, unique_id,
                                new CallRecordBody.Duration(String.valueOf(System.currentTimeMillis()), ""))));*/

                        // Disconnect call when balance ends
                        talkTimeHandler.postDelayed(() -> {
                                    endCall();
                                    Toast.makeText(com.meetlive.app.activity.VideoChatActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                                }
                                , AUTO_END_TIME);
                    }
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft();
                    endCall();
                    finish();
                    Toast.makeText(com.meetlive.app.activity.VideoChatActivity.this, "Call Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalView.view) > -1) {
            parent = mLocalContainer;
        }
        if (mRemoteView != null) {
            return;
        }

        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteView = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteView);

        ((ImageView) findViewById(R.id.live_btn_beautification)).performClick();

    }

    private void onRemoteUserLeft() {
        removeRemoteVideo();

    }

    private void removeRemoteVideo() {
        /*if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        mRemoteView = null;*/
        removeFromParent(mRemoteView);

    }

    private MessageAdaptervdo messageAdapter;
    private MessageAdaptervdoEmployee messageAdaptervdoEmployee;
    private ListView messagesView;
    private Socket socket;
    private RecyclerView rv_gift;
    private GridLayoutManager gridLayoutManager;
    private GiftAdapter giftAdapter;
    private ArrayList<Gift> giftArrayList = new ArrayList<>();
    private ArrayList<Message_> message_arrayList = new ArrayList<>();
    private NetworkCheck networkCheck;
    private int fPosition;
    private String reciverName = "";
    private String user_name = "";
    private String reciverProfilePic = "";
    private String converID = "";
    // for gift deduction
    private String startLong = "", giftLong = "";
    private String startTimeStamp = "";
    boolean RatingDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.Black));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        networkCheck = new NetworkCheck();
        setContentView(R.layout.activity_video_chat);

        /*mp = MediaPlayer.create(this, R.raw.let_me_love_you);
        mp.start();*/

        HashMap<String, String> user = new SessionManager(this).getUserDetails();
        gender = user.get(GENDER);


        Long tsLong = System.currentTimeMillis() / 1000;
        startLong = tsLong.toString();

        initUI();

        try {
            apiManager = new ApiManager(this, this);
            token = getIntent().getStringExtra("TOKEN");
            reciverId = getIntent().getStringExtra("ID");
            call_rate = getIntent().getStringExtra("CALL_RATE");
            unique_id = getIntent().getStringExtra("UNIQUE_ID");
            call_unique_id = getIntent().getStringExtra("UNIQUE_ID");
            AUTO_END_TIME = getIntent().getIntExtra("AUTO_END_TIME", 2000);
            reciverName = getIntent().getStringExtra("receiver_name");
            reciverProfilePic = getIntent().getStringExtra("receiver_image");
            converID = getIntent().getStringExtra("converID");
            try {
                isFreeCall = getIntent().getStringExtra("is_free_call");
                UID = getIntent().getStringExtra("UID");
           /* Log.e("UID", UID);
            Log.e("call_rate", call_rate);*/
            } catch (Exception e) {
            }
           /*Log.e("TOKEN", token);
            Log.e("reciverId", reciverId);
            Log.e("unique_id", unique_id);
            Log.e("converID", converID);
            Log.e("AUTO_END", AUTO_END_TIME + "");*/
            Log.e("reciverName", reciverName);
            Log.e("reciverProfilePic", reciverProfilePic);

            if (converID.equals("") || converID.equals("null")) {
                ((RelativeLayout) findViewById(R.id.rl_chat)).setVisibility(View.GONE);
                //   ((RadioButton) findViewById(R.id.rl_giftin)).setVisibility(View.GONE);
            }
            StringBuilder sb = new StringBuilder(reciverName);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

            ((TextView) findViewById(R.id.tv_username)).setText(sb.toString());

            ((TextView) findViewById(R.id.tv_username)).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),
                    "fonts/Poppins-Regular_0.ttf"));


            //if (gender.equals("male")) {}else {
            Picasso.get()
                    .load(reciverProfilePic)
                    .into(((ImageView) findViewById(R.id.img_profilepic)));
        } catch (Exception e) {

        }


        //  Log.d("TokenAg : ", token);

        waitingForConnect = new WaitingForConnect(this, reciverProfilePic, reciverName);
        handler = new Handler();
        talkTimeHandler = new Handler();
        giftRequestDismissHandler = new Handler();

        // End call if receiver not taking the call in 25 seconds
        handler.postDelayed(() -> {
            waitingForConnect.dismiss();

            SocketCallOperation socketCallOperation = new SocketCallOperation(new SessionManager(getApplicationContext()).getUserId(), reciverId);
            socket.emit("misscall", new Gson().toJson(socketCallOperation));

            ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);
            RequestMissCall requestMissCall = new RequestMissCall(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                    Integer.parseInt(reciverId), converID, "missedvideocall");

            Call<ResultMissCall> call = apiservice.sendMissCallData(3,requestMissCall);
            if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                call.enqueue(new Callback<ResultMissCall>() {
                    @Override
                    public void onResponse(Call<ResultMissCall> call, Response<ResultMissCall> response) {
                        //   Log.e("onResponceMissApi", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<ResultMissCall> call, Throwable t) {
                        //  Log.e("onErrorMissApi", t.getMessage());
                    }
                });
            }

            endCall();
            Toast.makeText(this, "Not answering the call", Toast.LENGTH_LONG).show();

        }, 20000);

        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
        new ApiManager(this, this).getWalletAmount2();
        loadLoader();

        registerReceiver(myReceiver, new IntentFilter("FBR-ENDTHIS"));
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("end")) {
                finish();
            }
        }
    };

    public void onLocalContainerClick(View view) {
        switchView(mLocalView);
        switchView(mRemoteView);
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void loadLoader() {
        Glide.with(getApplicationContext())
                .load(R.drawable.loader)
                .into((ImageView) findViewById(R.id.img_giftloader));
    }

    private void initUI() {
        chronometer = findViewById(R.id.chronometer);
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

       if (gender.equals("male")) {
            mSwitchCameraBtn.setVisibility(View.GONE);
        } else {
            mSwitchCameraBtn.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_giftmsg)).setText("You can request for gift by just tapping on that~");
        }
        // mLogView = findViewById(R.id.log_recycler_view);

        // Sample logs are optional.
        showSampleLogs();

        ((ImageView) findViewById(R.id.img_send)).setEnabled(false);
        ((ImageView) findViewById(R.id.img_send)).setImageDrawable(getResources().getDrawable(R.drawable.inactivedownloadarrow));

        ((RelativeLayout) findViewById(R.id.rl_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.VISIBLE);

              /*  if (gender.equals("male")) {

                } else {
                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                }*/


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(((EditText) findViewById(R.id.et_message)).getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                ((EditText) findViewById(R.id.et_message)).requestFocus();
            }
        });

        mRemoteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    hideKeybaord(view);
                }
                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                    messagesView.setVisibility(View.VISIBLE);
                }
            }
        });


        initKeyBoardListener();
        Context context;
        context = this;
        messageAdapter = new MessageAdaptervdo(context, message_arrayList);
        messageAdaptervdoEmployee = new MessageAdaptervdoEmployee(context, message_arrayList);
        messagesView = (ListView) findViewById(R.id.lv_allmessages);

        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mRemoteContainer.performClick();

            }
        });

        if (gender.equals("male")) {
            messagesView.setAdapter(messageAdapter);
        } else {
            messagesView.setAdapter(messageAdaptervdoEmployee);
        }

        rv_gift = findViewById(R.id.rv_gift);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        rv_gift.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(giftArrayList, R.layout.rv_gift, getApplicationContext());
        rv_gift.setAdapter(giftAdapter);


        ((RelativeLayout) findViewById(R.id.rl_giftin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesView.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_gift)).performClick();
            }
        });

        ((ImageView) findViewById(R.id.img_gift)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeybaord(view);

                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.GONE) {
                    ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                    ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.GONE);
                    ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.GONE);
                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);
                    ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                    String authToken = Constant.BEARER + new SessionManager(getApplicationContext()).getUserToken();
                    Call<ResultGift> call = apiservice.getGift(authToken,3);
                    if (networkCheck.isNetworkAvailable(getApplicationContext())) {
                        call.enqueue(new Callback<ResultGift>() {
                            @Override
                            public void onResponse(Call<ResultGift> call, Response<ResultGift> response) {
                                // Log.e("onGift: ", new Gson().toJson(response.body()));

                                if (response.body().isStatus()) {
                                   /* ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.VISIBLE);
                                    ((RelativeLayout) findViewById(R.id.rl_msgsend)).setVisibility(View.GONE);
                                    ((RelativeLayout) findViewById(R.id.rl_end)).setVisibility(View.GONE);
                                   */
                                    ((ImageView) findViewById(R.id.img_giftloader)).setVisibility(View.GONE);

                                   // ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.VISIBLE);
                                    giftArrayList.clear();
                                    giftArrayList.addAll(response.body().getResult());
                                    giftAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultGift> call, Throwable t) {
                                //  Log.e("onErrorGift: ", t.getMessage());

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

                if (gender.equals("male")) {

                    int currentCoin = Integer.parseInt(((TextView) findViewById(R.id.tv_coinchat)).getText().toString());
                    // currentCoin = currentCoin - giftArrayList.get(position).getAmount();
                    if (currentCoin > giftArrayList.get(position).getAmount()) {
                        fPosition = position;
                        //pass here 4/5/21 Integer.parseInt(unique_id)
                        new ApiManager(getApplicationContext(), VideoChatActivity.this).sendUserGift(
                                new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id,
                                        giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount(),
                                        startTimeStamp, String.valueOf(System.currentTimeMillis())));


                 /* ResponceDataguestLogin responceDataguestLogin = new ResponceDataguestLogin(String.valueOf(currentCoin));
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
                                //  Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
                                    ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
                                    //  messagesView.setVisibility(View.VISIBLE);

                              /*int talkTime = giftArrayList.get(position).getAmount() * 1000 * 60;
                                    // Minus 2 sec to prevent balance goes into minus
                                    int canCallTill = AUTO_END_TIME - talkTime;

                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    giftLong = tsLong.toString();

                                    Log.e("talkTimeVideo", talkTime + "");
                                    Log.e("canCallTillVideo", canCallTill + "");
                                    Log.e("startTimestamp", startLong);
                                    Log.e("giftTimestamp", giftLong);
                                    Log.e("AUTO_END_TIME", AUTO_END_TIME+"");

                                    long first, second;
                                    first = Long.parseLong(startLong);
                                    second = Long.parseLong(giftLong);

                                    Log.e("TimestampDiff", String.valueOf((second - first) * 1000));

                                    startLong = giftLong;
                                    canCallTill = canCallTill - ((Integer.parseInt(giftLong) - Integer.parseInt(startLong)) * 1000);

                                    AUTO_END_TIME = canCallTill;
                                    Log.e("AUTO_END_TIME2", AUTO_END_TIME+"");

                                    if (talkTimeHandler != null) {
                                        talkTimeHandler.removeCallbacksAndMessages(null);
                                    }

                                    talkTimeHandler.postDelayed(() -> {
                                                Toast.makeText(VideoChatActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                                            }
                                            , AUTO_END_TIME);*/


                                }
                            }


                            @Override
                            public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                //Log.e("onResponseSendMessagE: ", t.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(VideoChatActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                    }
                } else {
                    apiManager.hostSendGiftRequest(new RequestGiftRequest(String.valueOf(giftArrayList.get(position).getId()), reciverId));
                }

            }
        });

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

        ((ImageView) findViewById(R.id.img_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender.equals("male")) {
                    if (((EditText) findViewById(R.id.et_message)).toString().trim().length() > 0) {
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
                        RequestBody receiverid = RequestBody.create(MediaType.parse("text/plain"),
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
                                conversationId, id, name_1, senderProfilePic, senderType, receiverid, receiverName, receiverImageUrl,
                                receiverType, body, isFriendAccept, mimeType);

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
                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain"
                                        );
                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                  /*  Log.e("socketMessage:", new Gson().toJson(socketSendMessage));
                                    Log.e("socketMessage:", new Gson().toJson(socketSendMessage));*/

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
                                //         Log.e("onResponseSendMessagE: ", t.getMessage());
                            }
                        });

                    }
                } else {
                    if (((EditText) findViewById(R.id.et_message)).toString().trim().length() > 0) {

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
                                "2");
                        RequestBody receiverid = RequestBody.create(MediaType.parse("text/plain"),
                                reciverId);
                        RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                reciverName);
                        RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                reciverProfilePic);
                        RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                "1");
                        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                ((EditText) findViewById(R.id.et_message)).getText().toString().trim());
                        RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                "1");
                        RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                "html/plain");

                        Call<ResultSendMessage> call = apiservice.sendMessage(3,UserId,
                                conversationId, id, name_1, senderProfilePic, senderType, receiverid, receiverName, receiverImageUrl,
                                receiverType, body, isFriendAccept, mimeType);

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
                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain"
                                        );
                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                       /* Log.e("socketMessage:", new Gson().toJson(socketSendMessage));
                                        Log.e("socketMessage:", "Message Sent");*/

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
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                // Log.e("onResponseSendMessagE: ", t.getMessage());

                            }
                        });
                    }
                }
            }
        });

        ((EditText) findViewById(R.id.et_message)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //do what you want on the press of 'done'
                if (gender.equals("male")) {
                    if (((EditText) findViewById(R.id.et_message)).toString().trim().length() > 0) {
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
                        RequestBody receiverid = RequestBody.create(MediaType.parse("text/plain"),
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
                                conversationId, id, name_1, senderProfilePic, senderType, receiverid, receiverName, receiverImageUrl,
                                receiverType, body, isFriendAccept, mimeType);

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
                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain"
                                        );
                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                  /*  Log.e("socketMessage:", new Gson().toJson(socketSendMessage));
                                    Log.e("socketMessage:", new Gson().toJson(socketSendMessage));*/

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
                                // Log.e("onResponseSendMessagE: ", t.getMessage());
                            }
                        });

                    }
                } else {
                    if (((EditText) findViewById(R.id.et_message)).toString().trim().length() > 0) {

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
                                "2");
                        RequestBody receiverid = RequestBody.create(MediaType.parse("text/plain"),
                                reciverId);
                        RequestBody receiverName = RequestBody.create(MediaType.parse("text/plain"),
                                reciverName);
                        RequestBody receiverImageUrl = RequestBody.create(MediaType.parse("text/plain"),
                                reciverProfilePic);
                        RequestBody receiverType = RequestBody.create(MediaType.parse("text/plain"),
                                "1");
                        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                                ((EditText) findViewById(R.id.et_message)).getText().toString().trim());
                        RequestBody isFriendAccept = RequestBody.create(MediaType.parse("text/plain"),
                                "1");
                        RequestBody mimeType = RequestBody.create(MediaType.parse("text/plain"),
                                "html/plain");

                        Call<ResultSendMessage> call = apiservice.sendMessage(3,UserId,
                                conversationId, id, name_1, senderProfilePic, senderType, receiverid, receiverName, receiverImageUrl,
                                receiverType, body, isFriendAccept, mimeType);

                        call.enqueue(new Callback<ResultSendMessage>() {
                            @Override
                            public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                                //           Log.e("onResponseSendMessage: ", new Gson().toJson(response.body()));
                                try {
                                    if (response.body().getData().get(0).getStatus().equals("sent")) {

                                        SocketSendMessage socketSendMessage = new SocketSendMessage(
                                                new SessionManager(getApplicationContext()).getUserId(),
                                                reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                                                ((EditText) findViewById(R.id.et_message)).getText().toString().trim(),
                                                new SessionManager(getApplicationContext()).getUserProfilepic(), "html/plain"
                                        );
                                        socket.emit("message.send", new Gson().toJson(socketSendMessage));
                                       /*  Log.e("socketMessage:", new Gson().toJson(socketSendMessage));
                                        Log.e("socketMessage:", "Message Sent");*/

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
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                                //Log.e("onResponseSendMessagE: ", t.getMessage());

                            }
                        });
                    }
                }
                return true;
            }
        });

        loadGiftData();

    }

    private void loadGiftData() {
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        String authToken = Constant.BEARER + new SessionManager(getApplicationContext()).getUserToken();
        Call<ResultGift> call = apiservice.getGift(authToken,3);
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultGift>() {
                @Override
                public void onResponse(Call<ResultGift> call, Response<ResultGift> response) {

                    if (response.body().isStatus()) {

                        giftArrayList.clear();
                        giftArrayList.addAll(response.body().getResult());
                        giftAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ResultGift> call, Throwable t) {

                }
            });
        }

    }

    private void showSampleLogs() {
//        mLogView.logI("Welcome to Agora 1v1 video call");
//        mLogView.logW("You will see custom logs here");
//        mLogView.logE("You can also use this to show errors");
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();
        https:
//console.agora.io/invite?sign=YXBwSWQlM0QyYWY5M2VlODZlYWU0OGQxOTA4NGNjODhlOWUzYzkyNCUyNm5hbWUlM0RsaXZlJTI2dGltZXN0YW1wJTNEMTU5MTEwNTU5MCUyNmNoYW5uZWwlM0RkZW1vJTI2dG9rZW4lM0Q%3D

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid asactivity_video_call ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalView = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalView);

    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        /*String token = getString(R.string.agora_access_token);*/
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        //   mRtcEngine.joinChannel(token, "zeeplive", "Extra Optional Data", 0);
        mRtcEngine.joinChannel(token, "zeeplive" + unique_id, "Extra Optional Data", 0);
        //  Toast.makeText(this, "zeeplive" + unique_id, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.off("message.get");
        if (!mCallEnd) {
            leaveChannel();

        }
        //mp.stop();
        //    endCall();
        RtcEngine.destroy();

        unregisterReceiver(myGiftReceiver);
        unregisterReceiver(myReceiver);
        //set boolean value here is true if function is called 13/5/21 RatingDialog = false
        if (RatingDialog) {
            getRating();
            //rate dialog show for rating
            // new FemaleSideDialog(VideoChatActivity.this, receiverName, AUTO_END_TIME, profileImage);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        EndCallData endCallData = new EndCallData(unique_id, String.valueOf(System.currentTimeMillis()));
        list.add(endCallData);
        new SessionManager(getApplicationContext()).setUserEndcalldata(list);
        new SessionManager(getApplicationContext()).setUserGetendcalldata("error");

//        startPictureInPictureFeature();
    }

/*
    PictureInPictureParams.Builder pictureInPictureParamsBuilder = new PictureInPictureParams.Builder();

    public void startPictureInPictureFeature(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Rational aspectRatio = new Rational(((RelativeLayout) findViewById(R.id.activity_video_chat_view)).getWidth(),
                    ((RelativeLayout) findViewById(R.id.activity_video_chat_view)).getHeight());
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (!isInPictureInPictureMode()) {
            Rational aspectRatio = new Rational(((RelativeLayout) findViewById(R.id.activity_video_chat_view)).getWidth(),
                    ((RelativeLayout) findViewById(R.id.activity_video_chat_view)).getHeight());
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        if (isInPictureInPictureMode) {

            ((RelativeLayout) findViewById(R.id.rl_info)).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rl_close)).setVisibility(View.GONE);
            ((RadioButton) findViewById(R.id.rl_giftin)).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rl_pip)).setVisibility(View.GONE);
            ((FrameLayout) findViewById(R.id.local_video_view_container)).setVisibility(View.GONE);

        } else {
            ((RelativeLayout) findViewById(R.id.rl_info)).setVisibility(View.VISIBLE);
            ((RelativeLayout) findViewById(R.id.rl_close)).setVisibility(View.VISIBLE);
            ((RadioButton) findViewById(R.id.rl_giftin)).setVisibility(View.VISIBLE);
            ((RelativeLayout) findViewById(R.id.rl_pip)).setVisibility(View.VISIBLE);
            ((FrameLayout) findViewById(R.id.local_video_view_container)).setVisibility(View.VISIBLE);
        }
    }
*/

    private void leaveChannel() {
        try {
            mRtcEngine.leaveChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);

        } else {

            //endCall();

          /*  mCallEnd = true;
            mCallBtn.setImageResource(R.drawable.btn_startcall);
            mCallBtn.setVisibility(View.GONE);*/
            onBackPressed();
        }

        showButtons(!mCallEnd);

    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();

        // Calculate call charges accordingly
        getCallDuration(Calendar.getInstance().getTime());
    }

    private void removeLocalVideo() {
       /* if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;*/
        removeFromParent(mLocalView);
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        // mMuteBtn.setVisibility(visibility);
        //   mSwitchCameraBtn.setVisibility(visibility);
    }


    @Override
    public void onBackPressed() {
        if (((RelativeLayout) findViewById(R.id.rl_bottom)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
            //hideKeybaord(view);
        }
        if (((RelativeLayout) findViewById(R.id.rl_gift)).getVisibility() == View.VISIBLE) {
            ((RelativeLayout) findViewById(R.id.rl_gift)).setVisibility(View.GONE);
            messagesView.setVisibility(View.VISIBLE);
        } else {

            final Dialog dialog = new Dialog(com.meetlive.app.activity.VideoChatActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.disconnectcalldialog);

            DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
            // getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //int height = (int) (metrics.heightPixels * 0.9); //set height to 90% of total
            int width = (int) (metrics.widthPixels * 0.9); //set width to 90% of total

            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //   dialog.getWindow().setLayout(width, height); //set layout

            TextView text = (TextView) dialog.findViewById(R.id.msg);
            TextView tv_dailogcancel = (TextView) dialog.findViewById(R.id.tv_dailogcancel);
            TextView tv_dailogconfirm = (TextView) dialog.findViewById(R.id.tv_dailogconfirm);
            if (gender.equals("male")) {
                text.setText("Are you sure to close the video call?");
            } else {
                text.setText("If you hang up this video call now, you will not recevie coins for this present video call. Are you sure to do that?");
            }

            tv_dailogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            tv_dailogconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!gender.equals("male")) {
                        apiManager.getcallCutByHost(unique_id);
                        finish();
                    } else {
                        endCall();
                    }
                }
            });

            dialog.show();
        }
    }


    ArrayList<EndCallData> list = new ArrayList<>();

    void getCallDuration(Date endTime) {

        if (callStartTime != null) {
            long mills = endTime.getTime() - callStartTime.getTime();

            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;
            int sec = (int) (mills - hours * 3600000 - mins * 60000) / 1000;


            if (mins < 1 && sec > 1 && sec < 60) {

                apiManager.endCall(new CallRecordBody("", unique_id, Boolean.parseBoolean(isFreeCall),
                        new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));


                if (gender.equals("male")) {
                    endAllApi();
                }
            } else {
                int roundOf = 1;
                int totalMins;

                if (sec > 6) {
                    totalMins = mins + roundOf;
                } else {
                    totalMins = mins;
                }

                apiManager.endCall(new CallRecordBody("", unique_id,
                        new CallRecordBody.Duration("", String.valueOf(System.currentTimeMillis()))));
                if (gender.equals("male")) {
                    endAllApi();
                }
            }
        } else {
            if (talkTimeHandler != null) {
                talkTimeHandler.removeCallbacksAndMessages(null);
            }

            finish();
        }
    }

    public void getRating() {
        String call_duration = chronometer.getText().toString();
        if (new SessionManager(getApplicationContext()).getGender().equals("male")) {
            //rate dialog show for rating
           Intent intent = new Intent(VideoChatActivity.this, RateDialogActivity.class);
            intent.putExtra("host_name", reciverName);
            intent.putExtra("host_id", String.valueOf(UID));
            intent.putExtra("end_time", String.valueOf(call_duration));
            intent.putExtra("host_image", reciverProfilePic);
            startActivity(intent);
            finish();
        } else if (new SessionManager(getApplicationContext()).getGender().equals("female")) {
          /*  Intent intent = new Intent(VideoChatActivity.this, FemaleSideDialogActivity.class);
            intent.putExtra("user_image", reciverProfilePic);
            intent.putExtra("user_name", reciverName);
            intent.putExtra("end_time", String.valueOf(call_duration));
            startActivity(intent);
            finish();
       */
        }

    }

    private void endAllApi() {
        String duration = chronometer.getText().toString();

        ApiInterface apiservice = ApiClientChat.getClient().create(ApiInterface.class);

        RequestMissCall requestMissCall = new RequestMissCall(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()),
                Integer.parseInt(reciverId), converID, "videocall",
                duration, 0, "00000000000");

        //    Log.e("reqEndTime", new Gson().toJson(requestMissCall));

        SocketCallOperation socketCallOperation = new SocketCallOperation(new SessionManager(getApplicationContext()).getUserId(), reciverId);
        socket.emit("callDone", new Gson().toJson(socketCallOperation));


        Call<ResultMissCall> call = apiservice.sendMissCallData(3,requestMissCall);
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            call.enqueue(new Callback<ResultMissCall>() {
                @Override
                public void onResponse(Call<ResultMissCall> call, Response<ResultMissCall> response) {
                    //             Log.e("onResponceCallEnd", new Gson().toJson(response.body()));
                }

                @Override
                public void onFailure(Call<ResultMissCall> call, Throwable t) {
                    //            Log.e("onErrorCallEnd", t.getMessage());
                }
            });
        }
    }


    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
      /*  if (ServiceCode == Constant.DEDUCT_CALL_CHARGE) {
            WalletRechargeResponse rsp = (WalletRechargeResponse) response;

            finish();
        }*/
        if (ServiceCode == Constant.SEND_CALL_RECORD) {
            Object rsp = response;
        }
        if (ServiceCode == Constant.END_CALL) {
            Object rsp = response;

            if (talkTimeHandler != null) {
                talkTimeHandler.removeCallbacksAndMessages(null);
            }
            chronometer.stop();
            finish();

        }

        if (ServiceCode == Constant.WALLET_AMOUNT2) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getResult().getTotal_point()));
        }

        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
            ((TextView) findViewById(R.id.tv_coinchat)).setText(String.valueOf(rsp.getResult()));


            try {

              /*  SocketSendMessage socketSendMessage = new SocketSendMessage(
                        new SessionManager(getApplicationContext()).getUserId(),
                        reciverId, converID, new SessionManager(getApplicationContext()).getUserName(),
                        giftArrayList.get(fPosition).getGiftPhoto(),
                        new SessionManager(getApplicationContext()).getUserProfilepic(), "image/gift"
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
                });*/

                long tsLong = System.currentTimeMillis() / 1000;
                giftLong = Long.toString(tsLong);

                int didu = ((Integer.parseInt(giftLong) - Integer.parseInt(startLong)) / 60);
                didu = didu + 1;
                didu = didu * Integer.parseInt(call_rate);
                didu = rsp.getResult() - didu;
                //didu = didu - 2000;
                int talkTime = didu / Integer.parseInt(call_rate) * 1000 * 60;

                //   Log.e("AUTO_END_TIME2", AUTO_END_TIME + "");

                AUTO_END_TIME = talkTime;
                //  startLong = giftLong;

           /*     Log.e("TimestampDiff", didu + "");
                Log.e("walletBalance", rsp.getResult() + "");
                Log.e("AUTO_END_TIME", AUTO_END_TIME + "");
*/
                if (talkTimeHandler != null) {
                    talkTimeHandler.removeCallbacksAndMessages(null);
                }

                talkTimeHandler.postDelayed(() -> {
                            Toast.makeText(VideoChatActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                            endCall();
                        }
                        , AUTO_END_TIME);

                giftAnimation(fPosition);

            } catch (Exception e) {

            }

        }
    }

    private void giftAnimation(int position) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);
        switch (giftArrayList.get(position).getId()) {
            case 18:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.heart);
                break;
            case 21:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lips);
                break;
            case 22:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.bunny);
                break;
            case 23:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                break;
            case 24:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.boygirl);
                break;
            case 25:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.sandle);
                break;
            case 26:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.frock);
                break;
            case 27:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.car);
                break;
            case 28:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.ship);
                break;
            case 29:
                ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.tajmahal);
                break;
        }
        //   ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
        animFadeIn.reset();
        ((ImageView) findViewById(R.id.img_imageShow)).clearAnimation();
        ((ImageView) findViewById(R.id.img_imageShow)).startAnimation(animFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.GONE);
            }
        }, 3000);
    }


    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
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
                        //             Log.e("Keyboard", "SHOW");
                        messagesView.setSelection(messagesView.getCount() - 1);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        //           Log.e("Keyboard", "HIDE");
                        ((RelativeLayout) findViewById(R.id.rl_bottom)).setVisibility(View.GONE);
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    @Override
    protected void onResume() {
        initSocket();
        super.onResume();

        registerReceiver(myGiftReceiver, new IntentFilter("FBR-GIFTREC"));

    }

    public void initSocket() {
        try {
            socket = IO.socket(ApiClient.SOCKET_URL);
            socket.connect();
            Socketuserid socketuserid = new Socketuserid(Integer.parseInt(new SessionManager(getApplicationContext()).getUserId()));
            socket.emit("login", new Gson().toJson(socketuserid));
            //        Log.e("SocVDOcallStatus: ", new SessionManager(getApplicationContext()).getUserId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //  Log.e("connectError: ", e.getMessage());
        }

        socket.on("message.get", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*       Log.e("socketMessage:", "Message Rec");

                         */
                        //        Log.e("socketRecData:", new Gson().toJson(args[0]));
                        JSONObject data = (JSONObject) args[0];
                        try {

                            //      Log.e("socketData:", data.getString("data"));
                            JSONObject data2 = data.getJSONObject("data");
                            //      Log.e("from id:", data2.getString("from"));
                            //    Log.e("from message:", data2.getString("message"));
                            String body = data2.getString("message");
                            String id = data2.getString("from");
                            String mimeType = data2.getString("mimeType");
                            String conversationId = data2.getString("conversationId");

                            if (!body.equals("")) {
                                if (converID.equals(conversationId)) {
                                    final Message_ message = new Message_(id, body, mimeType);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (gender.equals("male")) {
                                                messageAdapter.add(message, reciverProfilePic);
                                            } else {
                                                messageAdaptervdoEmployee.add(message, reciverProfilePic);
                                            }
                                            messagesView.setSelection(messagesView.getCount() - 1);
                                        }
                                    });

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Log.e("socketError:", e.getMessage());
                        }


                    }
                });
            }
        });

    }

    public BroadcastReceiver myGiftReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            String from = intent.getStringExtra("from");
            int giftId = Integer.parseInt(action);
            if (from.equals("send")) {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.VISIBLE);
                switch (giftId) {
                    case 18:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.heart);
                        break;
                    case 21:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.lips);
                        break;
                    case 22:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.bunny);
                        break;
                    case 23:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                        break;
                    case 24:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.boygirl);
                        break;
                    case 25:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.sandle);
                        break;
                    case 26:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.frock);
                        break;
                    case 27:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.car);
                        break;
                    case 28:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.ship);
                        break;
                    case 29:
                        ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.tajmahal);
                        break;
                }
                //   ((ImageView) findViewById(R.id.img_imageShow)).setImageResource(R.drawable.rose);
                animFadeIn.reset();
                ((ImageView) findViewById(R.id.img_imageShow)).clearAnimation();
                ((ImageView) findViewById(R.id.img_imageShow)).startAnimation(animFadeIn);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.img_imageShow)).setVisibility(View.GONE);
                    }
                }, 3000);
            } else {
                ((RelativeLayout) findViewById(R.id.rl_incominggiftrequest)).setVisibility(View.VISIBLE);

                try {
                    if (giftRequestDismissHandler != null) {
                        giftRequestDismissHandler.removeCallbacksAndMessages(null);
                    }
                } catch (Exception e) {
                }

                giftRequestDismissHandler.postDelayed(() -> {
                            ((RelativeLayout) findViewById(R.id.rl_incominggiftrequest)).setVisibility(View.GONE);
                        }
                        , 10000);


                switch (giftId) {
                    case 18:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.heart);
                        break;
                    case 21:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.lips);
                        break;
                    case 22:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.bunny);
                        break;
                    case 23:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.rose);
                        break;
                    case 24:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.boygirl);
                        break;
                    case 25:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.sandle);
                        break;
                    case 26:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.frock);
                        break;
                    case 27:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.car);
                        break;
                    case 28:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.ship);
                        break;
                    case 29:
                        ((ImageView) findViewById(R.id.img_giftrequest)).setImageResource(R.drawable.tajmahal);
                        break;
                }

                ((TextView) findViewById(R.id.tv_sendGift)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RelativeLayout) findViewById(R.id.rl_incominggiftrequest)).setVisibility(View.GONE);

                        int position = 0;
                        for (int i = 0; i < giftArrayList.size(); i++) {
                            if (giftArrayList.get(i).getId() == giftId) {
                                position = i;
                            }
                        }

                        int currentCoin = Integer.parseInt(((TextView) findViewById(R.id.tv_coinchat)).getText().toString());
                        // currentCoin = currentCoin - giftArrayList.get(position).getAmount();
                        if (currentCoin > giftArrayList.get(position).getAmount()) {
                            fPosition = position;
                            //new value remove unique_id sendUserGift api 18/5/21 Integer.parseInt(unique_id)
                            new ApiManager(getApplicationContext(), VideoChatActivity.this).sendUserGift(new SendGiftRequest(Integer.parseInt(reciverId), call_unique_id,
                                    giftArrayList.get(position).getId(), giftArrayList.get(position).getAmount(), startTimeStamp, String.valueOf(System.currentTimeMillis())));

                        } else {
                            Toast.makeText(com.meetlive.app.activity.VideoChatActivity.this, "Out of Balance", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

        }
    };


    public void onBeautyClicked(View view) {
        view.setActivated(!view.isActivated());
        mRtcEngine.setBeautyEffectOptions(view.isActivated(),
                Constants.DEFAULT_BEAUTY_OPTIONS);
    }
}