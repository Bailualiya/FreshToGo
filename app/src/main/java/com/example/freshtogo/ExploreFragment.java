package com.example.freshtogo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ExploreFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    private Marker selectedMarker;
    private LatLng selectedLatLng;
    private String selectedAddress = "Kelowna, Canada"; // 默认地址

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // 确认按钮
        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("selected_address", selectedAddress);
            getParentFragmentManager().setFragmentResult("map_address_result", bundle);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // 默认位置改为 Kelowna
        LatLng kelownaLatLng = new LatLng(49.8880, -119.4960);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelownaLatLng, 13));

        // 显示多个农场标记（Kelowna 附近的虚构农场）
        googleMap.addMarker(new MarkerOptions().position(new LatLng(49.9100, -119.4500)).title("Maple Valley Farm"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(49.8700, -119.4800)).title("Okanagan Harvest Farm"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(49.8600, -119.4100)).title("Golden Apple Orchard"));

        // 地图点击监听，更新用户选择的位置
        googleMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Delivery here"));
            selectedLatLng = latLng;

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);
                    selectedAddress = addr.getAddressLine(0);
                } else {
                    selectedAddress = "Unknown Location";
                }
            } catch (IOException e) {
                e.printStackTrace();
                selectedAddress = "Error fetching address";
            }
        });
    }

    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}