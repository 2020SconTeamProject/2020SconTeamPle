package com.example.sconproject2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class SSLConnect {
    // always verify the host - dont check for certificate
    final  HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - don't check for any certificate
     */
    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpsURLConnection postHttps(String url, int connTimeout, int readTimeout) {
        trustAllHosts();

        HttpsURLConnection https = null;
        try {
            https = (HttpsURLConnection) new URL(url).openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            https.setConnectTimeout(connTimeout);
            https.setReadTimeout(readTimeout);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return https;
    }
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SSLConnect ssl = new SSLConnect();
        ssl.postHttps("https://www.andong.go.kr/openapi/service/welfareChildCareService/getList?ServiceKey=gWKQlqZhHhnPKeS33joGOfyhDsiGaJ9oObhCjJCXlzEsoHHlP6zYiiHL2oPlB1iYLEI77%2BFvoU%2FFeWb5tMkSmw%3D%3D",1000,1000);
        StrictMode.enableDefaults();

        TextView status1 = (TextView)findViewById(R.id.result); //파싱된 결과확인!

        boolean CInt = false, CName = false, CRoad = false, CTel = false, CType = false;

        String cint = null, cname = null, croad = null, ctel = null, ctype = null;


        try{
            URL url = new URL("https://www.andong.go.kr/openapi/service/welfareChildCareService/getList?ServiceKey=gWKQlqZhHhnPKeS33joGOfyhDsiGaJ9oObhCjJCXlzEsoHHlP6zYiiHL2oPlB1iYLEI77%2BFvoU%2FFeWb5tMkSmw%3D%3D"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            status1.setText("시작");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("CInt")){ //title 만나면 내용을 받을수 있게 하자
                            CInt = true;
                        }
                        if(parser.getName().equals("CName")){ //cintess 만나면 내용을 받을수 있게 하자
                            CName = true;
                        }
                        if(parser.getName().equals("CRoad")){ //mapx 만나면 내용을 받을수 있게 하자
                            CRoad = true;
                        }
                        if(parser.getName().equals("CTel")){ //mapy 만나면 내용을 받을수 있게 하자
                            CTel = true;
                        }
                        if(parser.getName().equals("CType")){ //mapy 만나면 내용을 받을수 있게 하자
                            CType = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText("에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(CInt){ //isTitle이 true일 때 태그의 내용을 저장.
                            cint = parser.getText();
                            CInt = false;
                        }
                        if(CName){ //iscintess이 true일 때 태그의 내용을 저장.
                            cname = parser.getText();
                            CName = false;
                        }
                        if(CRoad){ //isMapx이 true일 때 태그의 내용을 저장.
                            croad = parser.getText();
                            CRoad = false;
                        }
                        if(CTel){ //isMapy이 true일 때 태그의 내용을 저장.
                            ctel = parser.getText();
                            CTel = false;
                        }
                        if(CType){ //isMapy이 true일 때 태그의 내용을 저장.
                             ctype= parser.getText();
                            CType = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            status1.append("1 : "+ cint +"\n 2: "+ cname +"\n 3 : " + croad
                                    +"\n 4 : " + ctel +  "\n 5 : " +ctype);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("실패");
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            Log.e("mainActiviy:",""+e);
        }
    }
}