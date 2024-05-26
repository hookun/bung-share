package com.example.bung_share;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
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
        mapView = (MapView) v.findViewById(R.id.market_map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

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
    public void onMapReady(@NonNull GoogleMap googleMap) {

        MapsInitializer.initialize(this.getActivity());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.141233, 126.925594), 14);
        MarkerOptions yourMarkerInstance = new MarkerOptions();
        EditText address = v.findViewById(R.id.location);

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {
                MarkerOptions yourMarkerInstance = new MarkerOptions();
                yourMarkerInstance.draggable(false);

                googleMap.clear();
                LatLng centerOfMap = googleMap.getCameraPosition().target;
                yourMarkerInstance.position(centerOfMap);
                googleMap.addMarker(yourMarkerInstance);
                Geocoder g = new Geocoder(v.getContext(), Locale.KOREAN);
                try {
                    address.setText(g.getFromLocation(centerOfMap.latitude, centerOfMap.longitude, 1).get(0).getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if(!address.getText().toString().equals("")){
                            Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                            List<android.location.Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(address.getText().toString(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 위도, 경도가 있으면 지도의 위치를 변경합니다.
                            if (addresses != null && addresses.size() > 0) {
                                android.location.Address addressObject = addresses.get(0);
                                LatLng latLng = new LatLng(addressObject.getLatitude(), addressObject.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }
                        }

                }
                return false;
            }
        });
    }
}