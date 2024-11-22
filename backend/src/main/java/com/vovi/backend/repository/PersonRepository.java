package com.vovi.backend.repository;

import com.vovi.backend.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class PersonRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public List<Person> findAll() {
        TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p", Person.class);
        return query.getResultList();
    }

    public Person findById(Long id) {
        return entityManager.find(Person.class, id);
    }

    public Person save(Person person) {
        if (person.getId() == null) {
            entityManager.persist(person);
        } else {
            person = entityManager.merge(person);
        }
        return person;
    }

    public void delete(Person person) {
        if (!entityManager.contains(person)) {
            person = entityManager.merge(person);
        }
        entityManager.remove(person);
    }
}
