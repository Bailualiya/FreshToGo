package com.example.freshtogo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private Map<String, Integer> productMap = new HashMap<>();
    private Map<String, View> cardViews = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Add your products using full lowercase product names
        productMap.put("fresh blueberries", R.id.card_blueberry);
        productMap.put("organic eggs", R.id.card_egg);
        productMap.put("organic chicken", R.id.card_chicken);
        productMap.put("farm cheese", R.id.card_cheese);
        productMap.put("organic grapes", R.id.card_grape);
        productMap.put("local pumpkin", R.id.card_pumpkin);

        // Link cards to click events + store references
        for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
            View card = view.findViewById(entry.getValue());
            cardViews.put(entry.getKey(), card);
            setupClick(card, entry.getKey());
        }

        // Hook up search input
        EditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                for (Map.Entry<String, View> entry : cardViews.entrySet()) {
                    if (entry.getKey().contains(query)) {
                        entry.getValue().setVisibility(View.VISIBLE);
                    } else {
                        entry.getValue().setVisibility(View.GONE);
                    }
                }
            }
        });

        // Today's Menu section
        LinearLayout todaysMenuSection = view.findViewById(R.id.todays_menu_section);
        todaysMenuSection.setOnClickListener(v -> {
            Fragment recipeFragment = new RecipeFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, recipeFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void setupClick(View card, String productName) {
        card.setOnClickListener(v -> {
            Fragment fragment = new ProductDetailFragment();
            Bundle bundle = new Bundle();

            switch (productName) {
                case "fresh blueberries":
                    bundle.putString("name", "Fresh Blueberries");
                    bundle.putString("price", "4.99");
                    bundle.putString("description", "Hand-picked local blueberries, 100% organic.");
                    bundle.putInt("imageResId", R.drawable.blueberry);
                    break;
                case "organic eggs":
                    bundle.putString("name", "Organic Eggs");
                    bundle.putString("price", "5.99");
                    bundle.putString("description", "Farm-fresh organic eggs from free-range chickens.");
                    bundle.putInt("imageResId", R.drawable.egg);
                    break;
                case "organic chicken":
                    bundle.putString("name", "Organic Chicken");
                    bundle.putString("price", "9.99");
                    bundle.putString("description", "Locally raised, hormone-free organic chicken.");
                    bundle.putInt("imageResId", R.drawable.chicken);
                    break;
                case "farm cheese":
                    bundle.putString("name", "Farm Cheese");
                    bundle.putString("price", "6.49");
                    bundle.putString("description", "Creamy cheese made from fresh cow’s milk.");
                    bundle.putInt("imageResId", R.drawable.cheese);
                    break;
                case "organic grapes":
                    bundle.putString("name", "Organic Grapes");
                    bundle.putString("price", "4.29");
                    bundle.putString("description", "Sweet, seedless, freshly harvested grapes.");
                    bundle.putInt("imageResId", R.drawable.grape);
                    break;
                case "local pumpkin":
                    bundle.putString("name", "Local Pumpkin");
                    bundle.putString("price", "3.79");
                    bundle.putString("description", "Golden pumpkins perfect for soups or pies.");
                    bundle.putInt("imageResId", R.drawable.pumpkin);
                    break;
            }

            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
