package com.example.imtpmd;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    String [] ray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d( "NEGER","NIEGER ");
        setContentView(R.layout.activity_main);

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        String lijst[] = new String[4];

        lijst[0]="d";
        lijst[1]="o";
        lijst[2]="c";
        lijst[3]="k";
        bundle.putStringArray("lijst", lijst);
        ModulesFragment mtest = new ModulesFragment();
        fm.replace(R.id.fragmentPlaceholder, mtest);
        mtest.setArguments(bundle);

        fm.commit();
    }
}

