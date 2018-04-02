package com.invictus.prabodha.spectrummanager;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.invictus.prabodha.spectrummanager.Client.ClientActivity;
import com.invictus.prabodha.spectrummanager.Server.ControlActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    //static HashMap<String,Integer> deviceList;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeUI();
    }


    private void initializeUI() {
        Button centralUser = findViewById(R.id.central_user);
        Button otherUser = findViewById(R.id.other_user);
        Button hotspot = findViewById(R.id.hotspot);


        //  deviceList = new HashMap<>();
        centralUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ControlActivity.class);
                startActivity(myIntent);
            }
        });

        otherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ClientActivity.class);
                startActivity(myIntent);

            }
        });

        hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createHotspot();
                new SetUpHotspotTask().execute();
            }
        });

    }

    public void setHotSpot(String SSID, String passWord) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);


        Method[] mMethods = wifiManager.getClass().getDeclaredMethods();

        for (Method mMethod : mMethods) {

            if (mMethod.getName().equals("setWifiApEnabled")) {
                WifiConfiguration wifiConfig = new WifiConfiguration();
                if (passWord == "") {
                    wifiConfig.SSID = SSID;
                    wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                } else {
                    wifiConfig.SSID = SSID;
                    wifiConfig.preSharedKey = passWord;
                    wifiConfig.hiddenSSID = false;
                    wifiConfig.status = WifiConfiguration.Status.ENABLED;
                    wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                }
                try {

                    Field wcFreq = wifiConfig.getClass().getField("channel");

                    wcFreq.setInt(wifiConfig, 11);
                    int val = wcFreq.getInt(wifiConfig);
                    Log.d(TAG, val + "");
                    mMethod.invoke(wifiManager, wifiConfig, true);
                    wifiManager.saveConfiguration();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }


    private void createHotspot() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        Method getWifiApConfigurationMethod = null;
        try {
            getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration netConfig = (WifiConfiguration) getWifiApConfigurationMethod.invoke(wifiManager);

            Log.d("Writing HotspotData", "\nSSID:" + netConfig.SSID + "\nPassword:" + netConfig.preSharedKey + "\n");

            if (netConfig.preSharedKey == "") {
                //netConfig.SSID = netConfig.SSID;
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                //netConfig.SSID = "Spectrum app";
                //netConfig.preSharedKey = passWord;
                netConfig.hiddenSSID = false;
                netConfig.status = WifiConfiguration.Status.ENABLED;
                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                Field wcFreq = netConfig.getClass().getField("channel");

                wcFreq.setInt(netConfig, 11);
                int val = wcFreq.getInt(netConfig);
                Log.d(TAG, val + "");


            }
            Method setWifiApConfigurationMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setWifiApConfigurationMethod.invoke(wifiManager, netConfig);

            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, netConfig, true);


            // For Saving Data

            wifiManager.saveConfiguration();

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

    class SetUpHotspotTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {

            createHotspot();
            //setHotSpot("Prabodha","bloodbank");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }


}