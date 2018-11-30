package com.example.mefty.foodassistant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Obj {
    String name;
    String email;
    boolean meatAllergy;
    boolean molluscsAllergy;
    double moneyPP;
    int day;
    int month;
    int year;
    String dateName;
    boolean wantMeat;
    boolean fin;

    public Obj(){
        meatAllergy=false;
        molluscsAllergy =false;
        wantMeat=false;
        fin=false;
        moneyPP=0;
        Date d=new Date();
        day=d.getDate();
        month=d.getMonth()+1;
        year=d.getYear()+1900;
        dateName=new SimpleDateFormat("EEEE",Locale.getDefault()).format(d).toLowerCase();
    }
}
