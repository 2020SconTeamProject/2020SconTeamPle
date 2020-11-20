package com.example.sconproject2020.Calendar;

public class CalendarRecyclerItem {
    boolean isChecked;
    String todo ;
    String startDate;
    String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public CalendarRecyclerItem(boolean isChecked, String todo, String fromTime, String toTime) {
        this.isChecked = isChecked;
        this.todo = todo;
        this.startDate = fromTime;
        this.endDate = toTime;
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