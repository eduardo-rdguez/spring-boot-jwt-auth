package com.rdguez.eduardo.springbootjwtauth.security

import com.rdguez.eduardo.springbootjwtauth.security.jwt.JwtAuthenticationFilter
import com.rdguez.eduardo.springbootjwtauth.security.jwt.JwtAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager())
    jwtAuthenticationFilter.setFilterProcessesUrl("/api/login")

    http.csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers("/api/login/**").permitAll()
      .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("ROLE_USER")
      .antMatchers(HttpMethod.POST, "/api/users/**").hasAnyAuthority("ROLE_ADMIN")
      .anyRequest().authenticated()
      .and()
      .addFilter(jwtAuthenticationFilter)
      .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter)
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    super.authenticationManager()
  }

  @Bean
  static PasswordEncoder passwordEncoder() {
    new BCryptPasswordEncoder()
  }
}
