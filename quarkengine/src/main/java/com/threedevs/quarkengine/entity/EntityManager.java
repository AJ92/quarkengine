package com.threedevs.quarkengine.entity;

import android.util.Log;

import com.threedevs.quarkengine.components.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by AJ on 09.06.2016.
 */

public class EntityManager {
    private String TAG = "EntityManager";

    //eid, Entity
    private HashMap<Long, Entity> _entities; //stores all entities
    //ComponentClass, eid
    private HashMap<Class, ArrayList<Long> > _entityidByComponentClass;
    //eid, Component
    private HashMap<Long, ArrayList<Component> > _componentsByEntity;
    //eid, CompoenentClass
    private HashMap<Long, ArrayList<Class> > _componentClassByEntity;

    private long _lowestUnassignedEid;

    public static final long INVALID_EID = Long.MIN_VALUE;

    public EntityManager(){
        _entities = new HashMap<>();
        _entityidByComponentClass = new HashMap<>();
        _componentsByEntity = new HashMap<>();
        _componentClassByEntity = new HashMap<>();
        _lowestUnassignedEid = Long.MIN_VALUE + 1;
    }

    public long generateNewEid(){
        if(_lowestUnassignedEid < Long.MAX_VALUE){
            return _lowestUnassignedEid++;
        }
        for(long l = Long.MIN_VALUE + 1; l < Long.MAX_VALUE; l++ ){
            if(!_entities.containsKey(l)){
                return l;
            }
        }
        Log.e(TAG, "Error: No available eids");
        return INVALID_EID;
    }

    public Entity createEntity(){
        long generated_eid = generateNewEid();
        Entity entity = Entity.initWithEid(generated_eid);
        _entities.put(generated_eid, entity);
        _componentsByEntity.put(generated_eid, new ArrayList<Component>());
        _componentClassByEntity.put(generated_eid, new ArrayList<Class>());
        return entity;
    }

    public void addComponentToEntity(Component comp, Entity e) {
        long eid = e.eid();
        if(!_entities.containsKey(eid)){
            return;
        }

        Class clazz = comp.getClass();
        //_entityidByComponentClass
        {
            ArrayList<Long> entities;
            if (_entityidByComponentClass.containsKey(clazz)) {
                entities = new ArrayList<>();
                _entityidByComponentClass.put(clazz, entities);
            } else {
                entities = _entityidByComponentClass.get(clazz);
            }
            if(!entities.contains(eid)) {
                entities.add(eid);
            }
        }

        //_componentsByEntity and _componentClassByEntity
        {
            ArrayList<Component> entityCompoenents   = _componentsByEntity.get(eid);
            ArrayList<Class> entityCompoenentClasses = _componentClassByEntity.get(eid);
            if(!entityCompoenents.contains(comp)) {
                entityCompoenents.add(comp);
                entityCompoenentClasses.add(clazz);
            }
        }
    }


    public ArrayList<Component> getComponentsOfClassForEntity(Class clazz, Entity e){
        ArrayList<Component>    result = new ArrayList<>();
        long eid = e.eid();
        if(!_entityidByComponentClass.containsKey(clazz) || !_entities.containsKey(eid)){
            return result;
        }

        ArrayList<Component>    entityComponents = new ArrayList<>();
        ArrayList<Class>        entityComponentClasses = new ArrayList<>();
        for(int i=0; i<entityComponentClasses.size(); i++){
            if(entityComponentClasses.get(i) == clazz){
                result.add(entityComponents.get(i));
            }
        }
        return result;
    }


    public void removeEntity(Entity e){
        //get the Classes of all entity components
        long eid = e.eid();

        //clean up _componentClassByEntity
        if(_componentClassByEntity.containsKey(eid)){
            ArrayList<Class> entityComponentClasses = _componentClassByEntity.get(eid);
            for(int i = 0; i < entityComponentClasses.size(); i++){
                if(_entityidByComponentClass.containsKey(entityComponentClasses.get(i))){
                    //get index of eid in class keyed hashmap...
                    ArrayList<Long> eids = _entityidByComponentClass.get(entityComponentClasses.get(i));
                    int index = eids.indexOf(eid);
                    if(index != -1){
                        eids.remove(index);
                    }
                }
            }
            _componentClassByEntity.remove(eid);
        }

        if(_componentsByEntity.containsKey(eid)){
            _componentsByEntity.remove(eid);
        }

        if(_entities.containsKey(eid)){
            _entities.remove(eid);
        }
    }

    public ArrayList<Entity> getAllEntitiesPossesingCompoenetOfClass(Class clazz){
        ArrayList<Entity> result = new ArrayList<>();
        if(_entityidByComponentClass.containsKey(clazz)){
            ArrayList<Long> eids = _entityidByComponentClass.get(clazz);
            for(int i = 0; i < eids.size(); i++){
                result.add(_entities.get(eids.get(i)));
            }
        }
        return result;
    }


}