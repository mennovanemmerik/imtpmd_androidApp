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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

//in deze file krijgt de gebruiker meer info te zien over een module


public class ModuleActivity extends AppCompatActivity {
    private Button schrijfButton;
    String mnaam;
    publiek p = new publiek();
    private RequestQueue mQueue;
    String MY_MODULE_FILE ="_module_file.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        Bundle extras = getIntent().getExtras();
        mQueue = Volley.newRequestQueue(this);

        TextView moduleNaam = (TextView) findViewById(R.id.tvmodule);
        //TextView moduleStatus = (TextView) findViewById(R.id.tvstatus);

        Intent intent = getIntent();
        String text = intent.getStringExtra(ModulesFragment.EXTRA_TEXT);

        final String user = extras.getString("user");
        MY_MODULE_FILE = user+MY_MODULE_FILE;


      //  boolean isLokaalIngeschrevenString = isLokaalIngeschreven(mnaam);

        schrijfButton = (Button)findViewById(R.id.btnjoin);
        TextView moduleBeschrijving = (TextView) findViewById(R.id.tvbeschrijving);

        if (extras != null) {

            mnaam = extras.getString("moduleNaam");
            moduleNaam.setText(mnaam);
            getSupportActionBar().setTitle("Keuzevak: "+mnaam);

            if(p.internetIsConnected()){
                fixBeschrijvingAPI(extras.getString("moduleNaam"));
            }
            else{
                loadLokaleBeschrijving(extras.getString("moduleNaam"));
            }
        }
        if(isLokaalIngeschreven(mnaam) == false){
            schrijfButton.setText("Schrijf in");
        }
        else {
            schrijfButton.setText("Schrijf uit");
        }

        if(extras.getBoolean("geupdate")){
        schrijfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModuleActivity.this, MainActivity.class);
                if(isLokaalIngeschreven(mnaam) == false){
                    //                    schrijfIn(mnaam);
                    write2API(mnaam,user);

                    schrijfButton.setText("Schrijf uit");
                }
                else {
                   // schrijfButton.setEnabled(false);
                 //   schrijfUit(mnaam);
                    removeFromAPI(mnaam,user);

                    schrijfButton.setText("Schrijf in");
                }
                startActivity(intent);
            }
        });}
        else{
            schrijfButton.setText("U heeft internet nodig om u in/uit te schrijven");
            schrijfButton.setEnabled(false);
        }



    }

    public void fixBeschrijvingAPI(String module) {
        String url = "http://api.mrtvda.nl/api/keuzevakken/"+module;
       //De beschrijving van een module word hier aangeroepen in de API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    String keuzevak = response.getString("description");

                    TextView moduleBeschrijving = (TextView) findViewById(R.id.tvbeschrijving);
                    moduleBeschrijving.setText(keuzevak);


                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                     Bundle extras = getIntent().getExtras();
                     TextView moduleBeschrijving = (TextView) findViewById(R.id.tvbeschrijving);
                     saveLokaalBeschrijving(extras.getString("moduleNaam"),moduleBeschrijving.getText().toString());
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

/*
    private void schrijfUit(String module){
        TextView moduleStatus = (TextView) findViewById(R.id.tvstatus);
        moduleStatus.setText("In afwachting "+mnaam );
        schrijfButton.setEnabled(false);
    }


    private void schrijfIn(String module){
        if(!new publiek().internetIsConnected()){
            Toast.makeText(getApplicationContext(), "404 NO INTERNET, try again later", Toast.LENGTH_LONG).show();
            return;
        }
        TextView moduleStatus = (TextView) findViewById(R.id.tvstatus);
        moduleStatus.setText("In afwachting "+mnaam );
        schrijfButton.setEnabled(false);
    };
*/
    public boolean isLokaalIngeschreven(String module){
       //Deze functie kijkt of je in je locale file(per user) de modulenaam voorkomt
        String CHOSEN_FILE = MY_MODULE_FILE;


        File file = new File(getApplicationContext().getFilesDir(),CHOSEN_FILE);
        if (!file.exists()) {
            return false;
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

            String[] ar = sb.toString().substring(1,sb.length()-2).split(", ", 99);
            for(int i=0;i<ar.length;i++){
                if(ar[i].toLowerCase().equals(module.toLowerCase())){
                    return true;
                }

            }
            return false;

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
        return false;
    }


    private void saveLokaalBeschrijving(String module, String newBeschrijving){
        String MODULE_FILE = "beschrijving_"+module+".txt";
        Log.d("modAPI", "saveLokaalBeschrijving: word nu aangeroepen met module "+module+" en beschrijving "+newBeschrijving);
        FileOutputStream fos = null;
        try{
            fos = openFileOutput(MODULE_FILE,MODE_PRIVATE);
            fos.write(newBeschrijving.getBytes());
       //     Toast.makeText(this,"saved to "+getFilesDir()+MODULE_FILE,Toast.LENGTH_LONG).show();
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

    private String loadLokaleBeschrijving(String module){
        Log.d("modAPI", "loadLokaleBeschrijving: word nu aangeroepen");
        String MODULE_FILE = "beschrijving_"+module+".txt";
        File file = new File(getApplicationContext().getFilesDir(),MODULE_FILE);
        if (!file.exists()) {
            Log.d("modAPI", "load geen file gevonden "+MODULE_FILE);
            return "Geen internet // Geen lokale opslag gevonden // F";
        }
        FileInputStream fis = null;

        try {
            fis = openFileInput(MODULE_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }


            TextView moduleBeschrijving = (TextView) findViewById(R.id.tvbeschrijving);
            Log.d("modAPI", "modulebeschrijving word inload nu aangepast naar : word nu aangeroepen naar "+sb.toString());
            moduleBeschrijving.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Er is iets fout gegaan lol //Misschien ff internet fixen?//F ";
    }

    private void write2API(String module, String user){
          //  user = user
        Log.d("arden", "write2API: user= "+user+"  -schrijft zig in voor module "+module);

        String url = "http://api.mrtvda.nl/api/inschrijvingen/add/"+user+"/" + module;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("inschrijving", "gelukt!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("inschrijving", "mislukt");
            }
        });

        mQueue.add(stringRequest);

    }
    private void removeFromAPI(String module, String user){
        String url = "http://api.mrtvda.nl/api/inschrijvingen/delete/"+user+"/" + module;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("uitschrijving", "gelukt!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("uitschrijving", "mislukt");
            }
        });

        mQueue.add(stringRequest);
    }





}