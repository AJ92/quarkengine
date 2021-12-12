package xyz.sigsegowl.quarkengine.systems;

import xyz.sigsegowl.quarkengine.entity.EntityManager;

/**
 * Created by AJ on 15.06.2016.
 */
public class System {
    protected EntityManager _entityManager;

    public System(EntityManager em){
        _entityManager = em;
    }

    public void update(float dt){

    }

    public static System initWithEntityManager(EntityManager em){
        return new System(em);
    }
}
