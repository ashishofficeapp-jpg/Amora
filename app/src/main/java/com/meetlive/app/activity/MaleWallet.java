package com.meetlive.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.meetlive.app.R;
import com.meetlive.app.adapter.TransactionAdapter;
import com.meetlive.app.databinding.ActivityMaleWalletBinding;
import com.meetlive.app.response.NewWallet.WalletHistoryData;
import com.meetlive.app.response.NewWallet.WalletResponce;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.List;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaleWallet extends AppCompatActivity implements ApiResponseInterface {

    TransactionAdapter adapter;
    ActivityMaleWalletBinding binding;
    ApiManager apiManager;
    private int TOTAL_PAGES;
    TreeMap<String, List<WalletHistoryData>> walletHistory = new TreeMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_male_wallet);

        binding.heading.setText("Wallet");
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.setClickListener(new EventHandler(this));

        apiManager = new ApiManager(this, this);
        apiManager.getWalletAmount();
        apiManager.getTransactionHistory();
    }

    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void closeActivity() {
            finish();
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.WALLET_AMOUNT) {
            WalletBalResponse rsp = (WalletBalResponse) response;
            binding.availableCoins.setText(String.valueOf(rsp.getResult().getTotal_point()));

        } else if (ServiceCode == Constant.TRANSACTION_HISTORY) {
            WalletResponce rsp = (WalletResponce) response;
            walletHistory = rsp.getResult().getWalletHistory();
            TOTAL_PAGES = ((WalletResponce) response).getResult().getLastPage();

            adapter = new TransactionAdapter(this, walletHistory.descendingMap());
            binding.transactionList.setAdapter(adapter);
        }
    }
}