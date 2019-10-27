package com.example.aplikacjaism;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mReferenceOrder;
    private DatabaseReference mReferenceUser;
    private List<Pizza> users = new ArrayList<>();

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

            final String authKey = user.getUid();

            mReferenceUser = mDatabase.getReference("users");
            mReferenceUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        String DbKey = keyNode.getKey();
                        if (DbKey.equals(authKey)) {
                            admin = keyNode.getValue(Users.class).getAdmin();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            if (admin) {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }

            editButton = findViewById(R.id.editButton);
            deleteButton = findViewById(R.id.deleteButton);
            orderButton = findViewById(R.id.orderButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseDatabaseHelper().deletePizza(key, new FirebaseDatabaseHelper.DataStatus() {
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
                    });
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailsActivity.this, EditPizzaActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("pizzaName", pizzaName);
                    intent.putExtra("pizzaDescription", pizzaDescription);
                    startActivity(intent);


                }
            });

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pizza pizza = new Pizza();

                    key = getIntent().getStringExtra("key");
                    pizzaName = getIntent().getStringExtra("pizzaName");
                    pizzaDescription = getIntent().getStringExtra("pizzaDescription");

                    pizza.setPizzaName(pizzaName);
                    pizza.setPizzaDescription(pizzaDescription);

                    mReferenceOrder = mDatabase.getReference("orders");
                    String key = mReferenceOrder.push().getKey();
                    mReferenceOrder.child(key).setValue(pizza);

                    Toast.makeText(DetailsActivity.this, "Zamówiono pizzę!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailsActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}


