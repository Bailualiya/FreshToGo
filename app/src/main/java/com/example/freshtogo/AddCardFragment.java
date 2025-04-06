package com.example.freshtogo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class AddCardFragment extends Fragment {
    private EditText cardNumberInput, cardHolderNameInput, securityCodeInput, expiryDateInput, bankNameInput;
    private Button saveCardButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        cardNumberInput = view.findViewById(R.id.card_number_input);
        cardHolderNameInput = view.findViewById(R.id.card_holder_name_input);
        securityCodeInput = view.findViewById(R.id.security_code_input);
        expiryDateInput = view.findViewById(R.id.expiry_date_input);
        bankNameInput = view.findViewById(R.id.bank_name_input);
        saveCardButton = view.findViewById(R.id.save_card_button);

        saveCardButton.setOnClickListener(v -> saveCard());

        return view;
    }

    private void saveCard() {
        String cardNumber = cardNumberInput.getText().toString().trim();
        String cardHolderName = cardHolderNameInput.getText().toString().trim();
        String securityCode = securityCodeInput.getText().toString().trim();
        String expiryDate = expiryDateInput.getText().toString().trim();
        String bankName = bankNameInput.getText().toString().trim();

        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || securityCode.isEmpty() || expiryDate.isEmpty() || bankName.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the new card to the user's file
        String currentUser = SessionManager.getCurrentUser(getContext());
        if (currentUser == null) return;

        File file = new File(getContext().getFilesDir(), currentUser + ".txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean insideCardsSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("CARDS:")) {
                    insideCardsSection = true;
                    lines.add("CARDS:"); // Add cards section header if it's missing
                } else if (insideCardsSection && !line.trim().isEmpty()) {
                    lines.add(line);  // Add the saved card details
                }
            }

            // Add the new card to the CARDS section
            lines.add(cardNumber + " | " + cardHolderName + " | " + expiryDate + " | " + bankName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated lines to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), "Card added successfully", Toast.LENGTH_SHORT).show();
        // Navigate back to CheckoutFragment
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
