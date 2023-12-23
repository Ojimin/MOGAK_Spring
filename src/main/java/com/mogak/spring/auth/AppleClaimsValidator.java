package com.mogak.spring.auth;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
claims 검증(id token 파싱 후)
 */
@Component
public class AppleClaimsValidator {
    private static final String NONCE_KEY = "nonce";

    private final String iss;
    private final String clientId;
//    private final String nonce;

    public AppleClaimsValidator(
            @Value("${oauth.apple.iss}") String iss,
            @Value("${oauth.apple.client-id}") String clientId
//            @Value("${oauth.apple.nonce}") String nonce
    ) {
        this.iss = iss;
        this.clientId = clientId;
//        this.nonce = EncryptUtils.encrypt(nonce);
    }

    public boolean isValid(Claims claims) {
        System.out.println(claims.getIssuer().contains(iss));
        System.out.println(claims.getAudience().equals(clientId));
        System.out.println(iss + " / " + claims.getIssuer());
        System.out.println(clientId + " / " + claims.getAudience());

        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId);
//                claims.get(NONCE_KEY, String.class).equals(nonce);
    }
}
