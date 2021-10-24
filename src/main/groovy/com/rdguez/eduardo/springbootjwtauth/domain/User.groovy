package com.rdguez.eduardo.springbootjwtauth.domain

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  String username
  String password
  @ManyToMany(fetch = FetchType.EAGER)
  List<Role> roles
}
