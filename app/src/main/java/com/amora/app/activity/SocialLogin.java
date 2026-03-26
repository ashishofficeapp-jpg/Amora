package com.amora.app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

public class SocialLogin extends AppCompatActivity implements ApiResponseInterface, View.OnClickListener {

    private RelativeLayout login_layout;
    TextView guest_login;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    ErrorDialog errorDialog;
    boolean doubleBackToExitPressedOnce = false;
    /*---- Facebook variables ----*/
    LoginButton loginButton;
    ImageView fb_btn, gmail_btn;
    CallbackManager callbackManager;
    public static final int REQ_CODE = 9001;
    public static final int FACEBOOK_REQ_CODE = 64206;

    /* ----- Google+ Login Variables ---- */
    GoogleSignInClient googleApiClient;
    ApiManager apiManager;
    public static String currentVersion;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("version", "appVersion" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetAppVersion(this).execute();

        // Session Manager
        apiManager = new ApiManager(this, this);
        login_layout = findViewById(R.id.login_layout);
        guest_login = findViewById(R.id.guest_login);
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        fb_btn = findViewById(R.id.fb_btn);
        gmail_btn = findViewById(R.id.gmail_btn);
        guest_login.setOnClickListener(this);
        login_layout.setOnClickListener(this);

        errorDialog = new ErrorDialog(this, "Please check your internet connection");
        /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();*/
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SocialLogin.this, "cancel...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SocialLogin.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        });

        /*-------------- Google+ ----------------*/
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleApiClient = GoogleSignIn.getClient(this, googleSignInOptions);

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
                        try {
                            if (report.areAllPermissionsGranted()) {
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                            }

                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {
                                // autoCountrySelect();
                                enableLocationSettings();
                            }
                        } catch (Exception e) {
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

    SessionManager sessionManager;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;

    private void autoCountrySelect() {
        Log.e("LocationDetection", "m here");
        sessionManager = new SessionManager(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType")
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        //c_name = addresses.get(0).getCountryName();
                        String city_name = addresses.get(0).getLocality();
                        /*Log.e("countryname", addresses.get(0).getCountryName());
                        Log.e("cityname", city_name);*/
                        Log.e("cityname", city_name);
                        sessionManager.setUserLocation(addresses.get(0).getCountryName());
                        sessionManager.setUserAddress(city_name);
                       /* Log.e("cityname1", addresses.get(0).getAddressLine(0).toLowerCase());
                        Log.e("cityname2", addresses.get(0).getAdminArea());
                        Log.e("cityname3", addresses.get(0).getSubLocality());*/
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //autoCountrySelect();
                //enableLocationSettings();
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
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                    autoCountrySelect();

                })
                .addOnFailureListener(this, ex -> {
                    Log.e("LocationService", "Failure");
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(SocialLogin.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            googleApiClient.signOut()
                    .addOnCompleteListener(this, task -> {
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                //user clicked OK, you can startUpdatingLocation(...);
                autoCountrySelect();
                //enableLocationSettings();
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        // Pass the activity result to the FacebookCallback.
        if (requestCode == FACEBOOK_REQ_CODE) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        // Log.e("","fb"+data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {

                apiManager.login_FbGoogle(account.getDisplayName(), "google", account.getId(),account.getEmail());

                new SessionManager(this).setUserFacebookName(account.getDisplayName());
                Log.e("emeil",account.getEmail());
            }

        } catch (ApiException e) {
            //The ApiException status code indicates the detailed failure reason.
            //Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /*------- Get Facebook Data ------------*/
    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    try {
                        Log.e("LOG_TAG", "fb json object: " + object);
                        Log.e("LOG_TAG", "fb graph response: " + response);
                        String id = object.getString("id");
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        // 8/06/21 set user name in sheared prefrences
                        new SessionManager(this).setUserFacebookName(first_name + " " + last_name);
                        //Log.e("Userfacebooknamesocialp", first_name);
                        //String last_name = object.getString("last_name");
                        //String gender = object.getString("gender");
                        //String birthday = object.getString("birthday");
                        //String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

                        String email = "";
                        if (object.has("email")) {
                            email = object.getString("email");
                        }
                        Log.e("emailId",email);
                        apiManager.login_FbGoogle(first_name, "facebook", id,email);

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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.guest_login:
                @SuppressLint("HardwareIds")
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                 //String deviceId = "984036547822";
                //Log.e("Device_id", deviceId);
                apiManager.guestRegister("guest", deviceId);
               /* startActivity(new Intent(SocialLogin.this, LocationSelection.class));
                finish();*/
                break;
            case R.id.login_layout:
                //show dialog for user login
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                break;

            case R.id.fb_btn:
                checkInternetConnection();
                if (activeNetwork != null) {
                    loginButton.performClick();
                } else {
                    errorDialog.show();
                }
                return;

            case R.id.gmail_btn:
                checkInternetConnection();
                if (activeNetwork != null) {
                    Intent signInIntent = googleApiClient.getSignInIntent();
                    startActivityForResult(signInIntent, REQ_CODE);
                } else {
                    errorDialog.show();
                }
                return;

        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        String c_name = new SessionManager(getApplicationContext()).getUserLocation();

        if (ServiceCode == Constant.REGISTER) {
            LoginResponse rsp = (LoginResponse) response;
            //new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
            if (c_name.equals("null")) {
                Log.e("userLocationLog", c_name);
                if (rsp.getResult().getAllow_in_app_purchase() == 0) {
                    new SessionManager(this).createLoginSession(rsp);
                    //new SessionManager(this).setUserLocation("India");
                    //Log.e("counteryINACT", new SessionManager(this).getUserLocation());

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    new SessionManager(this).createLoginSession(rsp);
                    startActivity(new Intent(SocialLogin.this, LocationSelection.class));
                }
              /*new SessionManager(this).createLoginSession(rsp);
                startActivity(new Intent(SocialLogin.this, LocationSelection.class));*/
            } else {
                new SessionManager(this).createLoginSession(rsp);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }
        if (ServiceCode == Constant.GUEST_REGISTER) {
            LoginResponse rsp = (LoginResponse) response;
            /*if (rsp.getResult() != null && rsp.getResult().getUsername() != null && rsp.getResult().getDemo_password() != null) {
                new SessionManager(this).saveGuestPassword(rsp.getResult().getDemo_password());
                apiManager.login(rsp.getResult().getUsername(), rsp.getResult().getDemo_password());
            }*/
            if (rsp.getResult() != null && rsp.getResult().getUsername() != null && rsp.getResult().getDemo_password() != null) {
                //new SessionManager(this).saveGuestStatus(Integer.parseInt(rsp.getAlready_registered()));
                new SessionManager(this).saveGuestStatus((rsp.getResult().getGuest_status()));
                new SessionManager(this).saveGuestPassword(rsp.getResult().getDemo_password());
                apiManager.login(rsp.getResult().getUsername(), rsp.getResult().getDemo_password());
            }
        }

        if (ServiceCode == Constant.LOGIN) {
            LoginResponse rsp = (LoginResponse) response;
            if (c_name.equals("null")) {
                if (rsp.getResult().getAllow_in_app_purchase() == 0) {
                    new SessionManager(this).createLoginSession(rsp);
                    //Log.e("counteryINACT", new SessionManager(this).getUserLocation());
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    new SessionManager(this).createLoginSession(rsp);
                    startActivity(new Intent(SocialLogin.this, MainActivity.class));
                }
            } else {
                new SessionManager(this).createLoginSession(rsp);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //finishAffinity();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}