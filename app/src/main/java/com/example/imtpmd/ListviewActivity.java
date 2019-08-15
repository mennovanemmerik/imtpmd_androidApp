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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListviewActivity extends AppCompatActivity {
    String FILE_NAME;
    ArrayList<String> windowArray = new ArrayList<>();
    String wlijst;
    String ALL_MODULE_FILE ="example.txt";
    String MY_MODULE_FILE ="my_module_file.txt";
    private RequestQueue mQueue;
    publiek p = new publiek();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LISTVIEW", "onCreate aangeroepen");
        mQueue = Volley.newRequestQueue(this);



        super.onCreate(savedInstanceState);

        Log.d("API", "jsonParser: klaar==== "+windowArray);

        setContentView(R.layout.activity_listview);
        mQueue = Volley.newRequestQueue(this);
        jsonParser();

        Log.d("LISTVIEW", "onCreate: is voorbij setcomntent");

        Bundle extras = getIntent().getExtras();
        wlijst = extras.getString("welkeLijst");
        Log.d("e","voor if donkere lucht");
         donkermodus();




        Log.d("commie","$$"+wlijst);

        windowArray.add("eerste add in listviewac");

        Log.d("commie","tothier$$");
        if(wlijst.equals("allesLijst")){
           // FILE_NAME = "alle_modules.txt";
         //   windowArray.add("ALLES LIJST ADD IS NOG GELUKT NU OOK OP 13");


            if(p.internetIsConnected()){
                jsonParser();
                getSupportActionBar().setTitle("Alle modules");

            }
            else{
                Log.d("API", "ELSE INTENET NIET GECONT");
                load(ALL_MODULE_FILE);
                opzet();
                getSupportActionBar().setTitle("(Geen Internet) Alle modules");
            }

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
    }


    public void save(){
        Log.d("API", "save: aangeroepen ");
        //String text= mEditText.getText().toString();
        String file;
        if(wlijst=="allesLijst"){
             file = ALL_MODULE_FILE;
            Log.d("save", "wLIJST IS ALLESLIJST");
        }
        else{
             file = MY_MODULE_FILE;
        }

        String text = windowArray.toString();
       // Log.d("save", "!IMPORTANT SAVE "+text);
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(file,MODE_PRIVATE);
            fos.write(text.getBytes());

            Toast.makeText(this,"saved to "+getFilesDir()+file,Toast.LENGTH_LONG).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }



    public void load(String CHOSEN_FILE){
        File file = new File(getApplicationContext().getFilesDir(),CHOSEN_FILE);
        if (!file.exists()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = openFileInput(CHOSEN_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            Log.d("API", "WHILE  DOES WELL EXIST");
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            String[] ar = sb.toString().substring(1,sb.length()-2).split(", ", 99);
            for(int i=0;i<ar.length;i++){
                Log.d("load", "ADD LOAD "+ar[i]);
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
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    opzet();

                    Log.d("jsonAPI", "wat: ");
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

    protected void jsonParser() {
        String url = "http://api.mrtvda.nl/api/keuzevakken";


    }
}