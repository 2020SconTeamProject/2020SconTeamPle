package com.example.sconproject2020.Calendar;

public class CalendarRecyclerItem {
    boolean isChecked;
    String todo ;

    public CalendarRecyclerItem(boolean isChecked, String todo) {
        this.isChecked = isChecked;
        this.todo = todo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}