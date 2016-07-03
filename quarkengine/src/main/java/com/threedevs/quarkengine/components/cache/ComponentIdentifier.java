package com.threedevs.quarkengine.components.cache;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AJ on 25.06.2016.
 */
public class ComponentIdentifier {
    private String TAG = "ComponentIdentifier";

    private ArrayList<Long> _ids = new ArrayList<>();
    private long _lowestUnassignedID = Long.MIN_VALUE + 1;
    public final long INVALID_ID = Long.MIN_VALUE;

    public ComponentIdentifier(){

    }

    public long generateID(){
        if(_lowestUnassignedID < Long.MAX_VALUE){
            _ids.add(_lowestUnassignedID);
            return _lowestUnassignedID++;
        }
        for(long l = Long.MIN_VALUE + 1; l < Long.MAX_VALUE; l++ ){
            if(!_ids.contains(l)){
                return l;
            }
        }
        Log.e(TAG, "Error: No available texturePathID");
        return INVALID_ID;
    }

    public void removeID(Long id){
        if(_ids.contains(id)){
            _ids.remove(id);
        }
    }
}
