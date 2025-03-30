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

        LinearLayout blueberryCard = view.findViewById(R.id.card_blueberry);

        blueberryCard.setOnClickListener(v -> {
            Fragment fragment = new ProductDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", "Fresh Blueberries");
            bundle.putString("price", "4.99");
            bundle.putString("description", "Hand-picked local blueberries, 100% organic.");
            bundle.putInt("imageResId", R.drawable.blueberry);
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
