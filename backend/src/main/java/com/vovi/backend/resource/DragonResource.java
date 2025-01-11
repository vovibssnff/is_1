package com.vovi.backend.resource;

import com.vovi.backend.dto.DragonDTO;
import com.vovi.backend.entity.Dragon;
import com.vovi.backend.service.DragonService;
import com.vovi.backend.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("dragons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonResource {

    @Inject
    private DragonService dragonService;

    @Inject
    private UserService userService;

    @GET
    public Response getAll() {
        List<Dragon> dragons = dragonService.getAll();
        List<DragonDTO> dtos = new ArrayList<>();
        for (Dragon dragon : dragons) {
            dtos.add(dragonService.toDto(dragon));
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        Dragon dragon = dragonService.getById(id);
        if (dragon == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Dragon not found").build();
        }
        return Response.ok(dragonService.toDto(dragon)).build();
    }

    @POST
    @Transactional
    public Response create(@Context HttpServletRequest request, @Valid DragonDTO dto) {
        try {
            if (request.getSession().getAttribute("user") == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (dto == null || dto.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID must be null for creating a new Dragon").build();
            }
            Long savedDragonID = dragonService.createOrUpdate(userService.getUserByRequest(request), dto);
            return Response.status(Response.Status.CREATED).entity(savedDragonID).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Context HttpServletRequest request, @Valid DragonDTO dto) {
        if (!id.equals(dto.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and DTO ID must match for updating").build();
        }
        if (request.getSession().getAttribute("user") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            Long updatedDragonID = dragonService.createOrUpdate(userService.getUserByRequest(request), dto);
            return Response.ok(updatedDragonID).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id, @Context HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Dragon dragon = dragonService.getById(id);
        if (dragon == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Dragon not found").build();
        }
        dragonService.delete(id);
        return Response.noContent().build();
    }
}
