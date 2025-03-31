package com.example.freshtogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private AppDatabase db;
    private CartDao cartDao;
    private SessionManager sessionManager;

    public ExploreAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

        db = Room.databaseBuilder(context, AppDatabase.class, "fresh_to_go_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        cartDao = db.cartDao();
        sessionManager = new SessionManager(context);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.productImage.setImageResource(p.getImageResId());
        holder.productName.setText(p.getName());
        holder.productCategory.setText(p.getCategory());
        holder.productPrice.setText("$" + p.getPrice());

        holder.addToCartButton.setOnClickListener(v -> {
            int userId = sessionManager.getUserId();
            if (userId == -1) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build the new cart item
            CartItem item = new CartItem();
            item.userId = userId;
            item.name = p.getName();
            item.category = p.getCategory();
            item.price = p.getPrice();
            item.imageResId = p.getImageResId();
            item.quantity = 1;

            // Check if this item already exists
            CartItem existing = cartDao.getItemByName(userId, item.name);

            if (existing != null) {
                // Increment quantity
                cartDao.incrementQuantity(existing.id);
                Toast.makeText(context, "Increased quantity of " + item.name, Toast.LENGTH_SHORT).show();
            } else {
                // Add new item
                cartDao.insertItem(item);
                Toast.makeText(context, item.name + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productCategory, productPrice;
        Button addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
