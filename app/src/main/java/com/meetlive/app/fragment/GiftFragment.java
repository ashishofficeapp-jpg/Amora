package com.meetlive.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meetlive.app.R;
import com.meetlive.app.activity.InboxDetails;
import com.meetlive.app.adapter.GiftAdapterNew;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.gift.Gift;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.RecyclerTouchListener;
import com.meetlive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class GiftFragment extends Fragment implements ApiResponseInterface {
    public static final String ARG_POSITION = "position";
    public static final String ARG_GIFT_LIST = "giftList";
    private RecyclerView recyclerGift;
    private GiftAdapterNew giftAdapter;
    private List<Gift> giftImageModelArrayList;
    private List<Gift> giftImageList;
    private int pagerFragPos;
    int giftPerFragment = 8;
    private ApiManager apiManager;
    int currentCoin = 0;
    String receiverUserId;
    InboxDetails context;
    ImageView loader;
    public static int positions;
    String bt_value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        apiManager = new ApiManager(getContext(), this);
        View view = inflater.inflate(R.layout.fragment_gift, container, false);

        apiManager = new ApiManager(getContext(), this);
        if (!new SessionManager(getContext()).getGender().equals("female")) {
            apiManager.getWalletAmount2();
        }

        apiManager.getGiftList();
        getContext().registerReceiver(myButtonReceiver, new IntentFilter("FBR-USER-INPUT"));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) return;
        pagerFragPos = args.getInt(ARG_POSITION);

       /* if(getActivity() instanceof InboxDetails) {
            receiverUserId = ((InboxDetails) getActivity()).receiverUserId;
        }*/

        //giftImageModelArrayList = args.getParcelableArrayList(ARG_GIFT_LIST);
        //giftImageModelArrayList = args.getParcelable(ARG_GIFT_LIST);
        recyclerGift = view.findViewById(R.id.recyclerGift);

        loader = view.findViewById(R.id.img_loader);
        Glide.with(getContext()).load(R.drawable.loader).into(loader);
        loader.setVisibility(View.VISIBLE);
        giftRecyclerviewClickListener();
        //TextView countTV = view.findViewById(R.id.countTV);
        //countTV.setText((pagerFragPos+1)+"");

    }


    @Override
    public void isError(String errorCode) {
        loader.setVisibility(View.GONE);
        Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_GIFT_LIST) {
            loader.setVisibility(View.GONE);
            ResultGift giftResponse = (ResultGift) response;
            if (giftResponse.getResult() != null) {
                giftImageList = new ArrayList<>();
                giftImageModelArrayList = giftResponse.getResult();
                Log.e("giftValue", String.valueOf(giftImageModelArrayList.size()));

                int startIndex = pagerFragPos * giftPerFragment;
                for (int i = startIndex; i < giftImageModelArrayList.size(); i++) {
                    giftImageList.add(giftImageModelArrayList.get(i));
                    if (i == (startIndex + giftPerFragment - 1)) break;
                }
                giftAdapter = new GiftAdapterNew(getActivity(), giftImageList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
                recyclerGift.setLayoutManager(gridLayoutManager);
                recyclerGift.setAdapter(giftAdapter);
                giftAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "No Gift available", Toast.LENGTH_SHORT).show();
            }
        }
        if (ServiceCode == Constant.WALLET_AMOUNT2) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            currentCoin = rsp.getResult().getTotal_point();
        }

    }

    void giftRecyclerviewClickListener() {
        recyclerGift.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerGift, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                positions = position;
                Log.e("posInGf", String.valueOf(positions));

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myButtonReceiver);
    }

    public BroadcastReceiver myButtonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("reach", "working adapter:==="+intent.getStringExtra("value"));
            bt_value = intent.getStringExtra("value");
            giftAdapter.notifyDataSetChanged();

        }
    };

}
