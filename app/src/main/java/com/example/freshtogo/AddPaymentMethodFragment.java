package com.example.freshtogo;
import android.widget.Button;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddPaymentMethodFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_payment_method, container, false);

        LinearLayout creditCardOption = view.findViewById(R.id.credit_card_option);
        LinearLayout paypalOption = view.findViewById(R.id.paypal_option);
        LinearLayout giftCardOption = view.findViewById(R.id.gift_card_option);

        creditCardOption.setOnClickListener(v -> {
            AddCardFragment fragment = new AddCardFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        paypalOption.setOnClickListener(v -> {
            AddPayPalFragment fragment = new AddPayPalFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        giftCardOption.setOnClickListener(v -> {
            AddGiftCardFragment fragment = new AddGiftCardFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        Button backButton = view.findViewById(R.id.back_to_checkout_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
