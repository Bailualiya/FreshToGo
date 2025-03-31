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

        Button addToCartButton = view.findViewById(R.id.button_add_to_cart);
        addToCartButton.setOnClickListener(v -> {
            String name = getArguments().getString("name");
            String price = getArguments().getString("price");

            // 示例逻辑：你也可以加购物车数组、写数据库等
            Toast.makeText(getContext(), name + " added to cart!", Toast.LENGTH_SHORT).show();

            // TODO: 你可以把它加入购物车列表或保存到 SharedPreferences
        });


        return view;
    }
}
