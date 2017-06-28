package com.example.c_ronaldo.assignment3;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    static int screenWidth;
    static int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View drawingView = findViewById(R.id.drawingView);
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = screenSize.y;
        screenWidth = screenSize.x;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.color_mode_menus, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.DrawingMode:
                DrawingView.currentMode = DrawingView.DRAWING_MODE;
                Log.i("rew","drawing mode selected!");
                return true;
            case R.id.DeleteMode:
                DrawingView.currentMode = DrawingView.DELETE_MODE;
                Log.i("rew","delete mode selected!");
                return true;
            case R.id.MovingMode:
                DrawingView.currentMode = DrawingView.MOVING_MODE;
                Log.i("rew","moving mode selected!");
                return true;
            case R.id.black:
                Log.i("rew","black selected!");
                DrawingView.currentColor = DrawingView.blackFramed;
                return true;
            case R.id.blue:
                Log.i("rew","blue selected!");
                DrawingView.currentColor = DrawingView.blueFramed;
                return true;
            case R.id.green:
                Log.i("rew","green selected!");
                DrawingView.currentColor = DrawingView.greenFramed;
                return true;
            case R.id.red:
                Log.i("rew","red selected!");
                DrawingView.currentColor = DrawingView.redFramed;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }


