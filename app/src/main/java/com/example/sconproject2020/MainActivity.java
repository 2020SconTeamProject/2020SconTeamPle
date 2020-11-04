package com.example.sconproject2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        MapFragment mapFragment = new MapFragment();
        SettingFragment settingFragment = new SettingFragment();
        fragments.add(homeFragment);
        fragments.add(calendarFragment);
        fragments.add(mapFragment);
        fragments.add(settingFragment);

        BottomNavigationView navigationView =findViewById(R.id.nav_host_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}