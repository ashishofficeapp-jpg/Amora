package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meetlive.app.R;
import com.meetlive.app.response.FavNew.FavNewData;
import com.meetlive.app.utils.SessionManager;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.myViewHolder> {

    Context context;
    List<FavNewData> list;


    public FavouriteAdapter(Context context, List<FavNewData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fav_listview, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        try {
            if (list.get(position).getFavorites().get(0).getProfileImages().size() > 0) {
               /* if (new SessionManager(context).getGender().equals("male")) {*/
                    Glide.with(context)
                            .load(list.get(position).getFavorites().get(0).getProfileImages().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_photo_library_gray_100dp).
                                    error(R.drawable.ic_photo_library_gray_100dp)).into(holder.user_image);
               /* } else {*/
                    Glide.with(context)
                            .load(list.get(position).getFavorites().get(0).getProfileImages().get(0).getImage_name())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_photo_library_gray_100dp).
                                    error(R.drawable.maleplaceholder)).into(holder.user_image);

               // }
            }
            //   holder.total_flash.setText(String.valueOf(list.get(position).getFavorite_id()));
            holder.user_name.setText(list.get(position).getFavorites().get(0).getName());
            if (list.get(position).getFavorites().get(0).getIsOnline() == 0) {
                holder.is_online.setVisibility(View.GONE);
            }

            /*if (new SessionManager(context).getGender().equals("male")) {*/
                holder.available_coins.setVisibility(View.GONE);
            /*} else {
                holder.available_coins.setText(String.valueOf(list.get(position).getPoints()));
            }*/
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView total_flash, user_name, is_online, available_coins;
        CardView container;

        public myViewHolder(View itemView) {
            super(itemView);
            total_flash = itemView.findViewById(R.id.total_flash);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            is_online = itemView.findViewById(R.id.is_online);
            available_coins = itemView.findViewById(R.id.available_coins);

        }
    }
}