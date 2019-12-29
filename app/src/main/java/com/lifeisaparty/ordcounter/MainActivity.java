package com.lifeisaparty.ordcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import maes.tech.intentanim.CustomIntent;


public class MainActivity extends AppCompatActivity {

    Button settingsbutton;
    TextView ord_date_textview;
    TextView leave_quota_textview;
    TextView off_quota_textview;
    TextView payday_textview;
    TextView ord_countdown_textview;
    TextView working_days_textview;
    String orddate;
    String leavequota;
    String offquota;
    int payday;
    CircularSeekBar seekBar;
    int serviceduration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updatecontingency(); //for transition to version 5.0 from 4.0

        //Initialize all buttons/textviews/edittext here
        settingsbutton = findViewById(R.id.settingsbutton);
        ord_date_textview = findViewById(R.id.ord_date_textview);
        leave_quota_textview = findViewById(R.id.leave_quota_textview);
        off_quota_textview = findViewById(R.id.off_quota_textview);
        payday_textview = findViewById(R.id.payday_textview);
        ord_countdown_textview = findViewById(R.id.ord_countdown_textview);
        working_days_textview = findViewById(R.id.working_days_textview);
        seekBar = findViewById(R.id.seekBar);

        //Disable user interaction on seekbar
        seekBar.isTouchEnabled = false;

        //Use sharedpreferences to read user data
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        orddate = sharedPreferences.getString("orddate", "");
        leavequota = sharedPreferences.getString("leavequota", "");
        offquota = sharedPreferences.getString("offquota", "");
        payday = sharedPreferences.getInt("payday", 10);

        ord_date_textview.setText(orddate);
        leave_quota_textview.setText(leavequota);
        off_quota_textview.setText(offquota);

        /*
            Declare date class and pass orddate & payday variable for calculations
         */
        Dates date = new Dates();
        date.orddate = orddate;
        date.payday = payday;

        /*
            Get Serviceduration to update Seek Bar
        */
        if(sharedPreferences.getString("serviceduration", "").equals("2 YEARS")){
            serviceduration = 730;
            date.serviceduration = serviceduration;
        }
        else if(sharedPreferences.getString("serviceduration", "").equals("1 YEAR 10 MONTHS")){
            serviceduration = 669;
            date.serviceduration = serviceduration;
        }

        if(orddate.equals("")){ //For initial installion, there is no ord date in texview
            seekBar.setProgress(0);
        }
        else{
            seekBar.setProgress(date.percentageofdays());
        }

        ord_countdown_textview.setText(date.daystillord());
        payday_textview.setText(date.daystillpayday());
        working_days_textview.setText(date.workingdays());

        //Open Settings on settings button click
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Settings.class));
                finish(); //ensures that the current activity is destroyed
            }
        });
    }



    /*
        Massive update from Version 4 to Version 5. Scrapped the use of Internal Files. Using Shared Preferences instead. This function is to facilitate the transition.
     */
    public void updatecontingency(){

        String[] StringArray = {"orddate", "enlistmentdate", "serviceduration", "leavequota", "offquota", "payday"};

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //if orddate.txt exists, user is transitioning from Version 4
        File file = getBaseContext().getFileStreamPath("orddate.txt");

        if(file.exists()){

            for(int i=0; i<StringArray.length; i++){
                String x = StringArray[i];

                try{
                    FileInputStream fIn1 = getApplicationContext().openFileInput(x + ".txt");
                    InputStreamReader isr = new InputStreamReader(fIn1);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }

                    getApplicationContext().deleteFile(x + ".txt"); //delete the txt files once completed

                    if(x.equals("payday")){
                        String myString = sb.toString();
                        editor.putInt(x, Integer.parseInt(myString));
                        editor.commit();
                    }
                    else{
                        editor.putString(x, sb.toString());
                        editor.commit();
                    }


                } catch (IOException e){

                }

            }

        }

    }

}
