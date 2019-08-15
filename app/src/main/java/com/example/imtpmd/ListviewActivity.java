package com.example.imtpmd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LISTVIEW", "onCreate aangeroepen");
        mQueue = Volley.newRequestQueue(this);


        super.onCreate(savedInstanceState);

        Log.d("API", "jsonParser: klaar==== "+windowArray);

        setContentView(R.layout.activity_listview);

        Log.d("LISTVIEW", "onCreate: is voorbij setcomntent");

        Bundle extras = getIntent().getExtras();
        wlijst = extras.getString("welkeLijst");
        Log.d("e","voor if donkere lucht");
         donkermodus();




        Log.d("commie","$$"+wlijst);

        windowArray.add("eerste add in listviewac");
        windowArray.add("tweede in listviewac");
        Log.d("commie","tothier$$");
        if(wlijst.equals("allesLijst")){
           // FILE_NAME = "alle_modules.txt";
            windowArray.add("ALLES LIJST ADD IS NOG GELUKT NU OOK OP 13");
            jsonParser();
            getSupportActionBar().setTitle("Alle mogelijke modules");
        }
        else if(wlijst.equals("mijnLijst")){
          //  FILE_NAME = "mijn_modules.txt";
            windowArray.add("MIJN LIJST ADD");
            opzet();
            getSupportActionBar().setTitle("Uw persoonlijke modules");
        }
        else{
            Log.d("commie","GEEN GELDIGE LIJST");
            getSupportActionBar().setTitle("404 geen geldige lijst modules");
        }

        load();






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
    private void donkermodus(){
        LoginActivity login = new LoginActivity();
        if( login.isDonker()){
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
            layout.setBackgroundResource(R.drawable.luchtnacht);
        }
    }

    public void jsonParser() {
        Log.d("API", "jsonParser: aangeroepen");
        String url = "http://api.mrtvda.nl/api/keuzevakken";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("Keuzevakken");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject keuzevakken = jsonArray.getJSONObject(i);

                        String keuzevak = keuzevakken.getString("keuzevak");

                        windowArray.add(keuzevak);
                        Log.d("API", "jsonParser: jajaja "+keuzevak);
                    //    Log.d("API", "jsonParser: nenee "+windowArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    opzet();
                    Log.d("API", "wat: ");
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Er is iets fout gegaan");
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }
    public void opzet(){
        Log.d("API", "test: HET REICH ZLA LIJDEN DOT DHET VAN TIJD "+windowArray);

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
}