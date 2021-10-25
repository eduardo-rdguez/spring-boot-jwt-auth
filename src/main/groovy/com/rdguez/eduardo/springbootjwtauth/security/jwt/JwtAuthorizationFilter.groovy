package com.rdguez.eduardo.springbootjwtauth.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final String BEARER_PREFIX = "Bearer "

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
    if (validateJwtToken(request)) {
      try {
        DecodedJWT verifiedToken = verifyJwtToken(request)

        String username = verifiedToken.getSubject()
        Collection<SimpleGrantedAuthority> authorities = grantedAuthorities(verifiedToken)

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities)
        SecurityContextHolder.getContext().setAuthentication(authenticationToken)

        filterChain.doFilter(request, response)
      } catch (Exception exception) {
        printErrorMessage(response, exception)
      }
    } else {
      filterChain.doFilter(request, response)
    }
  }

  static void printErrorMessage(HttpServletResponse response, Exception exception) {
    log.error("Error: {}", exception.getMessage())
    response.setHeader("error", exception.getMessage())
    response.setStatus(response.SC_FORBIDDEN)
    Map error = [error_message: exception.getMessage()]
    response.setContentType(MediaType.APPLICATION_JSON_VALUE)
    new ObjectMapper().writeValue(response.getOutputStream(), error)
  }

  static Collection<SimpleGrantedAuthority> grantedAuthorities(DecodedJWT verifiedToken) {
    List<String> roles = verifiedToken.getClaim("roles").asArray(String)
    roles.collect { role ->
      new SimpleGrantedAuthority(role)
    }
  }

  DecodedJWT verifyJwtToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(BEARER_PREFIX.length())
    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes())
    JWTVerifier verifier = JWT.require(algorithm).build()
    verifier.verify(token)
  }

  Boolean validateJwtToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
    authorizationHeader && authorizationHeader.startsWith(BEARER_PREFIX)
  }
}
