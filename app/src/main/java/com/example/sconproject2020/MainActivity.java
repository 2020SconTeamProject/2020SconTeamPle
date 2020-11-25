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
        List<String> latitude = new ArrayList<>();
        List<String> longitude = new ArrayList<>();
        List<String> NAME = new ArrayList<>();

        String buffer = null;
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
                                buffer = xpp.getText();
                                latitude.add(buffer);
                                Log.e("lat", "value : "+c1 +"  "+ latitude.get(c1));
                                c1++;
                            }
                        }
                        else if (xpp.getName().equals("REFINE_WGS84_LOGT")){
                            parserEvent = xpp.next();
                            if (parserEvent == XmlPullParser.TEXT) {
                                buffer = xpp.getText();
                                longitude.add(buffer);
                                Log.e("logt",  "value : "+c2 +"  " + longitude.get(c2));
                                c2++;
                            }
                        }
                        else if (xpp.getName().equals("FACLT_NM")||xpp.getName().equals("BIZPLC_NM")||xpp.getName().equals("PLAY_FACLT_NM")){
                            parserEvent = xpp.next();
                            if (parserEvent == XmlPullParser.TEXT) {
                                buffer = xpp.getText();
                                NAME.add(buffer);
                                Log.e("name",  "value : "+c3 +"  " + NAME.get(c3));
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
