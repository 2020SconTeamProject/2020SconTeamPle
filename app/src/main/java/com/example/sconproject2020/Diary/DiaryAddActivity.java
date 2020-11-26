package com.example.sconproject2020.Diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sconproject2020.R;

public class DiaryAddActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add);

        titleEditText = findViewById(R.id.diaryTitleEditText);
        contentEditText = findViewById(R.id.diaryContent);
        saveBtn = findViewById(R.id.diarySaveBtn);

        Intent intent = getIntent();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                if(title.equals("")){
                    titleEditText.setError("제목을 입력하세요");
                }
                else{
                    intent.putExtra("title",title);
                    intent.putExtra("content",content);
                    setResult(100, intent);
                    finish();
                }
            }
        });
    }
}