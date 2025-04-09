package com.example.freshtogo;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<CartItem> cartItems = new ArrayList<>();
    public static void addItem(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return; // Item already in the cart, update quantity
            }
        }
        cartItems.add(item);
    }
    public static List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public static void clearCart() {
        cartItems.clear();
    }
    public static void updateCart(List<CartItem> updatedItems) {
        // Replace the entire cart with the updated items
        cartItems.clear();
        cartItems.addAll(updatedItems);
    }
    public static double calculateSubtotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public static final double DELIVERY_FEE = 2.99;

    public static double calculateTotal() {
        return calculateSubtotal() + DELIVERY_FEE;
    }

    public static void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public static double calculateDeliveryFee() {
        return DELIVERY_FEE;
    }
}
