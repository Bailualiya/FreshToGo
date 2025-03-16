package com.example.freshtogo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private LinearLayout savedList;

    public SavedFragment() {
        // 空的构造方法
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        savedList = view.findViewById(R.id.saved_list);

        // 加载农场数据
        loadSavedFarms();

        return view;
    }

    private void loadSavedFarms() {
        List<Farm> farms = new ArrayList<>();
        farms.add(new Farm("Berry Hills Farm", "Organic berries and fruits", "2.5 miles away", R.drawable.ic_farm1));
        farms.add(new Farm("Happy Hens Farm", "Free-range eggs and poultry", "3.2 miles away", R.drawable.ic_farm2));

        for (Farm farm : farms) {
            View itemView = getLayoutInflater().inflate(R.layout.item_saved_farm, savedList, false);

            TextView farmName = itemView.findViewById(R.id.farm_name);
            TextView farmDesc = itemView.findViewById(R.id.farm_desc);
            TextView farmDistance = itemView.findViewById(R.id.farm_distance);
            ImageView farmImage = itemView.findViewById(R.id.farm_image);

            farmName.setText(farm.getName());
            farmDesc.setText(farm.getDescription());
            farmDistance.setText(farm.getDistance());
            farmImage.setImageResource(farm.getImageRes());

            savedList.addView(itemView);
        }
    }

    static class Farm {
        private String name, description, distance;
        private int imageRes;

        public Farm(String name, String description, String distance, int imageRes) {
            this.name = name;
            this.description = description;
            this.distance = distance;
            this.imageRes = imageRes;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getDistance() { return distance; }
        public int getImageRes() { return imageRes; }
    }
}
