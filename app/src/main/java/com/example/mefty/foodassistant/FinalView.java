package com.example.mefty.foodassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.example.mefty.foodassistant.R.layout.activity_calendar;

public class FinalView extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_view);
    }
    public void callGorgias(View view) {
        //TODO;
    }
    public void exit(View view) {
        finish();
        System.exit(0);
    }
}
