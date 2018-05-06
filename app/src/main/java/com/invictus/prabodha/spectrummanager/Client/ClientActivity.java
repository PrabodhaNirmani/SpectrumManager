package com.invictus.prabodha.spectrummanager.Client;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.invictus.prabodha.spectrummanager.Advertise.AdvertiseActivity;
import com.invictus.prabodha.spectrummanager.MessagePassing.ClientActivityMulticastReceiver;
import com.invictus.prabodha.spectrummanager.Models.Client;
import com.invictus.prabodha.spectrummanager.R;
import com.invictus.prabodha.spectrummanager.Sense.SenseActivity;


import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {


    private static Context context;

    private static Client myDevice;
    private ProgressDialog progressDialog;

    private TextView tvIPAddress, tvMACAddress, tvChannelNo;
    private Button btnAdvertise,btnSensing;

    private IntentFilter packetReceiveFilter;

    private static ArrayList<Client> clientList;

    private static final String TAG = "ClientActivity";
    public static final String ACTION_PACKET_RECEIVED = "client_activity_packet_received";
    public  static final String EXTRA_DATA = "extra_data";
    public  static final String EXTRA_IP_ADDRESS = "extra_ip_address";

    private final BroadcastReceiver packetReceiveListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra(EXTRA_DATA);
//            if (progressDialog.isShowing()) {
//                dismissProgressDialog();
//            }
            updateUI(message);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        context = getApplicationContext();

        initializeUI();

        new ClientActivityMulticastReceiver().start();

        //displayProgressDialog("Please wait","Waiting to receive packet " );

        packetReceiveFilter = new IntentFilter(ACTION_PACKET_RECEIVED);
        registerReceiver(packetReceiveListener, packetReceiveFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(packetReceiveListener,packetReceiveFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(packetReceiveListener);
    }


    private void initializeUI(){

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        clientList = new ArrayList<>();

        btnAdvertise = findViewById(R.id.btn_advertise);
        btnSensing = findViewById(R.id.btn_sense);


//        btnAdvertise.setEnabled(false);
//        btnSensing.setEnabled(false);


        btnSensing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ClientActivity.this, SenseActivity.class);
                startActivity(myIntent);

            }
        });

        btnAdvertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ClientActivity.this, AdvertiseActivity.class);
                startActivity(myIntent);

            }
        });

        tvIPAddress = findViewById(R.id.tv_ip_value);
        tvMACAddress = findViewById(R.id.tv_mac_value);
        tvChannelNo = findViewById(R.id.tv_channel_value);

    }

    private void updateUI(String message){
        Log.d(TAG,message);

//        btnAdvertise.setEnabled(true);
//        btnSensing.setEnabled(true);

        clientList.clear();
        String temp = message.split("@")[1];
        String [] msgList = temp.split(" ");

        String ip=getIPAddress().trim();

        for (String msg : msgList){
            String [] line = msg.split(",");
            Client c = new Client(line[0].trim(),line[1].trim(),Integer.valueOf(line[2].trim()));
            clientList.add(c);
            if(ip.equalsIgnoreCase(c.getIpAddress())){
                myDevice = c;
                tvIPAddress.setText(c.getIpAddress());
                tvMACAddress.setText(c.getMacAddress());
                tvChannelNo.setText(String.valueOf(c.getChannelNo()));
            }
        }
    }

    public static Client getMyDevice(){
        return myDevice;
    }

    private String getIPAddress(){
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr != null ? wifiMgr.getConnectionInfo() : null;
        int ip = wifiInfo != null ? wifiInfo.getIpAddress() : 0;
        return Formatter.formatIpAddress(ip).trim();
    }

    public static Context getContext(){
        return context;
    }

}
