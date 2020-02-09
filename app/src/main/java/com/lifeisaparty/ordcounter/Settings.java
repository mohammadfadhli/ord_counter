package com.lifeisaparty.ordcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Calendar;

public class Settings extends AppCompatActivity {

    ImageButton savebutton;
    ImageButton setorddatebutton;
    TextView settings_ord_date_textview;
    EditText leave_quota_edittext;
    EditText off_quota_edittext;
    String orddate;
    String leavequota;
    String offquota;
    String enlistmentdate;
    int payday;
    Calendar c;
    DatePickerDialog dpd;
    RadioButton tenth_radiobutton;
    RadioButton twelve_radiobutton;
    ImageButton setenlistmentdatebutton;
    TextView settings_enlistment_date_textview;
    RadioButton twoyears_radiobutton;
    RadioButton oneyeartenmonths_radiobutton;
    String serviceduration;
    Button calculate_ord_date_button;
    int servicedurationint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Initialize all buttons/textviews/edittext here
        settings_ord_date_textview = findViewById(R.id.settings_ord_textview);
        leave_quota_edittext = findViewById(R.id.leave_quota_edittext);
        off_quota_edittext = findViewById(R.id.off_quota_edittext);
        savebutton = findViewById(R.id.savebutton);
        setorddatebutton = findViewById(R.id.set_ord_date_button);
        tenth_radiobutton = findViewById(R.id.tenth_radiobutton);
        twelve_radiobutton = findViewById(R.id.twelve_radiobutton);
        setenlistmentdatebutton = findViewById(R.id.set_enlistment_date_button);
        settings_enlistment_date_textview = findViewById(R.id.settings_enlistment_date_textview);
        twoyears_radiobutton = findViewById(R.id.twoyears_radiobutton);
        oneyeartenmonths_radiobutton = findViewById(R.id.oneyeartenmonths_radiobutton);
        calculate_ord_date_button = findViewById(R.id.calculate_ord_date_button);

        //Use sharedpreferences to save user data
        final SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        settings_ord_date_textview.setText(sharedPreferences.getString("orddate", ""));
        leave_quota_edittext.setText(sharedPreferences.getString("leavequota", "0"));
        off_quota_edittext.setText(sharedPreferences.getString("offquota", "0"));
        payday = sharedPreferences.getInt("payday", 0);
        settings_enlistment_date_textview.setText(sharedPreferences.getString("enlistmentdate", ""));
        serviceduration = sharedPreferences.getString("serviceduration", "");

        final Dates date = new Dates(); //date class
        final Regex regex = new Regex(); //regex class

        //For initial installion, there is no ord date/enlistment date
        if(sharedPreferences.getString("orddate", "").equals("")){
            settings_ord_date_textview.setText("DD/MM/YYYY");
        }

        if(sharedPreferences.getString("enlistmentdate", "").equals("")){
            settings_enlistment_date_textview.setText("DD/MM/YYYY");
        }


        //Check radio button according to user saved payday on SharedPreferences
        if(payday == 10){
            tenth_radiobutton.setChecked(true);
        }
        else if(payday == 12) {
            twelve_radiobutton.setChecked(true);
        }

        //Check radio button according to user saved service duration on SharedPreferences
        if(serviceduration.equals("2 YEARS")){
            twoyears_radiobutton.setChecked(true);
        }
        else if(serviceduration.equals("1 YEAR 10 MONTHS")){
            oneyeartenmonths_radiobutton.setChecked(true);
        }

        //Open Calendar Dialog when user presses the Calendar Edit button and show selected date on ord_date_edittext
        setorddatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        //Calculate ORD on button click & display ord date on ord_date_textview
        calculate_ord_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toastbox Variables
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                date.enlistmentdate = settings_enlistment_date_textview.getText().toString();
                if(twoyears_radiobutton.isChecked()){
                    servicedurationint = 730;
                }
                else if(oneyeartenmonths_radiobutton.isChecked()){
                    servicedurationint = 669;
                }
                date.serviceduration = servicedurationint;
                settings_ord_date_textview.setText(date.calculateorddate());
                MDToast mdToast = MDToast.makeText(context, "ORD Date successfully calculated!", duration, MDToast.TYPE_SUCCESS);
                mdToast.show();


            }
        });

        //Save settings on save button click & proceed to main activity
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orddate = settings_ord_date_textview.getText().toString();
                leavequota = leave_quota_edittext.getText().toString();
                offquota = off_quota_edittext.getText().toString();
                enlistmentdate = settings_enlistment_date_textview.getText().toString();

                //Regex to validate Leave and Off Quota
                regex.leavequota = leavequota;
                regex.offquota = offquota;

                //save payday according to user input radio
                if(tenth_radiobutton.isChecked()){
                    payday = 10;
                }
                else if(twelve_radiobutton.isChecked()){
                    payday = 12;
                }

                //save service duration according to user input radio
                if(twoyears_radiobutton.isChecked()){
                    serviceduration = "2 YEARS";
                }
                else if(oneyeartenmonths_radiobutton.isChecked()){
                    serviceduration = "1 YEAR 10 MONTHS";
                }


                //Toastbox Variables
                Context context = getApplicationContext();
                String text;
                int duration = Toast.LENGTH_SHORT;

                if(!regex.validateleavequota().equals("Passed.")) //if leave regex fails, show toast
                {
                    //text = regex.validateleavequota();
                    //Toast toast = Toast.makeText(context, text, duration);
                    //toast.show();
                    text = regex.validateleavequota();

                    MDToast mdToast = MDToast.makeText(context, text, duration, MDToast.TYPE_ERROR);
                    mdToast.show();

                }
                else if(!regex.validateoffquota().equals("Passed.")) //if leave regex fails, show toast
                {
                    text = regex.validateoffquota();

                    MDToast mdToast = MDToast.makeText(context, text, duration, MDToast.TYPE_ERROR);
                    mdToast.show();

                }
                else{ //else save the settings

                    editor.putString("orddate", orddate);
                    editor.putString("leavequota", leavequota);
                    editor.putString("offquota", offquota);
                    editor.putInt("payday", payday);
                    editor.putString("enlistmentdate", enlistmentdate);
                    editor.putString("serviceduration", serviceduration);

                    editor.commit();

                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    startActivity(intent);
                    finish(); //ensures that the current activity is destroyed

                    MDToast mdToast = MDToast.makeText(context, "Saved!", duration, MDToast.TYPE_SUCCESS);
                    mdToast.show();

                }
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finish(); //ensures that the current activity is destroyed
    }


}
