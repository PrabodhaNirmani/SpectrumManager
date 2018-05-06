package com.invictus.prabodha.spectrummanager.Models;

/**
 * Created by acer on 4/25/2018.
 */

public class WifiSignal {
    private String SSID;
    private String BSSID;
    private int level;
    private int frequency;
    private  int channel;
    //    private String capabilities;
    private int channelWidth=0;

    public WifiSignal(){

    }

    public WifiSignal(String ssid,String bssid, int level, int freq){
        this.SSID=ssid;
        this.BSSID=bssid;
        this.level=level;
        this.frequency=freq;

        convertFrequencyToChannel();

    }

    private void convertFrequencyToChannel() {
        if (frequency>= 2412 && frequency<= 2484) {
            this.channel = (frequency - 2412) / 5 + 1;
        }
        else if (frequency >= 5170 && frequency <= 5825) {
            this.channel = (frequency - 5170) / 5 + 34;
        } else {
            this.channel = -1;
        }
    }

    public int getChannel() {
        return channel;
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public int getLevel() {
        return level;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getChannelWidth() {
        return channelWidth;
    }
}
