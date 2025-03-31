package com.example.freshtogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView subtotalText, deliveryFeeText, totalText;
    private Button checkoutButton;
    private CartAdapter adapter;

    private AppDatabase db;
    private CartDao cartDao;
    private SessionManager sessionManager;

    private double deliveryFee = 2.99;
    private List<CartItem> cartItems;

    public CartFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.cartRecyclerView);
        subtotalText = view.findViewById(R.id.subtotalText);
        deliveryFeeText = view.findViewById(R.id.deliveryFeeText);
        totalText = view.findViewById(R.id.totalText);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "fresh_to_go_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        cartDao = db.cartDao();
        sessionManager = new SessionManager(requireContext());

        int userId = sessionManager.getUserId();
        cartItems = cartDao.getCartItems(userId);

        adapter = new CartAdapter(cartItems);
        adapter.setOnCartChangedListener(() -> updateTotals(cartItems));

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        updatePriceSummary();

        checkoutButton.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            } else {
                double total = calculateTotal();
                Intent intent = new Intent(requireContext(), PaymentActivity.class);
                intent.putExtra("totalAmount", total);
                startActivity(intent);
            }
        });
    }

    private void updateTotals(List<CartItem> items) {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.price * item.quantity;
        }

        double total = subtotal + deliveryFee;

        subtotalText.setText(String.format("$%.2f", subtotal));
        deliveryFeeText.setText(String.format("$%.2f", deliveryFee));
        totalText.setText(String.format("$%.2f", total));
    }

    private double calculateTotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.price * item.quantity;
        }
        return subtotal + deliveryFee;
    }

    private void updatePriceSummary() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.price * item.quantity;
        }

        subtotalText.setText("$" + String.format("%.2f", subtotal));
        deliveryFeeText.setText("$" + String.format("%.2f", deliveryFee));
        totalText.setText("$" + String.format("%.2f", subtotal + deliveryFee));
    }
}
