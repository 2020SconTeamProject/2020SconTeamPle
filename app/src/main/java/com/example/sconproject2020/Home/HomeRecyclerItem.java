package com.example.sconproject2020.Home;

public class HomeRecyclerItem {
    String todo;
    String when;
    boolean isChecked;

    public HomeRecyclerItem(String todo, String when, boolean isChecked) {
        this.todo = todo;
        this.when = when;
        this.isChecked = isChecked;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
