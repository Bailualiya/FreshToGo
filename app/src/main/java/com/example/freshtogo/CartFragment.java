package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class CartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Cart Items Container
        LinearLayout cartContainer = view.findViewById(R.id.cart_item_container);
        List<CartItem> items = CartManager.getCartItems();

        for (CartItem item : items) {
            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(16, 16, 16, 16);
            itemLayout.setBackgroundResource(R.color.white);

            // Item Name and Dynamic Price
            LinearLayout itemInfoLayout = new LinearLayout(getContext());
            itemInfoLayout.setOrientation(LinearLayout.VERTICAL);
            itemInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            itemInfoLayout.setPadding(0, 0, 0, 8); // Add space between price and name

            TextView itemName = new TextView(getContext());
            itemName.setText(item.getName());
            itemName.setTextSize(16);
            itemName.setTextColor(getResources().getColor(R.color.black));

            TextView itemPrice = new TextView(getContext());
            // Display price dynamically with 2 decimal places
            itemPrice.setText("$" + String.format("%.2f", item.getPrice() * item.getQuantity()));
            itemPrice.setTextSize(14);
            itemPrice.setTextColor(getResources().getColor(R.color.gray));

            itemInfoLayout.addView(itemName);
            itemInfoLayout.addView(itemPrice); // Add price below name

            // Quantity Input and Update Button
            LinearLayout quantityLayout = new LinearLayout(getContext());
            quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            quantityParams.setMargins(0, 0, 16, 0);
            quantityLayout.setLayoutParams(quantityParams);
            quantityLayout.setGravity(Gravity.START);

            Button decrementButton = new Button(getContext());
            decrementButton.setText("-");
            decrementButton.setTextSize(12);
            decrementButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

            EditText quantityInput = new EditText(getContext());
            quantityInput.setText(String.valueOf(item.getQuantity()));
            quantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            quantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            quantityInput.setPadding(8, 0, 8, 0);

            Button incrementButton = new Button(getContext());
            incrementButton.setText("+");
            incrementButton.setTextSize(12);
            incrementButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

            incrementButton.setOnClickListener(v -> {
                int quantity = Integer.parseInt(quantityInput.getText().toString()) + 1;
                quantityInput.setText(String.valueOf(quantity));
                item.setQuantity(quantity);
                itemPrice.setText("$" + String.format("%.2f", item.getPrice() * item.getQuantity()));
                updateTotal(view);
            });

            decrementButton.setOnClickListener(v -> {
                int quantity = Integer.parseInt(quantityInput.getText().toString());
                if (quantity > 1) {
                    quantityInput.setText(String.valueOf(quantity - 1));
                    item.setQuantity(quantity - 1);
                    itemPrice.setText("$" + String.format("%.2f", item.getPrice() * item.getQuantity()));
                    updateTotal(view);
                }
            });

            // Delete , Trash Icon
            ImageView deleteButton = new ImageView(getContext());
            deleteButton.setImageResource(R.drawable.ic_trash);
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(40, 40);
            deleteButtonParams.setMargins(16, 0, 0, 0); // Space between quantity and delete button
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setOnClickListener(v -> {
                CartManager.removeItem(item);
                cartContainer.removeView(itemLayout);
                updateTotal(view);
            });

            // Add Views to Layout
            quantityLayout.addView(decrementButton);
            quantityLayout.addView(quantityInput);
            quantityLayout.addView(incrementButton);
            itemLayout.addView(itemInfoLayout);
            itemLayout.addView(quantityLayout);
            itemLayout.addView(deleteButton);

            cartContainer.addView(itemLayout);
        }


        updateTotal(view);

        // Address functionality (as before)
        LinearLayout addressLayout = view.findViewById(R.id.delivery_address_layout);
        TextView addressText = view.findViewById(R.id.delivery_address_text);

        addressLayout.setOnClickListener(v -> {
            ExploreFragment exploreFragment = new ExploreFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, exploreFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Receive the map address
        getParentFragmentManager().setFragmentResultListener("map_address_result", this, (key, bundle) -> {
            String newAddress = bundle.getString("selected_address");
            if (newAddress != null) {
                addressText.setText("Delivery to: " + newAddress);
            }
        });

        // Checkout Button
        View checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                CheckoutFragment checkoutFragment = new CheckoutFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, checkoutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void updateTotal(View view) {

        TextView subtotalText = view.findViewById(R.id.subtotalText);
        TextView totalPriceText = view.findViewById(R.id.total_price_text);

        double subtotal = CartManager.calculateSubtotal();
        double total = subtotal + CartManager.calculateDeliveryFee();

        subtotalText.setText("$" + String.format("%.2f", subtotal));
        totalPriceText.setText("$" + String.format("%.2f", total));
    }
}
