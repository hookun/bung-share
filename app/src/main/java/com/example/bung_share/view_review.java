package com.example.bung_share;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class view_review extends BottomSheetDialogFragment {
    RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_view_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String storeId = bundle.getString("storeId");
        //번들에서 가게아이디 받아오기
        ratingBar = view.findViewById(R.id.avg_rating);
        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }//X버튼누르면 창 닫기
        });
        LinearLayout linearLayout = view.findViewById(R.id.review_scroll);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean hasReview = jsonResponse.getBoolean("success");
                    double averageRating = jsonResponse.getDouble("average_rating");//jsp에서 구한 별점 평균값

                    JSONArray reviewsArray = jsonResponse.getJSONArray("reviews");
                    if(hasReview) {
                        for (int i = 0; i < reviewsArray.length(); i++) {
                            JSONObject reviewObject = reviewsArray.getJSONObject(i);

                            String userId = reviewObject.getString("userId");
                            double rating = reviewObject.getDouble("rating");
                            String content = reviewObject.getString("content");
                            String modifiedDate = reviewObject.getString("modifiedDate");

                            // ConstraintLayout 동적생성
                            ConstraintLayout constraintLayout = new ConstraintLayout(view.getContext());
                            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                            ); //ConstraintLayout 동적생성 속성값
                            layoutParams.topMargin = dpToPx(10);
                            constraintLayout.setLayoutParams(layoutParams);
                            constraintLayout.setBackgroundResource(R.drawable.round_coners);
                            constraintLayout.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(), R.color.grey));


                            // ID용 Textview 동적생성
                            TextView reviewIdTextView = new TextView(view.getContext());
                            ConstraintLayout.LayoutParams reviewIdLayoutParams = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                            );//
                            reviewIdLayoutParams.topMargin = dpToPx(10);
                            reviewIdLayoutParams.leftMargin = dpToPx(20);
                            reviewIdTextView.setLayoutParams(reviewIdLayoutParams);
                            reviewIdTextView.setText(userId);
                            reviewIdTextView.setTextSize(18);
                            reviewIdTextView.setId(View.generateViewId());

                            //시간용 TextView 동적생성
                            TextView reviewTime = new TextView(view.getContext());
                            ConstraintLayout.LayoutParams reviewTimeLayoutParams = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                            );
                            reviewTimeLayoutParams.topMargin = dpToPx(15);
                            reviewTime.setLayoutParams(reviewTimeLayoutParams);
                            String text_to_set = modifiedDate.substring(0, modifiedDate.indexOf(" "));
                            reviewTime.setText(text_to_set);
                            reviewTime.setTextSize(12);
                            reviewTime.setId(View.generateViewId());

                            // RatingBar 동적생성
                            RatingBar ratingBar = new RatingBar(view.getContext(), null, android.R.attr.ratingBarStyleIndicator);
                            ConstraintLayout.LayoutParams ratingBarLayoutParams = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                            );

                            ratingBarLayoutParams.topMargin = dpToPx(5);
                            ratingBarLayoutParams.rightMargin = dpToPx(20);
                            ratingBar.setLayoutParams(ratingBarLayoutParams);
                            ratingBar.setRating((float) rating);
                            ratingBar.setNumStars(5);
                            ratingBar.setIsIndicator(true);
                            ratingBar.setId(View.generateViewId());
                            // 리뷰 내용 TextView 동적생성
                            TextView reviewTextView = new TextView(view.getContext());
                            ConstraintLayout.LayoutParams reviewTextLayoutParams = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                                    dpToPx(150)
                            );
                            reviewTextLayoutParams.topMargin = dpToPx(5);
                            reviewTextLayoutParams.leftMargin = dpToPx(20);
                            reviewTextLayoutParams.rightMargin = dpToPx(20);
                            reviewTextView.setLayoutParams(reviewTextLayoutParams);
                            reviewTextView.setText(content);
                            reviewTextView.setId(View.generateViewId());
                            // ConstraintLayout에 TextView와 RatingBar 추가
                            constraintLayout.addView(reviewIdTextView);
                            constraintLayout.addView(reviewTime);
                            constraintLayout.addView(ratingBar);
                            constraintLayout.addView(reviewTextView);
                            // ConstraintLayout에 제약조건 설정
                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(constraintLayout);
                            constraintSet.connect(reviewIdTextView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, dpToPx(20));
                            constraintSet.connect(reviewIdTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(10));

                            constraintSet.connect(reviewTime.getId(), ConstraintSet.LEFT, reviewIdTextView.getId(), ConstraintSet.RIGHT, dpToPx(20));
                            constraintSet.connect(reviewTime.getId(), ConstraintSet.RIGHT, ratingBar.getId(), ConstraintSet.LEFT, dpToPx(20));
                            constraintSet.connect(reviewTime.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(15));

                            constraintSet.connect(ratingBar.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, dpToPx(20));
                            constraintSet.connect(ratingBar.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(5));

                            constraintSet.connect(reviewTextView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, dpToPx(20));
                            constraintSet.connect(reviewTextView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, dpToPx(20));
                            constraintSet.connect(reviewTextView.getId(), ConstraintSet.TOP, reviewIdTextView.getId(), ConstraintSet.BOTTOM, dpToPx(5));

                            constraintSet.applyTo(constraintLayout);
                            // LinearLayout에 ConstraintLayout 추가
                            linearLayout.addView(constraintLayout);
                        }
                        ratingBar.setRating((float) averageRating);
                    }else{//리뷰가 하나도 존재하지 않을경우
                        dismiss();//창닫고
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//얼럿다이얼로그 출력
                        builder.setTitle("리뷰가 없습니다");
                        builder.setPositiveButton("확인", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ReviewRequest reviewRequest = new ReviewRequest(storeId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(reviewRequest);
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}