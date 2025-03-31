package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //  绑定每个商品卡片并设置点击事件
        setupClick(view, R.id.card_blueberry, "Fresh Blueberries", "4.99",
                "Hand-picked local blueberries, 100% organic.", R.drawable.blueberry);

        setupClick(view, R.id.card_egg, "Organic Eggs", "5.99",
                "Farm-fresh organic eggs from free-range chickens.", R.drawable.egg);

        setupClick(view, R.id.card_chicken, "Organic Chicken", "9.99",
                "Locally raised, hormone-free organic chicken.", R.drawable.chicken);

        setupClick(view, R.id.card_cheese, "Farm Cheese", "6.49",
                "Creamy cheese made from fresh cow’s milk.", R.drawable.cheese);

        setupClick(view, R.id.card_grape, "Organic Grapes", "4.29",
                "Sweet, seedless, freshly harvested grapes.", R.drawable.grape);

        setupClick(view, R.id.card_pumpkin, "Local Pumpkin", "3.79",
                "Golden pumpkins perfect for soups or pies.", R.drawable.pumpkin);

        return view;
    }

    //  封装跳转逻辑的方法
    private void setupClick(View view, int cardId, String name, String price,
                            String description, int imageResId) {

        LinearLayout card = view.findViewById(cardId);
        card.setOnClickListener(v -> {
            Fragment fragment = new ProductDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("price", price);
            bundle.putString("description", description);
            bundle.putInt("imageResId", imageResId);
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
