package com.example.freshtogo;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final double DELIVERY_FEE = 2.99;

    // Updated addItem method to include image
    public static void addItem(Context context, CartItem item) {
        String user = SessionManager.getCurrentUser(context);
        if (user == null) return;

        File file = new File(context.getFilesDir(), user + ".txt");
        List<CartItem> currentItems = loadCartItems(context);

        // Check if item already exists
        boolean updated = false;
        for (CartItem cartItem : currentItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                updated = true;
                break;
            }
        }

        if (!updated) {
            currentItems.add(item);
        }

        saveCartItems(file, currentItems);
    }

    public static List<CartItem> getCartItems(Context context) {
        return loadCartItems(context);
    }

    // Clear cart
    public static void clearCart(Context context) {
        String user = SessionManager.getCurrentUser(context);
        if (user == null) return;

        File file = new File(context.getFilesDir(), user + ".txt");
        File tempFile = new File(context.getFilesDir(), user + "_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean insideCart = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CART:")) {
                    insideCart = true;
                    writer.write("CART:\n"); // Keep the section header
                } else if (line.equals("ORDER_HISTORY:") || line.equals("CARDS:")) {
                    insideCart = false;
                    writer.write(line + "\n");
                } else if (!insideCart) {
                    writer.write(line + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        file.delete();
        tempFile.renameTo(file);
    }

    // Calculate subtotal
    public static double calculateSubtotal(Context context) {
        double total = 0;
        for (CartItem item : getCartItems(context)) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // Calculate total
    public static double calculateTotal(Context context) {
        return calculateSubtotal(context) + DELIVERY_FEE;
    }

    // Load cart items from file
    private static List<CartItem> loadCartItems(Context context) {
        List<CartItem> items = new ArrayList<>();
        String user = SessionManager.getCurrentUser(context);
        if (user == null) return items;

        File file = new File(context.getFilesDir(), user + ".txt");
        boolean insideCart = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CART:")) {
                    insideCart = true;
                } else if (line.equals("ORDER_HISTORY:") || line.equals("CARDS:")) {
                    insideCart = false;
                } else if (insideCart && !line.isEmpty()) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 3) {
                        String name = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double price = Double.parseDouble(parts[2]);

                        // Set the image resource based on the product
                        int imageResId = getProductImage(name);  // Method to map the product name to its image

                        items.add(new CartItem(name, price, quantity, imageResId));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    // Save cart items to file
    private static void saveCartItems(File file, List<CartItem> cartItems) {
        try {
            List<String> lines = new ArrayList<>();
            boolean insideCart = false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("CART:")) {
                    insideCart = true;
                    lines.add("CART:");  // Start the cart section
                    // Add the updated cart items to the file
                    for (CartItem item : cartItems) {
                        lines.add(item.getName() + " - " + item.getQuantity() + " - " + item.getPrice());
                    }
                } else if (line.equals("ORDER_HISTORY:") || line.equals("CARDS:")) {
                    insideCart = false;
                    lines.add(line);  // Keep the other sections intact
                } else if (!insideCart) {
                    lines.add(line);  // Keep the non-cart lines intact
                }
            }
            reader.close();

            // Overwrite the file with the new cart data
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String l : lines) {
                writer.write(l + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Get image resource based on product name
    private static int getProductImage(String productName) {
        switch (productName.toLowerCase()) {
            case "fresh blueberries":
                return R.drawable.blueberry;
            case "organic eggs":
                return R.drawable.egg;
            case "organic chicken":
                return R.drawable.chicken;
            case "farm cheese":
                return R.drawable.cheese;
            case "organic grapes":
                return R.drawable.grape;
            case "local pumpkin":
                return R.drawable.pumpkin;
            default:
                return 0;
        }
    }
}
