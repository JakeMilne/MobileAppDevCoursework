package com.example.mobileappdevcoursework;

public class Event {
    private int id;
    private String name;
    private String minute;
    private String result;
    private String addition;
    private String team;

    public Event(int id, String name, String minute, String result, String addition, String team) {
        this.id = id;
        this.name = name;
        this.minute = minute;
        this.result = result;
        this.addition = addition;
        this.team = team;
    }

    // Getter methods...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getResult() {
        return result;
    }

    public String getAddition() {
        return addition;
    }
    public String getName(){
        return name;
    }

    public String getMinute(){
        return minute;
    }
}
