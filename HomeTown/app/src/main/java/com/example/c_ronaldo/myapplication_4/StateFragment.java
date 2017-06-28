package com.example.c_ronaldo.myapplication_4;

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
import java.util.List;

public class StateFragment extends ListFragment implements AdapterView.OnItemClickListener  {

    static ArrayAdapter<String> stateAdapter;

    public interface stateSelectedListener{
        void stateSelected(String strState);
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
        stateAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,CountryStateActivity.stateList);
        setListAdapter(stateAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("rew", "position of click " + position );
        Log.i("rew", "id of click " + id );
        Log.i("rew", "state of click " + CountryStateActivity.states[position] );
        StateFragment.stateSelectedListener ssListener = (StateFragment.stateSelectedListener)getActivity();
        ssListener.stateSelected(CountryStateActivity.states[position]);
    }
}
