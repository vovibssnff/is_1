package com.vovi.backend.repository;

import com.vovi.backend.entity.AdminRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public class AdminRequestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AdminRequest> getAll() {
        return entityManager.createQuery("SELECT ar FROM AdminRequest ar", AdminRequest.class).getResultList();
    }

    @Transactional
    public AdminRequest create(AdminRequest adminRequest) {
        entityManager.persist(adminRequest);
        return adminRequest;
    }

    @Transactional
    public void delete(AdminRequest adminRequest) {
        if (!entityManager.contains(adminRequest)) {
            adminRequest = entityManager.merge(adminRequest);
        }
        entityManager.remove(adminRequest);
    }
}
