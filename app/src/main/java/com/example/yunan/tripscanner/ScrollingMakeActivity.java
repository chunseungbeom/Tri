package com.example.yunan.tripscanner;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.io.File;
import android.os.Environment;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ScrollingMakeActivity extends AppCompatActivity {
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
    private SearchTask mSearchTask;



    public static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private String mCurrentPhotoPath;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
    private Uri mImageCaptureUri;
    private static final String IMAGE_UNSPECIFIED = "image/*";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_make);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("동행 등록");
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
                //검색 쿼리 날려서 리스트뷰에 출력하기 (Material Card 이용)
                String city = mPlace.getName().toString();
                mSearchTask = new SearchTask(city, mStartString, mEndString);
                mSearchTask.execute((Void) null);

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

        //RecyclerView divider height control // item(cardview)에 margin 넣으면 필요없음.
        /*mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private final int dividerHeight = 0;
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = dividerHeight;
            }
        });*/

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //사진 업로드 버튼 액션
        mImageView = (ImageView) findViewById(R.id.imageView_Profile_Photo);
        mImageBitmap = null;
        if (savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState
                    .getParcelable(URI_INSTANCE_STATE_KEY);
        }
        try {
            createImageFile();
        } catch (IOException e) {
            mImageView.setImageResource(R.drawable.ln_logo);
        }
        photoDirectory();
        loadSnap();


        final Button button = (Button) findViewById(R.id.button_Change);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScrollingMakeActivity.this);
                alertDialog.setTitle(R.string.pick_profile_picture);

                alertDialog.setItems(R.array.change_button_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0) {
                            // Open Camera
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivity(intent);
                            //Specify the uri of the image
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            //startActivityForResult(intent, PICK_FROM_CAMERA);
                        }
                        if (which == 1){
                            //Select from Gallery
                            Intent intent2 = new Intent();
                            intent2.setType("image/*");
                            intent2.putExtra("crop", "true");
                            intent2.setAction(Intent.ACTION_GET_CONTENT);
                            intent2.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            startActivityForResult(Intent.createChooser(intent2, "Complete action using"), PICK_FROM_FILE);
                        }

                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });



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


    public class SearchTask extends AsyncTask<Void, Void, Trip> {
        private String mAddress;
        private String mCheckIn;
        private String mCheckOut;
        SearchTask(String address, String checkIn, String checkOut) {
            mAddress = address;
            mCheckIn = checkIn;
            mCheckOut = checkOut;
        }



        @Override
        protected Trip doInBackground(Void... params) {
            //attempt authentication against a network service.

            CommunicationManager communication = new CommunicationManager();
            String searchResult = communication.QUERY("http://huy.dlinkddns.com/api/v1/trips", mAddress, mCheckIn, mCheckOut);

            Trip trip = new Trip();
            ObjectMapper mapper = new ObjectMapper();
            try {
                trip = mapper.readValue(searchResult, Trip.class);
            } catch (IOException e) {
                e.printStackTrace();
            }



            return trip;
        }

        @Override
        protected void onPostExecute(final Trip trip) {
            mSearchTask = null;


            RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), trip);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            mSearchTask = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_CAMERA) {
                cropImage();
                Bundle extras = data.getExtras();
                // Set the picture image in UI
                mImageView.setImageBitmap((Bitmap) extras.getParcelable("data"));
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists())
                    f.delete();
            }
            if (requestCode == PICK_FROM_FILE) {
                cropImage();
                Bundle extras2 = data.getExtras();
                //mImageView.setImageBitmap((Bitmap) extras2.getParcelable("data"));
            }
        }
    }

// ****************** private helper functions ***************************//

    private void loadSnap() {
        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            mImageView.setImageBitmap(bmap);
            fis.close();
        }
        catch (IOException e) {
            // Default profile photo if no photo saved before.
            mImageView.setImageResource(R.drawable.ln_logo);
        }
    }
    private void saveSnap() {
        // Commit all the changes into preference file
        // Save profile image into internal storage.
        mImageView.buildDrawingCache();
        Bitmap bmap = mImageView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(mCurrentPhotoPath, MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     *
     */
    // A method that returns a unique file name for a new photo using a date-time stamp:
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private String photoDirectory(){
        return mCurrentPhotoPath;
    }

    //With the createImageFile() method available to create a file for the photo,
    // you can now create and invoke the Intent like this:

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */
		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }
    /**
     * Invoke the system's media scanner to add your photo to the Media Provider's database,
     * making it available in the Android Gallery application and to other apps.
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    /**Some lifecycle callbacks so that the image can survive orientation change*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }
    /** Settings overflow menu option */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling_make, menu);
        return true;
    }

    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
    }
}

