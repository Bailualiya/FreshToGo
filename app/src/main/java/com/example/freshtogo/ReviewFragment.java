package com.example.freshtogo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewFragment extends Fragment {

    public ReviewFragment() {
        // 空的构造方法
    }

    private final int[] images = new int[]{R.drawable.ic_farm1, R.drawable.ic_farm2};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_reviews, container, false);
        LinearLayout allReviews = view.findViewById(R.id.reviews);

        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        List<Review> reviews = loadReviewData();
        reviews.forEach(review -> generateReviewView(review, allReviews));

        Button addReview = view.findViewById(R.id.new_review);
        addReview.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container, new NewReviewFragment()).commit();
        });

        return view;
    }

    private void generateReviewView(Review review, LinearLayout parent) {
        LinearLayout container = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        containerParams.topMargin = 20;
        container.setOrientation(LinearLayout.HORIZONTAL);
        ImageView portrait = new ImageView(requireActivity());
        LinearLayout.LayoutParams portraitParams = new LinearLayout.LayoutParams(200, 200);
        portrait.setImageResource(review.userPortraitId);
        LinearLayout bodyContainer = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bodyParams.leftMargin = 20;
        LinearLayout headerContainer = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.bottomMargin = 50;
        headerContainer.setOrientation(LinearLayout.HORIZONTAL);
        bodyContainer.setOrientation(LinearLayout.VERTICAL);
        TextView userName = new TextView(requireActivity());
        userName.setText(review.name);
        TextView comment = new TextView(requireActivity());
        comment.setText(review.comment);
        TextView time = new TextView(requireActivity());
        time.setText(review.time);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeParams.leftMargin = 100;
        headerContainer.addView(userName);
        headerContainer.addView(time, timeParams);
        bodyContainer.addView(headerContainer, headerParams);
        bodyContainer.addView(comment);
        container.setGravity(Gravity.CENTER_VERTICAL);
        container.addView(portrait, portraitParams);
        container.addView(bodyContainer, bodyParams);
        parent.addView(container, containerParams);
    }

    private List<Review> loadReviewData() {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i != 30; i++) {
            reviews.add(new Review(
                    R.drawable.portrait1,
                    "I love this farm so much!",
                    "2024-01-01 12:00",
                    100,
                    "Jack"
            ));
        }

        return reviews;
    }


     static class Review {
        private int userPortraitId;
        private String comment;
        private String time;
        private int likes;
        private String name;

        public Review(int portraitId, String comment, String time, int likes, String name) {
            this.userPortraitId = portraitId;
            this.comment = comment;
            this.time = time;
            this.likes = likes;
            this.name = name;
        }

         public int getUserPortraitId() {
             return userPortraitId;
         }

         public void setUserPortraitId(int userPortraitId) {
             this.userPortraitId = userPortraitId;
         }

         public String getComment() {
             return comment;
         }

         public void setComment(String comment) {
             this.comment = comment;
         }

         public String getTime() {
             return time;
         }

         public void setTime(String time) {
             this.time = time;
         }

         public int getLikes() {
             return likes;
         }

         public void setLikes(int likes) {
             this.likes = likes;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }
     }
}
