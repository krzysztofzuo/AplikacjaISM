package com.example.aplikacjaism.trackingpackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacjaism.R;

import java.util.List;

public class RecyclerViewOrder {
    private Context mContext;
    private OrdersAdapter mOrdersAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Order> orders, List<String> keys) {
        mContext = context;
        mOrdersAdapter = new OrdersAdapter(orders, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mOrdersAdapter);
    }

    class OrderItemView extends RecyclerView.ViewHolder {
        private TextView mOrderDay;
        private TextView mOrderHour;
        private TextView mOrderMinute;
        private TextView mOrderPizza;

        private String key;


        public OrderItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.order_row, parent, false));
            mOrderDay = (TextView) itemView.findViewById(R.id.orderDate);
            mOrderHour = (TextView) itemView.findViewById(R.id.orderHour);
            mOrderMinute = (TextView) itemView.findViewById(R.id.orderMinute);
            mOrderPizza = (TextView) itemView.findViewById(R.id.pizzaOrderName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ShareLocationActivity.class);
                    intent.putExtra("key", key);
                    mContext.startActivity(intent);
                }
            });
        }


        public void bind(Order order, String key) {
            mOrderPizza.setText(order.getPizza().getPizzaName());
            mOrderDay.setText("" + order.getDate().getDate());
            mOrderHour.setText("" + order.getDate().getHours());
            mOrderMinute.setText(":" + order.getDate().getMinutes());

            this.key = key;
        }
    }

    class OrdersAdapter extends RecyclerView.Adapter<OrderItemView> {
        private List<Order> orderList;
        private List<String> mKeys;

        public OrdersAdapter(List<Order> mOrdersList, List<String> mKeys) {
            this.orderList = mOrdersList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderItemView holder, int position) {
            holder.bind(orderList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }
    }
}
