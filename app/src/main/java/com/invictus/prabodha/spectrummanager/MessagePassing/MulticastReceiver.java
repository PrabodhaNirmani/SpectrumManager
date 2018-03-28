package com.invictus.prabodha.spectrummanager.MessagePassing;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by acer on 3/27/2018.
 */

abstract class MulticastReceiver extends Thread {
    private MulticastSocket socket = null;
    private byte[] buf = new byte[256];

    public void run() {
        try {

            socket = new MulticastSocket(MulticastPublisher.PORT);
            InetAddress group = InetAddress.getByName("230.0.0.0");

            socket.joinGroup(group);
            while (true){
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                notifyReceiveComplete(received, packet.getAddress().getHostAddress());
            }

            //socket.leaveGroup(group);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }

    }

    protected void notifyReceiveComplete(String data, String ip){

    }
}
