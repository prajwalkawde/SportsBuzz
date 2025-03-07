package com.fantasy.team.prediction.Model;

public class Match_details {
    String id;
    String match_details;
    String premium_details;
    String team1;
    String team2;

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Match_details(String match_details) {
        this.match_details = match_details;
    }


    public String getPremium_details() {
        return premium_details;
    }

    public void setPremium_details(String premium_details) {
        this.premium_details = premium_details;
    }

    public Match_details() {

    }

    public String getMatch_details() {
        return match_details;
    }

    public void setMatch_details(String match_details) {
        this.match_details = match_details;
    }
}
