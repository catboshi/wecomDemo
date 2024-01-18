package tech.wedev.wecom.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    @Value("${jwtExpire}")
    private int jwtExpire;

    @Value("${encryptKey}")
    private String encryptKey;

    public String generateToken(String code) {
        long currentTime = System.currentTimeMillis();
        log.info("=====generateToken===now    is " + new Date(currentTime));
        log.info("=====generateToken===expire is " + new Date(currentTime + (long) jwtExpire * 1000 * 60));

        return JWT.create()
                .withIssuer(encryptKey)
                .withIssuedAt(new Date(currentTime))// 签发时间
                .withExpiresAt(new Date(currentTime + (long) jwtExpire * 1000 * 60))// 过期时间戳
                .withClaim("code", code)//自定义参数
                .sign(Algorithm.HMAC256(encryptKey));
    }
}
