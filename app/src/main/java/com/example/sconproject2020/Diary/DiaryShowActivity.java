package com.example.sconproject2020.Diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sconproject2020.R;

public class DiaryShowActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    Button saveBtn;
    String content, title, previewText;
    int position, dataArrPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_show);

        titleEditText = findViewById(R.id.diaryTitleEditText);
        contentEditText = findViewById(R.id.diaryContent);
        saveBtn = findViewById(R.id.diarySaveBtn);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        previewText = intent.getStringExtra("previewtext");
        position = Integer.parseInt(intent.getStringExtra("position"));
        dataArrPos = Integer.parseInt(intent.getStringExtra("dataArrPos"));

        titleEditText.setText(title);
        contentEditText.setText(content);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEditText.getText().toString();
                content = contentEditText.getText().toString();
                if (title.equals("")) {
                    titleEditText.setError("제목을 입력하세요.");
                }
                else{
                    intent.putExtra("title",title);
                    intent.putExtra("content",content);
                    intent.putExtra("position",""+position);
                    intent.putExtra("previewtext",previewText);
                    intent.putExtra("dataArrPos",""+dataArrPos);
                    setResult(200, intent);
                    finish();
                }
            }
        });
    }
}