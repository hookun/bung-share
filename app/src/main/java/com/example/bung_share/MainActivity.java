package com.example.bung_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    home homeFragment;
    addmap mapFragment;
    user userFragment;
    market_info market_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeFragment = new home();
        mapFragment = new addmap();
        userFragment = new user();
        market_info = new market_info();
        Intent inintent = getIntent();
        String id = inintent.getStringExtra("id");
        Bundle bundle = new Bundle();
        // 번들 객체에 값 저장
        bundle.putString("userid", id);
        // 프래그먼트에 번들 설정
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation_view);
        navigationBarView.setSelectedItemId(R.id.home);//시작시 선택된 아이콘 홈아이콘이 되게끔(디폴트가 왼쪽이라 그런지 설정안하면 맵아이콘에 선택되어있음)
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();//이부분 보소서에 switch쓸라는데 안됐다고 할것
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.addmap) {
                    Bundle bundle = new Bundle();
                    // 번들 객체에 값 저장
                    bundle.putString("key", id);
                    // 프래그먼트에 번들 설정
                    mapFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                    return true;
                } else if (itemId == R.id.user) {
                   //getSupportFragmentManager().beginTransaction().replace(R.id.main_container,market_info).commit();
                    Bundle bundle = new Bundle();
                    // 번들 객체에 값 저장
                    bundle.putString("key", id);
                    // 프래그먼트에 번들 설정
                    userFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, userFragment).commit();
                    return true;
                }
                return false;
            }
        });

    }

}