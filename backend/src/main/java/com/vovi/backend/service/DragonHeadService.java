package com.vovi.backend.service;

import com.vovi.backend.entity.DragonHead;
import com.vovi.backend.repository.DragonHeadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DragonHeadService {

    @Inject
    private DragonHeadRepository dragonHeadRepository;

    public List<DragonHead> getAll() {
        return dragonHeadRepository.findAll();
    }

    public DragonHead getById(Long id) {
        return dragonHeadRepository.findById(id);
    }

    @Transactional
    public DragonHead createOrUpdate(Long id, Double eyesCount, Double toothCount) {
        DragonHead dragonHead;
        if (id != null) {
            dragonHead = getById(id); // Fetch existing entity
            if (dragonHead == null) {
                throw new IllegalArgumentException("DragonHead with ID " + id + " does not exist");
            }
        } else {
            dragonHead = new DragonHead(); // Create new entity
        }

        dragonHead.setEyesCount(eyesCount);
        dragonHead.setToothCount(toothCount);

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
