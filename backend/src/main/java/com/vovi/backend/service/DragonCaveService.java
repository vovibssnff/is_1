package com.vovi.backend.service;

import com.vovi.backend.entity.DragonCave;
import com.vovi.backend.repository.DragonCaveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DragonCaveService {

    @Inject
    private DragonCaveRepository dragonCaveRepository;

    public List<DragonCave> getAll() {
        return dragonCaveRepository.findAll();
    }

    public DragonCave getById(Long id) {
        return dragonCaveRepository.findById(id);
    }

    @Transactional
    public DragonCave createOrUpdate(DragonCave dragonCave) {
        return dragonCaveRepository.save(dragonCave);
    }

    @Transactional
    public void delete(Long id) {
        DragonCave dragonCave = getById(id);
        if (dragonCave != null) {
            dragonCaveRepository.delete(dragonCave);
        }
    }
}

