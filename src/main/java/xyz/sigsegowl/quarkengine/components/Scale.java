package xyz.sigsegowl.quarkengine.components;

import xyz.sigsegowl.quarkengine.math.Matrix.Matrix4x4;
import xyz.sigsegowl.quarkengine.math.Vector.Vector3;

/**
 * Created by AJ on 19.09.2016.
 */

public class Scale extends Component {

    public float _scale_x;
    public float _scale_y;
    public float _scale_z;

    public Scale(){
        _scale_x = 0.f;
        _scale_y = 0.f;
        _scale_z = 0.f;
    }

    public Scale(float x, float y, float z){
        _scale_x = x;
        _scale_y = y;
        _scale_z = z;
    }

    public Scale(Vector3 vector){
        _scale_x = (float)vector.x();
        _scale_y = (float)vector.y();
        _scale_z = (float)vector.z();
    }

    public void setScale(float x, float y, float z){
        _scale_x = x;
        _scale_y = y;
        _scale_z = z;
    }

    public void setScale(Vector3 vector){
        _scale_x = (float)vector.x();
        _scale_y = (float)vector.y();
        _scale_z = (float)vector.z();
    }

    public float x(){
        return _scale_x;
    }

    public float y(){
        return _scale_y;
    }

    public float z(){
        return _scale_z;
    }

    public Vector3 toVector3(){
        return new Vector3(_scale_x, _scale_y, _scale_z);
    }

    public Matrix4x4 toMatrix4x4(){
        Matrix4x4 mat = new Matrix4x4();
        mat.scale(_scale_x, _scale_y, _scale_z);
        return mat;
    }
}
