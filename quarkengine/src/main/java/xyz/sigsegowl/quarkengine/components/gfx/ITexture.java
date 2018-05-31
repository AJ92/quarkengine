package xyz.sigsegowl.quarkengine.components.gfx;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.core.GlobalContext;

/**
 * Created by AJ on 06.06.2016.
 */

public class ITexture extends Component {
    //kind of a strange Component which manages itself and all other components...
    private static String TAG = "Texture2D";
    public int _textureID = 0; //0 is openGL's default "black" texture...
    private boolean _createdSuccessfully = false;
    public int _textureType = GLES20.GL_TEXTURE_2D;

    public int width = 0;
    public int height = 0;

    public ITexture() {

    }

    public boolean genGlTexture() {
        if (_textureID > 0) {
            Log.e(TAG, "texture slot could not be generated (there is already one!)");
            return false;
        }

        //try to allocate texture on GPU
        int gl_map[] = new int[1];
        GLES20.glGenTextures(1, gl_map, 0);
        if (gl_map[0] == 0) {
            Log.e(TAG, "texture slot could not be generated");
            return false;
        }
        _textureID = gl_map[0];
        return true;
    }

    public boolean deleteGlTexture() {
        if (_textureID <= 0) {
            return false;
        }

        int gl_map[] = new int[1];
        gl_map[0] = _textureID;
        GLES20.glDeleteTextures(1, gl_map, 0);
        _textureID = 0;
        return true;
    }

    public void setFilterSmooth() {
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    }

    public void setFilterPixelated() {
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    public void setFilterMipMapSmooth() {
        //mipmaps
        GLES20.glGenerateMipmap(_textureType);  //Generate num_mipmaps number of mipmaps here.

        //for mipmaps smooth
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
    }

    public void setFilterMipMapPixelated() {
        //mipmaps
        GLES20.glGenerateMipmap(_textureType);  //Generate num_mipmaps number of mipmaps here.

        //mipmaps pixelated
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_NEAREST);
    }

    public void setWrapRepeat(){
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(_textureType, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    }

    public boolean loadGlTextureRgba(Bitmap image){
        if(_textureID <= 0){
            return false;
        }

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(_textureType, 0, image, 0);
        if(_textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + _textureID + " could not be _loaded...");
            return false;
        }

        width = image.getWidth();
        height = image.getHeight();

        return true;
    }

    public boolean loadGlTextureRgba(byte[] image, int width, int height){
        if(_textureID <= 0){
            return false;
        }

        // Load the byte array into the bound texture.
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
        buffer.put(image);
        buffer.position(0);

        GLES20.glTexImage2D(_textureType, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);

        if(_textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + _textureID + " could not be _loaded...");
            return false;
        }

        this.width = width;
        this.height = height;

        return true;
    }

    public boolean loadGlTextureRgb(byte[] image, int width, int height){
        if(_textureID <= 0){
            return false;
        }
        // Load the byte array into the bound texture.
        ByteBuffer buffer = ByteBuffer.allocateDirect(3 * width * height);
        buffer.put(image);
        buffer.position(0);

        GLES20.glTexImage2D(_textureType, 0, GLES20.GL_RGB, width, height, 0,
                GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, buffer);

        if(_textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + _textureID + " could not be _loaded...");
            return false;
        }

        this.width = width;
        this.height = height;


        return true;
    }

    public boolean loadGlTextureRgbaEmpty(int width, int height){
        if(_textureID <= 0){
            return false;
        }

        GLES20.glTexImage2D(_textureType, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        if(_textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + _textureID + " could not be _loaded...");
            return false;
        }

        this.width = width;
        this.height = height;

        return true;
    }

    public boolean loadGlTextureRgbEmpty(int width, int height){
        if(_textureID <= 0){
            return false;
        }

        GLES20.glTexImage2D(_textureType, 0, GLES20.GL_RGB, width, height, 0,
                GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);

        if(_textureID == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + _textureID + " could not be _loaded...");
            return false;
        }

        this.width = width;
        this.height = height;

        return true;
    }


    public void bind(){
        GLES20.glBindTexture(_textureType, _textureID);
    }

    public boolean isCreatedSuccessfully(){
        return _createdSuccessfully;
    }
    public int getTextureID(){
        return _textureID;
    }
}
