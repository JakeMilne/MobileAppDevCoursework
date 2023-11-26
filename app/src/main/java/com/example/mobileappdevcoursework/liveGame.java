package com.example.mobileappdevcoursework;

public class liveGame {

    String title;
    String date;
    int id;

    public liveGame(String title, String date, int id) {
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
