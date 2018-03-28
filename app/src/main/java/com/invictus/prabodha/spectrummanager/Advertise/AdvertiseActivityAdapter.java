package com.invictus.prabodha.spectrummanager.Advertise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Models.Client;
import com.invictus.prabodha.spectrummanager.R;
import com.invictus.prabodha.spectrummanager.Server.ControlActivityAdapter;

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
        AdvertiseActivityAdapter.Holder holder;

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_advertise_cell, null);
            holder = new Holder();
            holder.tvMessage = view.findViewById(R.id.tv_message);
            holder.tvIPAddress = view.findViewById(R.id.tv_ip_value);
            holder.tvMACAddress = view.findViewById(R.id.tv_mac_value);
            holder.tvTimestamp = view.findViewById(R.id.tv_timestamp);
            holder.tvDuration = view.findViewById(R.id.tv_free_time);

            view.setTag(holder);
        } else {
            holder = (AdvertiseActivityAdapter.Holder) view.getTag();
        }
        String [] msgList = messagesList.get(position).split(",");

        String textMessage = msgList[0];
        String ipAddress = msgList[1];
        String macAddress = msgList[2];
        String timeStamp = msgList[3];
        String duration = msgList[4];

        holder.tvMessage.setText(textMessage);
        holder.tvIPAddress.setText(ipAddress);
        holder.tvMACAddress.setText(macAddress);
        holder.tvTimestamp.setText(timeStamp);
        holder.tvDuration.setText(duration);


        return view;
    }


    class Holder {


        TextView tvMessage;
        TextView tvIPAddress;
        TextView tvMACAddress;
        TextView tvTimestamp;
        TextView tvDuration;

    }
}
