package com.vovi.backend.resource;

import com.vovi.backend.entity.Person;
import com.vovi.backend.service.PersonService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PersonResource {

    @Inject
    private PersonService personService;

    @GET
    public Response getAll() {
        List<Person> persons = personService.getAll();
        return Response.ok(persons).build();
    }

    @POST
    @Transactional
    public Response createOrUpdate(Person person) {
        Person savedPerson = personService.createOrUpdate(person);
        return Response.ok(savedPerson).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        personService.delete(id);
        return Response.noContent().build();
    }
}
