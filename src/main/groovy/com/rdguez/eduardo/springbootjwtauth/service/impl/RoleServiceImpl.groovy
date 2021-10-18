package com.rdguez.eduardo.springbootjwtauth.service.impl

import com.rdguez.eduardo.springbootjwtauth.domain.Role
import com.rdguez.eduardo.springbootjwtauth.repository.RoleRepository
import com.rdguez.eduardo.springbootjwtauth.service.RoleService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class RoleServiceImpl implements RoleService {

  @Autowired
  RoleRepository roleRepository

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Role saveRole(Role role) {
    log.info("Saving new role to the database", role.name)
    roleRepository.save(role)
  }

  @Override
  @Transactional(readOnly = true)
  Role findRole(String name) {
    roleRepository.findByName(name)
  }
}
