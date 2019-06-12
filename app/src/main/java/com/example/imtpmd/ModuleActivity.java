package com.example.imtpmd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        Intent intent = getIntent();
        String text = intent.getStringExtra(ModulesFragment.EXTRA_TEXT);

        TextView textView1 = (TextView) findViewById(R.id.textview1);



       textView1.setText(text);


    }
}