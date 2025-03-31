package com.example.freshtogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView profileUsername, orderHistoryDetails, cardDetails;
    private Button logoutButton;

    private AppDatabase db;
    private UserDao userDao;
    private CartDao cartDao;
    private SavedCardDao savedCardDao;
    private SessionManager sessionManager;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileUsername = view.findViewById(R.id.profileUsername);
        orderHistoryDetails = view.findViewById(R.id.orderHistoryDetails);
        cardDetails = view.findViewById(R.id.cardDetails);
        logoutButton = view.findViewById(R.id.logoutButton);

        sessionManager = new SessionManager(requireContext());
        int userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to login page if no user is logged in
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "fresh_to_go_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        userDao = db.userDao();
        cartDao = db.cartDao();
        savedCardDao = db.savedCardDao();

        // Set username
        User user = userDao.getUserById(userId);
        if (user != null) {
            profileUsername.setText("Welcome, " + user.username);
        }

        // Show order history
        List<CartItem> cartItems = cartDao.getCartItems(userId);
        if (!cartItems.isEmpty()) {
            StringBuilder history = new StringBuilder();
            for (CartItem item : cartItems) {
                history.append("• ")
                        .append(item.name).append(" (")
                        .append(item.category).append(") - Qty: ")
                        .append(item.quantity)
                        .append(" - $").append(String.format("%.2f", item.price)).append("\n");
            }
            orderHistoryDetails.setText(history.toString());
        } else {
            orderHistoryDetails.setText("No orders yet.");
        }

        List<SavedCard> cards = savedCardDao.getCardsByUser(userId);
        if (!cards.isEmpty()) {
            StringBuilder cardList = new StringBuilder();
            for (SavedCard card : cards) {
                cardList.append("• ").append(card.cardLabel).append("\n");
            }
            cardDetails.setText(cardList.toString());
        } else {
            cardDetails.setText("No saved cards.");
        }

        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
    }
}
