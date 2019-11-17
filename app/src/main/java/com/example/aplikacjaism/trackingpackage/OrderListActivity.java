package com.example.aplikacjaism.trackingpackage;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.pizzapackage.Pizza;
import com.example.aplikacjaism.userpackage.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.order_list_activity);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerOrderViewId);
            new FirebaseDatabaseHelper().readOrders(new DataStatus() {
                @Override
                public void DataIsLoaded(List<Pizza> pizzas, List<String> keys) {

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
                    new RecyclerViewOrder().setConfig(mRecyclerViewList, OrderListActivity.this, orders, keys);
                }
            });

        }

    }
}
