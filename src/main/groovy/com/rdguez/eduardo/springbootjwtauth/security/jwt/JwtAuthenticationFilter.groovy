package com.rdguez.eduardo.springbootjwtauth.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.stream.Collectors

@Slf4j
class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager

  JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager
  }

  @Override
  Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String username = request.getParameter("username")
    String password = request.getParameter("password")
    log.info("Username is {} and password is {}", username, password)
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password)
    authenticationManager.authenticate(authenticationToken)
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    User user = (User) authentication.getPrincipal()
    String accessToken = generateAccessToken(user)
    String refreshToken = generateRefreshToken(user)
    Map tokens = [access_token: accessToken, refresh_token: refreshToken]
    response.setContentType(MediaType.APPLICATION_JSON_VALUE)
    new ObjectMapper().writeValue(response.getOutputStream(), tokens)
  }

  static String generateAccessToken(User user) {
    JWT.create()
      .withSubject(user.username)
      .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
      .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
      .sign(Algorithm.HMAC256("secret".getBytes()))
  }

  static String generateRefreshToken(User user) {
    JWT.create()
      .withSubject(user.username)
      .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
      .sign(Algorithm.HMAC256("secret".getBytes()))
  }
}
