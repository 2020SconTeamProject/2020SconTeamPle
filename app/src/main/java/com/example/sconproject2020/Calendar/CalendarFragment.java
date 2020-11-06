package com.example.sconproject2020.Calendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.sconproject2020.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    RecyclerView recyclerView;
    private ArrayList<CalendarRecyclerItem> dataArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recycerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CalendarRecyclerAdapter adapter = new CalendarRecyclerAdapter(dataArray);

        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        for(int i=0;i<10;i++){
            CalendarRecyclerItem item = new CalendarRecyclerItem(false, "아기보기");
            dataArray.add(item);
        }

        adapter.notifyDataSetChanged();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                int year, month, day;

                //선택된 날의 연도, 달, 일 가져오기
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

            }
        });

        return view;
    }
}