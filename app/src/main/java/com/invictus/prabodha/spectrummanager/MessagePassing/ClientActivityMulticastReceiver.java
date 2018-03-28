package com.invictus.prabodha.spectrummanager.MessagePassing;

import android.content.Intent;
import android.util.Log;

import com.invictus.prabodha.spectrummanager.Advertise.AdvertiseActivity;
import com.invictus.prabodha.spectrummanager.Client.ClientActivity;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Created by acer on 3/27/2018.
 */

public class ClientActivityMulticastReceiver extends MulticastReceiver {

    @Override
    protected void notifyReceiveComplete(String data, String ip) {
        super.notifyReceiveComplete(data, ip);

        if(data.startsWith("ClientActivity")){
            Intent broadcast = new Intent(ClientActivity.ACTION_PACKET_RECEIVED);
            broadcast.putExtra(ClientActivity.EXTRA_DATA, data);
            broadcast.putExtra(ClientActivity.EXTRA_IP_ADDRESS, ip);
            ClientActivity.getContext().sendBroadcast(broadcast);
        } else if(data.startsWith("AdvertiseActivity")){
            Intent broadcast = new Intent(AdvertiseActivity.ACTION_PACKET_RECEIVED);
            broadcast.putExtra(AdvertiseActivity.EXTRA_DATA, data);
            broadcast.putExtra(ClientActivity.EXTRA_IP_ADDRESS, ip);
            AdvertiseActivity.getContext().sendBroadcast(broadcast);
        } else if(data.startsWith("RequestChannel")) {
            Intent broadcast = new Intent(AdvertiseActivity.ACTION_REQUEST_CHANNEL_PACKET_RECEIVED);
            broadcast.putExtra(AdvertiseActivity.EXTRA_DATA, data);
            broadcast.putExtra(ClientActivity.EXTRA_IP_ADDRESS, ip);
            AdvertiseActivity.getContext().sendBroadcast(broadcast);
        } else if(data.startsWith("ChannelGranted")) {
            Intent broadcast = new Intent(AdvertiseActivity.ACTION_CHANNEL_GRANT_PACKET_RECEIVED);
            broadcast.putExtra(AdvertiseActivity.EXTRA_DATA, data);
            broadcast.putExtra(ClientActivity.EXTRA_IP_ADDRESS, ip);
            AdvertiseActivity.getContext().sendBroadcast(broadcast);
        }


    }
}
