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
 */
public class Shader {

    boolean created = false;
    int shader_type = 0;
    String shader_path = "";
    String error_string = "";
    String shader_source = "";
    int m_shader_id = 0;

    private String TAG = "Shader";



    public Shader(String shaderPath, int shaderType) {
        created = false;
        shader_type = shaderType;
        shader_path = shaderPath;
        error_string = "None";
        m_shader_id = 0;

        //load the shader source code.
        if(!load_shader_source(shaderPath)){
            return;
        }


        //create the shader, load the source and compile it
        m_shader_id = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource( m_shader_id, shader_source);
        GLES20.glCompileShader(m_shader_id);

        //check if we could compile the stuff...
        int compileStatus[] = new int[1];
        GLES20.glGetShaderiv(m_shader_id, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] != 0){
            error_string = "compiled successfully";
            created = true;
        }
        else{
            error_string = "compile failed...\n" +
                    "Path: " + shaderPath + "\n" +
                    GLES20.glGetShaderInfoLog(m_shader_id);
            GLES20.glDeleteShader(m_shader_id);
        }
    }

    public void destroy(){
        //TODO:
        //destroy the shader...
    }

    public String getShaderPath(){
        return shader_path;
    }

    public int getShaderId(){
        return m_shader_id;
    }

    public int getShaderType(){
        return shader_type;
    }

    public String getShaderSource(){
        return shader_source;
    }


    public boolean isCreated(){
        return created;
    }

    public String getError(){
        return error_string;
    }


    private boolean load_shader_source(String path){
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
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error 2: InputStreamReader could not be initialized...");
            return false;
        }
        BufferedReader file= new BufferedReader(isr);
        try {
            if (!file.ready())
            {
                Log.e(TAG,"Error 3: Shader file could not be loaded...");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error 3: Shader file could not be loaded...");
            return false;
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
        }

        shader_source = stringBuffer.toString();


        return true;
    }

}
