package com.example.imtpmd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModuleActivity extends AppCompatActivity {
    private Button joinModule;
    String mnaam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        TextView moduleNaam = (TextView) findViewById(R.id.tvmodule);
        TextView moduleBeschrijving = (TextView) findViewById(R.id.tvbeschrijving);
        TextView moduleStatus = (TextView) findViewById(R.id.tvstatus);

        Intent intent = getIntent();
        String text = intent.getStringExtra(ModulesFragment.EXTRA_TEXT);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mnaam = extras.getString("moduleNaam");
            moduleNaam.setText(mnaam);
            getSupportActionBar().setTitle("Keuzevak: "+mnaam);
        }








        joinModule = (Button)findViewById(R.id.btnjoin);
        joinModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schrijfIn();
            }
        });

    }


    private void schrijfIn(){
        TextView moduleStatus = (TextView) findViewById(R.id.tvstatus);

        moduleStatus.setText("In afwachting "+mnaam );

        joinModule.setEnabled(false);

        if(new publiek().internetIsConnected()){
            write2API();
        }
        else{
            Toast.makeText(getApplicationContext(), "404 NO INTERNET, try again later", Toast.LENGTH_LONG).show();


        }
    };


    private void checkIngeschreven(){
        
    }

    private void write2API(){

    }


}