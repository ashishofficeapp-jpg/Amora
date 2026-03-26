package com.amora.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amora.app.R;
import com.amora.app.response.UserListResponseNew.GetRatingTag;


import java.util.Arrays;
import java.util.List;

public class RateCountDisplayAdapter extends RecyclerView.Adapter<RateCountDisplayAdapter.ViewHolder> {
    Context context;
    List<GetRatingTag> list;

    public RateCountDisplayAdapter(Context context, List<GetRatingTag> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RateCountDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratecountdisplay, parent, false);
        return new RateCountDisplayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateCountDisplayAdapter.ViewHolder holder, int position) {

        try {
            String tag  = list.get(position).getTag();
            List<String> tagList = Arrays.asList(tag.split("\\,"));

           for (int i = 0; i<= tagList.size(); i++){
                holder.tv_tag_show.setText(tagList.get(i));
               holder.tv_tag_count.setText(" " + list.get(position).getTotalCount());
            }

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tag_show;
        TextView tv_tag_count;


        public ViewHolder(View view) {
            super(view);

            tv_tag_show = view.findViewById(R.id.tv_tag_show);
            tv_tag_count = view.findViewById(R.id.tv_tag_count);
        }


    }
}