package xyz.sigsegowl.quarkengine.math.Vector;

import xyz.sigsegowl.quarkengine.math.Mathematics;
import xyz.sigsegowl.quarkengine.math.Matrix.Matrix3x3;

public class Quaternion extends Vector4{

    //vec4 storage:x, y ,z, w

    public Quaternion(){
        super(0.0, 0.0, 0.0, 1.0);
    }

    public Quaternion(Vector4 vector) {
        super(vector.x(), vector.y(), vector.z(), vector.w());
    }

    public Quaternion(double scalar, Vector3 vector) {
        super(vector.x(), vector.y(), vector.z(), scalar);
    }

    public Quaternion(double x, double y, double z, double w) {
        super(x, y, z, w);
    }

    public void set_scalar(double scalar){
        set_w(scalar);
    }

    public double scalar(){
        return w();
    }

    public boolean is_identity(){
        return w() == 1.0 && x() == 0.0 && y() == 0.0 && z() == 0.0;
    }

    //x,y,z=axis   w=angle
    public Vector4 get_axis_and_angle(){
        // The quaternion representing the rotation is
        // q = cos(A/2)+sin(A/2)*(x*i+y*j+z*k)

        Vector4 vec = new Vector4();
        double length = x() * x() + y() * y() + z() * z();
        if (!Mathematics.fuzzyIsNull(length)) {
            vec.set_x(x());
            vec.set_y(y());
            vec.set_z(z());
            if (!Mathematics.fuzzyIsNull(length - 1.0)) {
                length = Math.sqrt(length);
                vec.set_x(x() / length);
                vec.set_y(y() / length);
                vec.set_z(z() / length);
            }
            vec.set_w(2.0f * Math.acos(w()));
        } else {
            // angle is 0 (mod 2*pi), so any axis will fit
            vec.set_x(0.0);
            vec.set_y(0.0);
            vec.set_z(0.0);
            vec.set_w(0.0);
        }
        vec.set_w(Mathematics.radiansToDegrees(vec.w()));
        return vec;
    }

    public static Quaternion from_axis_and_angle(double x, double y, double z, double angle) {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (!Mathematics.fuzzyIsNull(length - 1.0) && !Mathematics.fuzzyIsNull(length)) {
            x /= length;
            y /= length;
            z /= length;
        }
        double a = Mathematics.degreesToRadians(angle / 2.0);
        double s = Math.sin(a);
        double c = Math.cos(a);
        return new Quaternion(new Quaternion(x * s, y * s, z * s, c).normalized());
    }

    //x = pitch, y = yaw, z = roll
    public Vector3 to_euler_angles(){
        // Algorithm from:
        // http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q37
        Vector3 vector = new Vector3();

        double xx = x() * x();
        double xy = x() * y();
        double xz = x() * z();
        double xw = x() * w();
        double yy = y() * y();
        double yz = y() * z();
        double yw = y() * w();
        double zz = z() * z();
        double zw = z() * w();
        double lengthSquared = xx + yy + zz + w() * w();
        if (    !Mathematics.fuzzyIsNull(lengthSquared - 1.0) &&
                !Mathematics.fuzzyIsNull(lengthSquared))
        {
            xx /= lengthSquared;
            xy /= lengthSquared; // same as (xp / length) * (yp / length)
            xz /= lengthSquared;
            xw /= lengthSquared;
            yy /= lengthSquared;
            yz /= lengthSquared;
            yw /= lengthSquared;
            zz /= lengthSquared;
            zw /= lengthSquared;
        }

        vector.set_x(Math.asin(-2.0 * (yz - xw)));
        if (vector.x() < Mathematics.M_PI_2) {
            if (vector.x() > -Mathematics.M_PI_2) {
                vector.set_y(Math.atan2(2.0 * (xz + yw), 1.0 - 2.0 * (xx + yy)));
                vector.set_z(Math.atan2(2.0 * (xy + zw), 1.0 - 2.0 * (xx + zz)));
            } else {
                // not a unique solution
                vector.set_z(0.0);
                vector.set_y(-Math.atan2(-2.0 * (xy - zw), 1.0 - 2.0 * (yy + zz)));
            }
        } else {
            // not a unique solution
            vector.set_z(0.0);
            vector.set_y(Math.atan2(-2.0f * (xy - zw), 1.0f - 2.0f * (yy + zz)));
        }
        vector.set_x(Mathematics.radiansToDegrees(vector.x()));
        vector.set_y(Mathematics.radiansToDegrees(vector.y()));
        vector.set_z(Mathematics.radiansToDegrees(vector.z()));
        return vector;
    }

    public Vector3 rotated_vector(Vector3 vector) {
        return new Quaternion(this.multiply(new Quaternion(0, vector)).multiply(conjugated())).vector();
    }

    public Quaternion inverted() {
        double len = w() * w() + x() * x() + y() * y() + z() * z();
        if (!Mathematics.fuzzyIsNull(len)) {
            return new Quaternion(w() / len, -x() / len, -y() / len, -z() / len);
        }
        return new Quaternion(0.0, 0.0, 0.0, 0.0);
    }

    public Quaternion conjugated()
    {
        return new Quaternion(-x(), -y(), -z(), w());
    }

    public Vector3 vector(){
        return new Vector3(x(), y(), z());
    }


    public Quaternion multiply(Quaternion q)
    {
        double yy = (w() - y()) * (q.w() + q.z());
        double zz = (w() + y()) * (q.w() - q.z());
        double ww = (z() + x()) * (q.x() + q.y());
        double xx = ww + yy + zz;
        double qq = 0.5 * (xx + (z() - x()) * (q.x() - q.y()));
        double w = qq - ww + (z() - y()) * (q.y() - q.z());
        double x = qq - xx + (x() + w()) * (q.x() + q.w());
        double y = qq - yy + (w() - x()) * (q.y() + q.z());
        double z = qq - zz + (z() + y()) * (q.w() - q.x());
        return new Quaternion(x, y, z, w);
    }

    public Matrix3x3 to_rotation_matrix()
    {
        // Algorithm from:
        // http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q54
        Matrix3x3 rot3x3 = new Matrix3x3(1);
        double f2x = x() + x();
        double f2y = y() + y();
        double f2z = z() + z();
        double f2xw = f2x * w();
        double f2yw = f2y * w();
        double f2zw = f2z * w();
        double f2xx = f2x * x();
        double f2xy = f2x * y();
        double f2xz = f2x * z();
        double f2yy = f2y * y();
        double f2yz = f2y * z();
        double f2zz = f2z * z();
        rot3x3.set_value(0, 0, 1.0f - (f2yy + f2zz));
        rot3x3.set_value(0, 1,         f2xy - f2zw);
        rot3x3.set_value(0, 2,         f2xz + f2yw);
        rot3x3.set_value(1, 0,         f2xy + f2zw);
        rot3x3.set_value(1, 1, 1.0f - (f2xx + f2zz));
        rot3x3.set_value(1, 2,         f2yz - f2xw);
        rot3x3.set_value(2, 0,         f2xz - f2yw);
        rot3x3.set_value(2, 1,         f2yz + f2xw);
        rot3x3.set_value(2, 2, 1.0f - (f2xx + f2yy));
        return rot3x3;
    }

    public static Quaternion from_rotation_matrix(Matrix3x3 rot3x3)
    {
        // Algorithm from:
        // http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q55
        double scalar;
        double axis[] = {0, 0, 0};
        double trace =  rot3x3.get_value(0, 0) +
                        rot3x3.get_value(1, 1) +
                        rot3x3.get_value(2, 2);
        if (trace > 0.00000001f) {
            double s = 2.0f * Math.sqrt(trace + 1.0);
            scalar = 0.25f * s;
            axis[0] = (rot3x3.get_value(2, 1) - rot3x3.get_value(1, 2)) / s;
            axis[1] = (rot3x3.get_value(0, 2) - rot3x3.get_value(2, 0)) / s;
            axis[2] = (rot3x3.get_value(1, 0) - rot3x3.get_value(0, 1)) / s;
        } else {
            int s_next[] = { 1, 2, 0 };
            int i = 0;
            if (rot3x3.get_value(1, 1) > rot3x3.get_value(0, 0))
                i = 1;
            if (rot3x3.get_value(2, 2) > rot3x3.get_value(i, i))
                i = 2;
            int j = s_next[i];
            int k = s_next[j];
            double s = 2.0f * Math.sqrt(
                            rot3x3.get_value(i, i) -
                            rot3x3.get_value(j, j) -
                            rot3x3.get_value(k, k) + 1.0
            );
            axis[i] = 0.25 * s;
            scalar = (rot3x3.get_value(k, j) - rot3x3.get_value(j, k)) / s;
            axis[j] = (rot3x3.get_value(j, i) + rot3x3.get_value(i, j)) / s;
            axis[k] = (rot3x3.get_value(k, i) + rot3x3.get_value(i, k)) / s;
        }
        return new Quaternion(axis[0], axis[1], axis[2], scalar);
    }

    public void get_axes(Vector3 x_axis_out, Vector3 y_axis_out, Vector3 z_axis_out)
    {
        Matrix3x3 rot3x3 = to_rotation_matrix();

        x_axis_out.set_x(rot3x3.get_value(0, 0));
        x_axis_out.set_y(rot3x3.get_value(1, 0));
        x_axis_out.set_z(rot3x3.get_value(2, 0));

        y_axis_out.set_x(rot3x3.get_value(0, 1));
        y_axis_out.set_y(rot3x3.get_value(1, 1));
        y_axis_out.set_z(rot3x3.get_value(2, 1));

        z_axis_out.set_x(rot3x3.get_value(0, 2));
        z_axis_out.set_y(rot3x3.get_value(1, 2));
        z_axis_out.set_z(rot3x3.get_value(2, 2));
    }

    public static Quaternion from_axes(Vector3 x_axis, Vector3 y_axis, Vector3 z_axis)
    {
        Matrix3x3 rot3x3 = new Matrix3x3(1);
        rot3x3.set_value(0, 0, x_axis.x());
        rot3x3.set_value(1, 0, x_axis.y());
        rot3x3.set_value(2, 0, x_axis.z());
        rot3x3.set_value(0, 1, y_axis.x());
        rot3x3.set_value(1, 1, y_axis.y());
        rot3x3.set_value(2, 1, y_axis.z());
        rot3x3.set_value(0, 2, z_axis.x());
        rot3x3.set_value(1, 2, z_axis.y());
        rot3x3.set_value(2, 2, z_axis.z());
        return Quaternion.from_rotation_matrix(rot3x3);
    }

    public static Quaternion from_euler_angles(double pitch, double yaw, double roll)
    {
        // Algorithm from:
        // http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q60
        pitch = Mathematics.degreesToRadians(pitch);
        yaw =   Mathematics.degreesToRadians(yaw);
        roll =  Mathematics.degreesToRadians(roll);
        pitch *= 0.5;
        yaw *= 0.5;
        roll *= 0.5;
        double c1 = Math.cos(yaw);
        double s1 = Math.sin(yaw);
        double c2 = Math.cos(roll);
        double s2 = Math.sin(roll);
        double c3 = Math.cos(pitch);
        double s3 = Math.sin(pitch);
        double c1c2 = c1 * c2;
        double s1s2 = s1 * s2;
        double w = c1c2 * c3 + s1s2 * s3;
        double x = c1c2 * s3 + s1s2 * c3;
        double y = s1 * c2 * c3 - c1 * s2 * s3;
        double z = c1 * s2 * c3 - s1 * c2 * s3;
        return new Quaternion(x, y, z, w);
    }
}
