package com.woh.transactions.services;

import com.woh.transactions.entity.TransferUser;
import com.woh.transactions.repository.TransferUserRepository;
import com.woh.transactions.util.RedisCacheStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class JWTService {
    @Value("${transfer.expiresin}")
    private Long EXPIRES_IN;
    @Value("${transfer.rediskey}")
    private String REDIS_KEY;

    private final RedisCacheStore redisCacheStore;
    private final TransferUserRepository transferUserRepository;

    public String generateJwtToken(UUID id) throws NoSuchAlgorithmException, NoSuchProviderException {
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+EXPIRES_IN))
                .signWith(SignatureAlgorithm.HS256,(String) redisCacheStore.get(REDIS_KEY))
                .compact();
    }

    public String getUserIdFromJwt(String jwtToken){
        return Jwts.parser()
                .setSigningKey((String) redisCacheStore.get(REDIS_KEY))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject()
                .toString();
    }

    public Map<Boolean,String> isJwtValid(String jwtToken){
        try {
            Claims claims =  Jwts.parser().setSigningKey((String) redisCacheStore.get(REDIS_KEY)).build().parseClaimsJws(jwtToken).getBody();
            if(!isJwtExpired(claims)){
                Optional<TransferUser> transferUser = transferUserRepository.findById(UUID.fromString(claims.getSubject()));
                if(transferUser.isEmpty()){
                    return Map.of(false, "User not found");
                }
                if(transferUser.get().getTokenRefreshAttempts() < 3){
                    transferUser.get().setTokenRefreshAttempts(transferUser.get().getTokenRefreshAttempts() + 1);
                    transferUserRepository.save(transferUser.get());
                    generateJwtToken(transferUser.get().getId());
                    return Map.of(true, generateJwtToken(transferUser.get().getId()));
                } else {
                    transferUser.get().setTokenRefreshAttempts(0);
                    transferUserRepository.save(transferUser.get());
                    return Map.of(false, "Token refresh limit exceeded");
                }
            }
        }
        catch (Exception e) {
            return Map.of(false, "Invalid JWT token");
        }
        return Map.of(false, "JWT token is expired");
    }

    public boolean isJwtExpired(Claims claims){
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }
}
