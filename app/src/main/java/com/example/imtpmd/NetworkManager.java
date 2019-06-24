package com.example.imtpmd;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;
    private static final String prefixURL = "https://www.google.nl";

    public RequestQueue requestQueue;

    private NetworkManager(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

    }

    public static synchronized NetworkManager getInstance(Context context) {
        if(instance == null){
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public static synchronized NetworkManager getInstance() {
        if(null == instance){
            throw new IllegalStateException("call getInstance(...) first!");
        }
        return instance;

    }

    public void getRequest(String url, final VolleyCallBack callback){

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("test", "dit werkt");
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("oops",error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }
}
