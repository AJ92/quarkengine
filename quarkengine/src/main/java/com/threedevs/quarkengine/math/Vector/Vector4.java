package com.threedevs.quarkengine.math.Vector;


import com.threedevs.quarkengine.math.Mathematics;

/**
 * Created by AJ on 24.10.2014.
 */
public class Vector4 {

    private double vec4[] = new double[4];

    //private constructor to create an object without constructing any values...
    //java sets the values to zero anyways...
    private Vector4(int value){ }

    public Vector4() {
        set_to_null();
    }

    public Vector4(double x, double y, double z, double w) {
        this.vec4[0] = x;
        this.vec4[1] = y;
        this.vec4[2] = z;
        this.vec4[3] = w;
    }

    public Vector4(double[] vec4) {
        this.vec4 = vec4;
    }

    //copy constructor
    public Vector4(Vector4 vector){
        vec4[0] = vector.x();
        vec4[1] = vector.y();
        vec4[2] = vector.z();
        vec4[3] = vector.w();
    }

    public void set_to_null(){
        this.vec4[0] = 0.0;
        this.vec4[1] = 0.0;
        this.vec4[2] = 0.0;
        this.vec4[3] = 0.0;
    }

    public boolean is_null(){
        return vec4[0] == 0.0 &&
               vec4[1] == 0.0 &&
               vec4[2] == 0.0 &&
               vec4[3] == 0.0;
    }

    //not used atm...
    public boolean is_fuzzy_null(){
        if(!Mathematics.fuzzyIsNull(vec4[0])){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(vec4[1])){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(vec4[2])){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(vec4[3])){
            return false;
        }

        return true;
    }

    public void set_value(int index, double value){
        //i won't check if the index is right...
        vec4[index] = value;
    }

    public double get_value(int index){
        return vec4[index];
    }

    public double x(){
        return vec4[0];
    }

    public double y(){
        return vec4[1];
    }

    public double z(){
        return vec4[2];
    }

    public double w(){
        return vec4[3];
    }

    public void set_x(double x){
        vec4[0] = x;
    }

    public void set_y(double y){
        vec4[1] = y;
    }

    public void set_z(double z){
        vec4[2] = z;
    }

    public void set_w(double w){
        vec4[3] = w;
    }

    //pythagoras in case somebody doesn't know...
    public double length(){
        return Math.sqrt(vec4[0] * vec4[0] +
                         vec4[1] * vec4[1] +
                         vec4[2] * vec4[2] +
                         vec4[3] * vec4[3]);
    }

    public double lengthSquared(){
        return vec4[0] * vec4[0] +
               vec4[1] * vec4[1] +
               vec4[2] * vec4[2] +
               vec4[3] * vec4[3];
    }

    public Vector4 normalized(){
        return this.divide(this.length());
    }

    public void normalize(){
        double len = this.length();
        vec4[0] /= len;
        vec4[1] /= len;
        vec4[2] /= len;
        vec4[3] /= len;
    }

    public double distance(Vector4 vector){
        Vector4 diff = this.subtract(vector);
        return diff.length();
    }


    //vector - vector ops
    public Vector4 add(Vector4 vector){
        return new Vector4(
                vec4[0] + vector.x(),
                vec4[1] + vector.y(),
                vec4[2] + vector.z(),
                vec4[3] + vector.w()
        );
    }

    public Vector4 subtract(Vector4 vector){
        return new Vector4(
                vec4[0] - vector.x(),
                vec4[1] - vector.y(),
                vec4[2] - vector.z(),
                vec4[3] - vector.w()
        );
    }

    public Vector4 multiply(Vector4 vector){
        return new Vector4(
                vec4[0] * vector.x(),
                vec4[1] * vector.y(),
                vec4[2] * vector.z(),
                vec4[3] * vector.w()
        );
    }

    public Vector4 divide(Vector4 vector){
        return new Vector4(
                vec4[0] / vector.x(),
                vec4[1] / vector.y(),
                vec4[2] / vector.z(),
                vec4[3] / vector.w()
        );
    }

    //vector value ops
    public Vector4 add(double value){
        return new Vector4(
                vec4[0] + value,
                vec4[1] + value,
                vec4[2] + value,
                vec4[3] + value
        );
    }

    public Vector4 subtract(double value){
        return new Vector4(
                vec4[0] - value,
                vec4[1] - value,
                vec4[2] - value,
                vec4[3] - value
        );
    }

    public Vector4 multiply(double value){
        return new Vector4(
                vec4[0] * value,
                vec4[1] * value,
                vec4[2] * value,
                vec4[3] * value
        );
    }

    public Vector4 divide(double value){
        return new Vector4(
                vec4[0] / value,
                vec4[1] / value,
                vec4[2] / value,
                vec4[3] / value
        );
    }

    public static double dotProduct(Vector4 v1, Vector4 v2){
        return v1.x() * v2.x() +
               v1.y() * v2.y() +
               v1.z() * v2.z() +
               v1.w() * v2.w();
    }

    public boolean isEqual(Vector4 vector){
        return vec4[0] == vector.x() &&
               vec4[1] == vector.y() &&
               vec4[2] == vector.z() &&
               vec4[3] == vector.w();
    }

    public boolean isFuzzyEqual(Vector4 vector){
        Vector4 diff = this.subtract(vector);
        if(!Mathematics.fuzzyIsNull(diff.x())){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(diff.y())){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(diff.z())){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(diff.w())){
            return false;
        }
        return true;
    }

    public Vector3 vector3(){
        return new Vector3(this.x(), this.y(), this.z());
    }

    public float[] getFloatArray(){
        float vector[] = new float[4];
        vector[0] = (float) vec4[0];
        vector[1] = (float) vec4[1];
        vector[2] = (float) vec4[2];
        vector[3] = (float) vec4[3];

        return vector;
    }

}
