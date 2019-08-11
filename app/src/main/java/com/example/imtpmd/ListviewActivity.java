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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListviewActivity extends AppCompatActivity  {

    private RequestQueue mQueue;
    private ArrayList<String> windowArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mQueue = Volley.newRequestQueue(this);
        jsonParser();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_textview, windowArray);
        ListView listView = (ListView)findViewById(R.id.window_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Log.d("klikkie", "je hebt geklukt op: ");
                Toast.makeText(ListviewActivity.this, windowArray.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListviewActivity.this, ModuleActivity.class);
                startActivity(intent);
                // kijkt in arraylist welke plek word bedoeld
            }
        });
    }

    protected void jsonParser() {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
}