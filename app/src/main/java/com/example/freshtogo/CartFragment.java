package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import com.example.freshtogo.CheckoutFragment;

import java.util.List;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private EditText addressInput;  // Address input field

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Address input field
        addressInput = view.findViewById(R.id.address_input);

        // Cart item container (list of cart items)
        LinearLayout cartContainer = view.findViewById(R.id.cart_item_container);
        List<CartItem> items = CartManager.getCartItems(getContext());

        // Display cart items
        for (CartItem item : items) {
            TextView tv = new TextView(getContext());
            tv.setText(item.getName() + " - $" + item.getPrice() + " x " + item.getQuantity());
            tv.setTextSize(16);
            cartContainer.addView(tv);
        }

        // Set subtotal and total
        TextView subtotalView = view.findViewById(R.id.subtotalText);
        subtotalView.setText("$" + String.format("%.2f", CartManager.calculateSubtotal(getContext())));

        TextView totalView = view.findViewById(R.id.total_price_text);
        totalView.setText("$" + String.format("%.2f", CartManager.calculateTotal(getContext())));

        // Set up "Proceed to Checkout" button
        TextView checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            String address = addressInput.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
            } else {
                // Save the address to the user's file
                saveAddress(address);
                // Proceed to the checkout page (navigate to CheckoutFragment)
                navigateToCheckout();
            }
        });

        return view;
    }

    private void navigateToCheckout() {
        // You can replace the current fragment with the checkout fragment
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, checkoutFragment); // Replace with your CheckoutFragment
        transaction.addToBackStack(null); // Optionally add the transaction to the back stack
        transaction.commit(); // Commit the transaction
    }

    private void saveAddress(String address) {
        String user = SessionManager.getCurrentUser(getContext());
        if (user == null) return;

        File file = new File(getContext().getFilesDir(), user + ".txt");
        List<String> lines = new ArrayList<>();
        boolean addressFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean insideCart = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("CART:")) {
                    insideCart = true;
                    lines.add("CART:");
                } else if (line.equals("ORDER_HISTORY:") || line.equals("CARDS:")) {
                    insideCart = false;
                    lines.add(line);  // Preserve other sections
                } else if (!insideCart) {
                    lines.add(line);  // Preserve non-cart sections
                }

                // If the address section exists, update it
                if (line.startsWith("ADDRESS:")) {
                    lines.add("ADDRESS: " + address); // Update the address
                    addressFound = true;
                }
            }

            // If the address section doesn't exist, add it at the end of the file
            if (!addressFound) {
                lines.add("ADDRESS: " + address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

