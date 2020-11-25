package com.example.sconproject2020.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.sconproject2020.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;

    ArrayList<Facility> kidCafe, kidHouse, kinderGarden, playGround;
    ArrayList<String> latitudeArr = new ArrayList<>(), longitudeArr = new ArrayList<>(), nameArr = new ArrayList<>();
    CheckBox kidCafeChkBox, kidHouseChkBox, kinderGardenChkBox, playGroundChkBox;
    private String name, latitude, longitude;
    private boolean kcIsChecked, khIsChecked, kgIsChecked, pgIsChecked;
    int cameraZoom=10;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = (MapView) view.findViewById(R.id.map);

        kcIsChecked = khIsChecked = kgIsChecked = pgIsChecked = false;

        kidCafeChkBox = view.findViewById(R.id.kidCafeCheckbox);
        kidHouseChkBox = view.findViewById(R.id.kidHouseCheckbox);
        kinderGardenChkBox = view.findViewById(R.id.kindergardenCheckbox);
        playGroundChkBox = view.findViewById(R.id.playGroundCheckbox);

        kidCafeChkBox.setChecked(false);
        kinderGardenChkBox.setChecked(false);
        playGroundChkBox.setChecked(false);
        kidHouseChkBox.setChecked(false);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        kidCafe = new ArrayList<>();
        kidHouse = new ArrayList<>();
        kinderGarden = new ArrayList<>();
        playGround = new ArrayList<>();

        xmlParsing(R.raw.kids_cafe_res);
        for(int i=0;i<50;i++){
            kidCafe.add(new Facility(nameArr.get(i), latitudeArr.get(i), longitudeArr.get(i)));
        }
        xmlParsing(R.raw.kid_house_res);
        for(int i=0;i<49;i++){
            kidHouse.add(new Facility(nameArr.get(i), latitudeArr.get(i), longitudeArr.get(i)));
        }
        xmlParsing(R.raw.kindergarden_res);
        for(int i=0;i<50;i++){
            kinderGarden.add(new Facility(nameArr.get(i), latitudeArr.get(i), longitudeArr.get(i)));
        }
        xmlParsing(R.raw.playground_res);
        for(int i=0;i<50;i++){
            playGround.add(new Facility(nameArr.get(i), latitudeArr.get(i), longitudeArr.get(i)));
        }

//        Log.e("kd",""+kidHouse.size());
//        Log.e("kc",""+kidCafe.size());
//        Log.e("kg",""+kinderGarden.size());
//        Log.e("pg",""+playGround.size());
//
//        for(int i=0;i<50;i++){
//
//            Log.e("Outname"+i,playGround.get(i).getName());
//            Log.e("Outlatitude"+i,playGround.get(i).getLatitude());
//            Log.e("Outlongitude"+i,playGround.get(i).getLongitude());
//        }

        return view;
    }

    void xmlParsing(int res){
        nameArr.clear();
        latitudeArr.clear();
        longitudeArr.clear();
        InputStream inputStream = getResources().openRawResource(res);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        XmlPullParserFactory factory = null;
        XmlPullParser xpp = null;

        int c1 = 0, c2 = 0, c3 = 0;
        try {
            factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            xpp.setInput(reader);
            int parserEvent = xpp.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                if (parserEvent == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("REFINE_WGS84_LAT")){
                        parserEvent = xpp.next();
                        if (parserEvent == XmlPullParser.TEXT) {
                            latitude = xpp.getText();
                            Log.e("latitude"+c1,latitude);
                            latitudeArr.add(latitude);
                            c1++;
                        }
                    }
                    else if(xpp.getName().equals("REFINE_WGS84_LOGT")){
                        parserEvent = xpp.next();
                        if (parserEvent == XmlPullParser.TEXT) {
                            longitude = xpp.getText();
                            Log.e("longitude"+c2,longitude);
                            longitudeArr.add(longitude);
                            c2++;
                        }
                    }
                    else if(xpp.getName().equals("FACLT_NM") || xpp.getName().equals("BIZPLC_NM") || xpp.getName().equals("PLAY_FACLT_NM")) {
                        parserEvent = xpp.next();
                        if (parserEvent == XmlPullParser.TEXT) {
                            name = xpp.getText();
                            Log.e("name" + c3, name);
                            nameArr.add(name);
                            c3++;
                        }
                    }
                }
                parserEvent = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    void checkBoxMap(boolean kg, boolean kc, boolean kh, boolean pg, GoogleMap googleMap){
        int i;
        if(kg){
            for(i=0;i < 50;i++){
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng facility = new LatLng(Double.parseDouble(kinderGarden.get(i).getLatitude()), Double.parseDouble(kinderGarden.get(i).getLongitude()));

                markerOptions.position(facility);
                markerOptions.title(kinderGarden.get(i).getName());

                googleMap.addMarker(markerOptions);

            }
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(cameraZoom));
        }
        if(kc){
            for(i=0;i < 50;i++){
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng facility = new LatLng(Double.parseDouble(kidCafe.get(i).getLatitude()), Double.parseDouble(kidCafe.get(i).getLongitude()));

                markerOptions.position(facility);
                markerOptions.title(kidCafe.get(i).getName());

                googleMap.addMarker(markerOptions);

//                if(i == 49){
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(facility));
//                }
            }
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(cameraZoom));
        }
        if(kh){
            for(i=0;i < 49;i++){
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng facility = new LatLng(Double.parseDouble(kidHouse.get(i).getLatitude()), Double.parseDouble(kidHouse.get(i).getLongitude()));

                markerOptions.position(facility);
                markerOptions.title(kidHouse.get(i).getName());

                googleMap.addMarker(markerOptions);

//                if(i == 49){
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(facility));
//                }
            }
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(cameraZoom));
        }
        if(pg){
            for(i=0;i < 50;i++){
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng facility = new LatLng(Double.parseDouble(playGround.get(i).getLatitude()), Double.parseDouble(playGround.get(i).getLongitude()));

                markerOptions.position(facility);
                markerOptions.title(playGround.get(i).getName());

                googleMap.addMarker(markerOptions);

            }
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(cameraZoom));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng initialPosition = new LatLng(37.4750407232,126.8473133613);
        markerOptions.position(initialPosition);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPosition));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(cameraZoom));

        kinderGardenChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                googleMap.clear();
                kgIsChecked = b;
                checkBoxMap(kgIsChecked,kcIsChecked,khIsChecked,pgIsChecked,googleMap);
            }
        });

        kidCafeChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                googleMap.clear();
                kcIsChecked = b;
                checkBoxMap(kgIsChecked,kcIsChecked,khIsChecked,pgIsChecked,googleMap);
            }
        });

        kidHouseChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                googleMap.clear();
                khIsChecked = b;
                checkBoxMap(kgIsChecked,kcIsChecked,khIsChecked,pgIsChecked,googleMap);
            }
        });

        playGroundChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                googleMap.clear();
                pgIsChecked = b;
                checkBoxMap(kgIsChecked,kcIsChecked,khIsChecked,pgIsChecked,googleMap);
            }
        });
    }
}