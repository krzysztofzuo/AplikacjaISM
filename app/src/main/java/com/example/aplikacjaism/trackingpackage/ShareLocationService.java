package com.example.aplikacjaism.trackingpackage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareLocationService extends Service implements LocationListener {
    String key;
    LocationManager locationManager;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    public void onCreate() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("orders");


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng();
        latLng.setLatitude(location.getLatitude());
        latLng.setLongitude(location.getLongitude());

        mReference.child(key).child("coordinates").setValue(latLng);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        key = (String) intent.getExtras().get("key");
        return super.onStartCommand(intent, flags, startId);
    }
}
