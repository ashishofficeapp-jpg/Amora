package com.amora.app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;

// Task
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.amora.app.R;
import com.amora.app.dialog.CompleteProfileDialog;
import com.amora.app.fragment.DiscoverFragment;
import com.amora.app.fragment.InboxFragment;
import com.amora.app.fragment.MyAccountFragment;
import com.amora.app.fragment.MyFavourite;
import com.amora.app.fragment.OnCamFragment;
import com.amora.app.fragment.UserMenuFragment;
import com.amora.app.helper.MyCountDownTimer;
import com.amora.app.helper.NetworkCheck;
import com.amora.app.response.Ban.BanResponce;
import com.amora.app.response.OnlineStatusResponse;
import com.amora.app.response.ProfileDetailsResponse;
import com.amora.app.response.Token;
import com.amora.app.retrofit.ApiManager;
import com.amora.app.retrofit.ApiResponseInterface;
import com.amora.app.utils.Constant;
import com.amora.app.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ApiResponseInterface, BottomNavigationView.OnNavigationItemSelectedListener {
    String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;
    SessionManager sessionManager;
    BottomNavigationView navigation;
    Fragment fragment = null;
    //fragment new code
    final Fragment userMenuFragment = new UserMenuFragment();
    Fragment onCamFragment = new OnCamFragment();
    Fragment messageFragment = new InboxFragment();
    final Fragment myAccountFragment = new MyAccountFragment();
    Fragment discover = new DiscoverFragment();
    Fragment myFavourite = new MyFavourite();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;
    ApiManager apiManager;
    String fcmToken;
    DatabaseReference chatRef;
    private NetworkCheck networkCheck;
    int isGuest = 0;
    private static final int PICK_IMAGE_CAMERA_REQUEST_CODE = 0;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 1;
    private String city_name = "";
    int MY_REQUEST_CODE = 2020;
    int REQUEST_CODE_CHECK_SETTINGS = 2021;
    private String facebook_name = "";
    private double latitude;
    private double longitude;

    private AppUpdateManager appUpdateManager;
    private static final int ME_APP_UPDATE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.content_main);


        networkCheck = new NetworkCheck();
        sessionManager = new SessionManager(this);
        navigation = findViewById(R.id.bottomNavigationView);

        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
        fm.beginTransaction().add(R.id.fragment_view, messageFragment, "3").hide(messageFragment).commit();
        fm.beginTransaction().add(R.id.fragment_view, myAccountFragment, "4").hide(myAccountFragment).commit();
        fm.beginTransaction().add(R.id.fragment_view, myFavourite, "5").hide(myFavourite).commit();


        if (sessionManager.getGender().equals("male")) {
            addFragment(userMenuFragment, "1");
        } else {
            addFragment(userMenuFragment, "1");
        }

        apiManager = new ApiManager(this, this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            fcmToken = instanceIdResult.getToken();
            // I am commenting this line to prevent token expire problem
            // if (new SessionManager(this).getFcmToken() == null) {
            apiManager.registerFcmToken(fcmToken);

        });

        facebook_name = sessionManager.getUserFacebookName();
        Log.e("userfacebookname", facebook_name);

        checkGuestLogin();
        getPermission();
        if (sessionManager.getGender().equals("male")) {
            //getPermission();
            if (canGetLocation()) {
                //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    autoCountrySelect();
                }
            } else {
                //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                //showSettingsAlert();
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings();
                }


            }
        }
        //drawBadge();


        showAppUpdate();

    }


    private void getPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        try {
                            if (report.areAllPermissionsGranted()) {
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                            }

                            if (report.getGrantedPermissionResponses().get(0).getPermissionName().equals("android.permission.ACCESS_FINE_LOCATION")) {
                                autoCountrySelect();
                                //enableLocationSettings();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkGuestLogin() {
        if (sessionManager.getGender().equals("male")) {
            isGuest = sessionManager.getGuestStatus();
            Log.e("inActivity", "" + new SessionManager(this).getGuestStatus());
            if (isGuest != 1) {
                if (isGuest == 0) {
                    new CompleteProfileDialog(this, facebook_name);
                    isGuest = 1;
                } else if (facebook_name != null) {
                    new CompleteProfileDialog(this, facebook_name);
                }
                if (!sessionManager.getUserAskpermission().equals("no")) {
                    //new PermissionDialog(MainActivity.this);

                }
            }
        }

    }

    private void autoCountrySelect() {
        Log.e("LocationDetection", "m here");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType")
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        city_name = addresses.get(0).getLocality();

                        Log.e("myAddress", city_name);
                        sessionManager.setUserLocation(addresses.get(0).getCountryName());
                        sessionManager.setUserAddress(city_name);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e("Mainlatitude", String.valueOf(latitude));
                        Log.e("Mainlongitude", String.valueOf(longitude));

                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                enableLocationSettings();
            }
        }

    }

    public boolean canGetLocation() {
        boolean result = true;
        LocationManager lm;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled && networkEnabled;
    }


    public void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(100)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    //startUpdatingLocation(...);
                    //autoCountrySelect();

                })
                .addOnFailureListener(this, ex -> {
                    Log.e("LocationService", "Failure");
                    if (ex instanceof ResolvableApiException) {
                        //Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            //Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(MainActivity.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            //Ignore the error.
                        }
                    }
                });
    }

    void checkOnlineAvailability(String uid, String name, String image) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // Change online status when user comes back on app
                    HashMap<String, String> details = new HashMap<>();
                    details.put("uid", uid);
                    details.put("name", name);
                    details.put("image", image);
                    details.put("status", "Online");
                    chatRef.child(uid).setValue(details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Listener was cancelled");
            }
        });
    }


    private void showAppUpdate() {
//        appUpdateManager = AppUpdateManagerFactory.create(this);
//
//        appUpdateManager.registerListener(installStateUpdatedListener);
//
//        appUpdateManager.getAppUpdateInfo()
//                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//                    @Override
//                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
//
//                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//
//                            try {
//                                appUpdateManager.startUpdateFlowForResult(
//                                        appUpdateInfo,
//                                        AppUpdateType.IMMEDIATE,
//                                        MainActivity.this,
//                                        ME_APP_UPDATE
//                                );
//                            } catch (IntentSender.SendIntentException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
    }


//    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
//        @Override
//        public void onStateUpdate(@NonNull @NotNull InstallState state) {
//
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                showCompleteUpdate();
//            }
//        }
//    };

    @Override
    protected void onStop() {
        //hide for Immediate update for app
        //if (appUpdateManager != null) appUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    private void showCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New App is Availeble",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ME_APP_UPDATE && resultCode != RESULT_OK) {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
            Log.e("appUpadte", "failed==" + requestCode);
        }

        Log.e("selectedImage", "selectedImage:" + data);
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                enableLocationSettings();
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("ProductList", "onActivityResult: app download failed");
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null || requestCode == PICK_IMAGE_CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK) {
                        Intent Intent = new Intent("FBR-USER-IMAGE");
                        // Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri selectedCamera = data.getParcelableExtra("path");
                        //call this method to get the URI from the bitmap
                        //Uri selectedCamera = getImageUri(getApplicationContext(), photo);
                        // call this method to get the actual path of capture image
                        //String finalCameraImage = getProfileImagePath(this, selectedCamera);
                        String finalCameraImage = selectedCamera.getPath();
                        Log.e("selectedCameraImage", "selectedCameraImage:" + finalCameraImage);
                        if (!finalCameraImage.equals("Not found")) {
                            Intent.putExtra("uri", finalCameraImage);
                            Intent.putExtra("fromCam", "yes");
                            this.sendBroadcast(Intent);
                        }

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Intent myIntent = new Intent("FBR-USER-IMAGE");
                        //Uri selectedImage = data.getData();
                        Uri selectedImage = data.getParcelableExtra("path");
                        //Log.e("selectedImage", "selectedImage:" + selectedImage);
                        //String picturePath = getProfileImagePath(this, selectedImage);
                        String picturePath = selectedImage.getPath();
                        if (!picturePath.equals("Not found")) {
                            myIntent.putExtra("uri", picturePath);
                            myIntent.putExtra("fromCam", "no");
                            this.sendBroadcast(myIntent);
                            //Log.e("selectedImage", "selectedImage:" + picturePath);
                        }

                    }
                    break;
            }
        }

    }

    public static String getProfileImagePath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Camera", null);
        return Uri.parse(path);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("FBR-IMAGE"));
        Menu menu = navigation.getMenu();
        // if male user is offline hit api to change status
        apiManager.changeOnlineStatus(1);
        /*if (sessionManager.getGender().equals("male")) {
        } else {
        }*/

        // show immediate app update dialog
//        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//            @Override
//            public void onSuccess(AppUpdateInfo result) {
//                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                    try {
//                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this,
//                                ME_APP_UPDATE);
//                    } catch (IntentSender.SendIntentException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });

    }

    void verifyUserRegisteredFirebase(String uid, String name, String image) {
        chatRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.child("name").exists()) {
                        //Set for female user only(prevent server load from php)
                        if (!sessionManager.getGender().equals("male")) {
                            //Prepare offline status for existing user
                            HashMap<String, String> details = new HashMap<>();
                            details.put("uid", uid);
                            details.put("name", name);
                            details.put("image", image);
                            details.put("status", "Offline");
                            // for disconnected state
                            chatRef.child(uid).onDisconnect().setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        checkOnlineAvailability(uid, name, image);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        HashMap<String, String> details = new HashMap<>();
                        details.put("uid", uid);
                        details.put("name", name);
                        details.put("image", image);
                        //Set for female user only(prevent server load from php)
                        if (!sessionManager.getGender().equals("male")) {
                            details.put("status", "Offline");
                        }
                        chatRef.child(uid).setValue(details).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Set for female user only(prevent server load for php)
                                if (!sessionManager.getGender().equals("male")) {
                                    //for disconnected state
                                    chatRef.child(uid).onDisconnect().setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                checkOnlineAvailability(uid, name, image);
                                            }
                                        }
                                    });
                                }
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    TextView badgeText;
    /*private void drawBadge() {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);
        badgeText = badge.findViewById(R.id.notificationsinbag);
    }*/

    public void updateMessageCount(int msgCount) {
        if (msgCount >= 99) {
            badgeText.setText("99+");
            //((TextView) findViewById(R.id.tv_unreadmain)).setText("99+");
        } else {
            //badgeText.setText(String.valueOf(msgCount));
            //((TextView) findViewById(R.id.tv_unreadmain)).setText(String.valueOf(msgCount));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //badgeText.setBackground(getResources().getDrawable(R.drawable.circularbadgeunselect));
        //Handles unnecessary click on already selected item
        if (id == navigation.getSelectedItemId())
            return true;
        if (id == R.id.navigation_home) {
            if (sessionManager.getGender().equals("male")) {
                //fragment = new HomeFragment();
                showFragment(userMenuFragment);
                if (myCountDownTimer != null) {
                    inCount = false;
                    myCountDownTimer.cancel();
                }
                detachOncam();
                userMenuFragment.onResume();
            }
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //noinspection Simplifiable IfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void newHomeMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuHome)).setImageResource(R.drawable.heartattackselected);
        showFragment(userMenuFragment);
        userMenuFragment.onResume();
    }

    public void maleOnCamMenu(View v) {
       /* PaginationAdapterCallback paginationAdapterCallback = null;
        paginationAdapterCallback.retryPageLoad();
        DiscoverFragment discoverFragment = new DiscoverFragment();*/

       // DiscoverUserAdapter discoverUserAdapter = new DiscoverUserAdapter(MainActivity.this,paginationAdapterCallback,"",discoverFragment);
        //discoverFragment.

       /* Intent Intent = new Intent("FBR-USER-VIDEO");
        Intent.putExtra("video", "pause");
        this.sendBroadcast(Intent);*/

        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonselected);
        onCamFragment = new OnCamFragment();
        fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
        showFragment(onCamFragment);
    }

    public void newChatMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.drawable.conversationselected);
        showFragment(messageFragment);
    }

    public void newProfileMenu(View v) {
        unselectAllMenu();
        ((ImageView) findViewById(R.id.img_newMenuProfile)).setImageResource(R.drawable.avatarselected);
        showFragment(myAccountFragment);
    }

    private void unselectAllMenu() {
        ((ImageView) findViewById(R.id.img_newMenuHome)).setImageResource(R.drawable.heartattackunselect);
        ((ImageView) findViewById(R.id.img_newMenuOnCam)).setImageResource(R.drawable.playbuttonunselect);
        ((ImageView) findViewById(R.id.img_newMenuMessage)).setImageResource(R.drawable.conversationunselect);
        ((ImageView) findViewById(R.id.img_newMenuProfile)).setImageResource(R.drawable.avatarunselect);
    }

    public void showFollowers() {
        /*Intent myIntent = new Intent("FBR");
        myIntent.putExtra("action", "reload");
        this.sendBroadcast(myIntent); */
        showFragment(myFavourite);
    }

    private void addFragment(Fragment fragment, String tag) {
        fm.beginTransaction().add(R.id.fragment_view, fragment, tag).commit();
        active = fragment;
    }

    private void showFragment(Fragment fragment) {
        fm.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;
    }

    public void loadHomeFragment() {
        detachOncam();
        onCamFragment = new OnCamFragment();
        fm.beginTransaction().add(R.id.fragment_view, onCamFragment, "2").hide(onCamFragment).commit();
        showFragment(onCamFragment);
        navigation.getMenu().getItem(1).setChecked(true);
    }


    private void saveUserTokenIntoFirebase(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(sessionManager.getUserId()).setValue(token1);
    }

    private void detachOncam() {
        try {
            getSupportFragmentManager().beginTransaction().remove(onCamFragment).commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.REGISTER_FCM_TOKEN) {
            sessionManager.saveFcmToken(fcmToken);
            saveUserTokenIntoFirebase(fcmToken);
            Log.e("typoTpoken", fcmToken);
        }
        if (ServiceCode == Constant.MANAGE_ONLINE_STATUS) {
            OnlineStatusResponse reportResponse = (OnlineStatusResponse) response;
        }
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
            if (rsp.getSuccess() != null) {

                String img = "";
                if (rsp.getSuccess().getProfile_images() != null && rsp.getSuccess().getProfile_images().size() > 0) {
                    img = rsp.getSuccess().getProfile_images().get(0).getImage_name();
                }
                // Register User into firebase
                verifyUserRegisteredFirebase(String.valueOf(rsp.getSuccess().getProfile_id()), rsp.getSuccess().getName(), img);

            }
        }

        if (ServiceCode == Constant.BAN_DATAP) {
            BanResponce reportResponse = (BanResponce) response;
            if (reportResponse.getResult() != null) {
                if (reportResponse.getResult().getIsBanned() == 1) {
                    ((CardView) findViewById(R.id.cv_ban)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_banmsg)).setText("You are ban, please contact your manager.");
                    isBlock = 1;
                    navigation.setVisibility(View.GONE);
                } else {
                    ((CardView) findViewById(R.id.cv_ban)).setVisibility(View.GONE);
                }
            }
        }

    }

    int isBlock = 0;

    public int isBlockFunction() {
        return isBlock;
    }

    private MyCountDownTimer myCountDownTimer;
    private boolean inCount = false;

    @Override
    public void onBackPressed() {
        //unselectAllMenu();
        if (active instanceof UserMenuFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "click BACK again to go Exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            try {
                if (sessionManager.getGender().equals("male")) {
                    showFragment(userMenuFragment);
                    if (myCountDownTimer != null) {
                        inCount = false;
                        myCountDownTimer.cancel();
                    }
                    userMenuFragment.onResume();
                } else {
                    showFragment(userMenuFragment);
                }
                //loadFragment(fragment);
                navigation.getMenu().getItem(0).setChecked(true);
                setTitle("1");
            } catch (Exception e) {

            }

        }
    }

    public void logoutDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextView closeDialog = dialog.findViewById(R.id.close_dialog);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        TextView logout = dialog.findViewById(R.id.logout);
        tv_msg.setText("You have been logout. As your access token is expired.");
        closeDialog.setVisibility(View.GONE);
        closeDialog.setOnClickListener(view -> dialog.dismiss());

        logout.setText("OK");
        logout.setOnClickListener(view -> {
            dialog.dismiss();

            String eMail = new SessionManager(getApplicationContext()).getUserEmail();
            String passWord = new SessionManager(getApplicationContext()).getUserPassword();
            new SessionManager(getApplicationContext()).logoutUser();
            new ApiManager(getApplicationContext(), this).getUserLogout();
            new SessionManager(getApplicationContext()).setUserEmail(eMail);
            new SessionManager(getApplicationContext()).setUserPassword(passWord);
            finish();
        });
    }


    public void checkLocationSatae() {
        Log.e("userCityLog", "in cust function");
        if (sessionManager.getUserAddress().equals("null")) {
            Log.e("userCityLog", "in condition");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings();
                }
            } else {
                enableLocationSettings();
            }
        }
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equals("logout")) {
                logoutDialog();
            }
        }
    };


}