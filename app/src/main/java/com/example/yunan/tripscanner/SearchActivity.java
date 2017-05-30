package com.example.yunan.tripscanner;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.Projection;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private int mStartYear;
    private int mStartMonth;
    private int mStartDay;
    private int mEndYear;
    private int mEndMonth;
    private int mEndDay;
    private String mStartString;
    private String mEndString;
    private Place mPlace;
    Button minButton;
    Button maxButton;
    Button searchButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("동행 검색");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //장소 검색
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                //txtPlaceDetails.setText(placeDetailsStr);

                mPlace = place;
            }

            @Override
            public void onError(Status status) {
                // Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });


        //달력검색
        minButton = (Button) findViewById(R.id.minDate);
        maxButton = (Button) findViewById(R.id.maxDate);
        searchButton = (Button) findViewById(R.id.searchButton);
        final SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    Calendar minCal = Calendar.getInstance();
                    Calendar maxCal = Calendar.getInstance();
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        // grab the date range, do what you want


                        mStartString = yearStart + "-" + (monthStart+1) + "-" + dayStart;
                        mEndString = yearEnd + "-" + (monthEnd+1) + "-" + dayEnd;

                    }
                }

        );
        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                minButton.setText(mStartString);
                maxButton.setText(mEndString);


                CommunicationManager communication = new CommunicationManager();
                communication.GET("http://localhost:3000/api/v1/trips?address=수지구&city=수지구");
            }
        });
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }


    @Override
    public void onBackPressed(){
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentMain);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
