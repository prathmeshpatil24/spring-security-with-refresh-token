package com.demo.admin.service;

import com.demo.admin.dto.CreateUserWithRole;
import com.demo.admin.dto.RegistrationAdminDto;
import com.demo.entity.RoleEntity;
import com.demo.entity.UserEntity;
import com.demo.enums.RoleType;
import com.demo.refresh.service.RefreshTokenService;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserDataRepo;
import com.demo.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl  implements AdminService{

    @Autowired
    private UserDataRepo userDataRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserEntity registerAdmin(RegistrationAdminDto dto) {
        if (userDataRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("user with this email is already present, please try new mail id");
        }


        RoleEntity adminRole = roleRepository
                .findByRoleType(RoleType.ROLE_ADMIN)
                .orElseThrow(() ->
                        new RuntimeException("ROLE_ADMIN not found in DB")
                );

        try {

            UserEntity user = new UserEntity();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setIsActive(true);
            user.getRoles().add(adminRole);

            return userDataRepo.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity createNewRole(CreateUserWithRole dto) {
        if (userDataRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("user with this email is already present, please try new mail id");
        }

        //convert role string to enum
        RoleType roleType = RoleType.valueOf(dto.getRoleName().toUpperCase());

        RoleEntity roleEntity = roleRepository
                .findByRoleType(roleType)
                .orElseThrow(() ->
                        new RuntimeException(String.format("Role '%s' not found in DB", roleType))
                );

        try {

            UserEntity user = new UserEntity();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setIsActive(true);
            user.getRoles().add(roleEntity);

            return userDataRepo.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
