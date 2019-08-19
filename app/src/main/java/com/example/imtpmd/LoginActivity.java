package com.example.imtpmd;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {
    private EditText Name;
    publiek p =new publiek();
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    EditText mEditText;
    private RequestQueue mQueue;
    SensorManager sensorManager;
    Sensor sensor;
    public float licht;
    public String LOCAL_USERS_FILE = "local_users_file.txt";
    private String ACC_FILE = "acc_file.txt";
    ArrayList<String> localSavedUsers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mQueue = Volley.newRequestQueue(this);
        Log.d("vader", "APP GESTART");
         sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
         sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_userLocalOrAPI(Name.getText().toString(),Password.getText().toString());
            }
        });
        if(isDonker()){
            modus("donker");
        }
        else{
            modus("licht");
        }
    }

    public void validate_userLocalOrAPI(final String user, final String password){
        if(p.internetIsConnected()==false ){
           magDoor(isUserDanWelLokaal());

           return;
        }

        String url = "http://api.mrtvda.nl/api/gebruikers";
        Log.d("API", "jsonParser: aangeroepen2");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;

                try {
                    Log.d("yas", "onResponse: wait1s");
                    jsonArray = response.getJSONArray("gebruikers");

                    boolean klopt = false;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject gebruikers = jsonArray.getJSONObject(i);
                        Log.d("yas", user+" vs "+gebruikers.getString("email"));
                        if(localSavedUsers.contains(gebruikers.getString("email"))==false){
                            Log.d("yas", "add "+gebruikers.getString("name"));
                            localSavedUsers.add(gebruikers.getString("email"));
                        }

                        if(user.toLowerCase().equals(gebruikers.getString("email").toLowerCase()) ){
                            klopt = true;
                        }
                    }
                    if(klopt==true){
                        save2file(localSavedUsers.toString());
                        magDoor(true);
                    }
                    if(klopt==false) {
                        save2file(localSavedUsers.toString());
                        magDoor(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    return;
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Er is iets fout gegaan");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void magDoor(boolean magDoor){
        if(magDoor){
            Log.d("logAPI", "onResponse: TRUE IS IN API ");
            //      if ((userName.equals("")) && (userPassword.equals(""))) {
            save2file(Name.getText().toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{

            Log.d("logAPI", "onResponse: FALSE IS NOTNOT IN API ");
            counter--;
            Info.setText("No of attempts remaining " + String.valueOf(counter));
            if (counter <= 0)
                Login.setEnabled(false);
            //    Log.d("API", "jsonParser: nenee "+windowArray);
        }
    }




    private void modus(String mode){
        mode = "licht";
        if(mode == "donker"){
            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimaryDark);
            Info.setTextColor(Color.WHITE);
        }
        else{
            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimary);
        }
    }

    public void save2file(String wat){

        Log.d("yas", "save2file: aangeroepen met "+wat);
        String gekozen_file ="lol niks";
        if(wat.equals(localSavedUsers.toString())){
            Log.d("vaderlog", "wat in inlog = NIET NAAM MAAR: "+wat);
            gekozen_file = LOCAL_USERS_FILE;
            if(p.internetIsConnected()==false){
            //    Toast.makeText(this,"saved to "+getFilesDir()+gekozen_file,Toast.LENGTH_LONG).show();
                return;
            }
        }
        else if(wat.equals(Name.getText().toString())){
            Log.d("vaderlog", "wat in inlog = confirmed naam: "+wat+" of ");
            gekozen_file = ACC_FILE;
        };
        Log.d("yas", "save2file: file = "+gekozen_file);
       //String text= mEditText.getText().toString();
        String text = Name.getText().toString().toLowerCase();
        Log.d("yas", "save: "+text);
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(gekozen_file,MODE_PRIVATE);
        //    fos.write(text.getBytes());
            fos.write(wat.toString().getBytes());
      //      mEditText.getText().clear();
          //  Toast.makeText(this,"saved to "+getFilesDir()+gekozen_file,Toast.LENGTH_LONG).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

 public boolean isUserDanWelLokaal() {
     Log.d("YAS", "isUserDanWelLokaal: aangeroepen");
     File file = new File(getApplicationContext().getFilesDir(), LOCAL_USERS_FILE);
     if (!file.exists()) {
         Toast.makeText(this,"Geen internet of Eerdere opslag gevonden",Toast.LENGTH_LONG).show();
         return false;
     }
     FileInputStream fis = null;

     try {
         fis = openFileInput(LOCAL_USERS_FILE);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         StringBuilder sb = new StringBuilder();
         String text;
         String poginNaam = Name.getText().toString();
         while ((text = br.readLine()) != null) {
             sb.append(text).append("\n");
         }

         String[] ar = sb.toString().substring(1, sb.length() - 2).split(", ", 99);
         Log.d("YAS", "forloop isuserdanwel lokaal begint met "+ar +" met "+ +ar.length);
         for (int i = 0; i < ar.length; i++) {
             Log.d("YAS", "ADD LOAD " + ar[i].toString().toLowerCase() + " vs " + poginNaam.toLowerCase());

             if (ar[i].toLowerCase().equals(poginNaam.toLowerCase())) {
                 Log.d("YAS", "return TRUE");
                 return true;
             }

         }
         Log.d("YAS", "return FALSE");
         return false;
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         try {
             fis.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
return false;
 }


    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){

            licht = sensorEvent.values[0];
         //   mEditText.setText(""+licht);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public boolean isDonker(){
        int wanneerDonker = 3;
        if(licht<wanneerDonker){
            return true;
        }
        else{
            return false;
        }
    }


}

