package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        LinearLayout cartContainer = view.findViewById(R.id.cart_item_container); // 你可以在 XML 中加这个

        List<CartItem> items = CartManager.getCartItems();
        for (CartItem item : items) {
            TextView tv = new TextView(getContext());
            tv.setText(item.getName() + " - $" + item.getPrice());
            cartContainer.addView(tv);
        }

        TextView subtotalView = view.findViewById(R.id.subtotalText);
        subtotalView.setText("$" + String.format("%.2f", CartManager.calculateSubtotal()));

        TextView totalView = view.findViewById(R.id.total_price_text);
        totalView.setText("$" + String.format("%.2f", CartManager.calculateTotal()));

        return view;
    }
}
