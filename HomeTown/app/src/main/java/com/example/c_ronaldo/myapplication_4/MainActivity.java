package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_COUNTRYSTATE = 321;
    private static final int INTENT_REQUEST_LONG_AND_LATI = 123;
    static boolean selectLatLngSwitch = false;
    TextView countryAndState;
    EditText nickName;
    EditText passWord;
    EditText city;
    String year;
    EditText longtitude;
    EditText latitude;
    String countryString;
    String stateString;
    Double longtiDouble;
    Double latiDouble;
    static List<String> yearList = new ArrayList<>();
    Spinner yearSpinner;
    ArrayAdapter<String> adapterYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countryAndState = (TextView) this.findViewById(R.id.countryStateTextView);
        nickName = (EditText) this.findViewById(R.id.nickName);
        passWord = (EditText) this.findViewById(R.id.password);
        city = (EditText) this.findViewById(R.id.city);
        longtitude = (EditText) this.findViewById(R.id.longtitudeText);
        latitude = (EditText) this.findViewById(R.id.latitudeText);
        yearSpinner = (Spinner) this.findViewById(R.id.yearSpinner);
        generateYearList();
        yearSpinnerAdapter();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usermenu, menu);
        return true;
    }

    public void goToShowUsers(MenuItem selectedMenu) {
        Intent goToDisplayUsers = new Intent(this,DisplayUsersActivity.class);
        startActivity(goToDisplayUsers);
    }

    public void yearSpinnerAdapter(){
        adapterYear = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, yearList);
        yearSpinner.setAdapter(adapterYear);
        yearSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }});
    }


    public void generateYearList(){
        yearList.clear();
        for(int i=1970;i<=2017;i++){
            yearList.add(Integer.toString(i));
        }
        yearList.add(0,"None selected");
    }

    public void setCountryState(View button) {
        Intent goCountryState = new Intent(this,CountryStateActivity.class);
        startActivityForResult(goCountryState, INTENT_REQUEST_COUNTRYSTATE);
    }

    public void onSetLongLatClicked(View button){
        selectLatLngSwitch = true;
        Intent goToSetOnMap = new Intent(this,MapsActivity.class);
        startActivityForResult(goToSetOnMap,INTENT_REQUEST_LONG_AND_LATI);
    }

    public void postStudentInfo(View button){
        Log.i("rew","post clicked");
        boolean isInfoComplete = !TextUtils.isEmpty(nickName.getText())
                && !TextUtils.isEmpty(passWord.getText())
                && !TextUtils.isEmpty(city.getText())
                && !TextUtils.isEmpty(year)
                && !TextUtils.isEmpty(countryAndState.getText());

        if(isInfoComplete){
            Log.i("rew","complete!");
            //post info here

            //add the posting user to adapter
            String newUserValue = nickName.getText()+", "+countryString+", "+stateString+", "+city.getText();
            DisplayUsersActivity.userList.add(newUserValue);


            JSONObject newUser = new JSONObject();
            try {
                newUser.put("nickname", nickName.getText());
                newUser.put("password", passWord.getText());
                newUser.put("country", countryString);
                newUser.put("state", stateString);
                newUser.put("city", city.getText());
                if(year.equals("None selected")){
                    Toast.makeText(this, "You must select a year!", Toast.LENGTH_SHORT).show();
                    Log.i("rew","You must select a year!");
                    return;
                }
                newUser.put("year", Integer.parseInt(year));
                if(!TextUtils.isEmpty(longtitude.getText())
                        && !TextUtils.isEmpty(latitude.getText())){
                    newUser.put("longitude", Double.parseDouble(longtitude.getText().toString()));
                    newUser.put("latitude", Double.parseDouble(latitude.getText().toString()));

                }

                Response.Listener<JSONObject> postSuccess = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject postResponse) {
                        //Process response here
                        Log.i("rew", postResponse.toString());
                    }
                };
                Response.ErrorListener postFailure = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("rew", "post fail " + new String(error.networkResponse.data));
                    }
                };
                String postUrl = "http://bismarck.sdsu.edu/hometown/adduser";
                JsonObjectRequest postRequest = new JsonObjectRequest(postUrl, newUser, postSuccess, postFailure);
                CountryStateActivity.queue.add(postRequest);
                Toast.makeText(this, "New user posted!", Toast.LENGTH_LONG).show();
            } catch (JSONException error) {
                Log.e("rew", "JSON error", error);
                return;
            }
        }
        else{
            Toast.makeText(this, "Not posted because user info not complete!", Toast.LENGTH_LONG).show();
            Log.i("rew","not complete!");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == INTENT_REQUEST_LONG_AND_LATI){
            Log.i("rew", "get feedback for long and latitude!");
            switch (resultCode) {
                case RESULT_OK:
                    longtiDouble = data.getDoubleExtra("longitude",200.0);
                    latiDouble = data.getDoubleExtra("latitude",200.0);
                    longtitude.setText(Double.toString(longtiDouble));
                    latitude.setText(Double.toString(latiDouble));
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }

        if (requestCode == INTENT_REQUEST_COUNTRYSTATE){
            Log.i("rew", "get INTENT_REQUEST_COUNTRYSTATE!");
            switch (resultCode) {
                case RESULT_OK:
                    countryString = data.getStringExtra("COUNTRY");
                    stateString = data.getStringExtra("STATE");
                    countryAndState.setText(countryString+"/"+stateString);
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }
}
