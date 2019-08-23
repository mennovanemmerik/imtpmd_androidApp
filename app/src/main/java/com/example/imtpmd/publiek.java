package com.example.imtpmd;


import android.hardware.Sensor;

import android.hardware.SensorManager;

import android.support.v7.app.AppCompatActivity;


public class publiek extends AppCompatActivity  {

    //Deze file was origineel bedoeld om functies die vaak herhaald werden in te stoppen zodat ze konden worden aangeroepen vanuit andere files
    //Dit is echter alleen internetIsConnected geworden vanwege de kleine verschillen die andere functies hadden in andere files

    SensorManager sensorManager;
    Sensor sensor;


    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";

            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }



}
