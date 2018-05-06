package com.invictus.prabodha.spectrummanager.Sense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invictus.prabodha.spectrummanager.Models.Channel;
import com.invictus.prabodha.spectrummanager.R;

import java.util.List;

/**
 * Created by acer on 4/25/2018.
 */

public class SenseActivityAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    //    List <WifiSignal>wifiList;
    private List<Channel> channelList;


    public SenseActivityAdapter(Context context, List list){
        this.context = context;
        this.channelList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return channelList.size();
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
            view = inflater.inflate(R.layout.activity_sense_cell, null);
            holder = new Holder();
            holder.tvDetails = (TextView) view.findViewById(R.id.tvDetails);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
//        holder.tvDetails.setText("SSID :: " + wifiList.get(position).getSSID()
//                + "\nStrength :: " + wifiList.get(position).getLevel()
//                + "\nBSSID :: " + wifiList.get(position).getSSID()
//                + "\nChannel :: "+ wifiList.get(position).getChannel()
//                + "\nFrequency :: " + wifiList.get(position).getFrequency()
//                + "\nChannel Width :: " + wifiList.get(position).getChannelWidth());
        holder.tvDetails.setText("Channel no :: " + channelList.get(position).getChannelNo()
                + "\nFrequency :: " + channelList.get(position).getFrequency()
                + "\nRating :: " + channelList.get(position).getRating());
        return view;
    }


    class Holder {
        TextView tvDetails;

    }
}
