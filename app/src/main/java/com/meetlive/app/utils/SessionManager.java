package com.meetlive.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meetlive.app.activity.MainActivity;
import com.meetlive.app.activity.SocialLogin;
import com.meetlive.app.response.EndCallData.EndCallData;
import com.meetlive.app.response.LoginResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "Meet_live";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String PROFILE_ID = "profile_id";
    public static final String TOKEN_ID = "token_id";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String IS_ONLINE = "is_online";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String PIC_BASE_URL = "pic_base_url";
    public static final String ACCOUNT_VERIFIED = "account_verified";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String CURRENT_RECEIVER = "current_receiver";
    public static final String LOGIN_TYPE = "login_type";
    public static final String IS_CONSENT_SEEN = "is_consent_seen";
    public static final String GUEST_PASSWORD = "guest_password";
    public static final String LANG_STATE = "language_State";
    public static final String FASTMODE_STATE = "fastmode_State";
    public static final String ONLINE_STATE = "online_State";
    public static final String ONLINE_STATEBACK = "online_Stateback";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_Email = "user_Email";
    public static final String USER_Password = "user_Password";
    public static final String RECENT_CHAT_LIST_UPDATE = "recent_chat_list_update";
    public static final String USER_FACEBOOK_NAME = "user_facebook_name";
    public static final String USER_WALLET = "user_Wall";
    public static final String IS_GUEST_STATUS = "is_guest_status_data";
    public static final String USER_STRIPEK = "user_Stripek";
    public static final String USER_STRIPES = "user_Stripes";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_RAZK = "user_Razk";
    public static final String USER_RAZS = "user_Razs";

    public static final String USER_ENDCALLDATA = "user_EndCallData";
    public static final String USER_GETENDCALLDATA = "user_GETEndCallData";
    public static final String USER_LOADDATA = "userdataforloading";
    public static final String USER_ASKPERMISSION = "user_askpermission";

    // Constructor
    public SessionManager(Context context) {
        try {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
        }
    }

    /**
     * Create login session
     */

    public void createLoginSession(LoginResponse result) {
        //Log.e("inogin", new Gson().toJson(result));
        //Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(TOKEN_ID, result.getResult().getToken());
        editor.putString(NAME, result.getResult().getName());
        editor.putInt(IS_ONLINE, result.getResult().getIs_online());
        editor.putString(GENDER, result.getResult().getGender());
        editor.putString(PROFILE_ID, result.getResult().getProfile_id());
        // commit changes
        editor.commit();
    }


    public void saveGuestPassword(String password) {
        editor.putString(GUEST_PASSWORD, password);
        editor.commit();
    }

    public String getGuestPassword() {
        return pref.getString(GUEST_PASSWORD, null);
    }

    public void saveGuestStatus(int id) {
        editor.putInt(IS_GUEST_STATUS, id);
        editor.apply();
    }

    public int getGuestStatus() {
        return pref.getInt(IS_GUEST_STATUS, 0);
    }


    public String getUserToken() {
        return pref.getString(TOKEN_ID, null);
    }

    public int isOnline() {
        return pref.getInt(IS_ONLINE, 0);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {

            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        } else {
            Intent i = new Intent(_context, SocialLogin.class);
            // Staring Login Activity
            //send data socialLogin page for
            i.putExtra("Splashpage", "fromSplash");
            _context.startActivity(i);
        }
    }

    public void saveFcmToken(String token) {
        editor.putString(FCM_TOKEN, token);
        editor.commit();
    }

    public String getFcmToken() {
        return pref.getString(FCM_TOKEN, null);
    }


    public void setUserFacebookName(String f_name) {
        editor.putString(USER_FACEBOOK_NAME, f_name);
        editor.apply();
    }

    public String getUserFacebookName() {
        return pref.getString(USER_FACEBOOK_NAME, "null");
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(TOKEN_ID, pref.getString(TOKEN_ID, null));
        user.put(NAME, pref.getString(NAME, null));
        user.put(GENDER, pref.getString(GENDER, null));
        user.put(PROFILE_ID, pref.getString(PROFILE_ID, null));
        // return user
        return user;
    }


    public String getGender() {
        return pref.getString(GENDER, null);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Logout from facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
        // Logout from Google
        signoutFromGoogle();
        //Clear all data from Shared Preferences
        editor.clear();
        editor.commit();
        resetFcmToken();
        //After logout redirect user to Loing Activity
        Intent i = new Intent(_context, SocialLogin.class);
        //Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public void resetFcmToken() {
        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void signoutFromGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(_context, gso);
        googleSignInClient.signOut();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean getConsent() {
        return pref.getBoolean(IS_CONSENT_SEEN, false);
    }

    public void setConsent(boolean status) {
        editor.putBoolean(IS_CONSENT_SEEN, status);
        editor.apply();
    }


    public void saveCurrentReceiver(String id) {
        editor.putString(CURRENT_RECEIVER, id);
        editor.apply();
    }

    public String getCurrentReceiver() {
        return pref.getString(CURRENT_RECEIVER, "none");
    }

    public void setLangState(int id) {
        editor.putInt(LANG_STATE, id);
        editor.apply();
    }

    public int gettLangState() {
        return pref.getInt(LANG_STATE, 0);
    }

    public void setFastModeState(int id) {
        editor.putInt(FASTMODE_STATE, id);
        editor.apply();
    }

    public int getFastModeState() {
        return pref.getInt(FASTMODE_STATE, 0);
    }

    public void setOnlineState(int id) {
        editor.putInt(ONLINE_STATE, id);
        editor.apply();
    }

    public int getOnlineState() {
        return pref.getInt(ONLINE_STATE, 0);
    }

    public String getUserId() {
        return pref.getString(PROFILE_ID, "");
    }

    public String getUserName() {
        return pref.getString(NAME, "");
    }


    public void setUserProfilepic(String profileUrl) {
        try {
            editor.putString(PROFILE_PIC, profileUrl);
            editor.apply();
        } catch (Exception E) {
        }
    }

    public String getUserProfilepic() {
        return pref.getString(PROFILE_PIC, "");
    }

    public void setOnlineFromBack(int id) {
        editor.putInt(ONLINE_STATEBACK, id);
        editor.apply();
    }

    public int getOnlineFromBack() {
        return pref.getInt(ONLINE_STATEBACK, 0);
    }

    public void setUserLocation(String c_name) {
        editor.putString(USER_LOCATION, c_name);
        editor.apply();
    }


    public String getUserLocation() {
        return pref.getString(USER_LOCATION, "null");
    }

    public void setUserEmail(String c_name) {
        editor.putString(USER_Email, c_name);
        editor.apply();
    }


    public void setUserAddress(String address) {
        editor.putString(USER_ADDRESS, address);
        editor.apply();
    }

    public String getUserAddress() {
        return pref.getString(USER_ADDRESS, "null");
    }

    public String getUserEmail() {
        return pref.getString(USER_Email, "null");
    }

    public void setUserPassword(String c_name) {
        editor.putString(USER_Password, c_name);
        editor.apply();
    }

    public void isRecentChatListUpdateNeeded(boolean status) {
        editor.putBoolean(RECENT_CHAT_LIST_UPDATE, status);
        editor.apply();
    }

    public boolean getRecentChatListUpdateStatus() {
        return pref.getBoolean(RECENT_CHAT_LIST_UPDATE, false);
    }

    public String getUserPassword() {
        return pref.getString(USER_Password, "null");
    }

    public void setUserWall(int u_wall) {
        editor.putInt(USER_WALLET, u_wall);
        editor.apply();

        Log.e("MaleWalletAmount", String.valueOf(u_wall));
    }

    public int getUserWallet() {
        return pref.getInt(USER_WALLET, 0);
    }

    public void setUserStriepKS(String u_StripeK, String u_StripeS) {
        editor.putString(USER_STRIPEK, u_StripeK);
        editor.putString(USER_STRIPES, u_StripeS);
        editor.apply();
    }

    public void setUserRazKS(String u_RAZK, String u_RAZS) {
        editor.putString(USER_RAZK, u_RAZK);
        editor.putString(USER_RAZS, u_RAZS);
        editor.apply();
    }

    public String getUserStriepK() {
        return pref.getString(USER_STRIPEK, "null");
    }

    public String getUserStriepS() {
        return pref.getString(USER_STRIPES, "null");
    }

    public String getUserRazK() {
        return pref.getString(USER_RAZK, "null");
    }

    public String getUserRazs() {
        return pref.getString(USER_RAZS, "null");
    }


    //end call
    public void setUserEndcalldata(ArrayList<EndCallData> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(USER_ENDCALLDATA, json);
        editor.apply();
    }

    public ArrayList<EndCallData> getUserEndcalldata() {
        Gson gson = new Gson();
        String json = pref.getString(USER_ENDCALLDATA, null);
        Type type = new TypeToken<ArrayList<EndCallData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void setUserGetendcalldata(String U_GETENDCALLDATA) {
        editor.putString(USER_GETENDCALLDATA, U_GETENDCALLDATA);
        editor.apply();
    }

    public String getUserGetendcalldata() {
        return pref.getString(USER_GETENDCALLDATA, "null");
    }


    public void setUserLoaddata() {
        editor.putString(USER_LOADDATA, "yes");
        editor.apply();
    }

    public void setUserLoaddataNo() {
        editor.putString(USER_LOADDATA, "no");
        editor.apply();
    }


    public String getUserLoaddata() {
        return pref.getString(USER_LOADDATA, "null");
    }

    public void setUserAskpermission() {
        editor.putString(USER_ASKPERMISSION, "no");
        editor.apply();
    }

    public String getUserAskpermission() {
        return pref.getString(USER_ASKPERMISSION, "null");
    }

}