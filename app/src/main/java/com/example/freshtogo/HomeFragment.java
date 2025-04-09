package com.example.freshtogo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private LinearLayout blueberryCard, eggCard, chickenCard, cheeseCard, grapeCard, pumpkinCard;
    private EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize product cards
        blueberryCard = view.findViewById(R.id.card_blueberry);
        eggCard = view.findViewById(R.id.card_egg);
        chickenCard = view.findViewById(R.id.card_chicken);
        cheeseCard = view.findViewById(R.id.card_cheese);
        grapeCard = view.findViewById(R.id.card_grape);
        pumpkinCard = view.findViewById(R.id.card_pumpkin);

        // Set up the search bar functionality
        searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

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


    private void filterProducts(String query) {
        List<Product> products = new ArrayList<>();

        products.add(new Product("Fresh Blueberries", blueberryCard));
        products.add(new Product("Organic Eggs", eggCard));
        products.add(new Product("Organic Chicken", chickenCard));
        products.add(new Product("Farm Cheese", cheeseCard));
        products.add(new Product("Organic Grapes", grapeCard));
        products.add(new Product("Local Pumpkin", pumpkinCard));

        if (TextUtils.isEmpty(query)) {
            setAllProductsVisibility(View.VISIBLE);
        } else {
            setAllProductsVisibility(View.GONE);

            for (Product product : products) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    product.getCard().setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void setAllProductsVisibility(int visibility) {
        blueberryCard.setVisibility(visibility);
        eggCard.setVisibility(visibility);
        chickenCard.setVisibility(visibility);
        cheeseCard.setVisibility(visibility);
        grapeCard.setVisibility(visibility);
        pumpkinCard.setVisibility(visibility);
    }

    class Product {
        private String name;
        private LinearLayout card;

        public Product(String name, LinearLayout card) {
            this.name = name;
            this.card = card;
        }

        public String getName() {
            return name;
        }

        public LinearLayout getCard() {
            return card;
        }
    }
}
