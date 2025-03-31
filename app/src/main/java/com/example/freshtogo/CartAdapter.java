package com.example.freshtogo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.room.Room;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }
    public interface OnCartChangedListener {
        void onCartUpdated();
    }
    public void setOnCartChangedListener(OnCartChangedListener listener) {
        this.cartChangedListener = listener;
    }
    private OnCartChangedListener cartChangedListener;
    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productImage.setImageResource(item.imageResId);
        holder.productName.setText(item.name);
        holder.productCategory.setText(item.category);
        holder.productPrice.setText("$" + String.format("%.2f", item.price));
        holder.productQuantity.setText("Qty: " + item.quantity);


        holder.removeButton.setVisibility(View.VISIBLE);
        holder.removeButton.setOnClickListener(v -> {
            // Get the current item to remove
            CartItem itemToRemove = cartItems.get(position);

            // Remove from Room database
            AppDatabase db = Room.databaseBuilder(holder.itemView.getContext(),
                            AppDatabase.class, "fresh_to_go_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            db.cartDao().deleteById(itemToRemove.id);

            // Remove from list and update UI
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            if (cartChangedListener != null) {
                cartChangedListener.onCartUpdated();
            }

        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productCategory, productPrice, productQuantity;
        ImageButton removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartItemImage);
            productName = itemView.findViewById(R.id.cartItemName);
            productCategory = itemView.findViewById(R.id.cartItemCategory);
            productPrice = itemView.findViewById(R.id.cartItemPrice);
            productQuantity = itemView.findViewById(R.id.cartItemQuantity); // ✅ Corrected
            removeButton = itemView.findViewById(R.id.removeItemButton);
        }
    }
}
