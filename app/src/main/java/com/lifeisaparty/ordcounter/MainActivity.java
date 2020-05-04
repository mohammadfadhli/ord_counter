package com.lifeisaparty.ordcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity {

    ImageButton settingsbutton;
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
    TextView percentage_textview;
    TextView ord_countdown_label;
    Boolean isnightmodeon;
    int versionCode = 0;
    Boolean firstrun;
    TextView payday_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Use sharedpreferences to read user data
        final SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Check if Night Mode is on from sharedpreferences
        isnightmodeon = sharedPreferences.getBoolean("isnightmodeon", false);

        //Night Mode
        if(isnightmodeon.equals(false))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(isnightmodeon.equals(true))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Rate App Dialog
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(2)
                .setRemindInterval(2)
                .setShowLaterButton(true)
                .setDebug(false)
                .setOnClickButtonListener(new OnClickButtonListener() {
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show dialog if meets condition
        AppRate.showRateDialogIfMeetsConditions(this);

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
        percentage_textview = findViewById(R.id.percentage_textview);
        ord_countdown_label = findViewById(R.id.ord_countdown_label);
        payday_label = findViewById(R.id.payday_label);

        //Disable user interaction on seekbar
        seekBar.isTouchEnabled = false;

        orddate = sharedPreferences.getString("orddate", "");
        leavequota = sharedPreferences.getString("leavequota", "");
        offquota = sharedPreferences.getString("offquota", "");
        payday = sharedPreferences.getInt("payday", 10);
        firstrun = sharedPreferences.getBoolean("firstrun", true);

        //Show Changelog for Updates
        try{
            PackageInfo packageinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageinfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        if(versionCode == 7 && firstrun.equals(true))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("1. BRAND NEW! User Interface \n2. Settings now auto-saves \n3. Toggle Light/Dark Mode \n4. Bugs Fixes")
                    .setTitle("What's New in v7.0")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
            editor.putBoolean("firstrun", false);
            editor.apply();
        }

        if(orddate.equals("")){ //For initial installion, there is no ord date in textview
            ord_date_textview.setText("NIL");
        }
        else if(orddate.equals("DD/MM/YYYY"))
        {
            ord_date_textview.setText("NIL");
        }
        else{
            ord_date_textview.setText(orddate);
        }
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
        if(sharedPreferences.getString("serviceduration", "2 YEARS").equals("2 YEARS")){
            serviceduration = 730;
            date.serviceduration = serviceduration;
        }
        else if(sharedPreferences.getString("serviceduration", "2 YEARS").equals("1 YEAR 10 MONTHS")){
            serviceduration = 669;
            date.serviceduration = serviceduration;
        }

        if(orddate.equals("")){ //For initial installion, there is no ord date in textview
            seekBar.setProgress(0);
            percentage_textview.setText("0%");
        }
        else if(orddate.equals("DD/MM/YYYY"))
        {
            seekBar.setProgress(0);
            percentage_textview.setText("0%");
        }
        else if(Integer.parseInt(date.daystillord()) > 730) //if user has not enlisted but downloaded the app for future reference
        {
            seekBar.setProgress(0);
            percentage_textview.setText("0%");
        }
        else{
            seekBar.setProgress(date.percentageofdays());
            percentage_textview.setText(Integer.toString(date.percentageofdays()) + "%");
        }

        //For initial installion, there is no leavequota/offquota in textview
        if(leavequota.equals("")){
            leave_quota_textview.setText("0");
        }

        if(offquota.equals("")){
            off_quota_textview.setText("0");
        }

        //Display "ORDLO" if daystillsord <=0, orddate is not empty and orddate not equal to "DD/MM/YYYY"
        if(Integer.parseInt(date.daystillord()) <= 0 && !orddate.equals("") && !orddate.equals("DD/MM/YYYY")){
            ord_countdown_textview.setText("ORDLO!");
            ord_countdown_label.setText("Where got time NS");
        }
        else if(Integer.parseInt(date.daystillord()) == 1 && !orddate.equals("") && !orddate.equals("DD/MM/YYYY")) //if days till ord == 1, String should be singular
        {
            ord_countdown_textview.setText(date.daystillord());
            ord_countdown_label.setText("Day to ORD");
        }
        else{
            ord_countdown_textview.setText(date.daystillord());
        }

        if(date.daystillpayday().equals("0"))
        {
            payday_textview.setText("TODAY");
            payday_label.setText("is Payday");
        }
        else if(date.daystillpayday().equals("1"))
        {
            payday_textview.setText(date.daystillpayday() + " day");
        }
        else
        {
            payday_textview.setText(date.daystillpayday() + " days");
        }

        if(leavequota.equals(""))
        {
            leavequota = "0";
        }

        if(offquota.equals(""))
        {
            offquota = "0";
        }
        Float workingdayscalc = Float.parseFloat(leavequota) + Float.parseFloat(offquota);
        workingdayscalc = Float.parseFloat(date.workingdays()) - workingdayscalc;
        if(workingdayscalc % 1 == 0)
        {
            working_days_textview.setText(Integer.toString(Math.round(workingdayscalc)));
        }
        else
        {
            working_days_textview.setText(Float.toString(workingdayscalc));
        }

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
