package xyz.sigsegowl.quarkengine.entity.factory;


import xyz.sigsegowl.quarkengine.components.Meta;
import xyz.sigsegowl.quarkengine.components.Position;
import xyz.sigsegowl.quarkengine.components.Rotation;
import xyz.sigsegowl.quarkengine.components.Sprite;
import xyz.sigsegowl.quarkengine.components.gfx.ITexture;
import xyz.sigsegowl.quarkengine.components.gfx.Shader;
import xyz.sigsegowl.quarkengine.components.gfx.Texture2D;
import xyz.sigsegowl.quarkengine.components.cache.TextureCache;
import xyz.sigsegowl.quarkengine.core.OpenGLEngine;
import xyz.sigsegowl.quarkengine.entity.Entity;
import xyz.sigsegowl.quarkengine.entity.EntityManager;

/**
 * Created by AJ on 25.06.2016.
 */
public class SpriteFactory {

    //the _renderer has specified default shaders and bitmaps
    //as fallback solutions, so we need to provide a ref
    //to create sprites with default rendering assets and comps...
    private OpenGLEngine _renderer = null;
    private EntityManager _em = null;

    public SpriteFactory(OpenGLEngine renderer, EntityManager em){
        this._renderer = renderer;
        this._em = em;
    }

    /*
    Creates a sprite entity with a default shader for bitmaps
    using the bitmap Texture2D provided.
     */
    public Entity createSprite(ITexture bitmap){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Scale(), e);
        _em.addComponentToEntity(new Sprite(_renderer.getDefaultShader(), bitmap), e);
        return e;
    }

    /*
    Creates a sprite entity with a default shader for bitmaps
    using path to load a bitmap Texture2D which might be an instance
    if already loaded one.
     */
    public Entity createSprite(String bitmapPath){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(_renderer.getDefaultShader(), TextureCache.createTexture2D(bitmapPath)), e);
        return e;
    }

    /*
    Creates a sprite entity with a custom shader for bitmaps
    using the bitmap Texture2D provided.
     */
    public Entity createSprite(ITexture bitmap, Shader shader){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Scale(), e);
        _em.addComponentToEntity(new Sprite(shader, bitmap), e);
        return e;
    }

    /*
    Creates a sprite entity with a custom shader for bitmaps
    using path to load a bitmap Texture2D which might be an instance
    if already loaded one.
     */
    public Entity createSprite(String bitmapPath, Shader shader){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(shader, TextureCache.createTexture2D(bitmapPath)), e);
        return e;
    }

    //convenience constructor
    public static SpriteFactory initWithRendererAndEntityManager(
            OpenGLEngine renderer,
            EntityManager em){
        return new SpriteFactory(renderer, em);
    }
}
