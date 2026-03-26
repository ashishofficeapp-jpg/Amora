package com.meetlive.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.meetlive.app.R;
import com.meetlive.app.activity.SelectPaymentMethod;
import com.meetlive.app.utils.SessionManager;

public class PaymentCompletedDialog extends Dialog {

    int amount;
    String transaction_id;
    SelectPaymentMethod context;

    public PaymentCompletedDialog(SelectPaymentMethod context, String transactionId, int amount) {
        super(context);

        this.context = context;
        this.transaction_id = transactionId;
        this.amount = amount;
        init();
    }

    void init() {
        this.setContentView(R.layout.dialog_payment_successfull);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);


        TextView paidAmount = findViewById(R.id.payment_amount);
        TextView transactionId = findViewById(R.id.transaction_id);
        if (new SessionManager(getContext()).getUserLocation().equals("India")) {
            paidAmount.setText("Your payment of ₹" + amount + " was successfully completed");
        } else {
            paidAmount.setText("Your payment of $" + amount + " was successfully completed");
        }
        transactionId.setText(transaction_id);


        Button btn_done = findViewById(R.id.done_btn);
        btn_done.setOnClickListener(view -> {
            //  new UpGradedLevelDialog(context);
            dismiss();
            context.finish();

        });

        show();
    }
}