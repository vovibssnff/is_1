package com.vovi.backend.service;

import com.vovi.backend.entity.DragonHead;
import com.vovi.backend.entity.User;
import com.vovi.backend.repository.DragonHeadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class DragonHeadService {

    @Inject
    private DragonHeadRepository dragonHeadRepository;

    @Inject
    private UserService userService;

    public List<DragonHead> getAll() {
        return dragonHeadRepository.findAll();
    }

    public DragonHead getById(Long id) {
        return dragonHeadRepository.findById(id);
    }

    @Transactional
    public Long createOrUpdate(User usr, Long id, Double eyesCount, Double toothCount) {
        DragonHead dragonHead;
        if (id != null) {
            // Retrieve the existing DragonHead by id
            dragonHead = getById(id);
            if (dragonHead == null) {
                throw new IllegalArgumentException("DragonHead with ID " + id + " does not exist");
            }

            if (eyesCount != null) {
                dragonHead.setEyesCount(eyesCount);
            }
            if (toothCount != null) {
                dragonHead.setToothCount(toothCount);
            }
            dragonHead.setUpdateFields(usr, ZonedDateTime.now());
        } else {
            // Create a new DragonHead instance if no ID is provided
            dragonHead = new DragonHead(eyesCount, toothCount);
            dragonHead.setName("default_name");
            dragonHead.setCreatedBy(usr);
            dragonHead.setCreatedTime(ZonedDateTime.now());
            // dragonHead.setChangeHistory(new ArrayList<>());
        }
        // Save the DragonHead (new or updated) and return it
        return dragonHeadRepository.save(dragonHead);
    }

    @Transactional
    public void delete(Long id) {
        DragonHead dragonHead = getById(id);
        if (dragonHead != null) {
            dragonHeadRepository.delete(dragonHead);
        }
    }
}
