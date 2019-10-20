package com.example.aplikacjaism;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplikacjaism.database.Pizza;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private PizzasAdapter mPizzasAdapter;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    public void setConfig(RecyclerView recyclerView, Context context, List<Pizza> pizzas, List<String> keys){
        mContext = context;
        mPizzasAdapter = new PizzasAdapter(pizzas, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mPizzasAdapter);
    }

    class PizzaItemView extends RecyclerView.ViewHolder{
        private TextView pizzaName;
        private ImageView pizzaImage;

        private String key;

        public PizzaItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.row, parent, false));

            pizzaName = (TextView) itemView.findViewById(R.id.pizzaName);
            pizzaImage = (ImageView) itemView.findViewById(R.id.pizzaImage);
        }
        public void bind(Pizza pizza, String key){
            pizzaName.setText(pizza.getPizzaName());

            storageRef.child("zdjecia/" + pizza.getPizzaImage() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext)
                            .load(uri)
                            .into(pizzaImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            this.key = key;
        }
    }

    class PizzasAdapter extends  RecyclerView.Adapter<PizzaItemView> {
        private List<Pizza> mPizzalist;
        private List<String> mKeys;

        public PizzasAdapter(List<Pizza> mPizzalist, List<String> mKeys) {
            this.mPizzalist = mPizzalist;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public PizzaItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PizzaItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PizzaItemView holder, int position) {
            holder.bind(mPizzalist.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mPizzalist.size();
        }
    }
}
