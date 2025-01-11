package com.vovi.backend.service;

import com.vovi.backend.dto.DragonDTO;
import com.vovi.backend.entity.*;
import com.vovi.backend.repository.DragonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@ApplicationScoped
public class DragonService {

    @Inject
    private DragonRepository dragonRepository;

    @Inject
    private PersonService personService;
    @Inject
    private DragonCaveService dragonCaveService;
    @Inject
    private DragonHeadService dragonHeadService;

    public List<Dragon> getAll() {
        return dragonRepository.findAll();
    }

    public Dragon getById(Long id) {
        return dragonRepository.findById(id);
    }

    @Transactional
    public Long createOrUpdate(User usr, DragonDTO dragonDTO) {
        Dragon dragon;

        if (dragonDTO.getId() != null) {
            // Retrieve the existing Dragon by id
            dragon = getById(dragonDTO.getId());
            if (dragon == null) {
                throw new IllegalArgumentException("Dragon with ID " + dragonDTO.getId() + " does not exist");
            }

            // Update fields
            updateDragonFields(dragon, dragonDTO, usr);
        } else {
            // Create a new Dragon
            dragon = new Dragon();
            updateDragonFields(dragon, dragonDTO, usr);
            dragon.setCreatedBy(usr);
            dragon.setCreatedTime(ZonedDateTime.now());
        }

        // Save and return the dragon's ID
        return dragonRepository.save(dragon).getId();
    }

    @Transactional
    public void delete(Long id) {
        Dragon dragon = getById(id);
        if (dragon != null) {
            dragonRepository.delete(dragon);
        }
    }

    public DragonDTO getMinimalIdDragon() {
        Object[] result = dragonRepository.getMinimalIdDragon();
        DragonDTO dto = new DragonDTO();
        dto.setId(((Number) result[0]).longValue());
        dto.setName((String) result[1]);
        dto.setUpdatedTime(((java.util.Date) result[2]).toInstant().atZone(java.time.ZoneId.systemDefault()));
        return dto;
    }

    public List getDragonCharacterGrouping() {
        return dragonRepository.getDragonCharacterGrouping();
    }

    public Long countDragonsWithCharacterGreaterThan(String inputCharacter) {
        return dragonRepository.countDragonsWithCharacterGreaterThan(inputCharacter);
    }

    @Transactional
    public void killDragonById(Long dragonId) {
        dragonRepository.delete(dragonRepository.findById(dragonId));
    }

    private void updateDragonFields(Dragon dragon, DragonDTO dragonDTO, User usr) {
        if (dragonDTO.getName() != null) {
            dragon.setName(dragonDTO.getName());
        }
        if (dragonDTO.getX() != null) {
            dragon.getCoordinates().setX(dragonDTO.getX());
        }
        if (dragonDTO.getY() != null) {
            dragon.getCoordinates().setY(dragonDTO.getY());
        }
        if (dragonDTO.getCaveId() != null) {
            dragon.setCave(dragonCaveService.getById(dragonDTO.getCaveId()));
        }
        if (dragonDTO.getPersonId() != null) {
            dragon.setKiller(personService.getById(dragonDTO.getPersonId()));
        }
        if (dragonDTO.getAge() != null) {
            dragon.setAge(dragonDTO.getAge());
        }
        if (dragonDTO.getColor() != null) {
            dragon.setColor(dragonDTO.getColor());
        }
        if (dragonDTO.getType() != null) {
            dragon.setType(dragonDTO.getType());
        }
        if (dragonDTO.getCharacter() != null) {
            dragon.setCharacter(dragonDTO.getCharacter());
        }
        if (dragonDTO.getHeadId() != null) {
            dragon.setHead(dragonHeadService.getById(dragonDTO.getHeadId()));
        }

        dragon.setUpdateFields(usr, ZonedDateTime.now());
    }

    public DragonDTO toDto(Dragon dragon) {
        return new DragonDTO(
                dragon.getId(),
                dragon.getCreatedBy() != null ? dragon.getCreatedBy().getId() : null,
                dragon.getUpdatedTime(),
                dragon.getName(),
                dragon.getCoordinates().getX(),
                dragon.getCoordinates().getY(),
                dragon.getCave().getId(),
                dragon.getKiller().getId(),
                dragon.getAge(),
                dragon.getColor(),
                dragon.getType(),
                dragon.getCharacter(),
                dragon.getHead().getId()
        );
    }
}
