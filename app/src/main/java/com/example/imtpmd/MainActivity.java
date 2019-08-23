package com.example.imtpmd;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    Bundle extras = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        donkermodus();
        getSupportActionBar().setTitle("Keuzevakken App");



    }
    public void naarAlles(View v) {
        Log.d("note", "naarAlles: aangeroep ");
        Intent intent = new Intent(MainActivity.this, ListviewActivity.class);

        extras.putString("welkeLijst", "allesLijst");
        intent.putExtras(extras);
        startActivity(intent);

    };
    public void naarMijn(View v) {
        Log.d("note", "naarmijn aangere: ");
        Intent intent = new Intent(MainActivity.this, ListviewActivity.class);

        extras.putString("welkeLijst", "mijnLijst");
        intent.putExtras(extras);
        startActivity(intent);
    }
    public void naarLogin(View v) {
        Log.d("note", "naarLogin aangeroepen");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
    }

//    private void donkermodus(){
//         LoginActivity login = new LoginActivity();
//         if( login.isDonker()){
//             LinearLayout layout = (LinearLayout) findViewById(R.id.background);
//             layout.setBackgroundResource(R.drawable.luchtnacht);
//         }
//    }

//#// hier kan API worden opgehaald en doorgepaast en pas worden weergeven in listviewmocht het niet werken

}

