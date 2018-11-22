package com.example.mefty.foodassistant;

import android.content.Intent;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    static Obj obj=new Obj();
    EditText e;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callFinalView(View view){

        e=(EditText)findViewById(R.id.nametxt);
        obj.name= e.getText().toString();
        e=(EditText)findViewById(R.id.emailtxt);
        obj.email=e.getText().toString();
        Intent intent = new Intent(this, FinalView.class);
        finish();
        startActivity(intent);

    }
    public void callCalendar(View view) {
        e=(EditText)findViewById(R.id.nametxt);
        obj.name= e.getText().toString();
        e=(EditText)findViewById(R.id.emailtxt);
        obj.email=e.getText().toString();
        Intent intent = new Intent(this, Calendar.class);
        finish();
        startActivity(intent);
    }
    public void exit(View view) {
        finish();
        System.exit(0);
    }
}