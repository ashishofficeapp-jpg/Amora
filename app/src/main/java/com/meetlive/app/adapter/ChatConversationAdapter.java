package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meetlive.app.R;
import com.meetlive.app.response.ChatMessage;
import com.meetlive.app.utils.SessionManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatConversationAdapter extends RecyclerView.Adapter<ChatConversationAdapter.myViewHolder> {

    Context context;
    List<ChatMessage> list;
    String senderId, receiverId, gender;
    DatabaseReference chatMessageRef;

    public ChatConversationAdapter(Context context, List<ChatMessage> list, String senderId, String receiverId) {
        this.context = context;
        this.list = list;
        this.senderId = senderId;
        this.receiverId = receiverId;

        gender = new SessionManager(context).getGender();

        chatMessageRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(senderId).child(receiverId);
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_view, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        ChatMessage message = list.get(position);
        String fromUserId = message.getFrom();
        String fromMessageType = message.getType();

        if (fromMessageType.equals("text")) {
            holder.sendGiftContainer.setVisibility(View.GONE);
            holder.receiveGiftContainer.setVisibility(View.GONE);

            holder.sendImageContainer.setVisibility(View.GONE);
            holder.receiveImageContainer.setVisibility(View.GONE);
            holder.giftRequestContainer.setVisibility(View.GONE);
            holder.callRequestContainer.setVisibility(View.GONE);

            // Sender Text Message
            if (fromUserId.equals(senderId)) {
                holder.senderContainer.setVisibility(View.VISIBLE);
                holder.receiveContainer.setVisibility(View.GONE);
                holder.sender_msg.setText(message.getMessage());
                holder.sent_time.setText(getTime(message.getTime_stamp()));

                //  Delivery status check marks
               /* if (list.get(list.size() - 1).isIs_seen()) {
                    holder.deliveryStatus.setImageResource(R.drawable.ic_check_readed);
                } else if (message.isIs_seen()) {
                    holder.deliveryStatus.setImageResource(R.drawable.ic_check_readed);
                } else {
                    holder.deliveryStatus.setImageResource(R.drawable.ic_check_delivered);
                }*/

                // Receiver Text Message
            } else {
                holder.receiveContainer.setVisibility(View.VISIBLE);
                holder.senderContainer.setVisibility(View.GONE);
                holder.receiver_msg.setText(message.getMessage());
                holder.receive_time.setText(getTime(message.getTime_stamp()));
            }

            // Sender Image Message
        } else if (fromMessageType.equals("image")) {
            holder.callRequestContainer.setVisibility(View.GONE);
            holder.giftRequestContainer.setVisibility(View.GONE);
            holder.receiveContainer.setVisibility(View.GONE);
            holder.senderContainer.setVisibility(View.GONE);

            holder.sendGiftContainer.setVisibility(View.GONE);
            holder.receiveGiftContainer.setVisibility(View.GONE);

            if (fromUserId.equals(senderId)) {
                holder.sendImageContainer.setVisibility(View.VISIBLE);
                holder.receiveImageContainer.setVisibility(View.GONE);
                holder.sent_time_image.setText(getTime(message.getTime_stamp()));
                Glide.with(context.getApplicationContext()).load(message.getMessage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person)
                                .error(R.drawable.ic_person)).into(holder.senderImageMsg);

            } else {
                // Receiver Image Message
                holder.receiveImageContainer.setVisibility(View.VISIBLE);
                holder.sendImageContainer.setVisibility(View.GONE);
                holder.receive_time_image.setText(getTime(message.getTime_stamp()));
                Glide.with(context.getApplicationContext()).load(message.getMessage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_person)
                                .error(R.drawable.ic_person)).into(holder.receiverImageMsg);
            }

        } else if (fromMessageType.equals("gift")) {
            holder.callRequestContainer.setVisibility(View.GONE);
            holder.giftRequestContainer.setVisibility(View.GONE);
            holder.sendImageContainer.setVisibility(View.GONE);
            holder.receiveImageContainer.setVisibility(View.GONE);

            holder.receiveContainer.setVisibility(View.GONE);
            holder.senderContainer.setVisibility(View.GONE);

            // Sender Text Message
            if (fromUserId.equals(senderId)) {
                holder.sendGiftContainer.setVisibility(View.VISIBLE);
                holder.receiveGiftContainer.setVisibility(View.GONE);
                holder.sent_gift_time.setText(getTime(message.getTime_stamp()));
                holder.sender_gift.setImageResource(getGiftImage(message.getMessage()));

                // Receiver Text Message
            } else {
                holder.receiveGiftContainer.setVisibility(View.VISIBLE);
                holder.sendGiftContainer.setVisibility(View.GONE);
                holder.receive_gift_time.setText(getTime(message.getTime_stamp()));
                holder.receiver_gift.setImageResource(getGiftImage(message.getMessage()));
            }

        } else if (fromMessageType.equals("gift_request")) {
            holder.callRequestContainer.setVisibility(View.GONE);
            holder.sendImageContainer.setVisibility(View.GONE);
            holder.receiveImageContainer.setVisibility(View.GONE);

            holder.receiveContainer.setVisibility(View.GONE);
            holder.senderContainer.setVisibility(View.GONE);
//new code hide 9/4/21
            holder.sendGiftContainer.setVisibility(View.GONE);
            holder.receiveGiftContainer.setVisibility(View.GONE);
            holder.giftRequestContainer.setVisibility(View.GONE);

/*
            // If gift request received to male
            if (gender.equals("male")) {
                holder.giftRequestContainer.setVisibility(View.VISIBLE);

                String[] leaveType = message.getMessage().split(":");
                String giftId = leaveType[1];
                //   String giftAmount = leaveType[2];

                //  holder.request_message.setText("");
                holder.requested_gift_img.setImageResource(getGiftImage(giftId));
                holder.gift_request_time.setText(getTime(message.getTime_stamp()));

                String giftRequestStatus = "";
                if (leaveType.length > 2) {
                    giftRequestStatus = leaveType[2];
                }

                if (giftRequestStatus.equals("0")) {
                    holder.sent_gift_btn.setText("Send Gift");
                    holder.sent_gift_btn.setEnabled(true);

                } else if (giftRequestStatus.equals("1")) {
                    holder.sent_gift_btn.setText("Gift Sent Successfully");
                    holder.sent_gift_btn.setEnabled(false);
                }

            } else {
                // If gift request sent by female user
                holder.giftRequestContainer.setVisibility(View.GONE);
                holder.senderContainer.setVisibility(View.VISIBLE);
                holder.sender_msg.setText("Gift Request Sent");
                holder.sent_time.setText(getTime(message.getTime_stamp()));

            }*/
        } else if (fromMessageType.equals("call_request")) {
            holder.sendImageContainer.setVisibility(View.GONE);
            holder.receiveImageContainer.setVisibility(View.GONE);

            holder.receiveContainer.setVisibility(View.GONE);
            holder.senderContainer.setVisibility(View.GONE);

            holder.sendGiftContainer.setVisibility(View.GONE);
            holder.receiveGiftContainer.setVisibility(View.GONE);
            holder.giftRequestContainer.setVisibility(View.GONE);

            // If gift request received to male
            if (gender.equals("male")) {
                holder.callRequestContainer.setVisibility(View.VISIBLE);

                String[] requestType = message.getMessage().split(":");
                String callRequestStatus = requestType[1];

                if (callRequestStatus.equals("0")) {
                    holder.callRequestBtn.setText("Make a Call");
                    holder.callRequestBtn.setEnabled(true);

                } else if (callRequestStatus.equals("1")) {
                    holder.callRequestBtn.setText("Call Ended");
                    holder.callRequestBtn.setEnabled(false);
                }

                holder.call_request_time.setText(getTime(message.getTime_stamp()));

            } else {
                // If gift request sent by female user
                holder.callRequestContainer.setVisibility(View.GONE);
                holder.senderContainer.setVisibility(View.VISIBLE);
                holder.sender_msg.setText("Call Request Sent");
                holder.sent_time.setText(getTime(message.getTime_stamp()));
            }
        }


        // Check Date Tag
        long previousTs = 0;
        if (position > 0) {
            int my_position = position - 1;
            ChatMessage pm = list.get(my_position);
            previousTs = pm.getTime_stamp();
        }
        setDateTextVisibility(message.getTime_stamp(), previousTs, holder.time_stamp);
    }


    /* setDateTextVisibility */
    private void setDateTextVisibility(long ts1, long ts2, TextView timeText) {
        if (ts2 == 0) {
            timeText.setVisibility(View.VISIBLE);
            timeText.setText(getDate(ts1));
        } else {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTimeInMillis(ts1);
            cal2.setTimeInMillis(ts2);

            boolean sameDate = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE);

            if (sameDate) {
                timeText.setVisibility(View.GONE);
                timeText.setText("");
            } else {
                timeText.setVisibility(View.VISIBLE);
                timeText.setText(getDate(ts1));
            }
        }
    }

    int getGiftImage(String id) {
        int imgResource = 0;
        if (id.equals("18")) {
            imgResource = R.drawable.heart;

        } else if (id.equals("21")) {
            imgResource = R.drawable.lips;

        } else if (id.equals("22")) {
            imgResource = R.drawable.bunny;

        } else if (id.equals("23")) {
            imgResource = R.drawable.rose;

        } else if (id.equals("24")) {
            imgResource = R.drawable.boygirl;

        } else if (id.equals("25")) {
            imgResource = R.drawable.sandle;

        } else if (id.equals("26")) {
            imgResource = R.drawable.frock;

        } else if (id.equals("27")) {
            imgResource = R.drawable.car;

        } else if (id.equals("28")) {
            imgResource = R.drawable.ship;

        } else if (id.equals("29")) {
            imgResource = R.drawable.tajmahal;

        }
        return imgResource;
    }


    private String getDate(long time) {
        return DateFormat.getDateInstance().format(new Date(time));
    }

    private String getTime(long time) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(time));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView sender_msg, receiver_msg, time_stamp, sent_time, receive_time,
                sent_time_image, receive_time_image, sent_gift_time, receive_gift_time,
                request_message, sent_gift_btn, gift_request_time, call_request_time,
                callRequestBtn;

        ImageView senderImageMsg, receiverImageMsg, receiver_gift, sender_gift, requested_gift_img;

        RelativeLayout senderContainer, receiveContainer, sendImageContainer,
                receiveImageContainer, sendGiftContainer, receiveGiftContainer,
                giftRequestContainer, callRequestContainer;

        public myViewHolder(View itemView) {
            super(itemView);

            time_stamp = itemView.findViewById(R.id.time_stamp);
            sent_gift_time = itemView.findViewById(R.id.sent_gift_time);
            receive_gift_time = itemView.findViewById(R.id.receive_gift_time);
            sender_msg = itemView.findViewById(R.id.sender_msg);
            receiver_msg = itemView.findViewById(R.id.receiver_msg);
            senderImageMsg = itemView.findViewById(R.id.sender_image_msg);
            receiverImageMsg = itemView.findViewById(R.id.receiver_image_msg);
            receiver_gift = itemView.findViewById(R.id.receiver_gift);
            sender_gift = itemView.findViewById(R.id.sender_gift);

            sent_time = itemView.findViewById(R.id.sent_time);
            receive_time = itemView.findViewById(R.id.receive_time);
            sent_time_image = itemView.findViewById(R.id.sent_time_image);
            receive_time_image = itemView.findViewById(R.id.receive_time_image);
            senderContainer = itemView.findViewById(R.id.sender_container);
            receiveContainer = itemView.findViewById(R.id.receiver_container);
            sendImageContainer = itemView.findViewById(R.id.sender_image_container);
            receiveImageContainer = itemView.findViewById(R.id.receiver_image_container);
            sendGiftContainer = itemView.findViewById(R.id.sender_gift_container);
            receiveGiftContainer = itemView.findViewById(R.id.receiver_gift_container);

            giftRequestContainer = itemView.findViewById(R.id.gift_request_container);
            request_message = itemView.findViewById(R.id.request_message);
            requested_gift_img = itemView.findViewById(R.id.requested_gift_img);
            sent_gift_btn = itemView.findViewById(R.id.sent_gift_btn);
            gift_request_time = itemView.findViewById(R.id.gift_request_time);

            callRequestContainer = itemView.findViewById(R.id.call_request_container);
            call_request_time = itemView.findViewById(R.id.call_request_time);
            callRequestBtn = itemView.findViewById(R.id.call_btn);


         /*   sendImageContainer.setOnClickListener(v -> {
                //  TransitionManager.beginDelayedTransition(full_image_view_container);

                new ChatPictureView(context, list.get(getAdapterPosition()).getMessage());
            });
            receiveImageContainer.setOnClickListener(v -> new ChatPictureView(context, list.get(getAdapterPosition()).getMessage()));
*/
        }
    }
}