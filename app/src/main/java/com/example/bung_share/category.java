package com.example.bung_share;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link category#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class category extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment category.
     */
    // TODO: Rename and change types and number of parameters
    public static category newInstance(String param1, String param2) {
        category fragment = new category();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public category() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try{
            v = inflater.inflate(R.layout.fragment_category, container, false);
        }catch (InflateException e){
            // 구글맵 View가 이미 inflate되어 있는 상태이므로, 에러를 무시합니다.
        }

        ImageButton btn1,btn2,btn3,btn4,btn5;
        btn1 = (ImageButton) getView().findViewById(R.id.btn1);
        btn2 = (ImageButton) getView().findViewById(R.id.btn2);
        btn3 = (ImageButton) getView().findViewById(R.id.btn3);
        btn4 = (ImageButton) getView().findViewById(R.id.btn4);
        btn5 = (ImageButton) getView().findViewById(R.id.btn5);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("selected",btn1.getTag().toString());
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });
        return v;
    }
}