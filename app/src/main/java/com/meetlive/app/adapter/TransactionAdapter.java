package com.meetlive.app.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.meetlive.app.R;
import com.meetlive.app.response.NewWallet.WalletHistoryData;
import com.meetlive.app.utils.SessionManager;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.myViewHolder> {

    Context context;
    NavigableMap<String, List<WalletHistoryData>> list;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public TransactionAdapter(Context context, NavigableMap<String, List<WalletHistoryData>> descendingMap) {

        this.context = context;
        this.list = descendingMap;

        for (int i = 0; i < list.size(); i++) {
            expandState.append(i, false);
        }
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        holder.transaction_name.setText(getHashMapKeyFromIndex(list, position));
        final boolean isExpanded = expandState.get(position);
        //   holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.expandableLayout.setVisibility(View.VISIBLE);
        //llContainer
        holder.llContainer.removeAllViews();
        LayoutInflater layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Integer sum = 0;
        for (int i = 0; i < list.get(getHashMapKeyFromIndex(list, position)).size(); i++) {

            //Log.e("VALL", list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
            View view = layoutInflator.inflate(R.layout.row_subcat_child, null);
            TextView transaction_name = view.findViewById(R.id.transaction_name);
            TextView tv_price = view.findViewById(R.id.amount);
            TextView date = view.findViewById(R.id.date);
            RelativeLayout icon = view.findViewById(R.id.icon);

            transaction_name.setText(list.get(getHashMapKeyFromIndex(list, position)).get(i).getTransactionDes());

          /*  if (new SessionManager(context).getGender().equals("male")) {
                tv_price.setText(list.get(getHashMapKeyFromIndex(list, position)).get(i).getDebit().toString());
            } else {
                tv_price.setText(list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
            }*/

            date.setText(list.get(getHashMapKeyFromIndex(list, position)).get(i).getCreatedAt());
//2,6,7,8
            if (list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit() != 0) {
                icon.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorGreen));
                tv_price.setTextColor(ContextCompat.getColorStateList(context, R.color.colorGreen));
                if (new SessionManager(context).getGender().equals("male")) {
                    if (list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 2 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 6 ||
                            list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 7 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 8) {

                        tv_price.setText("+ " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
                    } else {
                        tv_price.setText("+ " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getDebit().toString());
                    }
                } else {
                    tv_price.setText("+ " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
                }
            } else {
                icon.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.pinkNew));
                tv_price.setTextColor(ContextCompat.getColorStateList(context, R.color.pinkNew));

                if (new SessionManager(context).getGender().equals("male")) {
                    // tv_price.setText("- " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getDebit().toString());
                    if (list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 2 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 6 ||
                            list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 7 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 8) {
                        tv_price.setText("+ " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
                    } else {
                        tv_price.setText("+ " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getDebit().toString());
                    }

                } else {
                    tv_price.setText("- " + list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit().toString());
                }
            }

            if (new SessionManager(context).getGender().equals("male")) {
                if (list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 2 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 6 ||
                        list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 7 || list.get(getHashMapKeyFromIndex(list, position)).get(i).getStatus() == 8) {
                    sum = sum + (list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit());
                } else {
                    sum = sum + (list.get(getHashMapKeyFromIndex(list, position)).get(i).getDebit());
                }
            } else {
                sum = sum + (list.get(getHashMapKeyFromIndex(list, position)).get(i).getCredit());
            }
            holder.llContainer.addView(view);

        }

        holder.textView_total.setText(sum + " Coins");


    /*    holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout, holder.buttonLayout, position);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout icon;
        TextView transaction_name, description, amount, date, textView_total;

        TextView tv_inputVideoCoinNaturalWeek,tv_inputAudioCoinNaturalWeek,tv_inputGiftCoinNaturalWeek,tv_inputTotalCoinNaturalWeek;
        TextView tv_inputVideoCoinLastWeek,tv_inputAudioCoinLastWeek,tv_inputGiftCoinLastWeek,tv_inputTotalCoinLastWeek;

        LinearLayout expandableLayout, llContainer;
        RelativeLayout buttonLayout;

        public myViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            transaction_name = itemView.findViewById(R.id.textView_name);
            textView_total = itemView.findViewById(R.id.textView_total);

            //define new variable for income report 14/4/21
           /* tv_inputVideoCoinNaturalWeek = itemView.findViewById(R.id.tv_inputVideoCoinNaturalWeek);
            tv_inputAudioCoinNaturalWeek = itemView.findViewById(R.id.tv_inputAudioCoinNaturalWeek);
            tv_inputGiftCoinNaturalWeek = itemView.findViewById(R.id.tv_inputGiftCoinNaturalWeek);
            tv_inputTotalCoinNaturalWeek = itemView.findViewById(R.id.tv_inputTotalCoinNaturalWeek);
            tv_inputVideoCoinLastWeek = itemView.findViewById(R.id.tv_inputVideoCoinLastWeek);
            tv_inputAudioCoinLastWeek = itemView.findViewById(R.id.tv_inputAudioCoinLastWeek);
            tv_inputGiftCoinLastWeek = itemView.findViewById(R.id.tv_inputGiftCoinLastWeek);
            tv_inputTotalCoinLastWeek = itemView.findViewById(R.id.tv_inputTotalCoinLastWeek);*/



          /*  description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);*/
            date = itemView.findViewById(R.id.date);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            buttonLayout = itemView.findViewById(R.id.button);
            llContainer = itemView.findViewById(R.id.llContainer);

        }
    }

    public static String getHashMapKeyFromIndex(NavigableMap<String, List<WalletHistoryData>> hashMap, int index) {

        String key = null;
        NavigableMap<String, List<WalletHistoryData>> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, List<WalletHistoryData>> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
            }
            pos++;
        }
        return key;
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }
}