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

public class AddGiftCardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_gift_card, container, false);

        EditText codeInput = view.findViewById(R.id.gift_card_code_input);
        Button saveButton = view.findViewById(R.id.save_gift_card_button);

        saveButton.setOnClickListener(v -> {
            String code = codeInput.getText().toString().trim();

            if (TextUtils.isEmpty(code) || code.length() < 6) {
                codeInput.setError("Enter a valid gift card code");
                return;
            }

            Card giftCard = new Card("Gift Card", code, "", "", Card.Type.GIFT);
            CardManager.addCard(giftCard);

            Toast.makeText(getContext(), "Gift Card added!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        Button cancelButton = view.findViewById(R.id.cancel_paypal_button);
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
