package com.meetlive.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.meetlive.app.R;
import com.meetlive.app.utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class LocationSelection extends AppCompatActivity {
    private String c_name = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        c_name = new SessionManager(getApplicationContext()).getUserLocation();
       /* if (!c_name.equals("null")) {
            Toast.makeText(this,
                    new SessionManager(getApplicationContext()).getUserLocation()
                    , Toast.LENGTH_SHORT).show();
        }*/

        ((TextView) findViewById(R.id.tv_india)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.tv_india)).setBackground(getResources().getDrawable(R.drawable.rounded_login));
                ((TextView) findViewById(R.id.tv_india)).setTextColor(getResources().getColor(R.color.white));
                ((TextView) findViewById(R.id.tv_other)).setBackground(getResources().getDrawable(R.drawable.rounded_white));
                ((TextView) findViewById(R.id.tv_other)).setTextColor(getResources().getColor(R.color.black));

                if (((TextView) findViewById(R.id.tv_next)).getVisibility() == View.GONE) {
                    ((TextView) findViewById(R.id.tv_next)).setVisibility(View.VISIBLE);
                }
                c_name = ((TextView) findViewById(R.id.tv_india)).getText().toString();
            }
        });

        ((TextView) findViewById(R.id.tv_other)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.tv_india)).setBackground(getResources().getDrawable(R.drawable.rounded_white));
                ((TextView) findViewById(R.id.tv_india)).setTextColor(getResources().getColor(R.color.black));
                ((TextView) findViewById(R.id.tv_other)).setBackground(getResources().getDrawable(R.drawable.rounded_login));
                ((TextView) findViewById(R.id.tv_other)).setTextColor(getResources().getColor(R.color.white));

                if (((TextView) findViewById(R.id.tv_next)).getVisibility() == View.GONE) {
                    ((TextView) findViewById(R.id.tv_next)).setVisibility(View.VISIBLE);
                }
                c_name = ((TextView) findViewById(R.id.tv_other)).getText().toString();

            }
        });

        ((TextView) findViewById(R.id.tv_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((LottieAnimationView) findViewById(R.id.loader)).setVisibility(View.VISIBLE);
                    new SessionManager(getApplicationContext()).setUserLocation(c_name);
                    Intent intent = new Intent(com.meetlive.app.activity.LocationSelection.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(com.meetlive.app.activity.LocationSelection.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        getPermission();

/*
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            autoCountrySelect();
        }
*/

        if (canGetLocation()) {
            //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                autoCountrySelect();
            }

        } else {
            //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
            showSettingsAlert();
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
            networkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled && networkEnabled;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        //alertDialog.setTitle("Error!");

        // Setting Dialog Message
        alertDialog.setMessage("We detect your location server is off. Kindly turn it on to use this service");

        // On pressing Settings button
        alertDialog.setPositiveButton(
                getResources().getString(R.string.button_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.show();
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

    private void autoCountrySelect() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        ((LottieAnimationView) findViewById(R.id.loader)).setVisibility(View.VISIBLE);
                        c_name = addresses.get(0).getCountryName();
                        new SessionManager(getApplicationContext()).setUserLocation(c_name);
                        Intent intent = new Intent(com.meetlive.app.activity.LocationSelection.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((LottieAnimationView) findViewById(R.id.loader)).setVisibility(View.GONE);
    }
}