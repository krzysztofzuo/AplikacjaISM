package com.example.aplikacjaism;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.aplikacjaism.database.AppDatabase;
import com.example.aplikacjaism.database.Pizza;

import java.util.List;

public class MainActivity extends AppCompatActivity implements przekazable {
    private final int ADD_ACTIVITY = 1;
    private final int DELETE_ACTIVITY = 2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase appDatabase;
    List<Pizza> listaElementow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_ACTIVITY) {
            listaElementow.add(appDatabase.pizzaDao().getById(appDatabase.pizzaDao().size()));
            mAdapter.notifyItemInserted(appDatabase.pizzaDao().size() - 1);
            recyclerView.scrollToPosition(listaElementow.size() - 1);
        }
        if (requestCode == DELETE_ACTIVITY) {
        }
    }

    @Override
    public void goToDescription(int idOfRow) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("id", idOfRow);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appDatabase = AppDatabase.getDatabase(this);

        listaElementow = appDatabase.pizzaDao().getAll();

        mAdapter = new MyAdapter(listaElementow, this);
        recyclerView.setAdapter(mAdapter);

    }
}
