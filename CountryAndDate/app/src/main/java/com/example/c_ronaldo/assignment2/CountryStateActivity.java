package com.example.c_ronaldo.assignment2;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CountryStateActivity extends AppCompatActivity implements CountryFragment.countrySelectedListener,StateFragment.stateSelectedListener {


    StateFragment sFragment;
    CountryFragment cFragment;
    String country;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_state);
        FragmentManager fragments = getSupportFragmentManager();
        sFragment = (StateFragment)fragments.findFragmentById(R.id.stateFragment);
        cFragment = (CountryFragment)fragments.findFragmentById(R.id.countryFragment);
    }

    public void countrySelected(String countryName){
        Log.i("rew", "hahaha!The country name is " + countryName);
        country = countryName;
        sFragment.setCountryName(countryName);
    }

    public void stateSelected(String strState){
        state = strState;
        Log.i("rew", "hahaha!The selected state is " + strState);
    }
//    public void countryStateBack(View button) {
//        finish();
//    }
    public void countryStateCanceled(View button){
        Intent result = getIntent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    public void countryStateDone(View button){
        Intent result = getIntent();
        result.putExtra("COUNTRY",country);
        result.putExtra("STATE",state);
        setResult(RESULT_OK, result);
        finish();
    }
}
