package com.demo.repository;

import com.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepo extends JpaRepository<UserEntity,Integer> {

    //check mail
    Optional<UserEntity> findByEmail(String email);
}
