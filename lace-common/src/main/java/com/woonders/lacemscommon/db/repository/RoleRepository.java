package com.woonders.lacemscommon.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.woonders.lacemscommon.db.entity.Role;

/**
 * Created by Emanuele on 14/11/2016.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleName(Role.RoleName roleName);
}