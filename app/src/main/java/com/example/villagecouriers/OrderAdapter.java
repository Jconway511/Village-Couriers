package com.example.villagecouriers;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
// created by Jason Conway
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.itemNameTextView.setText(order.getItemName());
        holder.itemQuantityTextView.setText(String.valueOf(order.getQuantity()));
        holder.itemPriceTextView.setText(String.valueOf(order.getPrice()));
        holder.textViewOrder.setText("Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId());
        System.out.println("Binding order: " +order.getItemName());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView;
        public TextView itemQuantityTextView;
        public TextView itemPriceTextView;
        public TextView textViewOrder;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrder = itemView.findViewById(R.id.textViewOrder);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            itemQuantityTextView = itemView.findViewById(R.id.item_quantity);
        }
    }
}
