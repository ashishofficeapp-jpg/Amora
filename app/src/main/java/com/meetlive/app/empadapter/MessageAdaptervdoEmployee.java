package com.meetlive.app.empadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.meetlive.app.R;
import com.meetlive.app.response.message.Message_;
import com.meetlive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class MessageAdaptervdoEmployee extends BaseAdapter {

    List<Message_> messages = new ArrayList<Message_>();
    Context context;
    String profilepic;
    private int SELF = 100;

    public MessageAdaptervdoEmployee(Context context, List<Message_> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void add(Message_ message, String profilepic) {
        this.messages.add(message);
        this.profilepic = profilepic;
        notifyDataSetChanged();
    }

    public void add(Message_ message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewvdoHolder holder = new MessageViewvdoHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message_ message = messages.get(i);

        if (message.getSender().equals(new SessionManager(context).getUserId())) {
            convertView = messageInflater.inflate(R.layout.msginvdo, null);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            holder.cv_image = (CardView) convertView.findViewById(R.id.cv_image);
            holder.img_image = (ImageView) convertView.findViewById(R.id.img_image);
            holder.img_profilepic = (ImageView) convertView.findViewById(R.id.img_profilepic);

            convertView.setTag(holder);
            holder.tv_msg.setTypeface(Typeface.createFromAsset(context.getAssets(),
                    "fonts/Poppins-Regular_0.ttf"));

            holder.tv_msg.setText(message.getBody());
            Picasso.get()
                    .load(new SessionManager(context).getUserProfilepic())
                    .fit()
                    .into(holder.img_profilepic);

            if (message.getMimeType().equals("image/giftrequest")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gift, 0, 0, 0);
                holder.tv_msg.setText("You demanded gift");

            } else {
                holder.tv_msg.setText(message.getBody());
            }

            if (message.getMimeType().equals("image/gift")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.cv_image.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(message.getBody())
                        .fit()
                        .into(holder.img_image);
            } else {
                holder.tv_msg.setText(message.getBody());
            }

  /*          if (message.getMimeType().equals("videocall")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.videosmall, 0, 0, 0);
            }
  */
        } else {
            convertView = messageInflater.inflate(R.layout.msgoutvdo, null);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            holder.cv_image = (CardView) convertView.findViewById(R.id.cv_image);
            holder.img_image = (ImageView) convertView.findViewById(R.id.img_image);
            holder.img_profilepic = (ImageView) convertView.findViewById(R.id.img_profilepic);

            convertView.setTag(holder);
            holder.tv_msg.setTypeface(Typeface.createFromAsset(context.getAssets(),
                    "fonts/Poppins-Regular_0.ttf"));

            Picasso.get()
                    .load(profilepic)
                    .fit()
                    .into(holder.img_profilepic);


            if (message.getMimeType().equals("image/gift")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.cv_image.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(message.getBody())
                        .fit()
                        .into(holder.img_image);
            } else {
                holder.tv_msg.setText(message.getBody());
            }

     /*       if (message.getMimeType().equals("videocall")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.videosmall, 0);
            }
*/
        }

        return convertView;
    }

}

class MessageViewvdoHolder {
    public CardView cv_image;
    public ImageView img_image, img_profilepic;
    public TextView tv_msg;
}