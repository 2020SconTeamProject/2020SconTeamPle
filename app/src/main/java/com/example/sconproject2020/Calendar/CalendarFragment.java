package com.example.sconproject2020.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.sconproject2020.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

            }

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
            String startDate, endDate, name;
            name = data.getStringExtra("name");
            startDate = data.getStringExtra("startDate");
            endDate = data.getStringExtra("endDate");

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
                CalendarRecyclerItem item = new CalendarRecyclerItem(false, name, startDate, endDate);
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
            if(json != null){
                dataArray.clear();
                dataArray.addAll(gson.fromJson(json, type));
            }
            else{
                dataArray.clear();
            }
            adapter.notifyDataSetChanged();

            checkArray();
        }
    }
}