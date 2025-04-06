package com.example.freshtogo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.*;
import java.util.*;

public class ProductDetailFragment extends Fragment {

    private ImageView imageProduct;
    private TextView textName, textPrice, textDescription, textQuantity;
    private Button buttonPlus, buttonMinus, buttonAddToCart;
    private int quantity = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        imageProduct = view.findViewById(R.id.image_product);
        textName = view.findViewById(R.id.text_name);
        textPrice = view.findViewById(R.id.text_price);
        textDescription = view.findViewById(R.id.text_description);
        textQuantity = view.findViewById(R.id.text_quantity);
        buttonPlus = view.findViewById(R.id.button_plus);
        buttonMinus = view.findViewById(R.id.button_minus);
        buttonAddToCart = view.findViewById(R.id.button_add_to_cart);

        Bundle args = getArguments();
        if (args != null) {
            textName.setText(args.getString("name"));
            textPrice.setText("$" + args.getString("price"));
            textDescription.setText(args.getString("description"));
            imageProduct.setImageResource(args.getInt("imageResId"));
        }

        textQuantity.setText(String.valueOf(quantity));

        buttonPlus.setOnClickListener(v -> {
            quantity++;
            textQuantity.setText(String.valueOf(quantity));
        });

        buttonMinus.setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            textQuantity.setText(String.valueOf(quantity));
        });

        buttonAddToCart.setOnClickListener(v -> addToCart());

        return view;
    }

    private void addToCart() {
        String name = getArguments().getString("name");
        String priceStr = getArguments().getString("price");
        double price = Double.parseDouble(priceStr);
        int qty = quantity;

        String currentUser = SessionManager.getCurrentUser(requireContext());
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(requireContext().getFilesDir(), currentUser + ".txt");
        if (!file.exists()) {
            Toast.makeText(getContext(), "User file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean inCart = false, itemFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CART:")) {
                    inCart = true;
                    lines.add(line);
                    continue;
                } else if (line.equals("ORDER_HISTORY:") || line.equals("CARDS:")) {
                    inCart = false;
                }

                if (inCart && line.startsWith(name + " -")) {
                    itemFound = true;
                    String[] parts = line.split(" - ");
                    int existingQty = Integer.parseInt(parts[1]);
                    int newQty = existingQty + qty;
                    lines.add(name + " - " + newQty + " - " + price);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to read cart", Toast.LENGTH_SHORT).show();
            return;
        }

        // If item wasn't found in cart, add it
        if (!itemFound) {
            int cartIndex = lines.indexOf("CART:");
            if (cartIndex != -1) {
                lines.add(cartIndex + 1, name + " - " + qty + " - " + price);
            }
        }

        // Write back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            Toast.makeText(getContext(), name + " added to cart", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to update cart", Toast.LENGTH_SHORT).show();
        }
    }
}
