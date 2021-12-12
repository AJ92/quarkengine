package xyz.sigsegowl.quarkengine.math.Vector;


import xyz.sigsegowl.quarkengine.math.Mathematics;

/**
 * Created by AJ on 24.10.2014.
 */
public class Vector3 {

    private double vec3[] = new double[3];

    //private constructor to create an object without constructing any values...
    //java sets the values to zero anyways...
    private Vector3(int value){ }

    public Vector3() {
        set_to_null();
    }

    public Vector3(double x, double y, double z) {
        this.vec3[0] = x;
        this.vec3[1] = y;
        this.vec3[2] = z;
    }

    public Vector3(double[] vec3) {
        this.vec3 = vec3;
    }

    //copy constructor
    public Vector3(Vector3 vector){
        vec3[0] = vector.x();
        vec3[1] = vector.y();
        vec3[2] = vector.z();
    }

    public void set_to_null(){
        this.vec3[0] = 0.0;
        this.vec3[1] = 0.0;
        this.vec3[2] = 0.0;
    }

    public boolean is_null(){
        return vec3[0] == 0.0 && vec3[1] == 0.0 && vec3[2] == 0.0;
    }

    //not used atm...
    public boolean is_fuzzy_null(){
        if(!Mathematics.fuzzyIsNull(vec3[0])){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(vec3[1])){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(vec3[2])){
            return false;
        }

        return true;
    }

    public void set_value(int index, double value){
        //i won't check if the index is right...
        vec3[index] = value;
    }

    public double get_value(int index){
        return vec3[index];
    }

    public double x(){
        return vec3[0];
    }

    public double y(){
        return vec3[1];
    }

    public double z(){
        return vec3[2];
    }

    public void set_x(double x){
        vec3[0] = x;
    }

    public void set_y(double y){
        vec3[1] = y;
    }

    public void set_z(double z){
        vec3[2] = z;
    }

    //pythagoras in case somebody doesn't know...
    public double length(){
        return Math.sqrt(vec3[0] * vec3[0] + vec3[1] * vec3[1] + vec3[2] * vec3[2]);
    }

    public double lengthSquared(){
        return vec3[0] * vec3[0] + vec3[1] * vec3[1] + vec3[2] * vec3[2];
    }

    public Vector3 normalized(){
        return this.divide(this.length());
    }

    public void normalize(){
        double len = this.length();
        vec3[0] /= len;
        vec3[1] /= len;
        vec3[2] /= len;
    }

    public double distance(Vector3 vector){
        Vector3 diff = this.subtract(vector);
        return diff.length();
    }


    //vector - vector ops
    public Vector3 add(Vector3 vector){
        return new Vector3(
                vec3[0] + vector.x(),
                vec3[1] + vector.y(),
                vec3[2] + vector.z()
        );
    }

    public Vector3 subtract(Vector3 vector){
        return new Vector3(
                vec3[0] - vector.x(),
                vec3[1] - vector.y(),
                vec3[2] - vector.z()
        );
    }

    public Vector3 multiply(Vector3 vector){
        return new Vector3(
                vec3[0] * vector.x(),
                vec3[1] * vector.y(),
                vec3[2] * vector.z()
        );
    }

    public Vector3 divide(Vector3 vector){
        return new Vector3(
                vec3[0] / vector.x(),
                vec3[1] / vector.y(),
                vec3[2] / vector.z()
        );
    }

    //vector value ops
    public Vector3 add(double value){
        return new Vector3(
                vec3[0] + value,
                vec3[1] + value,
                vec3[2] + value
        );
    }

    public Vector3 subtract(double value){
        return new Vector3(
                vec3[0] - value,
                vec3[1] - value,
                vec3[2] - value
        );
    }

    public Vector3 multiply(double value){
        return new Vector3(
                vec3[0] * value,
                vec3[1] * value,
                vec3[2] * value
        );
    }

    public Vector3 divide(double value){
        return new Vector3(
                vec3[0] / value,
                vec3[1] / value,
                vec3[2] / value
                );
    }

    public static double dotProduct(Vector3 v1, Vector3 v2){
        return v1.x() * v2.x() +
               v1.y() * v2.y() +
               v1.z() * v2.z();
    }

    public static Vector3 crossProduct(Vector3 v1, Vector3 v2){
        return new Vector3(
                v1.y() * v2.z() - v1.z() * v2.y(),
                v1.z() * v2.x() - v1.x() * v2.z(),
                v1.x() * v2.y() - v1.y() * v2.x()
        );
    }

    public static Vector3 normal(Vector3 v1, Vector3 v2){
        return Vector3.crossProduct(v1,v2).normalized();
    }

    public static Vector3 normal(Vector3 v1, Vector3 v2, Vector3 v3){
        return Vector3.crossProduct(v2.subtract(v1),v3.subtract(v1)).normalized();
    }

    public boolean isEqual(Vector3 vector){
        return vec3[0] == vector.x() &&
               vec3[1] == vector.y() &&
               vec3[2] == vector.z();
    }

    public boolean isFuzzyEqual(Vector3 vector){
        Vector3 diff = this.subtract(vector);
        if(!Mathematics.fuzzyIsNull(diff.x())){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(diff.y())){
            return false;
        }

        if(!Mathematics.fuzzyIsNull(diff.z())){
            return false;
        }
        return true;
    }

    public float[] getFloatArray(){
        float vector[] = new float[3];
        vector[0] = (float) vec3[0];
        vector[1] = (float) vec3[1];
        vector[2] = (float) vec3[2];

        return vector;
    }

}

