package com.invictus.prabodha.spectrummanager.MessagePassing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by acer on 3/28/2018.
 */

public class UDPClient {


    public void sendPacket(String ip, String message){
            DatagramSocket socket=null;
        try {
            socket = new DatagramSocket();
            byte [] buffer = message.getBytes();
            InetAddress group = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastPublisher.PORT);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }

        }


    }
}
