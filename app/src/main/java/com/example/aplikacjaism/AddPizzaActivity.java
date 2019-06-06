package com.example.aplikacjaism;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.database.AppDatabase;
import com.example.aplikacjaism.database.Pizza;

public class AddPizzaActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    private ImageView newPizzaImage;
    private TextView newPizzaName;
    private TextView newPizzaDescription;
    private FloatingActionButton addPizzaButton;
    private AppDatabase appDatabase;
    Uri cos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        newPizzaImage = findViewById(R.id.newPizzaImage);
        newPizzaName = findViewById(R.id.newPizzaName);
        newPizzaDescription = findViewById(R.id.newPizzaDescription);
        addPizzaButton = findViewById(R.id.addPizzaButton);
        appDatabase = AppDatabase.getDatabase(this);

        newPizzaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        addPizzaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Pizza pizza = new Pizza();
                    pizza.setPizzaImage(cos.toString());
                    pizza.setPizzaName(newPizzaName.getText().toString());
                    pizza.setPizzaDescription(newPizzaDescription.getText().toString());
                    appDatabase.pizzaDao().insert(pizza);
                } catch (NullPointerException e) {
                    Toast.makeText(AddPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            cos = data.getData();
            newPizzaImage.setImageURI(cos);
        }
    }
}
