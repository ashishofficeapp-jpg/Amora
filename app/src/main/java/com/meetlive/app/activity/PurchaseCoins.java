package com.meetlive.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meetlive.app.R;
import com.meetlive.app.adapter.CoinPlansAdapter;
import com.meetlive.app.response.RechargePlanResponse;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class PurchaseCoins extends AppCompatActivity implements ApiResponseInterface {

    ApiManager apiManager;
    RecyclerView plan_list;
    CoinPlansAdapter coinPlansAdapter;
    List<RechargePlanResponse.Data> activePlans;
    private Button payButton;
    TextView available_coins;
    int selectedPlanPosition = 0;
    String upiId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_recharge_coinns);

        // setToolbarTitle("Recharge Plans");
        plan_list = findViewById(R.id.plan_list);
        available_coins = findViewById(R.id.available_coins);
        plan_list.setLayoutManager(new GridLayoutManager(this, 3));
        payButton = findViewById(R.id.button_pay);

        apiManager = new ApiManager(this, this);
        apiManager.getRechargeList();
//        apiManager.getWalletAmount();

        payButton.setOnClickListener(v -> {

            if (!upiId.isEmpty()) {
                Intent intent = new Intent(PurchaseCoins.this, SelectPaymentMethod.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_plan", activePlans.get(selectedPlanPosition));
                intent.putExtra("upi_id", upiId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.tv_mail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getSendEmailIntent(PurchaseCoins.this, "zeepliveofficial@gmail.com", "Coin Topup", "Write your quries here"));
            }
        });

        ((LinearLayout) findViewById(R.id.ll_mail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getSendEmailIntent(PurchaseCoins.this, "zeepliveofficial@gmail.com", "Coin Topup", "Write your quries here"));
            }
        });

    }

    public Intent getSendEmailIntent(Context context, String email,
                                     String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        return intent;
    }

    public void selectedPlan(int position) {
        selectedPlanPosition = position;
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiManager.getWalletAmount();
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.RECHARGE_LIST) {
            RechargePlanResponse rsp = (RechargePlanResponse) response;

            upiId = rsp.getResult().getUpi_id();
            List<RechargePlanResponse.Data> planList = rsp.getResult().getData();
            activePlans = new ArrayList<>();

            if (planList != null && planList.size() > 0) {
                for (int i = 0; i < planList.size(); i++) {
                    if (planList.get(i).getStatus() == 1) {
                        activePlans.add(planList.get(i));
                    }
                }

                coinPlansAdapter = new CoinPlansAdapter(this, activePlans, "activity");
                plan_list.setAdapter(coinPlansAdapter);
            }

        } else if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            available_coins.setText(String.valueOf(rsp.getResult().getTotal_point()));
        }
    }
}