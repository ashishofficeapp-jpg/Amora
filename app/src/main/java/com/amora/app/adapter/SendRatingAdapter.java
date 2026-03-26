package com.amora.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.amora.app.R;
import com.amora.app.response.SendTagModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SendRatingAdapter extends BaseAdapter {
    private Context mContext;
    private List<SendTagModel> headers;
    public HashMap<Integer, Boolean> hashMapSelected;
    private ArrayList<Integer> positionTracker = new ArrayList<Integer>();

    public SendRatingAdapter(Context context, List<SendTagModel> headers) {
        this.mContext = context;
        this.headers = headers;
        hashMapSelected = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            hashMapSelected.put(i, false);
        }
        hashMapSelected.put(0, true);
        positionTracker.add(0);
    }


    @Override
    public int getCount() {
        return headers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void makeAllUnselect(int position) {
        hashMapSelected.put(position, true);
        positionTracker.add(position);

        if (positionTracker.size() >= 4) {
            hashMapSelected.put(positionTracker.get(positionTracker.size() - 4), false);
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_tag.setText(headers.get(position).getTag_name());
        //  viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white300));
        viewHolder.linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bg_rating));


        if (hashMapSelected.get(position) == true) {
            //viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rating));
            viewHolder.linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bg_ratingselected));
            viewHolder.tv_tag.setTextColor(ContextCompat.getColor(mContext, R.color.white300));

        } else {
            //      viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.perpal));
            viewHolder.linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_bg_rating));
            viewHolder.tv_tag.setTextColor(ContextCompat.getColor(mContext, R.color.backgroundEnd));
        }
        return convertView;
    }


    private class ViewHolder {
        TextView tv_tag;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            tv_tag = (TextView) view.findViewById(R.id.tv_tag);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        }
    }
}