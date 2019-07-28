package com.example.overlord.vklenta.Model;

/**
 * Created by OverLord on 24.04.2018.
 */

public class Doc implements DocModel{
    private int id;
    private String name;

    public Doc(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

}
