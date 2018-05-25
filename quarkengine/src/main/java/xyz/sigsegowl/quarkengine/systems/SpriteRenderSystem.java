package xyz.sigsegowl.quarkengine.systems;

import android.opengl.GLES20;
import android.util.Log;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.components.Position;
import xyz.sigsegowl.quarkengine.components.Rotation;
import xyz.sigsegowl.quarkengine.components.Sprite;
import xyz.sigsegowl.quarkengine.components.gfx.Geometry;
import xyz.sigsegowl.quarkengine.components.gfx.ITexture;
import xyz.sigsegowl.quarkengine.components.gfx.Shader;
import xyz.sigsegowl.quarkengine.core.OpenGLEngine;
import xyz.sigsegowl.quarkengine.entity.Entity;
import xyz.sigsegowl.quarkengine.entity.EntityManager;
import xyz.sigsegowl.quarkengine.math.Matrix.Matrix4x4;

import java.util.ArrayList;



/**
 * Created by AJ on 02.07.2016.
 */
public class SpriteRenderSystem extends System {

    private OpenGLEngine _renderer = null;
    private static final String TAG = "SpriteRenderSystem";
    private int changes = 0;
    private Shader last_shader = null;
    private ITexture last_texture = null;

    private long times = 0;
    private int sprites = 0;
    private int texBinds = 0;
    private int times_count = 0;

    private ArrayList<Entity> _sprite_entities = new ArrayList<>();
    ArrayList<Component> _sprite_components = new ArrayList<>();
    ArrayList<Component> _pos_components = new ArrayList<>();
    ArrayList<Component> _scale_components = new ArrayList<>();
    ArrayList<Component> _rot_components = new ArrayList<>();

    public SpriteRenderSystem(EntityManager em) {
        super(em);
    }

    public void setRenderer(OpenGLEngine renderer){
        _renderer = renderer;
    }

    @Override
    public void update(float dt){

        long startTime = java.lang.System.nanoTime();

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

        if(changes != _entityManager.getChanges()) {

            Log.w(TAG, "updating sprite dataset...");
            changes = _entityManager.getChanges();

            _sprite_entities.clear();
            _sprite_components.clear();
            _pos_components.clear();
            _scale_components.clear();
            _rot_components.clear();

            ArrayList<Entity> sprite_entities = _entityManager.getAllEntitiesPossesingCompoenetOfClass(Sprite.class);

            //Log.e(TAG, "Sprite count: " + sprite_entities.size());

            for (int i = 0; i < sprite_entities.size(); i++) {
                ArrayList<Component> sprite_components = _entityManager.getComponentsOfClassForEntity(Sprite.class, sprite_entities.get(i));
                ArrayList<Component> pos_components = _entityManager.getComponentsOfClassForEntity(Position.class, sprite_entities.get(i));
                ArrayList<Component> scale_components = _entityManager.getComponentsOfClassForEntity(Scale.class, sprite_entities.get(i));
                ArrayList<Component> rot_components = _entityManager.getComponentsOfClassForEntity(Rotation.class, sprite_entities.get(i));

                //draw single sprite object

                if (sprite_components.size() < 1) {
                    return;
                }
                if (pos_components.size() < 1) {
                    return;
                }
                if (scale_components.size() < 1) {
                    return;
                }
                if (rot_components.size() < 1) {
                    return;
                }

                _sprite_entities.add(sprite_entities.get(i));
                _sprite_components.add(sprite_components.get(0));
                _pos_components.add(pos_components.get(0));
                _scale_components.add(scale_components.get(0));
                _rot_components.add(rot_components.get(0));
            }
        }

        Matrix4x4 mvp = new Matrix4x4();
        for(int i = 0; i < _sprite_entities.size(); i++) {


            Sprite spriteData = (Sprite)_sprite_components.get(i);

            Shader shader = (Shader)spriteData.getShader();
            ITexture texture = (ITexture)spriteData.getTexture();

            int locPosition = GLES20.glGetAttribLocation(shader._program_id, "a_Position");
            int locTexcoord = GLES20.glGetAttribLocation(shader._program_id, "a_TexCoord");
            int locNormal = GLES20.glGetAttribLocation(shader._program_id, "a_Normal");
            int locTexture = GLES20.glGetUniformLocation(shader._program_id, "tex_sampler");
            int locMVPMatrix = GLES20.glGetUniformLocation(shader._program_id, "u_MVPMatrix");


            if(last_shader != shader) {
                GLES20.glUseProgram(shader._program_id);

                GLES20.glEnableVertexAttribArray(locPosition);
                GLES20.glEnableVertexAttribArray(locTexcoord);
                GLES20.glEnableVertexAttribArray(locNormal);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo._vbs[0]);
                //actually if once enabled we dont need to enable again.... but safety first xD
                //GLES20.glEnableVertexAttribArray(locPositionSimple);
                GLES20.glVertexAttribPointer(locPosition, 3, GLES20.GL_FLOAT, false, 0, 0);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo._vbs[1]);
                //GLES20.glEnableVertexAttribArray(locTexCoordSimple);
                GLES20.glVertexAttribPointer(locTexcoord, 3, GLES20.GL_FLOAT, false, 0, 0);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geo._vbs[2]);
                //GLES20.glEnableVertexAttribArray(locNormalSimple);
                GLES20.glVertexAttribPointer(locNormal, 3, GLES20.GL_FLOAT, false, 0, 0);

                last_shader = shader;
            }

            if(last_texture != texture) {
                // Set the active texture unit to texture unit 0.
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                // Bind the texture to this unit.
                texture.bind();
                // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
                GLES20.glUniform1i(locTexture, 0);
                last_texture = texture;
                texBinds += 1;
            }

            Position pos = (Position)_pos_components.get(i);
            mvp.translate(pos._pos_x, pos._pos_y, pos._pos_z);

            Scale scale = (Scale)_scale_components.get(i);
            mvp.scale(scale._scale_x, scale._scale_y, scale._scale_z);
            mvp.getFloatArray(render_mat, true);

            GLES20.glUniformMatrix4fv(locMVPMatrix, 1, false, render_mat, 0);

            if(geo.get_triangle_count() == 0){
                return;
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geo._triangle_count*3);

            sprites += 1;
        }

        long estimatedTime = java.lang.System.nanoTime() - startTime;
        times += estimatedTime;
        times_count += 1;

        if(times_count >= 60) {
            Log.w(TAG, "update took ~ " + times / 60.0 / 1000000.0);
            Log.w(TAG, "sprites drawn ~ " + sprites / 60.0 );
            Log.w(TAG, "textures bound ~ " + texBinds / 60.0 );
            times = 0;
            sprites = 0;
            texBinds = 0;
            times_count = 0;
        }

        last_texture = null; // reset last tex
    }



}
