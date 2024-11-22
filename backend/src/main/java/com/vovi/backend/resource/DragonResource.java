package com.vovi.backend.resource;

import com.vovi.backend.entity.Dragon;
import com.vovi.backend.entity.DragonCharacter;
import com.vovi.backend.entity.User;
import com.vovi.backend.service.DragonService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Path("/api/dragons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonResource {

    @Inject
    private DragonService dragonService;

    @Context
    private HttpServletRequest request;

    private User getCurrentUser() {
        return (User) request.getSession().getAttribute("user");
    }

    @GET
    public Response getAllDragons() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<Dragon> dragons = dragonService.getAllDragons();
        return Response.ok(dragons).build();
    }

    @POST
    @Transactional
    public Response createDragon(Dragon dragon) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Dragon createdDragon = dragonService.createDragon(dragon, currentUser);
        return Response.status(Response.Status.CREATED).entity(createdDragon).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteDragon(@PathParam("id") Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        dragonService.deleteDragon(id, currentUser);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateDragon(@PathParam("id") Long id, Dragon updatedDragon) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Dragon dragon = dragonService.updateDragon(id, updatedDragon, currentUser);
        return Response.ok(dragon).build();
    }

    @GET
    @Path("/counts-by-character")
    public Response countDragonsByCharacter() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Map<DragonCharacter, Long> counts = dragonService.countDragonsByCharacter();
        return Response.ok(counts).build();
    }

    @GET
    @Path("/count-greater-than")
    public Response countDragonsWithCharacterGreaterThan(@QueryParam("character") DragonCharacter character) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        long count = dragonService.countDragonsWithCharacterGreaterThan(character);
        return Response.ok(count).build();
    }
}
