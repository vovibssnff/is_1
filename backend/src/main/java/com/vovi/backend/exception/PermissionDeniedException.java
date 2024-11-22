package com.vovi.backend.exception;

import jakarta.ws.rs.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
