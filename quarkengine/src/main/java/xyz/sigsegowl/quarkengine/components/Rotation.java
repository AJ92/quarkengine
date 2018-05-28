package xyz.sigsegowl.quarkengine.components;

import xyz.sigsegowl.quarkengine.math.Matrix.Matrix4x4;
import xyz.sigsegowl.quarkengine.math.Vector.Quaternion;

/**
 * Created by AJ on 25.06.2016.
 */
public class Rotation extends Component{
    public Quaternion quaternion = new Quaternion();

    public Matrix4x4 toMatrix4x4(){
        Matrix4x4 mat = new Matrix4x4();
        return mat.rotate(quaternion);
    }
}
