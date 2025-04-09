package com.example.freshtogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class CheckoutFragment extends Fragment {

    private Card selectedCard = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        LinearLayout cardListLayout = view.findViewById(R.id.card_radio_group);
        EditText cvvInput = view.findViewById(R.id.confirm_cvv_input);
        Button addCardButton = view.findViewById(R.id.add_card_button);
        Button payNowButton = view.findViewById(R.id.pay_now_button);

        List<Card> cards = CardManager.getSavedCards();

        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (Card card : cards) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(card.getDisplayLabel());
            rb.setTextSize(16);
            rb.setPadding(8, 16, 8, 16);
            radioGroup.addView(rb);

            rb.setOnClickListener(v -> {
                selectedCard = card;
                if (card.getType() == Card.Type.PAYPAL || card.getType() == Card.Type.GIFT) {
                    cvvInput.setVisibility(View.GONE);
                } else {
                    cvvInput.setVisibility(View.VISIBLE); // Show CVV for Credit and Debit cards
                }
            });
        }

        cardListLayout.addView(radioGroup);

        addCardButton.setOnClickListener(v -> {
            AddPaymentMethodFragment fragment = new AddPaymentMethodFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        payNowButton.setOnClickListener(v -> {
            if (selectedCard == null) {
                Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            // CVV validation for credit and debit cards
            if (selectedCard.getType() != Card.Type.PAYPAL && selectedCard.getType() != Card.Type.GIFT) {
                String enteredCVV = cvvInput.getText().toString().trim();
                if (TextUtils.isEmpty(enteredCVV) || !enteredCVV.equals(selectedCard.getCvv())) {
                    Toast.makeText(getContext(), "Invalid or missing CVV", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(getContext(), "Payment Successful!", Toast.LENGTH_LONG).show();

            // Clear the cart after payment
            CartManager.clearCart();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        });

        Button manageCardsButton = view.findViewById(R.id.manage_cards_button);
        manageCardsButton.setOnClickListener(v -> {
            ManageCardsFragment manageFragment = new ManageCardsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, manageFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
