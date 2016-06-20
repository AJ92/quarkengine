package com.threedevs.quarkengine;

import com.threedevs.quarkengine.components.Component;
import com.threedevs.quarkengine.components.Position;
import com.threedevs.quarkengine.entity.Entity;
import com.threedevs.quarkengine.entity.EntityManager;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by AJ on 20.06.2016.
 */
public class EntityManagerUnitTest {
    @Test
    public void entityManagerInit() throws Exception {
        EntityManager em = new EntityManager();
        Entity testEntity1 = em.createEntity();
        Position testEntity1pos = new Position();
        em.addComponentToEntity(testEntity1pos, testEntity1);
        ArrayList<Component> testEntity1Positions = em.getComponentsOfClassForEntity(Position.class, testEntity1);
        assertEquals(testEntity1Positions.size(), 1);

        assertEquals(testEntity1Positions.get(0), testEntity1pos);
    }
}
