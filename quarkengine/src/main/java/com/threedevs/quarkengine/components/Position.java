package com.threedevs.quarkengine.components;

import com.threedevs.quarkengine.math.Vector.Vector3;

/**
 * Created by AJ on 20.06.2016.
 */
public class Position extends Component {

    private float pos_x;
    private float pos_y;
    private float pos_z;

    public Position(){
        pos_x = 0.f;
        pos_y = 0.f;
        pos_z = 0.f;
    }

    public Position(float x, float y, float z){
        pos_x = x;
        pos_y = y;
        pos_z = z;
    }

    public Position(Vector3 vector){
        pos_x = (float)vector.x();
        pos_y = (float)vector.y();
        pos_z = (float)vector.z();
    }

    public void setPos(float x, float y, float z){
        pos_x = x;
        pos_y = y;
        pos_z = z;
    }

    public void setPos(Vector3 vector){
        pos_x = (float)vector.x();
        pos_y = (float)vector.y();
        pos_z = (float)vector.z();
    }

    public float x(){
        return pos_x;
    }

    public float y(){
        return pos_y;
    }

    public float z(){
        return pos_z;
    }

    public Vector3 toVector3(){
        return new Vector3(pos_x, pos_y, pos_z);
    }
}
