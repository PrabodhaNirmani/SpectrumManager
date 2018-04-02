package com.invictus.prabodha.spectrummanager.Server;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.invictus.prabodha.spectrummanager.MessagePassing.MulticastPublisher;
import com.invictus.prabodha.spectrummanager.Models.Client;
import com.invictus.prabodha.spectrummanager.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class ControlActivity extends AppCompatActivity {


    private static ArrayList<Client> clientsList;

    private Button assignChannels;

    private static final String TAG = "ControlActivity";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);


        clientsList = new ArrayList<>();

        initializeUI();

        //discoverClients();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter(){
        ControlActivityAdapter adapter = new ControlActivityAdapter(getApplicationContext(), clientsList);
        listView.setAdapter(adapter);
    }

    private void initializeUI(){
        assignChannels = findViewById(R.id.assign_channels);

        listView = findViewById(R.id.client_list);

        assignChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignChannels();
                new BroadcastMessageTask().execute();
            }
        });
    }

    private void setUpHotspot(String channelNo){
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager != null && wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
        Method getWifiApConfigurationMethod = null;
        try {
            getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);

            Log.d("Writing HotspotData", "\nSSID:" + netConfig.SSID + "\nPassword:" + netConfig.preSharedKey + "\n");

            if (netConfig.preSharedKey == "") {
                //netConfig.SSID = "Spectrum app";
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                netConfig.SSID = "Spectrum app";
                //netConfig.preSharedKey = passWord;
                netConfig.hiddenSSID = true;
                netConfig.status = WifiConfiguration.Status.ENABLED;
                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                Field wcFreq = netConfig.getClass().getField("apChannel");

                wcFreq.setInt(netConfig, Integer.parseInt(channelNo));
                int val = wcFreq.getInt(netConfig);




            }
            Method setWifiApConfigurationMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setWifiApConfigurationMethod.invoke(wifiManager, netConfig);

            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, netConfig, true);



            // For Saving Data
            wifiManager.saveConfiguration();
            //setup(netConfig.SSID, netConfig.preSharedKey,wifiManager, netConfig, channelNo);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public static void discoverClients(){
        clientsList.clear();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] clientInfo = line.split(" +");
                String mac = clientInfo[3];
                if (mac.matches("..:..:..:..:..:..")) { // To make sure its not the title
                    Client client = new Client(clientInfo[0],clientInfo[3]);
                    clientsList.add(client);
                }

            }
        } catch (java.io.IOException aE) {
            aE.printStackTrace();

        }
    }

    private void assignChannels(){
        ArrayList<Integer> list=new ArrayList<>();
//        int size=clientsList.size();
        discoverClients();
        Random random = new Random();
        for(Client c:clientsList){
            int randomNo;
            while (true){
                randomNo= random.nextInt(10 - 1 + 1) + 1;
                if (!list.contains(randomNo)){
                    list.add(randomNo);
                    c.setChannel(randomNo);
                    break;
                }
            }
        }

    }

    private String generateBroadcastMessage(){
        //message format -> ip_address,mac_address,channel_no ip_address,mac_address,channel_no, ...
        String message="ClientActivity@";
        for (Client c:clientsList){
            message+=c.getIpAddress()+",";
            message+=c.getMacAddress()+",";
            message+=c.getChannelNo()+" ";


        }
        return message;
    }

    class BroadcastMessageTask extends AsyncTask<Void, Void, Void> {

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
            setAdapter();
            //assignChannels.setEnabled(false);

        }
    }



}
