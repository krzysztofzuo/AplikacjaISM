package com.example.aplikacjaism;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Model<String, String, Integer>> listaElementow = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pizzaName;
        public TextView pizzaDescription;
        public ImageView pizzaImage;

        public MyViewHolder(LinearLayout v) {
            super(v);
            pizzaName = v.findViewById(R.id.pizzaName);
            pizzaImage = v.findViewById(R.id.pizzaImage);
            pizzaDescription = v.findViewById(R.id.pizzaDescription);
        }
    }

    public MyAdapter(ArrayList<Model<String, String, Integer>> listaElementow) {
        this.listaElementow = listaElementow;
    }

    //jak ma wygladac pojedynczy row layout
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //wypelnienie danymi
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.pizzaName.setText(listaElementow.get(position).pizzaName);
        holder.pizzaDescription.setText(listaElementow.get(position).pizzaDescription);
        holder.pizzaImage.setImageResource(listaElementow.get(position).pizzaImage);
    }

    @Override
    public int getItemCount() {
        return listaElementow.size();
    }
}