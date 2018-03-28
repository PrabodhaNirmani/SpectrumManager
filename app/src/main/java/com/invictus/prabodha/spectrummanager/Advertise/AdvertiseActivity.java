package com.invictus.prabodha.spectrummanager.Advertise;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Client.ClientActivity;
import com.invictus.prabodha.spectrummanager.MessagePassing.MulticastPublisher;
import com.invictus.prabodha.spectrummanager.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdvertiseActivity extends AppCompatActivity {

    private TextView tvIPAddress, tvMACAddress, tvChannelNo;
    private ListView messageListView;

    private IntentFilter packetReceiveFilter;
    private IntentFilter requestChannelPacketReceiveFilter;
    private IntentFilter channelGrantedPacketReceiveFilter;


    private static Context context;


    private static final String TAG = "AdvertiseActivity";
    public static final String ACTION_PACKET_RECEIVED = "advertise_activity_packet_received";
    public static final String ACTION_CHANNEL_GRANT_PACKET_RECEIVED = "advertise_activity_channel_grant_packet_received";
    public static final String ACTION_REQUEST_CHANNEL_PACKET_RECEIVED = "advertise_activity_request_channel_packet_received";

    public  static final String EXTRA_DATA = "extra_data";
    public  static final String EXTRA_IP_ADDRESS = "extra_ip_address";

    private static ArrayList<String> messagesList;



    private final BroadcastReceiver packetReceiveListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(EXTRA_DATA);
            String ip = intent.getStringExtra(EXTRA_IP_ADDRESS);
            updateUI(message, ip);
        }
    };


    private final BroadcastReceiver requestChannelPacketReceiveListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra(EXTRA_DATA);
            String ip = intent.getStringExtra(EXTRA_IP_ADDRESS);


            showRequestChannelAlertDialog(message,ip);

        }
    };

    private final BroadcastReceiver channelGrantedPacketReceiveListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra(EXTRA_DATA);
            String ip = intent.getStringExtra(EXTRA_IP_ADDRESS);


            //show another alert dialog
            //updateUI(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        context = getApplicationContext();

        initializeUI();

        packetReceiveFilter = new IntentFilter(ACTION_PACKET_RECEIVED);
        registerReceiver(packetReceiveListener, packetReceiveFilter);

        requestChannelPacketReceiveFilter = new IntentFilter(ACTION_REQUEST_CHANNEL_PACKET_RECEIVED);
        registerReceiver(requestChannelPacketReceiveListener, requestChannelPacketReceiveFilter);

        channelGrantedPacketReceiveFilter = new IntentFilter(ACTION_CHANNEL_GRANT_PACKET_RECEIVED);
        registerReceiver(channelGrantedPacketReceiveListener, channelGrantedPacketReceiveFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(packetReceiveListener);
        unregisterReceiver(requestChannelPacketReceiveListener);
        unregisterReceiver(channelGrantedPacketReceiveListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(packetReceiveListener,packetReceiveFilter);
        registerReceiver(requestChannelPacketReceiveListener, requestChannelPacketReceiveFilter);
        registerReceiver(channelGrantedPacketReceiveListener, channelGrantedPacketReceiveFilter);

        setAdapter();
    }

    private void initializeUI(){

        messagesList = new ArrayList<>();

        Button btnAdvertiseMe = findViewById(R.id.advertise_me);

        btnAdvertiseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AdvertiseChannelTask().execute();

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


    private void updateUI(String message, String receiveIPAddress){
//message, timestamp, ip
        String [] temp = message.split("@");

        String ip=getIPAddress().trim();

        if(!ip.equalsIgnoreCase(receiveIPAddress)){
            String msg = temp[1]+","+receiveIPAddress;
            messagesList.add(msg);

        }
        setAdapter();

    }

    private void showRequestChannelAlertDialog(String message, String ip){

//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(AdvertiseActivity.this, R.style.myDialog));
        View mView = getLayoutInflater().inflate(R.layout.request_channel_alert_dialog, null);


        TextView tvIPAddress = mView.findViewById(R.id.ip_address);
        tvIPAddress.setText(ip);
        TextView tvChannelNo = mView.findViewById(R.id.channel_no);
        tvChannelNo.setText(message.split("@")[1]);


        final EditText etTimeDuration = mView.findViewById(R.id.et_time_duration);
        Button btnGrant = mView.findViewById(R.id.btn_grant);
        Button btnDecline = mView.findViewById(R.id.btn_decline);
        final TextView tvError = mView.findViewById(R.id.error);

        btnGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTimeDuration.getText().toString().isEmpty()){
                    tvError.setVisibility(View.VISIBLE);

                }
                else {
                    tvError.setVisibility(View.INVISIBLE);
                }
                //grant access
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decline access
            }
        });

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


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
        //message, timestamp
        String message = "AdvertiseActivity@";
        message += "Channel " + ClientActivity.getMyDevice().getChannelNo() + " is free ... ,";


        Date now = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(now);

        message += date;
        Log.d(TAG, date);
        Log.d(TAG, message);
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


        }
    }


}