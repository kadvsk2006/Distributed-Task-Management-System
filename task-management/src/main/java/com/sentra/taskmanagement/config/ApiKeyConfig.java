package com.sentra.taskmanagement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyConfig extends OncePerRequestFilter {

    private static final String API_KEY = "Mahesh@2003";
    private static final String HEADER_NAME = "X-API-KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Exclude spring boot default paths
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(HEADER_NAME);

        if (apiKey == null || !API_KEY.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid API Key\"}");
            return;
        }
        System.out.println("API KEY RECEIVED = " + apiKey);
        // Continue filter chain if API key is valid
        filterChain.doFilter(request, response);
    }
}
