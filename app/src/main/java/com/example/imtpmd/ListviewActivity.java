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
    String user;

    private RequestQueue mQueue;
    publiek p = new publiek();

    Bundle extras = new Bundle();

    String ALL_MODULE_FILE ="all_module_file.txt";
    String MY_MODULE_FILE ="_module_file.txt";

    String ALL_API_url = "http://api.mrtvda.nl/api/keuzevakken";
    String MY_API_url =  "http://api.mrtvda.nl/api/inschrijvingen/insblau@insblau.nl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LISTVIEW", "onCreate aangeroepen");
        mQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview);
        mQueue = Volley.newRequestQueue(this);

        Bundle extras = getIntent().getExtras();
        wlijst = extras.getString("welkeLijst");
        extras.putBoolean("geupdate",false);


        Log.d("vader", "onCreate:appelpen = "+MY_MODULE_FILE);
        donkermodus();

        load("acc_file.txt"); //User word ff ingeladen
        Log.d("vader", "onCreate:user= "+user);
        Log.d("vader", "onCreate:file="+MY_MODULE_FILE);
        Log.d("appelpen",  user.toString()+"x"+MY_MODULE_FILE.toString());

        MY_MODULE_FILE = user+MY_MODULE_FILE;
        Log.d("appelpen", MY_MODULE_FILE);

        //windowArray.add("eerste add in listviewac");
        //windowArray.add("DEZE IS NIET GESCHREIBEN");

        if(wlijst.equals("allesLijst")){
           // FILE_NAME = "alle_modules.txt";
         //   windowArray.add("ALLES LIJST ADD IS NOG GELUKT NU OOK OP 13");

            if(p.internetIsConnected()){
                getAllModulesAPI(ALL_API_url);
                getSupportActionBar().setTitle("Alle modules");

            }
            else{
                Log.d("API", "geen internet in alllijst = "+ALL_MODULE_FILE);
                load(ALL_MODULE_FILE);
                opzet();
                getSupportActionBar().setTitle("(Geen Internet) Alle modules");
            }

        }
        else if(wlijst.equals("mijnLijst")){
            if(p.internetIsConnected()){
                getMyModulesAPI(user);
                getSupportActionBar().setTitle("Uw modules");

            }
            else{
                Log.d("API", "geen internet in mijnlijst = "+MY_MODULE_FILE);
                load(MY_MODULE_FILE);
                opzet();
                getSupportActionBar().setTitle("(Geen Internet) Uw modules");
            }
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
        if(wlijst.equals("allesLijst")){
             file = ALL_MODULE_FILE;
            Log.d("save", "wLIJST IS ALLESLIJST");
        }
        else{
            Log.d("ja", wlijst+" file is mijnfile in de else = "+MY_MODULE_FILE);
             file = MY_MODULE_FILE;
        }

        String text = windowArray.toString();
       // Log.d("save", "!IMPORTANT SAVE "+text);
        FileOutputStream fos = null;
        try{
            if(p.internetIsConnected()) {
                fos = openFileOutput(file, MODE_PRIVATE);
                fos.write(text.getBytes());
                extras.putBoolean("geupdate", true);
                Toast.makeText(this, "saved to " + getFilesDir() + file, Toast.LENGTH_LONG).show();
            }
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
        Log.d("vaderload", "load: aangeroepen");
        Boolean isName=false;
        if(CHOSEN_FILE.equals("acc_file.txt")){
            isName=true;
        }

        File file = new File(getApplicationContext().getFilesDir(),CHOSEN_FILE);
        if (!file.exists()) {
            Log.d("vaderload", "file bestaat niet: "+CHOSEN_FILE);
            return;
        }
        FileInputStream fis = null;
        try {
            fis = openFileInput(CHOSEN_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            Log.d("vaderload", "WHILE  DOES WELL EXIST");
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }


            if(isName){
                windowArray.add(sb.toString());
                extras.putString("user", sb.toString());
                //Log.d("tussenvadertext", "this.user word nu: "+sb.toString().toLowerCase());
                this.user = sb.toString().toLowerCase();
                Log.d("vaderload", "load: "+sb.toString());
            }
            else {
                String[] ar = sb.toString().substring(1, sb.length() - 2).split(", ", 99);
                for (int i = 0; i < ar.length; i++) {
                    Log.d("vaderload", "ADD LOAD " + ar[i]);
                    windowArray.add(ar[i]);

                }

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

    public void getMyModulesAPI(String user) {
        if(true){
            return;
            //##
        }
        //Deze functie werkt niet omdat ik niet weet hoe het eruit zou zien

        Log.d("myapi", "getMyModulesAPI: met user "+user);
        String url = "http://api.mrtvda.nl/api/inschrijvingen/"+user;
        Log.d("myapi", "getMy module API");
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("myapi", "yas");
                JSONArray jsonArray = null;

                try {
                    Log.d("myapi", "onResponse: wait1s");
                    jsonArray = response.names();


                    Log.d("myapi", "onResponse: "+jsonArray);


                    windowArray.clear();
                    for (int i = 0; i < jsonArray.length(); i++) { //for keuzevak bij user
                        JSONObject keuzevakken = jsonArray.getJSONObject(i);

                      //  String keuzevak = //;

                        //windowArray.add(keuzevak);
                      //  Log.d("myapi", "jsonParser: jajaja "+keuzevak);
                        //    Log.d("API", "jsonParser: nenee "+windowArray);
                    }
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    opzet();

                    Log.d("myapi", "wat: ");
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapi", "Er is iets fout gegaan" +error);
                error.printStackTrace();
            }
        });

        mQueue.add(request2);

    }
    public void getAllModulesAPI(String url) {
      //  String url = "http://api.mrtvda.nl/api/inschrijvingen/insblau@insblau.nl";
        Log.d("API", "jsonParser: GET ALL MODULES API AANGEROEPEN");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;

                try {
                    Log.d("APIjson", "onResponse: wait1s");
                    jsonArray = response.getJSONArray("Keuzevakken");

                    Log.d("APIjson", "onResponse: "+jsonArray);
                    

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

        if(windowArray.size()<1  ){
            Log.d("opzet", "opzet: IS EMPTYH");
            return;
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_textview,windowArray);
        ListView listView = (ListView)findViewById(R.id.window_list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Log.d("YAS", "je hebt geklukt op: ");
                Toast.makeText(ListviewActivity.this, windowArray.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListviewActivity.this, ModuleActivity.class);
                extras.putString("moduleNaam", windowArray.get(position));



                intent.putExtras(extras);

                startActivity(intent);
                // kijkt in arraylist welke plek word bedoeld
            }
        });
    }


}