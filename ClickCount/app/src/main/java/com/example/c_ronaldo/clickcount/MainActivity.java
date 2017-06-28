package com.example.c_ronaldo.clickcount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity {

    TextView clickCountOutput;
    TextView bgCountOutput;
    int clickCount = 0;
    int bgCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("rew", "onCreate!");
        Configuration config = getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Portrait Mode
            Log.i("rew", "PortraitMode!");
            setContentView(R.layout.activity_main);
            clickCountOutput = (TextView) this.findViewById(R.id.clickCountOutput);
            bgCountOutput = (TextView) this.findViewById(R.id.bgCountOutput);
        } else {
            // Landscape Mode
            Log.i("rew", "LandscapeMode!");
            setContentView(R.layout.activity_landscape);
            clickCountOutput = (TextView) this.findViewById(R.id.clickCountOutput2);
            bgCountOutput = (TextView) this.findViewById(R.id.bgCountOutput2);
        }
    }

    public void increase(View button) {
        Log.i("rew", "increase");
        clickCount++;
        clickCountOutput.setText(String.valueOf(clickCount));
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("rew", "onStart!");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("rew", "onRestoreInstanceState!");
        clickCount = savedInstanceState.getInt("ClickCount_Key");
        bgCount = savedInstanceState.getInt("BgdCount_Key");
        clickCountOutput.setText(String.valueOf(clickCount));
        bgCountOutput.setText(String.valueOf(bgCount));
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("rew", "onRestart!");
        bgCountOutput.setText(String.valueOf(bgCount));
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("rew", "onResume!");
    }

        @Override
    protected void onPause() {
        super.onPause();
        Log.i("rew", "onPause!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState ) {
        Log.i("rew", "onSaveInstanceState!");
        super.onSaveInstanceState(outState);
        //if go into background bgCount++, if just rotation, no need
        if (isChangingConfigurations()==false){
            bgCount++;
        }
        outState.putInt("ClickCount_Key", clickCount);
        outState.putInt("BgdCount_Key", bgCount);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("rew", "onStop!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("rew", "onDestroy!");
    }
}

