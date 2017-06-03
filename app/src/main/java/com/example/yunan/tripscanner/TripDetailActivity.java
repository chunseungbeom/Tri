package com.example.yunan.tripscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class TripDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);



        HashMap<String,Object> tripTemp = (HashMap<String,Object>)  getIntent().getSerializableExtra("Trip");

        setTitle(tripTemp.get("address").toString());
    }
}
