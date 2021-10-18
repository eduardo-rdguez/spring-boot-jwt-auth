package com.rdguez.eduardo.springbootjwtauth.service

import com.rdguez.eduardo.springbootjwtauth.domain.User

interface UserService {
  User saveUser(User user)

  User findUser(String username)

  List<User> findAllUsers()

  void assignRoleToUser(String roleName, String username)
}