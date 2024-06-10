package com.example.bung_share;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
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
    public class CustomEvent {//이벤트 버스사용
        private int value;

        public CustomEvent(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // 이벤트 전송


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ratingBar = view.findViewById(R.id.RatingBar_review);
        review_content = view.findViewById(R.id.et_review);

        view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {//X버튼 누르면 창닫기
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        String userId, storeId;
        Button upload_btn = view.findViewById(R.id.btn_upload_review);

        Bundle bundle = getArguments();//번들에서 storeid,userid값 받아오기
        storeId = bundle.getString("storeId");
        userId = bundle.getString("userid");


        upload_btn.setOnClickListener(new View.OnClickListener() {//리뷰 업로드 버튼 눌렀을때
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingBar.getRating());
                String content = review_content.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("test",response.toString());
                            boolean success = jsonResponse.getBoolean("success");//DB삽입 확인용
                            boolean already = jsonResponse.getBoolean("already");//미리 같은 계정으로 작성한 리뷰가 있을경우 false
                            int reviewcount;

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            if(success){//성공시
                                reviewcount = jsonResponse.getInt("reviewCount");//그 가게의 리뷰 갯수를 성공했을때만 받아옴
                                dialog = builder.setMessage("등록완료.").setPositiveButton("확인", null).create();
                                CustomEvent event = new CustomEvent(reviewcount);
                                EventBus.getDefault().post(event);
                            }else if(!success&&already){//이미 작성한 리뷰가 있을경우
                                dialog = builder.setMessage("이미 작성한 리뷰가 있습니다.").setNegativeButton("확인", null).create();
                            }else{//실패시
                                dialog = builder.setMessage("등록실패").setNegativeButton("확인", null).create();
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