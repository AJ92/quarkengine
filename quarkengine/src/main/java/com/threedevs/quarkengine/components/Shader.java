package com.threedevs.quarkengine.components;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.threedevs.quarkengine.core.GlobalContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by AJ on 01.11.2014.
 *
 * only invoke this if there is an openGL ES 2.0 context!!!
 *
 * creates an openGL Shaderprogramm out of vertex and fragment shader sources
 */
public class Shader extends Component{

    boolean _created_successfully = false;
    String _vertex_shader_path = "";
    String _vertex_shader_source = "";
    int _vertex_shader_id = -1;
    String _fragment_shader_path = "";
    String _fragment_shader_source = "";
    int _fragment_shader_id = -1;
    String _error_string = "";
    int _program_id = -1;

    private String TAG = "Shader";



    public Shader(String vertexShaderPath,String fragmentShaderPath) {
        _created_successfully = false;
        _vertex_shader_path = vertexShaderPath;
        _fragment_shader_path = fragmentShaderPath;
        _error_string = "None";


        //load the shader source code.
        _vertex_shader_source = load_shader_source(_vertex_shader_path);
        if(_vertex_shader_source.equals("")){
            _error_string = "vertex shader source is empty";
            return;
        }

        _fragment_shader_source = load_shader_source(_fragment_shader_path);
        if(_fragment_shader_source.equals("")){
            _error_string = "fragment shader source is empty";
            return;
        }


        //create the shader, load the source and compile it
        _vertex_shader_id = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(_vertex_shader_id, _vertex_shader_source);
        GLES20.glCompileShader(_vertex_shader_id);

        //check if we could compile the stuff...
        int compileStatus_vertex[] = new int[1];
        GLES20.glGetShaderiv(_vertex_shader_id, GLES20.GL_COMPILE_STATUS, compileStatus_vertex, 0);
        if (compileStatus_vertex[0] != 0){
            _error_string = "compiled successfully";
        }
        else{
            _error_string = "vertex shader compile failed...\n" +
                    "Path: " + _vertex_shader_path + "\n" +
                    GLES20.glGetShaderInfoLog(_vertex_shader_id);
            GLES20.glDeleteShader(_vertex_shader_id);
            _created_successfully = false;
            return;
        }


        //create the shader, load the source and compile it
        _fragment_shader_id = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(_fragment_shader_id, _fragment_shader_source);
        GLES20.glCompileShader(_fragment_shader_id);

        //check if we could compile the stuff...
        int compileStatus_fragment[] = new int[1];
        GLES20.glGetShaderiv(_fragment_shader_id, GLES20.GL_COMPILE_STATUS, compileStatus_fragment, 0);
        if (compileStatus_fragment[0] != 0){
            _error_string = "compiled successfully";
        }
        else{
            _error_string = "fragment shader compile failed...\n" +
                    "Path: " + _fragment_shader_path + "\n" +
                    GLES20.glGetShaderInfoLog(_fragment_shader_id);
            GLES20.glDeleteShader(_fragment_shader_id);
            _created_successfully = false;
            return;
        }


        //Link the shaders to a program
        _program_id = GLES20.glCreateProgram();
        if (_program_id != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(_program_id, _vertex_shader_id);
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(_program_id, _fragment_shader_id);
            // Bind attribute
            //GLES20.glBindAttribLocation(programLine, 0, "a_Position");
            // Link the two shaders together into a program.
            GLES20.glLinkProgram(_program_id);
            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(_program_id, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(_program_id);
                _program_id = -1;
                _error_string = "linking failed...";
                _created_successfully = false;
                return;
            }
        }

        if (_program_id <= 0) {
            throw new RuntimeException("Error creating shader program:\n"+
            "Vertex shader path: " + _vertex_shader_path + "\n" +
            "Fragment shader path: " + _fragment_shader_path + "\n");
        }

        _error_string = "success";
        _created_successfully = true;
    }

    public void destroy(){
        //TODO:
        //destroy the shader...
    }



    public boolean isCreatedSuccessfully(){
        return _created_successfully;
    }

    public String getError(){
        return _error_string;
    }


    private String load_shader_source(String path){
        //Shader file and it's context as string
        AssetManager am = GlobalContext.getAppContext().getAssets();
        InputStream is = null;
        try {
            is = am.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            _error_string = "Error 1: InputStream could not be initialized...";
            Log.e(TAG, _error_string);
            _created_successfully = false;
            return "";
        }

        InputStreamReader isr = new InputStreamReader(is);
        try {
            if(!isr.ready()){
                _error_string = "Error 2: InputStreamReader could not be initialized...";
                Log.e(TAG, _error_string);
                _created_successfully = false;
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            _error_string = "Error 2: InputStreamReader could not be initialized...";
            Log.e(TAG, _error_string);
            _created_successfully = false;
            return "";
        }
        BufferedReader file= new BufferedReader(isr);
        try {
            if (!file.ready())
            {
                _error_string = "Error 3: Shader file could not be loaded...";
                Log.e(TAG, _error_string);
                _created_successfully = false;
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            _error_string = "Error 3: Shader file could not be loaded...";
            Log.e(TAG, _error_string);
            _created_successfully = false;
            return "";
        }

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        try {
            while((line = file.readLine())!=null){
                stringBuffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            _error_string = "Error 4: Shader file line could not be read...";
            Log.e(TAG, _error_string);
            _created_successfully = false;
            return "";
        }

        return stringBuffer.toString();
    }

}
