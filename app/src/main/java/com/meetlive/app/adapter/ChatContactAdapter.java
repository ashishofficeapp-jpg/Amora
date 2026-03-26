package com.meetlive.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.response.FirebaseChat.ContactListCustomResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatContactAdapter extends RecyclerView.Adapter<ChatContactAdapter.myViewHolder> {

    Context context;
    List<ContactListCustomResponse> list;
    String currentUserId;
    public final int[] mColors = {R.drawable.background_inbox_fragment,R.drawable.background_inbox_fragment1};

    public ChatContactAdapter(Context context, List<ContactListCustomResponse> list, String currentUserId) {
        this.context = context;
        this.list = list;
        this.currentUserId = currentUserId;
    }

    @Override
    public ChatContactAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_contact_list, parent, false);
        return new ChatContactAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatContactAdapter.myViewHolder holder, int position) {
        holder.container.setBackground(ContextCompat.getDrawable(context, mColors[position %2]));
       // holder.container.setBackgroundColor((mColors[position % 2])); // 2 can be replaced by mColors.length

        try {
            Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.default_profile)
                    .apply(RequestOptions.circleCropTransform()).error(R.drawable.default_profile)).into(holder.userImg);


        holder.last_message.setCompoundDrawablePadding(10);
        holder.last_message.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        if (list.get(position).getMessage().getType().equals("text")) {
            holder.last_message.setText(list.get(position).getMessage().getMessage());

        } else if (list.get(position).getMessage().getType().equals("image")) {
            holder.last_message.setText("Photo");

        } else if (list.get(position).getMessage().getType().equals("gift")) {
            holder.last_message.setCompoundDrawablesWithIntrinsicBounds(
                    context.getResources().getDrawable(R.drawable.ic_small_gift),
                    null, null, null);
            holder.last_message.setText("Gift");

        } else if (list.get(position).getMessage().getType().equals("gift_request")) {
            holder.last_message.setCompoundDrawablesWithIntrinsicBounds(
                    context.getResources().getDrawable(R.drawable.ic_small_gift),
                    null, null, null);

            holder.last_message.setText("Gift Request");
        }


        holder.name.setText(list.get(position).getName());
        holder.date.setText(getDate(list.get(position).getMessage().getTime_stamp()));

        // Check If last message comes from receiver(is Seen)
        if (!list.get(position).getMessage().getFrom().equals(currentUserId)) {

            // if (list.get(position).getMessage().isIs_seen()) {
            if (list.get(position).getUnread_message() > 0) {
                holder.seen_count.setVisibility(View.VISIBLE);
                holder.seen_count.setText(String.valueOf(list.get(position).getUnread_message()));

                if (list.get(position).getUnread_message() > 99) {
                    holder.seen_count.setText("99+");
                }

            } else {
                holder.seen_count.setVisibility(View.GONE);
            }
        } else {
            // Check If last message is sent by me
            holder.seen_count.setVisibility(View.GONE);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        //   SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy 'at' HH:mm'am'", Locale.ENGLISH);
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM hh:mm aa", Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView last_message, name, date, seen_count;
        ImageView userImg;

        public myViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            last_message = itemView.findViewById(R.id.last_message);
            name = itemView.findViewById(R.id.name);
            userImg = itemView.findViewById(R.id.user_image);
            date = itemView.findViewById(R.id.date);
            seen_count = itemView.findViewById(R.id.seen_count);
        }
    }
}