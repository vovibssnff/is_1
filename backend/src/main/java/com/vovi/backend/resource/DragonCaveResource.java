package com.vovi.backend.resource;

import com.vovi.backend.entity.DragonCave;
import com.vovi.backend.service.DragonCaveService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/dragon-caves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonCaveResource {

    @Inject
    private DragonCaveService dragonCaveService;

    @GET
    public Response getAll() {
        List<DragonCave> dragonCaves = dragonCaveService.getAll();
        return Response.ok(dragonCaves).build();
    }

    @POST
    @Transactional
    public Response createOrUpdate(DragonCave dragonCave) {
        DragonCave savedDragonCave = dragonCaveService.createOrUpdate(dragonCave);
        return Response.ok(savedDragonCave).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        dragonCaveService.delete(id);
        return Response.noContent().build();
    }
}
