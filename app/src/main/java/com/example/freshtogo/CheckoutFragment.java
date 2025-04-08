package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class CheckoutFragment extends Fragment {

    private String selectedCard = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        RadioGroup cardGroup = view.findViewById(R.id.card_radio_group);
        Button addCardButton = view.findViewById(R.id.add_card_button);
        Button payNowButton = view.findViewById(R.id.pay_now_button);


        List<String> cards = CardManager.getSavedCards();


        for (String card : cards) {
            RadioButton radio = new RadioButton(getContext());
            radio.setText(card);
            radio.setTextSize(16);
            cardGroup.addView(radio);

            radio.setOnClickListener(v -> selectedCard = card);
        }

        addCardButton.setOnClickListener(v -> {
            // Go to add card screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddCardFragment())
                    .addToBackStack(null)
                    .commit();
        });

        payNowButton.setOnClickListener(v -> {
            if (selectedCard == null) {
                Toast.makeText(getContext(), "Please select a card", Toast.LENGTH_SHORT).show();
            } else {
                CartManager.clearCart();
                Toast.makeText(getContext(), "Payment Successful with " + selectedCard, Toast.LENGTH_LONG).show();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });

        return view;
    }
}
