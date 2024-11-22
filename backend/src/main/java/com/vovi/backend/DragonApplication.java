package com.vovi.backend;

import jakarta.ws.rs.ApplicationPath;

import java.util.Set;

@ApplicationPath("/api")
public class DragonApplication extends jakarta.ws.rs.core.Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
                com.vovi.backend.filter.CorsFilter.class // Register your filter here explicitly if needed
        );
    }
}

