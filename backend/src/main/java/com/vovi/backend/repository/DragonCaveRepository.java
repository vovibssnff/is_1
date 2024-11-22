package com.vovi.backend.repository;

import com.vovi.backend.entity.DragonCave;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class DragonCaveRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<DragonCave> findAll() {
        TypedQuery<DragonCave> query = entityManager.createQuery("SELECT dc FROM DragonCave dc", DragonCave.class);
        return query.getResultList();
    }

    public DragonCave findById(Long id) {
        return entityManager.find(DragonCave.class, id);
    }

    public DragonCave save(DragonCave dragonCave) {
        if (dragonCave.getId() == null) {
            entityManager.persist(dragonCave);
        } else {
            dragonCave = entityManager.merge(dragonCave);
        }
        return dragonCave;
    }

    public void delete(DragonCave dragonCave) {
        if (!entityManager.contains(dragonCave)) {
            dragonCave = entityManager.merge(dragonCave);
        }
        entityManager.remove(dragonCave);
    }
}
