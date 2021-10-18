package com.rdguez.eduardo.springbootjwtauth.controller

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import com.rdguez.eduardo.springbootjwtauth.domain.User
import com.rdguez.eduardo.springbootjwtauth.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/users")
class UserController {

  @Autowired
  UserService userService

  @GetMapping
  ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.findAllUsers()
    ResponseEntity.ok().body(users)
  }

  @PostMapping
  ResponseEntity<User> saveUser(@RequestBody User user) {
    String requestUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()
    User newUser = userService.saveUser(user)
    ResponseEntity.created(URI.create(requestUri)).body(newUser)
  }

  @PostMapping("/roles")
  ResponseEntity<Role> saveRole( @RequestParam String roleName, @RequestParam String userName) {
    String requestUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()
    Role newRole = userService.assignRoleToUser(roleName, userName)
    ResponseEntity.created(URI.create(requestUri)).body(newRole)
  }
}
