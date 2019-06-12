package com.example.aplikacjaism;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.database.AppDatabase;
import com.example.aplikacjaism.database.Pizza;
import com.example.aplikacjaism.database.PizzaDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.ConstantCallSite;

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
                    Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) newPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
                    pizza.setPizzaImage(saveToInternalStorage(bitmap, String.valueOf(appDatabase.pizzaDao().size() + 1)));
                    pizza.setPizzaName(newPizzaName.getText().toString());
                    pizza.setPizzaDescription(newPizzaDescription.getText().toString());
                    appDatabase.pizzaDao().insert(pizza);
                } catch (NullPointerException e) {
                    Toast.makeText(AddPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(AddPizzaActivity.this, "Dodano nową pizzę", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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