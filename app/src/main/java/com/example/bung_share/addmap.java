package com.example.bung_share;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
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
    MapView mapView;
    category bottomSheet1;
    int i = 0;//카테고리 설정용
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try{
            v = inflater.inflate(R.layout.fragment_addmap, container, false);
        }catch (InflateException e){
            // 구글맵 View가 이미 inflate되어 있는 상태이므로, 에러를 무시합니다.
        }
        ToggleButton monday = v.findViewById(R.id.monday);
        TimePicker starttime = v.findViewById(R.id.timestart);
        TimePicker endtime = v.findViewById(R.id.timeend);
        starttime.setIs24HourView(true);
        endtime.setIs24HourView(true);
        Button categoryButton = v.findViewById(R.id.categorybtn);
        ImageButton category1, category2, category3;
        category1=v.findViewById(R.id.category1);
        category2=v.findViewById(R.id.category2);
        category3=v.findViewById(R.id.category3);
        mapView = (MapView) v.findViewById(R.id.market_map);
        mapView.onCreate(savedInstanceState);
        FragmentActivity activity = requireActivity();
        mapView.getMapAsync(this);

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
                        switch (i){//버튼이 왼쪽부터 채워지게
                            case 0:
                                category1.setImageResource(imageResourceId);//이미지 찾아서 넣고
                                category1.setTag(result);//tag사용해서 나중에 DB에 넣을 값설정
                                i++;
                                break;
                            case 1:
                                if(category1.getTag().equals(result)){//같은 카테고리 또 설정 못하게끔 
                                    Toast.makeText(v.getContext(),"같은 카테고리를 등록할 수 없습니다",Toast.LENGTH_SHORT).show();
                                }else {
                                    category2.setImageResource(imageResourceId);
                                    category2.setTag(result);
                                    i++;
                                }

                                break;
                            case 2:
                                if(category1.getTag().equals(result)||category2.getTag().equals(result)){//같은 카테고리 또 설정 못하게끔 
                                    Toast.makeText(v.getContext(),"같은 카테고리를 등록할 수 없습니다",Toast.LENGTH_SHORT).show();
                                }else {
                                    category3.setImageResource(imageResourceId);
                                    category3.setTag(result);
                                    Toast.makeText(v.getContext(),result,Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                break;
                            case 3://3개 이상 등록 못하게
                                Toast.makeText(v.getContext(),"3개 이상 등록할 수 없습니다",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        category1.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,테코리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category1.setTag("");
                category1.setImageResource(0);
                if(i>0)
                    i--;
            }
        });
        category2.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,테코리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category2.setTag("");
                category2.setImageResource(0);
                if(i>0)
                    i--;
            }
        });
        category3.setOnClickListener(new View.OnClickListener() {//버튼 누르면 이미지랑 태그 값 날리는거,테코리 버튼 이미지 순서용
            @Override
            public void onClick(View v) {
                category3.setTag("");
                category3.setImageResource(0);
                if(i>0)
                    i--;
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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.141233, 126.925594), 14);
        //처음 생성될때 마커 설정용
        MarkerOptions yourMarkerInstance = new MarkerOptions();//사용할 마커 설정
        EditText address = v.findViewById(R.id.location);//가게위치 인플레이팅
        LatLng centerOfMap = googleMap.getCameraPosition().target; //지도의 정중앙
        yourMarkerInstance.position(centerOfMap);//마커 지도 정중앙으로
        googleMap.addMarker(yourMarkerInstance);//마커 생성

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                googleMap.clear();//이전 마커 지우는 용도
                LatLng centerOfMap = googleMap.getCameraPosition().target; //지도의 정중앙
                yourMarkerInstance.position(centerOfMap);//마커 지도 정중앙으로
                googleMap.addMarker(yourMarkerInstance);//마커 생성
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
                        if(!address.getText().toString().equals("")){
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

}
