package com.example.mobileappdevcoursework;

public class Event {
    private int id;
    private String name;
    private int minute;

    public Event(int id, String name, int minute) {
        this.id = id;
        this.name = name;
        this.minute = minute;
    }

    // Getter methods

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinute() {
        return minute;
    }
}
