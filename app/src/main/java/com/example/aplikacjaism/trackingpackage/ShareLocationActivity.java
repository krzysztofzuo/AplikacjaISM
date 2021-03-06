package com.example.aplikacjaism.trackingpackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aplikacjaism.R;
import com.example.aplikacjaism.pizzapackage.PizzaListActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShareLocationActivity extends AppCompatActivity {
    private String key;
    private String address;
    LocationManager locationManager;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    private Button orderFinishButton;
    private Button navigateButton;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_location_activity);
        key = getIntent().getStringExtra("key");
        address = getIntent().getStringExtra("address");

        orderFinishButton = findViewById(R.id.orderFinishButton);
        navigateButton = findViewById(R.id.navigateButton);

        mAddress = findViewById(R.id.orderAddress);
        mAddress.setText(address);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("orders");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,},
                    11);
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            return;
        }

        orderFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShareLocationService.class);
                stopService(intent);
                mReference.child(key).setValue(null);
                Toast.makeText(ShareLocationActivity.this, "Dostawa została zakończona!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShareLocationActivity.this, PizzaListActivity.class));
                finish();
            }
        });

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShareLocationService.class);
                intent.putExtra("key", key);
                startService(intent);

                String map = "http://maps.google.co.in/maps?q=" + address;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });
    }
}
