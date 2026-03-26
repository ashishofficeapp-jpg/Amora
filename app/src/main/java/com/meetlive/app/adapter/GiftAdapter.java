package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meetlive.app.R;
import com.meetlive.app.response.gift.Gift;

import java.util.List;


public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ContactViewHolder> {
    private List<Gift> giftArrayList;
    private int rowLayout;
    private Context context;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout contactLayout;
        TextView tv_coin;
        ImageView img_gift;

        public ContactViewHolder(View v) {
            super(v);

            contactLayout = (RelativeLayout) v.findViewById(R.id.contact_layout);
            img_gift = (ImageView) v.findViewById(R.id.img_gift);
            tv_coin = (TextView) v.findViewById(R.id.tv_coin);
        }
    }

    public GiftAdapter(List<Gift> giftArrayList, int rowLayout, Context context) {
        this.giftArrayList = giftArrayList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

      /*  holder.tv_coin.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/POPPINS-BOLD_0.TTF"));*/

        holder.tv_coin.setText(String.valueOf(giftArrayList.get(position).getAmount()));

        holder.img_gift.setImageResource(getGiftImage(String.valueOf(giftArrayList.get(position).getId())));

      
       /* Glide.with(context)
                .load(giftArrayList.get(position).getGiftPhoto())
                .into(holder.img_gift);*/
    }

    int getGiftImage(String id) {
        int imgResource = 0;
        if (id.equals("18")) {
            imgResource = R.drawable.heart;

        } else if (id.equals("21")) {
            imgResource = R.drawable.lips;

        } else if (id.equals("22")) {
            imgResource = R.drawable.bunny;

        } else if (id.equals("23")) {
            imgResource = R.drawable.rose;

        } else if (id.equals("24")) {
            imgResource = R.drawable.boygirl;

        } else if (id.equals("25")) {
            imgResource = R.drawable.sandle;

        } else if (id.equals("26")) {
            imgResource = R.drawable.frock;

        } else if (id.equals("27")) {
            imgResource = R.drawable.car;

        } else if (id.equals("28")) {
            imgResource = R.drawable.ship;

        } else if (id.equals("29")) {
            imgResource = R.drawable.tajmahal;

        }else if (id.equals("30")) {
            imgResource = R.drawable.crown;

        }else if (id.equals("31")) {
            imgResource = R.drawable.bangels;

        }else if (id.equals("32")) {
            imgResource = R.drawable.diamonds;

        }else if (id.equals("33")) {
            imgResource = R.drawable.lovers;

        }

        return imgResource;
    }

    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }
}