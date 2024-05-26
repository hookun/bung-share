package com.example.bung_share;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link market_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class market_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public market_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment market_info.
     */
    // TODO: Rename and change types and number of parameters
    public static market_info newInstance(String param1, String param2) {
        market_info fragment = new market_info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    static View v;
    info_review_fragment bottomSheet1;
    view_review bottomSheet2;
    Button view_review,make_review;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 프래그먼트 오류방지용
        try{
            v = inflater.inflate(R.layout.fragment_market_info, container, false);
        }catch (InflateException e){
        }

        make_review = v.findViewById(R.id.make_review);
        view_review = v.findViewById(R.id.review_button);
        FragmentActivity activity = requireActivity();//getSupportFragmentManager() 사용하려고 적용
        make_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//화면아래서 올라오는거 적용
                bottomSheet1 = new info_review_fragment();
                bottomSheet1.show(activity.getSupportFragmentManager(), bottomSheet1.getTag());
            }
        });
        view_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet2 = new view_review();
                bottomSheet2.show(activity.getSupportFragmentManager(), bottomSheet2.getTag());
            }
        });
        return v;
    }
}