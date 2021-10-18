package com.rdguez.eduardo.springbootjwtauth.repository

import com.rdguez.eduardo.springbootjwtauth.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username)
}