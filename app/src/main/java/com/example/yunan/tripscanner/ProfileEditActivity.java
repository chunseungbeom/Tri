package com.example.yunan.tripscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ProfileEditActivity extends AppCompatActivity {

    private ProfileSaveTask mProfileSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        Button saveButton = (Button) findViewById(R.id.button_Save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSaveTask = new ProfileSaveTask();
                mProfileSaveTask.execute((Void) null);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class ProfileSaveTask extends AsyncTask<Void, Void, Boolean>{
        ProfileSaveTask() {

        }


        @Override
        protected Boolean doInBackground(Void... params) {
            //attempt authentication against a network service.
            String response = "";



            //CommunicationManager communication = new CommunicationManager();
            //response = communication.POST("http://huy.dlinkddns.com/api/v1/users/sign_in", user);

            if(response.contains("error")){
                return false;
            }

            /*ObjectMapper mapper = new ObjectMapper();
            try {
                user = mapper.readValue(response, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mProfileSaveTask = null;

            if (success) {
                Intent intent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
                finish();
            } else {

            }
        }
    }


}
