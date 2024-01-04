package com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt;

import com.ttknpdev.understandjwtmysqlmanytomany.log.Logging;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// bean
/* The class extends the Spring framework's OncePerRequestFilter,
// which ensures that the filter is only applied once per request. */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private Logging logging;
    /* @Autowired
    // The setter (SDI) takes two dependencies: JwtTokenProvider and UserDetailsService,
    // which are injected via Spring's constructor dependency injection mechanism. */
    @Autowired
    public void jwtAuthenticationFilter(
            @Qualifier("provider") JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        logging = new Logging(this.getClass());
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get JWT token from http request
        String token = getTokenFromRequest(request);
        logging.logBack.warn("token {}",token); // token eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyYW1lc2hAZ21haWwuY29tIiwiaWF0IjoxNzA0MjY2NDA3LCJleHAiOjE3MDQ4NzEyMDd9.Bl4P3FjAlsS1aCWufRJn2n9yDgFn_M6xziymcK7h4xZ7pxYDrOGlOAJe4ye6BHPe

        // validate token
        if( StringUtils.hasText(token) && jwtTokenProvider.validateToken(token) ){
            // get username from token
            String username = jwtTokenProvider.getUsername(token);
            logging.logBack.warn("username {}",username);

            // load the user associated with token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
    // The getTokenFromRequest method parses the Authorization header and returns the token portion.
    private String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");
        logging.logBack.warn("bearerToken {}",bearerToken);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}