package com.example.c_ronaldo.assignment2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class personActivity extends AppCompatActivity {
    EditText fName;
    EditText lName;
    EditText age;
    EditText email;
    EditText phone;
    TextView birthDate;
    TextView countryAndState;
    private static final int INTENT_EXAMPLE_REQUEST = 123;
    private static final int INTENT_REQUEST_COUNTRYSTATE = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        fName = (EditText) this.findViewById(R.id.mFirstName);
        lName = (EditText) this.findViewById(R.id.mLastName);
        age = (EditText) this.findViewById(R.id.mAge);
        email = (EditText) this.findViewById(R.id.mEmail);
        phone = (EditText) this.findViewById(R.id.mPhone);
        birthDate = (TextView) this.findViewById(R.id.birthDay);
        countryAndState = (TextView) this.findViewById(R.id.countryStateTextView);


        //load saved data
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String savedFirstName = pref.getString("firstName","");
        String savedLastName = pref.getString("lastName","");
        String savedAge = pref.getString("age","");
        String savedEmail = pref.getString("email","");
        String savedPhone = pref.getString("phone","");
        String savedBirthday = pref.getString("birthday","");
        String savedCountryState = pref.getString("countryState","");

        //set saved value to UI
        fName.setText(savedFirstName);
        lName.setText(savedLastName);
        age.setText(savedAge);
        email.setText(savedEmail);
        phone.setText(savedPhone);
        birthDate.setText(savedBirthday);
        countryAndState.setText(savedCountryState);
    }

    //go to DateActivity
    public void setDate(View button) {
        Intent goDate = new Intent(this,DateActivity.class);
        startActivityForResult(goDate, INTENT_EXAMPLE_REQUEST);
    }

    //go to CountryStateActivity
    public void setCountryState(View button) {
        Intent goCountryState = new Intent(this,CountryStateActivity.class);
        startActivityForResult(goCountryState, INTENT_REQUEST_COUNTRYSTATE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_EXAMPLE_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    String yearString = data.getStringExtra("YEAR");
                    String monthString = data.getStringExtra("MONTH");
                    String dayString = data.getStringExtra("DAY");
                    birthDate.setText(monthString+"/"+dayString+"/"+yearString);
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
        if (requestCode == INTENT_REQUEST_COUNTRYSTATE){
            Log.i("rew", "get INTENT_REQUEST_COUNTRYSTATE!");
            switch (resultCode) {
                case RESULT_OK:
                    String countryString = data.getStringExtra("COUNTRY");
                    String stateString = data.getStringExtra("STATE");
                    countryAndState.setText(countryString+"/"+stateString);
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }



    public void saveData(View button){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("firstName",fName.getText().toString());
        editor.putString("lastName",lName.getText().toString());
        editor.putString("age",age.getText().toString());
        editor.putString("email",email.getText().toString());
        editor.putString("phone",phone.getText().toString());
        editor.putString("birthday",birthDate.getText().toString());
        editor.putString("countryState",countryAndState.getText().toString());

        editor.commit();

    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("rew", "onDestroy!");
    }
}
