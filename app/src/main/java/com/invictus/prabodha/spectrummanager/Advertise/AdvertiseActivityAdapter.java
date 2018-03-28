package com.invictus.prabodha.spectrummanager.Advertise;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.MessagePassing.MulticastPublisher;
import com.invictus.prabodha.spectrummanager.MessagePassing.UDPClient;
import com.invictus.prabodha.spectrummanager.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by acer on 3/28/2018.
 */

public class AdvertiseActivityAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> messagesList;
    private LayoutInflater inflater;

    AdvertiseActivityAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.messagesList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String [] msgList = messagesList.get(position).split(",");

        final String textMessage = msgList[0];
        final String ipAddress = msgList[2];
        //final String macAddress = msgList[2];
        String timeStamp = msgList[1];

        AdvertiseActivityAdapter.Holder holder;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_advertise_cell, null);
            holder = new Holder();
            holder.tvMessage = view.findViewById(R.id.tv_message);
            holder.tvIPAddress = view.findViewById(R.id.tv_ip_value);
           // holder.tvMACAddress = view.findViewById(R.id.tv_mac_value);
            holder.tvTimestamp = view.findViewById(R.id.tv_timestamp);
            holder.btnRequestChannel = view.findViewById(R.id.request_channel);

            holder.btnRequestChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //advertise to the host
                    new RequestChannelTask().execute(ipAddress, generatePacketMassage(textMessage));

                }
            });

            view.setTag(holder);
        } else {
            holder = (AdvertiseActivityAdapter.Holder) view.getTag();
        }


        holder.tvMessage.setText(textMessage);
        holder.tvIPAddress.setText(ipAddress);
       // holder.tvMACAddress.setText(macAddress);
        holder.tvTimestamp.setText(timeStamp);

        return view;
    }

    private String generatePacketMassage(String textMessage){
        String message = "RequestChannel@";
        message += textMessage.split(" ")[1].trim();
        return message;

    }

    class Holder {

        TextView tvMessage;
        TextView tvIPAddress;
       // TextView tvMACAddress;
        TextView tvTimestamp;

        Button btnRequestChannel;
    }

    class RequestChannelTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... voids) {
            String ipAddress = voids[0];

            String message = voids[1];

            new UDPClient().sendPacket(ipAddress,message);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }
}
