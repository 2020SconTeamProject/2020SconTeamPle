package com.example.sconproject2020.Home;

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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    TextView nowTodoTextView;
    RecyclerView recyclerView;
    ArrayList<HomeRecyclerItem> datas = new ArrayList<>();

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        nowTodoTextView = view.findViewById(R.id.nowTodoTv);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(datas);
        recyclerView.setAdapter(adapter);

        for(int i=0;i<5;i++){
            HomeRecyclerItem item = new HomeRecyclerItem("학교보내기", "8:00", false);
            datas.add(item);
        }

        adapter.notifyDataSetChanged();

        return view;
    }
}