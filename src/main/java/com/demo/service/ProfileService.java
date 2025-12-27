package com.demo.service;


import com.demo.entity.UserEntity;
import com.demo.repository.UserDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserDataRepo userDataRepo;

    public UserEntity profileInfo(Integer userId) {
        UserEntity userEntity = userDataRepo.findById(userId).get();
        return userEntity;
    }
}
