package com.example.c_ronaldo.assignment3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by C_Ronaldo on 2/24/17.
 */

public class DrawingView extends View implements View.OnTouchListener{

    static Paint blackFramed;
    static Paint redFramed;
    static Paint blueFramed;
    static Paint greenFramed;
    static Paint currentColor;
    VelocityTracker velocity;
    public static int currentMode;
    public final static int DRAWING_MODE = 0;
    public final static int DELETE_MODE = 1;
    public final static int MOVING_MODE = 2;

    ArrayList<Float> circles = new ArrayList<>();
    ArrayList<Paint> paint = new ArrayList<>();


    static {
        blackFramed = new Paint();
        blackFramed.setColor(Color.BLACK);
        blackFramed.setStrokeWidth(8.0f);
        blackFramed.setStyle(Paint.Style.STROKE);

        redFramed = new Paint();
        redFramed.setColor(Color.RED);
        redFramed.setStrokeWidth(8.0f);
        redFramed.setStyle(Paint.Style.STROKE);

        blueFramed = new Paint();
        blueFramed.setColor(Color.BLUE);
        blueFramed.setStrokeWidth(8.0f);
        blueFramed.setStyle(Paint.Style.STROKE);

        greenFramed = new Paint();
        greenFramed.setColor(Color.GREEN);
        greenFramed.setStrokeWidth(8.0f);
        greenFramed.setStyle(Paint.Style.STROKE);
    }

    private float startX;
    private float startY;
    private float currentX;
    private float currentY;
    private float currentRadius;
    private float deleteX;
    private float deleteY;
    private boolean isMoving;
    private float tapX;
    private float tapY;
    private float movingX;
    private float movingY;
    private int movingCircleIndex;
    private float currentXVelocity;
    private float currentYVelocity;
    private float deltaX;
    private float deltaY;
    private float originalX;
    private float originalY;
    private float movingRadius;

    public DrawingView(Context context, AttributeSet xmlAttributes) {
        super(context, xmlAttributes);
        this.currentMode = DRAWING_MODE;
        this.currentColor = blackFramed;
        setOnTouchListener(this);
    }

    private void changeOnCollision() {
        if (xIsOutOfBounds(movingX)) currentXVelocity = currentXVelocity * -0.8f;
        if (yIsOutOfBounds(movingY)) currentYVelocity = currentYVelocity * -0.8f;
    }

    private boolean xIsOutOfBounds(float movingX) {
        if (movingX - movingRadius < 0) return true;
        if (movingX + movingRadius > MainActivity.screenWidth) return true;
        return false;
    }

    private boolean yIsOutOfBounds(float movingY) {
        if (movingY - movingRadius < 0) return true;
        if (movingY + movingRadius+ 150 > MainActivity.screenHeight) return true;
        return false;
    }


    public boolean onTouch(View arg0, MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                return handleActionDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleActionMove(event);
            case MotionEvent.ACTION_UP:
                return handelActionUp(event);
        }
        return false;
    }

    private boolean handleActionMove(MotionEvent event) {
        if (currentMode == DRAWING_MODE){
            currentX = event.getX();
            currentY = event.getY();
            currentRadius = (float) getRadius();
            invalidate();
            return true;
        }

        if (currentMode == MOVING_MODE){
            velocity.addMovement(event);
            currentX = event.getX();
            currentY = event.getY();
            deltaX = currentX - startX;
            deltaY = currentY - startY;
            invalidate();
            return true;
        }
        return true;
    }

    private boolean handleActionDown(MotionEvent event) {
        if (currentMode == DRAWING_MODE){
            Log.i("rew","drawing mode, in down!");
            startX = event.getX();
            startY = event.getY();
            return true;
        }
        if (currentMode == DELETE_MODE){
            Log.i("rew","delete mode, in down!");
            deleteX = event.getX();
            deleteY = event.getY();

            for (int i=0,len=circles.size();i<len/3;i++){
                float centreX = circles.get(3*i);
                float centreY = circles.get(3*i+1);
                float distance = (float) Math.sqrt((deleteX-centreX)*(deleteX-centreX)+
                        (deleteY-centreY)*(deleteY-centreY));
                if(distance <= circles.get(3*i+2)){
                    circles.set(3*i+2,0f);
                }
            }
            invalidate();
            return true;
        }
        if (currentMode == MOVING_MODE){
            Log.i("rew","moving mode, in down!");
            startX = event.getX();
            startY = event.getY();
            velocity = VelocityTracker.obtain();
            velocity.addMovement(event);
            tapX = event.getX();
            tapY = event.getY();

            for (int i=0;i<circles.size()/3;i++){
                float centreX = circles.get(3*i);
                float centreY = circles.get(3*i+1);
                float distance = (float) Math.sqrt((tapX-centreX)*(tapX-centreX)+
                        (tapY-centreY)*(tapY-centreY));
                if(distance <= circles.get(3*i+2)){
                    movingCircleIndex = i;
                    Log.i("rew","get the index! "+movingCircleIndex);
                    break;
                }
            }
            originalX = circles.get(3*movingCircleIndex);
            originalY = circles.get(3*movingCircleIndex+1);
            movingRadius = circles.get(3*movingCircleIndex+2);
            movingX = originalX;
            movingY = originalY;

            return true;
        }
        return true;
    }

    private boolean handelActionUp(MotionEvent event){
        if (currentMode == DRAWING_MODE) {
            currentX = event.getX();
            currentY = event.getY();
            float radius = (float) getRadius();
            circles.add(startX);
            circles.add(startY);
            circles.add(radius);
            paint.add(currentColor);
            invalidate();
            return true;
        }
        if (currentMode == MOVING_MODE){
            velocity.computeCurrentVelocity(1000);
            Log.i("rew", "X vel " + velocity.getXVelocity() + " Y vel " + velocity.getYVelocity());
            currentXVelocity = velocity.getXVelocity();
            currentYVelocity = velocity.getYVelocity();

            velocity.recycle();
            velocity = null;
            return true;
        }
        return true;
    }

    public double getRadius(){
       return Math.sqrt((currentX-startX)*(currentX-startX) + (currentY-startY)*(currentY-startY));
    }

    protected void onDraw(Canvas canvas) {
        if (currentMode == DELETE_MODE){
            for (int i = 0; i < circles.size() / 3; i++) {
                canvas.drawCircle(circles.get(3 * i), circles.get(3 * i + 1), circles.get(3 * i + 2), paint.get(i));
            }
        }

        if (currentMode == DRAWING_MODE) {
            canvas.drawCircle(startX, startY, currentRadius, currentColor);
            currentRadius = (float) 0;
            for (int i = 0; i < circles.size() / 3; i++) {
                canvas.drawCircle(circles.get(3 * i), circles.get(3 * i + 1), circles.get(3 * i + 2), paint.get(i));
            }
        }
        if (currentMode == MOVING_MODE){
                movingX = movingX + 0.01f*currentXVelocity;
                movingY = movingY + 0.01f*currentYVelocity;
                changeOnCollision();
                circles.set(3 * movingCircleIndex, movingX);
                circles.set(3 * movingCircleIndex + 1, movingY);

                for (int i = 0; i < circles.size() / 3; i++) {
                    canvas.drawCircle(circles.get(3 * i), circles.get(3 * i + 1), circles.get(3 * i + 2), paint.get(i));
                }
                invalidate();
        }
    }

}
