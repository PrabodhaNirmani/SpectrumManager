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
    protected void notifyReceiveComplete(DatagramPacket packet) {
        super.notifyReceiveComplete(packet);

        String data= null;
        try {
            data = new String(packet.getData(),"UTF-8").trim();

            if(data.startsWith("ClientActivity")){
                Intent broadcast = new Intent(ClientActivity.ACTION_PACKET_RECEIVED);
                broadcast.putExtra(ClientActivity.EXTRA_DATA, data);
                ClientActivity.getContext().sendBroadcast(broadcast);
            }else if(data.startsWith("AdvertiseActivity")){
                Intent broadcast = new Intent(AdvertiseActivity.ACTION_PACKET_RECEIVED);
                broadcast.putExtra(AdvertiseActivity.EXTRA_DATA, data);
                AdvertiseActivity.getContext().sendBroadcast(broadcast);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




    }
}
