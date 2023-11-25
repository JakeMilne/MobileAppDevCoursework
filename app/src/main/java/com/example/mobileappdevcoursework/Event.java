package com.example.mobileappdevcoursework;

public class Event {
    private int id;
    private String name;
    private int minute;
    private String result;
    private String addition;

    public Event(int id, String name, int minute, String result, String addition) {
        this.id = id;
        this.name = name;
        this.minute = minute;
        this.result = result;
        this.addition = addition;
    }

    // Getter methods...

    public String getResult() {
        return result;
    }

    public String getAddition() {
        return addition;
    }
    public String getName(){
        return name;
    }

    public int getMinute(){
        return minute;
    }
}
