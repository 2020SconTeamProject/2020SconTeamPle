package com.example.sconproject2020.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<String> asd;
    List<String> REFINE_WGS84_LAT;
    List<String> REFINE_WGS84_LOGT;
    List<String> FACLT_NM;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        REFINE_WGS84_LAT = new ArrayList<>();
        REFINE_WGS84_LOGT = new ArrayList<>();
        FACLT_NM = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.res);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        XmlPullParserFactory factory = null;
        XmlPullParser xpp = null;


        String adsf = null;
        int c1=0,c2=0,c3 = 0;
        try {
            factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            xpp.setInput(reader);
            int parserEvent = xpp.getEventType();
            Log.e("asd", xpp.getName() + "");
            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        //  Log.e("asd",xpp.getEventType()+"");
                        if (xpp.getName().equals("REFINE_WGS84_LAT")) {
                            parserEvent = xpp.next();
                            if (parserEvent == XmlPullParser.TEXT) {
                                adsf = xpp.getText();
                                REFINE_WGS84_LAT.add(adsf);
                                Log.e("lat", "value : " + REFINE_WGS84_LAT.get(c1));
                                c1++;
                            }
                        }
                        else if (xpp.getName().equals("REFINE_WGS84_LOGT")){
                            parserEvent = xpp.next();
                            if (parserEvent == XmlPullParser.TEXT) {
                                adsf = xpp.getText();
                                REFINE_WGS84_LOGT.add(adsf);
                                Log.e("logt",  "value : " + REFINE_WGS84_LOGT.get(c2));
                                c2++;
                            }
                        }
                        else if (xpp.getName().equals("FACLT_NM")){
                            parserEvent = xpp.next();
                            if (parserEvent == XmlPullParser.TEXT) {
                                adsf = xpp.getText();
                                FACLT_NM.add(adsf);
                                Log.e("name",  "value : " + FACLT_NM.get(c3));
                                c3++;
                            }
                        }
//
                }
                parserEvent = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

           return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        int size = REFINE_WGS84_LAT.size();
        for(int i=0;i < 200;i++){
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng facility = new LatLng(Double.parseDouble(REFINE_WGS84_LAT.get(i)), Double.parseDouble(REFINE_WGS84_LOGT.get(i)));

            markerOptions.position(facility);
            markerOptions.title(FACLT_NM.get(i));

            googleMap.addMarker(markerOptions);

            if(i == 199){
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(facility));
            }
        }
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}