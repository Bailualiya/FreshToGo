package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExploreAdapter exploreAdapter;
    private List<Product> productList;
    private List<Product> filteredList;
    private SearchView searchView;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.exploreRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Original product list
        productList = new ArrayList<>();
        productList.add(new Product("Lettuce", "Vegetable", 2.49, R.drawable.lettuce));
        productList.add(new Product("Carrot", "Vegetable", 1.99, R.drawable.carrot));
        productList.add(new Product("Tomato", "Vegetable", 3.29, R.drawable.tomato));
        productList.add(new Product("Apple", "Fruit", 2.89, R.drawable.apple));

        // Filtered copy for the adapter
        filteredList = new ArrayList<>(productList);
        exploreAdapter = new ExploreAdapter(requireContext(), filteredList);
        recyclerView.setAdapter(exploreAdapter);

        // Search filtering logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Not needed
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String keyword) {
        filteredList.clear();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(product);
            }
        }
        exploreAdapter.notifyDataSetChanged();
    }
}
