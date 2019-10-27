package com.example.aplikacjaism.userpackage;

import android.os.Bundle;

import com.example.aplikacjaism.DataStatus;
import com.example.aplikacjaism.FirebaseDatabaseHelper;
import com.example.aplikacjaism.R;
import com.example.aplikacjaism.pizzapackage.Pizza;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerUserViewId);
            new FirebaseDatabaseHelper().readUser(new DataStatus() {
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
                    new RecyclerViewUser().setConfig(mRecyclerViewList, UserListActivity.this, users, keys);
                }
            });
        }
    }

}
