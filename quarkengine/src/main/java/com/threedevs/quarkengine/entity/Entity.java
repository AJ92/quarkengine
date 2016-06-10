package com.threedevs.quarkengine.entity;

/**
 * Created by AJ on 09.06.2016.
 */

public class Entity {
    //entity ID
    //unique ID in it's system
    private long _eid;

    public Entity(long init_eid){
        _eid = init_eid;
    }

    public long eid(){
        return _eid;
    }

    //static method for creation...
    public static Entity initWithEid(long init_eid){
        return new Entity(init_eid);
    }
}
