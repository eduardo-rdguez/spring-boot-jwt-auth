package com.rdguez.eduardo.springbootjwtauth.service

import com.rdguez.eduardo.springbootjwtauth.domain.Role

interface RoleService {
  Role saveRole(Role role)

  Role findRole(String name)
}