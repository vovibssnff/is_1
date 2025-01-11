package com.vovi.backend.resource;

import com.vovi.backend.dto.DragonDTO;
import com.vovi.backend.service.DragonService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("special-dragons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SpecialDragonResource {

    @Inject
    private DragonService dragonService;

    @GET
    @Path("minimal")
    public Response getMinimalIdDragon() {
        DragonDTO minimalDragon = dragonService.getMinimalIdDragon();
        if (minimalDragon == null) {
            return Response.status(Response.Status.OK).entity("No dragons found").build();
        }
        return Response.ok(minimalDragon).build();
    }

    @GET
    @Path("character-grouping")
    public Response getDragonCharacterGrouping() {
        List grouping = dragonService.getDragonCharacterGrouping();
        if (grouping.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No grouping data available").build();
        }
        return Response.ok(grouping).build();
    }

    @GET
    @Path("count-with-character-greater/{character}")
    public Response countDragonsWithCharacterGreaterThan(@PathParam("character") String character) {
        try {
            Long count = dragonService.countDragonsWithCharacterGreaterThan(character);
            return Response.ok(count).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid character: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}/kill")
    @Transactional
    public Response killDragonById(@PathParam("id") Long id) {
        try {
            dragonService.killDragonById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error killing dragon: " + e.getMessage()).build();
        }
    }
}
