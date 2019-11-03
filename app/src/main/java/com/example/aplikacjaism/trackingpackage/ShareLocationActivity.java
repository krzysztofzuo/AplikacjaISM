package com.example.aplikacjaism.trackingpackage;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aplikacjaism.R;

public class ShareLocationActivity extends AppCompatActivity {
    private String key;
    private LocationListener mLocationListener;
    private Context context;
    LocationManager locationManager;
    TextView txtLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_location_activity);
        key = getIntent().getStringExtra("key");

        txtLat = (TextView) findViewById(R.id.lokacja);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, (LocationListener) this);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                txtLat = (TextView) findViewById(R.id.lokacja);
                txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
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
        };
    }
}
