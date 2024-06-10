package com.example.bung_share;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user.
     */
    // TODO: Rename and change types and number of parameters
    public static user newInstance(String param1, String param2) {
        user fragment = new user();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    static View v; // 프래그먼트의 뷰 인스턴스
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(v!=null){
            ViewGroup parent = (ViewGroup)v.getParent();
            if(parent!=null){
                parent.removeView(v);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            v = inflater.inflate(R.layout.fragment_user, container, false);
        }catch (InflateException e){
            // 구글맵 View가 이미 inflate되어 있는 상태이므로, 에러를 무시합니다.
        }
        TextView userid = v.findViewById(R.id.user);
        Bundle bundle = getArguments();
        String userID = bundle.getString("key");//유저아이디값 받아오기
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // JSON 형식의 문자열을 JSONObject로 변환
                    JSONObject jsonObject = new JSONObject(response);

                    // 찜 데이터 처리
                    JSONArray dibsArray = jsonObject.getJSONArray("Dibs");
                    for (int i = 0; i < dibsArray.length(); i++) {
                        JSONObject dibsObj = dibsArray.getJSONObject(i);
                        String storeId = dibsObj.getString("storeId");
                        String address = dibsObj.getString("address");
                        // 받아온 데이터를 원하는 대로 처리
                        LinearLayout linearLayout = v.findViewById(R.id.dibed_layout); // 여기에는 실제 LinearLayout의 ID를 넣어주세요

                        Button button = new Button(v.getContext()); // Context를 인자로 받는 생성자를 사용하여 Button 객체를 생성합니다.
                        button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(40) // 높이는 40dp
                            ));
                        button.setPadding(dpToPx(10), 0, dpToPx(20), 0); // padding 설정
                        button.setText(address); // 버튼에 표시할 텍스트 설정

                        linearLayout.addView(button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                market_info marketInfoFragment = new market_info();

                                // Market_info 프래그먼트에 마커의 정보를 전달
                                Bundle bundle = new Bundle();
                                bundle.putString("storeId", storeId);
                                bundle.putString("userid",userID);
                                marketInfoFragment.setArguments(bundle);

                                // Market_info 프래그먼트로 화면을 전환
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, marketInfoFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                    // 리뷰 데이터 처리
                    JSONArray reviewsArray = jsonObject.getJSONArray("Reviews");
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        JSONObject reviewsObj = reviewsArray.getJSONObject(i);
                        String storeId = reviewsObj.getString("storeId");
                        String address = reviewsObj.getString("address");
                        // 받아온 데이터를 원하는 대로 처리
                        LinearLayout linearLayout = v.findViewById(R.id.reviewed_layout);
                        //버튼 동적생성
                        Button button = new Button(v.getContext()); // Context를 인자로 받는 생성자를 사용하여 Button 객체를 생성합
                        button.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                dpToPx(40)
                        ));
                        button.setPadding(dpToPx(10), 0, dpToPx(20), 0); // padding 설정
                        button.setText(address); // 버튼에 표시할 텍스트 설정

                        linearLayout.addView(button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                market_info marketInfoFragment = new market_info();

                                // Market_info 프래그먼트에 마커의 정보를 전달
                                Bundle bundle = new Bundle();
                                bundle.putString("storeId", storeId);
                                bundle.putString("userid",userID);
                                marketInfoFragment.setArguments(bundle);

                                // Market_info 프래그먼트로 화면을 전환
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, marketInfoFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                    // 제보 데이터 처리
                    JSONArray storesArray = jsonObject.getJSONArray("Stores");
                    for (int i = 0; i < storesArray.length(); i++) {
                        JSONObject storesObj = storesArray.getJSONObject(i);
                        String storeId = storesObj.getString("storeId");
                        String address = storesObj.getString("address");

                        LinearLayout linearLayout = v.findViewById(R.id.added_layout); // 여기에는 실제 LinearLayout의 ID를 넣어주세요
                        //버튼 동적생성
                        Button button = new Button(v.getContext()); // Context를 인자로 받는 생성자를 사용하여 Button 객체를 생성합니다.
                        button.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                dpToPx(40) // 높이는 40dp
                        ));
                        button.setPadding(dpToPx(10), 0, dpToPx(20), 0); // padding 설정
                        button.setText(address); // 버튼에 표시할 텍스트 설정

                        linearLayout.addView(button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                market_info marketInfoFragment = new market_info();

                                // Market_info 프래그먼트에 마커의 정보를 전달
                                Bundle bundle = new Bundle();
                                bundle.putString("storeId", storeId);
                                bundle.putString("userid",userID);
                                marketInfoFragment.setArguments(bundle);

                                // Market_info 프래그먼트로 화면을 전환
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, marketInfoFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        UserRequest userRequest = new UserRequest(userID,responseListener);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(userRequest);

        // 번들 객체에서 값 받기

        userid.setText(userID);
        return v;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}