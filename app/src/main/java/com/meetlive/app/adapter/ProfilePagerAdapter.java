package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.dialog.PictureView;
import com.meetlive.app.response.UserListResponse;

import java.util.List;

public class ProfilePagerAdapter extends RecyclerView.Adapter<ProfilePagerAdapter.ViewHolder> {

    List<UserListResponse.UserPics> list;
    Context context;
    boolean canOpen;

    public ProfilePagerAdapter(Context context, List<UserListResponse.UserPics> list, boolean canOpen) {
        this.context = context;
        this.list = list;
        this.canOpen = canOpen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (canOpen) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_viewpics, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_viewpics2, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            Glide.with(context)
                    .load(list.get(position).getImage_name())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_photo_library_gray_100dp).error(R.drawable.ic_photo_library_gray_100dp))
                    .into(holder.imageView);
        } catch (Exception e) {
        }
        //  holder.imageView.setImageResource(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (canOpen) {
                        new PictureView(context, list, getAdapterPosition());
                    }
                }
            });
        }
    }
}