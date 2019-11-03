package com.example.aplikacjaism;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.aplikacjaism.pizzapackage.Pizza;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.userpackage.User;
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
    private DatabaseReference mReferenceUser;
    private DatabaseReference mReferenceOrders;

    private List<Pizza> pizzas = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePizza = mDatabase.getReference("pizzas");
        mReferenceUser = mDatabase.getReference("users");
        mReferenceOrders = mDatabase.getReference("orders");
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

    public void deletePizza(final String key, final DataStatus dataStatus) {
        mReferencePizza.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                        // Create a storage reference from our app
                        StorageReference storageRef = mStorage.getReference();
                        // Create a reference to the file to delete
                        StorageReference imageRef = storageRef.child("zdjecia/" + key + ".jpg");
                        // Delete the file
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                            }
                        });
                    }
                });
    }

    public void readUser(final DataStatus dataStatus) {
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataUsersIsLoaded(users, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readOrders(final DataStatus dataStatus) {
        mReferenceOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Order order = keyNode.getValue(Order.class);
                    orders.add(order);
                }
                dataStatus.DataOrdersIsLoaded(orders, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
