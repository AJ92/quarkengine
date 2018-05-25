package xyz.sigsegowl.quarkengine.components.gfx;

import android.opengl.GLES20;

import xyz.sigsegowl.quarkengine.components.Component;
import xyz.sigsegowl.quarkengine.math.Vector.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by AJ on 03.07.2016.
 */

public class Geometry extends Component{

    private String TAG = "MESH";

    private int _bytes_per_float = 4;

    public String _mesh_name;

    public int _triangle_count;

    //pointer to float arrays
    float _vertices[];
    float _texcoords[];
    float _normals[];


    public FloatBuffer _vertex_pos_buffer;
    public FloatBuffer _tex_coord_buffer;
    public FloatBuffer _normal_buffer;

    //vertex buffer objects (_vertices, _texcoords and _normals)
    public int _vertex_vbo;
    public int _texcoord_vbo;
    public int _normal_vbo;

    public int _vbs[] = new int[3];

    int _vertex_array_object;

    boolean _loaded;

    //for frustum culling
    //the bounding sphere in model space
    Vector3 _bounding_sphere_position;
    double _bounding_sphere_radius;


    public Geometry(
            String name,
            int triangle_count,
            float vertices[],
            float texcoords[],
            float normals[]
    )
    {
        _loaded = false;
        _mesh_name = name;

        this._triangle_count = triangle_count;

        this._vertices = vertices;
        this._texcoords = texcoords;
        this._normals = normals;
    }

    //Mesh::~Mesh(){
    public void Mesh_destroy(){
        //delete material;

        /*
        material = NULL;
        glDeleteBuffers(1,&_vertex_vbo);
        glDeleteBuffers(1,&_texcoord_vbo);
        glDeleteBuffers(1,&_normal_vbo);
        _vertex_vbo = 0;
        _texcoord_vbo = 0;
        _normal_vbo = 0;
        delete [] _vertices;
        delete [] _texcoords;
        delete [] _normals;
        _vertices = NULL;
        _texcoords = NULL;
        _normals = NULL;
        */
    }

    public String get_name(){
        return _mesh_name;
    }

    public int get_triangle_count(){
        return _triangle_count;
    }

    public float[] get_vertices(){
        return _vertices;
    }

    public float[] get_texcoords(){
        return _texcoords;
    }

    public float[] get_normals(){
        return _normals;
    }

    public int get_vertex_vbo(){
        return _vbs[0];//_vertex_vbo;
    }

    public int get_texcoord_vbo(){
        return _vbs[1];//_texcoord_vbo;
    }

    public int get_normal_vbo(){
        return _vbs[2];//_normal_vbo;
    }

    public int get_vertex_array_object(){
        return _vertex_array_object;
    }

    public void loadGLdata(){
        //don't load again
        if(_loaded){
            return;
        }


        //detailed comments for _vertices:
        //generate buffers on gpu
        GLES20.glGenBuffers(3, _vbs,0);


        // Allocate a direct block of memory on the native heap,
        // size in bytes is equal to cubePositions.length * BYTES_PER_FLOAT.
        // BYTES_PER_FLOAT is equal to 4, since a float is 32-bits, or 4 bytes.
        _vertex_pos_buffer = ByteBuffer.allocateDirect(_vertices.length * _bytes_per_float)

                // Floats can be in big-endian or little-endian order.
                // We want the same as the native platform.
                .order(ByteOrder.nativeOrder())

                // Give us a floating-point view on this byte buffer.
                .asFloatBuffer();

        //Transferring data from the Java heap to the native heap is then a matter of a couple calls:
        // Copy data from the Java heap to the native heap.
        _vertex_pos_buffer.put(_vertices)

                // Reset the buffer position to the beginning of the buffer.
                .position(0);




        //Bind the _vertices buffer and give OpenGL the data
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _vbs[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _triangle_count * 3* 3 * _bytes_per_float, _vertex_pos_buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        //glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, 0);
        //glEnableVertexAttribArray(0);



        _tex_coord_buffer = ByteBuffer.allocateDirect(_texcoords.length * _bytes_per_float).order(ByteOrder.nativeOrder()).asFloatBuffer();
        _tex_coord_buffer.put(_texcoords).position(0);

        /*
        for(int i = 0; i < _texcoords.length; i+=3){
            Debugger.warning(TAG, "texcoord: " + _texcoords[i] + " " + _texcoords[i+1] + " " + _texcoords[i+2]);
        }
        */

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _vbs[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _triangle_count * 3* 3 * _bytes_per_float, _tex_coord_buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        //glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, 0);
        //glEnableVertexAttribArray(1);



        //_normals
        _normal_buffer = ByteBuffer.allocateDirect(_normals.length * _bytes_per_float).order(ByteOrder.nativeOrder()).asFloatBuffer();
        _normal_buffer.put(_normals).position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _vbs[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, _triangle_count * 3* 3 * _bytes_per_float, _normal_buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        //glVertexAttribPointer(2, 3, GL_FLOAT, GL_FALSE, 0, 0);
        //glEnableVertexAttribArray(2);



        //qDebug("_loaded mesh!");

        _loaded = true;

    }

    public boolean isLoaded(){
        return _loaded;
    }

    public void set_vertex(int index, float x, float y, float z){

        float vertex[] = new float[3];
        vertex[0] = x;
        vertex[1] = y;
        vertex[2] = z;

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _vertex_vbo);
        //GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, sizeof(float)*3*index, sizeof(float)*3, vertex);
        //construct a Buffer

        FloatBuffer vertexBuf = FloatBuffer.wrap(vertex);

        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, _bytes_per_float *3*index, _bytes_per_float *3, vertexBuf);

        _vertices[index*3]   = x;
        _vertices[index*3+1] = y;
        _vertices[index*3+2] = z;
    }

    public void set_vertex(int index, Vector3 vector){
        set_vertex(index, (float) vector.x(), (float) vector.y(), (float) vector.z());
    }

    public Vector3 get_vertex(int index){
        return new Vector3(
                _vertices[index*3],
                _vertices[index*3+1],
                _vertices[index*3+2]);
    }


    public Vector3 getBoundingSpherePos(){
        return _bounding_sphere_position;
    }

    public double getBoundingSphereRadius(){
        return _bounding_sphere_radius;
    }

    public void setBoundingSpherePos(Vector3 pos){
        this._bounding_sphere_position = pos;
    }

    public void setBoundingSphereRadius(double radius){
        this._bounding_sphere_radius = radius;
    }


}
