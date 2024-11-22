package com.vovi.backend.resource;

import com.vovi.backend.dto.UserRegistrationDTO;
import com.vovi.backend.entity.User;
import com.vovi.backend.service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    @Transactional
    public Response register(UserRegistrationDTO registrationDTO) {
        System.out.println(registrationDTO);
        try {
            User registeredUser = userService.register(registrationDTO.getUsername(), registrationDTO.getPassword());
            return Response.status(Response.Status.CREATED).entity(registeredUser).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path("/login")
    public Response login(User user, @Context HttpServletRequest request) {
        Optional<User> authenticatedUser = userService.login(user.getUsername(), user.getPassword());
        if (authenticatedUser.isPresent()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authenticatedUser.get());
            return Response.ok(authenticatedUser.get()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok("Logged out successfully").build();
    }

    @GET
    @Path("/session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@Context HttpServletRequest request) {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(user).build();
    }
}
