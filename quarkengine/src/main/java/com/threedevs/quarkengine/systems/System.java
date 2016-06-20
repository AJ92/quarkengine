package com.threedevs.quarkengine.systems;

import com.threedevs.quarkengine.entity.EntityManager;

/**
 * Created by AJ on 15.06.2016.
 */
public class System {
    private EntityManager entityManager;

    public System(EntityManager em){
        entityManager = em;
    }

    public void update(float dt){

    }

    public static System initWithEntityManager(EntityManager em){
        return new System(em);
    }
}
