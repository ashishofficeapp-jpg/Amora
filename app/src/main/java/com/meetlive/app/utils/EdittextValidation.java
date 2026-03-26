package com.meetlive.app.utils;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class EdittextValidation {

    Activity activity;

    public EdittextValidation(Activity activity) {
        this.activity = activity;
    }

    public boolean validateGenericEdittext(EditText editText, String msg, int auth_length) {
        if (editText.getText().toString().trim().length() < auth_length) {
            editText.setError(msg);
          //  editText.setBackground(activity.getDrawable(R.drawable.edit_error_drawable));
            requestFocus(editText);
            return false;
        }
      //  editText.setBackground(activity.getDrawable(R.drawable.edit_valid_drawable));
        return true;
    }

    public boolean validateGenericTextview(TextView textView, String msg, int auth_length) {
        if (textView.getText().toString().trim().length() < auth_length) {
            textView.setError(msg);
       //     textView.setBackground(activity.getDrawable(R.drawable.edit_error_drawable));
            requestFocus(textView);
            return false;
        }
      //  textView.setBackground(activity.getDrawable(R.drawable.edit_valid_drawable));
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}