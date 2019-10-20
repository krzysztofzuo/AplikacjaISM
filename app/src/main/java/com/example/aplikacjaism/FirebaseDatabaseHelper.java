package com.example.aplikacjaism;

import androidx.annotation.NonNull;

import com.example.aplikacjaism.database.Pizza;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePizza;
    private List<Pizza> pizzas = new ArrayList<>();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public interface  DataStatus{
        void DataIsLoaded(List<Pizza> pizzas, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePizza = mDatabase.getReference("pizzas");
    }

    public void readPizzas(final DataStatus dataStatus){
        mReferencePizza.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pizzas.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
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

}
