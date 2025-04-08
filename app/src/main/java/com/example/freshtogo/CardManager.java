package com.example.freshtogo;

import java.util.ArrayList;
import java.util.List;

public class CardManager {

    private static final List<String> savedCards = new ArrayList<>();

    static {

        savedCards.add("Visa - **** 1234");
        savedCards.add("MasterCard - **** 5678");
        savedCards.add("Amex - **** 9876");
    }

    public static List<String> getSavedCards() {
        return new ArrayList<>(savedCards);
    }

    public static void addCard(String card) {
        savedCards.add(card);
    }
}
