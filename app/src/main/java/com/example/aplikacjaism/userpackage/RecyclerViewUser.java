package com.example.aplikacjaism.userpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aplikacjaism.R;

import java.util.List;

public class RecyclerViewUser {
    private Context mContext;
    private UsersAdapter mUsersAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<User> users, List<String> keys) {
        mContext = context;
        mUsersAdapter = new UsersAdapter(users, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mUsersAdapter);
    }

    class UsersItemView extends RecyclerView.ViewHolder {
        private TextView mUserEmail;
        private TextView mUserId;
        private CheckBox mUserAdmin;

        public UsersItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.user_row, parent, false));

            mUserEmail = (TextView) itemView.findViewById(R.id.userEmail);
            mUserId = (TextView) itemView.findViewById(R.id.userId);
            mUserAdmin = (CheckBox) itemView.findViewById(R.id.adminCheckBox);

        }

        public void bind(User user, String key) {
            mUserEmail.setText(user.getEmail());
            mUserId.setText(key);
            mUserAdmin.setChecked(user.getAdmin());
        }
    }

    class UsersAdapter extends RecyclerView.Adapter<UsersItemView> {
        private List<User> userList;
        private List<String> mKeys;

        public UsersAdapter(List<User> mUserslist, List<String> mKeys) {
            this.userList = mUserslist;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public UsersItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UsersItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UsersItemView holder, int position) {
            holder.bind(userList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }
}
