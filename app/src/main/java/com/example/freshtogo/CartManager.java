package com.example.freshtogo;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<CartItem> cartItems = new ArrayList<>();

    public static void addItem(CartItem item) {
        cartItems.add(item);
    }

    public static List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public static void clearCart() {
        cartItems.clear();
    }


    public static double calculateSubtotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice();
        }
        return total;
    }
    public static final double DELIVERY_FEE = 2.99;

    public static double calculateTotal() {
        return calculateSubtotal() + DELIVERY_FEE; // 假设 deliveryFee 是常量
    }

}
