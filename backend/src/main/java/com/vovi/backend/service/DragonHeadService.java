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
    public DragonHead createOrUpdate(DragonHead dragonHead) {
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

