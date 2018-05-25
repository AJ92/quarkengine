package xyz.sigsegowl.quarkengine.components;


import xyz.sigsegowl.quarkengine.math.Matrix.Matrix4x4;
import xyz.sigsegowl.quarkengine.math.Vector.Vector3;

/**
 * Created by AJ on 01.11.2014.
 */
public class Camera {
    //better pre init the values before i forget them later...

    //frustum settings
    private double Z_FAR = 1000.0;
    //never set Z_NEAR to ZERO !!!
    private double Z_NEAR = 1.0;
    private double FOV = 45.0;

    private boolean matrix_changed = true;

    //translation, scale, rotation...
    private Vector3 pos = new Vector3();

    private double angle_global = 0.0;
    private Vector3 rot_global = new Vector3();

    private double angle_local = 0.0;
    private Vector3 rot_local = new Vector3();

    //not used anyways...
    private Vector3 scl = new Vector3();

    //final one
    private Matrix4x4 M_camera_view = new Matrix4x4();

    private Matrix4x4 M_camera_rotation_local = new Matrix4x4();
    private Matrix4x4 M_camera_rotation_global = new Matrix4x4();
    private Matrix4x4 M_camera_rotation = new Matrix4x4();
    private Matrix4x4 M_camera_translation = new Matrix4x4();


    public Camera()
    {
        M_camera_view.set_to_identity();
        M_camera_rotation_local.set_to_identity();
        M_camera_rotation_global.set_to_identity();
        M_camera_rotation.set_to_identity();
        M_camera_translation.set_to_identity();
        Z_FAR = 1000.0;
        Z_NEAR = 1.0;
        FOV = 45.0;
        pos = new Vector3(0,0,0);
        angle_local = 0.0;
        angle_global = 0.0;
        rot_local = new Vector3(0,0,0);
        rot_global = new Vector3(0,0,0);
        scl = new Vector3(1,1,1);
        matrix_changed = true;
    }


    //LOCAL
    public void rotate_local_post_x(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_x(angle);
        M_camera_rotation_local =  M_camera_rotation_local.multiply(camerarotation);
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_local_post_y(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_y(angle);
        M_camera_rotation_local =  M_camera_rotation_local.multiply(camerarotation);
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_local_post_z(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_z(angle);
        M_camera_rotation_local =  M_camera_rotation_local.multiply(camerarotation);
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }





    public void rotate_local_pre_x(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_x(angle);
        M_camera_rotation_local =  camerarotation.multiply(M_camera_rotation_local);
        //M_camera_rotation = M_camera_rotation_local;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_local_pre_y(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_y(angle);
        M_camera_rotation_local =  camerarotation.multiply(M_camera_rotation_local);
        //M_camera_rotation = M_camera_rotation_local;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_local_pre_z(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_z(angle);
        M_camera_rotation_local =  camerarotation.multiply(M_camera_rotation_local);
        //M_camera_rotation = M_camera_rotation_local;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }







    //GLOBAL
    public void rotate_global_post_x(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_x(angle);
        M_camera_rotation_global =  M_camera_rotation_global.multiply(camerarotation);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_global_post_y(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_y(angle);
        M_camera_rotation_global =  M_camera_rotation_global.multiply(camerarotation);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_global_post_z(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_z(angle);
        M_camera_rotation_global =  M_camera_rotation_global.multiply(camerarotation);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }







    public void rotate_global_pre_x(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_x(angle);
        M_camera_rotation_global =  camerarotation.multiply(M_camera_rotation_global);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_global_pre_y(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_y(angle);
        M_camera_rotation_global =  camerarotation.multiply(M_camera_rotation_global);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }

    public void rotate_global_pre_z(double angle)
    {
        Matrix4x4 camerarotation = new Matrix4x4();
        camerarotation.rotate_z(angle);
        M_camera_rotation_global =  camerarotation.multiply(M_camera_rotation_global);
        //M_camera_rotation = M_camera_rotation_global;
        M_camera_view = M_camera_rotation_global.multiply(M_camera_translation).multiply(M_camera_rotation_local);
    }






    private void move(double x_dir, double y_dir, double z_dir)
    {
        /*
                    right     up    forward
                        0     1     2               3
                        4     5     6               7
                        8     9    10               11


                        12    13   14               15
            */


        /*
        pos[0]  = pos[0] + (x_dir * M_camera_view[0]);        //right / left
        pos[1]  = pos[1]  + (x_dir * M_camera_view[4]);
        pos[2]  = pos[2]  + (x_dir * M_camera_view[8]);

        pos[0]  = pos[0]  + (y_dir * M_camera_view[1]);        //up / down
        pos[1]  = pos[1]  + (y_dir * M_camera_view[5]);
        pos[2]  = pos[2]  + (y_dir * M_camera_view[9]);

        pos[0]  = pos[0]  + (z_dir * M_camera_view[2]);        //forward / backward
        pos[1]  = pos[1]  + (z_dir * M_camera_view[6]);
        pos[2]  = pos[2]  + (z_dir * M_camera_view[10]);

        M_camera_translation.translate(pos[0] ,pos[1] ,pos[2] );
        M_camera_view = M_camera_rotation_global * M_camera_translation * M_camera_rotation_local;
        */
    }

    private void translate(double x_dir, double y_dir, double z_dir)
    {
        /*
        x = x_dir;
        y = y_dir;
        z = z_dir;

        M_camera_translation.set_to_identity();
        M_camera_translation.translate(x_dir,y_dir,z_dir);
        M_camera_view = M_camera_rotation_global * M_camera_translation * M_camera_rotation_local;
        */
    }


    public void set_position(double x, double y, double z){
        pos.set_x(-x);
        pos.set_y(-y);
        pos.set_z(-z);
        set_matrix_pos();
    }

    public void set_position(Vector3 position){
        pos.set_x(-position.x());
        pos.set_y(-position.y());
        pos.set_z(-position.z());
        set_matrix_pos();
    }


    public void set_rotation_local(double angle, double x, double y, double z){
        angle_local = angle;
        rot_local.set_x(x);
        rot_local.set_y(y);
        rot_local.set_z(z);
        set_matrix_rot_local();
    }

    public void set_rotation_local(double angle, Vector3 rotation){
        angle_local = angle;
        rot_local = rotation;
        set_matrix_rot_local();
    }

    public void clear_rotation_local(){
        M_camera_rotation_local.set_to_identity();
        matrix_changed = true;
    }

    public void add_rotation_local(double angle, double x, double y, double z){
        Matrix4x4 temp_rot = new Matrix4x4();
        temp_rot.rotate(-angle, x, y, z);
        //pre multiply
        M_camera_rotation_local = temp_rot.multiply(M_camera_rotation_local);
        matrix_changed = true;
    }

    public void add_rotation_local(double angle, Vector3 rotation){
        add_rotation_local(angle, rotation.x(), rotation.y(), rotation.z());
    }




    public void set_rotation_global(double angle, double x, double y, double z){
        angle_global = angle;
        rot_global.set_x(x);
        rot_global.set_y(y);
        rot_global.set_z(z);
        set_matrix_rot_global();
    }

    public void set_rotation_global(double angle, Vector3 rotation){
        angle_global = angle;
        rot_global = rotation;
        set_matrix_rot_global();
    }

    public void clear_rotation_global(){
        M_camera_rotation_local.set_to_identity();
        matrix_changed = true;
    }

    public void add_rotation_global(double angle, double x, double y, double z){
        M_camera_rotation_global.rotate(angle, x, y, z);
        matrix_changed = true;
    }

    public void add_rotation_global(double angle, Vector3 rotation){
        M_camera_rotation_global.rotate(angle, rotation);
        matrix_changed = true;
    }


/*
void Camera::set_scale(double x, double y, double z){
    scl[0] = x;
    scl[1] = y;
    scl[2] = z;
    set_matrix_scl();
}

void Camera::set_scale(Vector3 scale){
    scl = scale;
    set_matrix_scl();
}
*/

    public Matrix4x4 get_view_matrix(){
        build_view_matrix();
        return M_camera_view;
    }

    public Matrix4x4 get_rotation_local_matrix(){
        return M_camera_rotation_local;
    }

    public Vector3 getPosition(){
        return new Vector3(-pos.x(),-pos.y(),-pos.z());
    }

    public double getFOV(){
        return FOV;
    }

    public double getZNEAR(){
        return Z_NEAR;
    }

    public double getZFAR(){
        return Z_FAR;
    }

    public void setFOV(double fov){
        FOV = fov;
    }

    public void setZNEAR(double znear){
        if(znear < 0.000001){
            Z_NEAR = 0.000001;
            return;
        }
        Z_NEAR = znear;
    }

    public void setZFAR(double zfar){
        Z_FAR = zfar;
    }


    public Vector3 getDirForward(){
        return new Vector3(M_camera_view.get_value(2),M_camera_view.get_value(6), M_camera_view.get_value(10));
    }

    public Vector3 getDirUp(){
        return new Vector3(M_camera_view.get_value(1),M_camera_view.get_value(5), M_camera_view.get_value(9));
    }

    public Vector3 getDirRight(){
        return new Vector3(M_camera_view.get_value(0),M_camera_view.get_value(4), M_camera_view.get_value(8));
    }

    void set_matrix_pos(){
        M_camera_translation.translate(pos);
        matrix_changed = true;
    }

    void set_matrix_rot_global(){
        Matrix4x4 rotation = new Matrix4x4();;
        rotation.rotate(angle_global,rot_global);
        M_camera_rotation_global = rotation;
        matrix_changed = true;
    }

    void set_matrix_rot_local(){
        Matrix4x4 rotation = new Matrix4x4();;
        rotation.rotate(angle_local,rot_local);
        M_camera_rotation_local = rotation;
        matrix_changed = true;
    }

    void add_matrix_rot_global(){

    }

    void add_matrix_rot_local(){

    }

    void set_matrix_scl(){
        //not used, we don't scale the camera ...
    }

    void build_view_matrix(){
        if(matrix_changed){
            M_camera_view = M_camera_rotation_local.multiply(M_camera_translation).multiply(M_camera_rotation_global);
            matrix_changed = false;
        }
    }

}
