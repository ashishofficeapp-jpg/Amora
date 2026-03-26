package com.meetlive.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.Swipe.OnSwipeTouchListener;
import com.meetlive.app.Swipe.TouchListener;
import com.meetlive.app.activity.ViewProfile;
import com.meetlive.app.fragment.HomeFragment;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<UserListResponse.Data> list;
    String type;
    HomeFragment homeFragment;

    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    public HomeUserAdapter(Context context, PaginationAdapterCallback mCallback, String type, HomeFragment homeFragment) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
        this.type = type;
        this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                if (type.equals("dashboard")) {
                    v1 = inflater.inflate(R.layout.adapter_user_gridview, parent, false);
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
                        if (type.equals("dashboard")) {
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
                            //               holder.is_online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_circle_green, 0, 0, 0);
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
                            homeFragment.startVideoCall(String.valueOf(list.get(position).getProfile_id()),
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

                                String[] dob = list.get(position).getDob().split("-");
                                int date = Integer.parseInt(dob[0]);
                                int month = Integer.parseInt(dob[1]);
                                int year = Integer.parseInt(dob[2]);
                                holder.user_age.setText(getAge(year, month, date));

                            } catch (Exception e) {
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                } catch (Exception e) {
                }
            case LOADING:

                if (retryPageLoad) {

                } else {

                }
                break;
        }
    }

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
        //  private ProgressBar mProgressBar;
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