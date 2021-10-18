package com.rdguez.eduardo.springbootjwtauth.service.impl

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import com.rdguez.eduardo.springbootjwtauth.domain.User
import com.rdguez.eduardo.springbootjwtauth.repository.UserRepository
import com.rdguez.eduardo.springbootjwtauth.service.RoleService
import com.rdguez.eduardo.springbootjwtauth.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User as UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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

  BCryptPasswordEncoder bCryptPasswordEncoder

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
    if (user) {
      log.error("User {} found in the database", username)
    } else {
      throw new UsernameNotFoundException("User ${username} not found in the database")
    }
    Collection<SimpleGrantedAuthority> authorities = user.roles.collect { role ->
      new SimpleGrantedAuthority(role.name)
    }
    return new UserDetails(user.username, user.password, authorities)
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  User saveUser(User user) {
    log.info("Saving new user to the database")
    user.with {
      it.password = bCryptPasswordEncoder.encode(password)
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
    User user = userRepository.findByUsername(username)
    Role role = roleService.findRole(roleName)
    user.roles.add(role)
  }
}
