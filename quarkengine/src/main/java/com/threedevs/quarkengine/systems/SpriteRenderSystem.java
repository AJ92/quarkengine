package com.threedevs.quarkengine.systems;

import android.opengl.GLES20;

import com.threedevs.quarkengine.components.Component;
import com.threedevs.quarkengine.components.Position;
import com.threedevs.quarkengine.components.Rotation;
import com.threedevs.quarkengine.components.Sprite;
import com.threedevs.quarkengine.components.gfx.Geometry;
import com.threedevs.quarkengine.components.gfx.Shader;
import com.threedevs.quarkengine.components.gfx.Texture;
import com.threedevs.quarkengine.core.OpenGLEngine;
import com.threedevs.quarkengine.entity.Entity;
import com.threedevs.quarkengine.entity.EntityManager;
import com.threedevs.quarkengine.math.Matrix.Matrix4x4;

import java.util.ArrayList;

/**
 * Created by AJ on 02.07.2016.
 */
public class SpriteRenderSystem extends System {

    private OpenGLEngine _renderer = null;

    public SpriteRenderSystem(EntityManager em) {
        super(em);
    }

    public void setRenderer(OpenGLEngine renderer){
        _renderer = renderer;
    }

    @Override
    public void update(float dt){
        if(_renderer == null){
            return;
        }

        Geometry geo = _renderer.getDefaultSpriteGeometry();
        if(!geo.isLoaded()){
            return;
        }

        //prep scene for object rendering...

        //todo ...
        float render_mat[] = new float[16];

        ArrayList<Entity> sprite_entities = _entityManager.getAllEntitiesPossesingCompoenetOfClass(Sprite.class);
        for(int i = 0; i < sprite_entities.size(); i++) {
            ArrayList<Component> sprite_components = _entityManager.getComponentsOfClassForEntity(Sprite.class, sprite_entities.get(i));
            ArrayList<Component> pos_components = _entityManager.getComponentsOfClassForEntity(Position.class, sprite_entities.get(i));
            ArrayList<Component> rot_components = _entityManager.getComponentsOfClassForEntity(Rotation.class, sprite_entities.get(i));

            //draw single sprite object

            if(sprite_components.size() < 1){
                return;
            }
            if(pos_components.size() < 1){
                return;
            }
            if(rot_components.size() < 1){
                return;
            }

            Matrix4x4 mvp = new Matrix4x4();

            Sprite spriteData = (Sprite)sprite_components.get(0);

            int programmID = ((Shader)spriteData.getShader()).getProgramID();
            int textureID = ((Texture)spriteData.getTexture()).getTextureID();

            int locPosition = GLES20.glGetAttribLocation(programmID, "a_Position");
            int locTexcoord = GLES20.glGetAttribLocation(programmID, "a_TexCoord");
            int locNormal = GLES20.glGetAttribLocation(programmID, "a_Normal");
            int locTexture = GLES20.glGetUniformLocation(programmID, "tex_sampler");
            int locMVPMatrix = GLES20.glGetUniformLocation(programmID, "u_MVPMatrix");

            GLES20.glUseProgram(programmID);

            GLES20.glEnableVertexAttribArray(locPosition);
            GLES20.glEnableVertexAttribArray(locTexcoord);
            GLES20.glEnableVertexAttribArray(locNormal);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo.get_vertex_vbo());
            //actually if once enabled we dont need to enable again.... but safety first xD
            //GLES20.glEnableVertexAttribArray(locPositionSimple);
            GLES20.glVertexAttribPointer(locPosition, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo.get_texcoord_vbo());
            //GLES20.glEnableVertexAttribArray(locTexCoordSimple);
            GLES20.glVertexAttribPointer(locTexcoord, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo.get_normal_vbo());
            //GLES20.glEnableVertexAttribArray(locNormalSimple);
            GLES20.glVertexAttribPointer(locNormal, 3, GLES20.GL_FLOAT, false, 0, 0);

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(locTexture, 0);


            // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
            // (which currently contains model * view).
            //Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

            // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
            // (which now contains model * view * projection).
            //Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);


            mvp.translate(((Position)pos_components.get(0)).toVector3());
            render_mat = mvp.getFloatArray(true);


            GLES20.glUniformMatrix4fv(locMVPMatrix, 1, false, render_mat, 0);

            if(geo.get_triangle_count() == 0){
                return;
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geo.get_triangle_count()*3);

        }
    }



}
