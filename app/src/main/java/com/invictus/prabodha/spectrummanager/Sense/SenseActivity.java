package com.invictus.prabodha.spectrummanager.Sense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Advertise.AdvertiseActivity;
import com.invictus.prabodha.spectrummanager.Client.ClientActivity;
import com.invictus.prabodha.spectrummanager.Models.Channel;
import com.invictus.prabodha.spectrummanager.Models.WifiSignal;
import com.invictus.prabodha.spectrummanager.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SenseActivity extends AppCompatActivity {


    private ListView freeChannelsListView;

    private static Context context;
    private WifiReceiver receiverWifi;

    private SenseActivityAdapter adapter;


    List<ScanResult> wifiList;
    static ArrayList<Channel> channelList;

    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = -55;
    private static final int NO_OF_LEVELS = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense);

        context = getApplicationContext();

        initializeUI();
    }



    private void initializeUI(){

        //messagesList = new ArrayList<>();

        Button btnSenseChannels = findViewById(R.id.btn_sense_channels);

        btnSenseChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanWifiList();

            }
        });

        freeChannelsListView = findViewById(R.id.free_channels_list);

        TextView tvIPAddress = findViewById(R.id.tv_ip_value);
        TextView tvMACAddress = findViewById(R.id.tv_mac_value);
        TextView tvChannelNo = findViewById(R.id.tv_channel_value);

        tvIPAddress.setText(ClientActivity.getMyDevice().getIpAddress());
        tvMACAddress.setText(ClientActivity.getMyDevice().getMacAddress());
        tvChannelNo.setText(String.valueOf(ClientActivity.getMyDevice().getChannelNo()));

        initializeChannels();

    }


    private void scanWifiList() {
        WifiManager mainWifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        mainWifi.startScan();
        wifiList = mainWifi.getScanResults();

        // Create Temporary HashMap
        HashMap<String, WifiSignal> map =
                new HashMap<String, WifiSignal>();

        // Add ScanResults to Map to remove duplicates
        for (ScanResult scanResult : wifiList) {
            if (scanResult.SSID != null &&
                    !scanResult.SSID.isEmpty()) {
                //Log.d("Tag", "got it");
                WifiSignal signal=new WifiSignal(scanResult.SSID, scanResult.BSSID, scanResult.level, scanResult.frequency);

                calculateSignalLevel(signal.getLevel(),signal.getChannel());
                map.put(scanResult.SSID, signal);
            }
        }

        // Add to new List
        List<WifiSignal> sortedWifiList = new ArrayList<WifiSignal>(map.values());

        // Create Comparator to sort by level
        Comparator<WifiSignal> comparator =
                new Comparator<WifiSignal>() {

                    @Override
                    public int compare(WifiSignal lhs, WifiSignal rhs) {
                        return (lhs.getLevel() < rhs.getLevel() ? -1 : (lhs.getLevel() == rhs.getLevel() ? 0 : 1));
                    }
                };

        // Apply Comparator and sort
        Collections.sort(sortedWifiList, comparator);



        setAdapter(channelList);

    }

    private void setAdapter(List<Channel> wifi) {
        adapter = new SenseActivityAdapter(context, wifi);
        freeChannelsListView.setAdapter(adapter);
    }

    private void calculateSignalLevel(int rssi, int channel) {
        int rating = 0;
        if (rssi <= MIN_RSSI) {
            rating = 0;
            channelList.get(channel-1).setRating(0);
        }
        if (rssi >= MAX_RSSI) {
            rating = NO_OF_LEVELS - 1;
            channelList.get(channel-1).setRating(NO_OF_LEVELS - 1);
        }
        rating = (rssi - MIN_RSSI) * (NO_OF_LEVELS - 1) / (MAX_RSSI - MIN_RSSI);
        channelList.get(channel-1).setRating((rssi - MIN_RSSI) * (NO_OF_LEVELS - 1) / (MAX_RSSI - MIN_RSSI));
        if(rating<channelList.get(channel-1).getRating()){
            channelList.get(channel-1).setRating(rating);
        }
    }


    private void initializeChannels(){
        channelList= new ArrayList<>();
        for(int i=0;i<14;i++){
            Channel ch=new Channel(i+1,2412+5*i);
            channelList.add(ch);

        }
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        }
    }
}


