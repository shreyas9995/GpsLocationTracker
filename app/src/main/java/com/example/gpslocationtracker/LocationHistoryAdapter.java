package com.example.gpslocationtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class LocationHistoryAdapter extends ArrayAdapter<Whereabouts> {

    private Context mcontext;
    int mResource;

    public LocationHistoryAdapter(Context context, int resource, ArrayList<Whereabouts> objects){
        super(context,resource,objects);
        mcontext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String latitude = getItem(position).getLatitude();
        String longitude = getItem(position).getLongitude();
        String area = getItem(position).getArea();

        Whereabouts whereabouts = new Whereabouts(latitude, longitude, area);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvarea = (TextView) convertView.findViewById(R.id.area);
        TextView tvlatitude = (TextView) convertView.findViewById(R.id.latitude);
        TextView tvlongitude = (TextView) convertView.findViewById(R.id.longitude);

        tvarea.setText(area);
        tvlatitude.setText(latitude);
        tvlongitude.setText(longitude);

        return convertView;
    }
}
