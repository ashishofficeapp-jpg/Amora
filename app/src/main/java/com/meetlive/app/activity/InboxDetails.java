package com.meetlive.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.meetlive.app.dialog.GiftDailogNew;
import com.yalantis.ucrop.UCrop;
import com.meetlive.app.R;
import com.meetlive.app.adapter.ChatConversationAdapter;
import com.meetlive.app.databinding.ActivityInboxDetailsBinding;
import com.meetlive.app.dialog.InsufficientCoins;
import com.meetlive.app.response.Call.ResultCall;
import com.meetlive.app.response.ChatMessage;
import com.meetlive.app.response.ChatPurchaseValidity;
import com.meetlive.app.response.FirebaseChat.ContactListCustomResponse;
import com.meetlive.app.response.MessageCallData.MessageCallDataRequest;
import com.meetlive.app.response.MessageCallData.MessageCallDataResponce;
import com.meetlive.app.response.MyResponse;
import com.meetlive.app.response.NotificationData;
import com.meetlive.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.meetlive.app.response.Sender;
import com.meetlive.app.response.Token;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.gift.SendGiftRequest;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.retrofit.ApiInterface;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.retrofit.FirebaseApiClient;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.RecyclerTouchListener;
import com.meetlive.app.utils.SessionManager;
import com.meetlive.app.utils.UploadImageUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meetlive.app.utils.SessionManager.NAME;
import static com.meetlive.app.utils.SessionManager.PROFILE_ID;

public class InboxDetails extends AppCompatActivity implements ApiResponseInterface {

    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    DatabaseReference userRef, chatRequestRef, contactRef, rootRef, onlineStatusRef, seenMessage;
    StorageTask uploadTask;
    SessionManager sessionManager;
    String currentUserId, currentUserName, receiverName, receiverImage, userid, newParem;
    public String receiverUserId;
    // Api taking time to get call rate so i default value is 25 to prevent crash
    String callRate = "25";
    ActivityInboxDetailsBinding binding;
    List<ChatMessage> chatList = new ArrayList<>();
    ChatConversationAdapter chatConversationAdapter;

    private ApiInterface firebaseApiService;
    private ApiManager apiManager;
    boolean notify = false;
    int purchasePlanStatus = -1;
    //ValueEventListener seenListener;
    EmojiconEditText messageEdittext;
    boolean isFirstMessage = true;
    String lastMessage, lastMessageType;
    long lastMessageTime;
    String currentReceiverToken;
    List<String> msgKeyList = new ArrayList<>();
    int currentCoin;
    String callType = "video";
    private String convId = "";
    int walletBalance;
    int receiverUnreadMessageCount;
    boolean isReceiverOnline;
    ImageView loader;
    private boolean success, canChat = false;
    private int remGiftCard = 0;
    private String freeSeconds, serverDate;
    private int chatStatus = 0;
    int onlineStatus, busyStatus;
    //Pagination Variables
    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES = 25;
    //private int currentPage = PAGE_START;
    LinearLayoutManager layoutManager;
    int index = 0;
    String lastNodeId;
    int itemSlot = 21;
    //define variable for elastic search
    private List<String> searchWordList;
    private List<String> splitedValue;
    private String inputSentence;
    private String gender = "male";
    private static final String Tag = "InboxDetails";

    /*private TabLayout tabLayout;
    private ViewPager tabViewpager;
    private GiftMenuPagerAdapter giftMenuPagerAdapter;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inbox_details);
        binding.setClickListener(new EventHandler(this));
        //getting array list here for matching words
        searchWordList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.searchWordsArray)));
        //EmojiconEditText not supporting databinding thats why i have initialize here
        messageEdittext = findViewById(R.id.et_message);
        EmojIconActions emojIcon = new EmojIconActions(this, binding.parentLayout, messageEdittext, binding.imgSmile);
        emojIcon.ShowEmojIcon();



        loader = findViewById(R.id.img_loader);
        Glide.with(getApplicationContext()).load(R.drawable.loader).into(loader);
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserDetails().get(PROFILE_ID);
        currentUserName = sessionManager.getUserDetails().get(NAME);

        newParem = getIntent().getStringExtra("newParem");
        receiverUserId = getIntent().getStringExtra("receiver_id");
        //new code 4/5/21 for uniqueId pass
        receiverName = getIntent().getStringExtra("receiver_name");
        receiverImage = getIntent().getStringExtra("receiver_image");

        //getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, GiftPagerFragment.getInstance()).commit();

        /*tabLayout = findViewById(R.id.tabLayout);
        tabViewpager = findViewById(R.id.tabViewpager);*/

        binding.tvUsername.setText(receiverName);
        Glide.with(getApplicationContext()).load(receiverImage).apply(new RequestOptions().placeholder(R.drawable.default_profile)
                .apply(RequestOptions.circleCropTransform()).error(R.drawable.default_profile)).into(binding.imgProfile);

        firebaseApiService = FirebaseApiClient.getClient().create(ApiInterface.class);
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        contactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        onlineStatusRef = FirebaseDatabase.getInstance().getReference().child("Online_Status");

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /*layoutManager.setStackFromEnd(false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setReverseLayout(true);*/
        binding.chatRecyclerview.setLayoutManager(layoutManager);


        chatConversationAdapter = new ChatConversationAdapter(this, chatList, currentUserId, receiverUserId);
        binding.chatRecyclerview.setAdapter(chatConversationAdapter);

        seenMessage();
        getReceiverUnreadMessageCount();
        getChatData();
        isReceiverOnline();
        //updateToken(FirebaseInstanceId.getInstance().getToken());
        apiManager = new ApiManager(this, this);
        apiManager.searchUser(String.valueOf(receiverUserId), "1");

        if (gender.equals("male")) {
            apiManager.getWalletAmount();

            MessageCallDataRequest messageCallDataRequest = new MessageCallDataRequest(receiverUserId);
            apiManager.getMessageCallDataFunction(messageCallDataRequest);
            ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.VISIBLE);
        } else {
            //((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.img_video_call)).setVisibility(View.VISIBLE);
        }

        binding.chatRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext()
                , binding.chatRecyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                view.findViewById(R.id.sent_gift_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendGift(position);
                    }
                });

                view.findViewById(R.id.call_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeRequestedCall(position);
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

       /* binding.chatRecyclerview.addOnScrollListener(new PaginationScrollListenerLinear(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                // mocking network delay for API call
                new Handler().postDelayed(() -> {

                    if (lastNodeId != null) {
                        loader.setVisibility(View.VISIBLE);
                        getMoreChatData();
                    }
                }, 100);
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
        });*/

    }

    void makeRequestedCall(int position) {
        if (sessionManager.getGender().equals("male")) {

            if (currentCoin >= Integer.parseInt(callRate)) {

                if (onlineStatus == 1 && busyStatus == 0) {
                    apiManager.getRemainingGiftCardFunction();

                    //Colon for seprator and used to split value
                    String tagLine = "Call me :" + 1;

                    ChatMessage message = new ChatMessage(chatList.get(position).getFrom(), tagLine,
                            chatList.get(position).getType(), chatList.get(position).getTime_stamp(), true);

                    chatList.set(position, message);
                    chatConversationAdapter.notifyItemChanged(position);

                    FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId)
                            .child(receiverUserId).child(msgKeyList.get(position)).setValue(message);


                } else if (onlineStatus == 1) {
                    if (busyStatus == 1) {
                        Toast.makeText(this, "User is busy", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User is offline", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Insufficient Coin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void sendGift(int position) {
        String[] leaveType = chatList.get(position).getMessage().split(":");
        String giftId = leaveType[1];
        String giftAmount = leaveType[3];
        int status = 1;

        //int position = getAdapterPosition();
        //Colon for seprator and used to split value
        String tagLine = "Please give me this gift :" + giftId + ":" + status + ":" + giftAmount;

        ChatMessage message = new ChatMessage(chatList.get(position).getFrom(), tagLine,
                chatList.get(position).getType(), chatList.get(position).getTime_stamp(), true);
        // Send gift from coming request button
        if (sessionManager.getGender().equals("male")) {

            if (currentCoin >= Integer.parseInt(giftAmount)) {
                chatList.set(position, message);
                chatConversationAdapter.notifyItemChanged(position);
                FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId)
                        .child(receiverUserId).child(msgKeyList.get(position)).setValue(message);

                sendRequestedGift(giftId, giftAmount);

            } else {
                Toast.makeText(InboxDetails.this, "Out of balance", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void isReceiverOnline() {
        onlineStatusRef.child(receiverUserId).child("is_online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if (snapshot.hasChild("is_online")) {
                if (snapshot.getValue() != null) {
                    boolean status = (boolean) snapshot.getValue();
                    if (status) {
                        isReceiverOnline = true;
                        //Reset Value when receiver comes online
                        receiverUnreadMessageCount = 0;
                    } else {
                        isReceiverOnline = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void getReceiverUnreadMessageCount() {
        contactRef.child(receiverUserId).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    ContactListCustomResponse userDetail = snapshot.getValue(ContactListCustomResponse.class);
                    receiverUnreadMessageCount = userDetail.getUnread_message();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //new code for send gift 12/03/2021
    void sendRequestedGift(String giftId, String giftAmount) {
        // If Gift item clicked by male user gift item will be send
        if (currentCoin > Integer.parseInt(giftAmount)) {
          /*  apiManager.sendUserGift(new SendGiftRequest(Integer.valueOf(receiverUserId),
                    Integer.parseInt(giftId), Integer.parseInt(giftAmount)));*/
            ///pass Integer.parseInt(uniqueId) hide here
            //pass here call_unique_id for gift Send 27/5/21 pass blanck
            apiManager.sendUserGift(new SendGiftRequest(Integer.parseInt(receiverUserId), "",
                    Integer.parseInt(giftId), Integer.parseInt(giftAmount), String.valueOf(System.currentTimeMillis()),
                    String.valueOf(System.currentTimeMillis())));
            // Send gift from firebase
            sendMessage("gift", giftId, giftAmount);
        } else {
            Toast.makeText(this, "Out of balance", Toast.LENGTH_SHORT).show();
        }
    }

    /*void getChatData() {
        // rootRef.child("Messages").child(currentUserId).child(receiverUserId).limitToLast(itemSlot).

        Query msgRef = FirebaseDatabase.getInstance().getReference().child("Messages").
                child(currentUserId).child(receiverUserId).limitToLast(itemSlot);

        msgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isLoading = false;

                // Getting user contact list
                if (snapshot.hasChildren()) {
                    loader.setVisibility(View.VISIBLE);
                }

                ChatMessage message = snapshot.getValue(ChatMessage.class);
                chatList.add(message);
                msgKeyList.add(snapshot.getKey());
                chatConversationAdapter.notifyItemInserted(chatList.size());

                // Store last message key of first slot
                if (chatList.size() == itemSlot) {
                    lastNodeId = snapshot.getKey();
                }

                if (chatList.size() > 0) {
                    binding.chatRecyclerview.smoothScrollToPosition(chatList.size());
                }

                // Save last message
                storeLastMessage(message.getMessage(), message.getType(), message.getTime_stamp());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dismissDialog();
                isLoading = false;
            }
        });
    }*/

    void getChatData() {
        rootRef.child("Messages").child(currentUserId).child(receiverUserId).limitToLast(itemSlot).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ChatMessage message = snapshot.getValue(ChatMessage.class);
                chatList.add(message);
                msgKeyList.add(snapshot.getKey());
                chatConversationAdapter.notifyItemInserted(chatList.size());

                if (chatList.size() > 0) {
                    binding.chatRecyclerview.smoothScrollToPosition(chatList.size());
                }

                //Save last message
                storeLastMessage(message.getMessage(), message.getType(), message.getTime_stamp());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dismissDialog();
                isLoading = false;
            }
        });
    }




    /*void getMoreChatData() {
        Query msgRef = FirebaseDatabase.getInstance().getReference().child("Messages").
                child(currentUserId).child(receiverUserId).orderByKey().endAt(lastNodeId).limitToLast(itemSlot);

        msgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (isLoading) {
                    isLoading = false;
                }

                ChatMessage message = snapshot.getValue(ChatMessage.class);
                chatList.add(message);
                msgKeyList.add(snapshot.getKey());
                chatConversationAdapter.notifyItemInserted(chatList.size());

                // Store last message key of slot
                if (chatList.size() == itemSlot) {
                    lastNodeId = snapshot.getKey();
                }

                if (chatList.size() > 0) {
                    binding.chatRecyclerview.smoothScrollToPosition(chatList.size());
                }

                // Save last message
                storeLastMessage(message.getMessage(), message.getType(), message.getTime_stamp());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dismissDialog();
            }
        });
    }*/

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void onBack() {
            onBackPressed();
        }

        public void sendRequest() {
            if (purchasePlanStatus == -1) {
                apiManager.isChatServicePurchased();

            } else if (purchasePlanStatus == 0) {
                //Open Insufficient Coin Dialog
                new InsufficientCoins(mContext, 6, 0);

            } else if (purchasePlanStatus == 1) {
                //sendChatRequest();
            }
        }

        public void clickSendMessage() {

            if (sessionManager.getGender().equals("male")) {
                if (chatStatus != 0) {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String getCurrentDateTime = sdf.format(c.getTime());
                    String getMyTime = serverDate;
                    Log.e("getCurrentDateTime", getCurrentDateTime);

                    if (getCurrentDateTime.compareTo(getMyTime) == 0) {
                        // true
                        sendMessage("text", "placeholder", "0");

                    } else if (getCurrentDateTime.compareTo(getMyTime) < 0) {
                        // true
                        sendMessage("text", "placeholder", "0");
                    } else {
                        //false
                        new InsufficientCoins(mContext, 6, 0);
                    }
                } else {
                    new InsufficientCoins(mContext, 6, 0);
                }


            } else {
                sendMessage("text", "placeholder", "0");
            }
        }

        public void openGiftLayout() {
           // RelativeLayout rl_gift = findViewById(R.id.rl_gift);
           // rl_gift.setVisibility(View.VISIBLE);
            //new GiftDialog(InboxDetails.this, receiverUserId);
            GiftDailogNew customDailog= new GiftDailogNew(InboxDetails.this, receiverUserId);
            customDailog.show(getSupportFragmentManager(),"customDailog");
        }

        public void clickSendAttachment() {
            pickImage();
        }

        /*public void makeVideoCall() {
            openVideoChat();
        }*/
    }

    public void sendGiftFun(String giftId) {

    }
    /*public void openVideoChat() {
        // Check user is online before make a call

        try {
            if (new SessionManager(this).getUserWallet() > callRate) {
                apiManager.searchUser(receiverUserId, "1");
            } else {
                new InsufficientCoins(this, 2, callRate);
            }
        } catch (Exception e) {
            apiManager.searchUser(receiverUserId, "1");
        }
    }*/

    //new for make VideoCall code 12/03/2021
    public void makeVideoCall(View view) {
        // Check user is online before make a call

        if (sessionManager.getGender().equals("male")) {
            apiManager.getRemainingGiftCardFunction();

        } else {
            sendMessage("call_request", "placeholder", "0");
        }


        /*try {
            try {
                if (remGiftCard > 0) {
                    apiManager.searchUser(String.valueOf(receiverUserId), "1");
                    return;
                }
            } catch (Exception e) {
            }
            if (new SessionManager(getApplicationContext()).getUserWallet() > Integer.parseInt(callRate)) {
                apiManager.searchUser(String.valueOf(receiverUserId), "1");
            } else {
                new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
            }
        } catch (Exception e) {
            apiManager.searchUser(String.valueOf(receiverUserId), "1");
        }*/
    }

    @Override
    public void onBackPressed() {
        ContactListCustomResponse customResponse = new ContactListCustomResponse();
        customResponse.setUid(receiverUserId);
        customResponse.setImage(receiverImage);
        customResponse.setName(receiverName);
        customResponse.setStatus("Demo");
        customResponse.setUnread_message(0);
        customResponse.setMessage(new ChatMessage(currentUserId, lastMessage, lastMessageType, lastMessageTime, true));

        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("last_message", customResponse);
            intent.putExtras(bundle);
            intent.putExtra("is_first_message", isFirstMessage);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void pickImage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                //File file = new File(FileUtils.getPath(this, uri));
                try {
                    sendImage(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image not supported", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = new UploadImageUtil().getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                new UploadImageUtil().openCropActivity(sourceUri, destinationUri, this);
            } catch (Exception e) {
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void sendImage(Uri selectedImageUri) throws IOException {
        loader.setVisibility(View.VISIBLE);
        // Compress Image
        File f = new File(selectedImageUri.getPath());
        File compressedImageFile = new Compressor(this).compressToFile(f);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");
        String msgSenderRef = "Messages/" + currentUserId + "/" + receiverUserId;
        String msgReceiverRef = "Messages/" + receiverUserId + "/" + currentUserId;

        DatabaseReference dbReference = rootRef.child("Messages").child(currentUserId).child(receiverUserId).push();
        String messagePushId = dbReference.getKey();

        StorageReference filePath = storageReference.child(messagePushId + ".jpg");
        uploadTask = filePath.putFile(Uri.fromFile(compressedImageFile));

        uploadTask.continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filePath.getDownloadUrl();
        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
            Uri downloadUrl = task.getResult();
            String myUrl = downloadUrl.toString();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", myUrl);
            messageTextBody.put("name", selectedImageUri.getLastPathSegment());
            messageTextBody.put("type", "image");
            messageTextBody.put("from", currentUserId);
            messageTextBody.put("to", receiverUserId);
            messageTextBody.put("messageID", messagePushId);
            messageTextBody.put("time_stamp", System.currentTimeMillis());

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(msgSenderRef + "/" + messagePushId, messageTextBody);
            messageBodyDetails.put(msgReceiverRef + "/" + messagePushId, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                } else {
                    Toast.makeText(this, "Image not sent", Toast.LENGTH_SHORT).show();
                }
                loader.setVisibility(View.VISIBLE);
            });
        });
    }

    //hide unnecessary words with special character 13/04/2021
    public static String elasticSearch(String inputWord, List<String> searchWordList) {
        String outPut = inputWord; // to handle no match condition
        String star = "";
        for (String searchWord : searchWordList) {
            if (inputWord.contains(searchWord)) {
                System.out.println("word found");
                String asterisk_val = "";
                for (int i = 0; i < searchWord.length(); i++) {
                    asterisk_val += '*';
                    star = asterisk_val;
                }
            }
            outPut = inputWord.replaceAll(searchWord, star);
            inputWord = outPut;
        }
        return outPut;
    }

    public void sendMessage(String type, String giftId, String giftAmount) {
        notify = true;
        String msg = "";
        if (type.equals("text")) {
            msg = messageEdittext.getText().toString().toLowerCase();
            //hide unnecessary words with special character 13/04/2021
            inputSentence = msg;
            String regex = "[-+^#<!@>$%&({*}?/.=~),'_:]*";
            inputSentence = inputSentence.replaceAll(regex, "")
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "");
            String outPut = elasticSearch(inputSentence, searchWordList);
            msg = outPut;
            Log.e("actual_words:", outPut);
            Log.e(Tag, "wordList:" + searchWordList);

        } else if (type.equals("gift")) {
            msg = giftId;

        } else if (type.equals("gift_request")) {
            int request_status = 0;
            String tagLine = "Please give me this gift :" + giftId + ":" + request_status + ":" + giftAmount;
            msg = tagLine;

        } else if (type.equals("call_request")) {
            int request_status = 0;
            String tagLine = "Call me :" + request_status;
            msg = tagLine;
        }

        if (!msg.isEmpty()) {
            String msgSenderRef = "Messages/" + currentUserId + "/" + receiverUserId;
            String msgReceiverRef = "Messages/" + receiverUserId + "/" + currentUserId;

            DatabaseReference dbReference = rootRef.child("Messages").child(currentUserId).child(receiverUserId).push();
            String messagePushId = dbReference.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("type", type);
            messageTextBody.put("message", msg);
            messageTextBody.put("from", currentUserId);
            messageTextBody.put("time_stamp", System.currentTimeMillis());
            messageTextBody.put("is_seen", false);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(msgSenderRef + "/" + messagePushId, messageTextBody);
            messageBodyDetails.put(msgReceiverRef + "/" + messagePushId, messageTextBody);

            messageEdittext.setText("");

            // If User is not in conversation then increase counter of unread message
            if (!isReceiverOnline) {
                ++receiverUnreadMessageCount;
            }

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(task -> {
                // Is message sent successfully
                if (task.isSuccessful()) {
                    //Update record in contact list
                    addOrUpdateValueInContactList();
                    isFirstMessage = false;

                } else {
                    Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Send Message in Notification
        if (notify) {

            // Send notification if User not on chat conversation screen
            if (!isReceiverOnline) {
                if (currentReceiverToken == null) {
                    getUserTokenFromDatabase(receiverUserId, currentUserName, msg, type);
                } else {
                    sendMessageInNotification(msg, currentUserName, type, currentReceiverToken);
                }
            }
        }
        notify = false;
    }

    void getUserTokenFromDatabase(String receiverUserId, String name, String message, String type) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiverUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Token token = snap.getValue(Token.class);

                    currentReceiverToken = token.getToken();
                    sendMessageInNotification(message, name, type, currentReceiverToken);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InboxDetails.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void sendMessageInNotification(String message, String senderName, String msgType, String token) {
        NotificationData data = new NotificationData(currentUserId, message, senderName,
                receiverUserId, R.mipmap.ic_launcher, msgType, System.currentTimeMillis(), receiverUnreadMessageCount);

        Sender sender = new Sender(data, token);
        Call<MyResponse> call = firebaseApiService.sendNotification(sender,3);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(InboxDetails.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(InboxDetails.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*void seenMessage() {

       seenMessage = FirebaseDatabase.getInstance().getReference("Messages").child(receiverUserId).child(currentUserId);

        Query fetchLastMessage = seenMessage.orderByKey().limitToLast(1);
        fetchLastMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ChatMessage message = snap.getValue(ChatMessage.class);

                    String from = message.getFrom();
                    if (from != null && message.getFrom().equals(receiverUserId)) {
                        Map messageTextBody = new HashMap();
                        messageTextBody.put("is_seen", true);
                        //  snap.getRef().updateChildren(messageTextBody);

                        snap.getRef().updateChildren(messageTextBody);
                    }

                    storeLastMessage(message.getMessage(), message.getType(), message.getTime_stamp());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    } */

    // Store Last Message to update it on recent contact list
    void storeLastMessage(String msg, String type, long time_stamp) {
        lastMessage = msg;
        lastMessageType = type;
        lastMessageTime = time_stamp;
    }

    /*void manageChatRequest() {
        loader.setVisibility(View.VISIBLE);
        chatRequestRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(receiverUserId)) {
                    String request_type = snapshot.child(receiverUserId).child("request_type").getValue().toString();
                    if (request_type.equals("sent") && purchasePlanStatus == 1) {
                        binding.addFriendContainer.setVisibility(View.GONE);
                        binding.rlBottom.setVisibility(View.VISIBLE);

                        // Reset Unread Message count and update info in contact list
                        addOrUpdateValueInContactList();
                    }
                } else {

                    // If user not exist in request table send request
                    sendChatRequest();
                }
                dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dismissDialog();
            }
        });
    }*/


  /*  void sendChatRequest() {
        loader.setVisibility(View.VISIBLE);
        chatRequestRef.child(currentUserId).child(receiverUserId).child("request_type").setValue("sent")
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        chatRequestRef.child(receiverUserId).child(currentUserId).child("request_type").setValue("sent")
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        // Show Chat box after friend request sent
                                        binding.addFriendContainer.setVisibility(View.GONE);
                                        binding.rlBottom.setVisibility(View.VISIBLE);

                                        addOrUpdateValueInContactList();

                                    } else {
                                        dismissDialog();
                                        Toast.makeText(this, "Something went wrong. Friend not Added !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        dismissDialog();
                        Toast.makeText(this, "Something went wrong. Friend not Added !", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    void seenMessage() {
        loader.setVisibility(View.VISIBLE);
        contactRef.child(currentUserId).child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ContactListCustomResponse value = snapshot.getValue(ContactListCustomResponse.class);

                //Set Unread message 0
                //If not a new user
                if (value != null) {
                    contactRef.child(currentUserId).child(receiverUserId).child("unread_message").setValue(0);
                }

                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    // Add value in contact list to sort data their
    void addOrUpdateValueInContactList() {
        ContactListCustomResponse recieverProfile = new ContactListCustomResponse();
        recieverProfile.setUid(receiverUserId);
        recieverProfile.setName(receiverName);
        recieverProfile.setImage(receiverImage);
        recieverProfile.setStatus("Offline");
        recieverProfile.setTimestamp(System.currentTimeMillis());
        //recieverProfile.setUnread_message(receiverUnreadMessages);
        contactRef.child(currentUserId).child(receiverUserId).setValue(recieverProfile);

        ContactListCustomResponse userProfile = new ContactListCustomResponse();
        userProfile.setUid(currentUserId);
        userProfile.setName(currentUserName);
        userProfile.setImage("Demo Image");
        userProfile.setTimestamp(System.currentTimeMillis());
        userProfile.setStatus("Online");
        userProfile.setUnread_message(receiverUnreadMessageCount);
        contactRef.child(receiverUserId).child(currentUserId).setValue(userProfile);

        dismissDialog();
    }

    // Save Current Receiver to prevent notification while live chat
    void saveCurrentReceiver(String id) {
        sessionManager.saveCurrentReceiver(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveCurrentReceiver(receiverUserId);

        // Check plan purchase status
       /* if (purchasePlanStatus == -1 || purchasePlanStatus == 0) {
            apiManager.isChatServicePurchased();
        }*/

        onlineStatusRef.child(currentUserId).child("is_online").setValue(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentReceiver("none");
        //seenMessage.removeEventListener(seenListener);

        onlineStatusRef.child(currentUserId).child("is_online").setValue(false);
    }

    /*private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(currentUserId).setValue(token1);
    }*/

    void dismissDialog() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void isError(String errorCode) {
        if (errorCode.equals("227")) {
            new InsufficientCoins(this, 6, 0);
        } else {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.IS_CHAT_SERVICE_ACTIVE) {
            ChatPurchaseValidity rsp = (ChatPurchaseValidity) response;

            purchasePlanStatus = rsp.getResult().getChat();
            Log.e("purchageplaneStatus", String.valueOf(purchasePlanStatus));

            // If user is female no need to chat plan recharge
            if (sessionManager.getGender().equals("female")) {
                purchasePlanStatus = 1;
            }

            if (purchasePlanStatus == 0 || purchasePlanStatus == -1) {
                // Open Insufficient Coin Dialog
                new InsufficientCoins(this, 6, 0);
                dismissDialog();
                binding.rlBottom.setVisibility(View.GONE);

            } else if (purchasePlanStatus == 1) {
                binding.rlBottom.setVisibility(View.VISIBLE);

                //Check receiver is already added in friend list ?
                //manageChatRequest();
            }
        }

        // -----------------------------------
        if (ServiceCode == Constant.GET_REMAINING_GIFT_CARD) {
            RemainingGiftCardResponce rsp = (RemainingGiftCardResponce) response;

            try {
                success = rsp.getSuccess();
                remGiftCard = rsp.getResult().getRemGiftCards();
                freeSeconds = rsp.getResult().getFreeSeconds();
                try {
                    if (remGiftCard > 0) {
                        //apiManager.searchUser(String.valueOf(receiverUserId), "1");
                        generateTokenMakeCall();
                        return;
                    }
                } catch (Exception e) {
                }
                if (new SessionManager(getApplicationContext()).getUserWallet() > Integer.parseInt(callRate)) {
                    // apiManager.searchUser(String.valueOf(receiverUserId), "1");
                    generateTokenMakeCall();
                } else {
                    new InsufficientCoins(InboxDetails.this, 2, Integer.parseInt(callRate));
                }
            } catch (Exception e) {
                //apiManager.searchUser(String.valueOf(receiverUserId), "1");
                generateTokenMakeCall();
            }
        }

        if (ServiceCode == Constant.GET_MESSAGE_CALL_DETAILS) {
            MessageCallDataResponce rsp = (MessageCallDataResponce) response;
            callRate = String.valueOf(rsp.getResult().getCallRate());
            userid = String.valueOf(rsp.getResult().getId());

            if (rsp.getResult().getChat() == 0) {
                // false
                canChat = false;
                return;
            }
            serverDate = rsp.getResult().getExpiry();
            chatStatus = rsp.getResult().getChat();
        }
        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
            currentCoin = rsp.getResult();

        } else if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;

            if (rsp.getResult() != null) {
                currentCoin = rsp.getResult().getTotal_point();
            }
        }

        //new code  for response 12/03/2021
        if (ServiceCode == Constant.SEARCH_USER) {
            UserListResponse rsp = (UserListResponse) response;

            if (rsp != null) {
                try {
                    onlineStatus = rsp.getResult().getData().get(0).getIs_online();
                    busyStatus = rsp.getResult().getData().get(0).getIs_busy();

                    if (onlineStatus == 1) {
                        setReceiverStatus("online", getResources().getColor(R.color.white), R.drawable.ic_online);

                        if (busyStatus == 1) {
                            setReceiverStatus("busy", getResources().getColor(R.color.colorBusy), R.drawable.ic_busy);
                        }

                    } else if (onlineStatus == 0) {
                        setReceiverStatus("offline", getResources().getColor(R.color.white), R.drawable.ic_offline);
                    }


                   /* if (onlineStatus == 1 && busyStatus == 0) {

                        if (remGiftCard > 0) {
                            apiManager.generateCallRequest(Integer.parseInt(userid), String.valueOf(System.currentTimeMillis()), "convId", Integer.parseInt(callRate),
                                    Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
                        } else {
                            apiManager.generateCallRequest(Integer.parseInt(userid), String.valueOf(System.currentTimeMillis()), "convId", Integer.parseInt(callRate),
                                    Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
                        }

                    }*/
                } catch (Exception e) {
                    onlineStatus = 0;
                    setReceiverStatus("offline", getResources().getColor(R.color.white), R.drawable.ic_offline);
                    //Toast.makeText(this, "User is Offline!", Toast.LENGTH_SHORT).show();
                    new SessionManager(getApplicationContext()).setOnlineState(0);
                    //finish();
                }
            }
        }

        //new code for New_Generate agora token 12/03/2021 here
        if (ServiceCode == Constant.NEW_GENERATE_AGORA_TOKEN) {
            ResultCall rsp = (ResultCall) response;
            //Log.e("newWalletValue", rsp.getResult().getPoints().getTotalPoint() + "");
            int walletBalance = rsp.getResult().getPoints().getTotalPoint();
            int talkTime = walletBalance / Integer.parseInt(callRate) * 1000 * 60;
            //int talkTime2 = userData.getCall_rate() * 1000 * 60;
            // Minus 2 sec to prevent balance goes into minus
            int canCallTill = talkTime - 2000;
            //here send userid and callRate
            //te by Kalpesh sir 12/03/2021
            Intent intent = new Intent(InboxDetails.this, VideoChatActivity.class);
            intent.putExtra("TOKEN", rsp.getResult().getData().getToken());
            intent.putExtra("ID", receiverUserId);
            intent.putExtra("UID", userid);
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
            intent.putExtra("receiver_name", receiverName);
            intent.putExtra("converID", "convId");
            intent.putExtra("receiver_image", receiverImage);
            startActivity(intent);

        }
    }

    void setReceiverStatus(String status, int color, int icon) {
        binding.tvUserstatus.setText(status);
        binding.tvUserstatus.setTextColor(color);
        binding.tvUserstatus.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    }

    void generateTokenMakeCall() {
        if (onlineStatus == 1 && busyStatus == 0) {
            //Check wallet balance before going to make a video call
            //apiManager.getWalletAmount();
            if (remGiftCard > 0) {
                apiManager.generateCallRequest(Integer.parseInt(userid), String.valueOf(System.currentTimeMillis()), "convId", Integer.parseInt(callRate),
                        Boolean.parseBoolean("true"), String.valueOf(remGiftCard));
            } else {
                apiManager.generateCallRequest(Integer.parseInt(userid), String.valueOf(System.currentTimeMillis()), "convId", Integer.parseInt(callRate),
                        Boolean.parseBoolean("false"), String.valueOf(remGiftCard));
            }

        } else {
            if (onlineStatus == 1) {
                if (busyStatus == 1) {
                    Toast.makeText(this, "User is busy", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User is offline", Toast.LENGTH_SHORT).show();
            }
        }
    }
}