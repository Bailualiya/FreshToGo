package com.example.freshtogo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class FarmFragment extends Fragment {

    private LinearLayout savedList;

    public FarmFragment() {
        // 空的构造方法
    }

    private final int[] images = new int[]{R.drawable.ic_farm1, R.drawable.ic_farm2};

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm, container, false);
        assert this.getArguments() != null;
        SavedFragment.Farm farm = (SavedFragment.Farm) this.getArguments().getSerializable("farm");
        assert farm != null;
        ((TextView) view.findViewById(R.id.farm_name)).setText("Farm: " + farm.getName());
        ((TextView) view.findViewById(R.id.location)).setText("Location: " + farm.getLocation());
        ((TextView) view.findViewById(R.id.description)).setText("Description " + farm.getDescription());
        ((TextView) view.findViewById(R.id.contact)).setText("Contact: " + farm.getContact());
        ViewPager viewPager = view.findViewById(R.id.farm_photos);
        viewPager.setAdapter(new ImageAdapter(images));
        LinearLayout imagesLayout = view.findViewById(R.id.images_layout);
        Button reviewButton = view.findViewById(R.id.reviews);
        reviewButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container, new ReviewFragment()).commit();
        });

        for (int i = 0; i != images.length; i++) {
            View imageBackgroundView = new View(requireActivity());
            imageBackgroundView.setBackgroundResource(R.drawable.images_background);
            imageBackgroundView.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.leftMargin = 10;
            }
            imagesLayout.addView(imageBackgroundView, params);
        }

        imagesLayout.getChildAt(0).setEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPagerListener(imagesLayout));

        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
           requireActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        return view;
    }

    private static class ViewPagerListener implements ViewPager.OnPageChangeListener {
        LinearLayout layout;
        int currentPos = 0;

        public ViewPagerListener(LinearLayout layout) {
            this.layout = layout;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            layout.getChildAt(currentPos).setEnabled(false);
            layout.getChildAt(position).setEnabled(true);
            currentPos = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class ImageAdapter extends PagerAdapter {

        private final int[] images;

        public ImageAdapter(int[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view , @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int imageId = images[position];
            ImageView imageView = new ImageView(requireActivity());
            imageView.setImageResource(imageId);
            container.addView(imageView);
            return imageView;
        }

    }
}
