package com.example.sconproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();


    BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private MapFragment mapFragment = new MapFragment();
    private SettingFragment settingFragment = new SettingFragment();

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
                    case R.id.menuitem_setting:
                        transaction.replace(R.id.main_frame, settingFragment).commitAllowingStateLoss();
                        break;
                }

                return true;
            }
        });
    }
}