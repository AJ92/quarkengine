package com.threedevs.quarkengine.math;

/**
 * Created by AJ on 24.10.2014.
 */
public class Mathematics {

    //for fuzzy compare... double
    public static double fuzzy_value = 0.000000000001;

    //matrix types...
    public static int mat_type_Identity     = 0x0001;
    public static int mat_type_General      = 0x0002;
    public static int mat_type_Translation  = 0x0004;
    public static int mat_type_Scale        = 0x0008;
    public static int mat_type_Rotation     = 0x0010;


    public static double M_PI       = 3.14159265358979323846;

    // Pre-calculated value of PI / 180.
    public static double kPI180     = 0.017453;

    // Pre-calculated value of 180 / PI.
    public static double  k180PI    = 57.295780;

    // Converts degrees to radians.
    public static double degreesToRadians(double x){
        return x * kPI180;
    }

    // Converts radians to degrees.
    public static double radiansToDegrees(double x){
        return x * k180PI;
    }

    public static boolean fuzzyIsNull(double value){
        return Math.abs(value) <= fuzzy_value;
    }

}
