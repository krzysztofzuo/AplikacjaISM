package com.example.aplikacjaism;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.database.Pizza;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private final int EDIT_ACTIVITY = 2;
    private ImageView pizzaImage;
    private TextView pizzaName;
    private TextView pizzaDescription;
//    private AppDatabase appDatabase;
    private List<Pizza> listaElementow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
 /*       pizzaImage = findViewById(R.id.pizzaImage);
        pizzaName = findViewById(R.id.pizzaName);
        pizzaDescription = findViewById(R.id.pizzaDescription);
        appDatabase = AppDatabase.getDatabase(this);
*/        Button editButton = findViewById(R.id.editButton);
        Button deleteButton = findViewById(R.id.deleteButton);

 //       listaElementow = appDatabase.pizzaDao().getAll();
        final int id = getIntent().getIntExtra("id", 0);

        final Pizza pizza = listaElementow.get(id);
        pizzaDescription.setText(pizza.getPizzaDescription());
        pizzaName.setText(pizza.getPizzaName());
        pizzaImage.setImageURI(Uri.parse(pizza.getPizzaImage()));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 //               appDatabase.pizzaDao().delete(pizza);
                Intent intent = new Intent(DetailsActivity.this, ListActivity.class);
                Toast.makeText(DetailsActivity.this, "Usunięto pizzę", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, EditPizzaActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, EDIT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
