package com.example.sconproject2020.Diary;

public class DiaryRecyclerItem {
    String title, previewText, content, date;

    public DiaryRecyclerItem(String name, String previewText, String content, String date) {
        this.title = name;
        this.previewText = previewText;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
