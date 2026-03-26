package com.amora.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.amora.app.R;
import com.amora.app.activity.EditProfile;
import com.amora.app.activity.LevelUpActivity;
import com.amora.app.activity.MainActivity;
import com.amora.app.activity.MaleWallet;
import com.amora.app.activity.SettingActivity;
import com.amora.app.databinding.FragmentMyAccountBinding;
import com.amora.app.dialog.InsufficientCoins;
import com.amora.app.response.ProfileDetailsResponse;
import com.amora.app.response.WalletBalResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment implements ApiResponseInterface {
    ApiManager apiManager;
    FragmentMyAccountBinding binding;
    String username = "";
    SessionManager sessionManager;
    String guestPassword;
    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());
        guestPassword = sessionManager.getGuestPassword();
        binding.setClickListener(new EventHandler(getContext()));
        apiManager = new ApiManager(getContext(), this);
        apiManager.getWalletAmount();

        binding.rlFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showFollowers();
            }
        });
    }



    public class EventHandler {
        Context mContext;
        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void editProfile() {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
        }

        public void purchaseCoins() {
            new InsufficientCoins(getActivity(), 2, Integer.parseInt(binding.availableCoins.getText().toString()));
        }

        public void maleWallet() {
            Intent my_wallet = new Intent(getActivity(), MaleWallet.class);
            startActivity(my_wallet);
        }

        public void setting() {
           Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }

        public void upGraded() {
            // show upgraded dialog here
            Intent intent = new Intent(getActivity(), LevelUpActivity.class);
            startActivity(intent);
            // new UpGradedLevelDialog(getContext());
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        apiManager.getProfileDetails();
        if (new SessionManager(getContext()).getUserAddress().equals("null")) {
            binding.userLocation.setVisibility(View.GONE);
        } else {
            if (binding.userLocation.getVisibility() == View.GONE) {
                binding.userLocation.setVisibility(View.VISIBLE);
            }
            binding.userLocation.setText(new SessionManager(getContext()).getUserAddress());
        }
    }

    String uid, nme, image;

    @Override
    public void isError(String errorCode) {

    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {

        try {
            if (ServiceCode == Constant.WALLET_AMOUNT) {
                WalletBalResponse rsp = (WalletBalResponse) response;
                binding.availableCoins.setText(String.valueOf(rsp.getResult().getTotal_point()));

            }
            if (ServiceCode == Constant.PROFILE_DETAILS) {
                ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;

                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    try {
                        if (!rsp.getSuccess().getProfile_images().get(0).getImage_name().equals("")) {
                            Glide.with(getContext()).load(rsp.getSuccess().getProfile_images().get(0).getImage_name())
                                    .circleCrop().placeholder(R.drawable.default_profile).into(binding.userImage);
                        }

                        String img = "";
                        if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                            img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                        }
                        uid = String.valueOf(rsp.getSuccess().getProfile_id());
                        nme = rsp.getSuccess().getName();
                        this.image = img;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                binding.followers.setText(String.valueOf(rsp.getSuccess().getFavorite_count()));
                binding.name.setText(rsp.getSuccess().getName());
                binding.userId.setText("ID : " + rsp.getSuccess().getProfile_id());
                markOnlineStatus(rsp.getSuccess().getIs_online());

                if (rsp.getSuccess().getLogin_type().equals("manualy") && new SessionManager(getContext()).getGender().equals("male")) {
                    //binding.changePassword.setVisibility(View.VISIBLE);
                    //binding.passwordSeprator.setVisibility(View.VISIBLE);
                }

                if (rsp.getSuccess().getUsername().startsWith("guest")) {
                    //binding.changePassword.setVisibility(View.GONE);
                    // binding.accountInfo.setVisibility(View.VISIBLE);
                    username = rsp.getSuccess().getUsername();
                }
                apiManager.getWalletAmount();
            }
        } catch (Exception e) {
        }
    }

    public void markOnlineStatus(int is_online) {
        //onlineStatus = is_online;
        if (is_online == 1) {
           // binding.status.setText("Online");
        } else {
           // binding.status.setText("Offline");
        }
    }


}