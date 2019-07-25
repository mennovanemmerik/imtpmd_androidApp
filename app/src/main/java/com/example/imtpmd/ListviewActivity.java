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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListviewActivity extends AppCompatActivity {
    String FILE_NAME;
    ArrayList<String> windowArray = new ArrayList<>();
    String wlijst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        Bundle extras = getIntent().getExtras();
        wlijst = extras.getString("welkeLijst");

        Log.d("commie","$$"+wlijst);

        windowArray.add("eerste add in listviewac");
        Log.d("commie","tothier$$");
        if(wlijst.equals("allesLijst")){
           // FILE_NAME = "alle_modules.txt";
            windowArray.add("ALLES LIJST ADD");
            getSupportActionBar().setTitle("Alle mogelijke modules");
        }
        else if(wlijst.equals("mijnLijst")){
          //  FILE_NAME = "mijn_modules.txt";
            windowArray.add("MIJN LIJST ADD");
            getSupportActionBar().setTitle("Uw persoonlijke modules");
        }
        else{
            Log.d("commie","GEEN GELDIGE LIJST");
            getSupportActionBar().setTitle("404 geen geldige lijst modules");
        }

        load();



        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_textview,windowArray);
        ListView listView = (ListView)findViewById(R.id.window_list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Log.d("klikkie", "je hebt geklukt op: ");
                Toast.makeText(ListviewActivity.this, windowArray.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListviewActivity.this, ModuleActivity.class);
                intent.putExtra("moduleNaam", windowArray.get(position));
                startActivity(intent);
                // kijkt in arraylist welke plek word bedoeld
            }
        });


    }

    public void load(){
        FileInputStream fis = null;
        try {
            fis = openFileInput("example.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            windowArray.add(sb.toString());
            String[] ar = sb.toString().substring(1,sb.length()-2).split(", ", 99);
            for(int i=0;i<ar.length;i++){
                windowArray.add(ar[i]);
            }


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                fis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}