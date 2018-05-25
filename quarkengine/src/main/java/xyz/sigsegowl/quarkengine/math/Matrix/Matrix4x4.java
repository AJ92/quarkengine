package xyz.sigsegowl.quarkengine.math.Matrix;

import xyz.sigsegowl.quarkengine.math.Mathematics;
import xyz.sigsegowl.quarkengine.math.Vector.Vector3;

/**
 * Created by AJ on 24.10.2014.
 */


/*column-major notation for openGL use...
  but using in code row-major notation for indizes...

  following stuff is colomn-major notation


    Indizes
    |    0        4        8        12   |
    |                                    |
    |    1        5        9        13   |
    |                                    |
    |    2        6        10       14   |
    |                                    |
    |    3        7        11       15   |

    Translation
    |    1        0        0        X    |
    |                                    |
    |    0        1        0        Y    |
    |                                    |
    |    0        0        1        Z    |
    |                                    |
    |    0        0        0        1    |

    Scale
    |    SX       0        0        0    |
    |                                    |
    |    0        SY       0        0    |
    |                                    |
    |    0        0        SZ       0    |
    |                                    |
    |    0        0        0        1    |

    Rotation X
    |    1        0        0        0    |
    |                                    |
    |    0      cos(?)   sin(?)     0    |
    |                                    |
    |    0     -sin(?)   cos(?)     0    |
    |                                    |
    |    0        0        0        1    |

    Rotation Y
    |  cos(?)     0    -sin(?)      0    |
    |                                    |
    |    0        1        0        0    |
    |                                    |
    |  sin(?)     0     cos(?)      0    |
    |                                    |
    |    0        0        0        1    |

    Rotation Z
    |  cos(?)  -sin(?)     0        0    |
    |                                    |
    |  sin(?)   cos(?)     0        0    |
    |                                    |
    |    0        0        1        0    |
    |                                    |
    |    0        0        0        1    |

    Directions
    |   RX        RY       RZ       12   |  R = right
    |                                    |
    |   UX        UY       UZ       13   |  U = up
    |                                    |
    |   LX        LY       LZ       14   |  L = look at (front)
    |                                    |
    |    3        7        11       15   |

*/


public class Matrix4x4 {

    private double mat4[] = new double[16];

    //binary composite type of the matrix... see Mathematics.java
    private int flagBits;

    //dummy constructor, don't do anything
    private Matrix4x4(int value){

    }

    public Matrix4x4(){
        set_to_identity();
    }

    //copy constructor
    public Matrix4x4(Matrix4x4 mat) {
        this.mat4[0] = mat.get_value(0);
        this.mat4[1] = mat.get_value(1);
        this.mat4[2] = mat.get_value(2);
        this.mat4[3] = mat.get_value(3);

        this.mat4[4] = mat.get_value(4);
        this.mat4[5] = mat.get_value(5);
        this.mat4[6] = mat.get_value(6);
        this.mat4[7] = mat.get_value(7);

        this.mat4[8] = mat.get_value(8);
        this.mat4[9] = mat.get_value(9);
        this.mat4[10] = mat.get_value(10);
        this.mat4[11] = mat.get_value(11);

        this.mat4[12] = mat.get_value(12);
        this.mat4[13] = mat.get_value(13);
        this.mat4[14] = mat.get_value(14);
        this.mat4[15] = mat.get_value(15);

        flagBits = Mathematics.mat_type_General;
    }

    public Matrix4x4(double mat4[]){
        this.mat4[0] = mat4[0];
        this.mat4[1] = mat4[1];
        this.mat4[2] = mat4[2];
        this.mat4[3] = mat4[3];

        this.mat4[4] = mat4[4];
        this.mat4[5] = mat4[5];
        this.mat4[6] = mat4[6];
        this.mat4[7] = mat4[7];

        this.mat4[8] = mat4[8];
        this.mat4[9] = mat4[9];
        this.mat4[10] = mat4[10];
        this.mat4[11] = mat4[11];

        this.mat4[12] = mat4[12];
        this.mat4[13] = mat4[13];
        this.mat4[14] = mat4[14];
        this.mat4[15] = mat4[15];
    }

    public Matrix4x4(double f1, double f2, double f3, double f4,
                     double f5, double f6, double f7, double f8,
                     double f9, double f10, double f11, double f12,
                     double f13, double f14, double f15, double f16){
        this.mat4[0] = f1;
        this.mat4[1] = f2;
        this.mat4[2] = f3;
        this.mat4[3] = f4;

        this.mat4[4] = f5;
        this.mat4[5] = f6;
        this.mat4[6] = f7;
        this.mat4[7] = f8;

        this.mat4[8] = f9;
        this.mat4[9] = f10;
        this.mat4[10] = f11;
        this.mat4[11] = f12;

        this.mat4[12] = f13;
        this.mat4[13] = f14;
        this.mat4[14] = f15;
        this.mat4[15] = f16;

        flagBits = Mathematics.mat_type_General;
    }

    public void set_to_identity(){
        mat4[0]    = mat4[5]  = mat4[10] = mat4[15] = 1.0;
        mat4[1]    = mat4[2]  = mat4[3]  = mat4[4]  = 0.0;
        mat4[6]    = mat4[7]  = mat4[8]  = mat4[9]  = 0.0;
        mat4[11]   = mat4[12] = mat4[13] = mat4[14] = 0.0;
        flagBits = Mathematics.mat_type_Identity;
    }

    public boolean is_identity(){
        if (flagBits == Mathematics.mat_type_Identity)
            return true;
        if (mat4[0] != 1.0f || mat4[1] != 0.0f || mat4[2] != 0.0f)
            return false;
        if (mat4[3] != 0.0f || mat4[4] != 0.0f || mat4[5] != 1.0f)
            return false;
        if (mat4[6] != 0.0f || mat4[7] != 0.0f || mat4[8] != 0.0f)
            return false;
        if (mat4[9] != 0.0f || mat4[10] != 1.0f || mat4[11] != 0.0f)
            return false;
        if (mat4[12] != 0.0f || mat4[13] != 0.0f || mat4[14] != 0.0f)
            return false;
        return (mat4[15] == 1.0f);
    }

    public boolean is_Fuzzy_identity(){
        for(int i = 0; i < 16; i++){
            if(i == 0 || i == 5 || i == 10 || i == 15){
                if(Math.abs(mat4[i] - 1.0) > Mathematics.fuzzy_value){
                    return false;
                }
            }
            else{
                if(Math.abs(mat4[i]) > Mathematics.fuzzy_value){
                    return false;
                }
            }
        }
        return true;
    }

    public void set_value(int index, double value){
        mat4[index] = value;
        flagBits = Mathematics.mat_type_General;
    }

    public void set_value(int aRow, int aColumn, double value){
        mat4[aColumn+aRow*4] = value;
        flagBits = Mathematics.mat_type_General;
    }

    public double get_value(int index){
        return mat4[index];
    }

    public double get_value(int aRow, int aColumn){
        return mat4[aColumn+aRow*3];
    }

    public double [] get_array(){
        return mat4;
    }

    public void set_array(double mat4[]){
        this.mat4[0] = mat4[0];
        this.mat4[1] = mat4[1];
        this.mat4[2] = mat4[2];
        this.mat4[3] = mat4[3];

        this.mat4[4] = mat4[4];
        this.mat4[5] = mat4[5];
        this.mat4[6] = mat4[6];
        this.mat4[7] = mat4[7];

        this.mat4[8] = mat4[8];
        this.mat4[9] = mat4[9];
        this.mat4[10] = mat4[10];
        this.mat4[11] = mat4[11];

        this.mat4[12] = mat4[12];
        this.mat4[13] = mat4[13];
        this.mat4[14] = mat4[14];
        this.mat4[15] = mat4[15];


        flagBits = Mathematics.mat_type_General;
    }

    public int getType(){
        return flagBits;
    }

    public void setType(int bits){
        this.flagBits = bits;
    }

    public void translate(double x, double y, double z){
        //set_to_identity();
        // Translate slots.
        mat4[12] = x;
        mat4[13] = y;
        mat4[14] = z;
        flagBits = Mathematics.mat_type_Translation;
    }

    public void scale(double sx, double sy, double sz){
        //set_to_identity();
        // Scale slots.
        mat4[0]   = sx;
        mat4[5]   = sy;
        mat4[10]  = sz;
        flagBits = Mathematics.mat_type_Scale;
    }

    public void translate(Vector3 translate_vector){
        translate(translate_vector.x(), translate_vector.y() ,translate_vector.z());
    }

    public void scale(Vector3 scale_vector){
        scale(scale_vector.x(), scale_vector.y(), scale_vector.z());
    }

    public void rotate_x(double degrees){
        set_to_identity();

        double radians = Mathematics.degreesToRadians(degrees);

        // Rotate X formula.
        mat4[5] = Math.cos(radians);
        mat4[6] = -Math.sin(radians);
        mat4[9] = -mat4[6];
        mat4[10] = mat4[5];
        flagBits = Mathematics.mat_type_Rotation;
    }

    public void rotate_y(double degrees){
        set_to_identity();

        double radians = Mathematics.degreesToRadians(degrees);

        // Rotate Y formula.
        mat4[0] = Math.cos(radians);
        mat4[2] = Math.sin(radians);
        mat4[8] = -mat4[2];
        mat4[10] = mat4[0];
        flagBits = Mathematics.mat_type_Rotation;
    }

    public void rotate_z(double degrees){
        set_to_identity();

        double radians = Mathematics.degreesToRadians(degrees);

        // Rotate Z formula.
        mat4[0] = Math.cos(radians);
        mat4[1] = Math.sin(radians);
        mat4[4] = -mat4[1];
        mat4[5] = mat4[0];
        flagBits = Mathematics.mat_type_Rotation;
    }

    public void rotate(double angle, Vector3 vector)
    {
        rotate(angle, vector.x(), vector.y(), vector.z());
    }

    public void rotate(double angle, double x, double y, double z)
    {
        if (angle == 0.0f)
            return;
        Matrix4x4 m = new Matrix4x4(1); // The "1" says to not load the identity.
        double c, s, ic;
        if (angle == 90.0 || angle == -270.0) {
            s = 1.0;
            c = 0.0;
        } else if (angle == -90.0 || angle == 270.0) {
            s = -1.0;
            c = 0.0;
        } else if (angle == 180.0 || angle == -180.0) {
            s = 0.0;
            c = -1.0;
        } else {
            double a = angle * Mathematics.M_PI / 180.0;
            c = Math.cos(a);
            s = Math.sin(a);
        }
        boolean quick = false;
        if (x == 0.0) {
            if (y == 0.0) {
                if (z != 0.0) {
                    // Rotate around the Z axis.
                    m.set_to_identity();
                    m.mat4[0] = c;
                    m.mat4[5] = c;
                    if (z < 0.0) {
                        m.mat4[4] = s;
                        m.mat4[1] = -s;
                    } else {
                        m.mat4[4] = -s;
                        m.mat4[1] = s;
                    }
                    m.setType(Mathematics.mat_type_General);
                    quick = true;
                }
            } else if (z == 0.0) {
                // Rotate around the Y axis.
                m.set_to_identity();
                m.mat4[0] = c;
                m.mat4[10] = c;
                if (y < 0.0) {
                    m.mat4[8] = -s;
                    m.mat4[2] = s;
                } else {
                    m.mat4[8] = s;
                    m.mat4[2] = -s;
                }
                m.setType(Mathematics.mat_type_General);
                quick = true;
            }
        } else if (y == 0.0 && z == 0.0) {
            // Rotate around the X axis.
            m.set_to_identity();
            m.mat4[5] = c;
            m.mat4[10] = c;
            if (x < 0.0) {
                m.mat4[9] = s;
                m.mat4[6] = -s;
            } else {
                m.mat4[9] = -s;
                m.mat4[6] = s;
            }
            m.setType(Mathematics.mat_type_General);
            quick = true;
        }
        if (!quick) {
            double len = x * x + y * y + z * z;
            if (!Mathematics.fuzzyIsNull(len - 1.0) && Mathematics.fuzzyIsNull(len)) {
                len = Math.sqrt(len);
                x /= len;
                y /= len;
                z /= len;
            }
            ic = 1.0 - c;
            m.mat4[0] = x * x * ic + c;
            m.mat4[4] = x * y * ic - z * s;
            m.mat4[8] = x * z * ic + y * s;
            m.mat4[12] = 0.0;
            m.mat4[1] = y * x * ic + z * s;
            m.mat4[5] = y * y * ic + c;
            m.mat4[9] = y * z * ic - x * s;
            m.mat4[13] = 0.0;
            m.mat4[2] = x * z * ic - y * s;
            m.mat4[6] = y * z * ic + x * s;
            m.mat4[10] = z * z * ic + c;
            m.mat4[14] = 0.0;
            m.mat4[3] = 0.0;
            m.mat4[7] = 0.0;
            m.mat4[11] = 0.0;
            m.mat4[15] = 1.0;
        }
        int flags = flagBits;

        //cpp style
        //*this *= m;
        //java...
        this.set_array(this.multiply(m).get_array());

        if (flags != Mathematics.mat_type_Identity)
            flagBits = flags | Mathematics.mat_type_Rotation;
        else
            flagBits = Mathematics.mat_type_Rotation;
    }


    // Calculate the determinant of a 3x3 sub-matrix.
    //     | A B C |
    // M = | D E F |   det(M) = A * (EI - HF) - B * (DI - GF) + C * (DH - GE)
    //     | G H I |
    public static double matrixDet3 (double mat4[], int row0, int row1, int row2,
                                     int col0, int col1, int col2)
    {
        return mat4[4*row0+col0] *
                    (mat4[4*row1+col1] * mat4[4*row2+col2] -
                     mat4[4*row1+col2] * mat4[4*row2+col1]) -
               mat4[4*row1+col0] *
                    (mat4[4*row0+col1] * mat4[4*row2+col2] -
                     mat4[4*row0+col2] * mat4[4*row2+col1]) +
               mat4[4*row2+col0] *
                    (mat4[4*row0+col1] * mat4[4*row1+col2] -
                     mat4[4*row0+col2] * mat4[4*row1+col1]);
    }

    // Calculate the determinant of a 4x4 matrix.
    public static double matrixDet4(double mat4[])
    {
        double det;
        det  = mat4[0] * matrixDet3(mat4, 1, 2, 3, 1, 2, 3);
        det -= mat4[4] * matrixDet3(mat4, 0, 2, 3, 1, 2, 3);
        det += mat4[8] * matrixDet3(mat4, 0, 1, 3, 1, 2, 3);
        det -= mat4[12] * matrixDet3(mat4, 0, 1, 2, 1, 2, 3);
        return det;
    }

    public double determinant(){
        return matrixDet4(mat4);
    }

    private Matrix4x4 orthonormalInverse()
    {
        Matrix4x4 result = new Matrix4x4(1);  // The '1' says not to load identity

        result.set_value(0,mat4[0]);
        result.set_value(4, mat4[1]);
        result.set_value(8, mat4[2]);

        result.set_value(1, mat4[4]);
        result.set_value(5, mat4[5]);
        result.set_value(9, mat4[6]);

        result.set_value(2, mat4[8]);
        result.set_value(6, mat4[9]);
        result.set_value(10, mat4[10]);

        result.set_value(3, 0.0);
        result.set_value(7, 0.0);
        result.set_value(11, 0.0);

        //might have wrong values
        result.set_value(12, -(result.get_value(0) * mat4[12] + result.get_value(4) * mat4[13] + result.get_value(8) * mat4[14]));
        result.set_value(13, -(result.get_value(1) * mat4[12] + result.get_value(5) * mat4[13] + result.get_value(9) * mat4[14]));
        result.set_value(14, -(result.get_value(2) * mat4[12] + result.get_value(6) * mat4[13] + result.get_value(10) * mat4[14]));
        result.set_value(15, 1.0);

        return result;
    }

    //cpp would have a boolean as input(pointer so output) to check if inversion was successful...
    //returns identity matrix if not invertible...
    public Matrix4x4 inverted(){

        // Handle some of the easy cases first.
        if (flagBits == Mathematics.mat_type_Identity) {
            return new Matrix4x4();
        } else if (flagBits == Mathematics.mat_type_Translation) {
            Matrix4x4 inv = new Matrix4x4();
            inv.mat4[12] = -mat4[12];
            inv.mat4[13] = -mat4[13];
            inv.mat4[14] = -mat4[14];
            inv.flagBits = Mathematics.mat_type_Translation;
            return inv;
        } else if (flagBits == Mathematics.mat_type_Rotation || flagBits == (Mathematics.mat_type_Rotation | Mathematics.mat_type_Translation)) {
            return orthonormalInverse();
        }



        double det = matrixDet4(mat4);
        if (det == 0.0f) {
            return new Matrix4x4();
        }
        det = 1.0f / det;

        return new Matrix4x4(
                 matrixDet3(mat4, 1, 2, 3, 1, 2, 3) * det,
                -matrixDet3(mat4, 0, 2, 3, 1, 2, 3) * det,
                 matrixDet3(mat4, 0, 1, 3, 1, 2, 3) * det,
                -matrixDet3(mat4, 0, 1, 2, 1, 2, 3) * det,
                -matrixDet3(mat4, 1, 2, 3, 0, 2, 3) * det,
                 matrixDet3(mat4, 0, 2, 3, 0, 2, 3) * det,
                -matrixDet3(mat4, 0, 1, 3, 0, 2, 3) * det,
                 matrixDet3(mat4, 0, 1, 2, 0, 2, 3) * det,
                 matrixDet3(mat4, 1, 2, 3, 0, 1, 3) * det,
                -matrixDet3(mat4, 0, 2, 3, 0, 1, 3) * det,
                 matrixDet3(mat4, 0, 1, 3, 0, 1, 3) * det,
                -matrixDet3(mat4, 0, 1, 2, 0, 1, 3) * det,
                -matrixDet3(mat4, 1, 2, 3, 0, 1, 2) * det,
                 matrixDet3(mat4, 0, 2, 3, 0, 1, 2) * det,
                -matrixDet3(mat4, 0, 1, 3, 0, 1, 2) * det,
                 matrixDet3(mat4, 0, 1, 2, 0, 1, 2) * det
        );
    }

    public Matrix4x4 transposed(){
        Matrix4x4 result = new Matrix4x4(1); // The "1" says to not load the identity.
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                result.set_value(col+row*4, mat4[4*row+col]);
            }
        }
        return result;
    }

    public Matrix3x3 normalMatrix(){

        Matrix3x3 inv = new Matrix3x3();

        // Handle the simple cases first.
        if (flagBits ==  Mathematics.mat_type_Identity || flagBits ==  Mathematics.mat_type_Translation) {
            return inv;
        } else if (flagBits ==  Mathematics.mat_type_Scale || flagBits == ( Mathematics.mat_type_Translation |  Mathematics.mat_type_Scale)) {
            if (mat4[0] == 0.0 || mat4[5] == 0.0 || mat4[10] == 0.0)
                return inv;
            inv.set_value(0, 1.0 / mat4[0]);
            inv.set_value(4, 1.0 / mat4[5]);
            inv.set_value(8, 1.0 / mat4[10]);
            return inv;
        }

        double det = matrixDet3(mat4, 0, 1, 2, 0, 1, 2);
        if (det == 0.0)
            return inv;
        det = 1.0 / det;


        // Invert and transpose in a single step.
        inv.set_value(0 + 0 * 3,  (mat4[5] * mat4[10] - mat4[9] * mat4[6]) * det);
        inv.set_value(1 + 0 * 3, -(mat4[4] * mat4[10] - mat4[6] * mat4[8]) * det);
        inv.set_value(2 + 0 * 3,  (mat4[4] * mat4[9] - mat4[5] * mat4[8]) * det);
        inv.set_value(0 + 1 * 3, -(mat4[1] * mat4[10] - mat4[9] * mat4[2]) * det);
        inv.set_value(1 + 1 * 3,  (mat4[0] * mat4[10] - mat4[2] * mat4[8]) * det);
        inv.set_value(2 + 1 * 3, -(mat4[0] * mat4[9] - mat4[1] * mat4[8]) * det);
        inv.set_value(0 + 2 * 3,  (mat4[1] * mat4[6] - mat4[2] * mat4[5]) * det);
        inv.set_value(1 + 2 * 3, -(mat4[0] * mat4[6] - mat4[2] * mat4[4]) * det);
        inv.set_value(2 + 2 * 3,  (mat4[0] * mat4[5] - mat4[4] * mat4[1]) * det);

        return inv;
    }


    //from Cpp quaternion is a vector with x,y,z, scalar (w)
/*!
    Multiples this matrix by another that rotates coordinates according
    to a specified \a quaternion.  The \a quaternion is assumed to have
    been normalized.

    \sa scale(), translate(), QQuaternion
*/

/*
void Matrix4x4::rotate(const Quaternion& quaternion)
{
    // Algorithm from:
    // http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q54
    Matrix4x4 m(1);
    double xx = quaternion.x() * quaternion.x();
    double xy = quaternion.x() * quaternion.y();
    double xz = quaternion.x() * quaternion.z();
    double xw = quaternion.x() * quaternion.scalar();
    double yy = quaternion.y() * quaternion.y();
    double yz = quaternion.y() * quaternion.z();
    double yw = quaternion.y() * quaternion.scalar();
    double zz = quaternion.z() * quaternion.z();
    double zw = quaternion.z() * quaternion.scalar();
    m(0,0) = 1.0f - 2 * (yy + zz);
    m(1,0) =        2 * (xy - zw);
    m(2,0) =        2 * (xz + yw);
    m(3,0) = 0.0f;
    m(0,1) =        2 * (xy + zw);
    m(1,1) = 1.0f - 2 * (xx + zz);
    m(2,1) =        2 * (yz - xw);
    m(3,1) = 0.0f;
    m(0,2) =        2 * (xz - yw);
    m(1,2) =        2 * (yz + xw);
    m(2,2) = 1.0f - 2 * (xx + yy);
    m(3,2) = 0.0f;
    m(0,3) = 0.0f;
    m(1,3) = 0.0f;
    m(2,3) = 0.0f;
    m(3,3) = 1.0f;
    int flags = flagBits;
    *this *= m;
    if (flags != Identity)
        flagBits = flags | Rotation;
    else
        flagBits = Rotation;
}
*/

    public Matrix4x4 ortho(double left, double right, double bottom, double top, double nearPlane, double farPlane){
        // Bail out if the projection volume is zero-sized.
        if (left == right || bottom == top || nearPlane == farPlane)
            return this;

        // Construct the projection.
        double width = right - left;
        double invheight = top - bottom;
        double clip = farPlane - nearPlane;

        //Vector class is incomplete and thus functions are missing...
        //A more efficient way of ortho projection...

        /*
        if (clip == 2.0f && (nearPlane + farPlane) == 0.0f) {
            // We can express this projection as a translate and scale
            // which will be more efficient to modify with further
            // transformations than producing a "General" matrix.
            translate(Vector3
                (-(left + right) / width,
                 -(top + bottom) / invheight,
                 0.0f));
            scale(Vector3
                (2.0f / width,
                 2.0f / invheight,
                 -1.0f));
            return;
        }
        */

        Matrix4x4 m = new Matrix4x4(1);
        m.set_value(0,0,2.0 / width);
        m.set_value(1,0,0.0);
        m.set_value(2,0,0.0);
        m.set_value(3,0, -(left + right) / width);
        m.set_value(0,1,0.0);
        m.set_value(1,1,2.0 / invheight);
        m.set_value(2,1,0.0);
        m.set_value(3,1, -(top + bottom) / invheight);
        m.set_value(0,2,0.0);
        m.set_value(1,2,0.0);
        m.set_value(2,2,-2.0 / clip);
        m.set_value(3,2,-(nearPlane + farPlane) / clip);
        m.set_value(0,3,0.0);
        m.set_value(1,3,0.0);
        m.set_value(2,3,0.0);
        m.set_value(3,3,1.0);

        // Apply the projection.
        return this.multiply(m);
    }

    public Matrix4x4 frustum(double left, double right, double bottom, double top, double nearPlane, double farPlane){
        // Bail out if the projection volume is zero-sized.
        if (left == right || bottom == top || nearPlane == farPlane)
            return this;

        // Construct the projection.
        Matrix4x4 m = new Matrix4x4(1);
        double width = right - left;
        double invheight = top - bottom;
        double clip = farPlane - nearPlane;
        m.set_value(0,0,2.0 * nearPlane / width);
        m.set_value(1,0,0.0);
        m.set_value(2,0,(left + right) / width);
        m.set_value(3,0,0.0);
        m.set_value(0,1,0.0);
        m.set_value(1,1,2.0 * nearPlane / invheight);
        m.set_value(2,1,(top + bottom) / invheight);
        m.set_value(3,1,0.0);
        m.set_value(0,2, 0.0);
        m.set_value(1,2,0.0);
        m.set_value(2,2,-(nearPlane + farPlane) / clip);
        m.set_value(3,2,-2.0 * nearPlane * farPlane / clip);
        m.set_value(0,3,0.0);
        m.set_value(1,3,0.0);
        m.set_value(2,3,-1.0);
        m.set_value(3,3, 0.0);

        // Apply the projection.
        return this.multiply(m);
    }

    public Matrix4x4 perspective(double angle, double aspect, double nearPlane, double farPlane){
        // Bail out if the projection volume is zero-sized.
        if (nearPlane == farPlane || aspect == 0.0)
            return this;

        // Construct the projection.
        Matrix4x4 m = new Matrix4x4(1);
        double radians = (angle / 2.0) * Mathematics.M_PI / 180.0;
        double sine = Math.sin(radians);
        if (sine == 0.0)
            return this;
        double cotan = Math.cos(radians) / sine;
        double clip = farPlane - nearPlane;
        m.set_value(0,0,cotan / aspect);
        m.set_value(1,0,0.0);
        m.set_value(2,0,0.0);
        m.set_value(3,0,0.0);
        m.set_value(0,1,0.0);
        m.set_value(1,1,cotan);
        m.set_value(2,1,0.0);
        m.set_value(3,1,0.0);
        m.set_value(0,2,0.0);
        m.set_value(1,2,0.0);
        m.set_value(2,2,-(nearPlane + farPlane) / clip);
        m.set_value(3,2,-(2.0 * nearPlane * farPlane) / clip);
        m.set_value(0,3,0.0);
        m.set_value(1,3,0.0);
        m.set_value(2,3,-1.0);
        m.set_value(3,3,0.0);

        // Apply the projection.
        return this.multiply(m);
    }

    public Matrix4x4 lookAt(Vector3 eye, Vector3 center, Vector3 up){
        Vector3 forward = (center.subtract(eye)).normalized();
        Vector3 side = Vector3.crossProduct(forward, up).normalized();
        Vector3 upVector = Vector3.crossProduct(side, forward);

        Matrix4x4 m = new Matrix4x4(1);

        m.set_value(0,0,side.x());
        m.set_value(1,0,side.y());
        m.set_value(2,0,side.z());
        m.set_value(3,0,0.0f);
        m.set_value(0,1,upVector.x());
        m.set_value(1,1,upVector.y());
        m.set_value(2,1,upVector.z());
        m.set_value(3,1,0.0f);
        m.set_value(0,2,-forward.x());
        m.set_value(1,2,-forward.y());
        m.set_value(2,2,-forward.z());
        m.set_value(3,2,0.0f);
        m.set_value(0,3,0.0f);
        m.set_value(1,3,0.0f);
        m.set_value(2,3,0.0f);
        m.set_value(3,3,1.0f);

        Matrix4x4 m2 = this.multiply(m);
        m2.translate(-eye.x(),-eye.y(),-eye.z());
        return m2;
    }


    public Vector3 get_vector_up(){
        return new Vector3(mat4[1],mat4[5],mat4[9]);
    }

    public Vector3 get_vector_right(){
        return new Vector3(mat4[0],mat4[4],mat4[8]);
    }

    public Vector3 get_vector_look_at(){
        return new Vector3(mat4[2],mat4[6],mat4[10]);
    }

    public Vector3 get_vector_pos(){
        return new Vector3(mat4[12],mat4[13],mat4[14]);
    }

    public Vector3 get_vector_scale(){
        return new Vector3(mat4[0],mat4[5],mat4[10]);
    }

    public Matrix3x3 rotationMatrix(){
        return new Matrix3x3(
                mat4[0],mat4[1],mat4[2],
                mat4[4],mat4[5],mat4[6],
                mat4[8],mat4[9],mat4[10]);
    }

    public Matrix4x4 add(Matrix4x4 other){
        return new Matrix4x4(
                this.mat4[0] + other.get_value(0),
                this.mat4[1] + other.get_value(1),
                this.mat4[2] + other.get_value(2),
                this.mat4[3] + other.get_value(3),

                this.mat4[4] + other.get_value(4),
                this.mat4[5] + other.get_value(5),
                this.mat4[6] + other.get_value(6),
                this.mat4[7] + other.get_value(7),

                this.mat4[8] + other.get_value(8),
                this.mat4[9] + other.get_value(9),
                this.mat4[10] + other.get_value(10),
                this.mat4[11] + other.get_value(11),

                this.mat4[12] + other.get_value(12),
                this.mat4[13] + other.get_value(13),
                this.mat4[14] + other.get_value(14),
                this.mat4[15] + other.get_value(15)
        );
    }

    public Matrix4x4 add(double value){
        return new Matrix4x4(
                this.mat4[0] + value,
                this.mat4[1] + value,
                this.mat4[2] + value,
                this.mat4[3] + value,
                this.mat4[4] + value,
                this.mat4[5] + value,
                this.mat4[6] + value,
                this.mat4[7] + value,
                this.mat4[8] + value,
                this.mat4[9] + value,
                this.mat4[10] + value,
                this.mat4[11] + value,
                this.mat4[12] + value,
                this.mat4[13] + value,
                this.mat4[14] + value,
                this.mat4[15] + value
                );
    }

    public Matrix4x4 subtract(Matrix4x4 other){
        return new Matrix4x4(
                this.mat4[0] - other.get_value(0),
                this.mat4[1] - other.get_value(1),
                this.mat4[2] - other.get_value(2),
                this.mat4[3] - other.get_value(3),
                this.mat4[4] - other.get_value(4),
                this.mat4[5] - other.get_value(5),
                this.mat4[6] - other.get_value(6),
                this.mat4[7] - other.get_value(7),
                this.mat4[8] - other.get_value(8),
                this.mat4[9] - other.get_value(9),
                this.mat4[10] - other.get_value(10),
                this.mat4[11] - other.get_value(11),
                this.mat4[12] - other.get_value(12),
                this.mat4[13] - other.get_value(13),
                this.mat4[14] - other.get_value(14),
                this.mat4[15] - other.get_value(15)
        );
    }

    public Matrix4x4 subtract(double value){
        return new Matrix4x4(
                this.mat4[0] - value,
                this.mat4[1] - value,
                this.mat4[2] - value,
                this.mat4[3] - value,
                this.mat4[4] - value,
                this.mat4[5] - value,
                this.mat4[6] - value,
                this.mat4[7] - value,
                this.mat4[8] - value,
                this.mat4[9] - value,
                this.mat4[10] - value,
                this.mat4[11] - value,
                this.mat4[12] - value,
                this.mat4[13] - value,
                this.mat4[14] - value,
                this.mat4[15] - value
        );
    }


    public Matrix4x4 multiply(double value){
        if(flagBits == Mathematics.mat_type_Identity){
            //dummy matrix
            Matrix4x4 new_mat = new Matrix4x4(0);
            new_mat.set_value(0,value);
            new_mat.set_value(5,value);
            new_mat.set_value(10,value);
            new_mat.set_value(15,value);
            return new_mat;
        }
        return new Matrix4x4(
                this.mat4[0] * value,
                this.mat4[1] * value,
                this.mat4[2] * value,
                this.mat4[3] * value,
                this.mat4[4] * value,
                this.mat4[5] * value,
                this.mat4[6] * value,
                this.mat4[7] * value,
                this.mat4[8] * value,
                this.mat4[9] * value,
                this.mat4[10] * value,
                this.mat4[11] * value,
                this.mat4[12] * value,
                this.mat4[13] * value,
                this.mat4[14] * value,
                this.mat4[15] * value
                );
    }

    public Matrix4x4 multiply(Matrix4x4 other){
        if(this.flagBits == Mathematics.mat_type_Identity)
            return new Matrix4x4(other);
        else if(other.getType() == Mathematics.mat_type_Identity)
            return new Matrix4x4(this);

        return new Matrix4x4(
                // Fisrt Column
                mat4[0]*other.get_value(0) + mat4[4]*other.get_value(1) + mat4[8]*other.get_value(2) + mat4[12]*other.get_value(3),
                mat4[1]*other.get_value(0) + mat4[5]*other.get_value(1) + mat4[9]*other.get_value(2) + mat4[13]*other.get_value(3),
                mat4[2]*other.get_value(0) + mat4[6]*other.get_value(1) + mat4[10]*other.get_value(2) + mat4[14]*other.get_value(3),
                mat4[3]*other.get_value(0) + mat4[7]*other.get_value(1) + mat4[11]*other.get_value(2) + mat4[15]*other.get_value(3),

                // Second Column
                mat4[0]*other.get_value(4) + mat4[4]*other.get_value(5) + mat4[8]*other.get_value(6) + mat4[12]*other.get_value(7),
                mat4[1]*other.get_value(4) + mat4[5]*other.get_value(5) + mat4[9]*other.get_value(6) + mat4[13]*other.get_value(7),
                mat4[2]*other.get_value(4) + mat4[6]*other.get_value(5) + mat4[10]*other.get_value(6) + mat4[14]*other.get_value(7),
                mat4[3]*other.get_value(4) + mat4[7]*other.get_value(5) + mat4[11]*other.get_value(6) + mat4[15]*other.get_value(7),

                // Third Column
                mat4[0]*other.get_value(8) + mat4[4]*other.get_value(9) + mat4[8]*other.get_value(10) + mat4[12]*other.get_value(11),
                mat4[1]*other.get_value(8) + mat4[5]*other.get_value(9) + mat4[9]*other.get_value(10) + mat4[13]*other.get_value(11),
                mat4[2]*other.get_value(8) + mat4[6]*other.get_value(9) + mat4[10]*other.get_value(10) + mat4[14]*other.get_value(11),
                mat4[3]*other.get_value(8) + mat4[7]*other.get_value(9) + mat4[11]*other.get_value(10) +  mat4[15]*other.get_value(11),

                // Fourth Column
                mat4[0]*other.get_value(12) + mat4[4]*other.get_value(13) + mat4[8]*other.get_value(14) + mat4[12]*other.get_value(15),
                mat4[1]*other.get_value(12) + mat4[5]*other.get_value(13) + mat4[9]*other.get_value(14) + mat4[13]*other.get_value(15),
                mat4[2]*other.get_value(12) + mat4[6]*other.get_value(13) + mat4[10]*other.get_value(14) + mat4[14]*other.get_value(15),
                mat4[3]*other.get_value(12) + mat4[7]*other.get_value(13) + mat4[11]*other.get_value(14) + mat4[15]*other.get_value(15)
                //is General matrix...
        );
    }



    public Vector3 multiply(Vector3 vector)
    {
        double x, y, z, w;
        if (this.flagBits == Mathematics.mat_type_Identity) {
            return vector;
        } else if (this.flagBits == Mathematics.mat_type_Translation) {
            return new Vector3( vector.x() + this.mat4[12],
                                vector.y() + this.mat4[13],
                                vector.z() + this.mat4[14]);
        } else if (this.flagBits ==
                (Mathematics.mat_type_Translation | Mathematics.mat_type_Scale)) {
            return new Vector3( vector.x() * this.mat4[0] + this.mat4[12],
                                vector.y() * this.mat4[5] + this.mat4[13],
                                vector.z() * this.mat4[10] + this.mat4[14]);
        } else if (this.flagBits == Mathematics.mat_type_Scale) {
            return new Vector3( vector.x() * this.mat4[0],
                                vector.y() * this.mat4[5],
                                vector.z() * this.mat4[10]);
        } else {
            x = vector.x() * this.mat4[0] +
                    vector.y() * this.mat4[4] +
                    vector.z() * this.mat4[8] +
                    this.mat4[12];
            y = vector.x() * this.mat4[1] +
                    vector.y() * this.mat4[5] +
                    vector.z() * this.mat4[9] +
                    this.mat4[13];
            z = vector.x() * this.mat4[2] +
                    vector.y() * this.mat4[6] +
                    vector.z() * this.mat4[10] +
                    this.mat4[14];
            w = vector.x() * this.mat4[3] +
                    vector.y() * this.mat4[7] +
                    vector.z() * this.mat4[11] +
                    this.mat4[15];
            if (w == 1.0f)
                return new Vector3(x, y, z);
            else
                return new Vector3(x / w, y / w, z / w);
        }
    }


    public Matrix4x4 divide(double value){
        if(flagBits == Mathematics.mat_type_Identity){
            //dummy matrix
            Matrix4x4 new_mat = new Matrix4x4(0);
            new_mat.set_value(0,1.0/value);
            new_mat.set_value(5,1.0/value);
            new_mat.set_value(10,1.0/value);
            new_mat.set_value(15,1.0/value);
            return new_mat;
        }
        return new Matrix4x4(
                this.mat4[0] / value,
                this.mat4[1] / value,
                this.mat4[2] / value,
                this.mat4[3] / value,
                this.mat4[4] / value,
                this.mat4[5] / value,
                this.mat4[6] / value,
                this.mat4[7] / value,
                this.mat4[8] / value,
                this.mat4[9] / value,
                this.mat4[10] / value,
                this.mat4[11] / value,
                this.mat4[12]/ value,
                this.mat4[13] / value,
                this.mat4[14] / value,
                this.mat4[15] / value
                );
    }

    public void getFloatArray(float[] mat4x4, boolean transpose){
        //TRANSPOSE
        if(transpose) {
            for (int f = 0; f < 4; f++) {
                for (int g = 0; g < 4; g++) {
                    mat4x4[f * 4 + g] = (float) (mat4[f * 4 + g]);
                }
            }
        }
        else{
            for (int f = 0; f < 16; f++) {
                mat4x4[f] = (float) (mat4[f]);
            }
        }
    }

}