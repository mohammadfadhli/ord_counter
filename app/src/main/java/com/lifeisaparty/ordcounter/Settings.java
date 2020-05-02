package com.lifeisaparty.ordcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Settings extends AppCompatActivity {

    ImageButton setorddatebutton;
    TextView settings_ord_date_textview;
    String enlistmentdate;
    int payday;
    Calendar c;
    DatePickerDialog dpd;
    ImageButton setenlistmentdatebutton;
    TextView settings_enlistment_date_textview;
    String serviceduration;
    int servicedurationint;
    Switch payday_switch;
    Switch servicedur_switch;
    TextView payday_textview;
    TextView servicedur_textview;
    Switch nightmode_switch;
    TextView leave_quota_textviewsettings;
    TextView off_quota_textviewsettings;
    ImageButton leavequotabuttonup;
    ImageButton leavequotabuttondown;
    ImageButton offquotabuttonup;
    ImageButton offquotabuttondown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Initialize all buttons/textviews/edittext here
        settings_ord_date_textview = findViewById(R.id.settings_ord_textview);
        setorddatebutton = findViewById(R.id.set_ord_date_button);
        setenlistmentdatebutton = findViewById(R.id.set_enlistment_date_button);
        settings_enlistment_date_textview = findViewById(R.id.settings_enlistment_date_textview);
        payday_switch = findViewById(R.id.payday_switch);
        payday_textview = findViewById(R.id.payday_textview);
        servicedur_switch = findViewById(R.id.servicedur_switch);
        servicedur_textview = findViewById(R.id.servicedur_textview);
        nightmode_switch = findViewById(R.id.nightmode_switch);
        leave_quota_textviewsettings = findViewById(R.id.leave_quota_textviewsettings);
        off_quota_textviewsettings = findViewById(R.id.off_quota_textviewsettings);
        leavequotabuttonup = findViewById(R.id.leavequotabuttonup);
        leavequotabuttondown = findViewById(R.id.leavequotabuttondown);
        offquotabuttonup = findViewById(R.id.offquotabuttonup);
        offquotabuttondown = findViewById(R.id.offquotabuttondown);


        //Initializing Date Class
        final Dates date = new Dates(); //date class

        //For Decimal Formatting
        final DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        //For Toast
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;

        //Use sharedpreferences to save user data
        final SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        settings_ord_date_textview.setText(sharedPreferences.getString("orddate", ""));
        leave_quota_textviewsettings.setText(sharedPreferences.getString("leavequota", "0"));
        off_quota_textviewsettings.setText(sharedPreferences.getString("offquota", "0"));
        payday = sharedPreferences.getInt("payday", 10);
        settings_enlistment_date_textview.setText(sharedPreferences.getString("enlistmentdate", ""));
        serviceduration = sharedPreferences.getString("serviceduration", "2 YEARS");

        //Night Mode Settings
        Boolean testnightmode = sharedPreferences.getBoolean("isnightmodeon", false);
        if(testnightmode.equals(true))
        {
            nightmode_switch.setChecked(true);
        }
        else if(testnightmode.equals(false))
        {
            nightmode_switch.setChecked(false);
        }

        //Service Duration Settings
        if(serviceduration.equals("2 YEARS"))
        {
            servicedur_switch.setChecked(true);
            servicedur_textview.setText("2 Years");
        }
        else if(serviceduration.equals("1 YEAR 10 MONTHS"))
        {
            servicedur_switch.setChecked(false);
            servicedur_textview.setText("1 Year 10 Months");
        }
        else
        {
            servicedur_switch.setChecked(true);
            servicedur_textview.setText("2 Years");
        }

        //Payday Settings
        if(payday == 10)
        {
            payday_switch.setChecked(true);
            payday_textview.setText("Falls on the 10th");
        }
        else if(payday == 12)
        {
            payday_switch.setChecked(false);
            payday_textview.setText("Falls on the 12th");
        }
        else
        {
            payday_switch.setChecked(true);
            payday_textview.setText("Falls on the 10th");
        }

        //Leave Quota Auto Save Settings (NEW)
        leave_quota_textviewsettings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String e = s.toString();
                editor.putString("leavequota", e);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Toggle up & down button for Leave Quota
        leavequotabuttondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = leave_quota_textviewsettings.getText().toString();
                double i = Double.parseDouble(s);
                i = i - 0.5;
                if(i < 0)
                {
                    leave_quota_textviewsettings.setText("0");
                }
                else
                {
                    leave_quota_textviewsettings.setText(String.valueOf(format.format(i)));
                }


            }
        });

        leavequotabuttonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = leave_quota_textviewsettings.getText().toString();
                double i = Double.parseDouble(s);
                i = i + 0.5;
                if(i > 20)
                {
                    leave_quota_textviewsettings.setText("20");
                }
                else
                {
                    leave_quota_textviewsettings.setText(String.valueOf(format.format(i)));
                }
            }
        });

        //OFF Quota Auto Save Settings (NEW)
        off_quota_textviewsettings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String e = s.toString();
                editor.putString("offquota", e);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Toggle up & down button for OFF Quota
        offquotabuttondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = off_quota_textviewsettings.getText().toString();
                double i = Double.parseDouble(s);
                i = i - 0.5;
                if(i < 0)
                {
                    off_quota_textviewsettings.setText("0");
                }
                else
                {
                    off_quota_textviewsettings.setText(String.valueOf(format.format(i)));
                }


            }
        });

        offquotabuttonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = off_quota_textviewsettings.getText().toString();
                double i = Double.parseDouble(s);
                i = i + 0.5;
                if(i > 50)
                {
                    off_quota_textviewsettings.setText("50");
                }
                else
                {
                    off_quota_textviewsettings.setText(String.valueOf(format.format(i)));
                }
            }
        });

        //Enlistment Date Auto Save Settings (NEW)
        settings_enlistment_date_textview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString("enlistmentdate", s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ORD Date Auto Save Settings (NEW)
        settings_ord_date_textview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString("orddate", s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Service Duration Auto Save Settings (NEW)
        servicedur_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(servicedur_switch.isChecked())
                {
                    servicedur_textview.setText("2 Years");
                    editor.putString("serviceduration", "2 YEARS" );
                    editor.apply();
                }
                else if(!servicedur_switch.isChecked())
                {
                    servicedur_textview.setText("1 Year 10 Months");
                    editor.putString("serviceduration", "1 YEAR 10 MONTHS");
                    editor.apply();
                }
            }
        });

        //Payday Auto Save Settings (NEW)
        payday_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(payday_switch.isChecked())
                {
                    payday_textview.setText("Falls on the 10th");
                    editor.putInt("payday", 10);
                    editor.apply();
                }
                else if(!payday_switch.isChecked())
                {
                    payday_textview.setText("Falls on the 12th");
                    editor.putInt("payday", 12);
                    editor.apply();
                }
            }
        });

        //Night Mode Switch Toggle
        nightmode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(nightmode_switch.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isnightmodeon", true);
                    editor.apply();
                    //Instead of calling recreate(), Restart the activity programmatically so that we can override the transition effect
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(!nightmode_switch.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isnightmodeon", false);
                    editor.apply();
                    //Instead of calling recreate(), Restart the activity programmatically so that we can override the transition effect
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        //For initial installation, there is no ord date/enlistment date
        if(sharedPreferences.getString("orddate", "").equals("")){
            settings_ord_date_textview.setText("DD/MM/YYYY");
        }

        if(sharedPreferences.getString("enlistmentdate", "").equals("")){
            settings_enlistment_date_textview.setText("DD/MM/YYYY");
        }

        //Open Calendar Dialog when user presses the Calendar Edit button and show selected date on ord_date_edittext
        setorddatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
                alertDialogBuilder.setMessage("Do you want to generate ORD Date based on your Enlistment Date & Service Duration?")
                        .setTitle("Set ORD Date")
                        .setCancelable(true)
                        .setNegativeButton("No, Input Manually", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                c = Calendar.getInstance();
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                int month = c.get(Calendar.MONTH);
                                int year = c.get(Calendar.YEAR);

                                dpd = new DatePickerDialog(Settings.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay){
                                        enlistmentdate = mDay + "/" + (mMonth+1) + "/" + mYear;
                                        settings_ord_date_textview.setText(enlistmentdate);
                                    }
                                }, year, month, day);
                                dpd.show();

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(settings_enlistment_date_textview.getText().toString().equals("DD/MM/YYYY"))
                                {
                                    MDToast mdToast = MDToast.makeText(context, "Please set enlistment date first!", duration, MDToast.TYPE_ERROR);
                                    mdToast.show();
                                    dialog.dismiss();
                                }
                                else
                                {
                                    date.enlistmentdate = settings_enlistment_date_textview.getText().toString();
                                    if(servicedur_textview.getText().equals("2 Years")){
                                        servicedurationint = 730;
                                    }
                                    else if(servicedur_textview.getText().equals("1 Year 10 Months")){
                                        servicedurationint = 669;
                                    }
                                    date.serviceduration = servicedurationint;
                                    settings_ord_date_textview.setText(date.calculateorddate());
                                    MDToast mdToast = MDToast.makeText(context, "ORD Date successfully calculated!", duration, MDToast.TYPE_SUCCESS);
                                    mdToast.show();
                                    dialog.dismiss();
                                }

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

            }
        });

        //Open Calendar Dialog when user presses the Enlistment Date Edit Button and show selected date on ord_date_edittext
        setenlistmentdatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Settings.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay){
                        settings_enlistment_date_textview.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finish(); //ensures that the current activity is destroyed
    }


}
