package com.invictus.prabodha.spectrummanager.MessagePassing;

import android.content.Intent;
import android.util.Log;

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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent broadcast = new Intent(ClientActivity.ACTION_PACKET_RECEIVED);
        broadcast.putExtra(ClientActivity.EXTRA_DATA, data);
        ClientActivity.getContext().sendBroadcast(broadcast);

    }
}
