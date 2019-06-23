package com.example.imtpmd;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ModulesFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    @Override
    //koppel de layout van jezelf aan layout van parent
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_modules, viewGroup, false);
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ArrayList<String> modules = new ArrayList<>();


        modules.add("ikml");
        modules.add("ipmedt2");
        modules.add("mimp");
        modules.add("gelukt");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, modules);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("klikkie", "je hebt geklukt op: " + position);
        Toast.makeText(getActivity(), "je drukt op ppersoon: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
        // kijkt in arraylist welke plek word bedoeld
        openActivity2(parent.getItemAtPosition(position).toString());
    }

    public void openActivity2(String clickedItem) {

        Intent intent = new Intent(this.getContext(), ModuleActivity.class);
        intent.putExtra(EXTRA_TEXT, clickedItem);

        startActivity(intent);
    }
}