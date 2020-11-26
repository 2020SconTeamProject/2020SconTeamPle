package com.example.sconproject2020.Diary;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.sconproject2020.Calendar.CalendarRecyclerAdapter;
import com.example.sconproject2020.Calendar.CalendarRecyclerItem;
import com.example.sconproject2020.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DiaryFragment extends Fragment {

    private DiaryRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton diaryAddBtn;
    private ArrayList<DiaryRecyclerItem> dataArray = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;

    private int year, month, day;
    private String date, json;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        sharedPreferences = getActivity().getSharedPreferences("diary", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        gson = new Gson();
        type = new TypeToken<ArrayList<DiaryRecyclerItem>>(){}.getType();

        json = sharedPreferences.getString("json",null);
        if(json != null){
            dataArray.clear();
            dataArray.addAll(gson.fromJson(json,type));
        }
        else{
            dataArray.clear();
        }

        recyclerView = view.findViewById(R.id.diaryRecyclerView);
        diaryAddBtn = view.findViewById(R.id.diaryAddBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DiaryRecyclerAdapter(dataArray);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        diaryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DiaryAddActivity.class);
                startActivityForResult(intent,1);
            }
        });

        adapter.setOnItemClickListener(new CalendarRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), DiaryShowActivity.class);
                intent.putExtra("title",dataArray.get(position).getTitle());
                intent.putExtra("content",dataArray.get(position).getContent());
                intent.putExtra("previewtext",dataArray.get(position).getPreviewText());
                intent.putExtra("position",""+position);
                startActivityForResult(intent, 2);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("일기 삭제");
                builder.setMessage("일기를 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataArray.remove(position);

                        json = gson.toJson(dataArray);
                        editor.putString("json",json);
                        editor.apply();

                        adapter.notifyDataSetChanged();
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode==100){

            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month++;

            Date now = new Date();
            int hour = now.getHours(), minute = now.getMinutes();
            date = ""+year+"/"+month+"/"+day+" "+hour+":"+minute;

            String preview = "";
            for(int i=0;i<5;i++){
                if(content.charAt(i) == '\n'){
                    break;
                }
                preview = preview+content.charAt(i);
            }
            DiaryRecyclerItem item = new DiaryRecyclerItem(title, preview+"...", content, date);
            dataArray.add(item);

            json = gson.toJson(dataArray);
            editor.putString("json",json);
            editor.apply();

            adapter.notifyDataSetChanged();

        }

        if(requestCode == 2 && resultCode == 200){
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String previewText = intent.getStringExtra("previewtext");
            int position = Integer.parseInt(intent.getStringExtra("position"));

            dataArray.get(position).setContent(content);
            dataArray.get(position).setTitle(title);
            dataArray.get(position).setPreviewText(previewText);

            json = gson.toJson(dataArray);
            editor.putString("json",json);
            editor.apply();

            adapter.notifyDataSetChanged();
        }
    }
}