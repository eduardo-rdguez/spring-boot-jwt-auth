package com.rdguez.eduardo.springbootjwtauth

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import com.rdguez.eduardo.springbootjwtauth.domain.User
import com.rdguez.eduardo.springbootjwtauth.service.RoleService
import com.rdguez.eduardo.springbootjwtauth.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringBootJwtAuthApplication {

  static void main(String[] args) {
    SpringApplication.run(SpringBootJwtAuthApplication, args)
  }

  @Bean
  CommandLineRunner run(UserService userService, RoleService roleService) {
    (args) -> {
      roleService.saveRole(new Role(name: "ROLE_USER"))
      roleService.saveRole(new Role(name: "ROLE_MANAGER"))
      roleService.saveRole(new Role(name: "ROLE_ADMIN"))
      roleService.saveRole(new Role(name: "ROLE_SUPER_ADMIN"))

      userService.saveUser(new User(username: "rick", password: "1234"))
      userService.saveUser(new User(username: "morty", password: "1234"))

      userService.assignRoleToUser("ROLE_USER", "rick",)
      userService.assignRoleToUser("ROLE_MANAGER", "morty")
    }
  }
}
