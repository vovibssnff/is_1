package com.vovi.backend.resource;

import com.vovi.backend.entity.DragonHead;
import com.vovi.backend.service.DragonHeadService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/dragon-heads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonHeadResource {

    @Inject
    private DragonHeadService dragonHeadService;

    @GET
    public Response getAll() {
        List<DragonHead> dragonHeads = dragonHeadService.getAll();
        return Response.ok(dragonHeads).build();
    }

    @POST
    @Transactional
    public Response createOrUpdate(DragonHead dragonHead) {
        DragonHead savedDragonHead = dragonHeadService.createOrUpdate(dragonHead);
        return Response.ok(savedDragonHead).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        dragonHeadService.delete(id);
        return Response.noContent().build();
    }
}

