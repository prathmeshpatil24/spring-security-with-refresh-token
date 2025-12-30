package com.demo.admin.service;

import com.demo.admin.dto.CreateUserWithRole;
import com.demo.admin.dto.RegistrationAdminDto;
import com.demo.entity.UserEntity;

public interface AdminService {

    //admin registration only
    UserEntity registerAdmin(RegistrationAdminDto dto);


    //assign new role to user
    UserEntity createNewRole(CreateUserWithRole dto);
}
