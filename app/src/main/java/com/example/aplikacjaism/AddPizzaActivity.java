package com.example.aplikacjaism;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddPizzaActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    protected ImageView mPizzaImage;
    protected TextView mPizzaName;
    protected TextView mPizzaDescription;

    private FloatingActionButton mAddPizzaButton;

    protected TextView addpizzaText;
    Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        mPizzaImage = findViewById(R.id.newPizzaImage);
        mPizzaName = findViewById(R.id.newPizzaName);
        mPizzaDescription = findViewById(R.id.newPizzaDescription);

        mAddPizzaButton = findViewById(R.id.addPizzaButton);
        addpizzaText = findViewById(R.id.addPizzaText);
        addpizzaText.setText("Dodaj nową pizzę");

        mPizzaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

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

                new FirebaseDatabaseHelper().addPizza(mPizzaImage, pizza, new FirebaseDatabaseHelper.DataStatus() {
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
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imgUri = data.getData();
            mPizzaImage.setImageURI(imgUri);
        }
    }


}
