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

public class Texture extends Component {
    //kind of a strange Component which manages itself and all other components...
    private static String TAG = "Texture";

    //Fallback solution...
    public final static String _defaultBitmapPath = "bitmaps/default.png";

    //real data of Texture Component:
    private String _texturePath = "";
    public int _textureID = 0; //0 is openGL's default "black" texture...
    private Bitmap _bitmap = null;


    private boolean _createdSuccessfully = false;

    public Texture(String texturePath){
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

        if(!loadGlTextureRgba(_textureID, _bitmap)){
            Log.e(TAG, "could not upload bitmap to gpu");
            return false;
        }

        _bitmap.recycle();

        return true;
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

    public boolean loadGlTextureRgba(int textureID, Bitmap image){
        if(textureID <= 0){
            return false;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        /* WORKS (but no mipmaps and no filtering)
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        */

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);

        /*
        //mipmaps
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);  //Generate num_mipmaps number of mipmaps here.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //for mipmaps smooth
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);

        //mipmaps pixelated
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_NEAREST);
        */


        //no mipmaps
        /*
        //smooth
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        */
        //pixelated
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Unbind from the texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        if(textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + textureID + " could not be _loaded...");
            return false;
        }
        return true;
    }

    public boolean loadGlTextureRgba(int textureID, byte[] image, int width, int height){
        if(textureID <= 0){
            return false;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

        // Load the byte array into the bound texture.

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
        buffer.put(image);
        buffer.position(0);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);

        //no mipmaps

        //pixelated
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Unbind from the texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        if(textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + textureID + " could not be _loaded...");
            return false;
        }
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

    public int getTextureID(){
        return _textureID;
    }
}
