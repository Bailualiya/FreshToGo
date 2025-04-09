package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ManageCardsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_cards, container, false);
        LinearLayout containerLayout = view.findViewById(R.id.card_list_container);

        TextView title = new TextView(getContext());
        title.setText("Edit Info for Cards");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(android.R.color.black));
        title.setPadding(16, 32, 16, 32);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        containerLayout.addView(title);

        List<Card> savedCards = CardManager.getSavedCards();
        for (int i = 0; i < savedCards.size(); i++) {
            Card card = savedCards.get(i);
            int index = i;

            LinearLayout cardItemLayout = new LinearLayout(getContext());
            cardItemLayout.setOrientation(LinearLayout.HORIZONTAL);
            cardItemLayout.setPadding(32, 32, 32, 32);
            cardItemLayout.setBackgroundResource(R.drawable.card_background); // Rounded corners
            cardItemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            cardItemLayout.setClickable(true);
            cardItemLayout.setFocusable(true);

            // Card Icon
            ImageView icon = new ImageView(getContext());
            int size = (int) (40 * getResources().getDisplayMetrics().density); // ~40dp
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(size, size);
            iconParams.setMargins(0, 0, 32, 0);
            icon.setLayoutParams(iconParams);

            switch (card.getType()) {
                case VISA:
                case MASTERCARD:
                case AMEX:
                    icon.setImageResource(R.drawable.card); break;
                case PAYPAL:
                    icon.setImageResource(R.drawable.paypal); break;
                case GIFT:
                    icon.setImageResource(R.drawable.gift); break;
            }

            TextView cardText = new TextView(getContext());
            cardText.setText(card.getDisplayLabel());
            cardText.setTextSize(16);
            cardText.setTextColor(getResources().getColor(android.R.color.black));
            cardText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            cardItemLayout.addView(icon);
            cardItemLayout.addView(cardText);

            cardItemLayout.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("card_index", index);
                EditCardFragment editFragment = new EditCardFragment();
                editFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, editFragment)
                        .addToBackStack(null)
                        .commit();
            });

            LinearLayout wrapper = new LinearLayout(getContext());
            wrapper.setOrientation(LinearLayout.VERTICAL);
            wrapper.setPadding(16, 0, 16, 16);
            wrapper.addView(cardItemLayout);
            containerLayout.addView(wrapper);
        }
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
