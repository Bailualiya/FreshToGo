package com.example.freshtogo;

import java.util.ArrayList;
import java.util.List;

public class CardManager {

    private static final List<Card> savedCards = new ArrayList<>();

    static {
        savedCards.add(new Card("John Doe", "4111111111111234", "12/26", "123", Card.Type.VISA));

        savedCards.add(new Card("John Doe", "5500000000005678", "11/25", "456", Card.Type.MASTERCARD));

        savedCards.add(new Card("John Doe", "paypal@example.com", "", "", Card.Type.PAYPAL));

        savedCards.add(new Card("Gift User", "123123", "", "", Card.Type.GIFT));
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
    public static void removeCard(Card card) {
        savedCards.remove(card);
    }
    public static void updateCard(Card oldCard, Card newCard) {
        int index = savedCards.indexOf(oldCard);
        if (index != -1) {
            savedCards.set(index, newCard);
        }
    }


}
