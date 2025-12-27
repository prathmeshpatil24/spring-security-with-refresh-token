package com.demo.security;
import com.demo.entity.UserEntity;
import com.demo.repository.UserDataRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDataRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity email = userRepo.findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found with email:- " + username)
                );
        return new CustomUserDetails(email);
    }
}
