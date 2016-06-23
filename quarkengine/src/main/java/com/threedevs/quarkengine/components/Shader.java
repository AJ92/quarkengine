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

    boolean m_created_successfully = false;
    String m_vertex_shader_path = "";
    String m_vertex_shader_source = "";
    int m_vertex_shader_id = -1;
    String m_fragment_shader_path = "";
    String m_fragment_shader_source = "";
    int m_fragment_shader_id = -1;
    String m_error_string = "";
    int m_program_id = -1;

    private String TAG = "Shader";



    public Shader(String vertexShaderPath,String fragmentShaderPath) {
        m_created_successfully = false;
        m_vertex_shader_path = vertexShaderPath;
        m_fragment_shader_path = fragmentShaderPath;
        m_error_string = "None";


        //load the shader source code.
        m_vertex_shader_source = load_shader_source(m_vertex_shader_path);
        if(!m_created_successfully){
            return;
        }

        m_fragment_shader_source = load_shader_source(m_fragment_shader_path);
        if(!m_created_successfully){
            return;
        }


        //create the shader, load the source and compile it
        m_vertex_shader_id = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource( m_vertex_shader_id, m_vertex_shader_source);
        GLES20.glCompileShader(m_vertex_shader_id);

        //check if we could compile the stuff...
        int compileStatus_vertex[] = new int[1];
        GLES20.glGetShaderiv(m_vertex_shader_id, GLES20.GL_COMPILE_STATUS, compileStatus_vertex, 0);
        if (compileStatus_vertex[0] != 0){
            m_error_string = "compiled successfully";
        }
        else{
            m_error_string = "vertex shader compile failed...\n" +
                    "Path: " + m_vertex_shader_path + "\n" +
                    GLES20.glGetShaderInfoLog(m_vertex_shader_id);
            GLES20.glDeleteShader(m_vertex_shader_id);
            m_created_successfully = false;
            return;
        }


        //create the shader, load the source and compile it
        m_fragment_shader_id = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource( m_fragment_shader_id, m_fragment_shader_source);
        GLES20.glCompileShader(m_fragment_shader_id);

        //check if we could compile the stuff...
        int compileStatus_fragment[] = new int[1];
        GLES20.glGetShaderiv(m_fragment_shader_id, GLES20.GL_COMPILE_STATUS, compileStatus_fragment, 0);
        if (compileStatus_fragment[0] != 0){
            m_error_string = "compiled successfully";
        }
        else{
            m_error_string = "fragment shader compile failed...\n" +
                    "Path: " + m_fragment_shader_path + "\n" +
                    GLES20.glGetShaderInfoLog(m_fragment_shader_id);
            GLES20.glDeleteShader(m_fragment_shader_id);
            m_created_successfully = false;
            return;
        }


        //Link the shaders to a program
        m_program_id = GLES20.glCreateProgram();
        if (m_program_id != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(m_program_id, m_vertex_shader_id);
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(m_program_id, m_fragment_shader_id);
            // Bind attribute
            //GLES20.glBindAttribLocation(programLine, 0, "a_Position");
            // Link the two shaders together into a program.
            GLES20.glLinkProgram(m_program_id);
            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(m_program_id, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(m_program_id);
                m_program_id = -1;
                m_error_string = "linking failed...";
                m_created_successfully = false;
                return;
            }
        }
        m_error_string = "success";
        m_created_successfully = true;
    }

    public void destroy(){
        //TODO:
        //destroy the shader...
    }



    public boolean isCreatedSuccessfully(){
        return m_created_successfully;
    }

    public String getError(){
        return m_error_string;
    }


    private String load_shader_source(String path){
        //Shader file and it's context as string
        AssetManager am = GlobalContext.getAppContext().getAssets();
        InputStream is = null;
        try {
            is = am.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error 1: InputStream could not be initialized...");
        }
        InputStreamReader isr = new InputStreamReader(is);
        try {
            if(!isr.ready()){
                Log.e(TAG, "Error 2: InputStreamReader could not be initialized...");
                m_created_successfully = false;
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error 2: InputStreamReader could not be initialized...");
            m_created_successfully = false;
            return "";
        }
        BufferedReader file= new BufferedReader(isr);
        try {
            if (!file.ready())
            {
                Log.e(TAG,"Error 3: Shader file could not be loaded...");
                m_created_successfully = false;
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error 3: Shader file could not be loaded...");
            m_created_successfully = false;
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
            Log.e(TAG,"Error 4: Shader file line could not be read...");
            m_created_successfully = false;
            return "";
        }

        return stringBuffer.toString();
    }

}
