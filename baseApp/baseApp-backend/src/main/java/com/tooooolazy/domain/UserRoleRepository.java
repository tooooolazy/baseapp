package com.tooooolazy.domain;

import org.springframework.stereotype.Repository;

import com.tooooolazy.domain.objects.UserRole;

@Repository("userRoleRepository")
public class UserRoleRepository extends AbstractRepository<UserRole, Integer> {

}
