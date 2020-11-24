package com.example.sconproject2020.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        if(dataArray.size() != 0){
            nowTodoTextView.setText("현재 할 일 : "+dataArray.get(0).getTodo());
        }
        else{
            nowTodoTextView.setText("");
        }

        return view;
    }
}