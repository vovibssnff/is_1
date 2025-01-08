package com.vovi.backend.resource;

import com.vovi.backend.dto.DragonHeadDTO;
import com.vovi.backend.entity.DragonHead;
import com.vovi.backend.service.DragonHeadService;
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

@Path("dragon-heads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class DragonHeadResource {

    @Inject
    private DragonHeadService dragonHeadService;

    @Inject
    private UserService userService;

    @GET
    public Response getAll() {
        List<DragonHead> dragonHeads = dragonHeadService.getAll();
        List<DragonHeadDTO> dtos = new ArrayList<>();
        DragonHeadDTO dto;

        for (DragonHead dragonHead : dragonHeads) {
            dto = new DragonHeadDTO(dragonHead.getId(), dragonHead.getCreatedBy().getId(),
                    dragonHead.getUpdatedTime(), dragonHead.getEyesCount(), dragonHead.getToothCount());
            dtos.add(dto);
        }
        return Response.ok(dtos).build();
    }

    @POST
    @Transactional
    public Response create(@Context HttpServletRequest request, @Valid DragonHeadDTO dto) {
        try {
            if (request.getSession().getAttribute("user") == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (dto == null || dto.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID must be null for creating a new Dragon Head").build();
            }
            Long savedDragonHeadID = dragonHeadService.createOrUpdate(userService.getUserByRequest(request), null, dto.getEyesCount(), dto.getToothCount());
            return Response.status(Response.Status.CREATED).entity(savedDragonHeadID).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Context HttpServletRequest request, @Valid DragonHeadDTO dto) {
        if (!id.equals(dto.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and DTO ID must match for updating").build();
        }
        if (!dragonHeadService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Long updatedDragonHeadID = dragonHeadService.createOrUpdate(userService.getUserByRequest(request), id, dto.getEyesCount(), dto.getToothCount());
        return Response.ok(updatedDragonHeadID).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id, @Context HttpServletRequest request) {
        if (!dragonHeadService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        dragonHeadService.delete(id);
        return Response.noContent().build();
    }
}
