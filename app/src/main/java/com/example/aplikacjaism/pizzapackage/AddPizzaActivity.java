package com.example.aplikacjaism.pizzapackage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.userpackage.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddPizzaActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    protected ImageView mPizzaImage;
    protected TextView mPizzaName;
    protected TextView mPizzaDescription;
    protected Button mPizzaImgageFromGallery;

    private FloatingActionButton mAddPizzaButton;

    protected TextView addpizzaText;
    Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.add_activity);

        mPizzaName = findViewById(R.id.newPizzaName);
        mPizzaDescription = findViewById(R.id.newPizzaDescription);

        addpizzaText = findViewById(R.id.addPizzaText);
        addpizzaText.setText("Dodaj nową pizzę");

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

        mAddPizzaButton = findViewById(R.id.addPizzaButton);
        mAddPizzaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pizza pizza = new Pizza();
                try {
                    pizza.setPizzaName(mPizzaName.getText().toString());
                    pizza.setPizzaDescription(mPizzaDescription.getText().toString());


                } catch (NullPointerException e) {
                    Toast.makeText(AddPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }

                new FirebaseDatabaseHelper().addPizza(mPizzaImage, pizza, new DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(AddPizzaActivity.this, "Dodano nową pizzę", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    @Override
                    public void DataIsUpdated() {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imgUri = data.getData();
            mPizzaImage.setImageURI(imgUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mPizzaImage.setImageBitmap(imageBitmap);
        }
    }

}
