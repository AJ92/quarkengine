package xyz.sigsegowl.quarkengine.systems;

import android.opengl.GLES20;
import android.util.Log;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.components.Position;
import xyz.sigsegowl.quarkengine.components.Rotation;
import xyz.sigsegowl.quarkengine.components.Scale;
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
    private Geometry geometry = null;
    private Shader last_shader = null;
    private ITexture last_texture = null;

    private float render_mat[] = new float[16];
    private Matrix4x4 mvp = new Matrix4x4();

    private long times = 0;
    private int sprites = 0;
    private int texBinds = 0;
    private int times_count = 0;

    private ArrayList<Entity> _sprite_entities = new ArrayList<>();

    public SpriteRenderSystem(EntityManager em) {
        super(em);
    }

    public void setRenderer(OpenGLEngine renderer){
        _renderer = renderer;
        geometry = _renderer.getDefaultSpriteGeometry();
    }

    @Override
    public void update(float dt){
        long startTime = java.lang.System.nanoTime();

        //prep scene for object rendering...
        if(changes != _entityManager.getChanges()) {
            Log.w(TAG, "updating sprite entity list...");
            changes = _entityManager.getChanges();
            _sprite_entities = _entityManager.getAllEntitiesPossesingCompoenetOfClass(Sprite.class);
        }

        for(int i = 0; i < _sprite_entities.size(); i++) {
            Entity e = _sprite_entities.get(i);
            if(drawSpriteEntity(e)) {
                sprites += 1;
            }
            else{
                Log.e(TAG, "sprite skipped! Entity("+e.eid()+")");
            }
        }

        long estimatedTime = java.lang.System.nanoTime() - startTime;
        times += estimatedTime;
        times_count += 1;

        if(times_count >= 60) {
            Log.w(TAG, String.format("update took ~ %.4f ns", times / 60.0 / 1000000000.0));
            Log.w(TAG, String.format("sprites drawn ~ %.2f", sprites / 60.0));
            Log.w(TAG, String.format("textures bound ~ %.2f", texBinds / 60.0));
            times = 0;
            sprites = 0;
            texBinds = 0;
            times_count = 0;
        }

        last_texture = null; // reset last tex
    }

    public boolean drawSpriteEntity(Entity e){
        if(_renderer == null){
            return false;
        }
        if(geometry == null){
            return false;
        }

        Sprite sprite       = null;
        ArrayList<Component> sprite_components = _entityManager.getComponentsOfClassForEntity(Sprite.class, e);
        if (sprite_components.size() > 0) {
            sprite = (Sprite) sprite_components.get(0);
        }
        if(sprite == null){
            return false;
        }

        Position position   = null;
        ArrayList<Component> pos_components = _entityManager.getComponentsOfClassForEntity(Position.class, e);
        if (pos_components.size() > 0) {
            position = (Position) pos_components.get(0);
        }
        if(position == null){
            return false;
        }

        Scale scale         = null;
        ArrayList<Component> scale_components = _entityManager.getComponentsOfClassForEntity(Scale.class, e);
        if (scale_components.size() > 0) {
            scale = (Scale) scale_components.get(0);
        }
        if(scale == null){
            return false;
        }

        Rotation rotation   = null;
        ArrayList<Component> rot_components = _entityManager.getComponentsOfClassForEntity(Rotation.class, e);
        if (rot_components.size() > 0) {
            rotation = (Rotation) rot_components.get(0);
        }
        if(rotation == null){
            return false;
        }

        Shader shader       = (Shader) sprite.getShader();
        ITexture texture    = (ITexture)sprite.getTexture();

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

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometry._vbs[0]);
            //actually if once enabled we dont need to enable again.... but safety first xD
            //GLES20.glEnableVertexAttribArray(locPositionSimple);
            GLES20.glVertexAttribPointer(locPosition, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometry._vbs[1]);
            //GLES20.glEnableVertexAttribArray(locTexCoordSimple);
            GLES20.glVertexAttribPointer(locTexcoord, 3, GLES20.GL_FLOAT, false, 0, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometry._vbs[2]);
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

        mvp.set_to_identity();
        mvp.scale(scale._scale_x, scale._scale_y, scale._scale_z);
        mvp = mvp.rotate(rotation.quaternion);
        mvp.translate(position._pos_x, position._pos_y, position._pos_z);
        mvp.getFloatArray(render_mat, true);

        GLES20.glUniformMatrix4fv(locMVPMatrix, 1, false, render_mat, 0);

        if(geometry.get_triangle_count() == 0){
            return false;
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometry._triangle_count*3);
        return true;
    }

}
