package com.rdguez.eduardo.springbootjwtauth.controller

import com.rdguez.eduardo.springbootjwtauth.domain.User
import com.rdguez.eduardo.springbootjwtauth.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}
