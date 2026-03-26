package com.meetlive.app.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.meetlive.app.R;
import com.meetlive.app.Swipe.OnSwipeTouchListener;
import com.meetlive.app.Swipe.TouchListener;
import com.meetlive.app.activity.ViewProfile;
import com.meetlive.app.fragment.DiscoverFragment;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.utils.PaginationAdapterCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DiscoverUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<UserListResponse.Data> list;
    String type;
    DiscoverFragment discoverFragment;
    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    private String fromVideo;
    String duration ;
    int i = 0;

    public DiscoverUserAdapter(Context context, PaginationAdapterCallback mCallback, String type, DiscoverFragment discoverFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.discoverFragment = discoverFragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View v1 = null;
                if (type.equals("discover")) {
                    v1 = inflater.inflate(R.layout.adapter_user_gridview_discover, parent, false);
                }
                viewHolder = new myViewHolder(v1);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                try {
                    final myViewHolder holder = (myViewHolder) hld;

                    if (!list.get(position).getProfile_images().get(0).getImage_name().equals("")) {
                        if (type.equals("search")) {
                            if (list.get(position).getProfile_images().size() > 0) {
                                Glide.with(context).load(list.get(position).getProfile_images().get(0).getImage_name())
                                        .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                                (R.drawable.default_profile).circleCrop()).into(holder.user_image);
                            } else {
                                Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                            }

                        } else {

                            if (list.get(position).getProfile_images().size() > 0) {
                                Glide.with(context)
                                        .load(list.get(position).getProfile_images().get(0).getImage_name())
                                        .apply(new RequestOptions().placeholder(R.drawable.female_placeholder).
                                                error(R.drawable.female_placeholder)).into(holder.user_image);
                            } else {
                                Glide.with(context).load(R.drawable.female_placeholder).apply(new RequestOptions()).into(holder.user_image);
                            }
                        }
                    }
                    holder.total_flash.setText(String.valueOf(list.get(position).getFavorite_count()));
                    holder.user_name.setText(list.get(position).getName());
                    holder.about_user.setText(list.get(position).getAbout_user());
                    holder.countryDisplay.setText(list.get(position).getCity());

                    if (list.get(position).getIs_busy() == 0) {
                        if (list.get(position).getIs_online() == 1) {
                            holder.is_online.setText("Online");
                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                            // holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
                        } else {
                            holder.is_online.setText("Offline");
                            holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_grey, 0, 0, 0);
                        }
                    } else {
                        holder.is_online.setText("Busy");
                        holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_orange, 0, 0, 0);
                    }

                    holder.img_video_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("TAG", ">> imgVideoCall tap");
                            discoverFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
                                    String.valueOf(list.get(position).getCall_rate()),
                                    list.get(position).getId(),
                                    list.get(position).getName(),
                                    list.get(position).getProfile_images().get(0).getImage_name());
                        }
                    });

                    holder.user_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent = new Intent(context, ViewProfile.class);
                                Bundle bundle = new Bundle();
                                //hide intent sending data homefragment to ViewprofileActivity...
                                //bundle.putSerializable("user_data", list.get(position));
                                bundle.putSerializable("id", list.get(position).getId());
                                bundle.putSerializable("profileId", list.get(position).getProfile_id());
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                            } catch (Exception e) {
                            }

                            try {
                                String[] dob = list.get(position).getDob().split("-");
                                int date = Integer.parseInt(dob[0]);
                                int month = Integer.parseInt(dob[1]);
                                int year = Integer.parseInt(dob[2]);
                                holder.user_age.setText(getAge(year, month, date));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    holder.user_image.setOnTouchListener(new OnSwipeTouchListener(context, new TouchListener() {
                        @Override
                        public void onSingleTap() {
                            Log.e("TAG", ">> Single tap");
                            try {
                                Intent intent = new Intent(context, ViewProfile.class);
                                Bundle bundle = new Bundle();
                                //hide intent sending data homefragment to ViewprofileActivity...
                                //  bundle.putSerializable("user_data", list.get(position));
                                bundle.putSerializable("id", list.get(position).getId());
                                bundle.putSerializable("profileId", list.get(position).getProfile_id());

                                intent.putExtras(bundle);

                                context.startActivity(intent);

                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onDoubleTap() {
                            Log.e("TAG", ">> Double tap");
                        }

                        @Override
                        public void onLongPress() {
                            Log.e("TAG", ">> Long press");
                        }

                        @Override
                        public void onSwipeLeft() {
                            Log.e("TAG", ">> Swipe left");

                        }

                        @Override
                        public void onSwipeRight() {
                            Log.e("TAG", ">> Swipe right");


                        }
                    }));
                    try {
                        String[] dob = list.get(position).getDob().split("-");
                        int date = Integer.parseInt(dob[0]);
                        int month = Integer.parseInt(dob[1]);
                        int year = Integer.parseInt(dob[2]);
                        holder.user_age.setText(getAge(year, month, date));

                        //discoverFragment.playVideo((myViewHolder) hld, position);
                        Animation call = AnimationUtils.loadAnimation(context, R.anim.call);
                        holder.img_video_call.startAnimation(call);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                } catch (Exception e) {
                }
            case LOADING:
                try {
                } catch (Exception e) {

                }
                if (retryPageLoad) {

                } else {

                }
                break;


        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        context.registerReceiver(myVideoReceiver, new IntentFilter("FBR-USER-VIDEO"));

        //play video
        MaterialCardView cv_video;
        LottieAnimationView search_loader;
        VideoView vv_video;
        cv_video = holder.itemView.findViewById(R.id.cv_video);
        search_loader = holder.itemView.findViewById(R.id.search_loader);
        vv_video = holder.itemView.findViewById(R.id.vv_video);

        /*if (TextUtils.isEmpty(fromVideo)) {
            if (fromVideo != null && fromVideo.equals("pause")) {
                vv_video.setVisibility(View.GONE);
            }
        }*/

        Log.e("listnew", String.valueOf(list.size()));

        String VideoUrl = "";
        UserListResponse.Data data = list.get(holder.getPosition());
        if (data != null) {
            List<UserListResponse.ProfileVideo> profileVideoslist = data.getProfileVideo();
            List<UserListResponse.ProfileVideo> newList;
            if (profileVideoslist != null && profileVideoslist.size() > 0) {
                for (i = 0; i < profileVideoslist.size(); i++) {
                    UserListResponse.ProfileVideo profileVideo = profileVideoslist.get(i);
                    Log.e("Videolist", "VideoListSize "+ String.valueOf(profileVideoslist.size()));
                    if (profileVideo != null) {

                        VideoUrl = profileVideo.getVideoUrl();
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(profileVideo.getVideoUrl(), new HashMap<String, String>());
                                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                long timeInMillisec = Long.parseLong(time);
                                retriever.release();
                                duration = convertMillieToHMmSs(timeInMillisec); //use this duration
                                Log.e("getVideo", "VideoDuration  " + duration);
                            }
                        });

                        /*for (j = 0; j < list.size(); j++) {
                            link = list.get(j).getVideo_link();
                            Uri uri = Uri.parse(link);
                            videoView.setVideoURI(uri);
                            videoView.requestFocus();
                            videoView.start();

                        }*/


                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UserListResponse.ProfileVideo profileVideo = profileVideoslist.get(i + 1);
                                if (profileVideo != null) {
                                    String videour = profileVideo.getVideoUrl();
                                    Uri uri = Uri.parse(videour);
                                    vv_video.setVideoURI(uri);
                                    search_loader.setVisibility(View.VISIBLE);

                                    vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mediaPlayer) {
                                            search_loader.setVisibility(View.GONE);
                                            mediaPlayer.setVolume(5.0f, 10f);
                                            vv_video.start();
                                            vv_video.requestFocus();
                                        }
                                    });
                                    vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            mediaPlayer.stop();
                                            cv_video.setVisibility(View.GONE);
                                            vv_video.setVisibility(View.GONE);
                                        }
                                    });
                                }
                                //hai to
                                //paly kra dena
                            }
                        }, Integer.parseInt(duration));*/

                    }
                }

            }
        }
        Log.e("vURLB", VideoUrl);
        if (TextUtils.isEmpty(VideoUrl)) {
            Log.e("vURLA", VideoUrl);
            if (vv_video != null) {
                search_loader.setVisibility(View.GONE);
                cv_video.setVisibility(View.GONE);
                vv_video.setVisibility(View.GONE);
            }
        } else {
            cv_video.setVisibility(View.VISIBLE);
            vv_video.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(VideoUrl);
            vv_video.setVideoURI(uri);
            search_loader.setVisibility(View.VISIBLE);

            vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    search_loader.setVisibility(View.GONE);
                    mediaPlayer.setVolume(5.0f, 10f);
                    vv_video.start();
                    vv_video.requestFocus();
                }
            });
            vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    cv_video.setVisibility(View.GONE);
                    vv_video.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        context.unregisterReceiver(myVideoReceiver);

        MaterialCardView cv_video;
        VideoView vv_video;
        cv_video = holder.itemView.findViewById(R.id.cv_video);
        vv_video = holder.itemView.findViewById(R.id.vv_video);
        if (vv_video != null && vv_video.isPlaying()) {
            vv_video.canPause();
            cv_video.setVisibility(View.GONE);
        }

    }

    public BroadcastReceiver myVideoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fromVideo = intent.getStringExtra("video");
            Log.e("videoData", "Videovalue " + fromVideo);
        }
    };

    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d", seconds * 1000);
        }

    }
   /* public void destroyer(){
        context.unregisterReceiver(myVideoReceiver);
    }

    public void start(){
        context.registerReceiver(myVideoReceiver, new IntentFilter("FBR-USER-VIDEO"));

    }*/

    /*private void playVideo(myViewHolder holder, int position) {
        Log.e("vvURLAdatbefore", String.valueOf(position));
        String VideoUrl = list.get(position).getProfileVideo().get(0).getVideoName();
        Log.e("vURLB", VideoUrl);
        if (TextUtils.isEmpty(VideoUrl)) {
            Log.e("vURLA", VideoUrl);
            holder.cv_video.setVisibility(View.GONE);
            holder.vv_video.setVisibility(View.GONE);
        } else {
            Log.e("vURLAdat", VideoUrl);
            Log.e("vURLafter", String.valueOf(position));
            Log.e("vURLD", String.valueOf(list.get(position).getId()));
            Log.e("vURLN", String.valueOf(list.get(position).getName()));

            holder.cv_video.setVisibility(View.VISIBLE);
            holder.vv_video.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(VideoUrl);
            holder.vv_video.setVideoURI(uri);
            holder.search_loader.setVisibility(View.VISIBLE);
            holder.vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    holder.search_loader.setVisibility(View.GONE);
                    mediaPlayer.setVolume(0f, 0f);
                    holder.vv_video.start();
                    holder.vv_video.requestFocus();
                }
            });
            holder.vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    //holder.cv_video.setVisibility(View.GONE);
                    //holder.vv_video.setVisibility(View.GONE);
                    holder.vv_video.start();
                    holder.vv_video.requestFocus();
                }
            });
        }

    }*/

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
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

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image, img_video_call;
        TextView total_flash, user_name, user_age, about_user, is_online, countryDisplay;
        RelativeLayout container;

        public myViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            total_flash = itemView.findViewById(R.id.total_flash);
            user_age = itemView.findViewById(R.id.user_age);
            user_image = itemView.findViewById(R.id.user_image);
            img_video_call = itemView.findViewById(R.id.img_video_call);
            user_name = itemView.findViewById(R.id.user_name);
            about_user = itemView.findViewById(R.id.about_user);
            is_online = itemView.findViewById(R.id.is_online);
            countryDisplay = itemView.findViewById(R.id.tv_countryName);

        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);
            //mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);
            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }

    }

           /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(UserListResponse.Data results) {
        list.add(results);
        notifyItemInserted(list.size() - 1);

    }

    public void addAll(List<UserListResponse.Data> moveResults) {
        for (UserListResponse.Data result : moveResults) {
            add(result);
        }
    }

    public void updateItem(int position, UserListResponse.Data data) {
        list.set(position, data);
        notifyItemChanged(position);
    }

    public void remove(UserListResponse.Result r) {
        int position = list.indexOf(r);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserListResponse.Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = list.size() - 1;
        UserListResponse.Data result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public UserListResponse.Data getItem(int position) {
        return list.get(position);
    }


}