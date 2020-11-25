package com.example.sconproject2020;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.SimpleAdapter;

public class MainActivity extends AppCompatActivity {
    private List<String> asd;
    private TextView tv;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.xmltext);
        lv = (ListView) findViewById(R.id.xmllist);
        InputStream inputStream = getResources().openRawResource(R.raw.res);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        XmlPullParserFactory factory = null;
        XmlPullParser xpp = null;
        List<String> REFINE_WGS84_LAT = new ArrayList<>();
        List<String> REFINE_WGS84_LOGT = new ArrayList<>();
        List<String> FACLT_NM = new ArrayList<>();

        String adsf = null;
        int c1=0,c2=0,c3 = 0;
        try {
            factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            xpp.setInput(reader);
            int parserEvent = xpp.getEventType();
            Log.e("asd", xpp.getName() + "");
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
//                Log.e("asd",xpp.getName());
//                Log.e("asd",xpp.getText());
                //  Log.d("asd",xpp.getEventType()+"");

                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        //  Log.e("asd",xpp.getEventType()+"");
                        if (xpp.getName().equals("REFINE_WGS84_LAT")) {
                            parserEvent = xpp.next();
                            Log.e("asddf",parserEvent+"");
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
    }
}
