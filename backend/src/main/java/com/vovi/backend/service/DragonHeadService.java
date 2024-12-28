package com.vovi.backend.service;

import com.vovi.backend.entity.DragonHead;
import com.vovi.backend.entity.User;
import com.vovi.backend.repository.DragonHeadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    public DragonHead createOrUpdate(User usr, Long id, Double eyesCount, Double toothCount) {
        DragonHead dragonHead;
        if (id != null) {
            dragonHead = getById(id);
            if (dragonHead == null) {
                throw new IllegalArgumentException("DragonHead with ID " + id + " does not exist");
            }
        } else {
            dragonHead = new DragonHead(eyesCount, toothCount);
            dragonHead.setName("default_name");
            dragonHead.setCreatedBy(usr);
            dragonHead.setCreatedTime(ZonedDateTime.now());
//            dragonHead.setChangeHistory(new ArrayList<>());
        }

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
