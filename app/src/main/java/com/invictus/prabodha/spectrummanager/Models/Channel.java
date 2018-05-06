package com.invictus.prabodha.spectrummanager.Models;

/**
 * Created by acer on 4/25/2018.
 */

public class Channel {

    private int channelNo;
    private int frequency;
    private int rating;

    public Channel(int cNo, int freq){
        this.channelNo=cNo;
        this.frequency=freq;
        this.rating=5;
    }
    public int getChannelNo() {
        return channelNo;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
