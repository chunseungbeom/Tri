package com.example.yunan.tripscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.io.IOException;

public class ScrollingSearchActivity extends AppCompatActivity {
    private Place mPlace;
    private String mStartString;
    private String mEndString;
    private Button mSearchMinDateButton;
    private Button mSearchMaxDateButton;
    private SmoothDateRangePickerFragment mSmoothDateRangePickerFragment;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private FloatingActionButton mFabBottom;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("동행검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        //장소 검색
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.parseColor("#FFFFFF"));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());

                /*String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                txtPlaceDetails.setText(placeDetailsStr);*/

                mPlace = place;
            }

            @Override
            public void onError(Status status) {
                // Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });



        mSearchMinDateButton = (Button) findViewById(R.id.search_button_mindate);
        mSearchMinDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        mSearchMaxDateButton = (Button) findViewById(R.id.search_button_maxdate);
        mSearchMaxDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDate();
            }
        });


        mFab = (FloatingActionButton) findViewById(R.id.search_fab);
        mFabBottom = (FloatingActionButton) findViewById(R.id.search_fab_bottom);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset){
                //is Fully Expanded
                //if(verticalOffset == 0){}                }

                //is Being Expanded
                if(verticalOffset > -360){
                    mFabBottom.hide();
                }
                //is Being Contracted
                else{//is
                    mFabBottom.show();
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppBarLayout.setExpanded(false);
                mFabBottom.show();

                if(mPlace == null || mStartString == null || mEndString == null){
                    return;
                }
                //TODO: 검색 쿼리 날려서 리스트뷰에 출력하기 (Material Card or ListView 이용)
                CommunicationManager communication = new CommunicationManager();
                String address = mPlace.getAddress().toString();
                String city = mPlace.getName().toString();
                String searchResult = communication.QUERY("http://huy.dlinkddns.com/api/v1/trips", address, city);

                Trip trip = new Trip();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    trip = mapper.readValue(searchResult, Trip.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), trip);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        mFabBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppBarLayout.setExpanded(true);
                mFabBottom.hide();
            }
        });


        //Searching Result View
        View resultView = findViewById(R.id.include_result_view);
        //getLayoutInflater().inflate(R.layout.content_scrolling_search, null, false);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) resultView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);





    }

    //user call calendar and select date
    protected void setDate(){
        mSmoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        // grab the date range, do what you want
                        mStartString = yearStart + "-" + (monthStart+1) + "-" + dayStart;
                        mEndString = yearEnd + "-" + (monthEnd+1) + "-" + dayEnd;
                        mSearchMinDateButton.setTextColor(Color.BLACK);
                        mSearchMaxDateButton.setTextColor(Color.BLACK);
                        mSearchMinDateButton.setText(mStartString);
                        mSearchMaxDateButton.setText(mEndString);
                    }
                }

        );

        mSmoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");

    }



    /*protected void makeList(){
        *//** JSON -> LIST 가공 메소드 **//*
        public void makeList(String myJSON) {
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                posts = jsonObj.getJSONArray(TAG_RESULTS);
                for(int i=0; i<posts.length(); i++) {
                    //JSON에서 각각의 요소를 뽑아옴
                    JSONObject c = posts.getJSONObject(i);
                    String title = c.getString(TAG_TITLE);
                    String writer = c.getString(TAG_WRITER);
                    String date = c.getString(TAG_DATE);
                    String content = c.getString(TAG_CONTENT);
                    if(content.length() > 50 ) {
                        content = content.substring(0,50) + "..."; //50자 자르고 ... 붙이기
                    }
                    if(title.length() > 16 ) {
                        title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                    }

                    //HashMap에 붙이기
                    HashMap<String,String> posts = new HashMap<String,String>();
                    posts.put(TAG_TITLE,title);
                    posts.put(TAG_WRITER,writer);
                    posts.put(TAG_DATE,date);
                    posts.put(TAG_CONTENT, content);

                    //ArrayList에 HashMap 붙이기
                    noticeList.add(posts);
                }
                //카드 리스트뷰 어댑터에 연결
                NoticeAdapter adapter = new NoticeAdapter(getActivity(),noticeList);
                Log.e("onCreate[noticeList]", "" + noticeList.size());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }catch(JSONException e) {
                e.printStackTrace();
            }
        }

    }*/
}
