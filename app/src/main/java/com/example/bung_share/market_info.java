package com.example.bung_share;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class market_info extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public market_info() {
        // Required empty public constructor
    }
    public void onDestroyView() {
        super.onDestroyView();
        // 이전에 있던 값 제거용
        removeDynamicViews();
    }

    private void removeDynamicViews() {
        // LinearLayout에서 모든 자식 뷰를 제거
        linearLayout.removeAllViews();
    }

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

    ImageView logo;
    TextView market_info;
    Button like_button;
    Button review_button;
    LinearLayout linearLayout;

    info_review_fragment bottomSheet1;
    view_review bottomSheet2;
    Button view_review, make_review,nav_button;
    SupportMapFragment mapFrag;
    LatLng latLng;
    private GoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            v = inflater.inflate(R.layout.fragment_market_info, container, false);
        } catch (InflateException e) {
            // Handle the exception
        }

        Bundle bundle = getArguments();
        String value = bundle.getString("storeId");
        String id = bundle.getString("userid");


        logo = v.findViewById(R.id.logo);
        market_info = v.findViewById(R.id.market_info);
        like_button = v.findViewById(R.id.likebutton);
        review_button = v.findViewById(R.id.review_button);
        linearLayout = v.findViewById(R.id.info_list);
        //다른가게 화면 갔었어도 원래 상태 되게끔 초기화
        like_button.setTag("off");
        like_button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.loveit_off, 0, 0, 0);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String category = jsonResponse.getString("category");
                    String address = jsonResponse.getString("address");
                    Double rating = jsonResponse.getDouble("rating");
                    int dibsCount = jsonResponse.getInt("dibsCount");
                    int reviewCount = jsonResponse.getInt("reviewCount");
                    String operationHours = jsonResponse.getString("operationHours");
                    String closedays = jsonResponse.getString("closedays");
                    String status = jsonResponse.getString("status");
                    String pay = jsonResponse.getString("pay");
                    String menu = jsonResponse.getString("menu");
                    boolean isdibed = jsonResponse.getBoolean("isDibsed");

                    market_info marketInfoFragment = new market_info();

                    // Market_info 프래그먼트에 마커의 정보를 전달합니다.
                    Bundle bundle = new Bundle();
                    bundle.putString("storeId", value);
                    marketInfoFragment.setArguments(bundle);
                    Log.d("test", address);
                    if(closedays.equals("")){
                        market_info.setText(address + "\n" + operationHours + "\n휴무일: 없음" + "\n결제방법: " + pay);
                    }else
                        market_info.setText(address + "\n" + operationHours + "\n휴무일: "+closedays + "\n결제방법: " + pay);
                    if (status.equals("closed")) {
                        logo.setImageResource(R.drawable.close_icon);
                    } else {
                        String maincategory = category.substring(0, category.indexOf(" "));
                        String[] imagename = {"taiyaki", "k_pancake", "ricecake", "takoyaki","toast" ,"tanghuru"};
                        Integer[] image = {R.drawable.taiyaki, R.drawable.k_pancake, R.drawable.ricecake, R.drawable.takoyaki, R.drawable.toast, R.drawable.tanghuru};
                        for (int i = 0; i < imagename.length; i++) {
                            if (imagename[i].equals(maincategory)) {
                                logo.setImageResource(image[i]);
                            }
                        }
                    }
                    if(isdibed){
                        like_button.setTag("on");
                        like_button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.loveit_on, 0, 0, 0);
                    }else{
                        like_button.setTag("off");
                    }
                    like_button.setText("찜 :" + dibsCount + "개");

                    like_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("test", response.toString());
                                        JSONObject jsonResponse = new JSONObject(response);
                                        Integer newdibsCount = jsonResponse.getInt("dibsCount");

                                        like_button.setText("찜 :"+newdibsCount+"개");

                                        if (like_button.getTag().equals("off")) {
                                            like_button.setTag("on");
                                            like_button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.loveit_on, 0, 0, 0);
                                        } else {
                                            like_button.setTag("off");
                                            like_button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.loveit_off, 0, 0, 0);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            Add_dibs_Request addDibsRequest = new Add_dibs_Request(like_button.getTag().toString(), value,id,responseListener);
                            RequestQueue queue = Volley.newRequestQueue(v.getContext());
                            queue.add(addDibsRequest);
                        }
                    });
                    mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.market_map);
                    if (mapFrag == null) {
                        mapFrag = SupportMapFragment.newInstance();
                        getChildFragmentManager().beginTransaction().replace(R.id.market_map, mapFrag).commit();
                    }

                    mapFrag.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            gMap = googleMap;
                            Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(address, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressObject = addresses.get(0);
                                latLng = new LatLng(addressObject.getLatitude(), addressObject.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                markerOptions.position(latLng);
                                gMap.addMarker(markerOptions);
                            }
                        }
                    });
                    nav_button = v.findViewById(R.id.how_to_go);
                    nav_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PackageManager packageManager = v.getContext().getPackageManager();
                            if (isPackageInstalled("com.google.android.apps.maps", packageManager)) {
                                // 구글맵 앱이 설치되어 있으면 길찾기 실행
                                String uri = "https://www.google.co.kr/maps/dir//"+address;
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);
                            } else {
                                // 구글맵 앱이 설치되어 있지 않으면 다운로드하라는 메시지 표시
                                Toast.makeText(v.getContext(), "Google Maps app is not installed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    review_button.setText("리뷰 :" + reviewCount + "개");
                    //TODO 리뷰 리스폰스추가

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(50)
                    );
                    String[] menuarray = menu.split("\n");
                    for(int i=0; i<menuarray.length; i++){
                        TextView textView = new TextView(v.getContext());
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText(menuarray[i]);
                        textView.setPadding(dpToPx(20), 0, 0, 0);
                        textView.setCompoundDrawablePadding(dpToPx(-200));
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.review, 0, 0, 0);
                        LinearLayout linearLayout = v.findViewById(R.id.info_list);
                        linearLayout.addView(textView);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        InfoRequest infoRequest = new InfoRequest(value, id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(infoRequest);

        make_review = v.findViewById(R.id.make_review);
        view_review = v.findViewById(R.id.review_button);
        FragmentActivity activity = requireActivity();
        make_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet1 = new info_review_fragment();

                // Market_info 프래그먼트에 마커의 정보를 전달합니다.
                Bundle bundle = new Bundle();
                bundle.putString("storeId", value);
                bundle.putString("userid",id);
                bottomSheet1.setArguments(bundle);
                bottomSheet1.show(activity.getSupportFragmentManager(), bottomSheet1.getTag());
            }
        });
        view_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet2 = new view_review();
                Bundle bundle = new Bundle();
                bundle.putString("storeId", value);
                bottomSheet2.setArguments(bundle);
                bottomSheet2.show(activity.getSupportFragmentManager(), bottomSheet2.getTag());
            }
        });

        return v;
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    // 이벤트 수신을 위한 메서드 작성
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomEventReceived(info_review_fragment.CustomEvent event) {
        // 전송된 값을 가져와서 버튼 텍스트 변경
        int receivedValue = event.getValue();
        review_button.setText("리뷰 :" + String.valueOf(receivedValue) + "개"); // 버튼의 텍스트를 전송된 값으로 변경
    }

    // EventBus에 이벤트 수신 등록
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    // EventBus에 이벤트 수신 등록 해제
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
