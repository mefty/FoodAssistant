package com.example.mefty.foodassistant;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FinalView extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_view);
    }
    private String callGorgias(boolean mail) {
        Obj o=MainActivity.obj;
        Double m=Double.parseDouble(((EditText)findViewById(R.id.txtMoney)).getText().toString());
        int p=Integer.parseInt(((EditText)findViewById(R.id.txtPeople)).getText().toString());
        o.moneyPP=m/p;
        o.meatAllergy=((CheckBox)findViewById(R.id.chkMeat)).isChecked();
        o.molluscsAllergy=((CheckBox)findViewById(R.id.chkMolluscs)).isChecked();
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url;
            if(mail==true)
                url = new URL("http://DESKTOP-GL6HQR2/gorgias/GorgiasAPI.php?action=executeJava&mailing=mail");
            else
                url = new URL("http://DESKTOP-GL6HQR2/gorgias/GorgiasAPI.php?action=executeJava");

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

            return sb.toString();
        } catch (Exception e) {
            System.err.println("Connection error: " +e.getMessage());
        }
        return null;
    }
    public void sendMail(View view) {
        String answer=callGorgias(true);
    }
    public void writeAnswer(View view) {
        String answer=callGorgias(false);
        System.out.println(answer);
        //TODO write answer.
    }
    public void exit(View view) {
        finish();
        System.exit(0);
    }
}
