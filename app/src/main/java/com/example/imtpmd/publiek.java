package com.example.imtpmd;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import java.io.IOException;
import java.io.InputStreamReader;

public class publiek extends AppCompatActivity  {

    SensorManager sensorManager;
    Sensor sensor;
    private String wLijst = "standaart Wlijst";
//     mQueue = Volley.newRequestQueue(this);
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