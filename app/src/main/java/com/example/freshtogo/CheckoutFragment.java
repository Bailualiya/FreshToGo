package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutFragment extends Fragment {

    private LinearLayout cardListContainer;
    private Button addCardButton;
    private Button payNowButton;
    private EditText securityCodeInput;
    private List<String> savedCards = new ArrayList<>();
    private String selectedCard = null;
    private String selectedCardSecurityCode = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        cardListContainer = view.findViewById(R.id.card_list_container);
        addCardButton = view.findViewById(R.id.add_card_button);
        payNowButton = view.findViewById(R.id.pay_now_button);
        securityCodeInput = view.findViewById(R.id.security_code_input);

        // Load saved cards from the user's file and display them
        loadSavedCards();

        // Set up Add Card Button
        addCardButton.setOnClickListener(v -> {
            // Show dialog or new screen to add a new card
            showAddNewCardDialog();
        });

        // Set up Pay Now Button
        payNowButton.setOnClickListener(v -> {
            String securityCode = securityCodeInput.getText().toString().trim();
            if (securityCode.isEmpty()) {
                Toast.makeText(getContext(), "Please enter security code", Toast.LENGTH_SHORT).show();
            } else {
                // Validate the security code and process the payment dynamically
                processPayment(securityCode);
            }
        });

        return view;
    }

    // Load saved cards from the user's file
    private void loadSavedCards() {
        String currentUser = SessionManager.getCurrentUser(getContext());
        if (currentUser == null) return;

        File file = new File(getContext().getFilesDir(), currentUser + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean insideCardsSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CARDS:")) {
                    insideCardsSection = true;
                } else if (insideCardsSection && !line.trim().isEmpty()) {
                    savedCards.add(line);  // Add card details to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dynamically add buttons for each saved card
        for (String card : savedCards) {
            Button cardButton = new Button(getContext());
            cardButton.setText("Card: " + card.substring(card.length() - 4)); // Display last 4 digits
            cardButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Selected Card: " + card, Toast.LENGTH_SHORT).show();
                selectedCard = card;  // Store the selected card
                // You would extract the corresponding security code for the card
                selectedCardSecurityCode = getSecurityCodeForCard(card); // Get the security code for the selected card
            });
            cardListContainer.addView(cardButton);
        }
    }

    // Get security code associated with the selected card
    private String getSecurityCodeForCard(String card) {
        String securityCode = null;
        String currentUser = SessionManager.getCurrentUser(getContext());
        if (currentUser == null) return securityCode;

        File file = new File(getContext().getFilesDir(), currentUser + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean insideCardsSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CARDS:")) {
                    insideCardsSection = true;
                } else if (insideCardsSection && line.contains(card)) {
                    if (line.contains("SECURITY_CODE:")) {
                        securityCode = line.replace("SECURITY_CODE: ", "").trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return securityCode;
    }

    // Show dialog or fragment to add a new card
    private void showAddNewCardDialog() {
        // Implement logic to show a new fragment or dialog for adding card details
        Toast.makeText(getContext(), "Add New Card clicked", Toast.LENGTH_SHORT).show();

        // Assuming you open a dialog or another screen to add the card details
        // Here you would gather the user's card number, security code, etc.
        String cardNumber = "1234567890123456";  // Example input
        String securityCode = "123";  // Example input
        saveNewCard(cardNumber, securityCode);
    }

    // Save the new card to the user's file
    private void saveNewCard(String cardNumber, String securityCode) {
        String currentUser = SessionManager.getCurrentUser(getContext());
        if (currentUser == null) return;

        File file = new File(getContext().getFilesDir(), currentUser + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("CARD: " + cardNumber + "\n");
            writer.write("SECURITY_CODE: " + securityCode + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the new card dynamically to the UI without reloading the entire list
        Button newCardButton = new Button(getContext());
        newCardButton.setText("Card: " + cardNumber.substring(cardNumber.length() - 4));  // Show last 4 digits
        newCardButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Selected Card: " + cardNumber, Toast.LENGTH_SHORT).show();
            selectedCard = cardNumber;  // Store the selected card
            selectedCardSecurityCode = securityCode;  // Store the selected card's security code
        });

        // Add the new card to the card list container
        cardListContainer.addView(newCardButton);
    }

    // Process payment with the entered security code
    private void processPayment(String securityCode) {
        if (selectedCard != null && selectedCardSecurityCode != null) {
            if (securityCode.equals(selectedCardSecurityCode)) {
                // Clear the cart and save the order to history
                CartManager.clearCart(getContext());
                saveOrderToHistory(CartManager.getCartItems(getContext()));  // Save cart items to order history
                Toast.makeText(getContext(), "Payment successful!", Toast.LENGTH_SHORT).show();
                // Optionally navigate to another fragment or activity (e.g., to order confirmation)
            } else {
                Toast.makeText(getContext(), "Invalid security code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please select a card", Toast.LENGTH_SHORT).show();
        }
    }

    // Save the order to history (user file)
    private void saveOrderToHistory(List<CartItem> cartItems) {
        String currentUser = SessionManager.getCurrentUser(getContext());
        if (currentUser == null) return;

        File file = new File(getContext().getFilesDir(), currentUser + ".txt");
        List<String> lines = new ArrayList<>();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save the order in order history with a timestamp
        lines.add("ORDER_HISTORY:");
        lines.add("Date: " + currentDate);
        lines.add("Total: " + CartManager.calculateTotal(getContext()));

        // Add each cart item to the order history
        for (CartItem item : cartItems) {
            lines.add(item.getName() + " - " + item.getQuantity() + " x $" + item.getPrice());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
