package com.example.sconproject2020.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.sconproject2020.R;

import java.sql.Time;

public class PlanAddActivity extends AppCompatActivity {

    EditText nameInput, startDateInput, endDateInput;
    Button okBtn;
    int year, month, day;
    int starty, startm, startd, endy, endm, endd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);

        nameInput = findViewById(R.id.nameEditText);
        startDateInput = findViewById(R.id.fromEditText);
        endDateInput = findViewById(R.id.ToEditText);
        okBtn = findViewById(R.id.Okbtn);

        Intent intent = getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);

        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new
                        DatePickerDialog(PlanAddActivity.this, startDateListener, year, month, day);
                dialog.show();
            }
        });

        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new
                        DatePickerDialog(PlanAddActivity.this, endDateListener, year, month, day);
                dialog.show();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String startDate = startDateInput.getText().toString();
                String endDate = endDateInput.getText().toString();
                if (name.equals("")){
                    nameInput.setError("이름을 입력하세요");
                }
                else if(startDate.equals("")){
                    startDateInput.setError("시작 날짜를 입력하세요");
                }
                else if(endDate.equals("")){
                    endDateInput.setError("종료 날짜를 입력하세요.");
                }
                else {
                    String[] sarr, earr;

                    sarr = startDate.split(" ");
                    startDate = sarr[0];

                    earr = endDate.split(" ");
                    endDate = earr[0];

                    intent.putExtra("name", name);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    setResult(100, intent);
                    finish();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            m = m + 1;

            startDateInput.setText(""+y+"/"+m+"/"+d);
            endDateInput.setText(""+y+"/"+m+"/"+d);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            m = m + 1;

            endDateInput.setText(""+y+"/"+m+"/"+d);
        }
    };
}