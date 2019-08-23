package com.example.imtpmd;

import android.app.Service;
import android.content.Intent;

import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.io.InputStreamReader;
import java.util.ArrayList;

//Hier word het inloggen geregeld, ook staat er een gecommente functie van wat de nachtmodus had moeten regelen

public class LoginActivity extends AppCompatActivity /*implements SensorEventListener*/ {
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
    public String LOCAL_PASSWORDS_FILE = "local_passwords_file.txt";
    public String ACC_FILE = "acc_file.txt";
    ArrayList<String> localSavedUsers = new ArrayList<>();
    ArrayList<String> localSavedPasswords = new ArrayList<>();



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
//        if(isDonker()){
//            modus("donker");
//        }
//        else{
//            modus("licht");
//        }
    }

    public void validate_userLocalOrAPI(final String user, final String password){
        //Deze kijkt wat er moet gebeuren zonder internet
        if(p.internetIsConnected()==false ){
           if( isDanWelLokaal("user") && isDanWelLokaal("password")) {
               magDoor(true);
           }
           else{
               magDoor(false);
           }
           return;
        }
        //Als het hier verder gaat hebben ze dus wel internet en word de informatie opgehaald uit de API

        String url = "http://api.mrtvda.nl/api/gebruikers";
        Log.d("API", "jsonParser: aangeroepen2");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;

                try {
                    jsonArray = response.getJSONArray("gebruikers");

                    boolean klopt = false;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject gebruikers = jsonArray.getJSONObject(i);

                        if(localSavedUsers.contains(gebruikers.getString("email"))==false){

                            localSavedUsers.add(gebruikers.getString("email"));
                            localSavedPasswords.add("#"+gebruikers.getString("password")+"*"+gebruikers.getString("email")+"#");
                            //Hier worden alle ingeladen gebruikers en wachtwoorden lokaal opgeslagen
                        }

                        if(user.toLowerCase().equals(gebruikers.getString("email").toLowerCase()) && password.equals(gebruikers.getString("password")) ){
                            klopt = true;
                        }
                    }
                    if(p.internetIsConnected()){
                        save2file(localSavedUsers.toString());
                        save2file(localSavedPasswords.toString());
                    }
                    if(klopt==true){
                        magDoor(true);
                    }
                    if(klopt==false) {
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
        //Deze functie krijgt een true of false mee(of de login goed was) en zorgt dan voor de gevolgen
        if(magDoor){

            //      if ((userName.equals("")) && (userPassword.equals(""))) {
            save2file(Name.getText().toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{


            counter--;
            Info.setText("No of attempts remaining: " + String.valueOf(counter));
            if (counter <= 0)
                Login.setEnabled(false);

        }
    }




//    private void modus(String mode){
//        Log.d("donker", "modus: aangeroepen in login "+mode);
//        //mode = "licht";
//        if(mode == "donker"){
//            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimaryDark);
//            Info.setTextColor(Color.WHITE);
//        }
//        else{
//            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimary);
//        }
//    }

    public void save2file(String wat){

        String gekozen_file ="";
        if(wat.equals(localSavedUsers.toString())){

            gekozen_file = LOCAL_USERS_FILE;
            if(p.internetIsConnected()==false){
            //    Toast.makeText(this,"saved to "+getFilesDir()+gekozen_file,Toast.LENGTH_LONG).show();
                return;
            }
        }
         else if(wat.equals(localSavedPasswords.toString())){
            if(p.internetIsConnected()==false){
                //    Toast.makeText(this,"saved to "+getFilesDir()+gekozen_file,Toast.LENGTH_LONG).show();
                return;
            }

            gekozen_file = LOCAL_PASSWORDS_FILE;
        }
        else if(wat.equals(Name.getText().toString())){

            gekozen_file = ACC_FILE;
        }

       //String text= mEditText.getText().toString();
        String text = Name.getText().toString().toLowerCase();

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

 public boolean isDanWelLokaal(String watLokaal) {

     File file = new File(getApplicationContext().getFilesDir(), LOCAL_USERS_FILE);
     if (!file.exists()) {
         Toast.makeText(this,"Geen internet of Eerdere opslag gevonden",Toast.LENGTH_LONG).show();
         return false;
     }
     FileInputStream fis = null;
     String gekozenFile="";
     String poginNaam = "";
     if(watLokaal.equals("user")){
         gekozenFile = LOCAL_USERS_FILE;
         poginNaam = Name.getText().toString();
     }
     else if(watLokaal.equals("password")){
         gekozenFile=LOCAL_PASSWORDS_FILE;
         poginNaam = "#"+Password.getText().toString() + "*" + Name.getText() +"#";
     }

     try {
         fis = openFileInput(gekozenFile);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         StringBuilder sb = new StringBuilder();
         String text;


         while ((text = br.readLine()) != null) {
             sb.append(text).append("\n");
         }

         String[] ar = sb.toString().substring(1, sb.length() - 2).split(", ", 99);

         for (int i = 0; i < ar.length; i++) {
             if (ar[i].toLowerCase().equals(poginNaam.toLowerCase())) {
                 return true;
             }

         }

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
//        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
//        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
//            Log.d("donke", "onSensorChanged: "+sensorEvent.values[0]+" / "+licht);
//            licht = sensorEvent.values[0];
//         //   mEditText.setText(""+licht);
//            checkLight();
//         //   isDonker();
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }

//    public boolean checkLight() {
//        Log.d("donker", "isDonker: aangeroepen in login, licht is "+licht);
//        int wanneerDonker = 5;
//        if(licht<wanneerDonker){
//            Log.d("donker", "isDonker: return true");
//            return true;
//        }
//        else{
//            Log.d("donker", "isDonker: return false ");
//            return false;
//        }
//    }

//    public boolean isDonker(){
//        boolean check = checkLight();
//        if (check) {
//            Log.d("isDonker", "true");
//            return true;
//        } else {
//            Log.d("isDonker", "false");
//            return false;
//        }
//    }


}

