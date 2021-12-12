package xyz.sigsegowl.quarkengine.core;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * Simple interface for handling the OpenGL surface and renderer.
 */
public class RenderInterface {

    private final String TAG = "RenderInterface";
    private GLSurfaceView mGLView;

    public RenderInterface(){
    }

    /**
     * Method for creating the surface where the OpenGL drawing will happen.
     * Notably we set the transparent options here.
     *
     * @param renderView The view to use.
     */
    public void onCreate(GLSurfaceView renderView) {
        mGLView = renderView;
        mGLView.setEGLContextClientVersion(2);
        mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        //mGLView.setRenderer(new OpenGLEngine(mainInterface));
        mGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGLView.setZOrderMediaOverlay(true);
    }

    public void onResume() {
        mGLView.onResume();
    }

    public void onPause() {
        mGLView.onPause();
    }
}
