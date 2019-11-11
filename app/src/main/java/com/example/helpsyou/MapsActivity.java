package com.example.helpsyou;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    Marker mMarker;
    TextView Lat, Long;
    Button Notify;
    Double latitude,longitude;
    private static  final String PRIMARY_CHANNEL_ID ="primatry notification channel";
    private NotificationManager mnNotificationManager;
    private static final int NOTIFICATION_ID=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Lat = findViewById(R.id.latitude_textview);
        Long = findViewById(R.id.longitude_textview);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Notify = findViewById(R.id.notify);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                     longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Lat.setText(Double.toString(latitude));
                    Long.setText(Double.toString(latitude));
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + " ";
                        str += addressList.get(0).getCountryName();

                        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                     latitude = location.getLatitude();
                     longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                     Lat.setText(Double.toString(latitude));
                    Long.setText(Double.toString(latitude));
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + " ";
                        str += addressList.get(0).getCountryName();

                        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        Notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });




        createNotificationVChannel();
    }








    public void sendNotification()
    {

        NotificationCompat.Builder notifiyBuilder = getNotificationBuilder();
        mnNotificationManager.notify(NOTIFICATION_ID,notifiyBuilder.build());

    }





    public void createNotificationVChannel() {

        mnNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Test Notification ",
                    NotificationManager.IMPORTANCE_HIGH);


            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from test notification channel");

            mnNotificationManager.createNotificationChannel(notificationChannel);




        }

    }

    private NotificationCompat.Builder getNotificationBuilder()

    {
        Intent notificationIntent = new Intent(this,MapsActivity.class);
        PendingIntent notificationPendingIntent =PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle("Location Details")
                .setContentText("Latitude = "+latitude +" "+ "Longitude = "+longitude+" ")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);


        return  notifyBuilder;
    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}