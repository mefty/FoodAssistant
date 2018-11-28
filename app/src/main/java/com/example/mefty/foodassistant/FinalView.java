package com.example.mefty.foodassistant;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FinalView extends AppCompatActivity implements Runnable{
    boolean mail;
    String answers[];
    int counter;
    boolean twoAnswers=false;
    boolean money=false;

    private String callGorgias(boolean mail) throws Exception {
        counter=0;
        Obj o=MainActivity.obj;
        if (findViewById(R.id.lblpeople).getVisibility()==View.VISIBLE) {
            Double m = Double.parseDouble(((EditText) findViewById(R.id.txtMoney)).getText().toString());
            int p = Integer.parseInt(((EditText) findViewById(R.id.txtPeople)).getText().toString());
            o.moneyPP=m/p;
        }else
            o.moneyPP=0.0;

        o.meatAllergy=((CheckBox)findViewById(R.id.chkMeat)).isChecked();
        o.molluscsAllergy=((CheckBox)findViewById(R.id.chkMolluscs)).isChecked();
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        String hostname="DESKTOP-GL6HQR2";
        //String hostname="10.16.22.16";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url;
            if(mail==true)
                url = new URL("http://"+hostname+"/gorgias/GorgiasAPI.php?action=executeJava&mailing=mail");
            else
                url = new URL("http://"+hostname+"/gorgias/GorgiasAPI.php?action=executeJava&mailing=none");

            JSONObject json = new JSONObject();
            json.put("name", o.name);
            json.put("email", o.email);
            json.put("moneyPP",o.moneyPP);
            json.put("meatAllergy", o.meatAllergy);
            json.put("molluscsAllergy", o.molluscsAllergy);
            json.put("day", o.day);
            json.put("month", o.month);
            json.put("year", o.year);
            json.put("dateName", o.dateName);
            String message = json.toString();

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.connect();

            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            os.flush();

            is = conn.getInputStream();

            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String readLine;
            while (((readLine = br.readLine()) != null))
                sb.append(readLine);

            os.close();
            is.close();
            conn.disconnect();

            return sb.toString().replace('#','\n').replace('|','=');
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void run() {
        String firstFood=null;
        String answer;
        try {
            answer = callGorgias(mail);
        }catch(Exception e){
            System.err.println("Connection error: " + e.getMessage());
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  toastHamCheese("Connection error.");
              }
            });
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toastHamCheese("Got answer.");
            }
        });
        answer=answer.replaceAll("=","");
        String[] array=answer.split("You will cook: ");
        answers=new String[array.length-1];
        for(int i=1;i<array.length;i++) {
            answers[i - 1] = array[i];
            String food=array[i].split("\n",2)[0];
            if(firstFood==null)
                firstFood=food;
            else if(firstFood.compareTo(food)!=0) {
                final String finalFirstFood = firstFood;
                final String finalFood= food;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        twoAnswers(finalFirstFood, finalFood);
                    }
                });
                return;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setDeltaView(0);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_view);
    }
    public void setDeltaView(int i){
        TextView t = (TextView) findViewById(R.id.txtDelta);
        t.setText((i+1)+"/"+answers.length+"\nYou will cook: "+answers[i]);
    }
    public void nextAnswer(View view) {
        TextView t = (TextView) findViewById(R.id.txtDelta);
        if(++counter<answers.length)
            setDeltaView(counter);
        else{
            t.setText("No more answers.");
            counter=-1;
        }
    }
    public void sendMail(View view) {
        if (twoAnswers && money==false)
            makeVisible(); //TODO choose the answer and display it and after ask for email
        else { //todo if money==true; call money.pl else gorgiasproject.pl
            EditText e;
            e = (EditText) findViewById(R.id.txtPeople);
            if ((e.getVisibility() == View.VISIBLE) && prevent()) {
                toastHamCheese("Fill the text fields");
                return;
            }
            mail = true;
            Thread thread = new Thread(this);
            thread.start();
            toastHamCheese("E-mail sent.");
        }
    }
    public void writeAnswer(View view) {
        //if (twoAnswers && money==false)
        //   chooseAnAnswer(); //TODO choose the answer and display it and after ask for email
        //else{
        EditText e = (EditText) findViewById(R.id.txtPeople);
        Button b=(Button)findViewById(R.id.btnEmail);
        b.setVisibility(View.VISIBLE);
        TextView t=(TextView)findViewById(R.id.txtDelta);
        t.setVisibility(View.VISIBLE);
        if ((e.getVisibility() == View.VISIBLE) && prevent()) {
            toastHamCheese("Fill the text fields.");
            return;
        }
        mail = false;
        Thread thread = new Thread(this);
        thread.start();
    //}
    }
    public void exit(View view) {
        twoAnswers("mef","meft");
       //  finish();
        // System.exit(0);
    }

    public boolean prevent(){
        EditText e=(EditText)findViewById(R.id.txtMoney);
        if (e.getText().toString().isEmpty())
            return true;
        e=(EditText)findViewById(R.id.txtPeople);
        if (e.getText().toString().isEmpty())
            return true ;
        return false;
    }
    public void toastHamCheese(String s){
        Toast toast= Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        toast.show();
    }
    public void makeVisible(){

        EditText e=(EditText)findViewById(R.id.txtMoney);
        e.setVisibility(View.VISIBLE);
        e=(EditText)findViewById(R.id.txtPeople);
        e.setVisibility(View.VISIBLE);
        TextView p=(TextView)findViewById(R.id.lblpeople);
        p.setVisibility(View.VISIBLE);
        p=(TextView)findViewById((R.id.lblmoney));
        p.setVisibility(View.VISIBLE);

        twoAnswers=false;
        money=true;
        Button b=(Button) findViewById((R.id.btnAnswer));
        b.setText("What will I eat?");
        b=(Button) findViewById((R.id.btnEmail));
        b.setText("Send Email");
        TextView t = (TextView) findViewById(R.id.txtDelta);
        t.setText("Fill the text boxes");

    }
    public void twoAnswers(String answer1,String answer2){
        TextView t = (TextView) findViewById(R.id.txtDelta);
        t.setText("You have 2 answers for this day.\nYou either cook "+answer1 +" or "+answer2+"\nDo you wanna choose an answer based on the money you want to spend or you just feel lucky?");
        Button b=(Button) findViewById((R.id.btnAnswer));
        b.setText("I am feeling lucky");
        b=(Button) findViewById((R.id.btnEmail));
        b.setText("money based");
        twoAnswers=true;
        TextView p;
        p=(TextView)findViewById((R.id.lblmeat));
        p.setVisibility(View.INVISIBLE);
        p=(TextView)findViewById((R.id.lblmolluscs));
        p.setVisibility(View.INVISIBLE);
        CheckBox q=(CheckBox) findViewById((R.id.chkMeat));
        q.setVisibility(View.INVISIBLE);
        q=(CheckBox) findViewById((R.id.chkMolluscs));
        q.setVisibility(View.INVISIBLE);
    }
}
