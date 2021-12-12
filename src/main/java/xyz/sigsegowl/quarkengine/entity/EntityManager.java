package xyz.sigsegowl.quarkengine.entity;

import android.util.Log;

import xyz.sigsegowl.quarkengine.components.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by AJ on 09.06.2016.
 */

public class EntityManager {
    private String TAG = "EntityManager";

    //eid, Entity
    private HashMap<Integer, Entity> _entities; //stores all entities
    //ComponentClass, eid
    private HashMap<Class, ArrayList<Integer> > _entityidByComponentClass;
    //eid, Component
    private HashMap<Integer, ArrayList<Component> > _componentsByEntity;
    //eid, CompoenentClass
    private HashMap<Integer, ArrayList<Class> > _componentClassByEntity;

    private int _lowestUnassignedEid;

    public static final int INVALID_EID = Integer.MIN_VALUE;


    private int _changes = 0;

    public EntityManager(){
        _entities = new HashMap<>();
        _entityidByComponentClass = new HashMap<>();
        _componentsByEntity = new HashMap<>();
        _componentClassByEntity = new HashMap<>();
        _lowestUnassignedEid = Integer.MIN_VALUE + 1;
    }

    private void change(){
        _changes += 1;
    }

    public int getChanges(){
        return _changes;
    }

    private int generateNewEid(){
        if(_lowestUnassignedEid < Integer.MAX_VALUE){
            return _lowestUnassignedEid++;
        }
        for(int l = Integer.MIN_VALUE + 1; l < Integer.MAX_VALUE; l++ ){
            if(!_entities.containsKey(l)){
                return l;
            }
        }
        Log.e(TAG, "Error: No available eids");
        return INVALID_EID;
    }

    public Entity createEntity(){
        int generated_eid = generateNewEid();
        Entity entity = Entity.initWithEid(generated_eid);
        _entities.put(generated_eid, entity);
        _componentsByEntity.put(generated_eid, new ArrayList<Component>());
        _componentClassByEntity.put(generated_eid, new ArrayList<Class>());
        change();
        return entity;
    }

    public void addComponentToEntity(Component comp, Entity e) {
        int eid = e.eid();
        if(!_entities.containsKey(eid)){
            debug();
            return;
        }

        Class clazz = comp.getClass();
        //_entityidByComponentClass
        {
            ArrayList<Integer> entities;
            if (!_entityidByComponentClass.containsKey(clazz)) {
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
        change();
    }


    public ArrayList<Component> getComponentsOfClassForEntity(Class clazz, Entity e){
        ArrayList<Component>    result = new ArrayList<>();
        int eid = e.eid();
        if(!_entityidByComponentClass.containsKey(clazz) || !_entities.containsKey(eid)){
            return result;
        }

        ArrayList<Component>    entityComponents = _componentsByEntity.get(eid);
        ArrayList<Class>        entityComponentClasses = _componentClassByEntity.get(eid);
        for(int i=0; i<entityComponentClasses.size(); i++){
            Component comp = entityComponents.get(i);
            if(comp.getClass() == clazz){
                result.add(comp);
            }
        }
        return result;
    }


    public void removeEntity(Entity e){
        //get the Classes of all entity components
        int eid = e.eid();

        //clean up _componentClassByEntity
        if(_componentClassByEntity.containsKey(eid)){
            ArrayList<Class> entityComponentClasses = _componentClassByEntity.get(eid);
            for(int i = 0; i < entityComponentClasses.size(); i++){
                if(_entityidByComponentClass.containsKey(entityComponentClasses.get(i))){
                    //get index of eid in class keyed hashmap...
                    ArrayList<Integer> eids = _entityidByComponentClass.get(entityComponentClasses.get(i));
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
        change();
    }

    public ArrayList<Entity> getAllEntitiesPossesingCompoenetOfClass(Class clazz){
        ArrayList<Entity> result = new ArrayList<>();
        if(_entityidByComponentClass.containsKey(clazz)){
            ArrayList<Integer> eids = _entityidByComponentClass.get(clazz);
            for(int i = 0; i < eids.size(); i++){
                result.add(_entities.get(eids.get(i)));
            }
        }
        return result;
    }

    public void debug(){
        //einfach bibidibabidi schreiben fertig - Frau Krieger
        {
            System.out.println("");
            System.out.println("###_entities-###");
            ArrayList<Integer> l = new ArrayList<Integer>(_entities.keySet());
            for (int i = 0; i < l.size(); i++) {
                System.out.println("   " + l.get(i));
                Entity l2 = _entities.get(l.get(i));
                System.out.println("      " + l2.getClass());
            }
        }

        {
            System.out.println("");
            System.out.println("###_entityidByComponentClass-###");
            ArrayList<Class> l = new ArrayList<Class>(_entityidByComponentClass.keySet());
            for (int i = 0; i < l.size(); i++) {
                System.out.println("   " + l.get(i));
                ArrayList<Integer> l2 = _entityidByComponentClass.get(l.get(i));
                for (int j = 0; j < l2.size(); j++) {
                    System.out.println("      " + l2.get(j));

                }
            }
        }

        {
            System.out.println("");
            System.out.println("###_componentsByEntity-###");
            ArrayList<Integer> l = new ArrayList<Integer>(_componentsByEntity.keySet());
            for (int i = 0; i < l.size(); i++) {
                System.out.println("   " + l.get(i));
                ArrayList<Component> l2 = _componentsByEntity.get(l.get(i));
                for (int j = 0; j < l2.size(); j++) {
                    System.out.println("      " + l2.get(j).getClass());

                }
            }
        }

        {
            System.out.println("");
            System.out.println("###_componentClassByEntity-###");
            ArrayList<Integer> l = new ArrayList<Integer>(_componentClassByEntity.keySet());
            for (int i = 0; i < l.size(); i++) {
                System.out.println("   " + l.get(i));
                ArrayList<Class> l2 = _componentClassByEntity.get(l.get(i));
                for (int j = 0; j < l2.size(); j++) {
                    System.out.println("      " + l2.get(j));

                }
            }
        }
    }

}
