package com.example.gpslocationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LocationHistory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Intent intent = getIntent();
        List<Whereabouts> locationHistory = (List<Whereabouts>) intent.getSerializableExtra("locationlist");
        LocationHistoryAdapter adapter = new LocationHistoryAdapter(this, R.layout.location_detail,
                (ArrayList<Whereabouts>) locationHistory);

        ListView locationView = (ListView) findViewById(R.id.location_list);
        locationView.setAdapter(adapter);

    }
}
