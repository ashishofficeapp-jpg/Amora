package com.amora.app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amora.app.databinding.ActivitySocialLoginBinding;
import com.amora.app.utils.HideStatus;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.amora.app.GetAppVersion;
import com.amora.app.R;
import com.amora.app.response.LoginResponse;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.ErrorDialog;
import com.amora.app.utils.SessionManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class SocialLogin extends AppCompatActivity implements ApiResponseInterface {

    ActivitySocialLoginBinding binding;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    ErrorDialog errorDialog;
    boolean doubleBackToExitPressedOnce = false;

    ImageView fb_btn, gmail_btn;
    CallbackManager callbackManager;
    public static final int REQ_CODE = 9001;
    public static final int FACEBOOK_REQ_CODE = 64206;

    GoogleSignInClient googleApiClient;
    ApiManager apiManager;
    public static String currentVersion;
    SessionManager sessionManager;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_login);
        binding.setClickListener(new EventHandler(this));

        // Initialize UI buttons
        fb_btn = binding.fbBtn;
        gmail_btn = binding.gmailBtn;
        callbackManager = CallbackManager.Factory.create();

        // Get App Version
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("version", "appVersion" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetAppVersion(this).execute();

        // API Manager
        apiManager = new ApiManager(this, this);
        errorDialog = new ErrorDialog(this, "Please check your internet connection");

        // Google Sign-In setup
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        googleApiClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Location permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            autoCountrySelect();
        } else {
            getPermission();
        }
    }

    private void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            enableLocationSettings();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(SocialLogin.this, "Permission denied permanently", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void autoCountrySelect() {
        sessionManager = new SessionManager(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());

        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType")
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String city_name = addresses.get(0).getLocality();
                        Log.e("cityname", city_name);
                        sessionManager.setUserLocation(addresses.get(0).getCountryName());
                        sessionManager.setUserAddress(city_name);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> autoCountrySelect())
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(SocialLogin.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ignored) {}
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            googleApiClient.signOut();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode && resultCode == Activity.RESULT_OK) {
            autoCountrySelect();
        }

        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                apiManager.login_FbGoogle(account.getDisplayName(), "google", account.getId(), account.getEmail());
                new SessionManager(this).setUserFacebookName(account.getDisplayName());
                Log.e("email", account.getEmail());
            }
        } catch (ApiException e) {
            Log.e("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /* Facebook Graph API */
    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    try {
                        String id = object.getString("id");
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        new SessionManager(this).setUserFacebookName(first_name + " " + last_name);

                        String email = object.has("email") ? object.getString("email") : "";
                        Log.e("emailId", email);
                        apiManager.login_FbGoogle(first_name, "facebook", id, email);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void checkInternetConnection() {
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
    }

    /* Event Handler for DataBinding */
    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void onGuestLogin() {
            @SuppressLint("HardwareIds")
            //String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceId = "984036547822";
            //Log.e("Device_id", deviceId);
            apiManager.guestRegister("guest", deviceId);
        }

        public void onUserLogin() {
            mContext.startActivity(new Intent(mContext, Login.class));
        }

        public void onFacebookClick() {
            checkInternetConnection();
            if (activeNetwork != null) {
                LoginManager.getInstance().logInWithReadPermissions(
                        (Activity) mContext,
                        java.util.Arrays.asList("public_profile", "email")
                );

                LoginManager.getInstance().registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                getFbInfo();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(mContext, "Facebook login cancelled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(mContext, "Facebook login error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } else {
                errorDialog.show();
            }
        }

        public void onGoogleClick() {
            checkInternetConnection();
            if (activeNetwork != null) {
                Intent signInIntent = googleApiClient.getSignInIntent();
                ((Activity) mContext).startActivityForResult(signInIntent, REQ_CODE);
            } else {
                errorDialog.show();
            }
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        String c_name = new SessionManager(getApplicationContext()).getUserLocation();

        if (ServiceCode == Constant.REGISTER || ServiceCode == Constant.LOGIN) {
            LoginResponse rsp = (LoginResponse) response;
            new SessionManager(this).createLoginSession(rsp);
            if (c_name.equals("null") && rsp.getResult().getAllow_in_app_purchase() != 0) {
                startActivity(new Intent(this, LocationSelection.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
            }
        }

        if (ServiceCode == Constant.GUEST_REGISTER) {
            LoginResponse rsp = (LoginResponse) response;
            if (rsp.getResult() != null && rsp.getResult().getUsername() != null && rsp.getResult().getDemo_password() != null) {
                new SessionManager(this).saveGuestStatus(rsp.getResult().getGuest_status());
                new SessionManager(this).saveGuestPassword(rsp.getResult().getDemo_password());
                apiManager.login(rsp.getResult().getUsername(), rsp.getResult().getDemo_password());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "You Want Close App", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}