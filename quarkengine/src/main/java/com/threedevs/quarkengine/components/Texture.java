package com.threedevs.quarkengine.components;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.threedevs.quarkengine.core.GlobalContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AJ on 06.06.2016.
 */

public class Texture extends Component{
    //kind of a strange Component which manages itself and all other components...

    private static String TAG = "Texture";

    private static ArrayList<Long> _texturePathIDs = new ArrayList<>();
    private static long _lowestUnassignedTexturePathID = Long.MIN_VALUE + 1;
    public static final long INVALID_TEXTUREPATHID = Long.MIN_VALUE;

    //holds all texture paths to identify if a texture was already initialized...
    private static HashMap<String, Long>    _texturePathIDByTexturePath = new HashMap<>();
    private static HashMap<Long, Bitmap>    _bitmapsByTexturePathID = new HashMap<>();
    private static HashMap<Long, Integer>   _textureIDByTexturePathID = new HashMap<>();
    private static HashMap<Long, Integer>   _instanceCountByTexturePathID = new HashMap<>();

    //Fallback solution...
    private static String _defaultBitmapPath = "bitmaps/default.png";

    //real data of Texture Component:
    private String _texturePath = "";
    private int _textureID = 0; //0 is openGL's default "black" texture...
    private Bitmap _bitmap = null;

    //the hash key
    private long _texturePathID = -1;

    private boolean _createdSuccessfully = false;

    public Texture(String texturePath){
        _texturePath = texturePath;
        if(isTextureCached()){
            if(loadCachedTexture()){
                _createdSuccessfully = true;
            }
            return;
        }

        if(!loadNewTexture()){
            //try reload with default values
            _texturePath = _defaultBitmapPath;
            if(isTextureCached()){
                if(loadCachedTexture()){
                    _createdSuccessfully = true;
                }
                return;
            }
            if(loadNewTexture()){
                _createdSuccessfully = true;
            }
        }
        else{
            _createdSuccessfully = true;
        }
    }

    private static long generateNewTexturePathID(){
        if(_lowestUnassignedTexturePathID < Long.MAX_VALUE){
            return _lowestUnassignedTexturePathID++;
        }
        for(long l = Long.MIN_VALUE + 1; l < Long.MAX_VALUE; l++ ){
            if(!_texturePathIDs.contains(l)){
                return l;
            }
        }
        Log.e(TAG, "Error: No available texturePathID");
        return INVALID_TEXTUREPATHID;
    }

    //if the _texturePathIDByTexturePath hashmap contains the texturePath
    //then the texture is cached and can be used as is...
    private boolean isTextureCached(){
        if(_texturePathIDByTexturePath.containsKey(_texturePath)){
            return true;
        }
        return false;
    }

    private boolean loadCachedTexture(){
        //get texturePathID
        Long texturePathID = _texturePathIDByTexturePath.get(_texturePath);
        if(texturePathID == null){
            return false;
        }
        _bitmap = _bitmapsByTexturePathID.get(texturePathID);
        _textureID = _textureIDByTexturePathID.get(texturePathID);
        _instanceCountByTexturePathID.put(
                texturePathID,
                _instanceCountByTexturePathID.get(texturePathID) + 1
        );

        if(_bitmap != null){
            if(_textureID >= 0){ // 0 might be a valid texture...
                _texturePathID = texturePathID;
                return true;
            }
        }
        return false;
    }

    private boolean loadNewTexture(){
        _bitmap = load_bitmap_rgba(_texturePath);
        if(_bitmap == null){
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

        if(!load_gl_texture_rgba(_textureID, _bitmap)){
            return false;
        }

        //on success insert new data into hashmaps
        _texturePathID = generateNewTexturePathID();
        _texturePathIDs.add(_texturePathID);

        _texturePathIDByTexturePath.put(_texturePath, _texturePathID);
        _bitmapsByTexturePathID.put(_texturePathID,_bitmap);
        _textureIDByTexturePathID.put(_texturePathID, _textureID);
        _instanceCountByTexturePathID.put(_texturePathID, 1); //instance count
        return true;
    }

    public boolean load_gl_texture_rgba(int slot, Bitmap image){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, slot);

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


        image.recycle();

        // Unbind from the texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        if(slot == 0){
            //looks like we couldn't load the texture and GLES gave us back the default black tex...
            Log.e(TAG,"tex slot " + slot + " could not be loaded...");
            return false;
        }
        return true;
    }

    private Bitmap load_bitmap_rgba(String path){
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
}
