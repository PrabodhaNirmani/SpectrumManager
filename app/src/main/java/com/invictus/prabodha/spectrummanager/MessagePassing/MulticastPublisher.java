package com.invictus.prabodha.spectrummanager.MessagePassing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by acer on 3/27/2018.
 */

public class MulticastPublisher {
//    private DatagramSocket socket;
    private MulticastSocket socket;

    public static final int PORT = 4446;

    private InetAddress group;
    private byte[] buf;

    public void multicast(String multicastMessage) throws IOException {
        socket = new MulticastSocket();

        NetworkInterface eth0 = null;
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();

        while (enumeration.hasMoreElements())
        {
            eth0 = enumeration.nextElement();

            if ("wlan0".equalsIgnoreCase (eth0.getDisplayName()))
            {
                break;
            }
        }

        socket.setNetworkInterface(eth0);

        socket.setBroadcast(true);

        group = InetAddress.getByName("230.0.0.0");
        buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
        socket.send(packet);
        socket.close();
    }
}
