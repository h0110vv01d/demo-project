package org.h0110w.som.core.configuration.security;

import org.h0110w.som.core.exception.ErrorResponse;
import org.h0110w.som.core.service.mapper.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessDeniedExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            response.setStatus(403);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(Mapper.getMapper().writeValueAsString(new ErrorResponse(403,
                    "access denied")));
            out.flush();
            return;
        }
    }
}
