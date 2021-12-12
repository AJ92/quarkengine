package xyz.sigsegowl.quarkengine.core;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;


import xyz.sigsegowl.quarkengine.components.Camera;
import xyz.sigsegowl.quarkengine.components.cache.TextureCache;
import xyz.sigsegowl.quarkengine.components.gfx.Geometry;
import xyz.sigsegowl.quarkengine.components.gfx.Shader;
import xyz.sigsegowl.quarkengine.components.gfx.Texture2D;
import xyz.sigsegowl.quarkengine.entity.EntityManager;
import xyz.sigsegowl.quarkengine.entity.factory.SpriteFactory;
import xyz.sigsegowl.quarkengine.math.Matrix.Matrix4x4;
import xyz.sigsegowl.quarkengine.math.Vector.Vector3;
import xyz.sigsegowl.quarkengine.systems.SpriteRenderSystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Handles the rendering of the objects.
 */
public class OpenGLEngine implements GLSurfaceView.Renderer {

    private final String TAG = "OpenGLEngine";

    //Entity manager
    public EntityManager entityManager;
    //factories for entities
    public SpriteFactory spriteFactory;
    //systems
    public SpriteRenderSystem spriteRenderSystem;

    //default resources and assets
    Shader shader_simple = null;
    Texture2D texture_2D_simple = null;
    Geometry sprite_geometry = null;

    boolean FATAL_ERROR = false;


    private int programLine;
    private int programSimple;

    //simple
    private int locMVPMatrixSimple;
    private int locPositionSimple;
    private int locTexCoordSimple;
    private int locNormalSimple;
    private int locTextureSimple;

    //line
    private int locMVPMatrixLine;
    private int locPositionLine;
    private int locColorLine;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private final int mPositionOffset = 0;
    private final int mPositionDataSize = 3;
    private final int mColorOffset = 3;
    private final int mColorDataSize = 4;
    private final int mBytesPerFloat = 4;
    private final int mStrideBytes = 7 * mBytesPerFloat;


    //---------------------- new and better stuff ----------------------------

    //window dimensions, will be used to place models and more...
    //coords are retreived from onSurfaceChanged...
    private int window_size_x = 0;
    private int window_size_y = 0;


    //DEFAULT MODEL to render in case we have broken models or so...
    //TODO:

    private Camera default_cam;

    private Matrix4x4 ortho_m = new Matrix4x4();

    //all the matrices we need... pre init them to identity
    private Matrix4x4 p_m = new Matrix4x4();
    private Matrix4x4 v_m = new Matrix4x4();
    private Matrix4x4 m_m = new Matrix4x4();
    private Matrix4x4 vm_m = new Matrix4x4();
    private Matrix4x4 pv_m = new Matrix4x4();
    private Matrix4x4 pvm_m = new Matrix4x4();


    private float render_mat[] = new float[16];


    private OpenGLSurfaceView surfaceView;

    protected OpenGLEngine(OpenGLSurfaceView sv) {
        surfaceView = sv;
    }


    //TODO: create some kind of destructor to clean up gpu resource...


    /**
     * Called when a surface is created.
     *
     * @param gl     The unused context.
     * @param config Unused configuration options.
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");

        TextureCache.clear();

        //Entity manager
        entityManager = new EntityManager();
        //factories for entities
        spriteFactory = SpriteFactory.initWithRendererAndEntityManager(this, entityManager);
        //systems
        spriteRenderSystem = new SpriteRenderSystem(entityManager);

        //purple 148,0,211
        GLES20.glClearColor(0.0f/256.0f, 0.0f/256.0f, 0.0f/256.0f, 0.0f);
        // GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;
        // Set our up vector.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Create shader prog:
        createShaders();

        createBasicAssets();

        spriteRenderSystem.setRenderer(this);

        surfaceView.onInit();
    }




    public void eventFrame(){

    }

    public void drawWorld(){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        //Log.e(TAG, "drawWorld");
        spriteRenderSystem.update(0.0f);
    }

    public void postProcess(){

    }

    public void drawUi(){

    }


    /**
     * @param gl Unused context.
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        surfaceView.onPreDrawFrame();

        GLES20.glViewport(0, 0, window_size_x, window_size_y);

        eventFrame();
        drawWorld();

        surfaceView.onPostDrawFrame();
        postProcess();

        drawUi();



        /*
        if(default_cam != null) {
            //create the projection matrix...
            p_m.set_to_identity();
            p_m = p_m.perspective(default_cam.getFOV(), (float) (window_size_x) / (float) (window_size_y),
                    default_cam.getZNEAR(), default_cam.getZFAR());


            // Clear Buffers:
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);



            // ------------------------ RENDER ------------------------


            //      ------------------- simple shader -----------------






                //        RENDER MODE SIMPLE

                if(ow.getStoreMode() == ObjectWorld.store_mode_simple) {

                    //MODELS
                    GLES20.glEnableVertexAttribArray(locPositionSimple);
                    GLES20.glEnableVertexAttribArray(locTexCoordSimple);
                    GLES20.glEnableVertexAttribArray(locNormalSimple);
                    for (CompositeObject co : ow.getCompositeObjectsModels()) {

                        if (co.hasModel() && (co.getRenderType() == CompositeObject.render_standard)) {

                            Positation posi = co.getPositation();

                            m_m = posi.get_model_matrix();
                            //vm_m = v_m.multiply(m_m);
                            pvm_m = pv_m.multiply(m_m);


                            drawModel(co.getModel());
                        }
                    }
                    GLES20.glDisableVertexAttribArray(locPositionSimple);
                    GLES20.glDisableVertexAttribArray(locTexCoordSimple);
                    GLES20.glDisableVertexAttribArray(locNormalSimple);
                    //MODELS DONE
                }

                //        RENDER MODE SIMPLE SORTED

                else if(ow.getStoreMode() == ObjectWorld.store_mode_simple_sorted){
                    //get all the data first
                    ArrayList<ArrayList<CompositeObject> > compositeObjects_mesh_list_simple_sorted = ow.getCompositeObjects_mesh_list_simple_sorted();
                    ArrayList<ArrayList<Mesh> > mesh_model_simple_sorted = ow.getMesh_model_simple_sorted();
                    ArrayList<ArrayList<Model> > model_mesh_simple_sorted = ow.getModel_mesh_simple_sorted();
                    ArrayList<Material> material_mesh_simple_sorted = ow.getMaterial_mesh_simple_sorted();



                    //MODELS
                    GLES20.glEnableVertexAttribArray(locPositionSimple);
                    GLES20.glEnableVertexAttribArray(locTexCoordSimple);
                    GLES20.glEnableVertexAttribArray(locNormalSimple);

                    GLES20.glUseProgram(programSimple);

                    int mtl_index = 0;
                    for (Material mtl : material_mesh_simple_sorted) {
                        //set up the texture
                        // Set the active texture unit to texture unit 0.
                        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                        // Bind the texture to this unit.
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mtl.get_diffuse_map_texture());
                        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
                        GLES20.glUniform1i(locTextureSimple, 0);



                        int co_index = 0;
                        for(CompositeObject co : compositeObjects_mesh_list_simple_sorted.get(mtl_index)) {

                            if (co.hasModel() && (co.getRenderType() == CompositeObject.render_standard)) {

                                Positation posi = co.getPositation();

                                m_m = posi.get_model_matrix();
                                //vm_m = v_m.multiply(m_m);
                                pvm_m = pv_m.multiply(m_m);


                                // Pass in the position information

                                Mesh mesh = mesh_model_simple_sorted.get(mtl_index).get(co_index);



                                if (mesh.isLoaded()) {

                                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_vertex_vbo());
                                    //actually if once enabled we dont need to enable again.... but safety first xD
                                    //GLES20.glEnableVertexAttribArray(locPositionSimple);
                                    GLES20.glVertexAttribPointer(locPositionSimple, 3, GLES20.GL_FLOAT, false, 0, 0);

                                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_texcoord_vbo());
                                    //GLES20.glEnableVertexAttribArray(locTexCoordSimple);
                                    GLES20.glVertexAttribPointer(locTexCoordSimple, 3, GLES20.GL_FLOAT, false, 0, 0);

                                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_normal_vbo());
                                    //GLES20.glEnableVertexAttribArray(locNormalSimple);
                                    GLES20.glVertexAttribPointer(locNormalSimple, 3, GLES20.GL_FLOAT, false, 0, 0);


                                    //TRANSPOSE
                                    render_mat = pvm_m.getFloatArray(true);

                                    GLES20.glUniformMatrix4fv(locMVPMatrixSimple, 1, false, render_mat, 0);
                                    if (mesh.get_triangle_count() == 0) {
                                        return;
                                    }
                                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.get_triangle_count() * 3);
                                }

                            }
                            co_index += 1;
                        }
                        //Debugger.error(TAG,"saved " + (co_index - 1) + "texture bindings");
                        mtl_index += 1;
                    }
                    GLES20.glDisableVertexAttribArray(locPositionSimple);
                    GLES20.glDisableVertexAttribArray(locTexCoordSimple);
                    GLES20.glDisableVertexAttribArray(locNormalSimple);
                    //MODELS DONE



                }



                //LINES
                GLES20.glEnableVertexAttribArray(locPositionLine);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                for (CompositeObject co : ow.getCompositeObjectsLines()) {
                    if (co.hasLine() && (co.getRenderType() == CompositeObject.render_standard)) {
                        Positation posi = co.getPositation();
                        pvm_m = pv_m.multiply(posi.get_model_matrix());
                        drawLine(co.getLine());
                    }
                }




                //render gizmo

                //convert it to space coords
                Vector3 gizmo_space_pos = touch_to_space(default_cam, 100, window_size_y - 100);
                //scale the pos (move away from origin)
                Vector3 gizmo_space_pos_scaled = gizmo_space_pos.multiply( default_cam.getZNEAR() + 6.0);
                //move away from camera pos (the position we want to render our model at...)
                Vector3 gizmo_final_space_pos = default_cam.getPosition().add(gizmo_space_pos_scaled);

                gizmo_posi.set_position(gizmo_final_space_pos);


                v_m = default_cam.get_view_matrix();
                pv_m = p_m.multiply(v_m);

                pvm_m = pv_m.multiply(gizmo_posi.get_model_matrix());

                if(gizmo_mode == OpenGLSurfaceView.moveMode) {
                    //gizmo for move mode
                    for (Line line_gizmo : gizmo_move) {
                        drawLine(line_gizmo);
                    }
                }
                else if(gizmo_mode == OpenGLSurfaceView.rotMode) {
                    //gizmo for rotate mode
                    for (Line line_gizmo : gizmo_rotate) {
                        drawLine(line_gizmo);
                    }
                }
                else if(gizmo_mode == OpenGLSurfaceView.scaleMode) {
                    //gizmo for scale mode
                    for (Line line_gizmo : gizmo_scale) {
                        drawLine(line_gizmo);
                    }
                }

                GLES20.glDisableVertexAttribArray(locPositionLine);
                //LINES DONE






            }


            GLES20.glDisableVertexAttribArray(locPositionLine);
        }
        */

    }






    /**
     * Called whenever the draw surface is changed – most notably on creation
     *
     * @param gl     Unused context.
     * @param width  Width in pixel of canvas.
     * @param height Height in pixel of canvas.
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        window_size_x = width;
        window_size_y = height;

        GLES20.glViewport(0, 0, width, height);
    }





    /*
    private void drawModel(Model mdl){
        GLES20.glUseProgram(programSimple);

        // Pass in the position information

        ArrayList<Mesh> mesh_list = mdl.get_meshs();

        for(Mesh mesh: mesh_list) {

            if(mesh.isLoaded()) {

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_vertex_vbo());
                //actually if once enabled we dont need to enable again.... but safety first xD
                //GLES20.glEnableVertexAttribArray(locPositionSimple);
                GLES20.glVertexAttribPointer(locPositionSimple, 3, GLES20.GL_FLOAT, false, 0, 0);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_texcoord_vbo());
                //GLES20.glEnableVertexAttribArray(locTexCoordSimple);
                GLES20.glVertexAttribPointer(locTexCoordSimple, 3, GLES20.GL_FLOAT, false, 0, 0);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mesh.get_normal_vbo());
                //GLES20.glEnableVertexAttribArray(locNormalSimple);
                GLES20.glVertexAttribPointer(locNormalSimple, 3, GLES20.GL_FLOAT, false, 0, 0);

                // Set the active texture unit to texture unit 0.
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                // Bind the texture to this unit.
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mesh.get_material().get_diffuse_map_texture());
                // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
                GLES20.glUniform1i(locTextureSimple, 0);


                // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
                // (which currently contains model * view).
                //Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

                // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
                // (which now contains model * view * projection).
                //Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);


                //TRANSPOSE

                render_mat = pvm_m.getFloatArray(true);


                GLES20.glUniformMatrix4fv(locMVPMatrixSimple, 1, false, render_mat, 0);
                // The (1+data.capacity() / 8) tells us how many vertices we need to
                // draw

                if(mesh.get_triangle_count() == 0){
                    return;
                }

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.get_triangle_count()*3);
            }
        }
    }


    private void drawLine(Line line){

        GLES20.glUseProgram(programLine);

        // Enable a handle to the triangle vertices
        //GLES20.glEnableVertexAttribArray(locPositionLine);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(locPositionLine, line.getCoordsPerVertex(),
                GLES20.GL_FLOAT, false,
                line.getVertexStride(), line.getVertexBuffer());

        // Set color for drawing the triangle
        GLES20.glUniform4fv(locColorLine, 1, line.getColor(), 0);

        render_mat = pvm_m.getFloatArray(true);


        GLES20.glUniformMatrix4fv(locMVPMatrixLine, 1, false, render_mat, 0);


        GLES20.glLineWidth(5f);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, line.getVertexCount());
    }
    */



    private void createBasicAssets(){

        //create basic camera...
        Camera cam = new Camera();
        cam.set_position(0.0,0.0,0.0);
        cam.setZFAR(300.0);
        default_cam = cam;


        texture_2D_simple = new Texture2D("bitmaps/default.png");

        float vertices[] = {
                1.0f, -1.0f, 0.0f,  //triangle 1
                -1.0f, -1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f,

                -1.0f, 1.0f, 0.0f,  //triangle 2
                1.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
        };
        float texcoords[] = {
                1.0f, 1.0f, 0.0f,  //triangle 1
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f,

                0.0f, 0.0f, 0.0f,  //triangle 2
                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f
        };
        float normals[] = {
                0.0f, 0.0f, 1.0f,  //triangle 1
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                0.0f, 0.0f, 1.0f,  //triangle 2
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f
        };

        sprite_geometry = new Geometry(
                "2d-plane",
                2, //triangle count
                vertices,
                texcoords,
                normals
        );

        sprite_geometry.loadGLdata();

        //creates it's own ModelLoader
        //simple storage mode is a linear unsorted list of models!!!
        //ow = new ObjectWorld(ObjectWorld.store_mode_simple);

        /*
        //TEST XML PARSER (after ow construction... cause we need ow...)
        XMLLoadTaskAsync xmllta = new XMLLoadTaskAsync();
        // add the renderer as listener so once xml and the process is ready we can
        //construct a 3D representation of it
        xmllta.addXMLProcessLoadedListener(this);
        //xmllta.retreiveXMLFromAssets("test.xml");
        xmllta.retreiveXMLFromAssets("test.xml");
        */

        /*
        for(int y = 0; y < 10; y++) {
            for(int x = 0; x < 10; x++) {
                test_marker_co = ow.loadModelObject("box", "box.obj", true);
                test_marker_co.getPositation().set_position(x * 4.0,- y * 4.0, 0.0);

                test_marker_text_co = ow.loadModelObject("test_text", "text_model.obj", false);
                //crazy chain...
                test_marker_text_co.getModel().get_meshs().get(0).get_material().setDiffuseText(
                        "FF00FF_TEXT_BG.png", "Node", 100.0f, 0, 255, 0
                );

                //move to the right
                test_marker_text_co.getPositation().set_position(0.5f + x * 4.0, 0.0f - y * 4.0, 1.02f);

                //shadow
                CompositeObject test_marker_text_co2 = ow.loadModelObject("test_text", "text_model.obj", false);
                //crazy chain...
                test_marker_text_co2.getModel().get_meshs().get(0).get_material().setDiffuseText(
                        "FF00FF_TEXT_BG.png", "Node", 100.0f, 0, 0, 0
                );

                //move to the right
                test_marker_text_co2.getPositation().set_position(0.51f + x * 4.0, 0.01f - y * 4.0, 1.01f);
            }
        }
        */




    }


    public Vector3 touch_to_space(Camera cam, int x,int y){
        Matrix4x4 M_projection = new Matrix4x4();
        M_projection = M_projection.perspective(cam.getFOV(),(float)(window_size_x) / (float)(window_size_y),cam.getZNEAR(),cam.getZFAR());

        Matrix4x4 camera_view_projection_m = M_projection.multiply(cam.get_view_matrix());

        Matrix4x4 inv_cam_view_projection = camera_view_projection_m.inverted();
        /*
        double touch_x = -((double)(x)/((double)(window_size_x)*0.5)-1.0);
        double touch_y = ((double)(y)/((double)(window_size_y)*0.5)-1.0);
        */
        double touch_x = ((double)(x)/((double)(window_size_x)*0.5)-1.0);
        double touch_y = -((double)(y)/((double)(window_size_y)*0.5)-1.0);

        /*
        Log.e(TAG, Double.toString(touch_x) + "  " +
                Double.toString(touch_y));
        */
        Vector3 projected_pos_near = inv_cam_view_projection.multiply(new Vector3( cam.getZNEAR() * touch_x, cam.getZNEAR() * touch_y, cam.getZNEAR()) );
        Vector3 projected_pos_far  = inv_cam_view_projection.multiply(new Vector3( cam.getZFAR() * touch_x, cam.getZFAR() * touch_y, cam.getZFAR()) );
        Vector3 projected_pos = projected_pos_far.subtract(projected_pos_near);
        Vector3 projected_pos_normalized = projected_pos.normalized();
        projected_pos_normalized.set_x(-projected_pos_normalized.x());
        projected_pos_normalized.set_y(-projected_pos_normalized.y());
        projected_pos_normalized.set_z(-projected_pos_normalized.z());
        return projected_pos_normalized;
    }




    /**
     * Method for creating the shader programs we need to render our geometry...
     * No magic involved
     */
    private void createShaders() {
        //v2 shaders
        //load shader and create a program out of it...
        shader_simple = new Shader("shaders/simple.vsh", "shaders/simple.fsh");
        if(!shader_simple.isCreatedSuccessfully()){
            Log.e(TAG, shader_simple.getError());
            FATAL_ERROR = true;
            return;
        }




        /*
        //---------------LINE SHADER-------------------------
        //load shader and create a program out of it...
        Shader vertex_shader_line = new Shader("shaders/line.vsh",GLES20.GL_VERTEX_SHADER);
        if(!vertex_shader_line.isCreated()){
            Log.e(TAG, vertex_shader_line.getError());
        }
        Shader fragment_shader_line = new Shader("shaders/line.fsh",GLES20.GL_FRAGMENT_SHADER);
        if(!fragment_shader_line.isCreated()){
            Log.e(TAG, fragment_shader_line.getError());
        }



        // Create a program object and store the handle to it.
        programLine = GLES20.glCreateProgram();

        if (programLine != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programLine, vertex_shader_line.getShaderId());

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programLine, fragment_shader_line.getShaderId());

            // Bind attribute
            GLES20.glBindAttribLocation(programLine, 0, "a_Position");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programLine);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programLine, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programLine);
                programLine = 0;
            }
        }

        if (programLine == 0) {
            throw new RuntimeException("Error creating simple shader program.");
        }

        // Set program handles. These will later be used to pass in values to the program.
        locMVPMatrixLine = GLES20.glGetUniformLocation(programLine, "u_MVPMatrix");
        locPositionLine = GLES20.glGetAttribLocation(programLine, "a_Position");



        locColorLine = GLES20.glGetUniformLocation(programLine, "u_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programLine);










        //---------------SIMPLE SHADER-----------------------


        //load shader and create a program out of it...
        Shader vertex_shader_simple = new Shader("shaders/simple.vsh",GLES20.GL_VERTEX_SHADER);
        if(!vertex_shader_simple.isCreated()){
            Log.e(TAG, vertex_shader_simple.getError());
        }
        Shader fragment_shader_simple = new Shader("shaders/simple.fsh",GLES20.GL_FRAGMENT_SHADER);
        if(!fragment_shader_simple.isCreated()){
            Log.e(TAG, fragment_shader_simple.getError());
        }



        // Create a program object and store the handle to it.
        programSimple = GLES20.glCreateProgram();

        if (programSimple != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programSimple, vertex_shader_simple.getShaderId());

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programSimple, fragment_shader_simple.getShaderId());

            // Bind attributes
            GLES20.glBindAttribLocation(programSimple, 0, "a_Position");
            GLES20.glBindAttribLocation(programSimple, 1, "a_TexCoord");
            GLES20.glBindAttribLocation(programSimple, 2, "a_Normal");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programSimple);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programSimple, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programSimple);
                programSimple = 0;
            }
        }

        if (programSimple == 0) {
            throw new RuntimeException("Error creating simple shader program.");
        }

        // Set program handles. These will later be used to pass in values to the program.
        locMVPMatrixSimple = GLES20.glGetUniformLocation(programSimple, "u_MVPMatrix");
        locPositionSimple = GLES20.glGetAttribLocation(programSimple, "a_Position");
        locTexCoordSimple = GLES20.glGetAttribLocation(programSimple, "a_TexCoord");
        locNormalSimple = GLES20.glGetAttribLocation(programSimple, "a_Normal");

        locTextureSimple = GLES20.glGetUniformLocation(programSimple, "tex_sampler");


        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programSimple);
        */
    }

    public Shader getDefaultShader(){
        return shader_simple;
    }

    public Geometry getDefaultSpriteGeometry() {
        return sprite_geometry;
    }

    public int getSurfaceWidth(){
        return window_size_x;
    }

    public int getSurfaceheight(){
        return window_size_y;
    }
}
