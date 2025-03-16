package com.example.freshtogo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // 空的构造方法
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 绑定 UI 组件
        ImageView avatar = view.findViewById(R.id.avatar);
        TextView userName = view.findViewById(R.id.user_name);
        TextView userEmail = view.findViewById(R.id.user_email);
        LinearLayout myOrders = view.findViewById(R.id.my_orders);
        LinearLayout addresses = view.findViewById(R.id.delivery_addresses);
        LinearLayout paymentMethods = view.findViewById(R.id.payment_methods);
        LinearLayout notifications = view.findViewById(R.id.notifications);
        LinearLayout help = view.findViewById(R.id.help_support);
        LinearLayout logout = view.findViewById(R.id.logout);

        // 设置用户信息
        avatar.setImageResource(R.drawable.ic_profile);
        userName.setText("John Doe");
        userEmail.setText("john.doe@example.com");

        // 设置点击事件
        myOrders.setOnClickListener(v -> showToast("My Orders"));
        addresses.setOnClickListener(v -> showToast("Delivery Addresses"));
        paymentMethods.setOnClickListener(v -> showToast("Payment Methods"));
        notifications.setOnClickListener(v -> showToast("Notifications"));
        help.setOnClickListener(v -> showToast("Help & Support"));
        logout.setOnClickListener(v -> showToast("Logged Out"));

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
