package com.example.freshtogo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountText;
    private Button payNowButton, addCardButton;
    private RadioGroup cardOptions;

    private AppDatabase db;
    private CartDao cartDao;
    private SavedCardDao savedCardDao;
    private OrderDao orderDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalAmountText = findViewById(R.id.totalAmountText);
        payNowButton = findViewById(R.id.payNowButton);
        addCardButton = findViewById(R.id.addCardButton);
        cardOptions = findViewById(R.id.cardOptions);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "fresh_to_go_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        cartDao = db.cartDao();
        savedCardDao = db.savedCardDao();
        orderDao = db.orderDao();
        sessionManager = new SessionManager(this);

        double total = getIntent().getDoubleExtra("totalAmount", 0.0);
        totalAmountText.setText("Total: $" + String.format("%.2f", total));

        loadSavedCards(); // Only for current user

        addCardButton.setOnClickListener(v -> showAddCardDialog());

        payNowButton.setOnClickListener(v -> {
            int selectedId = cardOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a card", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = sessionManager.getUserId();
            List<CartItem> cartItems = cartDao.getCartItems(userId);

            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Save to Order History
            for (CartItem item : cartItems) {
                OrderItem order = new OrderItem();
                order.userId = item.userId;
                order.name = item.name;
                order.category = item.category;
                order.price = item.price;
                order.imageResId = item.imageResId;
                order.quantity = item.quantity;
                orderDao.insertOrder(order);
            }

            // Clear cart after saving orders
            cartDao.clearCart(userId);

            Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadSavedCards() {
        cardOptions.removeAllViews();
        int userId = sessionManager.getUserId();
        List<SavedCard> cards = savedCardDao.getCardsByUser(userId);

        for (SavedCard card : cards) {
            RadioButton rb = new RadioButton(this);
            rb.setText(card.cardLabel != null ? card.cardLabel : (card.bankName + " xxxx" + card.last4Digits));
            rb.setId(View.generateViewId());
            cardOptions.addView(rb);
        }
    }

    private void showAddCardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Card");

        EditText inputBank = new EditText(this);
        inputBank.setHint("Bank Name (e.g. RBC Bank)");

        EditText inputDigits = new EditText(this);
        inputDigits.setHint("Last 4 Digits");
        inputDigits.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 16);
        layout.addView(inputBank);
        layout.addView(inputDigits);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String bank = inputBank.getText().toString().trim();
            String digits = inputDigits.getText().toString().trim();

            if (bank.isEmpty() || digits.length() != 4 || !digits.matches("\\d{4}")) {
                Toast.makeText(this, "Invalid card info", Toast.LENGTH_SHORT).show();
                return;
            }

            SavedCard card = new SavedCard();
            card.userId = sessionManager.getUserId();
            card.bankName = bank;
            card.last4Digits = digits;
            card.cardLabel = bank + " xxxx" + digits;

            savedCardDao.insertCard(card);
            loadSavedCards();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
