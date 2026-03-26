package com.amora.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.amora.app.R;

import com.amora.app.databinding.ActivitySettingBinding;
import com.amora.app.dialog.AccountInfoDialogSetting;
import com.amora.app.response.ProfileDetailsResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.SessionManager;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;


public class SettingActivity extends AppCompatActivity implements ApiResponseInterface {
    ActivitySettingBinding binding;
    String username = "";
    String guestPassword;
    SessionManager sessionManager;
    ApiManager apiManager;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        //setContentView(R.layout.activity_setting);
        binding.settingBack.setOnClickListener(view -> onBackPressed());
        sessionManager = new SessionManager(this);
        guestPassword = sessionManager.getGuestPassword();
        apiManager = new ApiManager(this, this);
        apiManager.getProfileDetails();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Users");

        binding.setClickListener(new EventHandler(this));

        initializeCache();
    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }


        public void aboutUs() {
            //new ComplaintDialog(getContext());
        }

        public void viewTicket() {
            //Intent my_wallet = new Intent(getActivity(), ViewTicketActivity.class);
            // startActivity(my_wallet);
        }

        public void privacyPolicy() {
            Intent intent = new Intent(SettingActivity.this, PrivacyPolicy.class);
            startActivity(intent);
        }

        public void changePassword() {
            //new ChangePasswordDialog(getContext());
        }


        public void accountInfo() {

            new AccountInfoDialogSetting(mContext, username, guestPassword);
        }

        public void logout() {
            // new ExitDialog(SettingActivity.this);
            logoutDialog();
        }

        public void delete() {
            accountDeleteDialog();
        }

        public void clear_cache() {
            Log.e("meHereCache", "hey");
            try {
                trimCache(SettingActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }

    private void initializeCache() {
        long size = 0;
        size += getDirSize(this.getCacheDir());
        size += getDirSize(this.getExternalCacheDir());
        ((TextView) findViewById(R.id.tv_cache_data)).setText(readableFileSize(size));
    }

    public long getDirSize(File dir){
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    void logoutDialog() {
        Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView logout = dialog.findViewById(R.id.logout);
        closeDialog.setOnClickListener(view -> dialog.dismiss());
        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String cName = new SessionManager(SettingActivity.this).getUserLocation();
            String eMail = new SessionManager(SettingActivity.this).getUserEmail();
            String passWord = new SessionManager(SettingActivity.this).getUserPassword();
            new SessionManager(SettingActivity.this).logoutUser();
            apiManager.getUserLogout();
            checkOnlineAvailability(uid, nme, image);
            new SessionManager(SettingActivity.this).setUserLocation(cName);
            new SessionManager(SettingActivity.this).setUserEmail(eMail);
            new SessionManager(SettingActivity.this).setUserPassword(passWord);
            new SessionManager(SettingActivity.this).setUserAskpermission();
            finishAffinity();
            //finish();
        });
    }

    void accountDeleteDialog() {
        Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);
        tv_msg.setText("After deleting account, the balance and earning will be unable to use. your personal information will be removed. your account will be removed permanently and unable to be recovered again. Please consider carefully!");
        tv_msg.setGravity(Gravity.START);
        logout.setText("OK");
        closeDialog.setText("Cancel");
        tv_msg.setTextColor(SettingActivity.this.getResources().getColor(R.color.black));

        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setOnClickListener(view -> {
            dialog.dismiss();
            String cName = new SessionManager(getApplicationContext()).getUserLocation();
            String eMail = new SessionManager(getApplicationContext()).getUserEmail();
            String passWord = new SessionManager(getApplicationContext()).getUserPassword();
            new SessionManager(getApplicationContext()).logoutUser();
            apiManager.getAccountDelete();
            checkOnlineAvailability(uid, nme, image);
            new SessionManager(getApplicationContext()).setUserLocation(cName);
            new SessionManager(getApplicationContext()).setUserEmail(eMail);
            new SessionManager(getApplicationContext()).setUserPassword(passWord);
            new SessionManager(getApplicationContext()).setUserAskpermission();
            //sessionManager.setUserLocation("null");
            finish();
        });
    }

    String uid, nme, image;

    void checkOnlineAvailability(String uid, String name, String image) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // Change online status when user comes back on app
                    try {
                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("image", image);
                        details.put("status", "Offline");
                        chatRef.child(uid).setValue(details);

                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fireeBase", "Listener was cancelled");
            }
        });
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(SettingActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
        try {
            if (rsp.getSuccess().getLogin_type().equals("manualy") && new SessionManager(getApplicationContext()).getGender().equals("male")) {
                binding.changePassword.setVisibility(View.VISIBLE);

            }
            if (rsp.getSuccess().getUsername().startsWith("guest")) {
                binding.changePassword.setVisibility(View.GONE);
                binding.accountInfo.setVisibility(View.VISIBLE);
                username = rsp.getSuccess().getUsername();

            }
        } catch (Exception e) {

        }
    }


}