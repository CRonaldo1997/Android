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
import java.util.List;


public class CountryFragment extends ListFragment implements AdapterView.OnItemClickListener {

    static ArrayAdapter<String> adapter;
    public CountryFragment() {
        // Required empty public constructor
    }

    public interface countrySelectedListener{
        void countrySelected(String countryName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,CountryStateActivity.strList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("rew", "position of click " + position );
        Log.i("rew", "id of click " + id );
        Log.i("rew", "Selected country is  " + CountryStateActivity.countries[position]);
        countrySelectedListener csListener = (countrySelectedListener)getActivity();
        csListener.countrySelected(CountryStateActivity.countries[position]);
    }
}
