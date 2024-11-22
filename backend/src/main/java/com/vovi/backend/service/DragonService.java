package com.vovi.backend.service;

import com.vovi.backend.entity.Dragon;
import com.vovi.backend.entity.DragonCharacter;
import com.vovi.backend.entity.User;
import com.vovi.backend.exception.PermissionDeniedException;
import com.vovi.backend.repository.DragonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DragonService {

    @Inject
    private DragonRepository dragonRepository;

    public List<Dragon> getAllDragons() {
        return dragonRepository.findAll();
    }

    @Transactional
    public Dragon createDragon(Dragon dragon, User createdBy) {
        dragon.setCreatedBy(createdBy);
        return dragonRepository.save(dragon);
    }

    @Transactional
    public void deleteDragon(Long dragonId, User user) {
        Dragon dragon = dragonRepository.findById(dragonId)
                .orElseThrow(() -> new IllegalArgumentException("Dragon not found with id: " + dragonId));

        if (!dragon.isEditableByUser(user)) {
            throw new PermissionDeniedException("User does not have permission to delete this dragon.");
        }

        dragonRepository.delete(dragon);
    }

    @Transactional
    public Dragon updateDragon(Long dragonId, Dragon updatedDragon, User user) {
        Dragon dragon = dragonRepository.findById(dragonId)
                .orElseThrow(() -> new IllegalArgumentException("Dragon not found with id: " + dragonId));

        if (!dragon.isEditableByUser(user)) {
            throw new PermissionDeniedException("User does not have permission to edit this dragon.");
        }

        dragon.setName(updatedDragon.getName());
        dragon.setAge(updatedDragon.getAge());
        dragon.setCoordinates(updatedDragon.getCoordinates());
        dragon.setCave(updatedDragon.getCave());
        dragon.setCharacter(updatedDragon.getCharacter());
        dragon.setColor(updatedDragon.getColor());
        dragon.setType(updatedDragon.getType());
        dragon.setKiller(updatedDragon.getKiller());
        dragon.setHead(updatedDragon.getHead());

        return dragonRepository.save(dragon);
    }

    public Map<DragonCharacter, Long> countDragonsByCharacter() {
        return dragonRepository.countGroupedByCharacter();
    }

    public long countDragonsWithCharacterGreaterThan(DragonCharacter character) {
        return dragonRepository.countByCharacterGreaterThan(character);
    }
}
