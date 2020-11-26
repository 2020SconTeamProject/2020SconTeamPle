package com.example.sconproject2020.Home;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sconproject2020.Calendar.CalendarRecyclerAdapter;
import com.example.sconproject2020.Calendar.CalendarRecyclerItem;
import com.example.sconproject2020.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    TextView nowTodoTextView, noPlanTextView;
    RecyclerView recyclerView;
    ArrayList<HomeRecyclerItem> dataArray = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlarmManager alarmManager;

    private int year, month, day, date;
    private String json;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        nowTodoTextView = view.findViewById(R.id.nowTodoTv);
        recyclerView = view.findViewById(R.id.recyclerView);
        noPlanTextView = view.findViewById(R.id.noPlanTv);

        noPlanTextView.setText("");
        dataArray.clear();

        sharedPreferences = getActivity().getSharedPreferences("calendar", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(dataArray);
        recyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("now",""+calendar.getTimeInMillis());
        date = (year*10000)+(month*100)+day;
        json = sharedPreferences.getString(""+date,null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CalendarRecyclerItem>>(){}.getType();
        ArrayList<CalendarRecyclerItem> calArr = new ArrayList<>();
        if(json != null){
            calArr.addAll(gson.fromJson(json, type));
            if(calArr.size() != 0){
                for(CalendarRecyclerItem calItem : calArr){
                    int hour = calItem.getAlarmHour();
                    int minute = calItem.getAlarmMinute();

                    HomeRecyclerItem homeItem;
                    if(hour != -1){
                        homeItem = new HomeRecyclerItem
                                (calItem.getTodo(), "" + hour + ":" + minute, calItem.isChecked());

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            cal.set(Calendar.MINUTE, minute);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);

                            alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(getContext(), AlarmReceiver.class);

                            intent.putExtra("name",homeItem.getTodo());
                            intent.putExtra("time", ""+hour+":"+minute);

                            PendingIntent pendingIntent =PendingIntent.getBroadcast(getContext(),2, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                            Log.e("alarmCal",""+cal.getTimeInMillis());

                            if(calendar.getTimeInMillis() <= cal.getTimeInMillis()){
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                }
                                else {
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                }
                            }


                    }
                    else{
                        homeItem = new HomeRecyclerItem
                                (calItem.getTodo(), "", calItem.isChecked());
                    }
                    dataArray.add(homeItem);
                }
            }
            else{
                noPlanTextView.setText("계획이 없습니다.");
            }
        }
        else{
            noPlanTextView.setText("계획이 없습니다.");
        }
        adapter.notifyDataSetChanged();

        adapter.setOnCheckedChangedListener(new CalendarRecyclerAdapter.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean isChecked, int position) {
                calArr.get(position).setChecked(isChecked);
                dataArray.get(position).setChecked(isChecked);
                String jsonText = gson.toJson(calArr);
                editor.putString(""+date,jsonText);
                editor.apply();

                if(dataArray.size() != 0){
                    for(HomeRecyclerItem item:dataArray){
                        if(!item.isChecked()){
                            nowTodoTextView.setText("현재 할 일 : "+item.getTodo());
                            break;
                        }
                        else{
                            nowTodoTextView.setText("모든 계획 완료");
                        }
                    }
                }
            }
        });

        adapter.setOnTextViewClickListener(new HomeRecyclerAdapter.OnTextViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.e("tv","클릭"+position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("계획 삭제");
                builder.setMessage("계획을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        calArr.remove(position);
                        dataArray.remove(position);
                        Gson gson = new Gson();
                        String jsonText = gson.toJson(calArr);
                        editor.putString(""+date,jsonText);
                        editor.apply();

                        adapter.notifyDataSetChanged();

                        if(dataArray.size() != 0){
                            for(HomeRecyclerItem item:dataArray){
                                if(!item.isChecked()){
                                    nowTodoTextView.setText("현재 할 일 : "+item.getTodo());
                                    break;
                                }
                                else{
                                    nowTodoTextView.setText("모든 계획 완료");
                                }
                            }
                            noPlanTextView.setText("");
                        }
                        else{
                            nowTodoTextView.setText("");
                            noPlanTextView.setText("계획이 없습니다.");
                        }
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        if(dataArray.size() != 0){
            for(HomeRecyclerItem item:dataArray){
                if(!item.isChecked()){
                    nowTodoTextView.setText("현재 할 일 : "+item.getTodo());
                    break;
                }
                else{
                    nowTodoTextView.setText("모든 계획 완료");
                }
            }
        }
        else{
            nowTodoTextView.setText("");
        }

        return view;
    }
}