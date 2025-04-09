package com.example.freshtogo;

public class Card {

    public enum Type {
        VISA, MASTERCARD, AMEX, PAYPAL, GIFT
    }

    private final String cardholderName;
    private final String cardNumber;
    private final String expiryDate;
    private final String cvv;
    private final Type type;

    public Card(String cardholderName, String cardNumber, String expiryDate, String cvv, Type type) {
        this.cardholderName = cardholderName;

        if (type == Type.GIFT && (cardNumber == null || !cardNumber.matches("\\d{6}"))) {
            throw new IllegalArgumentException("Gift Card code must be exactly 6 digits");
        }

        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.type = type;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getMaskedCardNumber() {
        if (cardNumber.length() >= 4) {
            return "•••• " + cardNumber.substring(cardNumber.length() - 4);
        } else {
            return cardNumber;
        }
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public Type getType() {
        return type;
    }

    public String getDisplayLabel() {
        switch (type) {
            case PAYPAL:
                return "PayPal - " + cardNumber;
            case GIFT:
                return "Gift Card - " + cardNumber;
            default:
                return type.name() + " - " + getMaskedCardNumber();
        }
    }
}
