package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CountryStateActivity extends AppCompatActivity implements CountryFragment.countrySelectedListener,StateFragment.stateSelectedListener {
    StateFragment sFragment;
    CountryFragment cFragment;
    String country;
    String state;
    static List<String> strList = new ArrayList<>();
    static List<String> stateList = new ArrayList<>();
    static String countries[];
    static String states[];
    String stateUrl;
    static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_state);
        FragmentManager fragments = getSupportFragmentManager();
        sFragment = (StateFragment)fragments.findFragmentById(R.id.stateFragment);
        cFragment = (CountryFragment)fragments.findFragmentById(R.id.countryFragment);
        getRequest();
    }

    public void getRequest() {
        Log.i("rew", "Start");
        //handle country data
        strList.clear();
        stateList.clear();
        Response.Listener<JSONArray> cSuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("rew", "get info is "+response.toString());
                try {
                    int len = response.length();
                    Log.i("rew", "length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        Log.i("rew", "before getString");
                        String value = response.getString(i);
                        Log.i("rew", "string value is " + value);
                        strList.add(value);
                    }

                    int size = strList.size();
                    Log.i("rew", "size is "+Integer.toString(size));
                    countries = strList.toArray(new String[size]);
                    for(int i=0;i<countries.length;i++){
                        Log.i("rew", "ith country is " +CountryStateActivity.countries[i] );
                    }
                    CountryFragment.adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener cFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };

        //handle state data
        Response.Listener<JSONArray> sSuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray stateResponse) {
                Log.d("rew", "get state info is "+stateResponse.toString());
                try {
                    int len = stateResponse.length();
                    Log.i("rew", "state length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        Log.i("rew", "before getString");
                        String value = stateResponse.getString(i);
                        Log.i("rew", "state value is " + value);
                        stateList.add(value);
                    }

                    int size = stateList.size();
                    Log.i("rew", "size of state is "+Integer.toString(size));
                    states = stateList.toArray(new String[size]);
                    StateFragment.stateAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener sFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };


        String countryUrl = "http://bismarck.sdsu.edu/hometown/countries";
        JsonArrayRequest getRequestForCountry = new JsonArrayRequest(countryUrl, cSuccess, cFailure);
        JsonArrayRequest getRequestForState = new JsonArrayRequest(stateUrl, sSuccess, sFailure);
        queue = Volley.newRequestQueue(this);
        queue.add(getRequestForCountry);
        queue.add(getRequestForState);
    }

    public void countrySelected(String countryName){
        Log.i("rew", "hahaha!The country name is " + countryName);
        stateUrl = "http://bismarck.sdsu.edu/hometown/states?country="+countryName;
        getRequest();
        country = countryName;
    }

    public void stateSelected(String strState){
        state = strState;
        Log.i("rew", "hahaha!The selected state is " + strState);
    }

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
