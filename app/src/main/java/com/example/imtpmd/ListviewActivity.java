package com.example.imtpmd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListviewActivity extends AppCompatActivity  {

    String[] windowArray = {"mijn liesdben","fucking","lieben"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        Bundle extras = getIntent().getExtras();
        String wlijst = extras.getString("welkeLijst");
        windowArray[2] = wlijst;





        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_textview,windowArray);
        ListView listView = (ListView)findViewById(R.id.window_list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Log.d("klikkie", "je hebt geklukt op: ");
                Toast.makeText(ListviewActivity.this, windowArray[position],Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListviewActivity.this, ModuleActivity.class);
                intent.putExtra("moduleNaam",windowArray[position]);
                startActivity(intent);
                // kijkt in arraylist welke plek word bedoeld
            }
        });


    }}