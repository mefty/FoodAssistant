package com.example.mefty.foodassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calendar extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        CalendarView myCalendar = (CalendarView) findViewById(R.id.calendar);
        myCalendar.setOnDateChangeListener(myCalendarListener);
    }

    CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener() {
        public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
            Date d=new Date();
            d.setMonth(month);
            d.setYear(year);
            d.setDate(day-1);
            Obj o=MainActivity.obj;
            o.day=day;
            o.month=month+1;
            o.year=year;
            o.dateName= new SimpleDateFormat("EEEE",Locale.getDefault()).format(d).toLowerCase();
        }
    };
    public void callFinalView(View view) {
        Intent intent = new Intent(this, FinalView.class);
        finish();
        startActivity(intent);
    }

    public void exit(View view) {
        finish();
        System.exit(0);
    }
}
