package com.example.mefty.foodassistant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Obj {
    String name;
    String email;
    boolean meatAllergy;
    boolean moluscsallergy;
    double moneyPP;
    Date date;
    String dateS;//mak en pio efkolo gia to today
    public Obj(){
        meatAllergy=false;
        moluscsallergy=false;
        moneyPP=0;
        dateS = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
