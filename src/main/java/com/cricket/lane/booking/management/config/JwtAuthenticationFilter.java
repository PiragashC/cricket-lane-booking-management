package com.cricket.lane.booking.management.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        LOGGER.info(() -> String.format("Processing request to: %s", request.getRequestURI()));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.fine("No Authorization header found or format incorrect. Skipping JWT validation.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        if (jwt.isBlank()) {
            LOGGER.warning("Empty JWT token received, skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String userEmail = jwtService.extractUsername(jwt);
            if (userEmail == null || userEmail.isBlank()) {
                LOGGER.warning("Failed to extract user email from JWT.");
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (userDetails == null) {
                    LOGGER.warning(() -> String.format("User not found: %s", userEmail));
                    filterChain.doFilter(request, response);
                    return;
                }

                boolean isTokenValid = jwtService.isTokenValid(jwt, userDetails);
                if (isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    LOGGER.info(() -> String.format("User authenticated successfully: %s", userEmail));
                } else {
                    LOGGER.warning(() -> String.format("Token validation failed for user: %s", userEmail));
                }
            }
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, "Security issue processing JWT token", e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid JWT token", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in JWT processing", e);
        }

        filterChain.doFilter(request, response);
    }
}
