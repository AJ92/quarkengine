package com.threedevs.quark;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.threedevs.quarkengine.core.OpenGLSurfaceView;

/**
 * Created by AJ on 21.06.2016.
 */

public class CustomOpenGLSurfaceView extends OpenGLSurfaceView{

    private String TAG = "CustomOpenGLSurfaceView";

    public CustomOpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onInit(){
        Log.d(TAG,"onInit()");
        renderer.spriteFactory.createSprite("bitmaps/default.png");
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
