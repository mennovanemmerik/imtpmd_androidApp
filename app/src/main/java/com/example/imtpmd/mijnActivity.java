package com.example.imtpmd;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class mijnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d( "NEGER","NIEGER ");
        setContentView(R.layout.activity_main);

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        String mijnVakkenLijst[] = new String[4];

        mijnVakkenLijst[0]="d";
        mijnVakkenLijst[1]="u";
        mijnVakkenLijst[2]="i";
        mijnVakkenLijst[3]="tMIJN LAND DEUTCHSLAND";
        bundle.putStringArray("lijst", mijnVakkenLijst);
        ModulesFragment mtest = new ModulesFragment();
        fm.replace(R.id.fragmentPlaceholder, mtest);
        mtest.setArguments(bundle);

        fm.commit();
    }
}
