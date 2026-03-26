package com.amora.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.amora.app.R;
import com.amora.app.activity.ViewProfile;
import com.amora.app.response.UserListResponse;
import com.amora.app.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<UserListResponse.Data> list;

    private PaginationAdapterCallback mCallback;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;


    public SearchUserAdapter(Context context, PaginationAdapterCallback mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.adapter_search, parent, false);
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
                        if (list.get(position).getProfile_images().size() > 0) {
                            Glide.with(context).load(list.get(position).getProfile_images().get(0).getImage_name())
                                    .apply(new RequestOptions().placeholder(R.drawable.default_profile).error
                                            (R.drawable.default_profile).circleCrop()).into(holder.user_image);
                        } else {
                            Glide.with(context).load(R.drawable.default_profile).apply(new RequestOptions()).into(holder.user_image);
                        }

                    }

                    holder.total_flash.setText(String.valueOf(list.get(position).getFavorite_count()));
                    holder.user_name.setText(list.get(position).getName());
                    String[] dob = list.get(position).getDob().split("-");
                    int date = Integer.parseInt(dob[0]);
                    int month = Integer.parseInt(dob[1]);
                    int year = Integer.parseInt(dob[2]);
                    holder.user_age.setText(getAge(year, month, date));

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


                    holder.main_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent = new Intent(context, ViewProfile.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id", list.get(position).getId());
                                bundle.putSerializable("profileId", list.get(position).getProfile_id());
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                            } catch (Exception e) {
                            }
                        }
                    });
                    break;

                } catch (Exception e) {
                }

            case LOADING:
                if (retryPageLoad) {

                } else {

                }
                break;
        }

        // call Animation function
        setAnimation(hld.itemView, position);
        //setNewAnimation(hld.itemView,position);

    }
    private int lastPosition = -1;
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        //if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));
            viewToAnimate.startAnimation(anim);
           // lastPosition = position;
       // }
    }

    private void setNewAnimation(View view, int position){
       // If the bound view wasn't previously displayed on screen, it's animated of
        if (position > lastPosition) {
            //Animation slidIn = AnimationUtils.loadAnimation(context, R.anim.r_anim);
            Animation slidIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(slidIn);
            lastPosition = position;
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

        ImageView user_image;
        TextView total_flash, user_name, user_age, is_online;
        LinearLayout main_container;

        public myViewHolder(View itemView) {
            super(itemView);

            total_flash = itemView.findViewById(R.id.total_flash);
            user_age = itemView.findViewById(R.id.user_age);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            is_online = itemView.findViewById(R.id.is_online);
            main_container = itemView.findViewById(R.id.main_container);
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