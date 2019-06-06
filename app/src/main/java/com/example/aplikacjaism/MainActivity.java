package com.example.aplikacjaism;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements przekazable {
    private final int ADD_ACTIVITY = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Model<String, String, Integer>> listaElementow = new ArrayList<>();
        for (int i = 0; i < MyApp.size; i++) {
            listaElementow.add(new Model<>(MyApp.pizzaName[i], MyApp.pizzaDescription[i], MyApp.pizzaImage.getResourceId(i, 0)));
        }

        mAdapter = new MyAdapter(listaElementow, this);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton addButton = findViewById(R.id.goToAddActivityButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPizzaActivity.class);
                startActivityForResult(intent, ADD_ACTIVITY);
            }
        });

    }

    @Override
    public void getPosition(int idOfRow) {
        Intent intent = new Intent(this, Description.class);
        intent.putExtra("id", idOfRow);
        startActivity(intent);
    }
}
