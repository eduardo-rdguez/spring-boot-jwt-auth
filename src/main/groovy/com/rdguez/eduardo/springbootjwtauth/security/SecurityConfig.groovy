package com.rdguez.eduardo.springbootjwtauth.security

import com.rdguez.eduardo.springbootjwtauth.security.jwt.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UserDetailsService userDetailsService

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    http.authorizeRequests().anyRequest().permitAll()
    http.addFilter(new JwtAuthenticationFilter(authenticationManager()))
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    super.authenticationManager()
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    new BCryptPasswordEncoder()
  }
}
