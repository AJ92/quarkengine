package xyz.sigsegowl.quark;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;


import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.components.Position;
import xyz.sigsegowl.quarkengine.components.Rotation;
import xyz.sigsegowl.quarkengine.components.Scale;
import xyz.sigsegowl.quarkengine.core.OpenGLSurfaceView;
import xyz.sigsegowl.quarkengine.entity.Entity;
import xyz.sigsegowl.quarkengine.math.Vector.Quaternion;

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

        Entity sprite = renderer.spriteFactory.createSprite("bitmaps/default.png");
        ArrayList<Component> positions = renderer.entityManager.getComponentsOfClassForEntity(Position.class, sprite);
        if(positions.size() > 0) {
            ((Position) positions.get(0)).setPos(
                    0.0f,
                    0.0f,
                    0.0f
            );
        }

        ArrayList<Component> scale = renderer.entityManager.getComponentsOfClassForEntity(Scale.class, sprite);
        if(scale.size() > 0) {
            ((Scale) scale.get(0)).setScale(
                    1.0f,
                    0.6f,
                    1.0f
            );
        }

        ArrayList<Component> rotation = renderer.entityManager.getComponentsOfClassForEntity(Rotation.class, sprite);
        if(rotation.size() > 0) {
            ((Rotation) rotation.get(0)).quaternion = Quaternion.from_euler_angles(
                    0.0,
                    0.0,
                    90.0
            );
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
