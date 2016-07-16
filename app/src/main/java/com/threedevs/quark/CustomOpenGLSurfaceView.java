package com.threedevs.quark;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.threedevs.quarkengine.components.Component;
import com.threedevs.quarkengine.components.Position;
import com.threedevs.quarkengine.core.OpenGLSurfaceView;
import com.threedevs.quarkengine.entity.Entity;

import java.util.ArrayList;

/**
 * Created by AJ on 21.06.2016.
 */

public class CustomOpenGLSurfaceView extends OpenGLSurfaceView{

    private String TAG = "CustomOpenGLSurfaceView";

    public CustomOpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomOpenGLSurfaceView(Context context) {
        super(context);
    }

    public float randomInRange(float min, float max) {
        return (float) (Math.random() < 0.5 ? ((1-Math.random()) * (max-min) + min) : (Math.random() * (max-min) + min));
    }

    @Override
    public void onInit(){
        Log.d(TAG,"onInit()");

        for(int i = 0; i < 200; i++) {
            Entity sprite = renderer.spriteFactory.createSprite("bitmaps/default.png");
            ArrayList<Component> positions = renderer.entityManager.getComponentsOfClassForEntity(Position.class, sprite);
            if(positions.size() > 0){
                ((Position)positions.get(0)).setPos(
                        randomInRange(-1.9f,1.9f),
                        randomInRange(-1.0f,1.0f),
                        -i / 80.0f);
            }

        }
        Log.d(TAG,"onInit() done");
    }

    @Override
    public void onPreDrawFrame(){
        //Log.d(TAG,"onPreDrawFrame()");
    }

    @Override
    public void onPostDrawFrame(){
        //Log.d(TAG,"onPostDrawFrame()");
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                break;
            }

            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            queueEvent(new Runnable() {
                // This method will be called on the rendering
                // thread:
                public void run() {
                    //renderer.handleDpadCenter();
                }});
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
