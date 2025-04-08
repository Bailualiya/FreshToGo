package com.example.freshtogo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddPayPalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_paypal, container, false);

        EditText emailInput = view.findViewById(R.id.paypal_email_input);
        Button linkButton = view.findViewById(R.id.save_paypal_button);

        linkButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid PayPal email");
                return;
            }

            Card paypal = new Card(email, email, "", "", Card.Type.PAYPAL);
            CardManager.addCard(paypal);

            Toast.makeText(getContext(), "PayPal linked!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        Button cancelButton = view.findViewById(R.id.cancel_paypal_button);
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
