package com.demo.refresh.service;

import com.demo.refresh.entity.RefreshTokenEntity;
import com.demo.entity.UserEntity;
import com.demo.refresh.repository.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_DURATION_MS = 10 * 60 * 1000;
            //7 * 24 * 60 * 60 * 1000;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;


    public RefreshTokenEntity createRefreshToken(UserEntity user){

        RefreshTokenEntity rte = new RefreshTokenEntity();

        rte.setUser(user);
        rte.setToken(UUID.randomUUID().toString());

        rte.setExpiryDate(
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION_MS)
        );

        refreshTokenRepo.save(rte);

        return rte;

    }

    public RefreshTokenEntity verifyRefreshToken(String token){

        RefreshTokenEntity rte = refreshTokenRepo.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid refresh token")
                );

        if (rte.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (rte.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.delete(rte);
            throw new RuntimeException("Refresh token expired");
        }

        return rte;

    }

    //Cannot generate new access tokens
    public void revokeToken(String token) {
        refreshTokenRepo.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepo.save(rt);
        });
    }
}
