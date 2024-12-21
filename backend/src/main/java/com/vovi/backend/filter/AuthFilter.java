package com.vovi.backend.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String path = httpRequest.getRequestURI();
//
//        // Allow access to static files and login endpoints
//        if (path.contains("/frontend") || path.endsWith("/api/auth/register")
//                || path.endsWith("/api/auth/login") || path.endsWith("/api/auth/logout")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Check if the session exists and the "user" attribute is set
//        if (httpRequest.getSession(false) == null || httpRequest.getSession().getAttribute("user") == null) {
//            // Redirect to login page
//            httpResponse.sendRedirect(httpRequest.getContextPath() + "/frontend/login");
//            return;
//        }

        // Proceed with the request
        chain.doFilter(request, response);
    }
}
