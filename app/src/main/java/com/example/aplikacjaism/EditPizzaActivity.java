package com.example.aplikacjaism;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.aplikacjaism.database.Pizza;

import java.util.List;

public class EditPizzaActivity extends AddPizzaActivity {
    private List<Pizza> listaElementow;
    private Pizza pizza;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addpizzaText.setText("Edytuj pizzę");

//        listaElementow = appDatabase.pizzaDao().getAll();
        id = getIntent().getIntExtra("id", 0);
        pizza = listaElementow.get(id);
        newPizzaName.setText(pizza.getPizzaName());
        newPizzaDescription.setText(pizza.getPizzaDescription());
        newPizzaImage.setImageURI(Uri.parse(pizza.getPizzaImage()));
    }

    public void klik(View v) {
        try {
            Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) newPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
            pizza.setPizzaImage(saveToInternalStorage(bitmap, String.valueOf(id + 1)));
            pizza.setPizzaName(newPizzaName.getText().toString());
            pizza.setPizzaDescription(newPizzaDescription.getText().toString());
        } catch (NullPointerException e) {
            Toast.makeText(EditPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(EditPizzaActivity.this, "Edytowano pizzę", Toast.LENGTH_SHORT).show();
 //       appDatabase.pizzaDao().update(pizza);
        finish();
    }
}
