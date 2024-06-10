package com.example.bung_share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    SupportMapFragment mapFrag;
    private GoogleMap gMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public home() {
        // Required empty public constructor
    }

    public static home newInstance(String param1, String param2) {
        home fragment = new home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    static View v;

    @Override
    public void onDestroyView() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            v = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
            // 구글맵 View가 이미 inflate되어 있는 상태이므로, 에러를 무시합니다.
        }
        Bundle bundle = getArguments();
        String id = bundle.getString("userid");//메인액티비티에서 날려준 번들의 id값 받아오기
        Log.d("test","id"+id);//값 확인용

        ImageButton mylocation = v.findViewById(R.id.mylocation); //내위치 버튼
        androidx.appcompat.widget.SearchView getaddress = v.findViewById(R.id.search);//홈화면 검색창
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);//지도프래그먼트
        if (mapFrag == null) {
            mapFrag = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFrag).commit();
        }

        mapFrag.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {//지도생성될때
                gMap = googleMap;
                showCurrentLocation();
                ArrayList<String> storeId = new ArrayList<String>();//가게아이디 리스트
                ArrayList<String> category = new ArrayList<String>();//카테고리 리스트
                ArrayList<String> address = new ArrayList<String>();//주소 리스트
                MarkerOptions markerOptions = new MarkerOptions();//마커 옵션

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {//각각 가게의 정보를 받아서 저장
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                storeId.add(jsonObject.getString("storeId"));
                                category.add(jsonObject.getString("category"));
                                address.add(jsonObject.getString("address"));
                                //Log.d("test", i + "/" + storeId.get(i) + "/" + address.get(i)+"/"+category.get(i));
                                String temp = category.get(i).toString();//카테고리 값 문자열을 받아서
                                //Log.d("test", "ㅠㅠㅠㅠ"+temp);
                                String icon = temp.substring(0, temp.indexOf(" "));//앞에 한종류(메인카테고리)만 잘라냄
                                // 주소를 위도, 경도로 변환
                                Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                                List<android.location.Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocationName(address.get(i), 1);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // 위도, 경도가 있으면 마커 생성
                                if (addresses != null && !addresses.isEmpty()) {
                                    android.location.Address addressObject = addresses.get(0);
                                    LatLng latLng = new LatLng(addressObject.getLatitude(), addressObject.getLongitude());
                                    markerOptions.position(latLng);

                                    // icon에 저장된 카테고리값과 같은 사진을 찾아서 그 id값을 저장
                                    int imageResourceId = getResources().getIdentifier(icon, "drawable", v.getContext().getPackageName());

                                    //마커 아이콘 커스텀용 bitmap생성
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
                                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);//마커아이콘사진 크기설정

                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));//아이콘 변경
                                    Marker marker = gMap.addMarker(markerOptions); // 마커생성
                                    marker.setTag(storeId.get(i)); //가게아이디를 태그로 설정

                                    gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {//마커 클릭시
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            String title = (String) marker.getTag(); // Get the store ID from the marker tag
                                            market_info marketInfoFragment = new market_info();

                                            // Market_info 프래그먼트에 마커의 정보 전달
                                            Bundle bundle = new Bundle();
                                            bundle.putString("storeId", title);
                                            bundle.putString("userid",id);
                                            marketInfoFragment.setArguments(bundle);

                                            // Market_info 프래그먼트로 화면을 전환
                                            requireActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.main_container, marketInfoFragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                            return false;
                                        }
                                    });
                                    Log.d("test", storeId.get(i) + "추가완료");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                StoreRequest storeRequest = new StoreRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                queue.add(storeRequest);
            }
        });

        mylocation.setOnClickListener(new View.OnClickListener() {//내위치 버튼 누를시 이벤트
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });
        getaddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//검색창에 주소를 직접 입력할 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!getaddress.getQuery().toString().equals("")){//값이 비어있지 않은 경우에만 실행
                    Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                    List<android.location.Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(getaddress.getQuery().toString(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 위도, 경도가 있으면 지도의 위치를 변경.
                    if (addresses != null && addresses.size() > 0) {//변환한 좌표로 mapview 목표좌표 변경
                        android.location.Address addressObject = addresses.get(0);
                        LatLng latLng = new LatLng(addressObject.getLatitude(), addressObject.getLongitude());
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });






        return v;
    }
    private void showCurrentLocation() { //locationManager를 사용해서 내위치를 찾아서 보내줌
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 16));
                locationManager.removeUpdates(this); // 위치 업데이트 중지
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

}
