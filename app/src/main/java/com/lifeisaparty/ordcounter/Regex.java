package com.lifeisaparty.ordcounter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    String leavequota;
    String offquota;
    String c;

    public String validateleavequota(){ //Regex to validate Leave Quota Input. Only accepts 0-14 and 0.0/0.5

        Pattern p = Pattern.compile("^([0-9]|1[0-4])(?:\\.([0]|[5]))?$");
        Matcher m = p.matcher(leavequota);
        if(m.matches()){

            c = "Passed.";

        }
        else if(leavequota.equals("")){
            c = "Leave Quota cannot be empty!";
        }
        else{

            c = "Leave Quota must be between 0 to 14 and mutiples of 5!";
        }

        return c;
    }

    public String validateoffquota(){

        Pattern p = Pattern.compile("^([0-9]|1[0-9]|2[0-9])(?:\\.([0]|[5]))?$");
        Matcher m = p.matcher(offquota);
        if(m.matches()){

            c = "Passed.";

        }
        else if(offquota.equals("")){
            c = "Off Quota cannot be empty!";
        }
        else
        {
            c = "Off Quota must be between 0 to 30 and mutiples of 5!";
        }

        return c;
    }

}
