package com.meetlive.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.meetlive.app.R;
import com.meetlive.app.databinding.DialogReportBinding;
import com.meetlive.app.response.ReportResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

public class ReportDialog extends Dialog {

    DialogReportBinding binding;
    String id;

    public ReportDialog(@NonNull Context context, String id) {
        super(context);
        this.id = id;

        init();
    }

    void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_report, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        show();

        binding.setClickListener(new EventHandler(getContext()));
    }

    public class EventHandler implements ApiResponseInterface {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void reportUser(int isBlock) {

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(binding.option.getCheckedRadioButtonId());
            new ApiManager(getContext(), this).reportUser(id, String.valueOf(isBlock),
                    radioButton.getText().toString(), binding.reportReason.getText().toString());
        }


        @Override
        public void isError(String errorCode) {
            Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void isSuccess(Object response, int ServiceCode) {
            if (ServiceCode == Constant.REPORT_USER) {

                ReportResponse reportResponse = (ReportResponse) response;
                if (reportResponse.getResult() != null && reportResponse.getResult().length() > 0) {
                    dismiss();
                    Toast.makeText(mContext, "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}