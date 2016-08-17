package com.threedevs.quarkengine.components;

import com.threedevs.quarkengine.math.Vector.Vector3;

/**
 * Created by AJ on 20.06.2016.
 */
public class Position extends Component {

    public float _pos_x;
    public float _pos_y;
    public float _pos_z;

    public Position(){
        _pos_x = 0.f;
        _pos_y = 0.f;
        _pos_z = 0.f;
    }

    public Position(float x, float y, float z){
        _pos_x = x;
        _pos_y = y;
        _pos_z = z;
    }

    public Position(Vector3 vector){
        _pos_x = (float)vector.x();
        _pos_y = (float)vector.y();
        _pos_z = (float)vector.z();
    }

    public void setPos(float x, float y, float z){
        _pos_x = x;
        _pos_y = y;
        _pos_z = z;
    }

    public void setPos(Vector3 vector){
        _pos_x = (float)vector.x();
        _pos_y = (float)vector.y();
        _pos_z = (float)vector.z();
    }

    public float x(){
        return _pos_x;
    }

    public float y(){
        return _pos_y;
    }

    public float z(){
        return _pos_z;
    }

    public Vector3 toVector3(){
        return new Vector3(_pos_x, _pos_y, _pos_z);
    }
}
