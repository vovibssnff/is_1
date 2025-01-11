package com.vovi.backend.repository;

import com.vovi.backend.entity.Dragon;
import com.vovi.backend.entity.DragonCharacter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class DragonRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Dragon> findAll() {
        TypedQuery<Dragon> query = entityManager.createQuery("SELECT d FROM Dragon d", Dragon.class);
        return query.getResultList();
    }

    public Dragon findById(Long id) {
        return entityManager.find(Dragon.class, id);
    }

    public Dragon save(Dragon dragon) {
        if (dragon.getId() == null) {
            entityManager.persist(dragon);
        } else {
            dragon = entityManager.merge(dragon);
        }
        return dragon;
    }

    public void delete(Dragon dragon) {
        if (!entityManager.contains(dragon)) {
            dragon = entityManager.merge(dragon);
        }
        entityManager.remove(dragon);
    }

    public Object[] getMinimalIdDragon() {
        return (Object[]) entityManager.createNativeQuery(
                "SELECT id, name, updatedtime " +
                        "FROM Dragon " +
                        "WHERE id = (SELECT MIN(id) FROM Dragon)"
        ).getSingleResult();
    }

    public List getDragonCharacterGrouping() {
        return entityManager.createNativeQuery(
                "SELECT character, COUNT(*) " +
                        "FROM Dragon " +
                        "GROUP BY character"
        ).getResultList();
    }


    public Long countDragonsWithCharacterGreaterThan(String inputCharacter) {
        return entityManager.createQuery(
                        "SELECT COUNT(d) " +
                                "FROM Dragon d " +
                                "WHERE d.character > :inputCharacter", Long.class)
                .setParameter("inputCharacter", DragonCharacter.valueOf(inputCharacter))
                .getSingleResult();
    }

    public void killDragonById(Long dragonId) {
        entityManager.createNativeQuery(
                        "DELETE FROM Dragon WHERE id = :id"
                ).setParameter("id", dragonId)
                .executeUpdate();
    }
}
