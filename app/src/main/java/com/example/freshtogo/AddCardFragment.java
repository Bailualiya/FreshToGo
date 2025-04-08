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

public class AddCardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        EditText nameInput = view.findViewById(R.id.cardholder_name_input);
        EditText numberInput = view.findViewById(R.id.card_number_input);
        EditText expiryInput = view.findViewById(R.id.expiry_date_input);
        EditText cvvInput = view.findViewById(R.id.cvv_input);
        Button saveButton = view.findViewById(R.id.save_card_button);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String number = numberInput.getText().toString().trim();
            String expiry = expiryInput.getText().toString().trim();
            String cvv = cvvInput.getText().toString().trim();


            if (TextUtils.isEmpty(name)) {
                nameInput.setError("Name required");
                return;
            }

            if (!number.matches("\\d{16}")) {
                numberInput.setError("Enter 16-digit card number");
                return;
            }

            if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                expiryInput.setError("Use MM/YY format");
                return;
            }

            if (!cvv.matches("\\d{3,4}")) {
                cvvInput.setError("CVV must be 3 or 4 digits");
                return;
            }

            Card.Type type;
            if (number.startsWith("4")) type = Card.Type.VISA;
            else if (number.startsWith("5")) type = Card.Type.MASTERCARD;
            else type = Card.Type.AMEX;

            Card newCard = new Card(name, number, expiry, cvv, type);
            CardManager.addCard(newCard);

            Toast.makeText(getContext(), "Card added successfully!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        Button cancelButton = view.findViewById(R.id.cancel_card_button);
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return view;
    }
}
