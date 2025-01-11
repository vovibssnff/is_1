package com.vovi.backend.service;

import com.vovi.backend.dto.PersonDTO;
import com.vovi.backend.entity.Color;
import com.vovi.backend.entity.Country;
import com.vovi.backend.entity.Person;
import com.vovi.backend.entity.User;
import com.vovi.backend.repository.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class PersonService {

    @Inject
    private PersonRepository personRepository;

    @Inject
    private UserService userService;

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getById(Long id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Long createOrUpdate(User usr, Long id, String name, Color eyeColor, Color hairColor, Double x, Float y,
                               String passportId, Country nationality) {
        Person person;

        if (id != null) {
            // Retrieve the existing Person by id
            person = getById(id);
            if (person == null) {
                throw new IllegalArgumentException("Person with ID " + id + " does not exist");
            }
            if (name != null) {
                person.setName(name);
            }
            // Update fields if provided
            if (eyeColor != null) {
                person.setEyeColor(eyeColor);
            }
            if (hairColor != null) {
                person.setHairColor(hairColor);
            }
            if (x != null) {
                person.getLocation().setX(x);
            }
            if (y != null) {
                person.getLocation().setY(y);
            }
            if (passportId != null && !passportId.isBlank()) {
                person.setPassportId(passportId);
            }
            if (nationality != null) {
                person.setNationality(nationality);
            }

            person.setUpdateFields(usr, ZonedDateTime.now());
        } else {
            // Create a new Person instance if no ID is provided
            person = new Person();
            person.setEyeColor(eyeColor);
            person.setHairColor(hairColor);
            person.getLocation().setX(x);
            person.getLocation().setY(y);
            person.setPassportId(passportId);
            person.setNationality(nationality);
            person.setName(name);
            person.setCreatedBy(usr);
            person.setCreatedTime(ZonedDateTime.now());
        }

        // Save the Person (new or updated) and return its ID
        return personRepository.save(person).getId();
    }

    @Transactional
    public void delete(Long id) {
        Person person = getById(id);
        if (person != null) {
            personRepository.delete(person);
        }
    }
}
