package com.invictus.prabodha.spectrummanager.Server;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Models.Client;
import com.invictus.prabodha.spectrummanager.R;

import java.util.ArrayList;

/**
 * Created by acer on 3/26/2018.
 */

public class ControlActivityAdapter extends BaseAdapter {

    Context context;
    ArrayList<Client> clientList;
    LayoutInflater inflater;

    public ControlActivityAdapter(Context context, ArrayList list){
        this.context = context;
        this.clientList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return clientList.size();
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
        Holder holder;
        System.out.println("viewpos" + position);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_control_cell, null);
            holder = new Holder();
            holder.tvIPAddress = view.findViewById(R.id.tv_ip_value);
            holder.tvMACAddress = view.findViewById(R.id.tv_mac_value);
            holder.tvChannelNo = view.findViewById(R.id.tv_channel_value);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Client client = clientList.get(position);
        holder.tvIPAddress.setText(client.getIpAddress());
        holder.tvMACAddress.setText(client.getMacAddress());
        holder.tvChannelNo.setText(String.valueOf(client.getChannelNo()));

        return view;
    }


    class Holder {
        TextView tvIPAddress;
        TextView tvMACAddress;
        TextView tvChannelNo;

    }
}
