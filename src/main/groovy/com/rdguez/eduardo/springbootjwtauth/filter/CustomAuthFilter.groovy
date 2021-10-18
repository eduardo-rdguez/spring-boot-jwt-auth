package com.rdguez.eduardo.springbootjwtauth.filter

import groovy.util.logging.Slf4j
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Slf4j
class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager

  CustomAuthFilter(AuthenticationManager authenticationManager) {
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
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult)
  }
}
