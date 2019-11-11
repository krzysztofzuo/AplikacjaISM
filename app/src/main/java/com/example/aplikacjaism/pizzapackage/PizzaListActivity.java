package com.example.aplikacjaism.pizzapackage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.trackingpackage.MapsActivity;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.trackingpackage.OrderListActivity;
import com.example.aplikacjaism.userpackage.SignInActivity;
import com.example.aplikacjaism.userpackage.User;
import com.example.aplikacjaism.userpackage.UserListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class PizzaListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUser;

    private boolean admin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pizza_list_activity);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
            new FirebaseDatabaseHelper().readPizzas(new DataStatus() {
                @Override
                public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {
                    new RecyclerViewPizza().setConfig(mRecyclerView, PizzaListActivity.this, pizzas, keys);
                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }

                @Override
                public void DataUsersIsLoaded(List<User> users, List<String> keys) {

                }

                @Override
                public void DataOrdersIsLoaded(List<Order> orders, List<String> keys) {

                }
            });


            final FloatingActionButton addButton = findViewById(R.id.goToAddActivityButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PizzaListActivity.this, AddPizzaActivity.class);
                    startActivity(intent);
                }
            });

            mDatabase = FirebaseDatabase.getInstance();
            mUser = mDatabase.getReference("users");
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.getKey().equals(user.getUid())) {
                            admin = keyNode.getValue(User.class).getAdmin();
                        }
                    }
                    if (admin) {
                        addButton.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getMenuInflater().inflate(R.menu.menu, menu);
            menu.getItem(0).setVisible(true);

            mDatabase = FirebaseDatabase.getInstance();
            mUser = mDatabase.getReference("users");
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if (keyNode.getKey().equals(user.getUid())) {
                            admin = keyNode.getValue(User.class).getAdmin();
                        }
                    }
                    if (admin) {
                        menu.getItem(1).setVisible(true);
                        menu.getItem(3).setVisible(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return true;
        } else return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            }
            case R.id.users: {
                startActivity(new Intent(this, UserListActivity.class));
                return true;
            }
            case R.id.maps: {
                startActivity(new Intent(this, MapsActivity.class));
            }
            case R.id.orders: {
                startActivity(new Intent(this, OrderListActivity.class));
            }

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FirebaseDatabaseHelper().readPizzas(new DataStatus() {
            @Override
            public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {
                new RecyclerViewPizza().setConfig(mRecyclerView, PizzaListActivity.this, pizzas, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }

            @Override
            public void DataUsersIsLoaded(List<User> users, List<String> keys) {

            }

            @Override
            public void DataOrdersIsLoaded(List<Order> orders, List<String> keys) {

            }
        });
    }
}
