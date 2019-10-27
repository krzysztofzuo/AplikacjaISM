package com.example.aplikacjaism.pizzapackage;

import android.content.Intent;
import android.os.Bundle;

import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.userpackage.User;
import com.example.aplikacjaism.userpackage.UserListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.List;

public class PizzaListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_list_activity);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

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
            });


            FloatingActionButton addButton = findViewById(R.id.goToAddActivityButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PizzaListActivity.this, AddPizzaActivity.class);
                    startActivity(intent);
                }
            });

            Button usersButton = findViewById(R.id.usersButton);
            usersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PizzaListActivity.this, UserListActivity.class);
                    startActivity(intent);
                }
            });


        } else {
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
//        appDatabase = AppDatabase.getDatabase(this);
//        listaElementow = appDatabase.pizzaDao().getAll();
//        mAdapter = new MyAdapter(listaElementow, this);
//        recyclerView.setAdapter(mAdapter);

    }
}
