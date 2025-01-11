package com.vovi.backend.resource;

import com.vovi.backend.dto.PersonDTO;
import com.vovi.backend.entity.Person;
import com.vovi.backend.service.PersonService;
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

@Path("persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PersonResource {

    @Inject
    private PersonService personService;

    @Inject
    private UserService userService;

    @GET
    public Response getAll() {
        List<Person> persons = personService.getAll();
        List<PersonDTO> dtos = new ArrayList<>();
        for (Person person : persons) {
            PersonDTO dto = new PersonDTO(
                    person.getId(),
                    person.getCreatedBy().getId(),
                    person.getUpdatedTime(),
                    person.getName(),
                    person.getEyeColor(),
                    person.getHairColor(),
                    person.getLocation().getX(),
                    person.getLocation().getY(),
                    person.getPassportId(),
                    person.getNationality()
            );
            dtos.add(dto);
        }
        return Response.ok(dtos).build();
    }

    @POST
    @Transactional
    public Response create(@Context HttpServletRequest request, @Valid PersonDTO dto) {
        try {
            if (request.getSession().getAttribute("user") == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (dto == null || dto.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID must be null for creating a new Person").build();
            }
            Long savedPersonId = personService.createOrUpdate(
                    userService.getUserByRequest(request),
                    null,
                    dto.getName(),
                    dto.getEyeColor(),
                    dto.getHairColor(),
                    dto.getX(),
                    dto.getY(),
                    dto.getPassportId(),
                    dto.getNationality()
            );
            return Response.status(Response.Status.CREATED).entity(savedPersonId).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Context HttpServletRequest request, @Valid PersonDTO dto) {
        if (!id.equals(dto.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and DTO ID must match for updating").build();
        }
        if (!personService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Long updatedPersonId = personService.createOrUpdate(
                userService.getUserByRequest(request),
                id,
                dto.getName(),
                dto.getEyeColor(),
                dto.getHairColor(),
                dto.getX(),
                dto.getY(),
                dto.getPassportId(),
                dto.getNationality()
        );
        return Response.ok(updatedPersonId).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id, @Context HttpServletRequest request) {
        if (!personService.getById(id).isEditableByUser(userService.getUserByRequest(request))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        personService.delete(id);
        return Response.noContent().build();
    }
}
