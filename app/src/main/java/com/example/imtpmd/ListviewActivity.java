package com.example.imtpmd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;


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



//        donkermodus();

        load("acc_file.txt"); //User word ff ingeladen


        MY_MODULE_FILE = user+MY_MODULE_FILE;
        //MY_MODULE_FILE word een combinatie van user en de rest van de filenaam, hierdoor krijg je voor elke user een andere lokale file waar zijn gekozen modules instaan


        if(wlijst.equals("allesLijst")){
            //wanneer de gebruiker alle modules wilt




            if(p.internetIsConnected()){
                getAllModulesAPI(ALL_API_url);
                getSupportActionBar().setTitle("Alle Keuzevakken");

            }
            else{
                //geen internet, haal de gevraagde items(alles hier) op uit load
                load(ALL_MODULE_FILE);
                opzet();
                getSupportActionBar().setTitle("(Geen Internet) Alle modules");
            }

        }
        else if(wlijst.equals("mijnLijst")){
            if(p.internetIsConnected()){
                getMyModulesAPI(user);
                getSupportActionBar().setTitle("Uw Keuzevakken");

            }
            else{

                load(MY_MODULE_FILE);
                opzet();
                getSupportActionBar().setTitle("(Geen Internet) Uw modules");
            }
        }
        else{

            getSupportActionBar().setTitle("404 geen geldige lijst modules");
        }
    }


    public void save(){
        //Deze save word aangeroepen na een succesvolle API call, hij slaat alle items uit windowArray(wat je ziet) op
        String file;
        if(wlijst.equals("allesLijst")){
             file = ALL_MODULE_FILE;
        }
        else{
             file = MY_MODULE_FILE;
        }

        String text = windowArray.toString();
        FileOutputStream fos = null;
        try{
            if(p.internetIsConnected()) {
                fos = openFileOutput(file, MODE_PRIVATE);
                fos.write(text.getBytes());
                extras.putBoolean("geupdate", true);
        //        Toast.makeText(this, "saved to " + getFilesDir() + file, Toast.LENGTH_LONG).show();
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
        //Mocht de API niet kunnen worden aangeroepen zal hier de lijst worden aangeroepen vanuit het lokale geheugen
        //dit kan zijn de lijst met alle modules, alleen gekozen modules of puur "acc_file.txt" waar alleen de ingelogde gebruiker in staat
        Boolean isName=false;
        if(CHOSEN_FILE.equals("acc_file.txt")){
            isName=true;
        }

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

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }


            if(isName){

                extras.putString("user", sb.toString());

                this.user = sb.toString().toLowerCase();

            }
            else {
                String[] ar = sb.toString().substring(1, sb.length() - 2).split(", ", 99);
                for (int i = 0; i < ar.length; i++) {

                    if(ar[i].length() >0) {
                        windowArray.add(ar[i]);
                    }


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
//    private void donkermodus(){
//        LoginActivity login = new LoginActivity();
//        if( login.isDonker()){
//            RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
//            layout.setBackgroundResource(R.drawable.luchtnacht);
//        }
//    }

    public void getMyModulesAPI(String user) {

        String url = "http://api.mrtvda.nl/api/inschrijvingen/" + user;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;

                try {

                    jsonArray = response.getJSONArray("inschrijvingen");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject keuzevak = jsonArray.getJSONObject(i);
                        String user = keuzevak.getString("keuzevak");
                        windowArray.add(user);
                    }
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();

                }finally {
                    opzet();


                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getMyModules", "Er is iets fout gegaan");
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }

    public void getAllModulesAPI(String url) {
      //  String url = "http://api.mrtvda.nl/api/inschrijvingen/insblau@insblau.nl";

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

                    }
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    opzet();


                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }

    public void opzet(){
        //Deze functie pakt de windowArray en zorgt dat je de items kan zien in de lijst

        if(windowArray.size()<1  ){
            return;
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_textview,windowArray);
        ListView listView = (ListView)findViewById(R.id.window_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                Intent intent = new Intent(ListviewActivity.this, ModuleActivity.class);
                extras.putString("moduleNaam", windowArray.get(position));
                //geeft de geklikte naam mee, deze word in moduleActivity opgehaald


                intent.putExtras(extras);

                startActivity(intent);
                // kijkt in arraylist welke plek word bedoeld
            }
        });
    }
}