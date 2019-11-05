package com.example.aplikacjaism.trackingpackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.R;
import com.example.aplikacjaism.pizzapackage.PizzaListActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareLocationActivity extends AppCompatActivity {
    private String key;
    private Context context;
    LocationManager locationManager;
    TextView txtLat;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    private Button orderFinishButton;
    private Button navigateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_location_activity);
        key = getIntent().getStringExtra("key");

        txtLat = findViewById(R.id.lokacja);
        orderFinishButton = findViewById(R.id.orderFinishButton);
        navigateButton = findViewById(R.id.navigateButton);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("orders");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,},
                    11);
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, (LocationListener) locationListener);

        orderFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationListener);
                mReference.child(key).setValue(null);
                Toast.makeText(ShareLocationActivity.this, "Dostawa została zakończona!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShareLocationActivity.this, PizzaListActivity.class));
                finish();
            }
        });
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            txtLat.setText("Szerokość: " + location.getLatitude() + " Długość: " + location.getLongitude());

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
    };
}
