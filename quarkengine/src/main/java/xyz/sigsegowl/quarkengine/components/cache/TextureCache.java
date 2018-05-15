package xyz.sigsegowl.quarkengine.components.cache;

import android.util.Log;

import xyz.sigsegowl.quarkengine.components.gfx.Texture;

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
    private static HashMap<Integer, Texture>   _textureObjectByCachekey = new HashMap<>();


    //if the _cachekeyByTexturePath hashmap contains the texturePath
    //then the texture is cached and can be used as is...
    public static boolean isTextureCached(String bitmapPath){
        if(_cachekeyByTexturePath.containsKey(bitmapPath)){
            return true;
        }
        return false;
    }

    //returns a valid Texture object reference or null if not cached yet...
    private static Texture loadCachedTexture(String bitmapPath){
        //get texturePathID
        Integer cachekey = _cachekeyByTexturePath.get(bitmapPath);
        if(cachekey == null){
            return null;
        }

        Texture cached_texture_ref = _textureObjectByCachekey.get(cachekey);
        return cached_texture_ref;
    }

    private static Texture getDefaultTexture(){
        Texture cached_tex_ref = null;
        if(isTextureCached(Texture._defaultBitmapPath)) {
            cached_tex_ref = loadCachedTexture(Texture._defaultBitmapPath);
            if(cached_tex_ref == null){
                //FATAL ERROR
                Log.e(TAG, "could not load cached default texture!!!");
            }
            return cached_tex_ref;
        }
        Texture uncached_tex_ref = new Texture(Texture._defaultBitmapPath);
        if(uncached_tex_ref.isCreatedSuccessfully()){
            //add to cache hashmaps...
            int id = cid.generateID();
            _cachekeyByTexturePath.put(Texture._defaultBitmapPath, id);
            _textureObjectByCachekey.put(id, uncached_tex_ref);

            return uncached_tex_ref;
        }

        Log.e(TAG, "could not load default texture!!!");
        return null;
    }

    //trys to get a cached ref to an already existing texture
    //if not existing it creates a new one
    public static Texture createTexture(String bitmapPath){

        //check for cache
        if(isTextureCached(bitmapPath)){
            Texture cached_tex_ref = loadCachedTexture(bitmapPath);
            if(cached_tex_ref == null){
                Log.e(TAG, "could not load cached texture!");
                cached_tex_ref = getDefaultTexture();
            }
            return cached_tex_ref;
        }

        //load new because not cached yet...
        Texture uncached_tex_ref = new Texture(bitmapPath);
        if(uncached_tex_ref.isCreatedSuccessfully()){
            //add to cache hashmaps...
            int id = cid.generateID();
            _cachekeyByTexturePath.put(uncached_tex_ref.getTexturePath(), id);
            _textureObjectByCachekey.put(id, uncached_tex_ref);
            return uncached_tex_ref;
        }

        return getDefaultTexture();
    }
}
