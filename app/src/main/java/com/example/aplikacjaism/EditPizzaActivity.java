package com.example.aplikacjaism;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.database.AppDatabase;
import com.example.aplikacjaism.database.Pizza;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EditPizzaActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    private final int EDIT_ACTIVITY_RESULT = 2;
    private ImageView newPizzaImage;
    private TextView newPizzaName;
    private TextView newPizzaDescription;
    private FloatingActionButton addPizzaButton;
    private AppDatabase appDatabase;
    private TextView addpizzaText;
    Uri cos;
    private List<Pizza> listaElementow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        newPizzaImage = findViewById(R.id.newPizzaImage);
        newPizzaName = findViewById(R.id.newPizzaName);
        newPizzaDescription = findViewById(R.id.newPizzaDescription);
        addPizzaButton = findViewById(R.id.addPizzaButton);
        appDatabase = AppDatabase.getDatabase(this);
        addpizzaText = findViewById(R.id.addPizzaText);
        addpizzaText.setText("Edytuj pizzę");

        listaElementow = appDatabase.pizzaDao().getAll();
        int id = getIntent().getIntExtra("id", 0);

        final Pizza pizza = listaElementow.get(id);
        newPizzaName.setText(pizza.getPizzaName());
        newPizzaDescription.setText(pizza.getPizzaDescription());
        newPizzaImage.setImageURI(Uri.parse(pizza.getPizzaImage()));
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

    private String saveToInternalStorage(Bitmap bitmapImage, String id) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, id + ".jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }
}
