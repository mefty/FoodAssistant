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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class FinalView extends AppCompatActivity implements Runnable{
    boolean mail;
    String answers[];
    int counter;
    Set<String> answerSet;
    boolean gotOneAnswer=false;

    private String callGorgias(boolean mail) throws Exception {
        counter=0;
        Obj o=MainActivity.obj;
        if (o.fin==true) {
            Double m = Double.parseDouble(((EditText) findViewById(R.id.txtMoney)).getText().toString());
            int p = Integer.parseInt(((EditText) findViewById(R.id.txtPeople)).getText().toString());
            o.moneyPP=m/p;
        }

        o.meatAllergy=((CheckBox)findViewById(R.id.chkMeat)).isChecked();
        o.molluscsAllergy=((CheckBox)findViewById(R.id.chkMolluscs)).isChecked();
        o.wantMeat=((CheckBox)findViewById(R.id.chkWantMeat)).isChecked();
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
            json.put("fin", o.fin);
            json.put("wantMeat", o.wantMeat);
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
                CheckBox c;
                c=(CheckBox)findViewById(R.id.chkMolluscs);
                c.setEnabled(false);
                c=(CheckBox)findViewById(R.id.chkMeat);
                c.setEnabled(false);
            }
        });
        answer=answer.replaceAll("=","");
        String[] array=answer.split("You will cook: ");
        answers=new String[array.length-1];
        answerSet = new HashSet<String>();
        for(int i=1;i<array.length;i++) {
            answers[i - 1] = array[i];
            answerSet.add(array[i].split("\n",2)[0]);
        }
        if(answerSet.size()>1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    manyAnswers();
                }
            });
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setDeltaView(0);
                Button b;
                b=(Button)findViewById(R.id.btnEmail);
                b.setVisibility(View.VISIBLE);
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
        if(gotOneAnswer!=true)
            return;
        TextView t = (TextView) findViewById(R.id.txtDelta);
        if(++counter<answers.length)
            setDeltaView(counter);
        else{
            t.setText("No more answers.");
            counter=-1;
        }
    }
    public void feelLucky(View view){
        boolean meat=answerSet.contains("meat");
        boolean souvla=answerSet.contains("souvla");
        boolean legumes=answerSet.contains("legumes");
        boolean molluscs=answerSet.contains("molluscs");
        if((meat&&souvla) || (legumes&&molluscs)){
            if(Math.random()>0.5) {
                ((EditText) findViewById(R.id.txtMoney)).setText("999");
                ((EditText) findViewById(R.id.txtPeople)).setText("1");
            }else{
                ((EditText) findViewById(R.id.txtMoney)).setText("1");
                ((EditText) findViewById(R.id.txtPeople)).setText("1");
            }
        }
        if(answerSet.size()>2 || ((meat&&legumes) || (meat&&molluscs) || (souvla&&legumes) || (souvla&&molluscs))){
            if(Math.random()>0.5)
                ((CheckBox) findViewById(R.id.chkWantMeat)).setChecked(true);
            else
                ((CheckBox) findViewById(R.id.chkWantMeat)).setChecked(false);
        }
        MainActivity.obj.fin=true;
        Button b;
        b=(Button)findViewById(R.id.btnLucky);
        b.setVisibility(View.GONE);
        b=(Button)findViewById(R.id.btnMoney);
        b.setVisibility(View.GONE);
        b=(Button)findViewById(R.id.btnAnswer);
        b.setVisibility(View.GONE);
        writeAnswer(view);
    }
    public void giveDetails(View view){
        boolean meat=answerSet.contains("meat");
        boolean souvla=answerSet.contains("souvla");
        boolean legumes=answerSet.contains("legumes");
        boolean molluscs=answerSet.contains("molluscs");
        if((meat&&souvla) || (legumes&&molluscs)){
            makeVisible();
        }
        if(answerSet.size()>2 || ((meat&&legumes) || (meat&&molluscs) || (souvla&&legumes) || (souvla&&molluscs))){
            TextView t=(TextView)findViewById(R.id.lblWantMeat);
            CheckBox c=(CheckBox)findViewById(R.id.chkWantMeat);
            t.setVisibility(View.VISIBLE);
            c.setVisibility(View.VISIBLE);
        }
        MainActivity.obj.fin=true;
        Button b;
        b=(Button)findViewById(R.id.btnLucky);
        b.setVisibility(View.GONE);
        b=(Button)findViewById(R.id.btnMoney);
        b.setVisibility(View.GONE);
        b=(Button)findViewById(R.id.btnAnswer);
        b.setVisibility(View.VISIBLE);
    }
    public void sendMail(View view) {
        mail = true;
        Thread thread = new Thread(this);
        thread.start();
        toastHamCheese("E-mail sent.");
        Button b=(Button)findViewById(R.id.btnAnswer);
        b.setVisibility(View.GONE);
        b=(Button)findViewById(R.id.btnEmail);
        b.setVisibility(View.GONE);
        TextView t=(TextView)findViewById(R.id.txtDelta);
        t.setText("Thank you!");
    }
    public void writeAnswer(View view) {
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
        gotOneAnswer=true;
        if(MainActivity.obj.fin==true) {
            b = (Button) findViewById(R.id.btnAnswer);
            b.setVisibility(View.GONE);
            t=(TextView)findViewById(R.id.lblpeople);
            t.setVisibility(View.GONE);
            t=(TextView)findViewById(R.id.lblmoney);
            t.setVisibility(View.GONE);
            t=(TextView)findViewById(R.id.lblWantMeat);
            t.setVisibility(View.GONE);
            e=(EditText)findViewById(R.id.txtPeople);
            e.setVisibility(View.GONE);
            e=(EditText)findViewById(R.id.txtMoney);
            e.setVisibility(View.GONE);
            CheckBox chk=(CheckBox)findViewById(R.id.chkWantMeat);
            chk.setVisibility(View.GONE);
        }
    }
    public void exit(View view) {
         finish();
         System.exit(0);
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

        Button b=(Button) findViewById((R.id.btnAnswer));
        b.setText("What will I eat?");
        b=(Button) findViewById((R.id.btnEmail));
        b.setText("Send Email");
        TextView t = (TextView) findViewById(R.id.txtDelta);
        t.setText("Fill the text boxes");

    }
    public void manyAnswers(){
        TextView t = (TextView) findViewById(R.id.txtDelta);
        StringBuilder sb=new StringBuilder();
        sb.append("You have more than one answers:\n");
        for(String s:answerSet)
            sb.append(s+"\n");
        sb.append("Do you want to give more details in order to decide or you just feel lucky?");
        t.setText(sb.toString());

        //**Changes buttons.**//
        Button b;
        b=(Button) findViewById((R.id.btnAnswer));
        b.setVisibility(View.GONE);
        b=(Button) findViewById((R.id.btnEmail));
        b.setVisibility(View.GONE);
        b=(Button) findViewById((R.id.btnLucky));
        b.setVisibility(View.VISIBLE);
        b=(Button) findViewById((R.id.btnMoney));
        b.setVisibility(View.VISIBLE);
        gotOneAnswer=false;
        TextView p;
        p=(TextView)findViewById((R.id.lblmeat));
        p.setVisibility(View.GONE);
        p=(TextView)findViewById((R.id.lblmolluscs));
        p.setVisibility(View.GONE);
        CheckBox q=(CheckBox) findViewById((R.id.chkMeat));
        q.setVisibility(View.GONE);
        q=(CheckBox) findViewById((R.id.chkMolluscs));
        q.setVisibility(View.GONE);
    }
}
