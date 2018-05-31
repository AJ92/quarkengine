package xyz.sigsegowl.quarkengine.components.gfx;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.core.GlobalContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by AJ on 06.06.2016.
 */

public class Texture2D extends ITexture {
    //kind of a strange Component which manages itself and all other components...
    private static String TAG = "Texture2D";

    //Fallback solution...
    public final static String _defaultBitmapPath = "bitmaps/default.png";

    //real data of Texture2D Component:
    private String _texturePath = "";
    private Bitmap _bitmap = null;


    private boolean _createdSuccessfully = false;

    public Texture2D(String texturePath){
        _textureType = GLES20.GL_TEXTURE_2D;
        _texturePath = texturePath;
        if(loadTexture()){
            _createdSuccessfully = true;
        }
    }

    private boolean loadTexture(){
        _bitmap = loadBitmapRgba(_texturePath);
        if(_bitmap == null){
            Log.e(TAG, "could not load bitmap");
            //try the default texture...
            _bitmap = loadBitmapRgba(_defaultBitmapPath);
            if(_bitmap == null){
                Log.e(TAG, "could not load bitmap");
                return false;
            }
        }

        if(!genGlTexture()){
            Log.e(TAG, "could not generate a texture slot on gpu");
            return false;
        }

        bind();

        if(!loadGlTextureRgba(_bitmap)){
            Log.e(TAG, "could not upload bitmap to gpu");
            return false;
        }

        setFilterSmooth();

        _bitmap.recycle();

        return true;
    }


    private Bitmap loadBitmapRgba(String path){
        if(path == null){
            return null;
        }

        AssetManager assetManager = GlobalContext.getAppContext().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Rect outPadding = new Rect();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        // Read in the resource
        //image = BitmapFactory.decodeStream(istr);
        Bitmap image = BitmapFactory.decodeStream(istr, outPadding, options);

        if(image == null){
            Log.e(TAG, "Bitmap: " + path + " could not decode the Stream...");
            return null;
        }
        return image;
    }

    public boolean isCreatedSuccessfully(){
        return _createdSuccessfully;
    }

    public String getTexturePath(){
        return _texturePath;
    }

}
