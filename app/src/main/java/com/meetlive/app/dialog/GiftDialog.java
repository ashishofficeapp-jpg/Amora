package com.meetlive.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meetlive.app.R;
import com.meetlive.app.activity.InboxDetails;
import com.meetlive.app.adapter.GiftAdapter;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.gift.Gift;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.response.gift.SendGiftRequest;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.RecyclerTouchListener;
import com.meetlive.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class GiftDialog extends Dialog implements ApiResponseInterface {

    InboxDetails context;
    RecyclerView recyclerView;
    String receiverUserId;
    private ApiManager apiManager;
    List<Gift> giftList = new ArrayList<>();
    ImageView loader;
    int currentCoin = 0;

    public GiftDialog(@NonNull InboxDetails context, String receiverUserId) {
        super(context);
        this.context = context;
        this.receiverUserId = receiverUserId;
        init();
    }

    void init() {
        this.setContentView(R.layout.gift_dialog);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        loader = findViewById(R.id.img_loader);
        Glide.with(getContext()).load(R.drawable.loader).into(loader);
        loader.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.gift_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false));

       /* SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);*/

      /* DividerItemDecoration itemDecorationVertical = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecorationHorizontal = new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL);
        itemDecorationVertical.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_decorator_line));
        itemDecorationHorizontal.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_decorator_line));
        recyclerView.addItemDecoration(itemDecorationVertical);
        recyclerView.addItemDecoration(itemDecorationHorizontal);*/

        apiManager = new ApiManager(context, this);
        if (!new SessionManager(context).getGender().equals("female")) {
            apiManager.getWalletAmount2();
        }

        apiManager.getGiftList();

        giftRecyclerviewClickListener();

        this.show();
    }

  /*  void data() {
        giftList = new ArrayList<>();


        //   giftList = sessionManager.getGiftList();

        for (int i = 0; i < 20; i++) {

            Gift gift = new Gift();
            gift.setAmount(20);
            gift.setGiftPhoto("https://zeep.live/public/gift_photo/1606228346.png");
            gift.setId(18);
            gift.setStatus(1);
            giftList.add(gift);

            Gift gift2 = new Gift();
            gift2.setAmount(20);
            gift2.setGiftPhoto("https://zeep.live/public/gift_photo/1606227404.png");
            gift2.setId(21);
            gift2.setStatus(1);
            giftList.add(gift2);
        }

        recyclerView.setAdapter(new GiftAdapter(giftList, R.layout.rv_gift, context));
    }*/

    @Override
    public void isError(String errorCode) {
        loader.setVisibility(View.GONE);
        Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.WALLET_AMOUNT2) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            currentCoin = rsp.getResult().getTotal_point();
        }
        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult giftResponse = (SendGiftResult) response;

            dismiss();
        }

        if (ServiceCode == Constant.SEND_GIFT) {
            SendGiftResult rsp = (SendGiftResult) response;
           currentCoin=rsp.getResult();
        }

        if (ServiceCode == Constant.GET_GIFT_LIST) {
            loader.setVisibility(View.GONE);
            ResultGift giftResponse = (ResultGift) response;

            if (giftResponse.getResult() != null) {
                giftList = giftResponse.getResult();
                recyclerView.setAdapter(new GiftAdapter(giftList, R.layout.rv_gift, context));
            } else {
                Toast.makeText(context, "No Gift available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void giftRecyclerviewClickListener() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                // If Gift item clicked by female user gift request will be send
                if (new SessionManager(context).getGender().equals("female")) {
                    //hide code gift layout not open if user is female 9/04/21
                   /* context.sendMessage("gift_request", String.valueOf(giftList.get(position).getId())
                            , String.valueOf(giftList.get(position).getAmount()));
                    dismiss();*/

                    Toast.makeText(context, "You can send gift request only on during call.", Toast.LENGTH_SHORT).show();
                } else {
                    // If Gift item clicked by male user gift item will be send
                    if (currentCoin > giftList.get(position).getAmount()) {
                       /* apiManager.sendUserGift(new SendGiftRequest(Integer.valueOf(receiverUserId),
                                giftList.get(position).getId(), giftList.get(position).getAmount()));*/
                        //4/5/21 pass here unique_id for gift send
                        apiManager.sendUserGift(new SendGiftRequest(Integer.parseInt(receiverUserId),"",
                                giftList.get(position).getId(), giftList.get(position).getAmount(), String.valueOf(System.currentTimeMillis()),
                                String.valueOf(System.currentTimeMillis())));

                        context.sendMessage("gift", String.valueOf(giftList.get(position).getId())
                                , String.valueOf(giftList.get(position).getAmount()));
                    } else {
                        Toast.makeText(context, "Out of coin", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
}