package com.example.freshtogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

            if (TextUtils.isEmpty(name) || number.length() != 16 || !number.matches("\\d{16}")
                    || !expiry.matches("(0[1-9]|1[0-2])/\\d{2}") || !cvv.matches("\\d{3,4}")) {
                Toast.makeText(getContext(), "Please enter valid card details", Toast.LENGTH_SHORT).show();
                return;
            }

            String maskedCard = number.substring(12); // last 4 digits
            String cardDisplay = name + " - **** " + maskedCard;
            CardManager.addCard(cardDisplay); // Save to memory

            Toast.makeText(getContext(), "Card Added: " + cardDisplay, Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
