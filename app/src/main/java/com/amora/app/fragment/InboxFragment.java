package com.amora.app.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.amora.app.R;
import com.amora.app.activity.InboxDetails;
import com.amora.app.activity.MainActivity;
import com.amora.app.adapter.ChatContactAdapter;
import com.amora.app.push_notification.MyFirebaseMessagingService;
import com.amora.app.response.ChatMessage;
import com.amora.app.response.FirebaseChat.ContactListCustomResponse;
import com.amora.app.utils.NotificationCallBack;
import com.amora.app.utils.PaginationScrollListenerLinear;
import com.amora.app.utils.RecyclerTouchListener;
import com.amora.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static android.app.Activity.RESULT_OK;
import static com.amora.app.utils.SessionManager.PROFILE_ID;

public class InboxFragment extends Fragment implements NotificationCallBack {

    RelativeLayout placeholder;
    RecyclerView contactRecyclerview;
    LinearLayoutManager linearLayoutManager;
    ImageView loader;

    DatabaseReference contactRef;
    SessionManager sessionManager;
    String currentUserId;
    String theLastMessage;

    List<ContactListCustomResponse> userProfile = new ArrayList<>();
    ChatContactAdapter chatContactAdapter;
    int index = 0;
    long lastNodeId = -1;
    int itemSlot = 20;

    private static final int PAGE_START = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int TOTAL_PAGES = 25;
    //  private int currentPage = PAGE_START;

    MainActivity activity;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyFirebaseMessagingService.setNotificationCallBack(this);

        loader = getActivity().findViewById(R.id.img_loader);
        Glide.with(getContext()).load(R.drawable.loader).into(loader);

        placeholder = getActivity().findViewById(R.id.recent_chat_placeholder);
        contactRecyclerview = getActivity().findViewById(R.id.contact_list);
        contactRecyclerview.setItemAnimator(null);


        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        currentUserId = user.get(PROFILE_ID);

        contactRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            init();
            initLoadData();
        }
    }

    void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        contactRecyclerview.setLayoutManager(linearLayoutManager);

        contactRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getContext(), contactRecyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getContext(), InboxDetails.class);
                intent.putExtra("receiver_id", userProfile.get(position).getUid());
                intent.putExtra("receiver_name", userProfile.get(position).getName());
                intent.putExtra("receiver_image", userProfile.get(position).getImage());
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        contactRecyclerview.addOnScrollListener(new PaginationScrollListenerLinear(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                // currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(() -> {

                    if (lastNodeId != -1) {
                        loader.setVisibility(View.VISIBLE);
                        getMoreItemsFirebase();
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
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (sessionManager.getRecentChatListUpdateStatus()) {

                if (chatContactAdapter != null) {

                    // Reset Data
                    isLoading = false;
                    isLastPage = false;
                    userProfile.clear();
                    lastNodeId = -1;
                    index = 0;
                    initLoadData();

                    sessionManager.isRecentChatListUpdateNeeded(false);
                }
            }
        }
    }

    void getMoreItemsFirebase() {
        Query fetchContactList = contactRef.orderByChild("timestamp").endAt(lastNodeId).limitToLast(itemSlot);
        fetchContactList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isLoading = false;

                // Getting user contact list
                if (snapshot.hasChildren()) {
                    loader.setVisibility(View.VISIBLE);

                    long lastNode = -1;

                    List<ContactListCustomResponse> userList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        try {
                            ContactListCustomResponse user = postSnapshot.getValue(ContactListCustomResponse.class);
                            userList.add(user);

                            if (lastNode == -1) {
                                lastNode = user.getTimestamp();
                                lastNodeId = user.getTimestamp();

                                // Old data have no timestamps so changing lastNodeId id -1 to prevent infinite loop
                                if (lastNode == 0) {
                                    lastNodeId = -1;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (userList.size() < itemSlot) {
                        // doing last record status -1 to prevent unnecesry loop in recycler OnScrollListener
                        lastNodeId = -1;
                        isLastPage = true;
                    }

                    Collections.reverse(userList);

                    // If list have more than 1 record
                    if (userList.size() > 1) {
                        if (lastNode != -1) {
                            userList.remove(userList.size() - 1);
                        }
                    }

                    for (int i = 0; i < userList.size(); i++) {
                        // Sending Request for getting last message
                        lastMessage(userList.get(i));
                    }

                } else {
                    isLastPage = true;
                    loader.setVisibility(View.GONE);

                    if (lastNodeId == -1) {
                        contactRecyclerview.setVisibility(View.GONE);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isLoading = false;
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initLoadData() {
        Query fetchContactList = contactRef.orderByChild("timestamp").limitToLast(itemSlot);
        fetchContactList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isLoading = false;

                // Getting user contact list
                if (snapshot.hasChildren()) {
                    loader.setVisibility(View.VISIBLE);

                    long lastNode = -1;

                    List<ContactListCustomResponse> userList = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        try {
                            ContactListCustomResponse user = postSnapshot.getValue(ContactListCustomResponse.class);
                            userList.add(user);

                            if (lastNode == -1) {
                                lastNode = user.getTimestamp();
                                lastNodeId = user.getTimestamp();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Collections.reverse(userList);

                    isUnreadMessage(userList);

                    // If list have more than 1 record
                    if (userList.size() >= itemSlot) {
                        userList.remove(userList.size() - 1);
                    } else {
                        // doing last record status -1 to prevent unnecessary loop
                        lastNodeId = -1;
                        isLastPage = true;
                    }

                    for (int i = 0; i < userList.size(); i++) {
                        // Sending Request for getting last message
                        lastMessage(userList.get(i));
                    }

                } else {
                    isLastPage = true;
                    loader.setVisibility(View.GONE);

                    if (lastNodeId == -1) {
                        contactRecyclerview.setVisibility(View.GONE);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isLoading = false;
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {

            if (data != null) {
                ContactListCustomResponse rsp = (ContactListCustomResponse) data.getSerializableExtra("last_message");
                boolean is_first_message = data.getBooleanExtra("is_first_message", true);

                if (userProfile.stream().anyMatch(emp -> emp.getUid().matches(rsp.getUid()))) {

                    int index = IntStream.range(0, userProfile.size())
                            .filter(userInd -> userProfile.get(userInd).getUid().equals(rsp.getUid()))
                            .findFirst().getAsInt();


                    if (!is_first_message) {
                        // Change index if sent or receive any new message
                        userProfile.remove(index);
                        userProfile.add(0, rsp);
                        chatContactAdapter.notifyItemMoved(index, 0);
                        chatContactAdapter.notifyItemChanged(0);

                    } else {
                        // Update index only if not sent or receive any new message
                        userProfile.set(index, rsp);
                        chatContactAdapter.notifyItemChanged(index);
                    }

                    // Update Unread Count on Bottom Navigation
                    isUnreadMessage(userProfile);
                }
            }
        }
    }

    private void lastMessage(ContactListCustomResponse rsp) {
        theLastMessage = "No Message Found";
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().
                child("Messages").child(String.valueOf(rsp.getUid())).child(currentUserId);
        Query lastMessage = messagesRef.orderByKey().limitToLast(1);

        lastMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                loader.setVisibility(View.GONE);

                if (snapshot.hasChildren()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        ChatMessage message = snap.getValue(ChatMessage.class);

                        rsp.setMessage(message);

                        if (userProfile.contains(rsp)) {
                            int index = userProfile.indexOf(rsp);

                            // update data on particular index if new message comes
                            userProfile.set(index, rsp);
                            chatContactAdapter.notifyItemChanged(index);

                        } else {
                            userProfile.add(rsp);

                            if (index == 0) {
                                chatContactAdapter = new ChatContactAdapter(getContext(), userProfile, currentUserId);
                                contactRecyclerview.setAdapter(chatContactAdapter);
                                index++;

                            } else {
                                index++;
                                chatContactAdapter.notifyItemInserted(index);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //  last_message.setText(theLastMessage);
                //  seen_count.setVisibility(View.GONE);
            }
        });
    }

    void isUnreadMessage(List<ContactListCustomResponse> list) {
        // Check If last message comes from receiver(is Seen)

        int unreadMessageCount = 0;

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                //   if (!list.get(i).getMessage().getFrom().equals(currentUserId)) {

                if (list.get(i).getUnread_message() > 0) {
                    unreadMessageCount += list.get(i).getUnread_message();

                    if (list.get(i).getUnread_message() > 99) {
                        unreadMessageCount = 99;
                        break;
                    }

                }
                //  }
            }
        }

        activity = (MainActivity) getContext();
        activity.updateMessageCount(unreadMessageCount);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGetMessage(RemoteMessage notificationData) {

        try {
            // Get Message from notification
            String userId = notificationData.getData().get("user");
            String icon = notificationData.getData().get("icon");
            String title = notificationData.getData().get("title");
            String type = notificationData.getData().get("type");
            String body = notificationData.getData().get("body");
            int unread_message_count = Integer.parseInt(notificationData.getData().get("unread_count"));
            long sentTime = Long.parseLong(notificationData.getData().get("sent_time"));

            ContactListCustomResponse customResponse = new ContactListCustomResponse();
            customResponse.setUid(userId);
            customResponse.setImage(icon);
            customResponse.setName(title);
            customResponse.setUnread_message(unread_message_count);
            customResponse.setStatus("Demo");

            customResponse.setMessage(new ChatMessage(userId, body, type, sentTime, false));

            // (String from, String message, String type, long time_stamp, boolean is_seen)


            //  if (userProfile.contains(customResponse)) {
       /* if (Stream.of().anyMatch(userProfile -> customResponse.getUid().matches(user))) {

            int index = Stream.range(0, userProfile.size())
                    .filter(userInd -> userProfile.get(userInd).getUid().equals(user))
                    .findFirst().get();*/

            //     }


            if (userProfile.stream().anyMatch(emp -> emp.getUid().matches(userId))) {

                int index = IntStream.range(0, userProfile.size())
                        .filter(userInd -> userProfile.get(userInd).getUid().equals(userId))
                        .findFirst().getAsInt();


                userProfile.remove(index);
                userProfile.add(0, customResponse);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        chatContactAdapter.notifyItemMoved(index, 0);
                        chatContactAdapter.notifyItemChanged(0);

                        // Update Unread Count on Bottom Navigation
                        isUnreadMessage(userProfile);
                    }
                });

            } else {
                userProfile.add(0, customResponse);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (chatContactAdapter == null) {
                            chatContactAdapter = new ChatContactAdapter(getContext(), userProfile, currentUserId);
                            contactRecyclerview.setAdapter(chatContactAdapter);
                            index++;
                        }
                        chatContactAdapter.notifyItemInserted(0);

                        // Update Unread Count on Bottom Navigation
                        isUnreadMessage(userProfile);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}