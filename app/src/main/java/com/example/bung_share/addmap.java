package com.example.bung_share;

import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addmap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addmap extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addmap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addmap.
     */
    // TODO: Rename and change types and number of parameters
    public static addmap newInstance(String param1, String param2) {
        addmap fragment = new addmap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    static View v; // 프래그먼트의 뷰 인스턴스

    @Override
    public void onDestroyView() {//가게정보 추가한뒤 다시 추가해도 남아있는 찌꺼기값 없게끔
        super.onDestroyView();
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
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

    MapView mapView;
    category bottomSheet1; //카테고리 눌렀을때 나오는 프래그먼트

    int i = 0;//카테고리 설정용
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            v = inflater.inflate(R.layout.fragment_addmap, container, false);
        } catch (InflateException e) {
            // 구글맵 View가 이미 inflate되어 있는 상태이므로, 에러를 무시합니다.
        }

        Bundle bundle = getArguments();
        String userid = bundle.getString("key"); //번들써서 userid값 받아오기

        TimePicker starttime = v.findViewById(R.id.timestart); //시작시간
        TimePicker endtime = v.findViewById(R.id.timeend);//종료시간
        starttime.setIs24HourView(true);//24시간기준으로 표시되게끔
        endtime.setIs24HourView(true);
        Button categoryButton = v.findViewById(R.id.categorybtn);//카테고리버튼
        Button addbtn = v.findViewById(R.id.mapUpload);//제보올리기 버튼
        ImageButton category1, category2, category3;//카테고리 설정되는거 1,2,3
        category1 = v.findViewById(R.id.category1);
        category2 = v.findViewById(R.id.category2);
        category3 = v.findViewById(R.id.category3);
        CheckBox[] pay = new CheckBox[3];//결제방법 체크박스 배열
        Integer[] payid = {R.id.paycheck_cash, R.id.paycheck_bank, R.id.paycheck_card};//인플레이팅용 id
        EditText menuinfo_edit = v.findViewById(R.id.menu_info);

        for (int i = 0; i < payid.length; i++) {
            pay[i] = (CheckBox) v.findViewById(payid[i]);//
        }
        ToggleButton[] cls_day = new ToggleButton[7];//휴무일 토글버튼 배열
        //휴무일 토글버튼 인플레이팅용 id
        Integer[] cls_id = {R.id.monday, R.id.tuesday, R.id.wendsday, R.id.thirsday, R.id.friday, R.id.saturday, R.id.sunday};
        for (int i = 0; i < cls_id.length; i++) {
            cls_day[i] = (ToggleButton) v.findViewById(cls_id[i]);//토글버튼 인플레이팅
        }
        mapView = (MapView) v.findViewById(R.id.market_map);//지도뷰
        mapView.onCreate(savedInstanceState);
        FragmentActivity activity = requireActivity();
        mapView.getMapAsync(this);
        EditText address = v.findViewById(R.id.location);//가게위치에딧텍스트 인플레이팅
        categoryButton.setOnClickListener(new View.OnClickListener() {//카테고리 설정
            @Override
            public void onClick(View v) {
                //카테고리 작은화면올라오는거
                bottomSheet1 = new category();
                bottomSheet1.show(activity.getSupportFragmentManager(), bottomSheet1.getTag());
                getParentFragmentManager().setFragmentResultListener("requestKey", activity, new FragmentResultListener() {
                    //데이터를 넘겨받아서 어떤 버튼 눌렸는지 확인
                    @Override
                    public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                        // 번들 키 값 입력
                        String result = bundle.getString("selected");
                        // 전달 받은 result 값(해당버튼이미지) 받아서 값설정
                        int imageResourceId = getResources().getIdentifier(result, "drawable", activity.getPackageName());
                        switch (i) {//버튼이 왼쪽부터 채워지게
                            case 0:
                                category1.setImageResource(imageResourceId);//이미지 찾아서 넣고
                                category1.setTag(result);//tag사용해서 나중에 DB에 넣을 값설정
                                returncategory(result);//카테고리버튼의 태그를 받아서 그거에 맞는 정보를 메뉴정보에 미리 생성해줌
                                i++;
                                break;
                            case 1:
                                if (category1.getTag().equals(result)) {//같은 카테고리 또 설정 못하게끔
                                    Toast.makeText(v.getContext(), "같은 카테고리를 등록할 수 없습니다", Toast.LENGTH_SHORT).show();
                                } else {
                                    category2.setImageResource(imageResourceId);
                                    category2.setTag(result);
                                    returncategory(result);
                                    i++;
                                }

                                break;
                            case 2:
                                if (category1.getTag().equals(result) || category2.getTag().equals(result)) {//같은 카테고리 또 설정 못하게끔
                                    Toast.makeText(v.getContext(), "같은 카테고리를 등록할 수 없습니다", Toast.LENGTH_SHORT).show();
                                } else {
                                    category3.setImageResource(imageResourceId);
                                    category3.setTag(result);
                                    returncategory(result);
                                    Toast.makeText(v.getContext(), result, Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                break;
                            case 3://3개 이상 등록 못하게
                                Toast.makeText(v.getContext(), "3개 이상 등록할 수 없습니다", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        category1.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,카테고리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category1.setTag("");
                category1.setImageResource(0);
                menuinfo_edit.setText("");
                if (i > 0)
                    i--;
            }
        });
        category2.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,카테고리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category2.setTag("");
                category2.setImageResource(0);
                menuinfo_edit.setText("");
                if (i > 0)
                    i--;
            }
        });
        category3.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,카테고리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category3.setTag("");
                category3.setImageResource(0);
                menuinfo_edit.setText("");
                if (i > 0)
                    i--;
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {//제보올리기 버튼
            @Override
            public void onClick(View v) {

                String Address = address.getText().toString();

                String sel_category = category1.getTag().toString() + " " + category2.getTag().toString() + " " + category3.getTag().toString();//카테고리버튼의 태그(선택된거이름)을 받아서 하나로 저장
                if (sel_category.trim().equals("")) {//합쳐진 카테고리문자열에 값이 없을경우
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    dialog = builder.setMessage("카테고리를 하나이상 체크해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                //체크된 결제방식 저장
                String howtopay = "";
                String temppay1;
                String temppay2;

                for (int i = 0; i < pay.length; i++) {
                    if (pay[i].isChecked()) {
                        temppay1 = howtopay;
                        temppay2 = pay[i].getText().toString();
                        if (temppay1.equals("")) {
                            howtopay = temppay1 + temppay2;
                        } else
                            howtopay = temppay1 + "," + temppay2;
                    }
                }
                if (howtopay.trim().isEmpty()) {//저장된결제방식 문자열에 값이 없을경우
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    dialog = builder.setMessage("결제방식을 하나이상 체크해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                //체크된 휴무일 저장
                String closeddays = "";
                String tempday1;
                String tempday2;
                for (int i = 0; i < cls_day.length; i++) {
                    if (cls_day[i].isChecked()) {
                        tempday1 = closeddays;
                        tempday2 = cls_day[i].getText().toString();
                        if (closeddays.equals("")) {
                            closeddays = tempday1 + tempday2;
                        } else
                            closeddays = tempday1 + "," + tempday2;
                    }
                }
                DecimalFormat formatter = new DecimalFormat("00");//시간,분을 두자리로 표현되게끔

                int s_hours = starttime.getHour();//시작시간
                int s_minutes = starttime.getMinute();//시작분
                int e_hours = endtime.getHour();//종료시간
                int e_minutes = endtime.getMinute();//종료분

                String formattedHour_s = formatter.format(s_hours);
                String formattedMinute_s = formatter.format(s_minutes);

                String formattedHour_e = formatter.format(e_hours);
                String formattedMinute_e = formatter.format(e_minutes);

                String operationtime = String.valueOf(formattedHour_s + ":" + formattedMinute_s + "~" + formattedHour_e + ":" + formattedMinute_e);//시간 전부 합쳐서 하나의 문자열로
                String menuinfo = menuinfo_edit.getText().toString();//저장된 메뉴정보 뽑아서 저장
                Response.Listener<String> responseListener = new Response.Listener<String>() {//리스폰스 사용
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");//success값 받아서 참이면 성공 거짓이면 실패
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            if (success) {
                                dialog = builder.setMessage("등록완료.").setPositiveButton("확인", null).create();
                            } else {
                                dialog = builder.setMessage("등록실패.").setNegativeButton("확인", null).create();
                            }
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddMapRequest addMapRequest = new AddMapRequest(Address, sel_category, howtopay, closeddays, operationtime, menuinfo, userid,responseListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                queue.add(addMapRequest);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {//mapview 설정

        MapsInitializer.initialize(this.getActivity());
        EditText address = v.findViewById(R.id.location);//가게위치 에딧텍스트 인플레이팅
        //LatLng centerOfMap = googleMap.getCameraPosition().target; //지도의 정중앙
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng centerOfMap = googleMap.getCameraPosition().target; //지도의 정중앙
                Geocoder g = new Geocoder(v.getContext(), Locale.KOREAN);//지오코딩
                try {//가게위치 에딧텍스트에 주소입력
                    address.setText(g.getFromLocation(centerOfMap.latitude, centerOfMap.longitude, 1).get(0).getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        address.setOnKeyListener(new View.OnKeyListener() {//에딧텍스트에 주소 직접 넣는경우
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER://엔터치면 주소를 좌표로 변환
                        if (!address.getText().toString().equals("")) {//값이 있는경우만
                            Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                            List<android.location.Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(address.getText().toString(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 위도, 경도가 있으면 지도의 위치를 변경합니다.
                            if (addresses != null && addresses.size() > 0) {//변환한 좌표로 mapview 목표좌표 변경
                                android.location.Address addressObject = addresses.get(0);
                                LatLng latLng = new LatLng(addressObject.getLatitude(), addressObject.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            }
                        }

                }
                return false;
            }
        });
    }
    private void returncategory(String category) {//카테고리버튼의 태그를 받아서 그거에 맞는 정보를 메뉴정보에 미리 생성해줌

        String info=category;
        EditText menuinfo_edit = v.findViewById(R.id.menu_info);
        String ex_text = menuinfo_edit.getText().toString();
        Log.d("test", category);
        switch (info) {
            case "taiyaki":
                if(ex_text.isEmpty()){//메뉴정보에 값이 없을경우
                    menuinfo_edit.setText(ex_text+"붕어빵 : 1개/   원");
                }else//있을경우 한줄 넘기고 붙이기
                    menuinfo_edit.setText(ex_text+"\n"+"붕어빵 : 1개/   원");
                break;
            case "k_pancake":
                if(ex_text.isEmpty()){
                    menuinfo_edit.setText(ex_text+"호떡 : 1개/   원");
                }else
                    menuinfo_edit.setText(ex_text+"\n"+"호떡 : 1개/   원");
                break;
            case "ricecake":
                if(ex_text.isEmpty()){
                    menuinfo_edit.setText(ex_text+"떡볶이 : 1개/   원");
                }else
                    menuinfo_edit.setText(ex_text+"\n"+"떡볶이 : 1개/   원");
                break;
            case "takoyaki":
                if(ex_text.isEmpty()){
                    menuinfo_edit.setText(ex_text+"타코야키 : 1개/   원");
                }else
                    menuinfo_edit.setText(ex_text+"\n"+"타코야키 : 1개/   원");
                break;
            case "toast":
                if(ex_text.isEmpty()){
                    menuinfo_edit.setText(ex_text+"토스트 : 1개/   원");
                }else
                    menuinfo_edit.setText(ex_text+"\n"+"토스트 : 1개/   원");
                break;
            case "tanghuru":
                if(ex_text.isEmpty()){
                    menuinfo_edit.setText(ex_text+"탕후루 : 1개/   원");
                }else
                    menuinfo_edit.setText(ex_text+"\n"+"탕후루 : 1개/   원");
                break;
        }
    }
}