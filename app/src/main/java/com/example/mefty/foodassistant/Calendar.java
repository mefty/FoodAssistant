package com.example.mefty.foodassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Calendar extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }

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
