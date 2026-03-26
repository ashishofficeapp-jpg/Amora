package com.meetlive.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meetlive.app.R;
import com.meetlive.app.activity.PurchaseCoins;
import com.meetlive.app.response.RechargePlanResponse;
import com.meetlive.app.utils.SessionManager;

import java.util.List;

public class CoinPlansAdapter extends RecyclerView.Adapter<CoinPlansAdapter.myViewHolder> {

    Context context;
    List<RechargePlanResponse.Data> list;
    int selecetdPlan = 0;
    String type;
    SessionManager sessionManager;

    public CoinPlansAdapter(Context context, List<RechargePlanResponse.Data> list, String type) {

        this.context = context;
        this.list = list;
        this.type = type;
        sessionManager = new SessionManager(context);
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (type.equals("activity")) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coin_plans, parent, false);
            return new myViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recharge_plan, parent, false);
            return new myViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        holder.coint_count.setText(String.valueOf(list.get(position).getPoints()));
        if (sessionManager.getUserLocation().equals("India")) {
            holder.price.setText("₹ " + list.get(position).getAmount());
            holder.tv_vipPlan.setText("Free "+ list.get(position).getValidity_in_days()+ " Days VIP");
        } else {
            holder.price.setText("$ " + list.get(position).getAmount());
            holder.tv_vipPlan.setText("Free "+ list.get(position).getValidity_in_days()+ " Days VIP");
        }
        if (list.get(position).getType() == 2) {
            holder.plan_type.setText("Video Call Plan");
            holder.validity.setText("Validity : N/A");

        } else if (list.get(position).getType() == 6) {
            holder.plan_type.setText("Chat Plan");
            holder.validity.setText("Validity : " + list.get(position).getValidity_in_days() + " days");

        } else if (list.get(position).getType() == 7) {
            holder.plan_type.setText("Video Call + Chat Plan");
            holder.validity.setText("Validity : " + list.get(position).getValidity_in_days() + " days");
        }

       /* if (selecetdPlan == position) {
            holder.container.setBackgroundResource(R.drawable.rounded_corner_stroke);
        } else {
            holder.container.setBackgroundResource(R.drawable.rounded_corner_white);
        }*/
    }

    @Override
    public int getItemCount() {
        //return list.siz(); replace with 3. 9/4/21
       // return 3;
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView coint_count, price, plan_type, validity,tv_vipPlan;
        FrameLayout container;

        public myViewHolder(View itemView) {
            super(itemView);

            validity = itemView.findViewById(R.id.validity);
            plan_type = itemView.findViewById(R.id.plan_type);
            tv_vipPlan = itemView.findViewById(R.id.tv_vipPlan);
            coint_count = itemView.findViewById(R.id.coint_count);
            price = itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);

            container.setOnClickListener(view -> {
                try {
                    selecetdPlan = getAdapterPosition();
                    container.setBackgroundResource(R.drawable.rounded_corner_stroke);
                    ((PurchaseCoins) context).selectedPlan((getAdapterPosition()));

                    notifyDataSetChanged();
                } catch (Exception e) {
                }
            });
        }
    }
}