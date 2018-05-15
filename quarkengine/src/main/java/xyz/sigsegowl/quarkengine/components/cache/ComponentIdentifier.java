package xyz.sigsegowl.quarkengine.components.cache;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AJ on 25.06.2016.
 */
public class ComponentIdentifier {
    private String TAG = "ComponentIdentifier";

    private ArrayList<Integer> _ids = new ArrayList<>();
    private int _lowestUnassignedID = Integer.MIN_VALUE + 1;
    public final int INVALID_ID = Integer.MIN_VALUE;

    public ComponentIdentifier(){

    }

    public int generateID(){
        if(_lowestUnassignedID < Integer.MAX_VALUE){
            _ids.add(_lowestUnassignedID);
            return _lowestUnassignedID++;
        }
        for(int l = Integer.MIN_VALUE + 1; l < Integer.MAX_VALUE; l++ ){
            if(!_ids.contains(l)){
                return l;
            }
        }
        Log.e(TAG, "Error: No available texturePathID");
        return INVALID_ID;
    }

    public void removeID(int id){
        if(_ids.contains(id)){
            _ids.remove(id);
        }
    }
}
