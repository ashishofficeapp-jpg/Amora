package com.amora.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.amora.app.R;
import com.amora.app.response.TopReceiver.Result;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Result> list;
    String type;
    private static final int ITEM = 0;


    public RankAdapter(Context context, List<Result> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = null;
                v1 = inflater.inflate(R.layout.rank_layout, parent, false);
                viewHolder = new myViewHolder(v1);
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
                    if (type.equals("top_receiver")) {
                        Log.e("winnerActivity", "adapter1 " + type);
                        Glide.with(context)
                                .load(list.get(position).getUserId().getProfileImages().get(0).getImageName())
                                .apply(new RequestOptions().placeholder(R.drawable.default_profile).error(R.drawable.default_profile).circleCrop())
                                .into(holder.user_image);
                        holder.total_coin.setText(list.get(position).getTotalCoins());
                        holder.user_name.setText(list.get(position).getUserId().getName());

                    } else if (type.equals("top_giver")){
                        Log.e("winnerActivity", "adapter2 " + type);
                        /*Glide.with(context)
                                .load(list.get(position).getUserId().getProfileImages().get(0).getImageName())
                                .apply(new RequestOptions().placeholder(R.drawable.default_profile).error(R.drawable.default_profile).circleCrop())
                                .into(holder.user_image);*/
                        //holder.total_coin.setText(list.get(position).getTotalSpent());
                        Log.e("winnerActivity", "adapter3 " + list.get(position).getTotalCoins());
                        //holder.user_name.setText(list.get(position).getUserId().getName());
                    }

                    break;

                } catch (Exception e) {
                }

        }


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name, total_coin;
        LinearLayout main_container;

        public myViewHolder(View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            total_coin = itemView.findViewById(R.id.total_coin);
            main_container = itemView.findViewById(R.id.main_container);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("winnerActivity", "adapter3 " + list.size());
        return list == null ? 0 : list.size();
    }




    /*switch (viewType) {
        case ITEM:
            View v1 = null;
            if (type.equals("top_receiver")) {
                v1 = inflater.inflate(R.layout.rank_layout, parent, false);
            } else if (type.equals("top_giver")) {
                v1 = inflater.inflate(R.layout.rank_layout, parent, false);
            }
            viewHolder = new myViewHolder(v1);
            break;


    }*/

}