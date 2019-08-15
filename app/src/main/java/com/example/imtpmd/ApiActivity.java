/*
package com.example.imtpmd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiActivity {
    package com.example.imtpmd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

    public class ApiActivity extends AppCompatActivity {

        private TextView tv;
        private RequestQueue mQueue;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_api);

            tv = (TextView)findViewById(R.id.response);

            mQueue = Volley.newRequestQueue(this);

            jsonParser();
        }

        protected void jsonParser() {
            String url = "http://api.mrtvda.nl/api/keuzevakken";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("array", "Dit werkt");
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("Keuzevakken");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject keuzevakken = jsonArray.getJSONObject(i);

                            String keuzevak = keuzevakken.getString("keuzevak");

                            tv.append(keuzevak + "\n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tv.append("hoi");
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        }
    }

}
*/