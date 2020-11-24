package com.example.sconproject2020.Calendar;

public class CalendarRecyclerItem {
    boolean isChecked;
    String todo ;
    String startDate;
    String endDate;
    int alarmHour = -1, alarmMinute = -1;

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

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

    public CalendarRecyclerItem(boolean isChecked, String todo, String startDate, String endDate, int alarmHour, int alarmMinute) {
        this.isChecked = isChecked;
        this.todo = todo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
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