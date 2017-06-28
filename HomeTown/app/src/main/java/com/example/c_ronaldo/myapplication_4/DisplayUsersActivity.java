package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class DisplayUsersActivity extends AppCompatActivity implements DisplayUserFragment.userSelectedListener{
    DisplayUserFragment dFragment;
    static List<String> userList = new ArrayList<>();
//    static List<String> selectedUsers = new ArrayList<>();
    Spinner spinnerCountry;
    Spinner spinnerState;
    Spinner spinnerYear;
    static List<String> countriesList = new ArrayList<>();
    static List<String> statesList = new ArrayList<>();
    String getUserInfoUrl;

    static JSONArray jsonUsers;
    static int selectedUserId;
    static boolean displayAllUserSwitch = false;
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> yearAdapter;
    String selectedCountry="";
    String selectedState="";
    String selectedYear="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);
        FragmentManager fragments = getSupportFragmentManager();
        dFragment = (DisplayUserFragment)fragments.findFragmentById(R.id.displayFragment);
        spinnerCountry = (Spinner) this.findViewById(R.id.spinnerCountry);
        spinnerState = (Spinner) this.findViewById(R.id.spinnerState);
        spinnerYear = (Spinner) this.findViewById(R.id.yearSpinner);
        statesList.add(0,"None selected");

        //get user info
//        getUserInfoUrl = "http://bismarck.sdsu.edu/hometown/users?reverse=true";
        getUserInfoUrl = "http://bismarck.sdsu.edu/hometown/users";
        displayAllUsers(getUserInfoUrl);

        //set country spinner
        setYearSpinnerAdapter();
        setCountrySpinnerAdapter();
        setStateSpinnerAdapter();
        getCountriesList();
    }

    public void userSelected(int id){
        Log.i("rew","userSelected id is "+id);
        selectedUserId = id;
        Intent toMap = new Intent(this,MapsActivity.class);
        startActivity(toMap);
    }

    public void onShowMapClicked(View button){
        displayAllUserSwitch = true;
        Intent goToMaps = new Intent(this,MapsActivity.class);
        startActivity(goToMaps);
    }
    public String generateUrl(String selectedCountry,String selectedState,String selectedYear ){

        if(selectedCountry.equals("None selected")&&selectedState.equals("None selected")&&selectedYear.equals("None selected")){//1
            return "http://bismarck.sdsu.edu/hometown/users";
        }
        else if(selectedCountry.equals("None selected")&&selectedState.equals("None selected")&&!selectedYear.equals("None selected")) {//2
            return "http://bismarck.sdsu.edu/hometown/users?&country=&state=&year="+selectedYear;
        }
        else if(selectedCountry.equals("None selected")&&!selectedState.equals("None selected")&&selectedYear.equals("None selected")){//3
            return "http://bismarck.sdsu.edu/hometown/users?&country=&state="+selectedState;
        }
        else if(!selectedCountry.equals("None selected")&&selectedState.equals("None selected")&&selectedYear.equals("None selected")){//4
            return "http://bismarck.sdsu.edu/hometown/users?&country="+selectedCountry+"&state=";
        }
        else if(selectedCountry.equals("None selected")&&!selectedState.equals("None selected")&&!selectedYear.equals("None selected")){//5
            return "http://bismarck.sdsu.edu/hometown/users?&country=&state="+selectedState+"&year="+selectedYear;
        }
        else if(!selectedCountry.equals("None selected")&&!selectedState.equals("None selected")&&selectedYear.equals("None selected")){//6
            return "http://bismarck.sdsu.edu/hometown/users?&country="+selectedCountry+"&state="+selectedState;
        }
        else if(!selectedCountry.equals("None selected")&&selectedState.equals("None selected")&&!selectedYear.equals("None selected")){//7
            return "http://bismarck.sdsu.edu/hometown/users?&country="+selectedCountry+"&state=&year="+selectedYear;
        }
        else{
        return "http://bismarck.sdsu.edu/hometown/users?&country="+selectedCountry+"&state="+selectedState+"&year="+selectedYear;
        }
    }

    public void setYearSpinnerAdapter(){
        yearAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, MainActivity.yearList);
        spinnerYear.setAdapter(yearAdapter);
        spinnerYear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(DisplayUsersActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                selectedYear = parent.getItemAtPosition(position).toString();
                Log.i("rew","Alohaa,selectedCountry is "+selectedCountry+" selectedState is "+selectedState+" selectedYear is "+selectedYear);
                String selectedYearUrl = generateUrl(selectedCountry,selectedState,selectedYear);
                selectedYearUrl = selectedYearUrl.replace(" ","%20");
                Log.i("rew","HiHiHi, get year url is "+selectedYearUrl);
                displayAllUsers(selectedYearUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    public void getStatesForCountry(String countryName){
        statesList.clear();
        Response.Listener<JSONArray> getStateSuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray getStateResponse) {
                Log.d("rew", "states info for country is "+getStateResponse.toString());
                try {
                    int len = getStateResponse.length();
                    Log.i("rew", "attention! state list length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        String stateValue = getStateResponse.getString(i);
                        statesList.add(stateValue);
                    }
                    if(!statesList.isEmpty()) {statesList.add(0,"None selected");}
                    stateAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener getStateFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };

        String getStateUrl = "http://bismarck.sdsu.edu/hometown/states?country="+countryName;
        JsonArrayRequest getRequestForStates = new JsonArrayRequest(getStateUrl, getStateSuccess, getStateFailure);
        RequestQueue queue10 = Volley.newRequestQueue(this);
        queue10.add(getRequestForStates);

    }

    public void getCountriesList(){
        countriesList.clear();
        Response.Listener<JSONArray> getCountrySuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray getCountryResponse) {
                Log.d("rew", "2nd get country info is "+getCountryResponse.toString());
                try {
                    int len = getCountryResponse.length();
                    Log.i("rew", "attention! country list length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        String countryValue = getCountryResponse.getString(i);
                        countriesList.add(countryValue);
                    }
                    if (!countriesList.isEmpty()){countriesList.add(0,"None selected");}
                    countryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener getCountryFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };

        String getCountryUrl = "http://bismarck.sdsu.edu/hometown/countries";
        JsonArrayRequest getRequestForCountries = new JsonArrayRequest(getCountryUrl, getCountrySuccess, getCountryFailure);
        RequestQueue queue3 = Volley.newRequestQueue(this);
        queue3.add(getRequestForCountries);
    }

    public void setStateSpinnerAdapter(){
        stateAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statesList);
        spinnerState.setAdapter(stateAdapter);
        spinnerState.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(DisplayUsersActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                selectedState = parent.getItemAtPosition(position).toString();
                Log.i("rew","Alohaa,selectedCountry is "+selectedCountry+" selectedState is "+selectedState+" selectedYear is "+selectedYear);
                String selectedStateUrl = generateUrl(selectedCountry,selectedState,selectedYear);
                selectedStateUrl = selectedStateUrl.replace(" ","%20");
                Log.i("rew","HiHiHi, get state url is "+selectedStateUrl);
                displayAllUsers(selectedStateUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    public void setCountrySpinnerAdapter(){
        countryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, countriesList);
        spinnerCountry.setAdapter(countryAdapter);
        spinnerCountry.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(DisplayUsersActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                getStatesForCountry(parent.getItemAtPosition(position).toString());
                selectedCountry = parent.getItemAtPosition(position).toString();
                Log.i("rew","Alohaa,selectedCountry is "+selectedCountry+" selectedState is "+selectedState+" selectedYear is "+selectedYear);
                String selectedCountryUrl = generateUrl(selectedCountry,selectedState,selectedYear);
                selectedCountryUrl = selectedCountryUrl.replace(" ","%20");
                Log.i("rew","HiHiHi, get country url is "+selectedCountryUrl);
                displayAllUsers(selectedCountryUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }

    public void displayAllUsers(String url){
        //get all users list
        userList.clear();
        Response.Listener<JSONArray> getUserSuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray getUserResponse) {
                jsonUsers = getUserResponse;
                Log.d("rew", "get user info is "+getUserResponse.toString());
                try {
                    int len = getUserResponse.length();
                    Log.i("rew", "attention! all users length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        String objectValue = getUserResponse.getString(i);
                        JSONObject newJson = new JSONObject(objectValue);
                        String userNickname = newJson.getString("nickname");
                        String userCountry = newJson.getString("country");
                        String userState = newJson.getString("state");
                        String userCity = newJson.getString("city");
                        String userYear = newJson.getString("year");
                        String userInfo = userNickname+", "+userCountry+", "+userState+", "+userCity+" ,"+userYear;
                        Log.i("rew", "a user's value is " + userInfo);
                        userList.add(userInfo);
                    }
                    DisplayUserFragment.userAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener getUserFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };
        JsonArrayRequest getRequestForAllUsers = new JsonArrayRequest(url, getUserSuccess, getUserFailure);
        RequestQueue queue2 = Volley.newRequestQueue(this);
        queue2.add(getRequestForAllUsers);
    }


}
