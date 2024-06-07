package com.example.bung_share;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class info_review_fragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_review_fragment, container, false);
    }
    private AlertDialog dialog;

    RatingBar ratingBar;
    EditText review_content;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ratingBar = view.findViewById(R.id.RatingBar_review);
        review_content = view.findViewById(R.id.et_review);

        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        String userId, storeId;
        Button upload_btn = view.findViewById(R.id.btn_upload_review);

        Bundle bundle = getArguments();
        storeId = bundle.getString("storeId");
        userId = bundle.getString("userid");

        //TODO:날짜랑 별점 처리하고 DB에 넣는거 구현할것


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingBar.getRating());
                String content = review_content.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            if(success){
                                dialog = builder.setMessage("등록완료.").setPositiveButton("확인", null).create();
                            }else{
                                dialog = builder.setMessage("등록실패.").setNegativeButton("확인", null).create();
                            }
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Make_review_Request makeReviewRequest = new Make_review_Request(userId, storeId, rating, content,responseListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                queue.add(makeReviewRequest);
            }
        });

    }
}