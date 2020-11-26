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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
    private ArrayList<DiaryRecyclerItem> showArray = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;
    private Spinner spinner;
    private TextView noDiaryTextView;

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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month++;

        gson = new Gson();
        type = new TypeToken<ArrayList<DiaryRecyclerItem>>() {
        }.getType();

        json = sharedPreferences.getString("json", null);
        if (json != null) {
            dataArray.clear();
            dataArray.addAll(gson.fromJson(json, type));
        } else {
            dataArray.clear();
        }

        recyclerView = view.findViewById(R.id.diaryRecyclerView);
        diaryAddBtn = view.findViewById(R.id.diaryAddBtn);
        spinner = view.findViewById(R.id.diarySpinner);
        noDiaryTextView = view.findViewById(R.id.noDiaryTv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DiaryRecyclerAdapter(showArray);
        recyclerView.setAdapter(adapter);

        json = gson.toJson(dataArray);
        editor.putString("json", json);
        editor.apply();

        adapter.notifyDataSetChanged();

        checkShowArr();

        diaryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DiaryAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnItemClickListener(new CalendarRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), DiaryShowActivity.class);
                intent.putExtra("title", showArray.get(position).getTitle());
                intent.putExtra("content", showArray.get(position).getContent());
                intent.putExtra("previewtext", showArray.get(position).getPreviewText());
                intent.putExtra("position", "" + position);
                intent.putExtra("dataArrPos", "" + dataArray.indexOf(showArray.get(position)));
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
                        json = gson.toJson(dataArray);
                        Log.e("json1", json);
                        dataArray.remove(showArray.get(position));
                        showArray.remove(position);

                        json = gson.toJson(dataArray);
                        Log.e("json2", json);
                        editor.putString("json", json);
                        editor.apply();

                        adapter.notifyDataSetChanged();
                        checkShowArr();
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {  //모두
                    Log.e("스피너", "모두");
                    showArray.clear();
                    showArray.addAll(dataArray);
                    adapter.notifyDataSetChanged();
                    checkShowArr();
                } else if (position == 1) { //최근 1달
                    Log.e("스피너", "최근1달");
                    showArray.clear();
                    for (DiaryRecyclerItem item : dataArray) {
                        String date = item.getDate();
                        String[] arr = date.split(" ");
                        String[] dateArr = arr[0].split("/");
                        Log.e("date", date);
                        int itemYear = Integer.parseInt(dateArr[0]);
                        int itemMonth = Integer.parseInt(dateArr[1]);
                        int itemDay = Integer.parseInt(dateArr[2]);
                        Log.e("num", "" + itemYear + ":" + itemMonth + ":" + itemDay);

                        if (itemYear >= year - 1 && itemMonth >= month - 1) {
                            showArray.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    checkShowArr();
                } else if (position == 2) { //최근 1주
                    Log.e("스피너", "최근 1주");
                    showArray.clear();
                    for (DiaryRecyclerItem item : dataArray) {
                        String date = item.getDate();
                        String[] arr = date.split(" ");
                        String[] dateArr = arr[0].split("/");
                        Log.e("date", date);
                        int itemYear = Integer.parseInt(dateArr[0]);
                        int itemMonth = Integer.parseInt(dateArr[1]);
                        int itemDay = Integer.parseInt(dateArr[2]);
                        Log.e("num", "" + itemYear + ":" + itemMonth + ":" + itemDay);

                        if (day >= 8 && month > 1) {
                            if (itemYear == year && itemMonth == month && itemDay >= day - 7) {
                                showArray.add(item);
                            }
                        } else if (day < 8 && month != 2) {
                            if (itemYear >= year - 1 && itemMonth >= month && itemDay >= 23 + day) {
                                showArray.add(item);
                            }
                        } else {
                            if (itemYear >= year - 1 && itemMonth >= month && itemDay >= 21 + day) {
                                showArray.add(item);
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
                    checkShowArr();
                } else if (position == 3) { //오늘
                    Log.e("스피너", "오늘");
                    showArray.clear();
                    for (DiaryRecyclerItem item : dataArray) {
                        String date = item.getDate();
                        String[] arr = date.split(" ");
                        String[] dateArr = arr[0].split("/");
                        Log.e("date", date);
                        int itemYear = Integer.parseInt(dateArr[0]);
                        int itemMonth = Integer.parseInt(dateArr[1]);
                        int itemDay = Integer.parseInt(dateArr[2]);
                        Log.e("num", "" + itemYear + ":" + itemMonth + ":" + itemDay);

                        if (year == itemYear && month == itemMonth && day == itemDay) {
                            showArray.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    checkShowArr();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    void checkShowArr() {
        if (showArray.size() == 0) {
            noDiaryTextView.setText("일기가 없습니다.");
        } else {
            noDiaryTextView.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 100) {

            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month++;

            Date now = new Date();
            int hour = now.getHours(), minute = now.getMinutes();
            date = "" + year + "/" + month + "/" + day + " " + hour + ":" + minute;

            String preview = "";
            try{
                for (int i = 0; i < 5; i++) {
                    if (content.charAt(i) == '\n' || content.charAt(i) == ' ') {
                        break;
                    }
                    preview = preview + content.charAt(i);
                }
            }
            catch (StringIndexOutOfBoundsException e){
                preview = content;
            }

            DiaryRecyclerItem item = new DiaryRecyclerItem(title, preview + "...", content, date);
            dataArray.add(item);
            showArray.add(item);

            json = gson.toJson(dataArray);
            editor.putString("json", json);
            editor.apply();

            adapter.notifyDataSetChanged();
            checkShowArr();

        }

        if (requestCode == 2 && resultCode == 200) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String previewText = intent.getStringExtra("previewtext");
            int position = Integer.parseInt(intent.getStringExtra("position"));
            int dataArrPos = Integer.parseInt(intent.getStringExtra("dataArrPos"));

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month++;

            Date now = new Date();
            int hour = now.getHours(), minute = now.getMinutes();
            date = "" + year + "/" + month + "/" + day + " " + hour + ":" + minute;

            showArray.get(position).setContent(content);
            showArray.get(position).setTitle(title);
            showArray.get(position).setPreviewText(previewText);
            showArray.get(position).setDate(date);

            dataArray.get(dataArrPos).setContent(content);
            dataArray.get(dataArrPos).setTitle(title);
            dataArray.get(dataArrPos).setPreviewText(previewText);
            dataArray.get(dataArrPos).setDate(date);

            json = gson.toJson(dataArray);
            editor.putString("json", json);
            editor.apply();

            adapter.notifyDataSetChanged();
            checkShowArr();
        }
    }
}