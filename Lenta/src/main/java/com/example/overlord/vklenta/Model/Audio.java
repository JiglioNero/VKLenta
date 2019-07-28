package com.example.overlord.vklenta.Model;

/**
 * Created by OverLord on 23.04.2018.
 */

public class Audio implements AudioModel {
    private int id;

    private String nameOfSonger;
    private String nameOfTrack;
    private String trackUrl;

    public String getNameOfSonger() {
        return nameOfSonger;
    }

    public String getNameOfTrack() {
        return nameOfTrack;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public Audio(int id, String nameOfSonger, String nameOfTrack, String trackUrl) {
        this.id = id;
        this.nameOfSonger = nameOfSonger;
        this.nameOfTrack = nameOfTrack;
        this.trackUrl = trackUrl;
    }

    @Override
    public int getID() {
        return id;
    }
}
