package com.example.gpslocationtracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.*;

public class MainActivity extends AppCompatActivity {

    Button btn_start;
    Button btn_history;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    TextView tv_latitude, tv_longitude, tv_address, tv_area, tv_locality;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude, longitude;
    Geocoder geocoder;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_history = (Button) findViewById(R.id.btn_history);

        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_locality = (TextView) findViewById(R.id.tv_locality);

        geocoder = new Geocoder(this, Locale.getDefault());

        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        medit = mPref.edit();

        final DatabaseHandler db = new DatabaseHandler(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                List<Address> addresses = null;
                String address = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    String postalCode = addresses.get(0).getPostalCode();

                    tv_area.setText(addresses.get(0).getAdminArea());
                    tv_locality.setText(postalCode);
                    tv_address.setText(address);

                    db.addLocation(new Whereabouts(latitude.toString(), longitude.toString(), postalCode));
                    Toast.makeText(getApplicationContext(), "location added", Toast.LENGTH_LONG).show();
                    Log.d("Added :", " Location added successfully");

                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                tv_latitude.setText(latitude+"");
                tv_longitude.setText(longitude+"");
                tv_address.getText();

                checkGeoFencing(address);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_PERMISSIONS);
            return;
        }else{
            configureButton();
        }

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Whereabouts> locationHistory = (ArrayList<Whereabouts>) db.getAllLocations();
                String postalCode = locationHistory.get(0).getArea();
                Log.d("location list :", postalCode);
                Intent intent = new Intent(getApplicationContext(), LocationHistory.class);
                intent.putExtra("locationlist", (Serializable) locationHistory);
                startActivity(intent);
            }
        });


    }

    private void checkGeoFencing(String address){
        if(address.contains("Indian Institute of Technology"))
            notifyUser();
    }

    private void notifyUser(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Geo-fencing")
                .setContentText("You have entered restricted location")
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(0,builder.build());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;
                    configureButton();
                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
                    boolean_permission = false;
                }
            }
        }
    }

    private void configureButton() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
            }
        });
    }

}
