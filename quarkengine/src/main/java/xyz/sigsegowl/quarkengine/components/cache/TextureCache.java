package xyz.sigsegowl.quarkengine.components.cache;

import android.util.Log;

import xyz.sigsegowl.quarkengine.components.gfx.ITexture;
import xyz.sigsegowl.quarkengine.components.gfx.Texture2D;

import java.util.HashMap;

/**
 * Created by AJ on 25.06.2016.
 */
public class TextureCache {

    private static String TAG = "TextureCache";

    //manages ids for identifiable components
    //generally used as a cache-key generator and manager
    private static ComponentIdentifier cid = new ComponentIdentifier();

    //holds all texture paths to identify if a texture was already initialized...
    private static HashMap<String, Integer>    _cachekeyByTexturePath = new HashMap<>();
    private static HashMap<Integer, ITexture>   _textureObjectByCachekey = new HashMap<>();

    public static void clear(){
        _cachekeyByTexturePath.clear();
        _textureObjectByCachekey.clear();
    }

    //if the _cachekeyByTexturePath hashmap contains the texturePath
    //then the texture is cached and can be used as is...
    public static boolean isTexture2DCached(String bitmapPath){
        if(_cachekeyByTexturePath.containsKey(bitmapPath)){
            return true;
        }
        return false;
    }

    //returns a valid Texture2D object reference or null if not cached yet...
    private static ITexture loadCachedTexture2D(String bitmapPath){
        //get texturePathID
        Integer cachekey = _cachekeyByTexturePath.get(bitmapPath);
        if(cachekey == null){
            return null;
        }

        ITexture cached_texture_2D_ref = _textureObjectByCachekey.get(cachekey);
        return cached_texture_2D_ref;
    }

    private static ITexture getDefaultTexture2D(){
        ITexture cached_tex_ref = null;
        if(isTexture2DCached(Texture2D._defaultBitmapPath)) {
            cached_tex_ref = loadCachedTexture2D(Texture2D._defaultBitmapPath);
            if(cached_tex_ref == null){
                //FATAL ERROR
                Log.e(TAG, "could not load cached default texture!!!");
            }
            return cached_tex_ref;
        }
        ITexture uncached_tex_ref = new Texture2D(Texture2D._defaultBitmapPath);
        if(uncached_tex_ref.isCreatedSuccessfully()){
            //add to cache hashmaps...
            int id = cid.generateID();
            _cachekeyByTexturePath.put(Texture2D._defaultBitmapPath, id);
            _textureObjectByCachekey.put(id, uncached_tex_ref);

            return uncached_tex_ref;
        }

        Log.e(TAG, "could not load default texture!!!");
        return null;
    }

    //trys to get a cached ref to an already existing texture
    //if not existing it creates a new one
    public static ITexture createTexture2D(String bitmapPath){

        //check for cache
        if(isTexture2DCached(bitmapPath)){
            ITexture cached_tex_ref = loadCachedTexture2D(bitmapPath);
            if(cached_tex_ref == null){
                Log.e(TAG, "could not load cached texture!");
                cached_tex_ref = getDefaultTexture2D();
            }
            return cached_tex_ref;
        }

        //load new because not cached yet...
        ITexture uncached_tex_ref = new Texture2D(bitmapPath);
        if(uncached_tex_ref.isCreatedSuccessfully()){
            //add to cache hashmaps...
            int id = cid.generateID();
            _cachekeyByTexturePath.put(bitmapPath, id);
            _textureObjectByCachekey.put(id, uncached_tex_ref);
            return uncached_tex_ref;
        }

        return getDefaultTexture2D();
    }
}
