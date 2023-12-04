package com.example.mobileappdevcoursework;

//used to store events which have occurred in live games
public class Event {
    private int id; //event id
    private String name; // name of event, such as goal {player name}
    private String minute; //minute of the event
    private String result; //score if the event was a goal
    private String addition; //additional information
    private String team; //name of team that the event refers to
    private boolean home;//was the event for the home team

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minute='" + minute + '\'' +
                ", result='" + result + '\'' +
                ", addition='" + addition + '\'' +
                ", team='" + team + '\'' +
                '}';
    }

    public Event(int id, String name, String minute, String result, String addition, String team, boolean home) {
        this.id = id;
        this.name = name;
        this.minute = minute;
        this.result = result;
        this.addition = addition;
        this.team = team;
        this.home = home;
    }

    // Getter methods...

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

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
