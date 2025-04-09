package com.example.freshtogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class EditCardFragment extends Fragment {

    private EditText nameInput, numberInput, expiryInput, cvvInput;
    private Card originalCard;
    private int cardIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);

        nameInput = view.findViewById(R.id.edit_cardholder_name_input);
        numberInput = view.findViewById(R.id.edit_card_number_input);
        expiryInput = view.findViewById(R.id.edit_expiry_date_input);
        cvvInput = view.findViewById(R.id.edit_cvv_input);

        Button updateButton = view.findViewById(R.id.update_card_button);
        Button deleteButton = view.findViewById(R.id.delete_card_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        Bundle args = getArguments();
        if (args != null && args.containsKey("card_index")) {
            cardIndex = args.getInt("card_index");
            List<Card> cards = CardManager.getSavedCards();

            if (cardIndex >= 0 && cardIndex < cards.size()) {
                originalCard = cards.get(cardIndex);
                Card.Type type = originalCard.getType();

                switch (type) {
                    case PAYPAL:
                        nameInput.setText(originalCard.getCardholderName());
                        numberInput.setText(originalCard.getCardNumber());
                        nameInput.setVisibility(View.GONE);
                        numberInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        expiryInput.setVisibility(View.GONE);
                        cvvInput.setVisibility(View.GONE);
                        break;

                    case GIFT:
                        nameInput.setVisibility(View.GONE);
                        numberInput.setText(originalCard.getCardNumber());

                        expiryInput.setVisibility(View.GONE);
                        cvvInput.setVisibility(View.GONE);
                        break;

                    default:
                        nameInput.setText(originalCard.getCardholderName());
                        numberInput.setText(originalCard.getCardNumber());
                        expiryInput.setText(originalCard.getExpiryDate());
                        break;
                }
            }
        }

        updateButton.setOnClickListener(v -> {
            Card.Type type = originalCard.getType();

            String newName = nameInput.getText().toString().trim();
            String newNumber = numberInput.getText().toString().trim();
            String newExpiry = expiryInput.getText().toString().trim();
            String newCvv = cvvInput.getText().toString().trim();

            if (type == Card.Type.PAYPAL) {
                if (TextUtils.isEmpty(newName)) {
                    nameInput.setError("Name required");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newNumber).matches()) {
                    numberInput.setError("Enter a valid PayPal email");
                    return;
                }
                Card updatedCard = new Card(newName, newNumber, "", "", type);
                CardManager.updateCard(originalCard, updatedCard);

            } else if (type == Card.Type.GIFT) {
                if (!newNumber.matches("\\d{6}")) {
                    numberInput.setError("Enter valid 6-digit gift code");
                    return;
                }
                Card updatedCard = new Card(originalCard.getCardholderName(), newNumber, "", "", type);
                CardManager.updateCard(originalCard, updatedCard);

            } else {
                if (TextUtils.isEmpty(newName)) {
                    nameInput.setError("Name required");
                    return;
                }
                if (!newNumber.matches("\\d{16}")) {
                    numberInput.setError("Enter 16-digit card number");
                    return;
                }
                if (!newExpiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                    expiryInput.setError("Use MM/YY format");
                    return;
                }
                if (!newCvv.matches("\\d{3,4}")) {
                    cvvInput.setError("CVV must be 3 or 4 digits");
                    return;
                }

                Card.Type inferredType;
                if (newNumber.startsWith("4")) inferredType = Card.Type.VISA;
                else if (newNumber.startsWith("5")) inferredType = Card.Type.MASTERCARD;
                else inferredType = Card.Type.AMEX;

                Card updatedCard = new Card(newName, newNumber, newExpiry, newCvv, inferredType);
                CardManager.updateCard(originalCard, updatedCard);
            }

            Toast.makeText(getContext(), "Card updated!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        deleteButton.setOnClickListener(v -> {
            CardManager.removeCard(originalCard);
            Toast.makeText(getContext(), "Card deleted", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
