package com.example.freshtogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;  // 必须导入 Button
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 绑定 UI 组件
        ImageView settingsIcon = view.findViewById(R.id.settings_icon);
        Button logoutButton = view.findViewById(R.id.logout);

        // 设置按钮点击事件
        settingsIcon.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Settings Clicked", Toast.LENGTH_SHORT).show();
        });

        // 登出按钮点击事件
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
