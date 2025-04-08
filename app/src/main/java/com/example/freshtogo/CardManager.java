package com.example.freshtogo;

import java.util.ArrayList;
import java.util.List;

public class CardManager {

    private static final List<Card> savedCards = new ArrayList<>();

    static {
        // Preload with a few example cards
        savedCards.add(new Card("John Doe", "4111111111111234", "12/26", "123", Card.Type.VISA));
        savedCards.add(new Card("John Doe", "5500000000005678", "11/25", "456", Card.Type.MASTERCARD));
        savedCards.add(new Card("paypal@example.com", "paypal@example.com", "", "", Card.Type.PAYPAL));
        savedCards.add(new Card("FreshToGo Gift", "FreshToGo123456", "", "", Card.Type.GIFT));
    }

    public static List<Card> getSavedCards() {
        return new ArrayList<>(savedCards);
    }

    public static void addCard(Card card) {
        savedCards.add(card);
    }

    public static void clearCards() {
        savedCards.clear();
    }
}
