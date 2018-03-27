package com.invictus.prabodha.spectrummanager.Models;

/**
 * Created by acer on 3/26/2018.
 */

public class Client {

    private String ipAddress;
    private String macAddress;
    private int channelNo;



    public Client(String ip, String mac, int ch){
        this.ipAddress = ip;
        this.macAddress = mac;
        this.channelNo = ch;
    }


    public Client(String ip, String mac){
        this.ipAddress = ip;
        this.macAddress = mac;

    }


    public void setChannel(int ch){
        this.channelNo=ch;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public int getChannelNo() {
        return channelNo;
    }



}
