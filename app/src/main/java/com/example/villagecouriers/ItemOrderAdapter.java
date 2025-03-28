package com.example.villagecouriers;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ViewHolder>{
    private List<ItemOrder> itemOrderList;

    public ItemOrderAdapter(List<ItemOrder> itemOrderList) {
        this.itemOrderList = itemOrderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemOrder itemOrder = itemOrderList.get(position);
        holder.itemName.setText(itemOrder.getItem_name());
        holder.itemQuantity.setText("Quantity: " + itemOrder.getItem_quantity());
        holder.itemPrice.setText("Price: " + itemOrder.getItem_price());
    }

    @Override
    public int getItemCount() {
        return itemOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemPrice = itemView.findViewById(R.id.item_price);

        }
    }
}
