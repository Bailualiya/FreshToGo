package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        //  购物项容器
        LinearLayout cartContainer = view.findViewById(R.id.cart_item_container);
        List<CartItem> items = CartManager.getCartItems();
        for (CartItem item : items) {
            TextView tv = new TextView(getContext());
            tv.setText(item.getName() + " - $" + item.getPrice());
            tv.setTextSize(16);
            cartContainer.addView(tv);
        }

        //  设置价格
        TextView subtotalView = view.findViewById(R.id.subtotalText);
        subtotalView.setText("$" + String.format("%.2f", CartManager.calculateSubtotal()));

        TextView totalView = view.findViewById(R.id.total_price_text);
        totalView.setText("$" + String.format("%.2f", CartManager.calculateTotal()));

        //  地址跳转地图功能
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

        //  接收地图返回的地址
        getParentFragmentManager().setFragmentResultListener("map_address_result", this, (key, bundle) -> {
            String newAddress = bundle.getString("selected_address");
            if (newAddress != null) {
                addressText.setText("Delivery to: " + newAddress);
            }
        });
        View checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            CheckoutFragment checkoutFragment = new CheckoutFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, checkoutFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;

    }
}
