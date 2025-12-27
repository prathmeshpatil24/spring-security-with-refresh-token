package com.demo.repository;

import com.demo.entity.RoleEntity;
import com.demo.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

    Optional<RoleEntity> findByRoleType(RoleType roleType);
}
