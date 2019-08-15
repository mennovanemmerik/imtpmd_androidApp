package com.example.imtpmd;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

public class publiek extends AppCompatActivity  {

    SensorManager sensorManager;
    Sensor sensor;
    private String wLijst = "standaart Wlijst";

    public String getWlijst(){
        return wLijst;
    }

    public void setWlijst(String newWlijst){
        this.wLijst = newWlijst;
        wLijst=newWlijst.toString();
        wLijst=newWlijst;
    }


    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";

            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }




}
