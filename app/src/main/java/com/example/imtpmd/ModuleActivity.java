package com.example.imtpmd;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ModuleActivity extends AppCompatActivity {
    private Button joinModule;

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
            String mnaam = extras.getString("moduleNaam");
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

       moduleStatus.setText("In afwachting");
        joinModule.setEnabled(false);
    };


    private void checkIngeschreven(){
        
    }



}