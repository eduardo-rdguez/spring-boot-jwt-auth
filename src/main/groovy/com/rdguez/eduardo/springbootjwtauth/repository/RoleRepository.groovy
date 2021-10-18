package com.rdguez.eduardo.springbootjwtauth.repository

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String name)
}