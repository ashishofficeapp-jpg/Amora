package com.amora.app.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amora.app.R;
import com.amora.app.dialog.GiftDailogNew;
import com.amora.app.response.gift.Gift;
import com.amora.app.utils.RecyclerClickListner;

import java.util.List;


public class GiftAdapterNew extends RecyclerView.Adapter<GiftAdapterNew.ViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    Activity currentActivity;
    private List<Gift> giftImageModelList;
    private int focusedItem = 0;

    public GiftAdapterNew(Activity currentActivity, List<Gift> giftImageModels) {
        this.currentActivity = currentActivity;
        this.giftImageModelList = giftImageModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_gift, parent, false);
        int height = parent.getMeasuredHeight() / 3;
        view.setMinimumHeight(height);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (giftImageModelList != null && giftImageModelList.size() > position) {

            holder.tv_coin.setText(String.valueOf(giftImageModelList.get(position).getAmount()));
            holder.giftIV.setImageResource(getGiftImage(String.valueOf(giftImageModelList.get(position).getId())));
            // Here I am just highlighting the background

            // Set selected state; use a state list drawable to style the view
            holder.contact_layout.setBackgroundResource(focusedItem == position ? R.drawable.gift_selected : R.drawable.gift_unselected);

            if (focusedItem == position) {
                Log.e("reach", "working adapter:=");
                holder.tv_bt_input.setVisibility(View.VISIBLE);
            } else {
                holder.tv_bt_input.setVisibility(View.GONE);
            }
            holder.tv_bt_input.setText("x" + GiftDailogNew.btValue);
            Log.e("reach", "working adapter value:="+GiftDailogNew.btValue);
        }

    }

    @Override
    public int getItemCount() {
        if (giftImageModelList == null) return 0;
        return giftImageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView giftIV;
        TextView tv_coin,tv_bt_input;
        RelativeLayout contact_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            giftIV = itemView.findViewById(R.id.img_gift);
            tv_coin = itemView.findViewById(R.id.tv_coin);
            tv_bt_input = itemView.findViewById(R.id.tv_bt_input);
            contact_layout = itemView.findViewById(R.id.contact_layout);

            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(focusedItem);
                    focusedItem = getLayoutPosition();
                    notifyItemChanged(focusedItem);
                }
            });

        }
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }
    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;
        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }
    public void setOnItemClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
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
}


