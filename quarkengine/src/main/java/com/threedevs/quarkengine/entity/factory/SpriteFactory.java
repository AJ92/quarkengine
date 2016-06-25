package com.threedevs.quarkengine.entity.factory;

import com.threedevs.quarkengine.components.Meta;
import com.threedevs.quarkengine.components.Position;
import com.threedevs.quarkengine.components.Rotation;
import com.threedevs.quarkengine.components.Shader;
import com.threedevs.quarkengine.components.Texture;
import com.threedevs.quarkengine.core.OpenGLEngine;
import com.threedevs.quarkengine.entity.Entity;
import com.threedevs.quarkengine.entity.EntityManager;

/**
 * Created by AJ on 25.06.2016.
 */
public class SpriteFactory {

    //the renderer has specified default shaders and bitmaps
    //as fallback solutions, so we need to provide a ref
    //to create sprites with default rendering assets and comps...
    private OpenGLEngine renderer = null;
    private EntityManager em = null;

    public SpriteFactory(OpenGLEngine renderer, EntityManager em){
        this.renderer = renderer;
        this.em = em;
    }

    public Entity createSprite(Texture bitmap){
        Entity e = em.createEntity();
        em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        em.addComponentToEntity(new Rotation(), e);
        em.addComponentToEntity(new Position(), e);
        em.addComponentToEntity(renderer.getDefaultShader(), e);
        em.addComponentToEntity(bitmap, e);
        return e;
    }

    public Entity createSprite(Texture bitmap, Shader shader){
        Entity e = em.createEntity();
        em.addComponentToEntity(new Meta("sprite-" + e.eid()), e);
        em.addComponentToEntity(new Rotation(), e);
        em.addComponentToEntity(new Position(), e);
        em.addComponentToEntity(shader, e);
        em.addComponentToEntity(bitmap, e);
        return e;
    }

    //convenience constructor
    public static SpriteFactory initWithRendererAndEntityManager(
            OpenGLEngine renderer,
            EntityManager em){
        return new SpriteFactory(renderer, em);
    }
}