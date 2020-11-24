package com.example.sconproject2020.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.sconproject2020.Home.AlarmReceiver;
import com.example.sconproject2020.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ArrayList<CalendarRecyclerItem> dataArray = new ArrayList<>();
    private CalendarRecyclerAdapter adapter;
    private ImageButton addBtn;
    private TextView noPlanTv;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int year, month, day, date;
    private String json;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        sharedPreferences = getActivity().getSharedPreferences("calendar", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recycerView);
        addBtn = view.findViewById(R.id.planAddBtn);
        noPlanTv = view.findViewById(R.id.noPlanTv);

        //CalendarFragment를 불러올때는 DayClickListener가 작동하지 않기 때문에
        //현재 날짜를 불러와 recyclerview에 띄워준다.
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = (year*10000)+(month*100)+day;
        Log.e("text",""+date);
        json = sharedPreferences.getString(""+date,null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CalendarRecyclerItem>>(){}.getType();
        if(json != null){
            dataArray.clear();
            dataArray.addAll(gson.fromJson(json, type));
        }
        else{
            dataArray.clear();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CalendarRecyclerAdapter(dataArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        checkArray();

        adapter.setOnItemClickListener(new CalendarRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.e("calendar","onClick");
                CalendarRecyclerItem item = dataArray.get(position);
                String planName = item.getTodo();
                String startDate = item.getStartDate();
                String endDate = item.getEndDate();
                String isAlarmSet;
                if(item.getAlarmHour() == -1){
                    isAlarmSet = "X";
                }
                else{
                    isAlarmSet = "O";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("계획 내용");
                if(isAlarmSet.equals("O")){
                    String alarmTime = ""+item.getAlarmHour()+":"+item.getAlarmMinute();
                    builder.setMessage("계획 이름 : "+planName+"\n계획 시작 날짜 : "
                            +startDate+"\n계획 종료 날짜 : "+endDate+"\n알람 설정 여부 : "+isAlarmSet+"\n알람 시간 : "+alarmTime);
                }
                else{
                    builder.setMessage("계획 이름 : "+planName+"\n계획 시작 날짜 : "
                            +startDate+"\n계획 종료 날짜 : "+endDate+"\n알람 설정 여부 : "+isAlarmSet);
                }

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }

            //리사이클러뷰의 항목을 길게누르면 삭제여부를 물어보는 다이얼로그를 띄움
            @Override
            public void onItemLongClick(View v, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("계획 삭제");
                builder.setMessage("계획을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataArray.remove(position);
                        Gson gson = new Gson();
                        String jsonText = gson.toJson(dataArray);
                        editor.putString(""+date,jsonText);
                        editor.apply();

                        adapter.notifyDataSetChanged();

                        checkArray();
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
        adapter.setOnCheckedChangedListener(new CalendarRecyclerAdapter.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean isChecked, int position) {
                dataArray.get(position).setChecked(isChecked);
                String jsonText = gson.toJson(dataArray);
                editor.putString(""+date,jsonText);
                editor.apply();
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();

                //선택된 날의 연도, 달, 일 가져오기
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                date = (year*10000)+(month*100)+ day;
                json = sharedPreferences.getString(""+date,null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CalendarRecyclerItem>>(){}.getType();
                if(json != null){
                    dataArray.clear();
                    dataArray.addAll(gson.fromJson(json, type));
                    Log.e("contents",json);
                }
                else{
                    dataArray.clear();
                }

                Log.e("size",""+dataArray.size());
                checkArray();

                adapter.notifyDataSetChanged();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlanAddActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    private void checkArray(){
        if(dataArray.size() == 0){
            noPlanTv.setText("계획 없음");
        }
        else{
            noPlanTv.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==100){
            String name = data.getStringExtra("name");
            String startDate = data.getStringExtra("startDate");
            String endDate = data.getStringExtra("endDate");

            int alarmHour = -1, alarmMinute = -1;
            alarmHour = Integer.parseInt(data.getStringExtra("alarmHour"));
            alarmMinute = Integer.parseInt(data.getStringExtra("alarmMinute"));

            Log.e("h",""+alarmHour);
            Log.e("m",""+alarmMinute);

            String[] startArr, endArr;
            startArr = startDate.split("/");
            endArr = endDate.split("/");

            int fromDate, toDate;
            int fromMonth, toMonth;
            fromMonth = Integer.parseInt(startArr[1]) - 1;
            toMonth = Integer.parseInt(endArr[1]) - 1;

            fromDate = (Integer.parseInt(startArr[0]) * 10000)
                    + (fromMonth*100) + (Integer.parseInt(startArr[2]));
            toDate = (Integer.parseInt(endArr[0]) * 10000)
                    + (toMonth*100) + (Integer.parseInt(endArr[2]));
            Log.e("from",""+fromDate);
            Log.e("to",""+toDate);

            for(int i=fromDate;i<=toDate;i++){
                String json = sharedPreferences.getString(""+i,null);
                ArrayList<CalendarRecyclerItem> array;
                CalendarRecyclerItem item = new CalendarRecyclerItem(false, name, startDate, endDate, alarmHour, alarmMinute);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CalendarRecyclerItem>>(){}.getType();
                if(json != null){
                    array = gson.fromJson(json, type);
                    array.add(item);
                    String jsonText = gson.toJson(array);
                    editor.putString(""+i,jsonText);
                }
                else{
                    array = new ArrayList<>();
                    array.add(item);

                    String jsonText = gson.toJson(array);
                    editor.putString(""+i,jsonText);
                }
                editor.apply();
            }

            int date = (year*10000)+(month*100)+day;
            String json = sharedPreferences.getString(""+date,null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CalendarRecyclerItem>>(){}.getType();
            dataArray.clear();
            if(json != null){
                dataArray.addAll(gson.fromJson(json, type));
            }
            adapter.notifyDataSetChanged();

            checkArray();
        }
    }
}