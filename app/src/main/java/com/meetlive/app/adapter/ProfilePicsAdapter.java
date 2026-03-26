package com.meetlive.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.meetlive.app.R;
import com.meetlive.app.response.UserListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfilePicsAdapter extends RecyclerView.Adapter<ProfilePicsAdapter.ViewHolder> {
    Context context;
    List<UserListResponse.UserPics> imageList;

    public ProfilePicsAdapter(Context context, List<UserListResponse.UserPics> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

     /* if (imageList.get(position).getImage_name().equals("add_pic")) {
            Glide.with(context).load(R.drawable.ic_add)
                    .fitCenter()
                    .into(holder.imageView);
        } else {
        }*/


        if (!imageList.get(position).getImage_name().equals("")) {
            Log.e("profileImallgesNEW", "ArrayImageName "+imageList.get(position).getImage_name());
            Glide.with(context)
                    .load(imageList.get(position).getImage_name())
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.ic_add)
                    .centerCrop()
                    .into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
      //  Log.e("profileImallgesNEW", "ArraySizeAD " + imageList.size());
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.pic);

            // if we are using for margin the recyclerview move in bottom automatic
            /*GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            float margin = convertDpToPixel(5);
            layoutParams.setMargins((int) margin, (int) margin, (int) margin, (int) margin);
            itemView.setLayoutParams(layoutParams);*/
         /* imageView.setOnClickListener(view -> {
            });*/

        }

    }

    public float convertDpToPixel(final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}