package com.example.imtpmd;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

        fm.replace(R.id.fragmentPlaceholder, new ModulesFragment());
        fm.commit();
    }
}

