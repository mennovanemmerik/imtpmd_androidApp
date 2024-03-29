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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

//
//Dit Fragment is een moduleKnop dat in een lijst staat, voor ieder item in de windowArray word er een aangemaakt
//

public class ModulesFragment extends ListFragment  {


    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    LinearLayout co;
    @Override
    //koppel de layout van jezelf aan layout van parent
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_modules, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ArrayList<String> modules = new ArrayList<>();


        if (getArguments() != null) {
            String lijst[] = getArguments().getStringArray("lijst");

            for (int i = 0; i < lijst.length; i++) {
                modules.add(lijst[i]);

            }
        }

/*
        ModulesFragment fragment = (ModulesFragment) getFragmentManager().findFragmentById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, modules);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // getListView().setBackgroundColor(Color.BLUE); //IS NIET ALLEN ITEMS MAAR OOK ACHTERGROND?
    }
/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "je drukt op ppersoon: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
        // kijkt in arraylist welke plek word bedoeld
        openActivity2(parent.getItemAtPosition(position).toString());
    }

    public void openActivity2(String clickedItem) {

        Intent intent = new Intent(this.getContext(), ModuleActivity.class);
        intent.putExtra(EXTRA_TEXT, clickedItem);

        startActivity(intent);
    }
    */
    }}
