package xyz.sigsegowl.quarkengine.entity;

/**
 * Created by AJ on 09.06.2016.
 */

public class Entity {
    //entity ID
    //unique ID in it's system
    private int _eid;

    public Entity(int init_eid){
        _eid = init_eid;
    }

    public int eid(){
        return _eid;
    }

    //static method for creation...
    public static Entity initWithEid(int init_eid){
        return new Entity(init_eid);
    }
}
