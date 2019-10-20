package com.example.aplikacjaism;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjaism.database.Pizza;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPizzaActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    protected ImageView newPizzaImage;
    protected TextView newPizzaName;
    protected TextView newPizzaDescription;
    protected FloatingActionButton addPizzaButton;
//    protected AppDatabase appDatabase;
    protected TextView addpizzaText;
    Uri cos;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myDatabase = database.getReference("pizzas/");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //generate UID
    DatabaseReference newDatabaseReference = myDatabase.child("loccheck").push();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        newPizzaImage = findViewById(R.id.newPizzaImage);
        newPizzaName = findViewById(R.id.newPizzaName);
        newPizzaDescription = findViewById(R.id.newPizzaDescription);
        addPizzaButton = findViewById(R.id.addPizzaButton);
//        appDatabase = AppDatabase.getDatabase(this);
        addpizzaText = findViewById(R.id.addPizzaText);
        addpizzaText.setText("Dodaj nową pizzę");


        newPizzaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


    }

    public void klik(View v) {

        Pizza pizza = new Pizza();
        try {
            //Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) newPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
            //pizza.setPizzaImage(saveToInternalStorage(bitmap, String.valueOf(appDatabase.pizzaDao().size() + 1)));
            pizza.setPizzaName(newPizzaName.getText().toString());
            pizza.setPizzaDescription(newPizzaDescription.getText().toString());
            pizza.setPizzaImage(newDatabaseReference.getKey());



// Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
// Create a reference to "image.jpg"
            StorageReference imageRef = storageRef.child("zdjecia/" + pizza.getPizzaImage() + ".jpg");
// Create a reference to 'images/image.jpg'
            StorageReference mountainImagesRef = storageRef.child("images/zdjecia/" + pizza.getPizzaImage() + ".jpg");
// While the file names are the same, the references point to different files
            imageRef.getName().equals(mountainImagesRef.getName());    // true
            imageRef.getPath().equals(mountainImagesRef.getPath());    // false
// Get the data from an ImageView as bytes
            newPizzaImage.setDrawingCacheEnabled(true);
            newPizzaImage.buildDrawingCache();
            Bitmap bitmapa = Bitmap.createScaledBitmap(((BitmapDrawable) newPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapa.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });



        } catch (NullPointerException e) {
            Toast.makeText(AddPizzaActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(AddPizzaActivity.this, "Dodano nową pizzę", Toast.LENGTH_SHORT).show();
 //       appDatabase.pizzaDao().insert(pizza);
        myDatabase.child(newDatabaseReference.getKey()).setValue(pizza);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            cos = data.getData();
            newPizzaImage.setImageURI(cos);
        }
    }

    protected String saveToInternalStorage(Bitmap bitmapImage, String id) {
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
