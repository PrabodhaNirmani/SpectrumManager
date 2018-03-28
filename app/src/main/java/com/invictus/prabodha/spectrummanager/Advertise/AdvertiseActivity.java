package com.invictus.prabodha.spectrummanager.Advertise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Client.ClientActivity;
import com.invictus.prabodha.spectrummanager.MessagePassing.MulticastPublisher;
import com.invictus.prabodha.spectrummanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AdvertiseActivity extends AppCompatActivity {

    private TextView tvIPAddress, tvMACAddress, tvChannelNo;
    private ListView messageListView;

    private IntentFilter packetReceiveFilter;

    private static Context context;


    private static final String TAG = "AdvertiseActivity";
    public static final String ACTION_PACKET_RECEIVED = "advertise_activity_packet_received";
    public  static final String EXTRA_DATA = "extra_data";

    private static ArrayList<String> messagesList;

    private final BroadcastReceiver packetReceiveListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra(EXTRA_DATA);

            updateUI(message);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        context = getApplicationContext();

        initializeUI();

        //new AdvertiseChannelMulticastReceiver().start();

        packetReceiveFilter = new IntentFilter(ACTION_PACKET_RECEIVED);
        registerReceiver(packetReceiveListener, packetReceiveFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(packetReceiveListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(packetReceiveListener,packetReceiveFilter);
        setAdapter();
    }

    private void initializeUI(){

        messagesList = new ArrayList<>();

        Button btnAdvertiseMe = findViewById(R.id.advertise_me);
        Button btnRequestChannel = findViewById(R.id.request_channel);

        btnAdvertiseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AdvertiseChannelTask().execute();

            }
        });

        btnRequestChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        messageListView = findViewById(R.id.msg_list);

        tvIPAddress = findViewById(R.id.tv_ip_value);
        tvMACAddress = findViewById(R.id.tv_mac_value);
        tvChannelNo = findViewById(R.id.tv_channel_value);

        tvIPAddress.setText(ClientActivity.getMyDevice().getIpAddress());
        tvMACAddress.setText(ClientActivity.getMyDevice().getMacAddress());
        tvChannelNo.setText(String.valueOf(ClientActivity.getMyDevice().getChannelNo()));

    }


    private void updateUI(String message){
        Log.d(TAG,message);


        String [] temp = message.split("@");

        String ip=getIPAddress().trim();
        String receivedIPAddress = temp[1].split(",")[1];
        if(!ip.equalsIgnoreCase(receivedIPAddress)){
            messagesList.add(temp[1]);

        }


        setAdapter();




    }

    private String getIPAddress(){
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr != null ? wifiMgr.getConnectionInfo() : null;
        int ip = wifiInfo != null ? wifiInfo.getIpAddress() : 0;
        return Formatter.formatIpAddress(ip).trim();
    }


    private void setAdapter(){
        AdvertiseActivityAdapter adapter = new AdvertiseActivityAdapter(getApplicationContext(), messagesList);
        messageListView.setAdapter(adapter);
    }
    private String generateBroadcastMessage(){
        //message, ip, mac, timestamp, time duration
        String message = "AdvertiseActivity@";
        message += "Channel " + ClientActivity.getMyDevice().getChannelNo() + " is free ... ,";
        message += ClientActivity.getMyDevice().getIpAddress() + ",";
        message += ClientActivity.getMyDevice().getMacAddress() + ",";
        message += new Date().toString()+",";
        message += "10 minutes";
        return message;
    }

    public static Context getContext(){
        return context;
    }

    class AdvertiseChannelTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {
            try {
                new MulticastPublisher().multicast(generateBroadcastMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            setAdapter();
//            assignChannels.setEnabled(false);

        }
    }


}