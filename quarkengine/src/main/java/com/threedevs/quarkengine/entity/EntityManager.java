package com.threedevs.quarkengine.entity;

import android.util.Log;

import com.threedevs.quarkengine.components.Component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AJ on 09.06.2016.
 */

public class EntityManager {
    private String TAG = "EntityManager";

    //eid, Entity
    private HashMap<Long, Entity> _entities; //stores all entities
    //ComponentClass, Component
    private HashMap<Class, ArrayList<Component> > _componentsByClass;
    //ComponentClass, eid, Component
    private HashMap<Class, HashMap<Long, ArrayList<Component> > > _componentsOfEntityByClass;
    private long _lowestUnassignedEid;

    public static final long INVALID_EID = Long.MIN_VALUE;

    public EntityManager(){
        _entities = new HashMap<>();
        _componentsByClass = new HashMap<>();
        _componentsOfEntityByClass = new HashMap<>();
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
        return entity;
    }

    public void addComponentToEntity(Component comp, Entity e){
        Class clazz = comp.getClass();

        //_componentsOfEntityByClass
        {
            HashMap<Long, ArrayList<Component>> components;
            if (_componentsOfEntityByClass.containsKey(clazz)) {
                components = new HashMap<>();
                _componentsOfEntityByClass.put(clazz, components);
            } else {
                components = _componentsOfEntityByClass.get(clazz);
            }

            ArrayList<Component> entityComponents;
            if (!components.containsKey(e.eid())) {
                entityComponents = new ArrayList<>();
                components.put(e.eid(), entityComponents);
            } else {
                entityComponents = components.get(e.eid());
            }
            entityComponents.add(comp);
        }


        //_componentsByClass
        {
            ArrayList<Component> components;
            if (_componentsByClass.containsKey(clazz)) {
                components = new ArrayList<>();
                _componentsByClass.put(clazz, components);
            } else {
                components = _componentsByClass.get(clazz);
            }
            components.add(comp);
        }
    }


    public ArrayList<Component> getComponentsOfClassForEntity(Class clazz, Entity e){
        ArrayList<Component> components;
        if(_componentsOfEntityByClass.containsKey(clazz)){
            HashMap<Long, ArrayList<Component> > componentsByEntity = _componentsOfEntityByClass.get(clazz);
            if(componentsByEntity.containsKey(e.eid())){
                return componentsByEntity.get(e.eid());
            }
        }
        components = new ArrayList<>();
        return components;
    }


    public void removeEntity(Entity e){

    }

    //TODO:


}
