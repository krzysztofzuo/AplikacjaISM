package com.example.aplikacjaism;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Description extends AppCompatActivity {
    private ImageView pizzaImage;
    private TextView pizzaName;
    private TextView pizzaDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        pizzaImage = findViewById(R.id.pizzaImage);
        pizzaName = findViewById(R.id.pizzaName);
        pizzaDescription = findViewById(R.id.pizzaDescription);
    }


}
