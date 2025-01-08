package com.vovi.backend.repository;

import com.vovi.backend.entity.DragonHead;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class DragonHeadRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<DragonHead> findAll() {
        TypedQuery<DragonHead> query = entityManager.createQuery("SELECT dh FROM DragonHead dh", DragonHead.class);
        return query.getResultList();
    }

    public DragonHead findById(Long id) {
        return entityManager.find(DragonHead.class, id);
    }

    public Long save(DragonHead dragonHead) {
        if (dragonHead.getId() == null) {
            entityManager.persist(dragonHead);
        } else {
            dragonHead = entityManager.merge(dragonHead);
        }
        return dragonHead.getId();
    }

    public void delete(DragonHead dragonHead) {
        if (!entityManager.contains(dragonHead)) {
            dragonHead = entityManager.merge(dragonHead);
        }
        entityManager.remove(dragonHead);
    }
}

