package xyz.sigsegowl.quarkengine.math.Matrix;


import xyz.sigsegowl.quarkengine.math.Mathematics;

/**
 * Created by AJ on 24.10.2014.
 */

/*column-major notation for openGL use...
  but using in code row-major notation for indizes...

  following stuff is colomn-major notation


    Indizes
    |    0        3        6    |
    |                           |
    |    1        4        7    |
    |                           |
    |    2        5        8    |

*/


public class Matrix3x3 {

    private double mat3[] = new double[9];

    //binary composite type of the matrix... see Mathematics.java
    private int flagBits;

    //dummy constructor, don't do anything
    public Matrix3x3(int value){

    }

    public Matrix3x3(){
        set_to_identity();
    }

    //copy constructor
    public Matrix3x3(Matrix3x3 mat) {
        this.mat3[0] = mat.get_value(0);
        this.mat3[1] = mat.get_value(1);
        this.mat3[2] = mat.get_value(2);
        this.mat3[3] = mat.get_value(3);
        this.mat3[4] = mat.get_value(4);
        this.mat3[5] = mat.get_value(5);
        this.mat3[6] = mat.get_value(6);
        this.mat3[7] = mat.get_value(7);
        this.mat3[8] = mat.get_value(8);
        flagBits = Mathematics.mat_type_General;
    }

    public Matrix3x3(double mat3[]){
        this.mat3[0] = mat3[0];
        this.mat3[1] = mat3[1];
        this.mat3[2] = mat3[2];
        this.mat3[3] = mat3[3];
        this.mat3[4] = mat3[4];
        this.mat3[5] = mat3[5];
        this.mat3[6] = mat3[6];
        this.mat3[7] = mat3[7];
        this.mat3[8] = mat3[8];
    }

    public Matrix3x3(double f1, double f2, double f3,
                     double f4, double f5, double f6,
                     double f7, double f8, double f9){
        this.mat3[0] = f1;
        this.mat3[1] = f2;
        this.mat3[2] = f3;
        this.mat3[3] = f4;
        this.mat3[4] = f5;
        this.mat3[5] = f6;
        this.mat3[6] = f7;
        this.mat3[7] = f8;
        this.mat3[8] = f9;

        flagBits = Mathematics.mat_type_General;
    }

    public void set_to_identity(){
        mat3[0]    = mat3[4]  = mat3[8] = 1.0;
        mat3[1]    = mat3[2]  = mat3[3] = 0.0;
        mat3[5]    = mat3[6]  = mat3[7] = 0.0;
        flagBits = Mathematics.mat_type_Identity;
    }

    public boolean is_identity(){
        if (flagBits == Mathematics.mat_type_Identity)
            return true;
        if (mat3[0] != 1.0 || mat3[1] != 0.0 || mat3[2] != 0.0)
            return false;
        if (mat3[3] != 0.0 || mat3[4] != 1.0 || mat3[5] != 0.0)
            return false;
        if (mat3[6] != 0.0 || mat3[7] != 0.0)
            return false;
        return (mat3[8] == 1.0f);
    }

    public boolean is_Fuzzy_identity(){
        for(int i = 0; i < 9; i++){
            if(i == 0 || i == 4 || i == 8){
                if(!Mathematics.fuzzyIsNull(mat3[i] - 1.0)){
                    return false;
                }
            }
            else{
                if(!Mathematics.fuzzyIsNull(mat3[i])){
                    return false;
                }
            }
        }
        return true;
    }

    public void set_value(int index, double value){
        mat3[index] = value;
        flagBits = Mathematics.mat_type_General;
    }

    public void set_value(int aRow, int aColumn, double value){
        mat3[aColumn+aRow*3] = value;
        flagBits = Mathematics.mat_type_General;
    }

    public double get_value(int index){
        return mat3[index];
    }

    public double get_value(int aRow, int aColumn){
        return mat3[aColumn+aRow*3];
    }

    public double [] get_array(){
        return mat3;
    }

    public void set_array(double mat3[]){
        this.mat3[0] = mat3[0];
        this.mat3[1] = mat3[1];
        this.mat3[2] = mat3[2];
        this.mat3[3] = mat3[3];
        this.mat3[4] = mat3[4];
        this.mat3[5] = mat3[5];
        this.mat3[6] = mat3[6];
        this.mat3[7] = mat3[7];
        this.mat3[8] = mat3[8];
        flagBits = Mathematics.mat_type_General;
    }

    public int getType(){
        return flagBits;
    }

    public Matrix3x3 add(Matrix3x3 other){
        return new Matrix3x3(this.mat3[0] + other.get_value(0),
                             this.mat3[1] + other.get_value(1),
                             this.mat3[2] + other.get_value(2),
                             this.mat3[3] + other.get_value(3),
                             this.mat3[4] + other.get_value(4),
                             this.mat3[5] + other.get_value(5),
                             this.mat3[6] + other.get_value(6),
                             this.mat3[7] + other.get_value(7),
                             this.mat3[8] + other.get_value(8));
    }

    public Matrix3x3 add(double value){
        return new Matrix3x3(   this.mat3[0] + value,
                                this.mat3[1] + value,
                                this.mat3[2] + value,
                                this.mat3[3] + value,
                                this.mat3[4] + value,
                                this.mat3[5] + value,
                                this.mat3[6] + value,
                                this.mat3[7] + value,
                                this.mat3[8] + value);
    }

    public Matrix3x3 subtract(Matrix3x3 other){
        return new Matrix3x3(   this.mat3[0] - other.get_value(0),
                                this.mat3[1] - other.get_value(1),
                                this.mat3[2] - other.get_value(2),
                                this.mat3[3] - other.get_value(3),
                                this.mat3[4] - other.get_value(4),
                                this.mat3[5] - other.get_value(5),
                                this.mat3[6] - other.get_value(6),
                                this.mat3[7] - other.get_value(7),
                                this.mat3[8] - other.get_value(8));
    }

    public Matrix3x3 subtract(double value){
        return new Matrix3x3(   this.mat3[0] - value,
                                this.mat3[1] - value,
                                this.mat3[2] - value,
                                this.mat3[3] - value,
                                this.mat3[4] - value,
                                this.mat3[5] - value,
                                this.mat3[6] - value,
                                this.mat3[7] - value,
                                this.mat3[8] - value);
    }


    public Matrix3x3 multiply(double value){
        if(flagBits == Mathematics.mat_type_Identity){
            //dummy matrix
            Matrix3x3 new_mat = new Matrix3x3(0);
            new_mat.set_value(0,value);
            new_mat.set_value(4,value);
            new_mat.set_value(8,value);
            return new_mat;
        }
        return new Matrix3x3(   this.mat3[0] * value,
                                this.mat3[1] * value,
                                this.mat3[2] * value,
                                this.mat3[3] * value,
                                this.mat3[4] * value,
                                this.mat3[5] * value,
                                this.mat3[6] * value,
                                this.mat3[7] * value,
                                this.mat3[8] * value);
    }

    public Matrix3x3 multiply(Matrix3x3 other){
        if(this.flagBits == Mathematics.mat_type_Identity)
            return new Matrix3x3(other);
        else if(other.getType() == Mathematics.mat_type_Identity)
            return new Matrix3x3(this);

        return new Matrix3x3(
                this.mat3[0]*other.get_value(0) + this.mat3[3]*other.get_value(1) + this.mat3[6]*other.get_value(2),
                this.mat3[1]*other.get_value(0) + this.mat3[4]*other.get_value(1) + this.mat3[7]*other.get_value(2),
                this.mat3[2]*other.get_value(0) + this.mat3[5]*other.get_value(1) + this.mat3[8]*other.get_value(2),

                        // Second Column
                this.mat3[0]*other.get_value(3) + this.mat3[3]*other.get_value(4) + this.mat3[6]*other.get_value(5),
                this.mat3[1]*other.get_value(3) + this.mat3[4]*other.get_value(4) + this.mat3[7]*other.get_value(5),
                this.mat3[2]*other.get_value(3) + this.mat3[5]*other.get_value(4) + this.mat3[8]*other.get_value(5),

                        // Third Column
                this.mat3[0]*other.get_value(6) + this.mat3[3]*other.get_value(7) + this.mat3[6]*other.get_value(8),
                this.mat3[1]*other.get_value(6) + this.mat3[4]*other.get_value(7) + this.mat3[7]*other.get_value(8),
                this.mat3[2]*other.get_value(6) + this.mat3[5]*other.get_value(7) + this.mat3[8]*other.get_value(8)
        );
    }


    public Matrix3x3 divide(double value){
        if(flagBits == Mathematics.mat_type_Identity){
            //dummy matrix
            Matrix3x3 new_mat = new Matrix3x3(0);
            new_mat.set_value(0,1.0/value);
            new_mat.set_value(4,1.0/value);
            new_mat.set_value(8,1.0/value);
            return new_mat;
        }
        return new Matrix3x3(   this.mat3[0] / value,
                                this.mat3[1] / value,
                                this.mat3[2] / value,
                                this.mat3[3] / value,
                                this.mat3[4] / value,
                                this.mat3[5] / value,
                                this.mat3[6] / value,
                                this.mat3[7] / value,
                                this.mat3[8] / value);
    }


    public float[] getFloatArray(boolean transpose){
        float matrix[] = new float[9];
        //TRANSPOSE
        if(transpose) {
            for (int f = 0; f < 3; f++) {
                for (int g = 0; g < 3; g++) {
                    matrix[f * 3 + g] = (float) (mat3[f * 3 + g]);
                }
            }
        }
        else{
            for (int f = 0; f < 9; f++) {
                matrix[f] = (float) (mat3[f]);
            }
        }
        return matrix;
    }

}
