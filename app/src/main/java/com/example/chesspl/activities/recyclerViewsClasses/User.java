package com.example.chesspl.activities.recyclerViewsClasses;

public class User {
    public String username;
    public int elo;

    public User(String username, int elo) {
        this.username = username;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
