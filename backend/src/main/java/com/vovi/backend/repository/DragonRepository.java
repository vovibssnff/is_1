package com.vovi.backend.repository;

import com.vovi.backend.entity.Dragon;
import com.vovi.backend.entity.DragonCharacter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DragonRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Dragon> findAll() {
        TypedQuery<Dragon> query = entityManager.createQuery("SELECT d FROM Dragon d", Dragon.class);
        return query.getResultList();
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
        entityManager.remove(dragon);
    }

    public Map<DragonCharacter, Long> countGroupedByCharacter() {
        List<Object[]> results = entityManager.createQuery(
                        "SELECT d.character, COUNT(d) FROM Dragon d GROUP BY d.character", Object[].class)
                .getResultList();

        Map<DragonCharacter, Long> counts = new EnumMap<>(DragonCharacter.class);
        for (Object[] result : results) {
            counts.put((DragonCharacter) result[0], (Long) result[1]);
        }
        return counts;
    }

    public long countByCharacterGreaterThan(DragonCharacter character) {
        return entityManager.createQuery(
                        "SELECT COUNT(d) FROM Dragon d WHERE d.character > :character", Long.class)
                .setParameter("character", character)
                .getSingleResult();
    }

    public java.util.Optional<Dragon> findById(Long id) {
        return java.util.Optional.ofNullable(entityManager.find(Dragon.class, id));
    }
}
