package com.example.aplikacjaism;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePizza;
    private List<Pizza> pizzas = new ArrayList<>();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public interface DataStatus {
        void DataIsLoaded(List<Pizza> pizzas, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePizza = mDatabase.getReference("pizzas");
    }

    public void readPizzas(final DataStatus dataStatus) {
        mReferencePizza.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pizzas.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Pizza pizza = keyNode.getValue(Pizza.class);
                    pizzas.add(pizza);
                }
                dataStatus.DataIsLoaded(pizzas, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPizza(ImageView mPizzaImage, Pizza pizza, final DataStatus dataStatus) {
        String key = mReferencePizza.push().getKey();
        mReferencePizza.child(key).setValue(pizza)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });

        // Create a storage reference from our app
        StorageReference storageRef = mStorage.getReference();
// Create a reference to "image.jpg"
        StorageReference pizzasRef = storageRef.child("zdjecia/" + key + ".jpg");
// Create a reference to 'images/image.jpg'
        StorageReference imageRef = storageRef.child("images/zdjecia/" + key + ".jpg");
// While the file names are the same, the references point to different files
        pizzasRef.getName().equals(imageRef.getName());    // true
        pizzasRef.getPath().equals(imageRef.getPath());    // false
// Get the data from an ImageView as bytes
        mPizzaImage.setDrawingCacheEnabled(true);
        mPizzaImage.buildDrawingCache();
        Bitmap bitmapa = Bitmap.createScaledBitmap(((BitmapDrawable) mPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapa.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = pizzasRef.putBytes(data);
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
    }

    public void updatePizza(ImageView mPizzaImage, String key, Pizza pizza, final DataStatus dataStatus) {
        mReferencePizza.child(key).setValue(pizza)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
        // Create a storage reference from our app
        StorageReference storageRef = mStorage.getReference();
// Create a reference to "image.jpg"
        StorageReference pizzasRef = storageRef.child("zdjecia/" + key + ".jpg");
// Create a reference to 'images/image.jpg'
        StorageReference imageRef = storageRef.child("images/zdjecia/" + key + ".jpg");
// While the file names are the same, the references point to different files
        pizzasRef.getName().equals(imageRef.getName());    // true
        pizzasRef.getPath().equals(imageRef.getPath());    // false
// Get the data from an ImageView as bytes
        mPizzaImage.setDrawingCacheEnabled(true);
        mPizzaImage.buildDrawingCache();
        Bitmap bitmapa = Bitmap.createScaledBitmap(((BitmapDrawable) mPizzaImage.getDrawable()).getBitmap(), 300, 300, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapa.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = pizzasRef.putBytes(data);
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
    }

    public void deletePizza(String key, final DataStatus dataStatus) {
        mReferencePizza.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}
