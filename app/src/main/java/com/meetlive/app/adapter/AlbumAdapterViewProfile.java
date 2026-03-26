package com.meetlive.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.dialog.PictureViewOnViewProfile;
import com.meetlive.app.dialog.PictureViewOnViewProfileAlbum;
import com.meetlive.app.response.UserListResponseNew.FemaleImage;

import java.util.List;
public class AlbumAdapterViewProfile extends RecyclerView.Adapter<AlbumAdapterViewProfile.ViewHolder> {

        List<FemaleImage> list;
        Context context;
        boolean canOpen;


        public AlbumAdapterViewProfile(Context context, List<FemaleImage> list, boolean canOpen) {
            this.context = context;
            this.list = list;
            this.canOpen = canOpen;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (canOpen) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_album, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_album2, parent, false);
            }
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            try {
                Glide.with(context)
                        .load(list.get(position).getImageName())
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
            Log.e("listFemaleSize", String.valueOf(list.size()));
            return list.size() ;
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
                            new PictureViewOnViewProfileAlbum(context, list, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }