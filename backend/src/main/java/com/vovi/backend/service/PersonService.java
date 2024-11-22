package com.vovi.backend.service;

import com.vovi.backend.entity.Person;
import com.vovi.backend.repository.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PersonService {

    @Inject
    private PersonRepository personRepository;

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getById(Long id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Person createOrUpdate(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        Person person = getById(id);
        if (person != null) {
            personRepository.delete(person);
        }
    }
}
