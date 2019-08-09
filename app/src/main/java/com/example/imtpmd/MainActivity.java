package com.example.imtpmd;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Wat wilt u inzien?");

    }
    public void naarAlles(View v) {
        Log.d("note", "naarAlles: aangeroep ");
        Intent intent = new Intent(MainActivity.this, ListviewActivity.class);

        intent.putExtra("welkeLijst", "allesLijst");
        startActivity(intent);

    };
    public void naarMijn(View v) {
        Log.d("note", "naarmijn aangere: ");
        Intent intent = new Intent(MainActivity.this, ListviewActivity.class);

        intent.putExtra("welkeLijst", "mijnLijst");
        startActivity(intent);
    }

}

