package com.example.aplikacjaism.pizzapackage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.trackingpackage.LatLng;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.trackingpackage.MapsActivity;
import com.example.aplikacjaism.userpackage.SignInActivity;
import com.example.aplikacjaism.userpackage.User;
import com.example.aplikacjaism.userpackage.UserListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mReferenceOrder;

    Boolean admin = false;

    private ImageView mPizzaImage;
    private TextView mPizzaName;
    private TextView mPizzaDescription;

    private Button editButton;
    private Button deleteButton;
    private Button orderButton;

    private String key;
    private String pizzaName;
    private String pizzaDescription;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.details);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mUser = mDatabase.getReference("users");

        if (user != null) {
            key = getIntent().getStringExtra("key");
            pizzaName = getIntent().getStringExtra("pizzaName");
            pizzaDescription = getIntent().getStringExtra("pizzaDescription");

            mPizzaImage = findViewById(R.id.pizzaImage);
            storageRef.child("zdjecia/" + key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(DetailsActivity.this).load(uri).into(mPizzaImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            mPizzaName = findViewById(R.id.pizzaName);
            mPizzaName.setText(pizzaName);

            mPizzaDescription = findViewById(R.id.pizzaDescription);
            mPizzaDescription.setText(pizzaDescription);

            deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseDatabaseHelper().deletePizza(key, new DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(DetailsActivity.this, "Usunięto pizzę", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        @Override
                        public void DataUsersIsLoaded(List<User> users, List<String> keys) {

                        }
                    });
                }
            });
            editButton = findViewById(R.id.editButton);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailsActivity.this, EditPizzaActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("pizzaName", pizzaName);
                    intent.putExtra("pizzaDescription", pizzaDescription);
                    startActivity(intent);
                    finish();

                }
            });

            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.getKey().equals(user.getUid())) {
                            admin = keyNode.getValue(User.class).getAdmin();
                        }
                    }
                    if (admin) {
                        deleteButton.setEnabled(true);
                        deleteButton.setVisibility(View.VISIBLE);
                        editButton.setEnabled(true);
                        editButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pizza pizza = new Pizza();
                Order order = new Order();

                key = getIntent().getStringExtra("key");
                pizzaName = getIntent().getStringExtra("pizzaName");
                pizzaDescription = getIntent().getStringExtra("pizzaDescription");

                pizza.setPizzaName(pizzaName);
                pizza.setPizzaDescription(pizzaDescription);
                order.setPizza(pizza);
                order.setDate(new Date());
                order.setCoordinates(new LatLng(51.2351799, 22.5488377));
                order.setUserId(mAuth.getUid());

                mReferenceOrder = mDatabase.getReference("orders");
                String key = mReferenceOrder.push().getKey();

                mReferenceOrder.child(key).setValue(order);

                Toast.makeText(DetailsActivity.this, "Zamówiono pizzę!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailsActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final FirebaseUser user = mAuth.getCurrentUser();

        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setVisible(true);

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().equals(user.getUid())) {
                        admin = keyNode.getValue(User.class).getAdmin();
                    }
                }
                if (admin) {
                    menu.getItem(1).setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                mAuth.signOut();
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            }
            case R.id.users: {
                startActivity(new Intent(this, UserListActivity.class));
                return true;
            }

        }
        return true;
    }
}


