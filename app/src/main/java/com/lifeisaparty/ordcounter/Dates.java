package com.lifeisaparty.ordcounter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Dates {

    String orddate;
    String currentdate;
    int payday = 10;
    int serviceduration;
    int daystillord;
    String enlistmentdate;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar cal = Calendar.getInstance();

    public String getcurrentdate(){ //get current date

        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()); //return current date in String format

    }

    public String daystillord(){ //get days till ord

        currentdate = getcurrentdate();

        try{

            Date dateAfter = dateFormat.parse(orddate);
            Date datebefore = dateFormat.parse(currentdate);

            long diff = dateAfter.getTime() - datebefore.getTime();
            return Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)); //return days till ord in String format

        }
        catch(Exception e) {
            return null;
        }

    }

    public String calculateorddate(){ //calculate ord date based on enlistment date and service duration

        Calendar cal = Calendar.getInstance();
        try{
            cal.setTime(dateFormat.parse(enlistmentdate));
        }
        catch(Exception e){

        }
        cal.add(Calendar.DAY_OF_MONTH, serviceduration);
        return dateFormat.format(cal.getTime());
    }


    public int percentageofdays(){ //get percentage between current date and service duration date

        daystillord = Integer.parseInt(daystillord());
        int numofdaysint = serviceduration - daystillord;

        int percentagevalue = (numofdaysint * 100)/serviceduration;

        if(percentagevalue > 100 || percentagevalue < 0){
            return 0;
        }
        else
        {
            return percentagevalue;
        }

    }

    public String workingdays(){ //get working days between current date and orddate

        int workdays = 0;
        currentdate = getcurrentdate();

        try{
            Date dateend = dateFormat.parse(orddate);
            Date datestart = dateFormat.parse(currentdate);

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(datestart);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(dateend);

            do{
                startCal.add(Calendar.DAY_OF_MONTH, 1);
                if(startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                    ++workdays;
                }
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

            return Integer.toString(workdays);

        } catch (Exception e){
            return null;
        }

    }

    public String daystillpayday(){ ///get days till payday

        String month;
        String year;

        try{
            Date currentdate = dateFormat.parse(getcurrentdate());
            cal.setTime(currentdate);

            int currentmonth = cal.get(Calendar.MONTH);
            int currentyear = cal.get(Calendar.YEAR);
            int currentday = cal.get(Calendar.DAY_OF_MONTH);

            if(currentyear == 11)
            {
                currentyear = currentyear + 1;
            }
            else if(currentday <= 10)
            {
                currentmonth = currentmonth + 1;
            }
            else{
                currentmonth = currentmonth + 2;
            }

            month = Integer.toString(currentmonth);
            year = Integer.toString(currentyear);

            String paydaydatestring = payday + "/" + month + "/" + year;;
            Date paydaydate = dateFormat.parse(paydaydatestring);
            long diff = paydaydate.getTime() - currentdate.getTime();
            return Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)); //return days to payday in String format
        }
        catch (Exception e){
            return null;
        }

    }

}
