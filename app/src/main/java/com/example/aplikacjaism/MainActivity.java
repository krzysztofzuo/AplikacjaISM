package com.example.aplikacjaism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

        mAdapter = new MyAdapter(listaElementow);
        recyclerView.setAdapter(mAdapter);

    }
}
