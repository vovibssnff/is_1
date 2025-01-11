package com.vovi.backend.service;

import com.vovi.backend.entity.AdminRequest;
import com.vovi.backend.entity.User;
import com.vovi.backend.entity.UserRole;
import com.vovi.backend.repository.AdminRequestRepository;
import com.vovi.backend.repository.UserRepository; // Assuming you have this repository
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AdminRequestService {

    @Inject
    private AdminRequestRepository adminRequestRepository;

    @Inject
    private UserRepository userRepository;

    public List<AdminRequest> getAll() {
        return adminRequestRepository.getAll();
    }

    @Transactional
    public Long createAdminRequest(User user) {
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setRequester(user);
        AdminRequest createdRequest = adminRequestRepository.create(adminRequest);
        return createdRequest.getId();
    }

    @Transactional
    public void acceptAdminRequest(Long requesterId) {
        User user = userRepository.findById(requesterId);
        if (user == null) {
            throw new IllegalArgumentException("User not found for ID: " + requesterId);
        }

        AdminRequest adminRequest = findAdminRequestByUserId(requesterId);
        if (adminRequest == null) {
            throw new IllegalArgumentException("AdminRequest not found for user ID: " + requesterId);
        }

        user.setRole(UserRole.ADMIN);
        userRepository.update(user);

        adminRequestRepository.delete(adminRequest);
    }

    @Transactional
    public void declineAdminRequest(Long requesterId) {
        AdminRequest adminRequest = findAdminRequestByUserId(requesterId);
        if (adminRequest == null) {
            throw new IllegalArgumentException("AdminRequest not found for user ID: " + requesterId);
        }

        adminRequestRepository.delete(adminRequest);
    }

    private AdminRequest findAdminRequestByUserId(Long userId) {
        return adminRequestRepository.getAll().stream()
                .filter(request -> request.getRequester().getId() == userId)
                .findFirst()
                .orElse(null);
    }
}
