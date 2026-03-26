package com.meetlive.app.empadapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.meetlive.app.R;
import com.meetlive.app.response.message.Message_;
import com.meetlive.app.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MessageEmployeeAdapter extends BaseAdapter {

    List<Message_> messages = new ArrayList<Message_>();
    Context context;
    private int SELF = 100;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MessageEmployeeAdapter(Context context, List<Message_> messages) {
        this.context = context;
        this.messages = messages;

        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message_ message = messages.get(i);

        if (messages.get(i).getSender().equals(new SessionManager(context).getUserId())) {
            convertView = messageInflater.inflate(R.layout.msgin, null);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            holder.cv_image = (CardView) convertView.findViewById(R.id.cv_image);
            holder.img_image = (ImageView) convertView.findViewById(R.id.img_image);
            holder.tv_timeduration = (TextView) convertView.findViewById(R.id.tv_timeduration);
            holder.rl_audio = (RelativeLayout) convertView.findViewById(R.id.rl_audio);
            holder.img_visualizer = (ImageView) convertView.findViewById(R.id.img_visualizer);

            convertView.setTag(holder);
            holder.tv_msg.setText(message.getBody());
            if (message.getMimeType().equals("image/jpeg")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.cv_image.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(message.getBody())
                        .fit()
                        .into(holder.img_image);
            } else {
                holder.tv_msg.setText(message.getBody());
                //          holder.tv_date.setText(getDate(message.getTimestamp()));
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
//                holder.tv_date.setText(getDate(message.getTimestamp()));
            }

            if (message.getMimeType().equals("videocall")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.videosmall, 0, 0, 0);
            }
            if (message.getMimeType().equals("image/giftrequest")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gift, 0, 0, 0);
                holder.tv_msg.setText("You demanded gift");
            }

            if (messages.get(i).isIsplaying()) {
                Glide.with(context)
                        .load(R.drawable.voicevis)
                        .into(holder.img_visualizer);

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            message.setIsplaying(false);
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));
                        }
                    });
                }
            }

            if (message.getMimeType().equals("audio/mp3")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.rl_audio.setVisibility(View.VISIBLE);
                try {
                    holder.tv_timeduration.setText(message.getAudioDuration());
                } catch (Exception e) {
                }
                holder.rl_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            try {
                                holder.rl_audio.setEnabled(false);
                                mediaPlayer.setDataSource(message.getBody());
                                mediaPlayer.prepareAsync();

                            } catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
                                e.printStackTrace();
                            }

                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    holder.rl_audio.setEnabled(true);
                                    mediaPlayer.start();
                                    for (int ii = 0; ii < messages.size(); ii++) {
                                        messages.get(ii).setIsplaying(false);
                                        Log.e("TAGDaya", String.valueOf(messages.get(i).isIsplaying()) + messages.get(i).getBody());
                                    }
                                    message.setIsplaying(true);
                                    notifyDataSetChanged();

                                }
                            });

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    message.setIsplaying(false);
                                    holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));
                                }
                            });
                        } else {

                            try {
                                holder.rl_audio.setEnabled(false);
                                mediaPlayer.setDataSource(message.getBody());
                                mediaPlayer.prepareAsync();
                            } catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
                                e.printStackTrace();
                            }

                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    holder.rl_audio.setEnabled(true);
                                    mediaPlayer.start();
                                    message.setIsplaying(true);
                                    notifyDataSetChanged();
                                }
                            });

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    message.setIsplaying(false);
                                    holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));
                                }
                            });
                        }
                    }
                });

            }

//sdsd
        } else {
            convertView = messageInflater.inflate(R.layout.msgout, null);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            holder.cv_image = (CardView) convertView.findViewById(R.id.cv_image);
            holder.img_image = (ImageView) convertView.findViewById(R.id.img_image);
            holder.tv_timeduration = (TextView) convertView.findViewById(R.id.tv_timeduration);
            holder.rl_audio = (RelativeLayout) convertView.findViewById(R.id.rl_audio);
            holder.img_visualizer = (ImageView) convertView.findViewById(R.id.img_visualizer);

            convertView.setTag(holder);

            holder.tv_msg.setText(message.getBody());

            if (message.getMimeType().equals("image/jpeg")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.cv_image.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(message.getBody())
                        .fit()
                        .into(holder.img_image);
            } else {
                holder.tv_msg.setText(message.getBody());
                //          holder.tv_date.setText(getDate(message.getTimestamp()));
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
//                holder.tv_date.setText(getDate(message.getTimestamp()));
            }

            if (message.getMimeType().equals("videocall")) {
                holder.tv_msg.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.videosmall, 0);
            }

            if (messages.get(i).isIsplaying()) {
                Glide.with(context)
                        .load(R.drawable.voicevis)
                        .into(holder.img_visualizer);

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            message.setIsplaying(false);
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));
                        }
                    });
                }
            }


            if (message.getMimeType().equals("audio/mp3")) {
                holder.tv_msg.setVisibility(View.GONE);
                holder.rl_audio.setVisibility(View.VISIBLE);

                try {
                    holder.tv_timeduration.setText(message.getAudioDuration());
                } catch (Exception e) {
                }

                holder.rl_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            //      notifyDataSetChanged();
                            try {
                                holder.rl_audio.setEnabled(false);
                                mediaPlayer.setDataSource(message.getBody());
                                mediaPlayer.prepareAsync();
                            } catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
                                e.printStackTrace();
                            }

                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    holder.rl_audio.setEnabled(true);
                                    mediaPlayer.start();
                                    for (int ii = 0; ii < messages.size(); ii++) {
                                        messages.get(ii).setIsplaying(false);
//                                        Log.e("TAGDaya", String.valueOf(messages.get(i).isIsplaying()) + messages.get(i).getBody());
                                    }
                                    message.setIsplaying(true);
                                    notifyDataSetChanged();
                                }
                            });

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    message.setIsplaying(false);
                                    holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));
                                }
                            });
                        } else {
                            try {
                                holder.rl_audio.setEnabled(false);
                                mediaPlayer.setDataSource(message.getBody());
                                mediaPlayer.prepareAsync();
                            } catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
                                e.printStackTrace();
                            }

                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    holder.rl_audio.setEnabled(true);
                                    mediaPlayer.start();
                                    message.setIsplaying(true);
                                    notifyDataSetChanged();
                                }
                            });

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    message.setIsplaying(false);
                                    holder.img_visualizer.setImageDrawable(context.getResources().getDrawable(R.drawable.voicevis));

                                }
                            });

                        }
                    }
                });

            }


        }

        return convertView;
    }


}

class MessageViewHolder {
    public View avatar;
    public CardView cv_image;
    public ImageView img_image, img_visualizer;
    public TextView tv_msg, tv_date, tv_timeduration;
    public RelativeLayout rl_audio;
}