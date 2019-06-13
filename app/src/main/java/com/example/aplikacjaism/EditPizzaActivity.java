package com.example.aplikacjaism;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.aplikacjaism.database.Pizza;

import java.util.List;

public class EditPizzaActivity extends AddPizzaActivity {
    private final int EDIT_ACTIVITY_RESULT = 2;
    Uri cos;
    private List<Pizza> listaElementow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addpizzaText.setText("Edytuj pizzę");

        listaElementow = appDatabase.pizzaDao().getAll();
        int id = getIntent().getIntExtra("id", 0);

        final Pizza pizza = listaElementow.get(id);
        newPizzaName.setText(pizza.getPizzaName());
        newPizzaDescription.setText(pizza.getPizzaDescription());
        newPizzaImage.setImageURI(Uri.parse(pizza.getPizzaImage()));

        addPizzaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) newPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
                    pizza.setPizzaImage(saveToInternalStorage(bitmap, String.valueOf(appDatabase.pizzaDao().size() + 1)));
                    pizza.setPizzaName(newPizzaName.getText().toString());
                    pizza.setPizzaDescription(newPizzaDescription.getText().toString());
                    appDatabase.pizzaDao().update(pizza);
                } catch (NullPointerException e) {
                    Toast.makeText(EditPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(EditPizzaActivity.this, "Edytowano pizzę", Toast.LENGTH_SHORT).show();
                appDatabase.pizzaDao().update(pizza);
                Intent intent = new Intent(EditPizzaActivity.this, MainActivity.class);
                startActivityForResult(intent, EDIT_ACTIVITY_RESULT);
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
