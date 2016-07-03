package com.threedevs.quarkengine.entity.factory;

import com.threedevs.quarkengine.components.Meta;
import com.threedevs.quarkengine.components.Position;
import com.threedevs.quarkengine.components.Rotation;
import com.threedevs.quarkengine.components.Sprite;
import com.threedevs.quarkengine.components.gfx.Shader;
import com.threedevs.quarkengine.components.gfx.Texture;
import com.threedevs.quarkengine.components.cache.TextureCache;
import com.threedevs.quarkengine.core.OpenGLEngine;
import com.threedevs.quarkengine.entity.Entity;
import com.threedevs.quarkengine.entity.EntityManager;

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
    using the bitmap Texture provided.
     */
    public Entity createSprite(Texture bitmap){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(_renderer.getDefaultShader(), bitmap), e);
        return e;
    }

    /*
    Creates a sprite entity with a default shader for bitmaps
    using path to load a bitmap Texture which might be an instance
    if already loaded one.
     */
    public Entity createSprite(String bitmapPath){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(_renderer.getDefaultShader(), TextureCache.createTexture(bitmapPath)), e);
        return e;
    }

    /*
    Creates a sprite entity with a custom shader for bitmaps
    using the bitmap Texture provided.
     */
    public Entity createSprite(Texture bitmap, Shader shader){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(shader, bitmap), e);
        return e;
    }

    /*
    Creates a sprite entity with a custom shader for bitmaps
    using path to load a bitmap Texture which might be an instance
    if already loaded one.
     */
    public Entity createSprite(String bitmapPath, Shader shader){
        Entity e = _em.createEntity();
        _em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        _em.addComponentToEntity(new Rotation(), e);
        _em.addComponentToEntity(new Position(), e);
        _em.addComponentToEntity(new Sprite(shader, TextureCache.createTexture(bitmapPath)), e);
        return e;
    }

    //convenience constructor
    public static SpriteFactory initWithRendererAndEntityManager(
            OpenGLEngine renderer,
            EntityManager em){
        return new SpriteFactory(renderer, em);
    }
}
