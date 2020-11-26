package com.example.sconproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sconproject2020.Calendar.CalendarFragment;
import com.example.sconproject2020.Home.HomeFragment;
import com.example.sconproject2020.Map.MapFragment;
import com.example.sconproject2020.Diary.DiaryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    private FragmentManager fragmentManager = getSupportFragmentManager();

    BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private MapFragment mapFragment = new MapFragment();
    private DiaryFragment diaryFragment = new DiaryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, homeFragment).commitAllowingStateLoss();

        bottomNavigationView =findViewById(R.id.main_bottomView);

        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(item.getItemId()){
                    case R.id.menuitem_home:
                        transaction.replace(R.id.main_frame, homeFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menuitem_calendar:
                        transaction.replace(R.id.main_frame, calendarFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menuitem_map:
                        transaction.replace(R.id.main_frame, mapFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menuitem_diary:
                        transaction.replace(R.id.main_frame, diaryFragment).commitAllowingStateLoss();
                        break;
                }

                return true;
            }
        });
    }
}