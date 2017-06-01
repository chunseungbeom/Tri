package com.example.yunan.tripscanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunan on 2017-05-24.
 */

public class Trip {

    private Map<String,Object> trip = new HashMap<String,Object>();
    private List<Map<String,Object>> trips = new ArrayList<Map<String,Object>>();

    public Map<String,Object> getTrip(){
        return trip;
    }
    public List getTrips(){
        return trips;
    }
}
