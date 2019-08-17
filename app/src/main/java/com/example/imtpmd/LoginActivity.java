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

    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    EditText mEditText;

    SensorManager sensorManager;
    Sensor sensor;
    public float licht;

    private String ACC_FILE = "acc_file.txt";
    ArrayList<String> arrai = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditText = findViewById(R.id.edit_text);



         sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
         sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });
        if(isDonker()){
            modus("donker");
        }
        else{
            modus("licht");
        }


    }
    private void validate(String userName, String userPassword) {
        /*    publiek p = new publiek();
            if(p.internetIsConnected()==false){
                return;
            }
*/
            if (true) {
      //      if ((userName.equals("")) && (userPassword.equals(""))) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                counter--;
                Info.setText("No of attempts remaining " + String.valueOf(counter));
                if (counter <= 0)
                    Login.setEnabled(false);
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

    public void save(View v){
       //String text= mEditText.getText().toString();
        String text = Name.getText().toString();
        Log.d("arden", "save: "+text);
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(ACC_FILE,MODE_PRIVATE);
            fos.write(text.getBytes());
            mEditText.getText().clear();
            Toast.makeText(this,"saved to "+getFilesDir()+ACC_FILE,Toast.LENGTH_LONG).show();
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
 public void load(View v) {

     File file = new File(getApplicationContext().getFilesDir(),ACC_FILE);
     if (!file.exists()) {
         return;
     }
     FileInputStream fis = null;

     try {
         fis = openFileInput(ACC_FILE);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         StringBuilder sb = new StringBuilder();
         String text;

         while ((text = br.readLine()) != null) {
             sb.append(text).append("\n");
         }
         Log.d("load", "NU MOET HET GLEOAD WORDNE");
         mEditText.setText(sb.toString());

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

