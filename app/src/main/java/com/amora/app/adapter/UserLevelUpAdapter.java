package com.amora.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amora.app.R;
import com.amora.app.response.GetUserLevelResponse.UserLevelResult;

import java.util.List;

public class UserLevelUpAdapter extends RecyclerView.Adapter<UserLevelUpAdapter.ViewHolder> {
    Context context;
    List<UserLevelResult> list;
    public String[] mColors = {"#eeeef6", "#ffffff"};
    // first define colors
    private final int[] backgroundColors = {
            R.drawable.level_one_bg,
            R.drawable.level_two_bg,
            R.drawable.level_three_bg,
            R.drawable.level_four_bg,
            R.drawable.level_five_bg,
            R.drawable.level_six_bg,
            R.drawable.level_seven_bg,
            R.drawable.level_eight_bg,
            R.drawable.level_nine_bg,
            R.drawable.level_ten_bg};

    public UserLevelUpAdapter(Context context, List<UserLevelResult> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_level_up_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            holder.layout_level.setBackgroundColor(Color.parseColor(mColors[position % 2])); // 2 can be replaced by mColors.length
            holder.tv_level_rule.setText("Lv " + list.get(position).getLevelId());
            holder.tv_level_rule_input.setText(String.valueOf(list.get(position).getEndCoins()));
            
            if (position % 9 == 0) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_one_bg));
            } else if (position % 9 == 1) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_two_bg));
            } else if (position % 9 == 2) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_three_bg));
            } else if (position % 9 == 3) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_four_bg));
            } else if (position % 9 == 4) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_five_bg));
            } else if (position % 9 == 5) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_six_bg));
            } else if (position % 9 == 6) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_seven_bg));
            } else if (position % 9 == 7) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_eight_bg));
            } else if (position % 9 == 8) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_nine_bg));
            } else if (position % 9 == 9) {
                holder.tv_level_rule.setBackground(ContextCompat.getDrawable(context, R.drawable.level_ten_bg));
            }
            // in onBindViewHolder
            int index = position % backgroundColors.length;
            Drawable drawable = ContextCompat.getDrawable(context, backgroundColors[index]);
            holder.tv_level_rule.setBackground(drawable);
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

        TextView tv_level_rule;
        TextView tv_level_rule_input;
        LinearLayout layout_level;

        public ViewHolder(View view) {
            super(view);

            tv_level_rule = view.findViewById(R.id.tv_level_rule);
            tv_level_rule_input = view.findViewById(R.id.tv_level_rule_input);
            layout_level = view.findViewById(R.id.layout_level);
        }


    }
}



