package com.rdguez.eduardo.springbootjwtauth.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id
  String name
}
