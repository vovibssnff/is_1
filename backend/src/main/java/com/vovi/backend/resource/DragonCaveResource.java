package com.vovi.backend.resource;

import com.vovi.backend.dto.DragonCaveDTO;
import com.vovi.backend.entity.DragonCave;
import com.vovi.backend.service.DragonCaveService;
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

@Path("dragon-caves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonCaveResource {

    @Inject
    private DragonCaveService dragonCaveService;

    @Inject
    private UserService userService;

    @GET
    public Response getAll() {
        List<DragonCave> dragonCaves = dragonCaveService.getAll();
        List<DragonCaveDTO> dtos = new ArrayList<>();
        DragonCaveDTO dto;

        for (DragonCave dragonCave : dragonCaves) {
            dto = new DragonCaveDTO(dragonCave.getId(), dragonCave.getCreatedBy().getId(),
                    dragonCave.getUpdatedTime(), dragonCave.getDepth(), dragonCave.getNumberOfTreasures());
            dtos.add(dto);
        }

        return Response.ok(dtos).build();
    }

    @POST
    @Transactional
    public Response create(@Context HttpServletRequest request, @Valid DragonCaveDTO dto) {
        try {
            if (request.getSession().getAttribute("user") == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Long savedDragonCaveID = dragonCaveService.createOrUpdate(userService.getUserByRequest(request), null, dto.getDepth(), dto.getNumberOfTreasures());
            return Response.status(Response.Status.CREATED).entity(savedDragonCaveID).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Context HttpServletRequest request, DragonCaveDTO dto) {
        if (!id.equals(dto.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and DTO ID must match for updating").build();
        }
        if (!dragonCaveService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Long updatedDragonCaveID = dragonCaveService.createOrUpdate(userService.getUserByRequest(request), id, dto.getDepth(), dto.getNumberOfTreasures());
        return Response.ok(updatedDragonCaveID).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id, @Context HttpServletRequest request) {
        if (!dragonCaveService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        dragonCaveService.delete(id);
        return Response.noContent().build();
    }
}
