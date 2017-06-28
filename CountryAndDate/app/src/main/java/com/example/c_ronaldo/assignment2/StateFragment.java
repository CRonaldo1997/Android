package com.example.c_ronaldo.assignment2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class StateFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] stateData ={"345", "fdg", "lor",
            "sist", "aasfmet","co13etuer", "adasing", "easft", "masfbi", "vadsfl"};
    String[] states;
    List<String> strList = new ArrayList<String>();
    List<String> newList = new ArrayList<String>();
//    String[] chinaStates = getResources().getStringArray(R.array.china);
    ArrayAdapter<String> adapter;

    public interface stateSelectedListener{
        void stateSelected(String strState);
    }

    public void freshData(){
        newList = Arrays.asList(states);
        strList.clear();
        strList.addAll(newList);
        adapter.notifyDataSetChanged();
    }

    public void setCountryName(String cName){
        if(cName.equals("China")){
            states = getResources().getStringArray(R.array.china);
            freshData();
            Log.i("rew", "China selected! ");
        }
        else if (cName.equals("India")){
            states = getResources().getStringArray(R.array.india);
            freshData();
            Log.i("rew", "india selected!" );
        }
        else if (cName.equals("Mexico")){
            states = getResources().getStringArray(R.array.mexico);
            freshData();
            Log.i("rew", "mexico selected!");
        }else if (cName.equals("USA")){
            states = getResources().getStringArray(R.array.usa);
            freshData();
            Log.i("rew", "usa selected!" );
        }
    }

    public StateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_state, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        states = getResources().getStringArray(R.array.china);
//        strList = Arrays.asList(states);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,strList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("rew", "position of click " + position );
        Log.i("rew", "id of click " + id );
        Log.i("rew", "state of click " + states[position] );
        StateFragment.stateSelectedListener ssListener = (StateFragment.stateSelectedListener)getActivity();
        ssListener.stateSelected(states[position]);
    }


}
