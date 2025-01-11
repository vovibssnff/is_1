package com.vovi.backend.resource;

import com.vovi.backend.dto.AdminRequestDTO;
import com.vovi.backend.dto.UserRegistrationDTO;
import com.vovi.backend.entity.User;
import com.vovi.backend.service.AdminRequestService;
import com.vovi.backend.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.restlet.resource.Get;

import java.util.Optional;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthResource {

    @Inject
    private UserService userService;

    @Inject
    AdminRequestService adminRequestService;

    @POST
    @Path("register")
    @Transactional
    public Response register(@Context HttpServletRequest request, @Valid UserRegistrationDTO dto) {
        try {
            // Register user using the UserService
            User registeredUser = userService.register(dto.getUsername(), dto.getPassword());

            // Start a new session for the authenticated user
            HttpSession session = request.getSession(true); // Create a new session if not exists
            session.setAttribute("user", registeredUser);  // Store the user in the session

            return Response.status(Response.Status.CREATED).entity(registeredUser.getId()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("login")
    public Response login(User user, @Context HttpServletRequest request) {
        Optional<User> authenticatedUser = userService.login(user.getUsername(), user.getPassword());

        if (authenticatedUser.isPresent()) {
            // Start a new session for the authenticated user
            HttpSession session = request.getSession(true); // Create a new session if not exists
            session.setAttribute("user", authenticatedUser.get()); // Store the user in the session
            return Response.ok(authenticatedUser.get()).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
    }

    @POST
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Do not create a new session if it doesn't exist
        if (session != null) {
            session.invalidate(); // Invalidate the session to logout the user
        }
        return Response.ok("Logged out successfully").build();
    }

    @GET
    @Path("session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Do not create a new session if it doesn't exist

        if (session == null || session.getAttribute("user") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Object user = session.getAttribute("user");
        return Response.ok(user).build();
    }

    @POST
    @Path("admin-request")
    @Transactional
    public Response requestAdmin(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User requester = (User) session.getAttribute("user");
        Long requestId = adminRequestService.createAdminRequest(requester);
        return Response.status(Response.Status.CREATED).entity(new AdminRequestDTO(requestId, requester.getId())).build();
    }

    @GET
    @Path("admin-request")
    public Response getAdminRequests() {
        return Response.ok(
                adminRequestService.getAll().stream()
                        .map(ar -> new AdminRequestDTO(ar.getId(), ar.getRequester().getId()))
                        .toList()
        ).build();
    }

    @POST
    @Path("admin-request/accept/{id}")
    @Transactional
    public Response acceptAdminRequest(@PathParam("id") Long id) {
        try {
            adminRequestService.acceptAdminRequest(id);
            return Response.ok("Admin request accepted, user role updated").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("admin-request/decline/{id}")
    @Transactional
    public Response declineAdminRequest(@PathParam("id") Long id) {
        try {
            adminRequestService.declineAdminRequest(id);
            return Response.ok("Admin request declined").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
