package com.example.c_ronaldo.assignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;


public class DateActivity extends AppCompatActivity {
    DatePicker datePicker;
    String year;
    String month;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        datePicker = (DatePicker) this.findViewById(R.id.mDatePicker);
    }

    public void dateCancel(View button){
        Intent result = getIntent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    public void dateDone(View button){
        Log.i("rew", "in dateDone!");
        year = String.valueOf(datePicker.getYear());
        month = String.valueOf((datePicker.getMonth()+1));
        day = String.valueOf(datePicker.getDayOfMonth());
        Log.i("rew", "birthday is " + year +"/"+ month);

        Intent result = getIntent();
        result.putExtra("YEAR",year);
        result.putExtra("MONTH",month);
        result.putExtra("DAY",day);
        setResult(RESULT_OK, result);
        finish();
    }
}
