package com.example.freshtogo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng courierLocation = new LatLng(49.935, -119.3904);
    private LatLng userLocation = new LatLng(49.934, -119.4);
    private Marker courierMarker;
    private Marker userMarker;
    private List<LatLng> routePoints;
    private Polyline remainingLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.btnChat).setOnClickListener(v -> showChatDialog());
        findViewById(R.id.btnCall).setOnClickListener(v -> showCallDialog());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDescriptor houseIcon = resizeIcon(R.drawable.ic_house, 48, 48);
        BitmapDescriptor bikeIcon = resizeIcon(R.drawable.ic_delivery, 48, 48);

        userMarker = mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("You")
                .icon(houseIcon));

        courierMarker = mMap.addMarker(new MarkerOptions()
                .position(courierLocation)
                .title("Your Courier")
                .icon(bikeIcon));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(courierLocation, 15f));

        String url = getDirectionsUrl(courierLocation, userLocation);
        new FetchRouteTask().execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String parameters = str_origin + "&" + str_dest + "&sensor=false&mode=driving";
        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=AIzaSyCS-q-foUIZ_Pdhmlq0oz01vrmbKLDQLvU";
    }

    private void animateCourierAlongRoute(List<LatLng> points) {
        if (points == null || points.size() < 2) return;

        final int[] index = {0};

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < points.size() - 1) {
                    LatLng next = points.get(index[0]);
                    courierMarker.setPosition(next);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(next));

                    updateRemainingRoute(points.subList(index[0], points.size()));

                    index[0]++;
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    private void updateRemainingRoute(List<LatLng> remainingPoints) {
        if (remainingLine != null) {
            remainingLine.remove();
        }

        PolylineOptions options = new PolylineOptions()
                .addAll(remainingPoints)
                .width(12)
                .color(Color.RED)
                .geodesic(true);
        remainingLine = mMap.addPolyline(options);
    }

    private void showCallDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Call Your Courier")
                .setMessage("Calling...")
                .setPositiveButton("Close", null)
                .show();
    }

    private void showChatDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter your message...");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Send Message To Your Courier")
                .setView(input)
                .setPositiveButton("Send", (dialog, which) -> {
                    String message = input.getText().toString();
                    if (!message.isEmpty()) {
                        Toast.makeText(this, "Sent successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    private BitmapDescriptor resizeIcon(int drawableResId, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), drawableResId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    private class FetchRouteTask extends AsyncTask<String, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(String... urls) {
            List<LatLng> route = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray routes = jsonObject.getJSONArray("routes");

                if (routes.length() > 0) {
                    JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                    JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

                    for (int i = 0; i < steps.length(); i++) {
                        String polyline = steps.getJSONObject(i)
                                .getJSONObject("polyline")
                                .getString("points");
                        route.addAll(decodePoly(polyline));
                    }
                }

                reader.close();
                is.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return route;
        }

        @Override
        protected void onPostExecute(List<LatLng> result) {
            if (result != null && !result.isEmpty()) {
                routePoints = result;


                animateCourierAlongRoute(routePoints);
            }
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLat = (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            lat += dLat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dLng = (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            lng += dLng;

            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }

        return poly;
    }
}
