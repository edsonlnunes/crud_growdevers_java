package br.com.growdev.growdevers.filters;

import br.com.growdev.growdevers.repositories.GrowdeverRepository;
import br.com.growdev.growdevers.services.TokenService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private GrowdeverRepository growdeverRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");

        if(authorization != null){
            var splitAuthorization = authorization.split(" ");
            if(splitAuthorization.length != 2){
                throw new RuntimeException("Token inválido");
            }
            var token = splitAuthorization[1];

            try {
                var subject = tokenService.verifyToken(token);
                var growdever = growdeverRepository.findByEmail(subject);
                var userLogged = new UsernamePasswordAuthenticationToken(growdever,null, growdever.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(userLogged);
            } catch (TokenExpiredException ex){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                response.setContentType("application/json");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
