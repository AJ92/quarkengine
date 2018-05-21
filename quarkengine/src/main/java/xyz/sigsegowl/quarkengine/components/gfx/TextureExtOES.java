package xyz.sigsegowl.quarkengine.components.gfx;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.core.GlobalContext;

/**
 * Created by AJ on 21.05.2018.
 */

public class TextureExtOES extends ITexture {
    //kind of a strange Component which manages itself and all other components...
    private static String TAG = "TextureExtOES";

    public int _textureID = 0; //0 is openGL's default "black" texture...
    private Bitmap _bitmap = null;


    private boolean _createdSuccessfully = false;

    public TextureExtOES(){
        if(!genGlTexture()) {
            return;
        }

        if(!loadGlTextureExternalOES(_textureID)) {
            return;
        }

        _createdSuccessfully = true;

    }

    public boolean genGlTexture(){
        if(_textureID > 0){
            Log.e(TAG, "texture slot could not be generated (there is already one!)");
            return false;
        }

        //try to allocate texture on GPU
        int gl_map[] = new int[1];
        GLES20.glGenTextures(1, gl_map, 0);
        if(gl_map[0] == 0){
            Log.e(TAG, "texture slot could not be generated");
            return false;
        }
        _textureID = gl_map[0];
        return true;
    }

    public boolean deleteGlTexture(){
        if(_textureID <= 0){
            return false;
        }

        int gl_map[] = new int[1];
        gl_map[0] = _textureID;
        GLES20.glDeleteTextures(1, gl_map, 0);
        _textureID = 0;
        return true;
    }

    public boolean loadGlTextureExternalOES(int textureID){
        if(textureID <= 0){
            return false;
        }

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureID);

        //pixelated
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


        if(textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + textureID + " could not be _loaded...");
            return false;
        }
        return true;
    }

    public void bind(){
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, _textureID);
    }

    public boolean isCreatedSuccessfully(){
        return _createdSuccessfully;
    }

    public int getTextureID(){
        return _textureID;
    }
}
