package com.example.aplikacjaism.pizzapackage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.userpackage.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EditPizzaActivity extends AddPizzaActivity {

    private FirebaseAuth mAuth;

    private FloatingActionButton addPizzaButton;

    private String key;
    private String pizzaName;
    private String pizzaDescription;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.add_activity);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            addpizzaText.setText("Edytuj pizzę");

            key = getIntent().getStringExtra("key");
            pizzaName = getIntent().getStringExtra("pizzaName");
            pizzaDescription = getIntent().getStringExtra("pizzaDescription");

            mPizzaName = findViewById(R.id.newPizzaName);
            mPizzaName.setText(pizzaName);

            mPizzaDescription = findViewById(R.id.newPizzaDescription);
            mPizzaDescription.setText(pizzaDescription);

            mPizzaImgageFromGallery = findViewById(R.id.newGalleryImage);
            mPizzaImgageFromGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });

            mPizzaImage = findViewById(R.id.newPizzaImage);
            mPizzaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });
            storageRef.child("zdjecia/" + key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(EditPizzaActivity.this).load(uri).into(mPizzaImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            addPizzaButton = findViewById(R.id.addPizzaButton);
            addPizzaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pizza pizza = new Pizza();
                    pizza.setPizzaName(mPizzaName.getText().toString());
                    pizza.setPizzaDescription(mPizzaDescription.getText().toString());

                    new FirebaseDatabaseHelper().updatePizza(mPizzaImage, key, pizza, new DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {
                        }

                        @Override
                        public void DataIsInserted() {
                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(EditPizzaActivity.this, "Edytowano pizzę", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditPizzaActivity.this, PizzaListActivity.class);
                            startActivity(intent);
                            finish();
                            return;

                        }

                        @Override
                        public void DataIsDeleted() {
                        }

                        @Override
                        public void DataUsersIsLoaded(List<User> users, List<String> keys) {

                        }

                        @Override
                        public void DataOrdersIsLoaded(List<Order> orders, List<String> keys) {

                        }
                    });
                }
            });
        }
    }
}
