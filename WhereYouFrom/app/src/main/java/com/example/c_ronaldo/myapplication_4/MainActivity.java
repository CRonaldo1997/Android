package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
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
    EditText email;
    EditText signInEmail;
    EditText signInPassword;
    String year;
    EditText longtitude;
    EditText latitude;
    String countryString;
    String stateString;
    Double longtiDouble;
    Double latiDouble;
    static String emailUsed;
    static List<String> yearList = new ArrayList<>();
    Spinner yearSpinner;
    ArrayAdapter<String> adapterYear;
    private FirebaseAuth mAuth;
    String urlForDataBase;
    static DBOperations dbOperations;
    List<String> firebaseUserList;
    int nextIdStr;


    public  class GetDatabaseTask extends AsyncTask<User,Void, Void> {
        @Override
        protected Void doInBackground(User... params) {
            dbOperations.addInfo(params[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countryAndState = (TextView) this.findViewById(R.id.countryStateTextView);
        nickName = (EditText) this.findViewById(R.id.nickName);
        passWord = (EditText) this.findViewById(R.id.password);
        city = (EditText) this.findViewById(R.id.city);
        longtitude = (EditText) this.findViewById(R.id.longtitudeText);
        email = (EditText) this.findViewById(R.id.emailText);
        signInEmail = (EditText) this.findViewById(R.id.signInEmail);
        signInPassword = (EditText) this.findViewById(R.id.signInPsword);
        latitude = (EditText) this.findViewById(R.id.latitudeText);
        yearSpinner = (Spinner) this.findViewById(R.id.yearSpinner);
        generateYearList();
        yearSpinnerAdapter();
        mAuth = FirebaseAuth.getInstance();
        dbOperations = new DBOperations(this);
        firebaseUserList = new ArrayList<>();

        getNextId();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usermenu, menu);
        return true;
    }

    public void signInClicked(View button){
         emailUsed = signInEmail.getText().toString();
        String pswordUsed = signInPassword.getText().toString();

        if(!TextUtils.isEmpty(signInEmail.getText()) && !TextUtils.isEmpty(signInPassword.getText())) {
            mAuth.signInWithEmailAndPassword(emailUsed, pswordUsed)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplication(), "Sign in successfully!", Toast.LENGTH_LONG).show();
                                Log.d("rew", "signInWithEmail:onComplete:" + task.isSuccessful());
                                Intent goToChatList = new Intent(getApplication(), chatListActivity.class);
                                startActivity(goToChatList);
                            }else{
                                Toast.makeText(getApplication(), "Failed! email or password may not correct!", Toast.LENGTH_LONG).show();
                                Log.w("rew", "signInWithEmail:failed", task.getException());
                            }

                            // ...
                        }
                    });
        }
        else{
            Toast.makeText(this, "email or password can NOT be empty!", Toast.LENGTH_LONG).show();

        }
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
        Log.i("rew","messageText clicked");
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



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newUser = database.getReference("newUser");
        newUser.child(nickName.getText().toString()+"/nickName").setValue(nickName.getText().toString());
        newUser.child(nickName.getText().toString()+"/country").setValue(countryString);
        newUser.child(nickName.getText().toString()+"/state").setValue(stateString);
        newUser.child(nickName.getText().toString()+"/city").setValue(city.getText().toString());
        newUser.child(nickName.getText().toString()+"/email").setValue(email.getText().toString());
        if(!TextUtils.isEmpty(longtitude.getText())
                && !TextUtils.isEmpty(latitude.getText())){
            newUser.child(nickName.getText().toString()+"/longitude").setValue(longtitude.getText().toString());
            newUser.child(nickName.getText().toString()+"/latitude").setValue(latitude.getText().toString());
        }
        String getEmail = email.getText().toString();
        String password = passWord.getText().toString();

        mAuth.createUserWithEmailAndPassword(getEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("rew", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()){

                        }
                        else{

                        }
                    }
                });
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

    public void getNextId(){
       String urlNextID = "http://bismarck.sdsu.edu/hometown/nextid";
        Response.Listener<String> getNextIdSuccess = new Response.Listener<String>() {
            public void onResponse(String getnextIDResponse) {
                Log.d("rew", "got JSONObject is "+ getnextIDResponse);
                nextIdStr = Integer.parseInt(getnextIDResponse);
                int afterId = nextIdStr-500;
                urlForDataBase  = "http://bismarck.sdsu.edu/hometown/users?reverse=true&beforeid="+nextIdStr+"&afterid="+afterId;
                Log.i("getURL","urlForDataBase is "+urlForDataBase);
                loadServerData();
            }
        };
        Response.ErrorListener getUserFailure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", "get error is "+error.toString());
            }
        };
        StringRequest getRequestForNextId = new StringRequest(urlNextID,getNextIdSuccess,getUserFailure);
        RequestQueue queue23 = Volley.newRequestQueue(this);
        queue23.add(getRequestForNextId);
    }

    //load server data into SQLite
    public void loadServerData(){
        Response.Listener<JSONArray> getDataSuccess = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray getServerResponse) {
//                JSONArray jsonUsers = getServerResponse;
                Log.d("rew", "get user info is "+ getServerResponse.toString());
                try {
                    int len = getServerResponse.length();
                    Log.i("rew", "in main Activity! users length is "+Integer.toString(len));
                    for (int i=0;i<len;i++){
                        String objectValue = getServerResponse.getString(i);
                        JSONObject newJson = new JSONObject(objectValue);
                        String userNickname = newJson.getString("nickname");
                        String userCountry = newJson.getString("country");
                        String userState = newJson.getString("state");
                        String userCity = newJson.getString("city");
                        String userYear = newJson.getString("year");
                        String userLongitude = newJson.getString("longitude");
                        String userLatitude = newJson.getString("latitude");
                        String userId = newJson.getString("id");

                        String userInfo = userNickname+", "+userCountry+", "+userState+", "+userCity+" ,"+userYear;
                        Log.i("rew", "in main activity, a user's value is " + userInfo);

                        //insert users into SQLite
                        User oneUser = new User(userNickname,userCountry,userState,userCity,userYear,userLongitude,userLatitude,userId);
                        new GetDatabaseTask().execute(oneUser);
                    }

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
        JsonArrayRequest getRequestForAllUsers = new JsonArrayRequest(urlForDataBase, getDataSuccess, getUserFailure);
        RequestQueue queue22 = Volley.newRequestQueue(this);
        queue22.add(getRequestForAllUsers);
    }



}
