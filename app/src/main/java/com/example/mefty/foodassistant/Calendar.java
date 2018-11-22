package com.example.mefty.foodassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;

public class Calendar extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }



    CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener() {

        public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

            // add one because month starts at 0
            month = month + 1;
            // output to log cat **not sure how to format year to two places here**
            MainActivity.obj.dateS = year + "-" + month + "-" + day;
            Log.d("NEW_DATE", MainActivity.obj.dateS);
        }
    };
    public void callFinalView(View view) {


        CalendarView myCalendar = (CalendarView) findViewById(R.id.calendarView2);
        myCalendar.setOnDateChangeListener(myCalendarListener);
        Intent intent = new Intent(this, FinalView.class);
        finish();
        startActivity(intent);
    }

    public void exit(View view) {
        finish();
        System.exit(0);
    }
}
