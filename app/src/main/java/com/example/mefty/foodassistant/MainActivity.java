package com.example.mefty.foodassistant;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static Obj obj=new Obj();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callCalendar(View view) {

        Intent intent = new Intent(this, Calendar.class);
        finish();
        startActivity(intent);
    }
    public void exit(View view) {
        finish();
        System.exit(0);
    }
}