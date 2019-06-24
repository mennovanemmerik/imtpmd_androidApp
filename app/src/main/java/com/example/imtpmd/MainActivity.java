package com.example.imtpmd;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = (TextView) findViewById(R.id.response);
        String url = "https://age-of-empires-2-api.herokuapp.com/api/v1/civilization/2" ;
        Context context = this.getApplicationContext();
        NetworkManager.getInstance(this).getRequest(url, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Civilization c = gson.fromJson(result,Civilization.class);
                Log.d("civ",c.getName());
                tv.setText(c.getName());
            }
        });

//        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
//
//        fm.replace(R.id.fragmentPlaceholder, new ModulesFragment());
//        fm.commit();
    }
}

