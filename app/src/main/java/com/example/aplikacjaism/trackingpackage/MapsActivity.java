package com.example.aplikacjaism.trackingpackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.aplikacjaism.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceOrder;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mDatabase = FirebaseDatabase.getInstance();
        mReferenceOrder = mDatabase.getReference("orders");


        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        final FirebaseUser user = mAuth.getCurrentUser();

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(51.2351799, 22.5488377);
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(location, 20);

        MarkerOptions markerOptions = new MarkerOptions().position(location);

        final Marker marker = mMap.addMarker(markerOptions);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pizza));

        marker.setPosition(location);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(zoom);


        mReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());

                    if (keyNode.getValue(Order.class).getUserId().equals(user.getUid())) {
                        Order order = keyNode.getValue(Order.class);
                        LatLng coordinates = new LatLng(order.getCoordinates().getLatitude(), order.getCoordinates().getLongitude());
                        marker.setPosition(coordinates);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(coordinates, 20);
                        mMap.animateCamera(zoom);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
