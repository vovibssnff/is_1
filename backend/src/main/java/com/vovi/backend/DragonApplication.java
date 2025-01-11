package com.vovi.backend;

import com.vovi.backend.filter.CorsFilter;
import com.vovi.backend.resource.*;
import jakarta.ws.rs.ApplicationPath;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class DragonApplication extends jakarta.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<Class<?>>();
//        set.add(CorsFilter.class);
        set.add(DragonResource.class);
        set.add(DragonHeadResource.class);
        set.add(DragonCaveResource.class);
        set.add(PersonResource.class);
        set.add(AuthResource.class);
        set.add(SpecialDragonResource.class);
        return set;
    }
}

