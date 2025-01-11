package com.vovi.backend.service;

import com.vovi.backend.dto.DragonCaveDTO;
import com.vovi.backend.entity.DragonCave;
import com.vovi.backend.entity.User;
import com.vovi.backend.repository.DragonCaveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class DragonCaveService {

    @Inject
    private DragonCaveRepository dragonCaveRepository;

    @Inject
    private UserService userService;

    public List<DragonCave> getAll() {
        return dragonCaveRepository.findAll();
    }

    public DragonCave getById(Long id) {
        return dragonCaveRepository.findById(id);
    }

    @Transactional
    public Long createOrUpdate(User usr, Long id, Float depth, Double numberOfTreasures) {
        DragonCave dragonCave;
        if (id != null) {
            // Retrieve the existing DragonCave by id
            dragonCave = getById(id);
            if (dragonCave == null) {
                throw new IllegalArgumentException("DragonCave with ID " + id + " does not exist");
            }

            // Update fields if provided
            if (depth != null) {
                dragonCave.setDepth(depth);
            }
            if (numberOfTreasures != null) {
                dragonCave.setNumberOfTreasures(numberOfTreasures);
            }

            dragonCave.setUpdateFields(usr, ZonedDateTime.now());
        } else {
            // Create a new DragonCave instance if no ID is provided
            dragonCave = new DragonCave();
            dragonCave.setDepth(depth);
            dragonCave.setNumberOfTreasures(numberOfTreasures);
            dragonCave.setName("default_name");
            dragonCave.setCreatedBy(usr);
            dragonCave.setCreatedTime(ZonedDateTime.now());
        }
        // Save the DragonCave (new or updated) and return its ID
        return dragonCaveRepository.save(dragonCave).getId();
    }

    @Transactional
    public void delete(Long id) {
        DragonCave dragonCave = getById(id);
        if (dragonCave != null) {
            dragonCaveRepository.delete(dragonCave);
        }
    }
}
