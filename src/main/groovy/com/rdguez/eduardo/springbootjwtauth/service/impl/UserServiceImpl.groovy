package com.rdguez.eduardo.springbootjwtauth.service.impl

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import com.rdguez.eduardo.springbootjwtauth.domain.User
import com.rdguez.eduardo.springbootjwtauth.repository.UserRepository
import com.rdguez.eduardo.springbootjwtauth.service.RoleService
import com.rdguez.eduardo.springbootjwtauth.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class UserServiceImpl implements UserService, UserDetailsService {

  @Autowired
  UserRepository userRepository

  @Autowired
  RoleService roleService

  @Autowired
  PasswordEncoder passwordEncoder

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = findUser(username)
    if (user) {
      log.info("User {} found in the database", username)
    } else {
      throw new UsernameNotFoundException("User ${username} not found in the database")
    }
    Collection<SimpleGrantedAuthority> authorities = user.roles.collect { role ->
      new SimpleGrantedAuthority(role.name)
    }
    new org.springframework.security.core.userdetails.User(user.username, user.password, authorities)
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  User saveUser(User user) {
    log.info("Saving new user to the database")
    user.with {
      it.password = passwordEncoder.encode(password)
    }
    userRepository.save(user)
  }

  @Override
  @Transactional(readOnly = true)
  User findUser(String username) {
    userRepository.findByUsername(username)
  }

  @Override
  @Transactional(readOnly = true)
  List<User> findAllUsers() {
    userRepository.findAll()
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  void assignRoleToUser(String roleName, String username) {
    log.info("Adding role {} to user {}", roleName, username)
    User user = findUser(username)
    Role role = roleService.findRole(roleName)
    user.roles.add(role)
  }
}
