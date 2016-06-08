package com.threedevs.quarkengine.core;


import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.Calendar;

/**
 * Created by AJ on 14.11.2014.
 */

//TODO: create a callback from the openGL renderer to this view and try to set up NodeView
public class OpenGLSurfaceView extends GLSurfaceView {

    private final OpenGLEngine renderer;
    private ScaleGestureDetector sgd;
    private String TAG = "OpenGLSurfaceView";

    public OpenGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        renderer = new OpenGLEngine();

        setRenderer(renderer);

        // old : TRANSLUCENT  possible fix for sigseg fault...
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setZOrderMediaOverlay(true);



        // Render the view all the time...
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        sgd = new ScaleGestureDetector(context, new ScaleListener());
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            /*
            mPreviousScale *= (1.0f / detector.getScaleFactor());
            mPreviousScale = Math.max(min_scale, Math.min(mPreviousScale, max_scale));
            */
            return true;
        }
    }


}
