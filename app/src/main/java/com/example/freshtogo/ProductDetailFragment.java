package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        buttonAddToCart.setOnClickListener(v -> {
            Toast.makeText(getContext(),
                    quantity + " item(s) added to cart!",
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
