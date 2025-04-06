package com.example.freshtogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.*;

public class ProfileFragment extends Fragment {

    private TextView profileUsername, orderText, cardText;
    private Button logoutButton;
    private String currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileUsername = view.findViewById(R.id.profileUsername);
        orderText = view.findViewById(R.id.profileOrders);
        cardText = view.findViewById(R.id.profileCards);
        logoutButton = view.findViewById(R.id.logoutButton);

        currentUser = SessionManager.getCurrentUser(requireContext());
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        profileUsername.setText("Logged in as: " + currentUser);

        File file = new File(requireContext().getFilesDir(), "user_" + currentUser + ".txt");

        if (file.exists()) {
            StringBuilder orderBuilder = new StringBuilder();
            StringBuilder cardBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String section = "";
                while ((line = reader.readLine()) != null) {
                    if (line.equals("ORDER_HISTORY:")) section = "ORDER";
                    else if (line.equals("CARDS:")) section = "CARDS";
                    else if (line.equals("CART:")) section = ""; // skip cart
                    else if (!line.trim().isEmpty()) {
                        switch (section) {
                            case "ORDER":
                                orderBuilder.append("• ").append(line).append("\n");
                                break;
                            case "CARDS":
                                cardBuilder.append("• ").append(line).append("\n");
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
            }

            orderText.setText(orderBuilder.length() > 0 ? orderBuilder.toString() : "No orders yet.");
            cardText.setText(cardBuilder.length() > 0 ? cardBuilder.toString() : "No saved cards.");
        }

        logoutButton.setOnClickListener(v -> {
            SessionManager.clear(requireContext());
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
    }
}
